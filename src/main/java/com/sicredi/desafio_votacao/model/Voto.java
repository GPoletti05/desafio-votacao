package com.sicredi.desafio_votacao.model;

import com.sicredi.desafio_votacao.enums.EscolhaVoto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "voto", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"associado_id", "sessao_votacao_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voto_id_seq")
    @SequenceGenerator(name = "voto_id_seq", sequenceName = "voto_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "associado_id", nullable = false)
    private Associado associado;

    @ManyToOne
    @JoinColumn(name = "sessao_votacao_id", nullable = false)
    private SessaoVotacao sessaoVotacao;

    @Enumerated(EnumType.STRING)
    private EscolhaVoto escolha;

    @Column(name = "data_voto", nullable = false)
    private LocalDateTime dataVoto = LocalDateTime.now();
}
