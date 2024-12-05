package com.techchallenge.payments.pkg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PedidoDto(
        String id,
        String status,
        @JsonProperty("total_price")
        BigDecimal totalPrice
) {
}
