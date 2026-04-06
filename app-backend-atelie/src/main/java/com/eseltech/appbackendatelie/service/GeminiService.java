package com.eseltech.appbackendatelie.service;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final String API_KEY = "AIzaSyDcMx8SaQgnVSTqcA8p6daOX792RUkf2BM";
    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    public String consultar(String prompt) {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        API_URL + API_KEY,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        return response.getBody();
    }
}
