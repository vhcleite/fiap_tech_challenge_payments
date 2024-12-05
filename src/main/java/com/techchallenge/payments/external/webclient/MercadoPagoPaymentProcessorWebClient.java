package com.techchallenge.payments.external.webclient;

import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.pkg.interfaces.IPaymentProcessorWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class MercadoPagoPaymentProcessorWebClient implements IPaymentProcessorWebClient {
    private final String callbackUrl;

    public MercadoPagoPaymentProcessorWebClient(
            @Value("${payment.callback.url}") String callbackUrl
    ) {
        this.callbackUrl = callbackUrl;
    }

    @Override
    public String criarPagamento(BigDecimal valor) {
        return UUID.randomUUID().toString();
    }

    @Override
    public StatusPagamento consultarStatusPagamento(String externalId) {
        return StatusPagamento.APROVADO;
    }
}
