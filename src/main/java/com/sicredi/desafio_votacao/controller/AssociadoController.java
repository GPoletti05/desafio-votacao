package com.sicredi.desafio_votacao.controller;


import com.sicredi.desafio_votacao.dto.AssociadoCriarDTO;
import com.sicredi.desafio_votacao.dto.ResponseDTO;
import com.sicredi.desafio_votacao.model.Associado;
import com.sicredi.desafio_votacao.service.AssociadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associados")
@RequiredArgsConstructor
public class AssociadoController {
    private final AssociadoService associadoService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Associado>> criarAssociado(@Valid @RequestBody AssociadoCriarDTO associadoCriarDTO) {
        Associado criada = associadoService.criarAssociado(associadoCriarDTO);

        ResponseDTO<Associado> response = ResponseDTO.<Associado>builder()
                .mensagem("Associado criado com sucesso!")
                .objeto(criada)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Associado>> listarTodas() {
        List<Associado> associados = associadoService.listarTodas();
        return ResponseEntity.ok(associados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Associado> buscarPorId(@PathVariable Long id) {
        return associadoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Associado> buscarPorCpf(@PathVariable String cpf) {
        return associadoService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAssociado(@PathVariable Long id) {
        try {
            associadoService.deletarAssociado(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
