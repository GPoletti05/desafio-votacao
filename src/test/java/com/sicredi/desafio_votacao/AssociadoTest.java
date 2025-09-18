package com.sicredi.desafio_votacao;

import com.sicredi.desafio_votacao.dto.AssociadoCriarDTO;
import com.sicredi.desafio_votacao.exception.AssociadoException;
import com.sicredi.desafio_votacao.model.Associado;
import com.sicredi.desafio_votacao.repository.AssociadoRepository;
import com.sicredi.desafio_votacao.service.AssociadoService;
import com.sicredi.desafio_votacao.util.AssociadoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssociadoTest {
    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoService associadoService;

    private AssociadoCriarDTO dtoValido;
    private Associado associado;

    @BeforeEach
    void setUp() {
        dtoValido = AssociadoCriarDTO.builder()
                .nome("Gabriel")
                .cpf("12345678901") // CPF fictício, mas com formato válido
                .build();

        associado = Associado.builder()
                .id(1L)
                .nome("Gabriel")
                .cpf("12345678901")
                .build();
    }

    @Test
    void deveCriarAssociadoComSucesso() {
        // Mock da utilidade de CPF para sempre retornar true
        mockStatic(AssociadoUtils.class).when(() -> AssociadoUtils.verificaCPF(dtoValido.getCpf())).thenReturn(true);

        when(associadoRepository.save(any(Associado.class))).thenReturn(associado);

        Associado result = associadoService.criarAssociado(dtoValido);

        assertNotNull(result);
        assertEquals("Gabriel", result.getNome());
        assertEquals("12345678901", result.getCpf());

        verify(associadoRepository, times(1)).save(any(Associado.class));
    }

    @Test
    void deveBuscarAssociadoPorId() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));

        Optional<Associado> result = associadoService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("Gabriel", result.get().getNome());
    }

    @Test
    void deveBuscarAssociadoPorCpf() {
        when(associadoRepository.findByCpf("12345678901")).thenReturn(Optional.of(associado));

        Optional<Associado> result = associadoService.buscarPorCpf("12345678901");

        assertTrue(result.isPresent());
        assertEquals("Gabriel", result.get().getNome());
    }

    @Test
    void deveListarTodosAssociados() {
        when(associadoRepository.findAll()).thenReturn(Arrays.asList(associado));

        List<Associado> result = associadoService.listarTodas();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void deveDeletarAssociadoComSucesso() {
        when(associadoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(associadoRepository).deleteById(1L);

        assertDoesNotThrow(() -> associadoService.deletarAssociado(1L));

        verify(associadoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoTentarDeletarAssociadoInexistente() {
        when(associadoRepository.existsById(1L)).thenReturn(false);

        AssociadoException exception = assertThrows(AssociadoException.class, () ->
                associadoService.deletarAssociado(1L));

        assertEquals("Associado não encontrada com id: 1", exception.getMessage());
        verify(associadoRepository, never()).deleteById(anyLong());
    }
}
