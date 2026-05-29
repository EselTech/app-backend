package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.AuthenticationDTO;
import com.eseltech.appbackendatelie.DTO.RegisterDTO;
import com.eseltech.appbackendatelie.DTO.TokenPairDTO;
import com.eseltech.appbackendatelie.DTO.request.AtualizarSenhaRequest;
import com.eseltech.appbackendatelie.entity.Usuario;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.UsuarioRepository;
import com.eseltech.appbackendatelie.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    private final Logger logger = Logger.getLogger(UsuarioService.class.getName());

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public void atualizarUsuario(Usuario usuario, Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setRole(usuario.getRole());
        usuarioExistente.setUsername(usuario.getUsername());
        usuarioExistente.setEmpresa(usuario.getEmpresa());
        usuarioRepository.save(usuarioExistente);
    }

    public void atualizarSenha(AtualizarSenhaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.userID()).orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + request.userID()));

        if (encoder.matches(request.senhaAntiga(), usuario.getSenha())) {
            String novaSenhaCriptografada = encoder.encode(request.novaSenha());
            usuario.setSenha(novaSenhaCriptografada);
            usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Senha antiga incorreta");
        }
    }

    public void removerUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public TokenPairDTO logar(AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.senha());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        Usuario usuario = (Usuario) auth.getPrincipal();
        String accessToken = tokenService.gerarAccessToken(usuario);
        String refreshToken = tokenService.gerarRefreshToken(usuario);

        return new TokenPairDTO(accessToken, refreshToken);
    }

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
