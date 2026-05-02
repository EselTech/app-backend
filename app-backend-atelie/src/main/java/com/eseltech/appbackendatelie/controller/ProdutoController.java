package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.ProdutoDTO;
import com.eseltech.appbackendatelie.DTO.request.SimularPrecoRequestDTO;
import com.eseltech.appbackendatelie.DTO.response.SimularPrecoResponseDTO;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.service.ProdutoService;
import com.eseltech.appbackendatelie.service.SimulacaoService;
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
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos do ateliê")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private SimulacaoService simulacaoService;

    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista com todos os produtos cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Nenhum produto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Sem produtos",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Nenhum produto encontrado\",\"path\":\"/produtos\"}"
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        return ResponseEntity.ok(produtoService.findAll());
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna um produto específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Produto não encontrado",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Produto não encontrado com id: 1\",\"path\":\"/produtos/1\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @Operation(
            summary = "Deletar produto",
            description = "Remove um produto do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Produto removido com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Produto não encontrado",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Produto não encontrado com id: 1\",\"path\":\"/produtos/1\"}"
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Simular preço de produto",
            description = "Calcula o preço sugerido de venda baseado nos custos de materiais, mão de obra e margem de lucro"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Simulação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimularPrecoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos para simulação",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Erro de validação",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/produtos/simular-preco\"}"
                            )
                    )
            )
    })
    @PostMapping("/simular-preco")
    public ResponseEntity<SimularPrecoResponseDTO> simularPreco(@RequestBody @Valid SimularPrecoRequestDTO simularPrecoRequestDTO) {
        SimularPrecoResponseDTO response = simulacaoService.simularPreco(simularPrecoRequestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cadastrar novo produto",
            description = "Cria um novo produto no sistema com os dados fornecidos, incluindo cálculo automático de custos e preços"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class),
                            examples = @ExampleObject(
                                    name = "Produto cadastrado",
                                    value = "{\"id\":1,\"empresa\":{\"id\":1},\"nome\":\"Sacola temática\",\"descricao\":\"Agendas para anotações escolares\",\"custo\":15.50,\"preco\":25.90,\"listaMateriais\":[]}"
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
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/produtos\"}"
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Produto> saveProduto(@RequestBody @Valid ProdutoDTO produto) {
        return ResponseEntity.ok(produtoService.salvarProduto(produto));
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente baseado no ID fornecido, recalculando custos e preços"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Erro de validação",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":400,\"error\":\"Erro de Validação\",\"message\":\"Um ou mais campos estão inválidos\",\"path\":\"/produtos/1\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produto não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Produto não encontrado",
                                    value = "{\"timestamp\":\"2026-05-01T10:00:00Z\",\"status\":404,\"error\":\"Recurso não encontrado\",\"message\":\"Produto não encontrado com id: 1\",\"path\":\"/produtos/1\"}"
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produto) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produto));
    }
}
