package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.AuthenticationDTO;
import com.eseltech.appbackendatelie.DTO.RegisterDTO;
import com.eseltech.appbackendatelie.DTO.TokenPairDTO;
import com.eseltech.appbackendatelie.DTO.request.AtualizarSenhaRequest;
import com.eseltech.appbackendatelie.entity.Usuario;
import com.eseltech.appbackendatelie.entity.enums.UserRole;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.UsuarioRepository;
import com.eseltech.appbackendatelie.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setUsername("usuario.teste");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha(encoder.encode("senha123"));
        usuario.setRole(UserRole.USER);
    }

    @Test
    void registrarUsuario_DeveSalvarComSucesso() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO(
                "Novo Usuario",
                "novo.usuario",
                "novo@teste.com",
                "senha123",
                UserRole.USER
        );

        when(usuarioRepository.findByUsername("novo.usuario")).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.registrarUsuario(registerDTO);

        // Assert
        verify(usuarioRepository, times(1)).findByUsername("novo.usuario");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_DeveLancarExcecao_QuandoUsuarioJaExiste() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO(
                "Usuario Existente",
                "usuario.teste",
                "teste@teste.com",
                "senha123",
                UserRole.USER
        );

        when(usuarioRepository.findByUsername("usuario.teste")).thenReturn(usuario);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.registrarUsuario(registerDTO);
        });

        assertEquals("Usuário já existe", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void buscarTodos_DeveRetornarListaDeUsuarios() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        // Act
        List<Usuario> resultado = usuarioService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Usuario Teste", resultado.getFirst().getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarUsuarioPorId_DeveRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.buscarUsuarioPorId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals("Usuario Teste", resultado.getNome());
        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    void buscarUsuarioPorId_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
            usuarioService.buscarUsuarioPorId(999)
        );
    }

    @Test
    void atualizarUsuario_DeveAtualizarComSucesso() {
        // Arrange
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Nome Atualizado");
        usuarioAtualizado.setEmail("atualizado@teste.com");
        usuarioAtualizado.setUsername("usuario.atualizado");
        usuarioAtualizado.setRole(UserRole.ADMIN);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.atualizarUsuario(usuarioAtualizado, 1);

        // Assert
        assertEquals("Nome Atualizado", usuario.getNome());
        assertEquals("atualizado@teste.com", usuario.getEmail());
        assertEquals("usuario.atualizado", usuario.getUsername());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void atualizarUsuario_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        Usuario usuarioAtualizado = new Usuario();
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            usuarioService.atualizarUsuario(usuarioAtualizado, 999)
        );
    }

    @Test
    void atualizarSenha_DeveAtualizarComSucesso() {
        // Arrange
        AtualizarSenhaRequest request = new AtualizarSenhaRequest("senha123", "novaSenha123", 1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.atualizarSenha(request);

        // Assert
        verify(usuarioRepository, times(1)).save(usuario);
        assertTrue(encoder.matches("novaSenha123", usuario.getSenha()));
    }

    @Test
    void atualizarSenha_DeveLancarExcecao_QuandoSenhaAntigaIncorreta() {
        // Arrange
        AtualizarSenhaRequest request = new AtualizarSenhaRequest("senhaErrada", "novaSenha123", 1);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.atualizarSenha(request);
        });

        assertEquals("Senha antiga incorreta", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void atualizarSenha_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
        // Arrange
        AtualizarSenhaRequest request = new AtualizarSenhaRequest("senha123", "novaSenha123", 999);
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            usuarioService.atualizarSenha(request);
        });
    }

    @Test
    void removerUsuario_DeveDeletarComSucesso() {
        // Arrange
        doNothing().when(usuarioRepository).deleteById(1);

        // Act
        usuarioService.removerUsuario(1);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    void logar_DeveRetornarTokens() {
        // Arrange
        AuthenticationDTO authDTO = new AuthenticationDTO("usuario.teste", "senha123");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(tokenService.gerarAccessToken(usuario)).thenReturn("access-token");
        when(tokenService.gerarRefreshToken(usuario)).thenReturn("refresh-token");

        // Act
        TokenPairDTO resultado = usuarioService.logar(authDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("access-token", resultado.accessToken());
        assertEquals("refresh-token", resultado.refreshToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void renovarAccessToken_DeveRetornarNovoToken() {
        // Arrange
        String refreshToken = "valid-refresh-token";
        when(tokenService.validarRefreshToken(refreshToken)).thenReturn("usuario.teste");
        when(usuarioRepository.findByUsername("usuario.teste")).thenReturn(usuario);
        when(tokenService.gerarAccessToken(usuario)).thenReturn("new-access-token");

        // Act
        String resultado = usuarioService.renovarAccessToken(refreshToken);

        // Assert
        assertEquals("new-access-token", resultado);
        verify(tokenService, times(1)).validarRefreshToken(refreshToken);
        verify(tokenService, times(1)).gerarAccessToken(usuario);
    }

    @Test
    void renovarAccessToken_DeveRetornarNull_QuandoRefreshTokenInvalido() {
        // Arrange
        String refreshToken = "invalid-refresh-token";
        when(tokenService.validarRefreshToken(refreshToken)).thenReturn(null);

        // Act
        String resultado = usuarioService.renovarAccessToken(refreshToken);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository, never()).findByUsername(anyString());
    }

    @Test
    void renovarAccessToken_DeveRetornarNull_QuandoUsuarioNaoExiste() {
        // Arrange
        String refreshToken = "valid-refresh-token";
        when(tokenService.validarRefreshToken(refreshToken)).thenReturn("usuario.inexistente");
        when(usuarioRepository.findByUsername("usuario.inexistente")).thenReturn(null);

        // Act
        String resultado = usuarioService.renovarAccessToken(refreshToken);

        // Assert
        assertNull(resultado);
        verify(tokenService, never()).gerarAccessToken(any(Usuario.class));
    }

    @Test
    void renovarAccessToken_DeveRetornarNull_QuandoUsernameVazio() {
        // Arrange
        String refreshToken = "valid-refresh-token";
        when(tokenService.validarRefreshToken(refreshToken)).thenReturn("");

        // Act
        String resultado = usuarioService.renovarAccessToken(refreshToken);

        // Assert
        assertNull(resultado);
        verify(usuarioRepository, never()).findByUsername(anyString());
    }
}



