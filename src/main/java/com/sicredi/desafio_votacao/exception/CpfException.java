package com.sicredi.desafio_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CpfException extends RuntimeException{
    public CpfException(String cpf) {
        super("CPF inválido: " + cpf);
    }
}
