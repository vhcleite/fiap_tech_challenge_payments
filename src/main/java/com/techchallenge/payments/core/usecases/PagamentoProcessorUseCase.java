package com.techchallenge.payments.core.usecases;

import com.techchallenge.payments.adapters.gateway.pagamento.PaymentProcessorGateway;
import com.techchallenge.payments.core.entities.pagamento.ExternalPagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;

import java.math.BigDecimal;

public class PagamentoProcessorUseCase {
    private final PaymentProcessorGateway paymentProcessorGateway;

    public PagamentoProcessorUseCase(
            PaymentProcessorGateway paymentProcessorGateway
    ) {
        this.paymentProcessorGateway = paymentProcessorGateway;
    }

    public ExternalPagamentoEntity criar(BigDecimal valor) {
        return paymentProcessorGateway.criarPagamento(valor);
    }

    public StatusPagamento consultarStatusPagamento(String externalId) {
        return paymentProcessorGateway.consultarStatusPagamento(externalId);
    }
}

