package com.sicredi.desafio_votacao.repository;

import com.sicredi.desafio_votacao.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
