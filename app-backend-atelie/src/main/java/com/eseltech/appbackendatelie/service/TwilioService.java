package com.eseltech.appbackendatelie.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioService {
    @Value("${twilio.account.sid}")
    private static String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private static String AUTH_TOKEN;

    private static final PhoneNumber FROM_NUMBER = new PhoneNumber("whatsapp:+14155238886");
    private static final PhoneNumber TO_NUMBER = new PhoneNumber("whatsapp:+5511967140472");

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

