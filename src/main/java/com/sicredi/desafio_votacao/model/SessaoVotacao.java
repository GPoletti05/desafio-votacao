package com.sicredi.desafio_votacao.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessao_votacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessao_votacao_id_seq")
    @SequenceGenerator(name = "sessao_votacao_id_seq", sequenceName = "sessao_votacao_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(name = "data_fechamento", nullable = false)
    private LocalDateTime dataFechamento;

    @Column(name = "encerrada")
    private Boolean encerrada = false;
}
