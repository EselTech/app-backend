package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.Notificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.List;

@Service
public class TwilioService {
    @Autowired
    private NotificacaoService notificacaoService;

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth-token}")
    private  String AUTH_TOKEN;

    private static final PhoneNumber FROM_NUMBER = new PhoneNumber("whatsapp:+14155238886");
    private static final PhoneNumber TO_NUMBER = new PhoneNumber("whatsapp:+5511967140472");

    @Scheduled(cron = "0 * * * * ?") // Executa a cada 5 minutos
    public void buscarNotificacoes() {

        List<Notificacao> notificacoesAEnviar = notificacaoService.buscarNotificacoesAEnviar();

        if (notificacoesAEnviar.isEmpty()) {
            System.out.println("Nenhuma notificação para enviar.");
            return;
        }

        for (Notificacao notificacao : notificacoesAEnviar) {
            try {
                sendMessage(notificacao.getMensagem());
                notificacao.setDtEnvio(java.time.LocalDateTime.now());
                notificacaoService.atualizarNotificacao(notificacao.getId(), notificacao);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(String textoMensagem) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                TO_NUMBER,
                FROM_NUMBER,
                textoMensagem
        ).create();

        System.out.println("Mensagem enviada: " + message.getBody());
    }
}

