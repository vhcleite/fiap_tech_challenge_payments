package com.techchallenge.payments.adapters.gateway.pagamento;

import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.exceptions.InvalidPagamentoException;
import com.techchallenge.payments.helpers.PagamentoHelper;
import com.techchallenge.payments.pkg.dto.PagamentoDto;
import com.techchallenge.payments.pkg.interfaces.IPagamentoDataSource;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
class PagamentoGatewayTest {

    @Mock
    private IPagamentoDataSource dataSource;

    @InjectMocks
    private PagamentoGateway pagamentoGateway;

    @Test
    void testCriarPagamento_Success() {
        // Arrange
        PagamentoEntity pagamento = PagamentoHelper.generatePagamentoNullId(StatusPagamento.PENDENTE);

        PagamentoDto savedDto = PagamentoHelper.generatePagamentoDto(StatusPagamento.PENDENTE);

        when(dataSource.insertPagamento(any(PagamentoDto.class))).thenReturn(savedDto);

        // Act
        PagamentoEntity result = pagamentoGateway.criarPagamento(pagamento);

        // Assert
        assertNotNull(result);
        assertEquals(savedDto.id(), result.getId());
        verify(dataSource).insertPagamento(any(PagamentoDto.class));
    }

    @Test
    void testCriarPagamento_InvalidPagamento() {
        // Arrange
        PagamentoEntity pagamento = PagamentoHelper.generatePagamento(StatusPagamento.PENDENTE);

        // Act & Assert
        InvalidPagamentoException exception = assertThrows(InvalidPagamentoException.class, () -> pagamentoGateway.criarPagamento(pagamento));
        assertEquals("Pagamento já existente", exception.getMessage());
        verifyNoInteractions(dataSource);
    }

    @Test
    void testConsultarByPedidoId_Success() {
        // Arrange
        String pedidoId = "pedido123";
        PagamentoDto pagamentoDto = PagamentoHelper.generatePagamentoDto(StatusPagamento.APROVADO);

        when(dataSource.firstPagamentoByPedidoIdSortByUpdatedAtDesc(pedidoId)).thenReturn(pagamentoDto);

        // Act
        PagamentoEntity result = pagamentoGateway.consultarByPedidoId(pedidoId);

        // Assert
        assertNotNull(result);
        assertEquals(pagamentoDto.id(), result.getId());
        verify(dataSource).firstPagamentoByPedidoIdSortByUpdatedAtDesc(pedidoId);
    }

    @Test
    void testConsultarByExternalId_Success() {
        // Arrange
        String externalId = "external123";
        PagamentoDto pagamentoDto = PagamentoHelper.generatePagamentoDto(StatusPagamento.APROVADO);

        when(dataSource.firstPagamentoByExternalIdSortByUpdatedAtDesc(externalId)).thenReturn(pagamentoDto);

        // Act
        PagamentoEntity result = pagamentoGateway.consultarByExternalId(externalId);

        // Assert
        assertNotNull(result);
        assertEquals(pagamentoDto.id(), result.getId());
        verify(dataSource).firstPagamentoByExternalIdSortByUpdatedAtDesc(externalId);
    }

    @Test
    void testAtualizarPagamento_Success() {
        // Arrange
        PagamentoEntity pagamento = PagamentoHelper.generatePagamento(StatusPagamento.PENDENTE);
        PagamentoDto pagamentoDto = PagamentoHelper.generatePagamentoDto(StatusPagamento.PENDENTE);

        when(dataSource.updatePagamento(any(PagamentoDto.class))).thenReturn(pagamentoDto);

        // Act
        PagamentoEntity result = pagamentoGateway.atualizarPagamento(pagamento);

        // Assert
        assertNotNull(result);
        assertEquals(pagamentoDto.id(), result.getId());
        verify(dataSource).updatePagamento(any(PagamentoDto.class));
    }
}