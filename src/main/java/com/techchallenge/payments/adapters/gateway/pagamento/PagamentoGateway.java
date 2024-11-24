package com.techchallenge.payments.adapters.gateway.pagamento;

import com.techchallenge.payments.adapters.gateway.pagamento.mapper.PagamentoMapper;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.exceptions.InvalidPagamentoException;
import com.techchallenge.payments.pkg.dto.PagamentoDto;
import com.techchallenge.payments.pkg.interfaces.IPagamentoDataSource;

import java.util.Optional;

public class PagamentoGateway {

    private final IPagamentoDataSource dataSource;

    public PagamentoGateway(IPagamentoDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PagamentoEntity criarPagamento(PagamentoEntity pagamento) {
        if (pagamento.getId() != null) throw new InvalidPagamentoException("Pagamento já existente");

        PagamentoDto savedPagamento = dataSource.insertPagamento(PagamentoMapper.toDto(pagamento));

        return PagamentoMapper.toEntity(savedPagamento);
    }

    public PagamentoEntity consultarByPedidoId(String pedidoId) {
        return Optional.ofNullable(pedidoId)
                .map(dataSource::firstPagamentoByPedidoIdSortByUpdatedAtDesc)
                .map(PagamentoMapper::toEntity)
                .orElse(null);
    }

    public PagamentoEntity consultarByExternalId(String externalId) {
        return Optional.ofNullable(externalId)
                .map(dataSource::firstPagamentoByExternalIdSortByUpdatedAtDesc)
                .map(PagamentoMapper::toEntity)
                .orElse(null);
    }

    public PagamentoEntity atualizarPagamento(PagamentoEntity pagamento) {
        PagamentoDto savedPagamento = dataSource.updatePagamento(PagamentoMapper.toDto(pagamento));
        return PagamentoMapper.toEntity(savedPagamento);
    }
}
