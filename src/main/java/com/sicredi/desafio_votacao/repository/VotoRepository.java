package com.sicredi.desafio_votacao.repository;

import com.sicredi.desafio_votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsBySessaoVotacao_IdAndAssociado_Id(Long sessaoId, Long associadoId);

    List<Voto> findBySessaoVotacao_Id(Long sessaoVotacaoId);
}
