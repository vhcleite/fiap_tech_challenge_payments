package com.techchallenge.payments.external.datasource.mongodb;

import com.techchallenge.payments.pkg.dto.PagamentoDto;
import com.techchallenge.payments.pkg.interfaces.IPagamentoDataSource;

import java.util.UUID;

//@Component
public class PagamentoMongoSpringDataSource implements IPagamentoDataSource {
    private final PagamentoSpringRepository repository;

    public PagamentoMongoSpringDataSource(
            PagamentoSpringRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public PagamentoDto insertPagamento(PagamentoDto pagamento) {
        String id = UUID.randomUUID().toString();
        return repository.save(pagamento.withId(id));
    }

    @Override
    public PagamentoDto firstPagamentoByPedidoIdSortByUpdatedAtDesc(String pedidoId) {
        return repository.findByPedidoIdOrderByUpdatedAtDesc(pedidoId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public PagamentoDto firstPagamentoByExternalIdSortByUpdatedAtDesc(String externalId) {
        return repository.findByExternalIdOrderByUpdatedAtDesc(externalId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public PagamentoDto updatePagamento(PagamentoDto pagamento) {
        return repository.save(pagamento);
    }
}
