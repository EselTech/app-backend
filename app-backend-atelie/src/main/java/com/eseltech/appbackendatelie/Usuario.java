package com.eseltech.appbackendatelie;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidade que representa um usuário do sistema.
 */
@Entity
@Table(name = "usuario")
@Schema(description = "Entidade que representa um usuário cadastrado no sistema")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Size(max = 40)
    @NotNull
    @Column(name = "nome", nullable = false, length = 40)
    @Schema(description = "Nome do usuário", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 40)
    private String nome;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    @Schema(description = "Email do usuário", example = "joao.silva@email.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String email;

    @Size(max = 200)
    @NotNull
    @Column(name = "senha", nullable = false, length = 200)
    @Schema(description = "Senha do usuário", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String senha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}