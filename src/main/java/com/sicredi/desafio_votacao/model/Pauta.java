package com.sicredi.desafio_votacao.model;

import com.sicredi.desafio_votacao.enums.StatusPauta;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pauta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pauta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pauta_id_seq")
    @SequenceGenerator(name = "pauta_id_seq", sequenceName = "pauta_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusPauta status;
}
