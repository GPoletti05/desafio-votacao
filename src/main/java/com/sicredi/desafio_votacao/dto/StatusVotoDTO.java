package com.sicredi.desafio_votacao.dto;

import com.sicredi.desafio_votacao.enums.AssociadoVotoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusVotoDTO {
    private AssociadoVotoStatus status;

}
