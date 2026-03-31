package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.AuthenticationDTO;
import com.eseltech.appbackendatelie.DTO.RegisterDTO;
import com.eseltech.appbackendatelie.DTO.TokenPairDTO;
import com.eseltech.appbackendatelie.modal.Usuario;
import com.eseltech.appbackendatelie.repository.UsuarioRepository;
import com.eseltech.appbackendatelie.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Serviço responsável pelas operações relacionadas a usuários.
 */
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

    /**
     * Autentica o usuário e gera o par de tokens (Access e Refresh).
     *
     * @param authenticationDTO credenciais do usuário
     * @return TokenPairDTO contendo os tokens de acesso e refresh
     */
    public TokenPairDTO logar(AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.senha());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        Usuario usuario = (Usuario) auth.getPrincipal();
        String accessToken = tokenService.gerarAccessToken(usuario);
        String refreshToken = tokenService.gerarRefreshToken(usuario);

        return new TokenPairDTO(accessToken, refreshToken);
    }

    /**
     * Gera um novo Access Token a partir de um Refresh Token válido.
     *
     * @param refreshToken o Refresh Token do usuário
     * @return o novo Access Token ou null se o Refresh Token for inválido
     */
    public String renovarAccessToken(String refreshToken) {
        String username = tokenService.validarRefreshToken(refreshToken);
        if (username == null || username.isEmpty()) {
            return null;
        }

        Usuario usuario = (Usuario) usuarioRepository.findByUsername(username);
        if (usuario == null) {
            return null;
        }

        return tokenService.gerarAccessToken(usuario);
    }
}
