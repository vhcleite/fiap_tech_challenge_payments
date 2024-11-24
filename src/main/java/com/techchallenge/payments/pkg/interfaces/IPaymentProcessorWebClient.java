package com.techchallenge.payments.pkg.interfaces;

import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;

import java.math.BigDecimal;

public interface IPaymentProcessorWebClient {
    String criarPagamento(BigDecimal valor);

    StatusPagamento consultarStatusPagamento(String externalId);
}
