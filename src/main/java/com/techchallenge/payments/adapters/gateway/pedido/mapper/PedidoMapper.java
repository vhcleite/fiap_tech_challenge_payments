package com.techchallenge.payments.adapters.gateway.pedido.mapper;

import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.pkg.dto.PedidoDto;

public class PedidoMapper {
    public static PedidoEntity toEntity(PedidoDto pedido) {
        return new PedidoEntity(
                pedido.id(),
                pedido.totalPrice()
        );
    }
}
