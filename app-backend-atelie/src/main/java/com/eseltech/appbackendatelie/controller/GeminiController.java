package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ia")
public class GeminiController {

    private final GeminiService service;

    public GeminiController(GeminiService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> perguntar(@RequestParam String prompt) {
        return ResponseEntity.ok(service.perguntar(prompt));
    }
}
