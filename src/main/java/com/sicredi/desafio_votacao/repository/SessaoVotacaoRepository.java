package com.sicredi.desafio_votacao.repository;

import com.sicredi.desafio_votacao.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
    boolean existsByPautaId(Long pautaId);

    Optional<SessaoVotacao> findByPauta_Id(Long pautaId);
}
