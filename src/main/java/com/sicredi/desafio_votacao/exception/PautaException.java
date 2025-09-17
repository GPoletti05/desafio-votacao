package com.sicredi.desafio_votacao.exception;

public class PautaException extends RuntimeException {

    public PautaException(String mensagem) {
        super(mensagem);
    }

    public PautaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
