package com.techchallenge.payments.bdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.requests.CallbackPagamentoDto;
import com.techchallenge.payments.core.requests.CriarPagamentoDto;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StepDefinition {

    ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());
    private Response response;
    private PagamentoEntity pagamentoEntity;
    private CriarPagamentoDto dto;

    private final String ENDPOINT_API_PAGAMENTO = "http://localhost:8080/api/pagamentos";
    private final String ENDPOINT_API_CALLBACK = "http://localhost:8080/api/pagamentos/callback";

    @Dado("que tenho um pedido previamente cadastrado")
    public void que_tenho_um_pedido_previamente_cadastrado() {

    }

    @Quando("crio um pagamento")
    public void crio_um_pagamento() {
        dto = new CriarPagamentoDto(UUID.randomUUID().toString());
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .post(ENDPOINT_API_PAGAMENTO);
    }

    @Então("vejo que o pagamento foi criado")
    public void vejo_que_o_pagamento_foi_criado() {
        response.body().prettyPrint();
        Object a = response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body("pedido_id", equalTo(dto.pedidoId()))
                .body("status", equalTo("PENDENTE"));
    }

    @Dado("que tenho um pagamento previamente criado")
    public void que_tenho_um_pagamento_previamente_criado() throws JsonProcessingException {
        dto = new CriarPagamentoDto(UUID.randomUUID().toString());
        String response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .post(ENDPOINT_API_PAGAMENTO)
                .body().asString();

        pagamentoEntity = mapper.readValue(response, PagamentoEntity.class);
    }

    @Quando("recebo o callback de um pagamento aprovado")
    public void recebo_o_callback_de_um_pagamento_aprovado() {
        var dto = new CallbackPagamentoDto(pagamentoEntity.getExternalId());
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .post(ENDPOINT_API_CALLBACK);
    }

    @Então("vejo que o pagamento mudou de estado")
    public void vejo_que_o_pagamento_mudou_de_estado() {
        response.body().prettyPrint();
        Object a = response.then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(pagamentoEntity.getId()))
                .body("status", equalTo("APROVADO"));
    }
}
