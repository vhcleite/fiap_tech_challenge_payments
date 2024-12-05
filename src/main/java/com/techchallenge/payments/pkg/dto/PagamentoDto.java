package com.techchallenge.payments.pkg.dto;

import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("pagamentos")
public record PagamentoDto(
        @Id
        String id,
        String externalId,
        String pedidoId,
        BigDecimal valor,
        StatusPagamento status,
        String pagamentoConfirmadoAt,
        String createdAt,
        String updatedAt
) {
    /**
     * Returns a new instance of PagamentoDto with the specified id.
     *
     * @param newId the new id to set
     * @return a new PagamentoDto instance with the updated id
     */
    public PagamentoDto withId(String newId) {
        return new PagamentoDto(
                newId,
                this.externalId,
                this.pedidoId,
                this.valor,
                this.status,
                this.pagamentoConfirmadoAt,
                this.createdAt,
                this.updatedAt
        );
    }
}

