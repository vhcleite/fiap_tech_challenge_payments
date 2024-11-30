package com.techchallenge.payments.core.usecases;

import com.techchallenge.payments.adapters.gateway.pagamento.PagamentoGateway;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@MockitoSettings
class PagamentoUseCaseTest {

    @Mock
    PagamentoGateway pagamentoGateway;

    @InjectMocks
    PagamentoUseCase pagamentoUseCase;

    @Test
    public void shouldCreatePagamentoWhenThereIsNoPreviousPagamento() {
        var pedidoId = "id";
        var totalPrice = BigDecimal.valueOf(100L);
        var externalId = "externalId";

        Mockito.when(pagamentoGateway.consultarByPedidoId(pedidoId)).thenReturn(null);
        Mockito.when(pagamentoGateway.criarPagamento(any())).thenAnswer(i -> i.getArguments()[0]);
        var result = pagamentoUseCase.criar(pedidoId, totalPrice, externalId);

        assertEquals(pedidoId, result.getPedidoId());
        assertEquals(externalId, result.getExternalId());
        assertEquals(StatusPagamento.PENDENTE, result.getStatus());
        assertEquals(totalPrice, result.getValor());
    }
}