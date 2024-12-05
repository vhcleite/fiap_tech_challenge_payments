package com.techchallenge.payments.adapters.gateway.pagamento;

import com.techchallenge.payments.core.entities.pagamento.ExternalPagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.pkg.interfaces.IPaymentProcessorWebClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
class PaymentProcessorGatewayTest {

    @Mock
    private IPaymentProcessorWebClient datasource;

    @InjectMocks
    private PaymentProcessorGateway paymentProcessorGateway;

    @Test
    void testCriarPagamento_Success() {
        // Arrange
        BigDecimal valor = new BigDecimal("100.00");
        String externalId = "external123";
        ExternalPagamentoEntity expectedPagamento = new ExternalPagamentoEntity(externalId, valor);

        when(datasource.criarPagamento(valor)).thenReturn(externalId);

        // Act
        ExternalPagamentoEntity result = paymentProcessorGateway.criarPagamento(valor);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPagamento, result);
        verify(datasource).criarPagamento(valor);
    }

    @Test
    void testCriarPagamento_NullValor_ThrowsException() {
        // Arrange
        BigDecimal valor = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> paymentProcessorGateway.criarPagamento(valor));
        verifyNoInteractions(datasource);
    }

    @Test
    void testConsultarStatusPagamento_Success() {
        // Arrange
        String externalId = "external123";
        StatusPagamento expectedStatus = StatusPagamento.APROVADO;

        when(datasource.consultarStatusPagamento(externalId)).thenReturn(expectedStatus);

        // Act
        StatusPagamento result = paymentProcessorGateway.consultarStatusPagamento(externalId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStatus, result);
        verify(datasource).consultarStatusPagamento(externalId);
    }

    @Test
    void testConsultarStatusPagamento_NotFound() {
        // Arrange
        String externalId = "external123";

        when(datasource.consultarStatusPagamento(externalId)).thenReturn(null);

        // Act
        StatusPagamento result = paymentProcessorGateway.consultarStatusPagamento(externalId);

        // Assert
        assertNull(result);
        verify(datasource).consultarStatusPagamento(externalId);
    }
}
