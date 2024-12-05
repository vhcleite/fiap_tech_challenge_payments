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

        assertEquals(pedidoId, result.pedidoId());
        assertEquals(externalId, result.externalId());
        assertEquals(StatusPagamento.PENDENTE, result.status());
        assertEquals(totalPrice, result.valor());

        verify(pagamentoGateway, times(1)).criarPagamento(any(PagamentoEntity.class));
        verify(pagamentoGateway, times(1)).consultarByPedidoId(pedidoId);
    }

    @Test
    void shouldNotCreatePagamentoWithThereIsAlreadyOneWithStatusPendente() {
        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);

        Mockito.when(pagamentoGateway.consultarByPedidoId(savedPagamentoEntity.pedidoId()))
                .thenReturn(savedPagamentoEntity);

        InvalidPagamentoException ex = assertThrows(
                InvalidPagamentoException.class,
                () -> pagamentoUseCase.criar(
                        savedPagamentoEntity.pedidoId(),
                        savedPagamentoEntity.valor(),
                        savedPagamentoEntity.externalId()
                )
        );
        assertEquals("pedido já possui um pagamento pendente", ex.getMessage());

        verify(pagamentoGateway, never()).criarPagamento(any(PagamentoEntity.class));
        verify(pagamentoGateway, times(1)).consultarByPedidoId(savedPagamentoEntity.pedidoId());
    }

    @Test
    void shouldNotCreatePagamentoWithThereIsAlreadyOneWithStatusAprovado() {
        var savedPagamentoEntity = generatePagamento(StatusPagamento.APROVADO);

        Mockito.when(pagamentoGateway.consultarByPedidoId(savedPagamentoEntity.pedidoId()))
                .thenReturn(savedPagamentoEntity);

        InvalidPagamentoException ex = assertThrows(
                InvalidPagamentoException.class,
                () -> pagamentoUseCase.criar(
                        savedPagamentoEntity.pedidoId(),
                        savedPagamentoEntity.valor(),
                        savedPagamentoEntity.externalId()
                )
        );
        assertEquals("pedido já possui um pagamento aprovado", ex.getMessage());

        verify(pagamentoGateway, never()).criarPagamento(any(PagamentoEntity.class));
        verify(pagamentoGateway, times(1)).consultarByPedidoId(savedPagamentoEntity.pedidoId());
    }

    @Test
    void shouldReceivePagamentoCallback() {

        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);

        Mockito.when(pagamentoGateway.consultarByExternalId(savedPagamentoEntity.externalId()))
                .thenReturn(savedPagamentoEntity);

        when(pagamentoGateway.atualizarPagamento(any(PagamentoEntity.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        var result = pagamentoUseCase.callbackPagamento(savedPagamentoEntity.externalId(), StatusPagamento.APROVADO);

        assertNotNull(result.pagamentoConfirmadoAt());
        assertEquals(result.updatedAt(), result.pagamentoConfirmadoAt());
        assertEquals(StatusPagamento.APROVADO, result.status());

        verify(pagamentoGateway, times(1)).consultarByExternalId(savedPagamentoEntity.externalId());
        verify(pagamentoGateway, times(1)).atualizarPagamento(result);
    }

    @Test
    void shouldNotReceivePagamentoCallbackWhenPagamentoDoesNotExists() {

        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);

        Mockito.when(pagamentoGateway.consultarByExternalId(savedPagamentoEntity.externalId()))
                .thenReturn(null);

        InvalidPagamentoException ex = assertThrows(
                InvalidPagamentoException.class,
                () -> pagamentoUseCase.callbackPagamento(
                        savedPagamentoEntity.externalId(), StatusPagamento.APROVADO
                )
        );

        assertEquals("pagamento para external id nao encontrado", ex.getMessage());

        verify(pagamentoGateway, times(1)).consultarByExternalId(savedPagamentoEntity.externalId());
        verify(pagamentoGateway, never()).atualizarPagamento(any());
    }

    @Test
    void shouldqueryPagamento() {
        var savedPagamentoEntity = generatePagamento(StatusPagamento.PENDENTE);
        Mockito.when(pagamentoGateway.consultarByPedidoId(savedPagamentoEntity.pedidoId()))
                .thenReturn(savedPagamentoEntity);

        var result = pagamentoUseCase.consultarByPedidoId(savedPagamentoEntity.pedidoId());

        assertEquals(savedPagamentoEntity, result);
        verify(pagamentoGateway, times(1)).consultarByPedidoId(savedPagamentoEntity.pedidoId());
    }
}
