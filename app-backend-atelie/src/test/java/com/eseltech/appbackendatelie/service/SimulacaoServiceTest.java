package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.DTO.MaterialProdutoDTO;
import com.eseltech.appbackendatelie.DTO.request.SimularPrecoRequestDTO;
import com.eseltech.appbackendatelie.DTO.response.SimularPrecoResponseDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.entity.enums.Categoria;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulacaoServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private ImpostoService impostoService;

    @InjectMocks
    private SimulacaoService simulacaoService;

    private Empresa empresa;
    private Material material1;
    private Material material2;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1);
        empresa.setRazaoSocial("Empresa Teste");

        // Material 1: Papel - 50 reais para 100 cm
        material1 = new Material();
        material1.setId(1);
        material1.setNome("Papel Cartão");
        material1.setCategoria(Categoria.CENTIMETRO);
        material1.setQtdEstoque(new BigDecimal("100.00"));
        material1.setPreco(new BigDecimal("50.00"));
        material1.setEmpresa(empresa);

        // Material 2: Cola - 20 reais para 500 ml
        material2 = new Material();
        material2.setId(2);
        material2.setNome("Cola Branca");
        material2.setCategoria(Categoria.MILILITRO);
        material2.setQtdEstoque(new BigDecimal("500.00"));
        material2.setPreco(new BigDecimal("20.00"));
        material2.setEmpresa(empresa);
    }

    @Test
    void simularPreco_DeveCalcularCorretamenteComUmMaterial() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1, new BigDecimal("10.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("5.00"),  // Mão de obra
                new BigDecimal("50.00")  // Margem de lucro 50%
        );

        HistoricoImpostoDTO impostoDTO = new HistoricoImpostoDTO(
                "IPCA",
                new BigDecimal("4.50"), // 4.5%
                LocalDate.of(2026, 5, 1)
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material1));
        when(impostoService.buscarTodos()).thenReturn(List.of(impostoDTO));

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        assertNotNull(resultado);

        // Cálculo esperado:
        // Custo unitário material1 = 50.00 / 100.00 = 0.50 por cm
        // Custo para 10 cm = 0.50 * 10 = 5.00
        assertEquals(new BigDecimal("5.00"), resultado.custoMateriais());

        // Mão de obra
        assertEquals(new BigDecimal("5.00"), resultado.custoMaoDeObra());

        // Custo total base = 5.00 + 5.00 = 10.00
        // IPCA = 10.00 * 0.045 = 0.45
        assertEquals(new BigDecimal("0.45"), resultado.valorImpostos());

        // Custo com imposto = 10.00 + 0.45 = 10.45
        assertEquals(new BigDecimal("10.45"), resultado.custoTotal());

        // Preço sugerido = 10.45 * 1.50 = 15.675 -> 15.68
        assertEquals(new BigDecimal("15.68"), resultado.precoSugerido());
    }

    @Test
    void simularPreco_DeveCalcularComMultiplosMateriais() {
        // Arrange
        MaterialProdutoDTO materialDTO1 = new MaterialProdutoDTO(1, new BigDecimal("20.00"));
        MaterialProdutoDTO materialDTO2 = new MaterialProdutoDTO(2, new BigDecimal("100.00"));

        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO1, materialDTO2),
                new BigDecimal("10.00"),
                new BigDecimal("40.00")
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material1));
        when(materialRepository.findById(2)).thenReturn(Optional.of(material2));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // Material1: (50/100) * 20 = 10.00
        // Material2: (20/500) * 100 = 4.00
        // Total materiais = 14.00
        assertEquals(new BigDecimal("14.00"), resultado.custoMateriais());

        // Mão de obra = 10.00
        assertEquals(new BigDecimal("10.00"), resultado.custoMaoDeObra());

        // Sem IPCA
        assertEquals(new BigDecimal("0.00"), resultado.valorImpostos());

        // Custo total = 14.00 + 10.00 = 24.00
        assertEquals(new BigDecimal("24.00"), resultado.custoTotal());

        // Preço = 24.00 * 1.40 = 33.60
        assertEquals(new BigDecimal("33.60"), resultado.precoSugerido());
    }

    @Test
    void simularPreco_DeveTratarIPCAZero() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1, new BigDecimal("10.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("5.00"),
                new BigDecimal("30.00")
        );

        HistoricoImpostoDTO impostoDTO = new HistoricoImpostoDTO(
                "IPCA",
                BigDecimal.ZERO,
                LocalDate.of(2026, 5, 1)
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material1));
        when(impostoService.buscarTodos()).thenReturn(List.of(impostoDTO));

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        assertEquals(new BigDecimal("0.00"), resultado.valorImpostos());
        assertEquals(new BigDecimal("10.00"), resultado.custoTotal());
    }

    @Test
    void simularPreco_DeveTratarListaDeImpostosVazia() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1, new BigDecimal("5.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("3.00"),
                new BigDecimal("25.00")
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material1));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // Sem impostos no sistema
        assertEquals(new BigDecimal("0.00"), resultado.valorImpostos());

        // Custo material = (50/100) * 5 = 2.50
        // Custo base = 2.50 + 3.00 = 5.50
        assertEquals(new BigDecimal("5.50"), resultado.custoTotal());

        // Preço = 5.50 * 1.25 = 6.875 -> 6.88
        assertEquals(new BigDecimal("6.88"), resultado.precoSugerido());
    }

    @Test
    void simularPreco_DeveTratarIPCANaoEncontrado() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1, new BigDecimal("10.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("5.00"),
                new BigDecimal("20.00")
        );

        // Imposto que não é IPCA
        HistoricoImpostoDTO impostoDTO = new HistoricoImpostoDTO(
                "ICMS",
                new BigDecimal("18.00"),
                LocalDate.of(2026, 5, 1)
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material1));
        when(impostoService.buscarTodos()).thenReturn(List.of(impostoDTO));

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // IPCA não encontrado = 0
        assertEquals(new BigDecimal("0.00"), resultado.valorImpostos());
    }

    @Test
    void simularPreco_DeveArredondarCorretamente() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1, new BigDecimal("7.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("3.33"),
                new BigDecimal("33.33")
        );

        HistoricoImpostoDTO impostoDTO = new HistoricoImpostoDTO(
                "IPCA",
                new BigDecimal("3.33"),
                LocalDate.of(2026, 5, 1)
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material1));
        when(impostoService.buscarTodos()).thenReturn(List.of(impostoDTO));

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // Verificar que todos os valores têm exatamente 2 casas decimais
        assertEquals(2, resultado.custoMateriais().scale());
        assertEquals(2, resultado.custoMaoDeObra().scale());
        assertEquals(2, resultado.valorImpostos().scale());
        assertEquals(2, resultado.custoTotal().scale());
        assertEquals(2, resultado.precoSugerido().scale());

        // Custo material = (50/100) * 7 = 3.50
        assertEquals(new BigDecimal("3.50"), resultado.custoMateriais());
    }

    @Test
    void simularPreco_DeveTratarValoresPequenos() {
        // Arrange
        Material materialPequeno = new Material();
        materialPequeno.setId(3);
        materialPequeno.setQtdEstoque(new BigDecimal("1000.00"));
        materialPequeno.setPreco(new BigDecimal("1.00"));

        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(3, new BigDecimal("0.01"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("0.01"),
                new BigDecimal("10.00")
        );

        when(materialRepository.findById(3)).thenReturn(Optional.of(materialPequeno));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // Verificar que valores pequenos são calculados corretamente
        assertNotNull(resultado);
        assertTrue(resultado.custoMateriais().compareTo(BigDecimal.ZERO) >= 0);
        assertTrue(resultado.precoSugerido().compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void simularPreco_DeveTratarValoresGrandes() {
        // Arrange
        Material materialGrande = new Material();
        materialGrande.setId(4);
        materialGrande.setQtdEstoque(new BigDecimal("10000.00"));
        materialGrande.setPreco(new BigDecimal("50000.00"));

        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(4, new BigDecimal("5000.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("10000.00"),
                new BigDecimal("100.00")
        );

        when(materialRepository.findById(4)).thenReturn(Optional.of(materialGrande));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // Custo material = (50000/10000) * 5000 = 25000.00
        assertEquals(new BigDecimal("25000.00"), resultado.custoMateriais());

        // Custo total = 25000 + 10000 = 35000
        assertEquals(new BigDecimal("35000.00"), resultado.custoTotal());

        // Preço = 35000 * 2.00 = 70000.00
        assertEquals(new BigDecimal("70000.00"), resultado.precoSugerido());
    }

    @Test
    void simularPreco_DeveCalcularComDivisaoComplexa() {
        // Arrange
        Material materialComplexo = new Material();
        materialComplexo.setId(5);
        materialComplexo.setQtdEstoque(new BigDecimal("3.00"));
        materialComplexo.setPreco(new BigDecimal("10.00"));

        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(5, new BigDecimal("1.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                new BigDecimal("5.00"),
                new BigDecimal("50.00")
        );

        HistoricoImpostoDTO impostoDTO = new HistoricoImpostoDTO(
                "IPCA",
                new BigDecimal("5.00"),
                LocalDate.of(2026, 5, 1)
        );

        when(materialRepository.findById(5)).thenReturn(Optional.of(materialComplexo));
        when(impostoService.buscarTodos()).thenReturn(List.of(impostoDTO));

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // Custo unitário = 10 / 3 = 3.3333...
        // Custo para 1 unidade = 3.3333... * 1 = 3.3333... -> 3.33
        assertEquals(new BigDecimal("3.33"), resultado.custoMateriais());

        // Custo base = 3.33 + 5.00 = 8.33
        // IPCA = 8.33 * 0.05 = 0.4165 -> 0.42
        assertEquals(new BigDecimal("0.42"), resultado.valorImpostos());

        // Custo total = 8.33 + 0.42 = 8.75
        assertEquals(new BigDecimal("8.75"), resultado.custoTotal());

        // Preço = 8.75 * 1.50 = 13.125 -> 13.12 (HALF_UP na divisão anterior)
        assertEquals(new BigDecimal("13.12"), resultado.precoSugerido());
    }

    @Test
    void simularPreco_DeveAplicarMargemLucroCorretamente() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1, new BigDecimal("10.00"));

        // Teste com margem de 0%
        SimularPrecoRequestDTO requestSemMargem = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(materialRepository.findById(1)).thenReturn(Optional.of(material1));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(requestSemMargem);

        // Assert
        // Com margem 0%, o preço sugerido deve ser igual ao custo total
        assertEquals(resultado.custoTotal(), resultado.precoSugerido());
    }

    @Test
    void simularPreco_DeveUsarRoundingModeHalfUp() {
        // Arrange
        Material materialArredondamento = new Material();
        materialArredondamento.setId(6);
        materialArredondamento.setQtdEstoque(new BigDecimal("3.00"));
        materialArredondamento.setPreco(new BigDecimal("1.00"));

        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(6, new BigDecimal("1.00"));
        SimularPrecoRequestDTO request = new SimularPrecoRequestDTO(
                List.of(materialDTO),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(materialRepository.findById(6)).thenReturn(Optional.of(materialArredondamento));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());

        // Act
        SimularPrecoResponseDTO resultado = simulacaoService.simularPreco(request);

        // Assert
        // 1.00 / 3.00 = 0.333333... que deve arredondar para 0.33 com HALF_UP
        assertEquals(new BigDecimal("0.33"), resultado.custoMateriais());
    }
}






