package com.sicredi.desafio_votacao.controller;

import com.sicredi.desafio_votacao.dto.PautaCriarDTO;
import com.sicredi.desafio_votacao.dto.ResponseDTO;
import com.sicredi.desafio_votacao.model.Pauta;
import com.sicredi.desafio_votacao.service.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Pauta>> criarPauta(@Valid @RequestBody PautaCriarDTO pauta) {
        Pauta criada = pautaService.criarPauta(pauta);

        ResponseDTO<Pauta> response = ResponseDTO.<Pauta>builder()
                .mensagem("Pauta criada com sucesso!")
                .objeto(criada)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pauta>> listarTodas() {
        List<Pauta> pautas = pautaService.listarTodas();
        return ResponseEntity.ok(pautas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pauta> buscarPorId(@PathVariable Long id) {
        return pautaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPauta(@PathVariable Long id) {
        try {
            pautaService.deletarPauta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
