package com.techchallenge.payments.core.entities.pedido;

import java.math.BigDecimal;

public class PedidoEntity {
    String id;
    BigDecimal totalPrice;

    public PedidoEntity(String id, BigDecimal totalPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
