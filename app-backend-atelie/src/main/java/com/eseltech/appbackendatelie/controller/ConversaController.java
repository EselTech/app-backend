package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.ConversaDTO;
import com.eseltech.appbackendatelie.service.ConversaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversa")
@Tag(name = "Conversas Chatbot", description = "Gerenciamento do histórico de conversas com o Gemini")
public class ConversaController {

    @Autowired
    private ConversaService service;

    @PostMapping
    @Operation(summary = "Salva uma nova mensagem no histórico", description = "Registra uma mensagem (usuário ou sistema) no banco de dados")
    public ResponseEntity<ConversaDTO> salvar(@RequestBody ConversaDTO dto) {
        return ResponseEntity.ok(service.salvarMensagem(dto));
    }

    @GetMapping("/{empresaId}")
    @Operation(summary = "Busca histórico de conversas", description = "Retorna todas as mensagens cadastradas para uma empresa específica")
    public ResponseEntity<List<ConversaDTO>> listarPorEmpresa(@PathVariable Integer empresaId) {
        return ResponseEntity.ok(service.buscarHistorico(empresaId));
    }
}