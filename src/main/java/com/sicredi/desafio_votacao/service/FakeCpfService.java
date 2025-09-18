package com.sicredi.desafio_votacao.service;

import com.sicredi.desafio_votacao.dto.StatusVotoDTO;
import com.sicredi.desafio_votacao.enums.AssociadoVotoStatus;
import com.sicredi.desafio_votacao.exception.CpfException;
import com.sicredi.desafio_votacao.util.AssociadoUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FakeCpfService {
    private final Random random = new Random();

    public StatusVotoDTO verificarCpf(String cpf) {
        // Verifica se o cpf segue o padrão.
        if (!AssociadoUtils.verificaCPF(cpf)) {
            throw new CpfException(cpf);
        }

        // Aleatoriamente decide se pode votar
        boolean podeVotar = random.nextBoolean();
        AssociadoVotoStatus status = podeVotar ? AssociadoVotoStatus.ABLE_TO_VOTE : AssociadoVotoStatus.UNABLE_TO_VOTE;

        return new StatusVotoDTO(status);
    }
}
