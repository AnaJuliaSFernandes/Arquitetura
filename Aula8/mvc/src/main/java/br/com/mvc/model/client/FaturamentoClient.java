package br.com.mvc.model.client;

import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

/** Cliente REST do hub de faturamento (aplicação hexagonal). */
@Component
public class FaturamentoClient {

    private final RestClient restClient;

    public FaturamentoClient(
            @Value("${hub.faturamento.url}") String baseUrl,
            @Value("${hub.faturamento.timeout-ms:5000}") int timeoutMs) {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(timeoutMs));
        factory.setReadTimeout(Duration.ofMillis(timeoutMs));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    /** POST /fatura -> devolve a fatura criada pelo hub. */
    public FaturaResponse faturar(Long pedidoId, BigDecimal valor) {
        try {
            FaturaResponse response = restClient.post()
                    .uri("/fatura")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new FaturaRequest(pedidoId, valor))
                    .retrieve()
                    .body(FaturaResponse.class);

            if (response == null || response.faturaId() == null || response.faturaId().isBlank()) {
                throw new FaturamentoException("O hub respondeu sem o ID da fatura");
            }
            return response;

        } catch (RestClientResponseException ex) {
            // hub respondeu 4xx/5xx
            throw new FaturamentoException(
                    "O hub de faturamento recusou o pedido (HTTP " + ex.getStatusCode().value() + "): "
                            + ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            // hub fora do ar, conexão recusada, timeout...
            throw new FaturamentoException("Hub de faturamento indisponível: " + ex.getMessage(), ex);
        }
    }
}