package com.sicredi.desafio_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PautaCriarDTO {
    @NotBlank(message = "O título da Pauta é obrigatório!")
    private String titulo;

    private String motivo;
}
