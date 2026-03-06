package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
