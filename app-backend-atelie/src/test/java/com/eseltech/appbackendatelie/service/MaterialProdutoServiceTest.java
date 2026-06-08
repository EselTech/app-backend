package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.MaterialProduto;
import com.eseltech.appbackendatelie.repository.MaterialProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialProdutoServiceTest {

    @Mock
    private MaterialProdutoRepository materialProdutoRepository;

    @InjectMocks
    private MaterialProdutoService materialProdutoService;

    private MaterialProduto materialProduto;

    @BeforeEach
    void setUp() {
        materialProduto = new MaterialProduto();
        materialProduto.setId(1);
    }

    @Test
    void findById_DeveRetornarMaterialProduto() {
        // Arrange
        when(materialProdutoRepository.findById(1)).thenReturn(Optional.of(materialProduto));

        // Act
        MaterialProduto resultado = materialProdutoService.findById(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(materialProdutoRepository, times(1)).findById(1);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(materialProdutoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
            materialProdutoService.findById(999)
        );

        assertTrue(exception.getMessage().contains("MaterialProduto não encontrado com id: 999"));
        verify(materialProdutoRepository, times(1)).findById(999);
    }

    @Test
    void findById_DeveRetornarMaterialProdutoDiferente() {
        // Arrange
        MaterialProduto materialProduto2 = new MaterialProduto();
        materialProduto2.setId(2);

        when(materialProdutoRepository.findById(2)).thenReturn(Optional.of(materialProduto2));

        // Act
        MaterialProduto resultado = materialProdutoService.findById(2);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getId());
        assertNotEquals(materialProduto.getId(), resultado.getId());
    }
}


