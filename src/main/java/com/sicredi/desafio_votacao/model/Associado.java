package com.sicredi.desafio_votacao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "associado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Associado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "associado_id_seq")
    @SequenceGenerator(name = "associado_id_seq", sequenceName = "associado_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
}
