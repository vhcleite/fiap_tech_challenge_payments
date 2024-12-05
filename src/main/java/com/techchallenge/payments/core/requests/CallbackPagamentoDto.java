package com.techchallenge.payments.core.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CallbackPagamentoDto(@JsonProperty("external_id") String externalId) {
}
