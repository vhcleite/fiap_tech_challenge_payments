package com.techchallenge.payments.pkg.dto;

import java.math.BigDecimal;

public class PedidoDto {
    String id;
    String status;
    BigDecimal totalPrice;

    public PedidoDto(String id, String status, BigDecimal totalPrice) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
