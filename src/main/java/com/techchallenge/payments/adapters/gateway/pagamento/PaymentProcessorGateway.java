package com.techchallenge.payments.adapters.gateway.pagamento;

import com.techchallenge.payments.core.entities.pagamento.ExternalPagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.pkg.interfaces.IPaymentProcessorWebClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class PaymentProcessorGateway {

    private final IPaymentProcessorWebClient datasource;

    public PaymentProcessorGateway(IPaymentProcessorWebClient datasource) {
        this.datasource = datasource;
    }

    public ExternalPagamentoEntity criarPagamento(BigDecimal valor) {
        return Optional.of(valor)
                .map(datasource::criarPagamento)
                .map(externalId -> new ExternalPagamentoEntity(externalId, valor))
                .orElseThrow();
    }

    public StatusPagamento consultarStatusPagamento(String externalId) {
        return datasource.consultarStatusPagamento(externalId);
    }
}
