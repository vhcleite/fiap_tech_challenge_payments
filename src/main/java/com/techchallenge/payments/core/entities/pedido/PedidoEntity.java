package com.techchallenge.payments.core.entities.pedido;

import java.math.BigDecimal;

public record PedidoEntity(
        String id,
        BigDecimal totalPrice) {
}
