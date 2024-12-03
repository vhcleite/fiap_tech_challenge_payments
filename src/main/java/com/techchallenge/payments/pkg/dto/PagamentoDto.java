package com.techchallenge.payments.pkg.dto;

import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PagamentoDto(
        String id,
        String externalId,
        String pedidoId,
        BigDecimal valor,
        StatusPagamento status,
        OffsetDateTime pagamentoConfirmadoAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

