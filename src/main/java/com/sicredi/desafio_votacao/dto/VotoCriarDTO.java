package com.sicredi.desafio_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoCriarDTO {
    @NotNull(message = "O id da sessão é obrigatório para registrar o voto!")
    private Long sessaoId;

    @NotNull(message = "O id do associado é obrigatório para registrar o voto!")
    private Long associadoId;

    @NotBlank(message = "A escolha do voto é obrigatória")
    private String escolha;
}
