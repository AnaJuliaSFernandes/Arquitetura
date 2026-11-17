package br.com.mvc.model.client;

public class FaturamentoException extends RuntimeException {

    public FaturamentoException(String message) {
        super(message);
    }

    public FaturamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}