package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Notificacao;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.NotificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Notificacao notificacao;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1);
        empresa.setRazaoSocial("Empresa Teste");
        empresa.setCnpj("12345678901234");

        notificacao = new Notificacao();
        notificacao.setId(1);
        notificacao.setEmpresa(empresa);
        notificacao.setTopico("Teste");
        notificacao.setMensagem("Mensagem de teste");
        notificacao.setDtEnvio(null);
    }

    @Test
    void findAll_DeveRetornarListaDeNotificacoes() {
        // Arrange
        when(notificacaoRepository.findAll()).thenReturn(List.of(notificacao));

        // Act
        List<Notificacao> resultado = notificacaoService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Mensagem de teste", resultado.getFirst().getMensagem());
        verify(notificacaoRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveLancarExcecao_QuandoListaVazia() {
        // Arrange
        when(notificacaoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
            notificacaoService.findAll()
        );

        assertEquals("Nenhuma notificação encontrada", exception.getMessage());
    }

    @Test
    void findById_DeveRetornarNotificacao() {
        // Arrange
        when(notificacaoRepository.findById(1)).thenReturn(Optional.of(notificacao));

        // Act
        Notificacao resultado = notificacaoService.findById(1);

        // Assert
        assertNotNull(resultado);
        assertEquals("Mensagem de teste", resultado.getMensagem());
        verify(notificacaoRepository, times(1)).findById(1);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(notificacaoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
            notificacaoService.findById(999)
        );

        assertTrue(exception.getMessage().contains("Notificação não encontrada com id: 999"));
    }

    @Test
    void removerNotificacao_DeveRemoverComSucesso() {
        // Arrange
        when(notificacaoRepository.findById(1)).thenReturn(Optional.of(notificacao));
        doNothing().when(notificacaoRepository).deleteById(1);

        // Act
        notificacaoService.removerNotificacao(1);

        // Assert
        verify(notificacaoRepository, times(1)).findById(1);
        verify(notificacaoRepository, times(1)).deleteById(1);
    }

    @Test
    void removerNotificacao_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(notificacaoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            notificacaoService.removerNotificacao(999)
        );

        verify(notificacaoRepository, never()).deleteById(anyInt());
    }

    @Test
    void salvarNotificacao_DeveSalvarComSucesso() {
        // Arrange
        Notificacao novaNotificacao = new Notificacao();
        novaNotificacao.setEmpresa(empresa);
        novaNotificacao.setTopico("Novo Tópico");
        novaNotificacao.setMensagem("Nova Mensagem");
        novaNotificacao.setDtEnvio(LocalDateTime.now());

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        // Act
        Notificacao resultado = notificacaoService.salvarNotificacao(novaNotificacao);

        // Assert
        assertNotNull(resultado);
        verify(empresaRepository, times(1)).findById(1);
        verify(notificacaoRepository, times(1)).save(any(Notificacao.class));
    }

    @Test
    void salvarNotificacao_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        Notificacao novaNotificacao = new Notificacao();
        novaNotificacao.setEmpresa(empresa);

        when(empresaRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
            notificacaoService.salvarNotificacao(novaNotificacao)
        );

        assertTrue(exception.getMessage().contains("Empresa não encontrada com id: 1"));
        verify(notificacaoRepository, never()).save(any(Notificacao.class));
    }

    @Test
    void atualizarNotificacao_DeveAtualizarComSucesso() {
        // Arrange
        Notificacao notificacaoAtualizada = new Notificacao();
        notificacaoAtualizada.setEmpresa(empresa);
        notificacaoAtualizada.setTopico("Tópico Atualizado");
        notificacaoAtualizada.setMensagem("Mensagem Atualizada");
        notificacaoAtualizada.setDtEnvio(LocalDateTime.now());

        when(notificacaoRepository.findById(1)).thenReturn(Optional.of(notificacao));
        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        // Act
        Notificacao resultado = notificacaoService.atualizarNotificacao(1, notificacaoAtualizada);

        // Assert
        assertNotNull(resultado);
        verify(notificacaoRepository, times(1)).findById(1);
        verify(empresaRepository, times(1)).findById(1);
        verify(notificacaoRepository, times(1)).save(any(Notificacao.class));
    }

    @Test
    void atualizarNotificacao_DeveLancarExcecao_QuandoNotificacaoNaoExiste() {
        // Arrange
        Notificacao notificacaoAtualizada = new Notificacao();
        notificacaoAtualizada.setEmpresa(empresa);

        when(notificacaoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            notificacaoService.atualizarNotificacao(999, notificacaoAtualizada)
        );

        verify(empresaRepository, never()).findById(anyInt());
        verify(notificacaoRepository, never()).save(any(Notificacao.class));
    }

    @Test
    void atualizarNotificacao_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        Notificacao notificacaoAtualizada = new Notificacao();
        notificacaoAtualizada.setEmpresa(empresa);

        when(notificacaoRepository.findById(1)).thenReturn(Optional.of(notificacao));
        when(empresaRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            notificacaoService.atualizarNotificacao(1, notificacaoAtualizada)
        );

        verify(notificacaoRepository, never()).save(any(Notificacao.class));
    }

    @Test
    void buscarNotificacoesAEnviar_DeveRetornarLista() {
        // Arrange
        List<Notificacao> notificacoes = List.of(notificacao);
        when(notificacaoRepository.findNotificacoesAEnviar()).thenReturn(notificacoes);

        // Act
        List<Notificacao> resultado = notificacaoService.buscarNotificacoesAEnviar();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository, times(1)).findNotificacoesAEnviar();
    }

    @Test
    void buscarNotificacoesAEnviar_DeveRetornarListaVazia() {
        // Arrange
        when(notificacaoRepository.findNotificacoesAEnviar()).thenReturn(new ArrayList<>());

        // Act
        List<Notificacao> resultado = notificacaoService.buscarNotificacoesAEnviar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(notificacaoRepository, times(1)).findNotificacoesAEnviar();
    }
}



