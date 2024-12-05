package com.techchallenge.payments.adapters.gateway.pagamento.mapper;

import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.pkg.dto.PagamentoDto;

public class PagamentoMapper {
    public static PagamentoDto toDto(PagamentoEntity pagamento) {
        return new PagamentoDto(
                pagamento.id(),
                pagamento.externalId(),
                pagamento.pedidoId(),
                pagamento.valor(),
                pagamento.status(),
                pagamento.pagamentoConfirmadoAt(),
                pagamento.createdAt(),
                pagamento.updatedAt()
        );
    }

    public static PagamentoEntity toEntity(PagamentoDto pagamento) {
        return new PagamentoEntity(
                pagamento.id(),
                pagamento.externalId(),
                pagamento.pedidoId(),
                pagamento.valor(),
                pagamento.status(),
                pagamento.pagamentoConfirmadoAt(),
                pagamento.createdAt(),
                pagamento.updatedAt()
        );
    }
}
