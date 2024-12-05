package com.techchallenge.payments.adapters.gateway.pedido;

import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import com.techchallenge.payments.pkg.dto.PedidoDto;
import com.techchallenge.payments.pkg.interfaces.IPedidoWebClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
class PedidoGatewayTest {

    @Mock
    private IPedidoWebClient webClient;

    @InjectMocks
    private PedidoGateway pedidoGateway;

    @Test
    void testGetById_Success() {
        // Arrange
        String pedidoId = "pedido123";
        PedidoDto pedidoDto = new PedidoDto(pedidoId, PedidoStatus.PENDENTE.name(), BigDecimal.TEN);
        PedidoEntity expectedPedidoEntity = new PedidoEntity(pedidoId, BigDecimal.TEN);

        when(webClient.getPedido(pedidoId)).thenReturn(pedidoDto);

        // Act
        PedidoEntity result = pedidoGateway.getById(pedidoId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPedidoEntity, result);
        verify(webClient).getPedido(pedidoId);
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        String pedidoId = "pedido123";

        when(webClient.getPedido(pedidoId)).thenReturn(null);

        // Act
        PedidoEntity result = pedidoGateway.getById(pedidoId);

        // Assert
        assertNull(result);
        verify(webClient).getPedido(pedidoId);
    }

    @Test
    void testAtualizarStatus_Success() {
        // Arrange
        String pedidoId = "pedido123";
        PedidoStatus pedidoStatus = PedidoStatus.RECEBIDO;
        PedidoDto pedidoDto = new PedidoDto(pedidoId, PedidoStatus.PENDENTE.name(), BigDecimal.TEN);
        PedidoEntity expectedPedidoEntity = new PedidoEntity(pedidoId, BigDecimal.TEN);

        when(webClient.updateStatus(pedidoId, pedidoStatus)).thenReturn(pedidoDto);

        // Act
        PedidoEntity result = pedidoGateway.atualizarStatus(pedidoId, pedidoStatus);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPedidoEntity, result);
        verify(webClient).updateStatus(pedidoId, pedidoStatus);
    }

    @Test
    void testAtualizarStatus_Failure() {
        // Arrange
        String pedidoId = "pedido123";
        PedidoStatus pedidoStatus = PedidoStatus.RECEBIDO;

        when(webClient.updateStatus(pedidoId, pedidoStatus)).thenReturn(null);

        // Act
        PedidoEntity result = pedidoGateway.atualizarStatus(pedidoId, pedidoStatus);

        // Assert
        assertNull(result);
        verify(webClient).updateStatus(pedidoId, pedidoStatus);
    }
}