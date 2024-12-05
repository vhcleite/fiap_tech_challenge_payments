package com.techchallenge.payments.core.entities.pagamento;

import com.techchallenge.payments.core.exceptions.InvalidPagamentoException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PagamentoEntity(
        String id,
        String externalId,
        String pedidoId,
        BigDecimal valor,
        StatusPagamento status,
        OffsetDateTime pagamentoConfirmadoAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public PagamentoEntity {
        validate(pedidoId, externalId, valor, status);
    }

    private static void validate(
            String pedidoId,
            String externalId,
            BigDecimal valor,
            StatusPagamento status
    ) {
        if (pedidoId == null || pedidoId.trim().isEmpty()) {
            throw new InvalidPagamentoException("O pedido_id não pode ser nulo ou vazio.");
        }

        validateExternalId(externalId);

        if (valor == null) {
            throw new InvalidPagamentoException("O valor não pode ser nulo.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPagamentoException("O valor deve ser maior que zero.");
        }

        if (status == null) {
            throw new InvalidPagamentoException("O status não pode ser nulo.");
        }
    }

    private static void validateExternalId(String externalId) {
        if (externalId != null && externalId.trim().isEmpty()) {
            throw new InvalidPagamentoException("external_id nao informado.");
        }
    }

    public PagamentoEntity withExternalId(String newExternalId) {
        validateExternalId(newExternalId);
        return new PagamentoEntity(id, newExternalId, pedidoId, valor, status, pagamentoConfirmadoAt, createdAt, updatedAt);
    }

    public PagamentoEntity withStatus(StatusPagamento newStatus) {
        return new PagamentoEntity(id, externalId, pedidoId, valor, newStatus, pagamentoConfirmadoAt, createdAt, updatedAt);
    }

    public PagamentoEntity withPagamentoConfirmadoAt(OffsetDateTime newPagamentoConfirmadoAt) {
        return new PagamentoEntity(id, externalId, pedidoId, valor, status, newPagamentoConfirmadoAt, createdAt, updatedAt);
    }

    public PagamentoEntity withUpdatedAt(OffsetDateTime newUpdatedAt) {
        return new PagamentoEntity(id, externalId, pedidoId, valor, status, pagamentoConfirmadoAt, createdAt, newUpdatedAt);
    }
}
