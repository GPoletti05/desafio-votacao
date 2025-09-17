package com.sicredi.desafio_votacao.controller;

import com.sicredi.desafio_votacao.dto.ResponseDTO;
import com.sicredi.desafio_votacao.dto.VotoCriarDTO;
import com.sicredi.desafio_votacao.model.Voto;
import com.sicredi.desafio_votacao.service.VotoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votos")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Voto>> registrarVoto(@Valid @RequestBody VotoCriarDTO votoCriarDTO) {
        Voto criado = votoService.registrarVoto(votoCriarDTO);

        ResponseDTO<Voto> response = ResponseDTO.<Voto>builder()
                .mensagem("Voto registrado com sucesso!")
                .objeto(criado)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/sessao/{sessaoId}")
    public ResponseEntity<List<Voto>> listarPorSessao(@PathVariable @NotNull Long sessaoId) {
        List<Voto> pautas = votoService.listarPorSessao(sessaoId);
        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voto> buscarPorId(@PathVariable Long id) {
        return votoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
