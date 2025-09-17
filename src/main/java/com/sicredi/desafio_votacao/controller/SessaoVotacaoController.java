package com.sicredi.desafio_votacao.controller;

import com.sicredi.desafio_votacao.dto.ResponseDTO;
import com.sicredi.desafio_votacao.dto.SessaoVotacaoCriarDTO;
import com.sicredi.desafio_votacao.exception.SessaoVotacaoException;
import com.sicredi.desafio_votacao.model.SessaoVotacao;
import com.sicredi.desafio_votacao.service.SessaoVotacaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessoes")
@RequiredArgsConstructor
@Slf4j
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    @PostMapping
    public ResponseEntity<ResponseDTO<SessaoVotacao>> abrirSessao(@Valid @RequestBody SessaoVotacaoCriarDTO dto) {
        SessaoVotacao sessao = sessaoVotacaoService.abrirSessao(dto);
        return ResponseEntity.ok(
                ResponseDTO.<SessaoVotacao>builder()
                        .mensagem("Sessão de votação criada com sucesso!")
                        .objeto(sessao)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoVotacao> buscarPorId(@PathVariable @NotNull Long id) {
        return sessaoVotacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pauta/{pautaId}")
    public ResponseEntity<SessaoVotacao> buscarPorPauta(@PathVariable @NotNull Long pautaId) {
        return sessaoVotacaoService.buscarPorPauta(pautaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SessaoVotacao>> listarTodas() {
        List<SessaoVotacao> sessoes = sessaoVotacaoService.listarTodas();
        return ResponseEntity.ok(sessoes);
    }

    /**
     * Endpoint para encerrar uma sessão de votação
     * @param sessaoId ID da sessão
     * @return Sessão encerrada com status atualizado da pauta
     */
    @PutMapping("/{sessaoId}/encerrar")
    public ResponseEntity<ResponseDTO<SessaoVotacao>> encerrarSessao(@PathVariable Long sessaoId) {
        try {
            SessaoVotacao sessaoEncerrada = sessaoVotacaoService.encerrarSessao(sessaoId);

            ResponseDTO<SessaoVotacao> response = ResponseDTO.<SessaoVotacao>builder()
                    .mensagem("Sessão encerrada com sucesso e status da pauta atualizado")
                    .objeto(sessaoEncerrada)
                    .build();

            return ResponseEntity.ok(response);

        } catch (SessaoVotacaoException e) {
            log.error("Erro ao encerrar sessão: {}", e.getMessage());
            ResponseDTO<SessaoVotacao> response = ResponseDTO.<SessaoVotacao>builder()
                    .mensagem("Erro ao encerrar sessão: " + e.getMessage())
                    .objeto(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
