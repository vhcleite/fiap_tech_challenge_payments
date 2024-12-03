package com.techchallenge.payments.adapters.controllers;

import com.techchallenge.payments.core.entities.pagamento.ExternalPagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.requests.CriarPagamentoDto;
import com.techchallenge.payments.core.usecases.PagamentoProcessorUseCase;
import com.techchallenge.payments.core.usecases.PagamentoUseCase;
import com.techchallenge.payments.core.usecases.PedidoUseCase;
import org.springframework.stereotype.Component;

@Component
public class PagamentoController {
    private final PedidoUseCase pedidoUseCase;
    private final PagamentoUseCase pagamentoUseCase;
    private final PagamentoProcessorUseCase pagamentoProcessorUseCase;

    public PagamentoController(
            PedidoUseCase pedidoUseCase,
            PagamentoUseCase pagamentoUseCase,
            PagamentoProcessorUseCase pagamentoProcessorUseCase) {
        this.pedidoUseCase = pedidoUseCase;
        this.pagamentoUseCase = pagamentoUseCase;
        this.pagamentoProcessorUseCase = pagamentoProcessorUseCase;
    }

    public PagamentoEntity criarPagamento(CriarPagamentoDto dto) {
        PedidoEntity pedido = pedidoUseCase.buscarPorId(dto.pedidoId());
        ExternalPagamentoEntity externalPagamento = this.pagamentoProcessorUseCase.criar(pedido.getTotalPrice());

        return this.pagamentoUseCase.criar(pedido.getId(), pedido.getTotalPrice(), externalPagamento.getExternalId());
    }

    public PagamentoEntity pagamentoStatusCallback(String externalId) {
        StatusPagamento status = pagamentoProcessorUseCase.consultarStatusPagamento(externalId);
        PagamentoEntity pagamento = pagamentoUseCase.callbackPagamento(externalId, status);

        pedidoUseCase.atualizarStatusCallbackPagamento(pagamento.getPedidoId(), pagamento.getStatus());
        return pagamento;
    }
}
