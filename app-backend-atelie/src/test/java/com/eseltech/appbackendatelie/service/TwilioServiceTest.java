package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.Notificacao;
import com.twilio.rest.api.v2010.account.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import com.twilio.Twilio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwilioServiceTest {

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private TwilioService twilioService;

    private Notificacao notificacao;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(twilioService, "ACCOUNT_SID", "test-account-sid");
        ReflectionTestUtils.setField(twilioService, "AUTH_TOKEN", "test-auth-token");

        notificacao = new Notificacao();
        notificacao.setId(1);
        notificacao.setMensagem("Mensagem de teste");
        notificacao.setDtEnvio(null);
    }

    @Test
    void buscarNotificacoes_DeveProcessarNotificacoes_QuandoExistemNotificacoes() {
        // Arrange
        List<Notificacao> notificacoes = List.of(notificacao);
        when(notificacaoService.buscarNotificacoesAEnviar()).thenReturn(notificacoes);
        when(notificacaoService.atualizarNotificacao(anyInt(), any(Notificacao.class))).thenReturn(notificacao);

        // Mock Twilio - usando spy para evitar chamadas reais
        TwilioService twilioServiceSpy = spy(twilioService);
        doNothing().when(twilioServiceSpy).sendMessage(anyString());

        // Act
        twilioServiceSpy.buscarNotificacoes();

        // Assert
        verify(notificacaoService, times(1)).buscarNotificacoesAEnviar();
        verify(twilioServiceSpy, times(1)).sendMessage("Mensagem de teste");
        verify(notificacaoService, times(1)).atualizarNotificacao(eq(1), any(Notificacao.class));
    }

    @Test
    void buscarNotificacoes_NaoDeveProcessar_QuandoListaVazia() {
        // Arrange
        when(notificacaoService.buscarNotificacoesAEnviar()).thenReturn(new ArrayList<>());

        TwilioService twilioServiceSpy = spy(twilioService);

        // Act
        twilioServiceSpy.buscarNotificacoes();

        // Assert
        verify(notificacaoService, times(1)).buscarNotificacoesAEnviar();
        verify(twilioServiceSpy, never()).sendMessage(anyString());
        verify(notificacaoService, never()).atualizarNotificacao(anyInt(), any(Notificacao.class));
    }

    @Test
    void buscarNotificacoes_DeveProcessarMultiplasNotificacoes() {
        // Arrange
        Notificacao notificacao2 = new Notificacao();
        notificacao2.setId(2);
        notificacao2.setMensagem("Segunda mensagem");
        notificacao2.setDtEnvio(null);

        List<Notificacao> notificacoes = List.of(notificacao, notificacao2);
        when(notificacaoService.buscarNotificacoesAEnviar()).thenReturn(notificacoes);
        when(notificacaoService.atualizarNotificacao(anyInt(), any(Notificacao.class))).thenReturn(notificacao);

        TwilioService twilioServiceSpy = spy(twilioService);
        doNothing().when(twilioServiceSpy).sendMessage(anyString());

        // Act
        twilioServiceSpy.buscarNotificacoes();

        // Assert
        verify(twilioServiceSpy, times(2)).sendMessage(anyString());
        verify(notificacaoService, times(2)).atualizarNotificacao(anyInt(), any(Notificacao.class));
    }

    @Test
    void buscarNotificacoes_DeveLancarExcecao_QuandoErroAoEnviar() {
        // Arrange
        List<Notificacao> notificacoes = List.of(notificacao);
        when(notificacaoService.buscarNotificacoesAEnviar()).thenReturn(notificacoes);

        TwilioService twilioServiceSpy = spy(twilioService);
        doThrow(new RuntimeException("Erro ao enviar mensagem"))
                .when(twilioServiceSpy).sendMessage(anyString());

        // Act & Assert
        assertThrows(RuntimeException.class, twilioServiceSpy::buscarNotificacoes);

        verify(notificacaoService, never()).atualizarNotificacao(anyInt(), any(Notificacao.class));
    }

    @Test
    void buscarNotificacoes_DeveAtualizarDataEnvio() {
        // Arrange
        List<Notificacao> notificacoes = List.of(notificacao);
        when(notificacaoService.buscarNotificacoesAEnviar()).thenReturn(notificacoes);
        when(notificacaoService.atualizarNotificacao(anyInt(), any(Notificacao.class)))
                .thenAnswer(invocation -> {
                    Notificacao not = invocation.getArgument(1);
                    assertNotNull(not.getDtEnvio());
                    return not;
                });

        TwilioService twilioServiceSpy = spy(twilioService);
        doNothing().when(twilioServiceSpy).sendMessage(anyString());

        // Act
        twilioServiceSpy.buscarNotificacoes();

        // Assert
        verify(notificacaoService, times(1)).atualizarNotificacao(eq(1), argThat(not ->
            not.getDtEnvio() != null
        ));
    }
}


