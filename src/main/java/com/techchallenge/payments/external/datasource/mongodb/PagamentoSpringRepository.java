package com.techchallenge.payments.external.datasource.mongodb;

import com.techchallenge.payments.pkg.dto.PagamentoDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PagamentoSpringRepository extends MongoRepository<PagamentoDto, String> {
    @Query("{ 'pedidoId': ?0 }")
    List<PagamentoDto> findByPedidoIdOrderByUpdatedAtDesc(String pedidoId);

    @Query("{ 'externalId': ?0 }")
    List<PagamentoDto> findByExternalIdOrderByUpdatedAtDesc(String externalId);
}