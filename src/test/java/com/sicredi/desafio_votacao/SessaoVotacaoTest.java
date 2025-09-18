package com.sicredi.desafio_votacao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.sicredi.desafio_votacao.dto.SessaoVotacaoCriarDTO;
import com.sicredi.desafio_votacao.enums.StatusPauta;
import com.sicredi.desafio_votacao.exception.PautaException;
import com.sicredi.desafio_votacao.exception.SessaoVotacaoException;
import com.sicredi.desafio_votacao.model.Pauta;
import com.sicredi.desafio_votacao.model.SessaoVotacao;
import com.sicredi.desafio_votacao.repository.PautaRepository;
import com.sicredi.desafio_votacao.repository.SessaoVotacaoRepository;
import com.sicredi.desafio_votacao.repository.VotoRepository;
import com.sicredi.desafio_votacao.service.SessaoVotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class SessaoVotacaoTest {
    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    private Pauta pauta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pauta = Pauta.builder()
                .id(1L)
                .titulo("Pauta Teste")
                .status(StatusPauta.APROVADA)
                .build();
    }

    @Test
    void deveAbrirSessaoComSucesso() {
        // Arrange
        SessaoVotacaoCriarDTO dto = new SessaoVotacaoCriarDTO();
        dto.setPautaId(1L);
        dto.setDuracaoMinutos(5);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.existsByPautaId(1L)).thenReturn(false);
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SessaoVotacao sessao = sessaoVotacaoService.abrirSessao(dto);

        // Assert
        assertNotNull(sessao);
        assertEquals(pauta, sessao.getPauta());
        assertFalse(sessao.getEncerrada());
        assertTrue(sessao.getDataFechamento().isAfter(sessao.getDataAbertura()));

        verify(sessaoVotacaoRepository).save(any(SessaoVotacao.class));
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoExiste() {
        SessaoVotacaoCriarDTO dto = new SessaoVotacaoCriarDTO();
        dto.setPautaId(99L);

        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PautaException.class, () -> sessaoVotacaoService.abrirSessao(dto));
    }

    @Test
    void deveLancarExcecaoQuandoSessaoJaExiste() {
        SessaoVotacaoCriarDTO dto = new SessaoVotacaoCriarDTO();
        dto.setPautaId(1L);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.existsByPautaId(1L)).thenReturn(true);

        assertThrows(SessaoVotacaoException.class, () -> sessaoVotacaoService.abrirSessao(dto));
    }

    @Test
    void deveUsarDuracaoPadraoQuandoNaoInformado() {
        SessaoVotacaoCriarDTO dto = new SessaoVotacaoCriarDTO();
        dto.setPautaId(1L);
        dto.setDuracaoMinutos(null);

        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.existsByPautaId(1L)).thenReturn(false);
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SessaoVotacao sessao = sessaoVotacaoService.abrirSessao(dto);

        assertEquals(1, java.time.Duration.between(sessao.getDataAbertura(), sessao.getDataFechamento()).toMinutes());
    }
}
