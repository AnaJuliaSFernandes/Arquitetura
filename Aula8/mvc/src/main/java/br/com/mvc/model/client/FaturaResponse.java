package br.com.mvc.model.client;

import java.math.BigDecimal;

public record FaturaResponse(String faturaId, Long pedidoId, BigDecimal valor) {
}