package com.eseltech.appbackendatelie.service;

import com.twilio.http.TwilioRestClient;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.previewmessaging.v1.Message.CreateMessagesRequest;

import java.net.URI;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class TwilioService {
    private static final String ACCOUNT_SID = "AC9193385d8426f42dfc279fec424f331c";
    private static final String AUTH_TOKEN = "b38ede21ebb1691a96e2ee0137a6c6f4";
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

