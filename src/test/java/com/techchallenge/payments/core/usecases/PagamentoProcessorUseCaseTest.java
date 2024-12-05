package com.techchallenge.payments.core.usecases;

import com.techchallenge.payments.adapters.gateway.pagamento.PaymentProcessorGateway;
import com.techchallenge.payments.core.entities.pagamento.ExternalPagamentoEntity;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
class PagamentoProcessorUseCaseTest {

    @Mock
    private PaymentProcessorGateway paymentProcessorGateway;

    @InjectMocks
    private PagamentoProcessorUseCase pagamentoProcessorUseCase;

    @Test
    void testCriarPagamento_Success() {
        // Arrange
        BigDecimal valor = new BigDecimal("100.00");
        ExternalPagamentoEntity expectedPagamento = new ExternalPagamentoEntity("external123", valor);
        when(paymentProcessorGateway.criarPagamento(valor)).thenReturn(expectedPagamento);

        // Act
        ExternalPagamentoEntity result = pagamentoProcessorUseCase.criar(valor);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPagamento, result);
        verify(paymentProcessorGateway).criarPagamento(valor);
    }

    @Test
    void testConsultarStatusPagamento_Success() {
        // Arrange
        String externalId = "external123";
        StatusPagamento expectedStatus = StatusPagamento.APROVADO;
        when(paymentProcessorGateway.consultarStatusPagamento(externalId)).thenReturn(expectedStatus);

        // Act
        StatusPagamento result = pagamentoProcessorUseCase.consultarStatusPagamento(externalId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStatus, result);
        verify(paymentProcessorGateway).consultarStatusPagamento(externalId);
    }
}
