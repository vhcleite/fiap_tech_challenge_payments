package com.techchallenge.payments.core.entities.pagamento;

import com.techchallenge.payments.core.exceptions.InvalidPagamentoException;

import java.math.BigDecimal;

public record ExternalPagamentoEntity(String externalId, BigDecimal valor) {

    public ExternalPagamentoEntity {
        // Perform validation during construction
        validate(externalId, valor);
    }

    private static void validate(String externalId, BigDecimal valor) {
        validateExternalId(externalId);

        if (valor == null) {
            throw new InvalidPagamentoException("O valor não pode ser nulo.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPagamentoException("O valor deve ser maior que zero.");
        }
    }

    private static void validateExternalId(String externalId) {
        if (externalId != null && externalId.trim().isEmpty()) {
            throw new InvalidPagamentoException("external_id nao informado.");
        }
    }
}
