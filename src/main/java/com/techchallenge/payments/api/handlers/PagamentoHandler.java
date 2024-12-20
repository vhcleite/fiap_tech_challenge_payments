package com.techchallenge.payments.api.handlers;

import com.techchallenge.payments.adapters.controllers.PagamentoController;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.requests.CallbackPagamentoDto;
import com.techchallenge.payments.core.requests.CriarPagamentoDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/pagamentos")
public class PagamentoHandler {

    private final PagamentoController pagamentoController;

    public PagamentoHandler(PagamentoController pagementoController) {
        this.pagamentoController = pagementoController;
    }

    @Operation(
            summary = "Cria novo pagamento",
            description = "Cria um novo pagamento na base de dados."
    )
    @PostMapping
    public ResponseEntity<PagamentoEntity> create(@RequestBody CriarPagamentoDto dto) {
        PagamentoEntity pagamento = pagamentoController.criarPagamento(dto);
        return new ResponseEntity<>(pagamento, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Busca estado pagamento por pedido id",
            description = "Busca estado pagamento por pedido id na base de dados."
    )
    @GetMapping("/pedidos/{pedido_id}")
    public ResponseEntity<PagamentoEntity> buscarPorPedidoId(@PathVariable("pedido_id") String pedidoId) {
        PagamentoEntity pagamento = pagamentoController.consultarByPedidoId(pedidoId);
        return new ResponseEntity<>(pagamento, HttpStatus.OK);
    }

    @Operation(
            summary = "Callback de pagamento",
            description = "Endpoint para receber o callback de um pagamento para saber se foi aprovado ou não."
    )
    @PostMapping("/callback")
    public ResponseEntity<PagamentoEntity> callback(@RequestBody CallbackPagamentoDto dto) {
        PagamentoEntity pagamento = pagamentoController.pagamentoStatusCallback(dto.externalId());
        return new ResponseEntity<>(pagamento, HttpStatus.OK);
    }
}
