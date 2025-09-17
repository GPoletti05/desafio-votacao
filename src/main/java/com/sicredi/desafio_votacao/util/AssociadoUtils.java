package com.sicredi.desafio_votacao.util;

public class AssociadoUtils {

    public static boolean verificaCPF(String cpf) {
        //remove caracteres não numericos
        cpf = cpf.replaceAll("\\D", "");

        // verifica se tem 11 digitos
        if (cpf.length() != 11) {
            return false;
        }

        // descarta CPFs com todos os dígitos iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            int peso = 10;

            // calculo do primeiro digito verificador
            for (int i = 0; i < 9; i++) {
                int num = cpf.charAt(i) - '0';
                soma += num * peso--;
            }

            int resto = 11 - (soma % 11);
            int digito1 = (resto == 10 || resto == 11) ? 0 : resto;

            // calculo do segundo digito verificador
            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                int num = cpf.charAt(i) - '0';
                soma += num * peso--;
            }

            resto = 11 - (soma % 11);
            int digito2 = (resto == 10 || resto == 11) ? 0 : resto;

            // Vverifica se os digitos calculados conferem com os informados
            return (digito1 == (cpf.charAt(9) - '0')) && (digito2 == (cpf.charAt(10) - '0'));

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
