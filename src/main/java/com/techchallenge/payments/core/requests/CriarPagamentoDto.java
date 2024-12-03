package com.techchallenge.payments.core.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CriarPagamentoDto(@JsonProperty("pedido_id") String pedidoId) {
}
