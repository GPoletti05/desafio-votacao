package com.sicredi.desafio_votacao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessaoVotacaoCriarDTO {
    @NotNull(message = "O id da Pauta é obrigatório para criar uma sessão de votação!")
    private Long pautaId;

    private Integer duracaoMinutos;
}
