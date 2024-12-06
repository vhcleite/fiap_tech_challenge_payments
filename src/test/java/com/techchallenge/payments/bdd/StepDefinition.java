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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StepDefinition {

    ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());
    private Response response;
    private String pedidoId;
    private PagamentoEntity pagamentoEntity;
    private CriarPagamentoDto dto;

    private final String ENDPOINT_API_PEDIDO = "http://localhost:8081/api/pedidos";
    private final String ENDPOINT_API_PAGAMENTO = "http://localhost:8080/api/pagamentos";
    private final String ENDPOINT_API_CALLBACK = "http://localhost:8080/api/pagamentos/callback";

    @Dado("que tenho um pedido previamente cadastrado")
    public void que_tenho_um_pedido_previamente_cadastrado() {
        {
            String requestBody = """
                        {
                            "client_id": "1",
                            "combos": [
                                {
                                    "lanche": {
                                        "id_produto": "1",
                                        "quantity": 1,
                                        "price": 10
                                    },
                                    "acompanhamento": {
                                        "id_produto": "2",
                                        "quantity": 2,
                                        "price": 10
                                    },
                                    "bebida": {
                                        "id_produto": "3",
                                        "quantity": 1,
                                        "price": 10
                                    },
                                    "sobremesa": {
                                        "id_produto": "4",
                                        "quantity": 1,
                                        "price": 10
                                    }
                                }
                            ]
                        }
                    """;

            // Make the POST request
            Response response = given()
                    .header("Content-Type", "application/json") // Set Content-Type header
                    .body(requestBody) // Set the body of the request
                    .when()
                    .post(ENDPOINT_API_PEDIDO) // Endpoint to hit
                    .then()
                    .statusCode(201) // Validate the response status code (change as per your API)
                    .extract()
                    .response(); // Extract the response

            // Print the response body
            pedidoId = response.jsonPath().getString("id");
        }
    }

    @Quando("crio um pagamento")
    public void crio_um_pagamento() {
        dto = new CriarPagamentoDto(pedidoId);
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
        que_tenho_um_pedido_previamente_cadastrado();
        crio_um_pagamento();

        String content = response.asString();
        pagamentoEntity = mapper.readValue(content, PagamentoEntity.class);
    }

    @Quando("recebo o callback de um pagamento aprovado")
    public void recebo_o_callback_de_um_pagamento_aprovado() {
        var dto = new CallbackPagamentoDto(pagamentoEntity.externalId());
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
                .body("id", equalTo(pagamentoEntity.id()))
                .body("status", equalTo("APROVADO"));
    }
}
