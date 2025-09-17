package com.sicredi.desafio_votacao.enums;

public enum StatusPauta {
    AGUARDANDO_SESSAO("Aguardando Sessão"),
    APROVADA("Aprovada"),
    REJEITADA("Rejeitada");

    private final String valor;

    StatusPauta(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
