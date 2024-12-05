package com.techchallenge.payments.adapters.gateway.pagamento.mapper;

import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.pkg.dto.PagamentoDto;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

public class PagamentoMapper {
    public static PagamentoDto toDto(PagamentoEntity pagamento) {
        return new PagamentoDto(
                pagamento.id(),
                pagamento.externalId(),
                pagamento.pedidoId(),
                pagamento.valor(),
                pagamento.status(),
                Optional.ofNullable(pagamento.pagamentoConfirmadoAt()).map(Objects::toString).orElse(null),
                pagamento.createdAt().toString(),
                pagamento.updatedAt().toString()
        );
    }

    public static PagamentoEntity toEntity(PagamentoDto pagamento) {
        return new PagamentoEntity(
                pagamento.id(),
                pagamento.externalId(),
                pagamento.pedidoId(),
                pagamento.valor(),
                pagamento.status(),
                Optional.ofNullable(pagamento.pagamentoConfirmadoAt()).map(OffsetDateTime::parse).orElse(null),
                OffsetDateTime.parse(pagamento.createdAt()),
                OffsetDateTime.parse(pagamento.updatedAt())
        );
    }
}
