package com.sicredi.desafio_votacao.exception;

public class AssociadoException extends RuntimeException {

    public AssociadoException(String mensagem) {
        super(mensagem);
    }

    public AssociadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
