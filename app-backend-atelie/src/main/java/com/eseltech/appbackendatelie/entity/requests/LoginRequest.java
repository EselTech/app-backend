package com.eseltech.appbackendatelie.entity.requests;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para requisição de login do usuário.
 */
@Schema(description = "Objeto contendo as credenciais de login do usuário")
public class LoginRequest {
    @Schema(description = "Nome do usuário para autenticação", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "Senha do usuário para autenticação", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senha;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
