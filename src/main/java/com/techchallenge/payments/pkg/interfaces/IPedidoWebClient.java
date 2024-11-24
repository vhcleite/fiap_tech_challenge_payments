package com.techchallenge.payments.pkg.interfaces;

import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import com.techchallenge.payments.pkg.dto.PedidoDto;

public interface IPedidoWebClient {
    PedidoDto getPedido(String id);

    PedidoDto updateStatus(PedidoStatus pedidoStatus);
}
