package com.techchallenge.payments.core.usecases;

import com.techchallenge.payments.adapters.gateway.pagamento.PagamentoGateway;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.exceptions.InvalidPagamentoException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;

import static com.techchallenge.payments.helpers.PagamentoHelper.generatePagamento;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings
class PagamentoUseCaseTest {

    @Mock
    private PagamentoGateway pagamentoGateway;

    @InjectMocks
    private PagamentoUseCase pagamentoUseCase;

    @Test
    void shouldCreatePagamentoWhenThereIsNoPreviousPagamento() {
        var pedidoId = "id";
        var totalPrice = BigDecimal.valueOf(100L);
        var externalId = "externalId";

        Mockito.when(pagamentoGateway.consultarByPedidoId(pedidoId)).thenReturn(null);
        Mockito.when(pagamentoGateway.criarPagamento(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        var result = pagamentoUseCase.criar(pedidoId, totalPrice, externalId);

        assertEquals(pedidoId, result.getPedidoId());
        assertEquals(externalId, result.getExternalId());
        assertEquals(StatusPagamento.PENDENTE, result.getStatus());
        assertEquals(totalPrice, result.getValor());

        verify(pagamentoGateway, times(1)).criarPagamento(any(PagamentoEntity.class));
        verify(pagamentoGateway, times(1)).consultarByPedidoId(pedidoId);
    }

    @Test
    void shouldNotCreatePagamentoWithThereIsAlreadyOneWithStatusPendente() {
        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);

        Mockito.when(pagamentoGateway.consultarByPedidoId(savedPagamentoEntity.getPedidoId()))
                .thenReturn(savedPagamentoEntity);

        InvalidPagamentoException ex = assertThrows(
                InvalidPagamentoException.class,
                () -> pagamentoUseCase.criar(
                        savedPagamentoEntity.getPedidoId(),
                        savedPagamentoEntity.getValor(),
                        savedPagamentoEntity.getExternalId()
                )
        );
        assertEquals("pedido já possui um pagamento pendente", ex.getMessage());

        verify(pagamentoGateway, never()).criarPagamento(any(PagamentoEntity.class));
        verify(pagamentoGateway, times(1)).consultarByPedidoId(savedPagamentoEntity.getPedidoId());
    }

    @Test
    void shouldNotCreatePagamentoWithThereIsAlreadyOneWithStatusAprovado() {
        var savedPagamentoEntity = generatePagamento(StatusPagamento.APROVADO);

        Mockito.when(pagamentoGateway.consultarByPedidoId(savedPagamentoEntity.getPedidoId()))
                .thenReturn(savedPagamentoEntity);

        InvalidPagamentoException ex = assertThrows(
                InvalidPagamentoException.class,
                () -> pagamentoUseCase.criar(
                        savedPagamentoEntity.getPedidoId(),
                        savedPagamentoEntity.getValor(),
                        savedPagamentoEntity.getExternalId()
                )
        );
        assertEquals("pedido já possui um pagamento aprovado", ex.getMessage());

        verify(pagamentoGateway, never()).criarPagamento(any(PagamentoEntity.class));
        verify(pagamentoGateway, times(1)).consultarByPedidoId(savedPagamentoEntity.getPedidoId());
    }

    @Test
    void shouldReceivePagamentoCallback() {

        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);

        Mockito.when(pagamentoGateway.consultarByExternalId(savedPagamentoEntity.getExternalId()))
                .thenReturn(savedPagamentoEntity);

        when(pagamentoGateway.atualizarPagamento(any(PagamentoEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        var result = pagamentoUseCase.callbackPagamento(savedPagamentoEntity.getExternalId(), StatusPagamento.APROVADO);

        assertNotNull(result.getPagamentoConfirmadoAt());
        assertEquals(result.getUpdatedAt(), result.getPagamentoConfirmadoAt());
        assertEquals(StatusPagamento.APROVADO, result.getStatus());

        verify(pagamentoGateway, times(1)).consultarByExternalId(savedPagamentoEntity.getExternalId());
        verify(pagamentoGateway, times(1)).atualizarPagamento(result);
    }

    @Test
    void shouldNotReceivePagamentoCallbackWhenPagamentoDoesNotExists() {

        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);

        Mockito.when(pagamentoGateway.consultarByExternalId(savedPagamentoEntity.getExternalId()))
                .thenReturn(null);

        InvalidPagamentoException ex = assertThrows(
                InvalidPagamentoException.class,
                () -> pagamentoUseCase.callbackPagamento(
                        savedPagamentoEntity.getExternalId(), StatusPagamento.APROVADO
                )
        );

        assertEquals("pagamento para external id nao encontrado", ex.getMessage());

        verify(pagamentoGateway, times(1)).consultarByExternalId(savedPagamentoEntity.getExternalId());
        verify(pagamentoGateway, never()).atualizarPagamento(any());
    }

    @Test
    void shouldqueryPagamento() {
        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);
        Mockito.when(pagamentoGateway.consultarByPedidoId(savedPagamentoEntity.getPedidoId()))
                .thenReturn(savedPagamentoEntity);

        var result = pagamentoUseCase.consultarByPedidoId(savedPagamentoEntity.getPedidoId());

        assertEquals(savedPagamentoEntity, result);
        verify(pagamentoGateway, times(1)).consultarByPedidoId(savedPagamentoEntity.getPedidoId());
    }
}
