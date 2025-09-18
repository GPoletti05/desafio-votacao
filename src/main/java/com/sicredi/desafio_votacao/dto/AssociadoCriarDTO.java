package com.sicredi.desafio_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociadoCriarDTO {
    @NotBlank(message = "O nome do associado é obrigatório!")
    private String nome;

    @NotBlank(message = "O CPF do associado é obrigatório!")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 dígitos numéricos")
    private String cpf;
}
