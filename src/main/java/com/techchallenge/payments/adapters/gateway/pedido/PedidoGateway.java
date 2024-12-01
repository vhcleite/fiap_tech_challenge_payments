package com.techchallenge.payments.adapters.gateway.pedido;

import com.techchallenge.payments.adapters.gateway.pedido.mapper.PedidoMapper;
import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import com.techchallenge.payments.pkg.interfaces.IPedidoWebClient;

import java.util.Optional;

public class PedidoGateway {
    private final IPedidoWebClient webClient;

    public PedidoGateway(IPedidoWebClient webClient) {
        this.webClient = webClient;
    }

    public PedidoEntity getById(String id) {
        return Optional.of(id)
                .map(webClient::getPedido)
                .map(PedidoMapper::toEntity)
                .orElse(null);
    }

    public PedidoEntity atualizarStatus(String pedidoId, PedidoStatus pedidoStatus) {
        return Optional.of(webClient.updateStatus(pedidoId, pedidoStatus))
                .map(PedidoMapper::toEntity)
                .orElse(null);
    }
}
