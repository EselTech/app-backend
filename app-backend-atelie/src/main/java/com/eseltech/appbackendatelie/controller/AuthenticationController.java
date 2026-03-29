package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.DTO.AuthenticationDTO;
import com.eseltech.appbackendatelie.DTO.LoginResponseDTO;
import com.eseltech.appbackendatelie.DTO.RegisterDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsável pela autenticação e registro de usuários.
 * Implementa o fluxo de autenticação JWT da aplicação.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "API para autenticação JWT - login e registro de usuários")
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Realizar login",
            description = """
                    Autentica o usuário com username e senha, retornando um token JWT válido.
                    
                    **Fluxo de autenticação:**
                    1. O usuário envia suas credenciais (username e senha)
                    2. O sistema valida as credenciais contra o banco de dados
                    3. Se válidas, um token JWT é gerado com validade de 2 horas
                    4. O token deve ser usado no header `Authorization: Bearer {token}` nas próximas requisições
                    
                    **Importante:** Este endpoint é público e não requer autenticação prévia.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Resposta de sucesso",
                                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
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
    public ResponseEntity logar(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        try {
            return ResponseEntity.ok(usuarioService.logar(authenticationDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer login: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = """
                    Cadastra um novo usuário no sistema.
                    
                    **Restrições de acesso:**
                    - Este endpoint requer autenticação com token JWT
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
    public ResponseEntity registrar(@RequestBody @Valid RegisterDTO registerDTO) {
        try{
            usuarioService.registrarUsuario(registerDTO);
            return ResponseEntity.ok().body("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
