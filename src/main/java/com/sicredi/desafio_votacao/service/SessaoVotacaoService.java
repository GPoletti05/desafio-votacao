package com.sicredi.desafio_votacao.service;

import com.sicredi.desafio_votacao.dto.AssociadoCriarDTO;
import com.sicredi.desafio_votacao.dto.SessaoVotacaoCriarDTO;
import com.sicredi.desafio_votacao.enums.EscolhaVoto;
import com.sicredi.desafio_votacao.enums.StatusPauta;
import com.sicredi.desafio_votacao.exception.PautaException;
import com.sicredi.desafio_votacao.exception.SessaoVotacaoException;
import com.sicredi.desafio_votacao.model.Pauta;
import com.sicredi.desafio_votacao.model.SessaoVotacao;
import com.sicredi.desafio_votacao.model.Voto;
import com.sicredi.desafio_votacao.repository.PautaRepository;
import com.sicredi.desafio_votacao.repository.SessaoVotacaoRepository;
import com.sicredi.desafio_votacao.repository.VotoRepository;
import com.sicredi.desafio_votacao.util.AssociadoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;

    // property de duração maxima da sessão.
    @Value("${sessao.duracao-maxima-minutos:#{null}}")
    private Integer maxDurationMinutes;

    public SessaoVotacao abrirSessao(SessaoVotacaoCriarDTO dto) {

        // verifica se a sessão informada existe.
        Pauta pauta = pautaRepository.findById(dto.getPautaId())
                .orElseThrow(() -> new PautaException("Pauta não encontrada com ID: " + dto.getPautaId()));

        // verifica se já existe uma sessão cadastrada para a pauta.
        if (sessaoVotacaoRepository.existsByPautaId(dto.getPautaId())) {
            log.warn("Tentativa de abrir nova sessão para pauta {} que já possui sessão", dto.getPautaId());
            throw new SessaoVotacaoException("Já existe uma sessão de votação para esta pauta, tente criar outra!.");
        }

        // se não informar duração, assume 1 minuto
        int duracao = (dto.getDuracaoMinutos() == null || dto.getDuracaoMinutos() <= 0)
                ? 1
                : dto.getDuracaoMinutos();

        log.info("Duração da sessão: {} minutos", dto.getDuracaoMinutos());

        // validar limite máximo
        if (maxDurationMinutes != null && duracao > maxDurationMinutes) {
            log.warn("Duração informada ({}) excede o limite máximo permitido ({} minutos)", dto.getDuracaoMinutos(), maxDurationMinutes);
            throw new SessaoVotacaoException("A duração máxima permitida da sessão é de " + maxDurationMinutes + " minutos.");
        }

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(duracao);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .dataAbertura(inicio)
                .dataFechamento(fim)
                .encerrada(false)
                .build();

        SessaoVotacao salva = sessaoVotacaoRepository.save(sessao);
        log.info("Sessão de votação criada com sucesso para pauta {}. Início: {}, Fim: {}", pauta.getId(), inicio, fim);
        return salva;
    }

    public Optional<SessaoVotacao> buscarPorId(Long id) {
        return sessaoVotacaoRepository.findById(id);
    }

    public Optional<SessaoVotacao> buscarPorPauta(Long pautaId) {
        return sessaoVotacaoRepository.findByPauta_Id(pautaId);
    }

    public List<SessaoVotacao> listarTodas() {
        return sessaoVotacaoRepository.findAll();
    }

    public void deletarSessaoVotacao(Long id) {
        if (!sessaoVotacaoRepository.existsById(id)) {
            throw new SessaoVotacaoException("Sessão não encontrada com id: " + id);
        }
        sessaoVotacaoRepository.deleteById(id);
    }

    public SessaoVotacao encerrarSessao(Long sessaoId) {

        SessaoVotacao sessao = sessaoVotacaoRepository.findById(sessaoId)
                .orElseThrow(() -> new SessaoVotacaoException("Sessão não encontrada para ID: " + sessaoId));

        if (sessao.getEncerrada()) {
            log.warn("Sessão {} já foi encerrada.", sessaoId);
            throw new SessaoVotacaoException("Sessão já foi encerrada anteriormente.");
        }

        LocalDateTime agora = LocalDateTime.now();
        if (agora.isBefore(sessao.getDataAbertura())) {
            throw new SessaoVotacaoException("Sessão ainda não iniciou, não pode ser encerrada.");
        }

        if (sessao.getDataFechamento().isAfter(agora)) {
            log.info("Encerrando sessão antes do horário previsto: {}", sessao.getDataFechamento());
            sessao.setDataFechamento(agora); // Encerramento manual
        }

        // Buscar votos
        List<Voto> votos = votoRepository.findBySessaoVotacao_Id(sessaoId);
        if(votos.isEmpty()){
            log.warn("Nenhum voto registrado, mas encerrando a sessão {}, mesmo assim.", sessaoId);
        }
        long totalSim = votos.stream()
                .filter(v -> v.getEscolha() == EscolhaVoto.SIM)
                .count();
        long totalNao = votos.stream()
                .filter(v -> v.getEscolha() == EscolhaVoto.NAO)
                .count();

        // Definir status da pauta (so será considerado aprovada se for voto da maioria, não havendo empate)
        Pauta pauta = sessao.getPauta();
        if (totalSim > totalNao) {
            pauta.setStatus(StatusPauta.APROVADA);
        } else {
            pauta.setStatus(StatusPauta.REJEITADA);
        }

        // Salvar alterações
        sessao.setEncerrada(true);
        pautaRepository.save(pauta);
        SessaoVotacao salva = sessaoVotacaoRepository.save(sessao);

        log.info("Sessão {} encerrada. Status da pauta '{}' atualizado para {}", sessaoId, pauta.getTitulo(), pauta.getStatus().getValor());

        return salva;
    }
}

