package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha")
    Usuario findByNomeAndSenha(String nome, String senha);

    UserDetails findByUsername(String username);
}
