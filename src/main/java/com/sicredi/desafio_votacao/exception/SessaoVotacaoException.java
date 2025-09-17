package com.sicredi.desafio_votacao.exception;

public class SessaoVotacaoException extends RuntimeException {

    public SessaoVotacaoException(String mensagem) {
        super(mensagem);
    }

    public SessaoVotacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
