package br.com.hexagonal.infra.adapters.out.soap;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.hexagonal.domain.entities.Fatura;
import br.com.hexagonal.domain.ports.out.soap.NotaSoap;

@Component
public class NotaSoapImp implements NotaSoap {

    /**
     * Emissão simulada por SOAP: gera uma chave de acesso com 44 dígitos.
     * A integração real com o serviço fiscal poderá substituir este adaptador.
     */
    @Override
    public String emitirNota(Fatura fatura) {
        String pedido = String.format("%012d", fatura.getPedidoId());
        String sequencia = UUID.randomUUID().toString().replace("-", "")
                .substring(0, 32).toUpperCase();

        return pedido + sequencia;
    }
}