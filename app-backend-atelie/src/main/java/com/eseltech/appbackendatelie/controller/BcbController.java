package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.entity.Imposto;
import com.eseltech.appbackendatelie.service.ImpostoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelas operações relacionadas ao Banco Central do Brasil (BCB)
 * e gerenciamento de impostos.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/bcb")
@Tag(name = "BCB - Impostos", description = "Endpoints para gerenciamento de impostos e integração com o Banco Central do Brasil")
public class BcbController {
    @Autowired
    private ImpostoService impostoService;

    @Operation(
            summary = "Listar todos os impostos",
            description = "Retorna uma lista com todos os impostos cadastrados no sistema, " +
                    "incluindo o histórico mais recente de cada imposto com seus valores atualizados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de impostos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = HistoricoImpostoDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    @GetMapping("/find-all")
    public ResponseEntity<List<HistoricoImpostoDTO>> getAll() {
        return ResponseEntity.ok(impostoService.buscarTodos());
    }

    @Operation(
            summary = "Inserir novo imposto",
            description = "Cadastra um novo imposto no sistema. O imposto deve conter o nome " +
                    "e o código SGS (Sistema Gerenciador de Séries) do Banco Central para " +
                    "permitir a atualização automática dos valores."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Imposto inserido com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Salvo com sucesso!")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos fornecidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    @PostMapping("/inserir-imposto")
    public ResponseEntity<String> inserirImposto(
            @Parameter(
                    description = "Objeto contendo os dados do imposto a ser cadastrado",
                    required = true
            )
            @RequestBody Imposto imposto) {
        return ResponseEntity.ok(impostoService.inserirImposto(imposto));
    }

    @Operation(
            summary = "Atualizar valores dos impostos",
            description = "Busca os valores atualizados de todos os impostos cadastrados " +
                    "através da API do Banco Central do Brasil (BCB) e atualiza o histórico " +
                    "no banco de dados. Esta operação consulta a série temporal SGS do BCB " +
                    "para cada imposto cadastrado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Impostos atualizados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Atualizado com sucesso!")
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Serviço do BCB indisponível",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    @PostMapping("/atualizar-impostos")
    public ResponseEntity<String> atualizarImpostos() {
        impostoService.atualizarImpostos();
        return ResponseEntity.ok("Atualizado com sucesso!");
    }

    @Operation(
            summary = "Deletar imposto",
            description = "Remove um imposto do sistema pelo seu ID. " +
                    "Esta operação também remove todo o histórico associado ao imposto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Imposto deletado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "Imposto deletado com sucesso!")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Imposto não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    @DeleteMapping("/deletar-imposto/{id}")
    public ResponseEntity<String> deletarImposto(
            @Parameter(
                    description = "ID único do imposto a ser deletado",
                    required = true,
                    example = "1"
            )
            @PathVariable Integer id) {
        impostoService.deletarImposto(id);
        return ResponseEntity.ok("Imposto deletado com sucesso!");
    }

    @GetMapping("/find-all-impostos")
    public ResponseEntity<List<Imposto>> findAllImpostos() {
        List<Imposto> impostos = impostoService.buscarTodosImpostos();
        return ResponseEntity.ok(impostos);
    }

    @PutMapping("/atualizar-imposto/{id}")
    public ResponseEntity<String> atualizarImposto(
            @Parameter(
                    description = "Objeto contendo os dados do imposto a ser atualizado, incluindo o ID",
                    required = true
            )
            @RequestBody Imposto imposto, @PathVariable Integer id) {
        impostoService.atualizarImposto(imposto, id);
        return ResponseEntity.ok("Imposto atualizado com sucesso!");
    }
}