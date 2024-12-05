package com.techchallenge.payments.api.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.payments.adapters.controllers.PagamentoController;
import com.techchallenge.payments.core.entities.pagamento.StatusPagamento;
import com.techchallenge.payments.core.requests.CallbackPagamentoDto;
import com.techchallenge.payments.core.requests.CriarPagamentoDto;
import com.techchallenge.payments.helpers.PagamentoHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PagamentoHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private PagamentoController controller;

    private AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        PagamentoHandler pagamentoHandler = new PagamentoHandler(controller);
        mockMvc = MockMvcBuilders.standaloneSetup(pagamentoHandler).build();
    }

    @AfterEach
    void tearsDown() throws Exception {
        mock.close();
    }

    @Test
    public void shouldCreatePagamento() throws Exception {
        //Arrange
        var pagamento = PagamentoHelper.generatePagamento(StatusPagamento.PENDENTE);
        var dto = new CriarPagamentoDto("pedidoId");
        Mockito.when(controller.criarPagamento(dto)).thenReturn(pagamento);

        //Act & Assert
        mockMvc.perform(
                        post("/api/pagamentos")
                                .contentType("application/json")
                                .content(asJsonString(dto))
                )
                .andExpect(status().isCreated());

        verify(controller, times(1)).criarPagamento(dto);
    }

    @Test
    public void shouldConsultarByPedidoId() throws Exception {
        //Arrange
        var pedidoId = "pedido_id";
        var pagamento = PagamentoHelper.generatePagamento(StatusPagamento.PENDENTE);
        Mockito.when(controller.consultarByPedidoId(pedidoId)).thenReturn(pagamento);

        //Act & Assert
        mockMvc.perform(get("/api/pagamentos/pedidos/" + pedidoId))
                .andExpect(status().isOk());

        verify(controller, times(1)).consultarByPedidoId(pedidoId);
    }

    @Test
    public void shouldReceiveCallbackPagamento() throws Exception {
        //Arrange
        var pagamento = PagamentoHelper.generatePagamento(StatusPagamento.PENDENTE);
        var externaId = "externalId";
        Mockito.when(controller.pagamentoStatusCallback(externaId)).thenReturn(pagamento);

        //Act & Assert
        mockMvc.perform(
                        post("/api/pagamentos/callback")
                                .contentType("application/json")
                                .content(asJsonString(new CallbackPagamentoDto(externaId)))
                )
                .andExpect(status().isOk());

        verify(controller, times(1)).pagamentoStatusCallback(externaId);
    }

    private String asJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper()
                .writeValueAsString(object);
    }
}