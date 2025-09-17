package com.sicredi.desafio_votacao.enums;

public enum EscolhaVoto {
    SIM("Sim"),
    NAO("Não");

    private final String valor;

    EscolhaVoto(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    // Método para normalizar a entrada
    public static EscolhaVoto fromString(String entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("A escolha do voto não pode ser nula");
        }

        String normalizado = entrada.trim().toLowerCase();

        return switch (normalizado) {
            case "sim", "s" -> SIM;
            case "nao", "não", "n" -> NAO;
            default -> throw new IllegalArgumentException("Escolha inválida. Use: Sim ou Não.");
        };
    }
}
