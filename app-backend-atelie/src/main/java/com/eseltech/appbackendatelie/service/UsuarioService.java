package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.Usuario;
import com.eseltech.appbackendatelie.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public void cadastrarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void atualizarUsuario(Usuario usuario, Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        usuarioExistente = usuario;
        usuarioRepository.save(usuarioExistente);
    }

    public void removerUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
