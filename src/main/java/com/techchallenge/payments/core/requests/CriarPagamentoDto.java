package com.techchallenge.payments.core.requests;

import java.math.BigDecimal;

public class CriarPagamentoDto {
    private final String pedidoId;

    public CriarPagamentoDto(
            String pedidoId,
            BigDecimal valor
    ) {
        this.pedidoId = pedidoId;
    }

    public String getPedidoId() {
        return pedidoId;
    }
}
