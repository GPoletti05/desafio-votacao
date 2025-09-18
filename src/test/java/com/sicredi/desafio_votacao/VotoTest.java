package com.sicredi.desafio_votacao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sicredi.desafio_votacao.dto.VotoCriarDTO;
import com.sicredi.desafio_votacao.enums.EscolhaVoto;
import com.sicredi.desafio_votacao.exception.VotoException;
import com.sicredi.desafio_votacao.model.Associado;
import com.sicredi.desafio_votacao.model.Pauta;
import com.sicredi.desafio_votacao.model.SessaoVotacao;
import com.sicredi.desafio_votacao.model.Voto;
import com.sicredi.desafio_votacao.repository.AssociadoRepository;
import com.sicredi.desafio_votacao.repository.SessaoVotacaoRepository;
import com.sicredi.desafio_votacao.repository.VotoRepository;
import com.sicredi.desafio_votacao.service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
public class VotoTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private VotoService votoService;

    private SessaoVotacao sessao;
    private Associado associado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        associado = Associado.builder()
                .id(1L)
                .nome("Gabriel")
                .cpf("12345678900")
                .build();

        sessao = SessaoVotacao.builder()
                .id(1L)
                .pauta(Pauta.builder().id(10L).titulo("Pauta Teste").build())
                .dataAbertura(LocalDateTime.now().minusMinutes(1))
                .dataFechamento(LocalDateTime.now().plusMinutes(5))
                .encerrada(false)
                .build();
    }

    @Test
    void deveRegistrarVotoComSucesso() {
        // Arrange
        VotoCriarDTO dto = new VotoCriarDTO();
        dto.setSessaoId(1L);
        dto.setAssociadoId(1L);
        dto.setEscolha("SIM");

        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
        when(votoRepository.existsBySessaoVotacao_IdAndAssociado_Id(1L, 1L)).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Voto voto = votoService.registrarVoto(dto);

        // Assert
        assertNotNull(voto);
        assertEquals(EscolhaVoto.SIM, voto.getEscolha());
        assertEquals(associado, voto.getAssociado());
        assertEquals(sessao, voto.getSessaoVotacao());

        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void deveLancarExcecaoQuandoSessaoForaDoPrazo() {
        VotoCriarDTO dto = new VotoCriarDTO();
        dto.setSessaoId(1L);
        dto.setAssociadoId(1L);
        dto.setEscolha("NAO");

        SessaoVotacao sessaoFechada = SessaoVotacao.builder()
                .id(1L)
                .dataAbertura(LocalDateTime.now().minusMinutes(10))
                .dataFechamento(LocalDateTime.now().minusMinutes(5))
                .build();

        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoFechada));

        assertThrows(VotoException.class, () -> votoService.registrarVoto(dto));
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoNaoExiste() {
        VotoCriarDTO dto = new VotoCriarDTO();
        dto.setSessaoId(1L);
        dto.setAssociadoId(99L);
        dto.setEscolha("SIM");

        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        when(associadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(VotoException.class, () -> votoService.registrarVoto(dto));
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotou() {
        VotoCriarDTO dto = new VotoCriarDTO();
        dto.setSessaoId(1L);
        dto.setAssociadoId(1L);
        dto.setEscolha("SIM");

        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        when(associadoRepository.findById(1L)).thenReturn(Optional.of(associado));
        when(votoRepository.existsBySessaoVotacao_IdAndAssociado_Id(1L, 1L)).thenReturn(true);

        assertThrows(VotoException.class, () -> votoService.registrarVoto(dto));
    }
}
