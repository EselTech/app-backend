package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.NotificacaoDTO;
import com.eseltech.appbackendatelie.entity.Notificacao;
import com.eseltech.appbackendatelie.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "API para gerenciamento de notificações do ateliê")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @Operation(
            summary = "Listar todas as notificações",
            description = "Retorna uma lista com todas as notificações cadastradas no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de notificações retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notificacao.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhuma notificação encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Sem notificações",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Nenhuma notificação encontrada\",\"path\":\"/notificacoes\"}"
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Notificacao>> findAll() {
        return ResponseEntity.ok(notificacaoService.findAll());
    }

    @Operation(
            summary = "Buscar notificação por ID",
            description = "Retorna uma notificação específica baseada no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificação encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notificacao.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificação não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Notificação não encontrada",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Notificação não encontrada com id: 1\",\"path\":\"/notificacoes/1\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacao> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacaoService.findById(id));
    }

    @Operation(
            summary = "Deletar notificação",
            description = "Remove uma notificação do sistema baseada no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Notificação removida com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificação não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Notificação não encontrada",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Notificação não encontrada com id: 1\",\"path\":\"/notificacoes/1\"}"
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        notificacaoService.removerNotificacao(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Cadastrar nova notificação",
            description = "Cria uma nova notificação no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificação cadastrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notificacao.class),
                            examples = @ExampleObject(
                                    name = "Notificação cadastrada",
                                    value = "{\"id\":1,\"empresa\":{\"id\":1},\"topico\":\"Novo Pedido\",\"mensagem\":\"Um novo pedido foi realizado para sua empresa.\",\"dtEnvio\":\"2026-05-10T14:30:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou empresa não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Erro de validação",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/notificacoes\"}"
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Notificacao> saveNotificacao(@RequestBody @Valid NotificacaoDTO notificacaoDTO) {
        return ResponseEntity.ok(notificacaoService.salvarNotificacao(notificacaoDTO));
    }

    @Operation(
            summary = "Atualizar notificação",
            description = "Atualiza os dados de uma notificação existente baseada no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notificação atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notificacao.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Erro de validação",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/notificacoes/1\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificação não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Notificação não encontrada",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Notificação não encontrada com id: 1\",\"path\":\"/notificacoes/1\"}"
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Notificacao> updateNotificacao(@PathVariable Integer id, @RequestBody @Valid NotificacaoDTO notificacaoDTO) {
        return ResponseEntity.ok(notificacaoService.atualizarNotificacao(id, notificacaoDTO));
    }
}

