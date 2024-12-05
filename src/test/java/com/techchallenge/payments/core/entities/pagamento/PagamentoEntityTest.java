package com.techchallenge.payments.core.entities.pagamento;

import com.techchallenge.payments.core.exceptions.InvalidPagamentoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PagamentoEntityTest {

    @Test
    void shouldCreatePagamentoEntitySuccessfully() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        String id = "123";
        String externalId = "ext123";
        String pedidoId = "pedido123";
        BigDecimal valor = new BigDecimal("100.00");
        StatusPagamento status = StatusPagamento.APROVADO;

        // Act
        PagamentoEntity pagamento = new PagamentoEntity(id, externalId, pedidoId, valor, status, now, now, now);

        // Assert
        assertEquals(id, pagamento.id());
        assertEquals(externalId, pagamento.externalId());
        assertEquals(pedidoId, pagamento.pedidoId());
        assertEquals(valor, pagamento.valor());
        assertEquals(status, pagamento.status());
        assertEquals(now, pagamento.pagamentoConfirmadoAt());
        assertEquals(now, pagamento.createdAt());
        assertEquals(now, pagamento.updatedAt());
    }

    @Test
    void shouldThrowExceptionWhenPedidoIdIsNull() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();

        // Act & Assert
        InvalidPagamentoException exception = assertThrows(
                InvalidPagamentoException.class,
                () -> new PagamentoEntity(null, "ext123", null, new BigDecimal("100.00"), StatusPagamento.PENDENTE, now, now, now)
        );
        assertEquals("O pedido_id não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValorIsNull() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();

        // Act & Assert
        InvalidPagamentoException exception = assertThrows(
                InvalidPagamentoException.class,
                () -> new PagamentoEntity("1", "ext123", "pedido123", null, StatusPagamento.PENDENTE, now, now, now)
        );
        assertEquals("O valor não pode ser nulo.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValorIsZeroOrNegative() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();

        // Act & Assert for zero
        InvalidPagamentoException exceptionZero = assertThrows(
                InvalidPagamentoException.class,
                () -> new PagamentoEntity("1", "ext123", "pedido123", BigDecimal.ZERO, StatusPagamento.PENDENTE, now, now, now)
        );
        assertEquals("O valor deve ser maior que zero.", exceptionZero.getMessage());

        // Act & Assert for negative
        InvalidPagamentoException exceptionNegative = assertThrows(
                InvalidPagamentoException.class,
                () -> new PagamentoEntity("1", "ext123", "pedido123", new BigDecimal("-1.00"), StatusPagamento.PENDENTE, now, now, now)
        );
        assertEquals("O valor deve ser maior que zero.", exceptionNegative.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();

        // Act & Assert
        InvalidPagamentoException exception = assertThrows(
                InvalidPagamentoException.class,
                () -> new PagamentoEntity("1", "ext123", "pedido123", new BigDecimal("100.00"), null, now, now, now)
        );
        assertEquals("O status não pode ser nulo.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenExternalIdIsEmpty() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();

        // Act & Assert
        InvalidPagamentoException exception = assertThrows(
                InvalidPagamentoException.class,
                () -> new PagamentoEntity("1", "", "pedido123", new BigDecimal("100.00"), StatusPagamento.PENDENTE, now, now, now)
        );
        assertEquals("external_id nao informado.", exception.getMessage());
    }

    @Test
    void shouldCreateWithUpdatedExternalId() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        PagamentoEntity pagamento = new PagamentoEntity("1", "ext123", "pedido123", new BigDecimal("100.00"), StatusPagamento.PENDENTE, now, now, now);

        // Act
        PagamentoEntity updatedPagamento = pagamento.withExternalId("newExternal123");

        // Assert
        assertEquals("newExternal123", updatedPagamento.externalId());
    }

    @Test
    void shouldCreateWithUpdatedStatus() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        PagamentoEntity pagamento = new PagamentoEntity("1", "ext123", "pedido123", new BigDecimal("100.00"), StatusPagamento.PENDENTE, now, now, now);

        // Act
        PagamentoEntity updatedPagamento = pagamento.withStatus(StatusPagamento.PENDENTE);

        // Assert
        assertEquals(StatusPagamento.PENDENTE, updatedPagamento.status());
    }

    @Test
    void shouldCreateWithUpdatedPagamentoConfirmadoAt() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime newPagamentoConfirmadoAt = now.plusDays(1);
        PagamentoEntity pagamento = new PagamentoEntity("1", "ext123", "pedido123", new BigDecimal("100.00"), StatusPagamento.PENDENTE, now, now, now);

        // Act
        PagamentoEntity updatedPagamento = pagamento.withPagamentoConfirmadoAt(newPagamentoConfirmadoAt);

        // Assert
        assertEquals(newPagamentoConfirmadoAt, updatedPagamento.pagamentoConfirmadoAt());
    }

    @Test
    void shouldCreateWithUpdatedUpdatedAt() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime newUpdatedAt = now.plusHours(2);
        PagamentoEntity pagamento = new PagamentoEntity("1", "ext123", "pedido123", new BigDecimal("100.00"), StatusPagamento.PENDENTE, now, now, now);

        // Act
        PagamentoEntity updatedPagamento = pagamento.withUpdatedAt(newUpdatedAt);

        // Assert
        assertEquals(newUpdatedAt, updatedPagamento.updatedAt());
    }
}
