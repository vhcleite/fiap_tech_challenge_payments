package com.techchallenge.payments.core.usecases;

import com.techchallenge.payments.adapters.gateway.pedido.PedidoGateway;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
class PedidoUseCaseTest {

    @Mock
    private PedidoGateway pedidoGateway;

    @InjectMocks
    private PedidoUseCase pedidoUseCase;

    @Test
    void testBuscarPorId_Success() {
        // Arrange
        String id = "123";
        PedidoEntity expectedPedido = new PedidoEntity(id, BigDecimal.TEN);
        when(pedidoGateway.getById(id)).thenReturn(expectedPedido);

        // Act
        PedidoEntity result = pedidoUseCase.buscarPorId(id);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPedido, result);
        verify(pedidoGateway).getById(id);
    }

    @Test
    void testAtualizarStatusCallbackPagamento_Aprovado() {
        // Arrange
        String id = "123";
        PedidoEntity pedido = new PedidoEntity(id, BigDecimal.TEN);
        when(pedidoGateway.getById(id)).thenReturn(pedido);
        PedidoEntity updatedPedido = new PedidoEntity(id, BigDecimal.TEN);
        when(pedidoGateway.atualizarStatus(id, PedidoStatus.RECEBIDO)).thenReturn(updatedPedido);

        // Act
        PedidoEntity result = pedidoUseCase.atualizarStatusCallbackPagamento(id, StatusPagamento.APROVADO);

        // Assert
        assertNotNull(result);
        verify(pedidoGateway).getById(id);
        verify(pedidoGateway).atualizarStatus(id, PedidoStatus.RECEBIDO);
    }

    @Test
    void testAtualizarStatusCallbackPagamento_NaoAprovado() {
        // Arrange
        String id = "123";
        PedidoEntity pedido = new PedidoEntity(id, BigDecimal.TEN);
        when(pedidoGateway.getById(id)).thenReturn(pedido);

        // Act
        PedidoEntity result = pedidoUseCase.atualizarStatusCallbackPagamento(id, StatusPagamento.REJEITADO);

        // Assert
        assertNotNull(result);
        verify(pedidoGateway).getById(id);
        verify(pedidoGateway, never()).atualizarStatus(anyString(), any());
    }
}
