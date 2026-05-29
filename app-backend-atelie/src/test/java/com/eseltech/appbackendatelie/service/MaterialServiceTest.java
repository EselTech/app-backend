package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.MaterialDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.entity.enums.Categoria;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private MaterialService materialService;

    private Empresa empresa;
    private Material material;

    @BeforeEach
    void setUp() {
        // Setup Empresa
        empresa = new Empresa();
        empresa.setId(1);
        empresa.setRazaoSocial("Empresa Teste");
        empresa.setCnpj("12345678901234");

        // Setup Material
        material = new Material();
        material.setId(1);
        material.setNome("Papel Cartão Vermelho");
        material.setDescricao("Papel cartão da cor vermelha");
        material.setCategoria(Categoria.CENTIMETRO);
        material.setQtdEstoque(new BigDecimal("100.00"));
        material.setPreco(new BigDecimal("50.00"));
        material.setEmpresa(empresa);
    }

    @Test
    void salvarMaterial_DeveSalvarComSucesso() {
        // Arrange
        MaterialDTO dto = new MaterialDTO(
                null,
                1,
                Categoria.CENTIMETRO,
                "Papel Cartão Azul",
                "Papel cartão da cor azul",
                new BigDecimal("150.00"),
                new BigDecimal("75.00")
        );

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(materialRepository.save(any(Material.class))).thenAnswer(i -> {
            Material m = i.getArgument(0);
            m.setId(2);
            return m;
        });

        // Act
        Material resultado = materialService.salvarMaterial(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Papel Cartão Azul", resultado.getNome());
        assertEquals("Papel cartão da cor azul", resultado.getDescricao());
        assertEquals(Categoria.CENTIMETRO, resultado.getCategoria());
        assertEquals(new BigDecimal("150.00"), resultado.getQtdEstoque());
        assertEquals(new BigDecimal("75.00"), resultado.getPreco());
        assertEquals(empresa, resultado.getEmpresa());
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void salvarMaterial_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        MaterialDTO dto = new MaterialDTO(
                null, 999, Categoria.GRAMA, "Material", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN
        );

        when(empresaRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> materialService.salvarMaterial(dto));
        verify(materialRepository, never()).save(any(Material.class));
    }

    @Test
    void salvarMaterial_DeveAceitarDiferentesCategorias() {
        // Arrange
        MaterialDTO dtoGrama = new MaterialDTO(
                null, 1, Categoria.GRAMA, "Glitter",
                "Glitter dourado", new BigDecimal("500.00"),
                new BigDecimal("25.00")
        );

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(materialRepository.save(any(Material.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Material resultado = materialService.salvarMaterial(dtoGrama);

        // Assert
        assertNotNull(resultado);
        assertEquals(Categoria.GRAMA, resultado.getCategoria());
    }

    @Test
    void findById_DeveRetornarMaterial_QuandoExiste() {
        // Arrange
        when(materialRepository.findById(1)).thenReturn(Optional.of(material));

        // Act
        Material resultado = materialService.findById(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(Integer.valueOf(1), resultado.getId());
        assertEquals("Papel Cartão Vermelho", resultado.getNome());
        verify(materialRepository, times(1)).findById(1);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(materialRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> materialService.findById(999));
        verify(materialRepository, times(1)).findById(999);
    }

    @Test
    void findAll_DeveRetornarLista_QuandoExistemMateriais() {
        // Arrange
        List<Material> materiais = List.of(material);
        when(materialRepository.findAll()).thenReturn(materiais);

        // Act
        List<Material> resultado = materialService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveLancarExcecao_QuandoListaVazia() {
        // Arrange
        when(materialRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> materialService.findAll());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void removerMaterial_DeveRemover_QuandoExiste() {
        // Arrange
        when(materialRepository.findById(1)).thenReturn(Optional.of(material));
        doNothing().when(materialRepository).deleteById(1);

        // Act
        materialService.removerMaterial(1);

        // Assert
        verify(materialRepository, times(1)).findById(1);
        verify(materialRepository, times(1)).deleteById(1);
    }

    @Test
    void removerMaterial_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(materialRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> materialService.removerMaterial(999));
        verify(materialRepository, times(1)).findById(999);
        verify(materialRepository, never()).deleteById(anyInt());
    }

    @Test
    void atualizarMaterial_DeveAtualizarComSucesso() {
        // Arrange
        MaterialDTO dto = new MaterialDTO(
                1,
                1,
                Categoria.MILILITRO,
                "Cola Branca Atualizada",
                "Cola branca escolar 1",
                new BigDecimal("1000.00"),
                new BigDecimal("15.00")
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material));
        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(materialRepository.save(any(Material.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Material resultado = materialService.atualizarMaterial(1, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Cola Branca Atualizada", resultado.getNome());
        assertEquals("Cola branca escolar 1", resultado.getDescricao());
        assertEquals(Categoria.MILILITRO, resultado.getCategoria());
        assertEquals(new BigDecimal("1000.00"), resultado.getQtdEstoque());
        assertEquals(new BigDecimal("15.00"), resultado.getPreco());
        verify(materialRepository, times(1)).findById(1);
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void atualizarMaterial_DeveLancarExcecao_QuandoMaterialNaoExiste() {
        // Arrange
        MaterialDTO dto = new MaterialDTO(
                999, 1, Categoria.INTEIRO, "Material", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN
        );

        when(materialRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> materialService.atualizarMaterial(999, dto));
        verify(materialRepository, never()).save(any(Material.class));
    }

    @Test
    void atualizarMaterial_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        MaterialDTO dto = new MaterialDTO(
                1, 999, Categoria.INTEIRO, "Material", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material));
        when(empresaRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> materialService.atualizarMaterial(1, dto));
        verify(materialRepository, never()).save(any(Material.class));
    }

    @Test
    void salvarMaterial_DeveAceitarValoresDecimaisPequenos() {
        // Arrange
        MaterialDTO dto = new MaterialDTO(
                null, 1, Categoria.GRAMA, "Material Preciso",
                "Material com medidas precisas",
                new BigDecimal("0.01"),
                new BigDecimal("0.01")
        );

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(materialRepository.save(any(Material.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Material resultado = materialService.salvarMaterial(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(new BigDecimal("0.01"), resultado.getQtdEstoque());
        assertEquals(new BigDecimal("0.01"), resultado.getPreco());
    }

    @Test
    void salvarMaterial_DeveAceitarValoresDecimaisGrandes() {
        // Arrange
        MaterialDTO dto = new MaterialDTO(
                null, 1, Categoria.CENTIMETRO, "Tecido em Rolo",
                "Grande quantidade de tecido",
                new BigDecimal("10000.50"),
                new BigDecimal("5000.75")
        );

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(materialRepository.save(any(Material.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Material resultado = materialService.salvarMaterial(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(new BigDecimal("10000.50"), resultado.getQtdEstoque());
        assertEquals(new BigDecimal("5000.75"), resultado.getPreco());
    }
}





