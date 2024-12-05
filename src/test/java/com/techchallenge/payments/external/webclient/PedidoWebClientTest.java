package com.techchallenge.payments.external.webclient;

import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import com.techchallenge.payments.pkg.dto.AtualizarPedidoStatusDto;
import com.techchallenge.payments.pkg.dto.PedidoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PedidoWebClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PedidoWebClient pedidoWebClient;

    private final String host = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pedidoWebClient = new PedidoWebClient(restTemplate, host);
    }

    @Test
    void getPedido_DeveRetornarPedido() {
        // Arrange
        String pedidoId = "123";
        String url = host + "/api/pedidos/" + pedidoId;

        PedidoDto expectedPedido = new PedidoDto(
                "123",
                PedidoStatus.PENDENTE.name(),
                BigDecimal.TEN
        );

        when(restTemplate.getForObject(url, PedidoDto.class)).thenReturn(expectedPedido);

        // Act
        PedidoDto result = pedidoWebClient.getPedido(pedidoId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPedido, result);
        verify(restTemplate, times(1)).getForObject(url, PedidoDto.class);
    }

    @Test
    void updateStatus_DeveAtualizarStatusEPedidoRetornado() {
        // Arrange
        String pedidoId = "123";
        PedidoStatus novoStatus = PedidoStatus.RECEBIDO;
        String url = host + "/api/pedidos/" + pedidoId;

        AtualizarPedidoStatusDto atualizarPedidoStatusDto = new AtualizarPedidoStatusDto(novoStatus);

        PedidoDto expectedPedido = new PedidoDto(
                "123",
                PedidoStatus.RECEBIDO.name(),
                BigDecimal.TEN
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<AtualizarPedidoStatusDto> requestEntity = new HttpEntity<>(atualizarPedidoStatusDto, headers);

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.PATCH),
                eq(requestEntity),
                eq(PedidoDto.class)
        )).thenReturn(ResponseEntity.ok(expectedPedido));

        // Act
        PedidoDto result = pedidoWebClient.updateStatus(pedidoId, novoStatus);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPedido, result);
        verify(restTemplate, times(1)).exchange(
                eq(url),
                eq(HttpMethod.PATCH),
                eq(requestEntity),
                eq(PedidoDto.class)
        );
    }
}
