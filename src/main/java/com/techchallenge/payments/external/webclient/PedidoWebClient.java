package com.techchallenge.payments.external.webclient;

import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import com.techchallenge.payments.pkg.dto.AtualizarPedidoStatusDto;
import com.techchallenge.payments.pkg.dto.PedidoDto;
import com.techchallenge.payments.pkg.interfaces.IPedidoWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpMethod.PATCH;

@Component
public class PedidoWebClient implements IPedidoWebClient {

    private final RestTemplate restTemplate;
    private final String host;

    public PedidoWebClient(RestTemplate restTemplate, @Value("${pedido.uri}") String host) {
        this.restTemplate = restTemplate;
        this.host = host;
    }

    @Override
    public PedidoDto getPedido(String id) {
        String url = UriComponentsBuilder
                .fromUriString(host + "/api/pedidos/{id}")
                .buildAndExpand(id)
                .toString();

        return restTemplate.getForObject(url, PedidoDto.class);
    }

    @Override
    public PedidoDto updateStatus(String pedidoId, PedidoStatus pedidoStatus) {
        AtualizarPedidoStatusDto atualizarPedidoStatusDto = new AtualizarPedidoStatusDto(pedidoStatus);
        String url = host + "/api/pedidos/" + pedidoId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<AtualizarPedidoStatusDto> requestEntity = new HttpEntity<>(atualizarPedidoStatusDto, headers);

        ResponseEntity<PedidoDto> response = restTemplate.exchange(
                url,
                PATCH,
                requestEntity,
                PedidoDto.class
        );

        return response.getBody();
    }
}
