package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.service.GeminiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ia")
public class GeminiController {

    private final GeminiService service;

    public GeminiController(GeminiService service) {
        this.service = service;
    }

    @PostMapping
    public String perguntar(@RequestBody Map<String, String> body) {
        return service.consultar(body.get("pergunta"));
    }
}
