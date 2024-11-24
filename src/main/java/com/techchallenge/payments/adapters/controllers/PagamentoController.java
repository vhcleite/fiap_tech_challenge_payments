package com.techchallenge.payments.adapters.controllers;

import com.techchallenge.payments.adapters.gateway.pagamento.PagamentoGateway;
import com.techchallenge.payments.adapters.gateway.pagamento.PaymentProcessorGateway;
import com.techchallenge.payments.adapters.gateway.pedido.PedidoGateway;
import com.techchallenge.payments.core.entities.pagamento.ExternalPagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.entities.pedido.PedidoEntity;
import com.techchallenge.payments.core.requests.CriarPagamentoDto;
import com.techchallenge.payments.core.usecases.PagamentoProcessorUseCase;
import com.techchallenge.payments.core.usecases.PagamentoUseCase;
import com.techchallenge.payments.core.usecases.PedidoUseCase;
import com.techchallenge.payments.pkg.interfaces.IPagamentoDataSource;
import com.techchallenge.payments.pkg.interfaces.IPaymentProcessorWebClient;
import com.techchallenge.payments.pkg.interfaces.IPedidoWebClient;

public class PagamentoController {
    private final PedidoUseCase pedidoUseCase;
    private final PagamentoUseCase pagamentoUseCase;
    private final PagamentoProcessorUseCase pagamentoProcessorUseCase;

    public PagamentoController(
            IPedidoWebClient pedidoWebClient,
            IPagamentoDataSource pagamentoDataSource,
            IPaymentProcessorWebClient paymentProcessorWebClient
    ) {
        PagamentoGateway pagamentoGateway = new PagamentoGateway(pagamentoDataSource);
        this.pagamentoUseCase = new PagamentoUseCase(pagamentoGateway);

        PaymentProcessorGateway paymentProcessorGateway = new PaymentProcessorGateway(paymentProcessorWebClient);
        this.pagamentoProcessorUseCase = new PagamentoProcessorUseCase(paymentProcessorGateway);

        PedidoGateway pedidoGateway = new PedidoGateway(pedidoWebClient);
        this.pedidoUseCase = new PedidoUseCase(pedidoGateway);
    }

    public PagamentoEntity criarPagamento(CriarPagamentoDto dto) {
        PedidoEntity pedido = pedidoUseCase.buscarPorId(dto.getPedidoId());
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
