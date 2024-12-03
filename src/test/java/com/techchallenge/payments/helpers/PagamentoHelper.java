package com.techchallenge.payments.helpers;

import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.pkg.dto.PagamentoDto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class PagamentoHelper {
    public static PagamentoEntity generatePagamento(StatusPagamento status) {
        var pedidoId = "id";
        var totalPrice = BigDecimal.valueOf(100L);
        var externalId = "externalId";

        return new PagamentoEntity(
                "id",
                externalId,
                pedidoId,
                totalPrice,
                status,
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    public static PagamentoEntity generatePagamentoNullId(StatusPagamento status) {
        var pedidoId = "id";
        var totalPrice = BigDecimal.valueOf(100L);
        var externalId = "externalId";

        return new PagamentoEntity(
                null,
                externalId,
                pedidoId,
                totalPrice,
                status,
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    public static PagamentoDto generatePagamentoDto(StatusPagamento statusPagamento) {
        var pedidoId = "id";
        var totalPrice = BigDecimal.valueOf(100L);
        var externalId = "externalId";

        return new PagamentoDto(
                "id",
                externalId,
                pedidoId,
                totalPrice,
                statusPagamento,
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }
}
