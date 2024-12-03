package com.techchallenge.payments.adapters.controllers;

import com.techchallenge.payments.core.entities.pagamento.ExternalPagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.requests.CriarPagamentoDto;
import com.techchallenge.payments.core.usecases.PagamentoProcessorUseCase;
import com.techchallenge.payments.core.usecases.PagamentoUseCase;
import com.techchallenge.payments.core.usecases.PedidoUseCase;
import com.techchallenge.payments.helpers.PagamentoHelper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
class PagamentoControllerTest {

    @Mock
    private PedidoUseCase pedidoUseCase;

    @Mock
    private PagamentoUseCase pagamentoUseCase;

    @Mock
    private PagamentoProcessorUseCase pagamentoProcessorUseCase;

    @InjectMocks
    private PagamentoController pagamentoController;

    @Test
    void testCriarPagamento_Success() {
        // Arrange
        String pedidoId = "pedido123";
        BigDecimal totalPrice = new BigDecimal("150.00");
        CriarPagamentoDto dto = new CriarPagamentoDto(pedidoId);

        PedidoEntity pedido = new PedidoEntity(pedidoId, totalPrice);
        ExternalPagamentoEntity externalPagamento = new ExternalPagamentoEntity("external123", totalPrice);
        PagamentoEntity expectedPagamento = PagamentoHelper.generatePagamento(StatusPagamento.PENDENTE);

        when(pedidoUseCase.buscarPorId(pedidoId)).thenReturn(pedido);
        when(pagamentoProcessorUseCase.criar(totalPrice)).thenReturn(externalPagamento);
        when(pagamentoUseCase.criar(pedidoId, totalPrice, "external123")).thenReturn(expectedPagamento);

        // Act
        PagamentoEntity result = pagamentoController.criarPagamento(dto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPagamento, result);
        verify(pedidoUseCase).buscarPorId(pedidoId);
        verify(pagamentoProcessorUseCase).criar(totalPrice);
        verify(pagamentoUseCase).criar(pedidoId, totalPrice, "external123");
    }

    @Test
    void testPagamentoStatusCallback_Success() {
        // Arrange
        String externalId = "external123";
        String pedidoId = "id";
        BigDecimal totalPrice = new BigDecimal("150.00");

        StatusPagamento statusPagamento = StatusPagamento.APROVADO;
        PagamentoEntity pagamento = PagamentoHelper.generatePagamento(statusPagamento);

        when(pagamentoProcessorUseCase.consultarStatusPagamento(externalId)).thenReturn(statusPagamento);
        when(pagamentoUseCase.callbackPagamento(externalId, statusPagamento)).thenReturn(pagamento);

        // Act
        PagamentoEntity result = pagamentoController.pagamentoStatusCallback(externalId);

        // Assert
        assertNotNull(result);
        assertEquals(StatusPagamento.APROVADO, result.getStatus());
        verify(pagamentoProcessorUseCase).consultarStatusPagamento(externalId);
        verify(pagamentoUseCase).callbackPagamento(externalId, statusPagamento);
        verify(pedidoUseCase).atualizarStatusCallbackPagamento(pedidoId, StatusPagamento.APROVADO);
    }
}
