package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.service.ShopeeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shopee")
@Tag(name = "Shopee API", description = "Endpoints para integração com a API da Shopee")
public class ShopeeApiController {

    private final ShopeeApiService shopeeApiService;

    @Autowired
    public ShopeeApiController(ShopeeApiService shopeeApiService) {
        this.shopeeApiService = shopeeApiService;
    }

    @GetMapping("/{shopId}/produtos")
    @Operation(summary = "Listar produtos da loja",
               description = "Retorna a lista de produtos cadastrados na loja Shopee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Loja não encontrada ou credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro ao comunicar com a API da Shopee")
    })
    public ResponseEntity<Map<String, Object>> listarProdutos(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId,
            @Parameter(description = "Offset para paginação")
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @Parameter(description = "Quantidade de itens por página")
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        Map<String, Object> produtos = shopeeApiService.buscarListaProdutos(shopId, offset, pageSize);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{shopId}/produtos/{itemId}")
    @Operation(summary = "Obter detalhes do produto",
               description = "Retorna as informações detalhadas de um produto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalhes do produto retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto ou loja não encontrados"),
        @ApiResponse(responseCode = "500", description = "Erro ao comunicar com a API da Shopee")
    })
    public ResponseEntity<Map<String, Object>> obterDetalhesProduto(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId,
            @Parameter(description = "ID do produto na Shopee", required = true)
            @PathVariable Long itemId) {

        Map<String, Object> detalhes = shopeeApiService.obterDetalhesProduto(shopId, itemId);
        return ResponseEntity.ok(detalhes);
    }

    @PutMapping("/{shopId}/produtos/{itemId}/estoque")
    @Operation(summary = "Atualizar estoque do produto",
               description = "Atualiza a quantidade em estoque de um produto na Shopee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estoque atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Produto ou loja não encontrados"),
        @ApiResponse(responseCode = "500", description = "Erro ao comunicar com a API da Shopee")
    })
    public ResponseEntity<Map<String, Object>> atualizarEstoque(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId,
            @Parameter(description = "ID do produto na Shopee", required = true)
            @PathVariable Long itemId,
            @Parameter(description = "Nova quantidade em estoque", required = true)
            @RequestBody Map<String, Integer> requestBody) {

        Integer stock = requestBody.get("stock");
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Campo 'stock' é obrigatório e deve ser maior ou igual a zero");
        }

        Map<String, Object> response = shopeeApiService.atualizarEstoque(shopId, itemId, stock);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shopId}/request")
    @Operation(summary = "Fazer requisição GET genérica",
               description = "Executa uma requisição GET customizada para a API da Shopee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Requisição executada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro ao comunicar com a API da Shopee")
    })
    public ResponseEntity<Map<String, Object>> fazerRequisicaoGet(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId,
            @Parameter(description = "Path da API (ex: /api/v2/product/get_category)", required = true)
            @RequestParam String path,
            @Parameter(description = "Parâmetros adicionais da requisição")
            @RequestParam(required = false) Map<String, String> params) {

        Map<String, Object> response = shopeeApiService.fazerRequisicaoGet(shopId, path, params);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{shopId}/request")
    @Operation(summary = "Fazer requisição POST genérica",
               description = "Executa uma requisição POST customizada para a API da Shopee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Requisição executada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro ao comunicar com a API da Shopee")
    })
    public ResponseEntity<Map<String, Object>> fazerRequisicaoPost(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId,
            @Parameter(description = "Path da API (ex: /api/v2/product/add_item)", required = true)
            @RequestParam String path,
            @Parameter(description = "Corpo da requisição", required = true)
            @RequestBody Map<String, Object> requestBody) {

        Map<String, Object> response = shopeeApiService.fazerRequisicaoPost(shopId, path, requestBody);
        return ResponseEntity.ok(response);
    }
}

