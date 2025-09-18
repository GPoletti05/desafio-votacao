package com.sicredi.desafio_votacao.controller;

import com.sicredi.desafio_votacao.dto.ResponseDTO;
import com.sicredi.desafio_votacao.dto.StatusVotoDTO;
import com.sicredi.desafio_votacao.dto.VotoCriarDTO;
import com.sicredi.desafio_votacao.model.Voto;
import com.sicredi.desafio_votacao.service.FakeCpfService;
import com.sicredi.desafio_votacao.service.VotoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cpf")
@RequiredArgsConstructor
public class CpfController {

    private final FakeCpfService fakeCpfService;

    @GetMapping("/{cpf}")
    public ResponseEntity<StatusVotoDTO> verificarCpf(@PathVariable String cpf) {
        StatusVotoDTO response = fakeCpfService.verificarCpf(cpf);
        return ResponseEntity.ok(response);
    }

}
