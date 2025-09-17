package com.sicredi.desafio_votacao.service;

import com.sicredi.desafio_votacao.dto.VotoCriarDTO;
import com.sicredi.desafio_votacao.enums.EscolhaVoto;
import com.sicredi.desafio_votacao.exception.VotoException;
import com.sicredi.desafio_votacao.model.Associado;
import com.sicredi.desafio_votacao.model.SessaoVotacao;
import com.sicredi.desafio_votacao.model.Voto;
import com.sicredi.desafio_votacao.repository.AssociadoRepository;
import com.sicredi.desafio_votacao.repository.SessaoVotacaoRepository;
import com.sicredi.desafio_votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final AssociadoRepository associadoRepository;

    public Voto registrarVoto(VotoCriarDTO votoCriarDTO) {

        // Verifica se o voto é valido.
        EscolhaVoto escolhaVoto = EscolhaVoto.fromString(votoCriarDTO.getEscolha());

        // Validar sessão
        SessaoVotacao sessao = sessaoVotacaoRepository.findById(votoCriarDTO.getSessaoId())
                .orElseThrow(() -> new VotoException("Sessão não encontrada para ID: " + votoCriarDTO.getSessaoId()));

        // Verificar se a sessão está ativa
        LocalDateTime agora = LocalDateTime.now();
        if (agora.isBefore(sessao.getDataAbertura()) || agora.isAfter(sessao.getDataFechamento())) {
            log.warn("Tentativa de votar na sessão {}, fora do prazo.", sessao.getId());
            throw new VotoException("Sessão não está ativa para votação.");
        }

        // Validar associado
        Associado associado = associadoRepository.findById(votoCriarDTO.getAssociadoId())
                .orElseThrow(() -> new VotoException("Associado não encontrado para ID: " + votoCriarDTO.getAssociadoId()));

        // Verificar se já votou
        if (votoRepository.existsBySessaoVotacao_IdAndAssociado_Id(sessao.getId(), associado.getId())) {
            log.warn("Associado {} tentou votar novamente na sessão {}", sessao.getId(), associado.getId());
            throw new VotoException("Associado já votou nesta sessão.");
        }

        // Criar e salvar voto
        Voto voto = Voto.builder()
                .sessaoVotacao(sessao)
                .associado(associado)
                .escolha(escolhaVoto)
                .dataVoto(LocalDateTime.now())
                .build();

        Voto salvo = votoRepository.save(voto);
        log.info("Voto registrado com sucesso para Associado {} na Sessão {}", associado.getId(), sessao.getId());

        return salvo;
    }

    public List<Voto> listarPorSessao(Long sessaoId) {

        if (!sessaoVotacaoRepository.existsById(sessaoId)) {
            throw new VotoException("Sessão não encontrada para ID: " + sessaoId);
        }

        return votoRepository.findBySessaoVotacao_Id(sessaoId);
    }

    public Optional<Voto> buscarPorId(Long id) {
        return votoRepository.findById(id);
    }


}
