package com.sicredi.desafio_votacao.service;

import com.sicredi.desafio_votacao.dto.AssociadoCriarDTO;
import com.sicredi.desafio_votacao.exception.AssociadoException;
import com.sicredi.desafio_votacao.model.Associado;
import com.sicredi.desafio_votacao.repository.AssociadoRepository;
import com.sicredi.desafio_votacao.util.AssociadoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final AssociadoRepository associadoRepository;

    public Associado criarAssociado(AssociadoCriarDTO dto) {
        // Verifca os dados do associado, a nulidade dos campos esta sendo verificada no dto.
        this.verificarDadosAssociado(dto);

        Associado associado = Associado.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();
        return associadoRepository.save(associado);
    }

    public Optional<Associado> buscarPorId(Long id) {
        return associadoRepository.findById(id);
    }

    public Optional<Associado> buscarPorCpf(String cpf) {
        return associadoRepository.findByCpf(cpf);
    }

    public List<Associado> listarTodas() {
        return associadoRepository.findAll();
    }

    public void deletarAssociado(Long id) {
        if (!associadoRepository.existsById(id)) {
            throw new AssociadoException("Associado não encontrada com id: " + id);
        }
        associadoRepository.deleteById(id);
    }

    private void verificarDadosAssociado(AssociadoCriarDTO dto) {
        if (!AssociadoUtils.verificaCPF(dto.getCpf())) {
            throw new AssociadoException("O CPF informado é inválido: " + dto.getCpf());
        }
    }
}
