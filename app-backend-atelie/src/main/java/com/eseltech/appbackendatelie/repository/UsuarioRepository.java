package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha")
    public Usuario findByNomeAndSenha(String nome, String senha);
}
