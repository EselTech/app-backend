package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.PedidoDTO;
import com.eseltech.appbackendatelie.entity.Pedido;
import com.eseltech.appbackendatelie.service.PedidoService;
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
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos do ateliê")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(
            summary = "Listar todos os pedidos",
            description = "Retorna uma lista com todos os pedidos cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum pedido encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Sem pedidos",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Nenhum pedido encontrado\",\"path\":\"/pedidos\"}"
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Pedido>> findAll() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna um pedido específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Pedido não encontrado",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Pedido não encontrado com id: 1\",\"path\":\"/pedidos/1\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.findById(id));
    }

    @Operation(
            summary = "Deletar pedido",
            description = "Remove um pedido do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pedido removido com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Pedido não encontrado",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Pedido não encontrado com id: 1\",\"path\":\"/pedidos/1\"}"
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        pedidoService.removerPedido(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Cadastrar novo pedido",
            description = "Cria um novo pedido no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class),
                            examples = @ExampleObject(
                                    name = "Pedido cadastrado",
                                    value = "{\"id\":1,\"empresa\":{\"id\":1},\"nome\":\"Pedido de Agendas - Sirlene\",\"descricao\":\"Pedido de 50x agendas escolares para a escola Frei Caneca realizado por Sirlene\",\"valor\":92.90,\"status\":\"Em andamento\",\"prazo\":\"2026-04-15\",\"listaProdutos\":[]}"
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
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/pedidos\"}"
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Pedido> savePedido(@RequestBody @Valid PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.salvarPedido(pedidoDTO));
    }

    @Operation(
            summary = "Atualizar pedido",
            description = "Atualiza os dados de um pedido existente baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Erro de validação",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/pedidos/1\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Pedido não encontrado",
                                    value = "{\"timestamp\":\"2026-05-10T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Pedido não encontrado com id: 1\",\"path\":\"/pedidos/1\"}"
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Integer id, @RequestBody @Valid PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.atualizarPedido(id, pedidoDTO));
    }
}

