package com.techchallenge.payments.external.webclient;

import com.techchallenge.payments.core.entities.pedido.PedidoStatus;
import com.techchallenge.payments.pkg.dto.PedidoDto;
import com.techchallenge.payments.pkg.interfaces.IPedidoWebClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PedidoWebClient implements IPedidoWebClient {
    //TODO implementar comunicacao com api
    @Override
    public PedidoDto getPedido(String id) {
        return new PedidoDto("123", "INICIAL", BigDecimal.valueOf(57));
    }

    @Override
    public PedidoDto updateStatus(PedidoStatus pedidoStatus) {
        return new PedidoDto("123", pedidoStatus.name(), BigDecimal.valueOf(57));
    }
}
