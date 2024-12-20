package com.techchallenge.payments.adapters.gateway.pedido;

import com.techchallenge.payments.adapters.gateway.pedido.mapper.PedidoMapper;
import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import com.techchallenge.payments.pkg.interfaces.IPedidoWebClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PedidoGateway {
    private final IPedidoWebClient webClient;

    public PedidoGateway(IPedidoWebClient webClient) {
        this.webClient = webClient;
    }

    public PedidoEntity getById(String id) {
        return Optional.ofNullable(id)
                .map(webClient::getPedido)
                .map(PedidoMapper::toEntity)
                .orElse(null);
    }

    public PedidoEntity atualizarStatus(String pedidoId, PedidoStatus pedidoStatus) {
        return Optional.ofNullable(webClient.updateStatus(pedidoId, pedidoStatus))
                .map(PedidoMapper::toEntity)
                .orElse(null);
    }
}
