package com.eseltech.appbackendatelie.modal;

import com.eseltech.appbackendatelie.entity.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entidade que representa um usuário do sistema.
 */
@Table(name = "usuario")
@Entity
@Schema(description = "Entidade que representa um usuário cadastrado no sistema")
public class Usuario implements UserDetails {
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

    @Size(max = 40)
    @NotNull
    @Column(name = "username", nullable = false, length = 40)
    private String username;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    public Usuario() {
    }

    public Usuario(String nome, String username, String email, String senha, UserRole role) {
        this.nome = nome;
        this.username = username;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}