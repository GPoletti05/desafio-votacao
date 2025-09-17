package com.sicredi.desafio_votacao.service;

import com.sicredi.desafio_votacao.dto.PautaCriarDTO;
import com.sicredi.desafio_votacao.enums.StatusPauta;
import com.sicredi.desafio_votacao.exception.PautaException;
import com.sicredi.desafio_votacao.model.Pauta;
import com.sicredi.desafio_votacao.repository.PautaRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;

    public Pauta criarPauta(PautaCriarDTO dto) {

        Pauta pauta = Pauta.builder()
                .titulo(dto.getTitulo())
                .motivo(dto.getMotivo())
                .dataCriacao(LocalDateTime.now())
                .status(StatusPauta.AGUARDANDO_SESSAO)
                .build();
        return pautaRepository.save(pauta);
    }

    public Optional<Pauta> buscarPorId(Long id) {
        return pautaRepository.findById(id);
    }

    public List<Pauta> listarTodas() {
        return pautaRepository.findAll();
    }

    public void deletarPauta(Long id) {
        if (!pautaRepository.existsById(id)) {
            throw new PautaException("Pauta não encontrada com id: " + id);
        }
        pautaRepository.deleteById(id);
    }
}
