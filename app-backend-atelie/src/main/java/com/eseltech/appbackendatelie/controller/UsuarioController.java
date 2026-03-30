package com.eseltech.appbackendatelie.controller;

import com.eseltech.appbackendatelie.modal.Usuario;
import com.eseltech.appbackendatelie.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * Controller responsável pelo gerenciamento de usuários do sistema.
 * Fornece endpoints para operações CRUD e autenticação de usuários.
 */
@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuários", description = "API para gerenciamento de usuários do sistema - cadastro, atualização, remoção e autenticação")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista completa de todos os usuários cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    @GetMapping("/find-all")
    public ResponseEntity<List<Usuario>> buscarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário existente através do seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Atualizado com sucesso!"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
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
    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarUsuario(
            @Parameter(description = "Dados atualizados do usuário", required = true)
            @RequestBody Usuario usuario,
            @Parameter(description = "ID do usuário a ser atualizado", required = true, example = "1")
            @PathVariable Long id) {
        usuarioService.atualizarUsuario(usuario, id);
        return ResponseEntity.ok().body("Atualizado com sucesso!");
    }

    @Operation(
            summary = "Remover usuário",
            description = "Remove um usuário do sistema através do seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário removido com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<String> removerUsuario(
            @Parameter(description = "ID do usuário a ser removido", required = true, example = "1")
            @PathVariable Long id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
