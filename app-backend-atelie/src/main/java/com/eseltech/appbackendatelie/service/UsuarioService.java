package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.AuthenticationDTO;
import com.eseltech.appbackendatelie.DTO.LoginResponseDTO;
import com.eseltech.appbackendatelie.DTO.RegisterDTO;
import com.eseltech.appbackendatelie.entity.Usuario;
import com.eseltech.appbackendatelie.repository.UsuarioRepository;
import com.eseltech.appbackendatelie.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public void registrarUsuario(RegisterDTO registerDTO) {
        if (usuarioRepository.findByUsername(registerDTO.username()) != null) {
            throw new RuntimeException("Usuário já existe");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(registerDTO.senha());
        Usuario novoUsuario = new Usuario(registerDTO.nome(), registerDTO.username(), registerDTO.email(), senhaCriptografada, registerDTO.role());

        usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public void atualizarUsuario(Usuario usuario, Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        usuarioExistente = usuario;
        usuarioRepository.save(usuarioExistente);
    }

    public void removerUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public LoginResponseDTO logar(AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.senha());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }
}
