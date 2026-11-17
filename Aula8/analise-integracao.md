# Análise da integração entre vendas e hub de faturamento

## Problema 1 – Hub indisponível

**O que acontece:** a aplicação de vendas tenta enviar o pedido, mas não consegue estabelecer conexão com o hub ou ocorre timeout.

**O que o negócio percebe:** o pedido não recebe o ID da fatura e permanece com status ABERTO. O usuário pode tentar fechar o pedido novamente.

**Solução:** utilizar timeout e tratamento de exceções, como já previsto no cliente REST. Para uma evolução, pode ser aplicado retry com limite de tentativas. A atuação fica no cliente de integração da aplicação de vendas.

## Problema 2 – Resposta perdida após a criação da fatura

**O que acontece:** o hub cria e salva a fatura, mas a resposta com o ID não chega à aplicação de vendas.

**O que o negócio percebe:** a fatura pode existir no hub, enquanto o pedido continua ABERTO no sistema de vendas. Uma nova tentativa pode gerar outra fatura.

**Solução:** utilizar uma chave de idempotência para identificar cada pedido e impedir a criação duplicada de faturas. A validação deve ocorrer no hub, antes de criar uma nova fatura.
