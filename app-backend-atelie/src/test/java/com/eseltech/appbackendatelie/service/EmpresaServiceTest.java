package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do EmpresaService")
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa("EselTech Ltda", "12345678000190");
        empresa.setId(1); // Empresa usa Integer como ID
    }

    @Test
    @DisplayName("findById - Deve retornar empresa quando ID existe")
    void findById_DeveRetornarEmpresa_QuandoIdExiste() {
        // Arrange
        when(empresaRepository.findById(anyInt())).thenReturn(Optional.of(empresa));

        // Act
        Empresa resultado = empresaService.findById(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(Integer.valueOf(1), resultado.getId());
        assertEquals("EselTech Ltda", resultado.getRazaoSocial());
        assertEquals("12345678000190", resultado.getCnpj());

        verify(empresaRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("findById - Deve lançar exceção quando ID não existe")
    void findById_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(empresaRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.findById(999);
        });

        assertEquals("Empresa não encontrada com id: 999", exception.getMessage());
        verify(empresaRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("findById - Deve retornar empresa com CNPJ correto")
    void findById_DeveRetornarEmpresaComCnpjCorreto_QuandoChamado() {
        // Arrange
        Empresa empresaComCnpj = new Empresa("Empresa Teste", "98765432000111");
        empresaComCnpj.setId(2);

        when(empresaRepository.findById(2)).thenReturn(Optional.of(empresaComCnpj));

        // Act
        Empresa resultado = empresaService.findById(2);

        // Assert
        assertNotNull(resultado);
        assertEquals("98765432000111", resultado.getCnpj());
        assertEquals("Empresa Teste", resultado.getRazaoSocial());

        verify(empresaRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("findById - Deve chamar repository com ID correto")
    void findById_DeveChamarRepositoryComIdCorreto_QuandoChamado() {
        // Arrange
        Integer idEsperado = 5;
        when(empresaRepository.findById(idEsperado)).thenReturn(Optional.of(empresa));

        // Act
        empresaService.findById(idEsperado);

        // Assert
        verify(empresaRepository, times(1)).findById(idEsperado);
        verify(empresaRepository, never()).findById(argThat(id -> !id.equals(idEsperado)));
    }

    @Test
    @DisplayName("findById - Deve retornar empresa com todos os campos preenchidos")
    void findById_DeveRetornarEmpresaCompleta_QuandoDadosValidos() {
        // Arrange
        Empresa empresaCompleta = new Empresa("EselTech Solutions", "11222333000144");
        empresaCompleta.setId(10);

        when(empresaRepository.findById(10)).thenReturn(Optional.of(empresaCompleta));

        // Act
        Empresa resultado = empresaService.findById(10);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertNotNull(resultado.getRazaoSocial());
        assertNotNull(resultado.getCnpj());
        assertEquals(Integer.valueOf(10), resultado.getId());
        assertEquals("EselTech Solutions", resultado.getRazaoSocial());
        assertEquals("11222333000144", resultado.getCnpj());
    }

    @Test
    @DisplayName("findById - Deve lançar exceção com mensagem correta para ID negativo")
    void findById_DeveLancarExcecaoComMensagemCorreta_QuandoIdNegativo() {
        // Arrange
        Integer idNegativo = -1;
        when(empresaRepository.findById(idNegativo)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.findById(idNegativo);
        });

        assertTrue(exception.getMessage().contains("Empresa não encontrada com id: -1"));
        verify(empresaRepository, times(1)).findById(idNegativo);
    }

    @Test
    @DisplayName("findById - Deve lançar exceção com mensagem correta para ID zero")
    void findById_DeveLancarExcecaoComMensagemCorreta_QuandoIdZero() {
        // Arrange
        when(empresaRepository.findById(0)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            empresaService.findById(0);
        });

        assertTrue(exception.getMessage().contains("Empresa não encontrada com id: 0"));
        verify(empresaRepository, times(1)).findById(0);
    }

    @Test
    @DisplayName("findById - Deve retornar mesma instância retornada pelo repository")
    void findById_DeveRetornarMesmaInstancia_QuandoChamado() {
        // Arrange
        when(empresaRepository.findById(anyInt())).thenReturn(Optional.of(empresa));

        // Act
        Empresa resultado = empresaService.findById(1);

        // Assert
        assertSame(empresa, resultado);
        verify(empresaRepository, times(1)).findById(1);
    }
}






