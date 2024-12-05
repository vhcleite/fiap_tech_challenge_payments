package com.techchallenge.payments.core.usecases;

import com.techchallenge.payments.adapters.gateway.pedido.PedidoGateway;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import org.springframework.stereotype.Service;

@Service
public class PedidoUseCase {
    private final PedidoGateway pedidoGateway;

    public PedidoUseCase(PedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    public PedidoEntity buscarPorId(String id) {
        return pedidoGateway.getById(id);
    }

    public PedidoEntity atualizarStatusCallbackPagamento(String id, StatusPagamento statusPagamento) {
        PedidoEntity pedido = buscarPorId(id);

        if (statusPagamento == StatusPagamento.APROVADO) {
            return pedidoGateway.atualizarStatus(pedido.id(), PedidoStatus.RECEBIDO);
        }
        return pedido;
    }
}
