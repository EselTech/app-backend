package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.MaterialDTO;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.service.MaterialService;
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
@RequestMapping("/materiais")
@Tag(name = "Materiais", description = "API para gerenciamento de materiais do ateliê")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Operation(
            summary = "Listar todos os materiais",
            description = "Retorna uma lista com todos os materiais cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de materiais retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Material.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum material encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Sem materiais",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Nenhum material encontrado\",\"path\":\"/materiais\"}"
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Material>> findAll() {
        return ResponseEntity.ok(materialService.findAll());
    }

    @Operation(
            summary = "Buscar material por ID",
            description = "Retorna um material específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Material encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Material.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Material não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Material não encontrado",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Material não encontrado com id: 1\",\"path\":\"/materiais/1\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Material> getById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }

    @Operation(
            summary = "Deletar material",
            description = "Remove um material do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Material removido com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Material não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Material não encontrado",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Material não encontrado com id: 1\",\"path\":\"/materiais/1\"}"
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        materialService.removerMaterial(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Cadastrar novo material",
            description = "Cria um novo material no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Material cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Material.class),
                            examples = @ExampleObject(
                                    name = "Material cadastrado",
                                    value = "{\"id\":1,\"empresa\":{\"id\":1},\"categoria\":\"CENTIMETRO\",\"nome\":\"Papel cartão vermelho\",\"descricao\":\"Papel cartão da cor vermelha geralmente usado para fazer decorações\",\"qtdEstoque\":50.00,\"preco\":12.90}"
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
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/materiais\"}"
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Material> saveMaterial(@RequestBody @Valid MaterialDTO materialDTO) {
        return ResponseEntity.ok(materialService.salvarMaterial(materialDTO));
    }

    @Operation(
            summary = "Atualizar material",
            description = "Atualiza os dados de um material existente baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Material atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Material.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Erro de validação",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/materiais/1\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Material não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Material não encontrado",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Material não encontrado com id: 1\",\"path\":\"/materiais/1\"}"
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Long id, @RequestBody @Valid MaterialDTO materialDTO) {
        return ResponseEntity.ok(materialService.atualizarMaterial(id, materialDTO));
    }

}
