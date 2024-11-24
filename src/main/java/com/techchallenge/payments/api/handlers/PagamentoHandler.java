package com.techchallenge.payments.api.handlers;

import com.techchallenge.payments.adapters.controllers.PagamentoController;
import com.techchallenge.payments.core.entities.pagamento.PagamentoEntity;
import com.techchallenge.payments.core.requests.CallbackPagamentoDto;
import com.techchallenge.payments.core.requests.CriarPagamentoDto;
import com.techchallenge.payments.external.datasource.mongodb.PagamentoMongoDbDataSource;
import com.techchallenge.payments.external.webclient.MercadoPagoPaymentProcessorWebClient;
import com.techchallenge.payments.external.webclient.PedidoWebClient;
import com.techchallenge.payments.pkg.interfaces.IPagamentoDataSource;
import com.techchallenge.payments.pkg.interfaces.IPaymentProcessorWebClient;
import com.techchallenge.payments.pkg.interfaces.IPedidoWebClient;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/pagamentos")
public class PagamentoHandler {

    private final PagamentoController pagamentoController;

    public PagamentoHandler(
            @Value("${mongo.connection}") String mongoConnection,
            @Value("${mongo.database}") String mongoDatabase,
            @Value("${payment.callback.url}") String paymentCallbackUrl
    ) {
        IPagamentoDataSource pagamentoDataSource = new PagamentoMongoDbDataSource(mongoConnection, mongoDatabase);
        IPedidoWebClient pedidoWebClient = new PedidoWebClient();
        IPaymentProcessorWebClient webClient = new MercadoPagoPaymentProcessorWebClient(paymentCallbackUrl);

        this.pagamentoController = new PagamentoController(pedidoWebClient, pagamentoDataSource, webClient);
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

    @PostMapping("/callback")
    public ResponseEntity<PagamentoEntity> callback(@RequestBody CallbackPagamentoDto dto) {
        PagamentoEntity pagamento = pagamentoController.pagamentoStatusCallback(dto.getExternalId());
        return new ResponseEntity<>(pagamento, HttpStatus.CREATED);
    }
}
