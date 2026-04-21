package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.entity.ShopeeTokenInfo;
import com.eseltech.appbackendatelie.service.ShopeeTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/shopee/tokens")
@Tag(name = "Shopee Token", description = "Endpoints para gerenciamento de tokens da API Shopee")
public class ShopeeTokenController {

    private final ShopeeTokenService shopeeTokenService;

    @Autowired
    public ShopeeTokenController(ShopeeTokenService shopeeTokenService) {
        this.shopeeTokenService = shopeeTokenService;
    }

    @GetMapping("/{shopId}/access-token")
    @Operation(summary = "Obter access token válido",
               description = "Retorna um access token válido, renovando automaticamente se necessário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access token retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Credenciais não encontradas para a loja"),
        @ApiResponse(responseCode = "500", description = "Erro ao renovar token")
    })
    public ResponseEntity<Map<String, String>> getAccessToken(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId) {

        String accessToken = shopeeTokenService.getValidAccessToken(shopId);

        Map<String, String> response = new HashMap<>();
        response.put("shopId", shopId.toString());
        response.put("accessToken", accessToken);
        response.put("status", "valid");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Cadastrar credenciais",
               description = "Cadastra ou atualiza as credenciais de acesso da Shopee para uma loja")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Credenciais cadastradas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ShopeeTokenInfo> saveTokenInfo(@RequestBody ShopeeTokenInfo tokenInfo) {
        ShopeeTokenInfo saved = shopeeTokenService.saveTokenInfo(tokenInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "Consultar credenciais",
               description = "Retorna as informações de credenciais cadastradas para uma loja")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credenciais encontradas"),
        @ApiResponse(responseCode = "404", description = "Credenciais não encontradas")
    })
    public ResponseEntity<ShopeeTokenInfo> getTokenInfo(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId) {

        ShopeeTokenInfo tokenInfo = shopeeTokenService.getTokenInfo(shopId);
        return ResponseEntity.ok(tokenInfo);
    }

    @GetMapping("/{shopId}/exists")
    @Operation(summary = "Verificar existência de credenciais",
               description = "Verifica se existem credenciais cadastradas para uma loja")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso")
    })
    public ResponseEntity<Map<String, Boolean>> checkTokenExists(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId) {

        boolean exists = shopeeTokenService.hasTokenInfo(shopId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{shopId}")
    @Operation(summary = "Remover credenciais",
               description = "Remove as credenciais cadastradas para uma loja")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Credenciais removidas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Credenciais não encontradas")
    })
    public ResponseEntity<Void> deleteTokenInfo(
            @Parameter(description = "ID da loja na Shopee", required = true)
            @PathVariable Long shopId) {

        shopeeTokenService.deleteTokenInfo(shopId);
        return ResponseEntity.noContent().build();
    }
}

