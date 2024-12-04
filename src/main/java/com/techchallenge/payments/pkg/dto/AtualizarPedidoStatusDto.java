package com.techchallenge.payments.pkg.dto;

import com.techchallenge.payments.core.entities.pedido.PedidoStatus;

public record AtualizarPedidoStatusDto(PedidoStatus status) {
}
