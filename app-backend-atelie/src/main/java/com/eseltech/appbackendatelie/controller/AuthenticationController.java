package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.AuthResponseDTO;
import com.eseltech.appbackendatelie.DTO.AuthenticationDTO;
import com.eseltech.appbackendatelie.DTO.RegisterDTO;
import com.eseltech.appbackendatelie.DTO.TokenPairDTO;
import com.eseltech.appbackendatelie.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * Controller responsável pela autenticação e registro de usuários.
 * Implementa o fluxo de autenticação JWT com HttpOnly Cookies.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "API para autenticação JWT - login e registro de usuários com HttpOnly Cookies")
public class AuthenticationController {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final Duration ACCESS_TOKEN_MAX_AGE = Duration.ofMinutes(15);
    private static final Duration REFRESH_TOKEN_MAX_AGE = Duration.ofDays(7);

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Realizar login",
            description = """
                    Autentica o usuário com username e senha, retornando tokens JWT em cookies HttpOnly.
                    
                    **Fluxo de autenticação:**
                    1. O usuário envia suas credenciais (username e senha)
                    2. O sistema valida as credenciais contra o banco de dados
                    3. Se válidas, são gerados dois tokens JWT:
                       - **Access Token**: válido por 15 minutos, usado para autenticação nas requisições
                       - **Refresh Token**: válido por 7 dias, usado para renovar o Access Token
                    4. Os tokens são retornados em cookies HttpOnly seguros
                    
                    **Cookies retornados:**
                    - `access_token`: Path="/", HttpOnly=true, Secure=true, SameSite=Strict
                    - `refresh_token`: Path="/auth/refresh", HttpOnly=true, Secure=true, SameSite=Strict
                    
                    **Importante:** Este endpoint é público e não requer autenticação prévia.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso - Tokens retornados em cookies HttpOnly",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Resposta de sucesso",
                                    value = "{\"message\": \"Login realizado com sucesso\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credenciais inválidas ou erro na autenticação",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Erro de autenticação",
                                    value = "Erro ao fazer login: Bad credentials"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não encontrado ou senha incorreta",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> logar(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        try {
            TokenPairDTO tokens = usuarioService.logar(authenticationDTO);

            ResponseCookie accessTokenCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, tokens.accessToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(ACCESS_TOKEN_MAX_AGE)
                    .sameSite("Strict")
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, tokens.refreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/auth/refresh")
                    .maxAge(REFRESH_TOKEN_MAX_AGE)
                    .sameSite("Strict")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
            headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new AuthResponseDTO("Login realizado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer login: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Renovar Access Token",
            description = """
                    Renova o Access Token utilizando o Refresh Token armazenado em cookie HttpOnly.
                    
                    **Fluxo de renovação:**
                    1. O sistema captura o Refresh Token do cookie `refresh_token`
                    2. Valida o Refresh Token
                    3. Se válido, gera um novo Access Token
                    4. Retorna o novo Access Token em cookie HttpOnly
                    
                    **Importante:** Este endpoint é público mas requer um Refresh Token válido no cookie.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Access Token renovado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Sucesso",
                                    value = "{\"message\": \"Token renovado com sucesso\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Refresh Token inválido ou expirado",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Token inválido",
                                    value = "Refresh token inválido ou expirado"
                            )
                    )
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(401).body("Refresh token não fornecido");
        }

        String newAccessToken = usuarioService.renovarAccessToken(refreshToken);

        if (newAccessToken == null) {
            return ResponseEntity.status(401).body("Refresh token inválido ou expirado");
        }

        ResponseCookie accessTokenCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, newAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(ACCESS_TOKEN_MAX_AGE)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(new AuthResponseDTO("Token renovado com sucesso"));
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = """
                    Cadastra um novo usuário no sistema.
                    
                    **Restrições de acesso:**
                    - Este endpoint requer autenticação com token JWT (via cookie HttpOnly)
                    - Apenas usuários com perfil **ADMIN** podem registrar novos usuários
                    
                    **Perfis disponíveis:**
                    - `ADMIN`: Acesso total ao sistema, incluindo registro de usuários
                    - `USER`: Acesso básico às funcionalidades do sistema
                    
                    **Validações:**
                    - O username deve ser único no sistema
                    - A senha será criptografada com BCrypt antes de ser armazenada
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário registrado com sucesso",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Sucesso",
                                    value = "Usuário registrado com sucesso"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na validação ou usuário já existe",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Usuário duplicado",
                                    value = "Usuário já existe"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado - Token JWT não fornecido ou inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado - Apenas administradores podem registrar usuários",
                    content = @Content
            )
    })
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody @Valid RegisterDTO registerDTO) {
        try{
            usuarioService.registrarUsuario(registerDTO);
            return ResponseEntity.ok().body("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
