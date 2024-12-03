# language: pt
Funcionalidade: Pagamento

  Cenario: Criar pagamento
    Dado que tenho um pedido previamente cadastrado
    Quando crio um pagamento
    Então vejo que o pagamento foi criado

  Cenario: Receber callback de pagamento
    Dado que tenho um pagamento previamente criado
    Quando recebo o callback de um pagamento aprovado
    Então vejo que o pagamento mudou de estado
