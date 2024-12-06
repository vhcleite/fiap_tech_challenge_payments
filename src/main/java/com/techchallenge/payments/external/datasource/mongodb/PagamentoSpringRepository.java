package com.techchallenge.payments.external.datasource.mongodb;

import com.techchallenge.payments.pkg.dto.PagamentoDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PagamentoSpringRepository extends MongoRepository<PagamentoDto, String> {
    List<PagamentoDto> findByPedidoIdOrderByUpdatedAtDesc(String pedidoId);

    List<PagamentoDto> findByExternalIdOrderByUpdatedAtDesc(String externalId);
}