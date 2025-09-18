package com.sicredi.desafio_votacao;

import com.sicredi.desafio_votacao.dto.PautaCriarDTO;
import com.sicredi.desafio_votacao.enums.StatusPauta;
import com.sicredi.desafio_votacao.exception.PautaException;
import com.sicredi.desafio_votacao.model.Pauta;
import com.sicredi.desafio_votacao.repository.PautaRepository;
import com.sicredi.desafio_votacao.service.PautaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class PautaTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPautaComStatusAguardandoSessao() {
        // Arrange
        PautaCriarDTO dto = new PautaCriarDTO("Título Teste", "Motivo Teste");
        Pauta pauta = Pauta.builder()
                .id(1L)
                .titulo(dto.getTitulo())
                .motivo(dto.getMotivo())
                .dataCriacao(LocalDateTime.now())
                .status(StatusPauta.AGUARDANDO_SESSAO)
                .build();

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        // Act
        Pauta criada = pautaService.criarPauta(dto);

        // Assert
        assertNotNull(criada);
        assertEquals(StatusPauta.AGUARDANDO_SESSAO, criada.getStatus());
        assertEquals("Título Teste", criada.getTitulo());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    void deveBuscarPautaPorId() {
        // Arrange
        Pauta pauta = Pauta.builder().id(1L).titulo("Teste").motivo("Motivo").build();
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        // Act
        Optional<Pauta> result = pautaService.buscarPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Teste", result.get().getTitulo());
        verify(pautaRepository, times(1)).findById(1L);
    }

    @Test
    void deveListarTodasAsPautas() {
        // Arrange
        List<Pauta> pautas = Arrays.asList(
                Pauta.builder().id(1L).titulo("Pauta 1").build(),
                Pauta.builder().id(2L).titulo("Pauta 2").build()
        );
        when(pautaRepository.findAll()).thenReturn(pautas);

        // Act
        List<Pauta> result = pautaService.listarTodas();

        // Assert
        assertEquals(2, result.size());
        verify(pautaRepository, times(1)).findAll();
    }

    @Test
    void deveDeletarPautaQuandoExistir() {
        // Arrange
        when(pautaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pautaRepository).deleteById(1L);

        // Act
        pautaService.deletarPauta(1L);

        // Assert
        verify(pautaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarPautaInexistente() {
        // Arrange
        when(pautaRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        PautaException ex = assertThrows(PautaException.class, () -> pautaService.deletarPauta(1L));
        assertEquals("Pauta não encontrada com id: 1", ex.getMessage());
        verify(pautaRepository, never()).deleteById(1L);
    }
}
