package com.sicredi.desafio_votacao.exception;

public class VotoException extends RuntimeException {

    public VotoException(String mensagem) {
        super(mensagem);
    }

    public VotoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
