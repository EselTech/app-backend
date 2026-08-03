package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.home.*;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private HomeService homeService;

    private Integer empresaId;

    @BeforeEach
    void setUp() {
        empresaId = 1;
    }

    @Test
    void getHomeData_DeveRetornarDadosCompletos() {
        // Arrange
        BigDecimal receita = new BigDecimal("10000.00");
        BigDecimal despesas = new BigDecimal("5000.00");
        BigDecimal lucro = new BigDecimal("5000.00");
        BigDecimal aReceber = new BigDecimal("3000.00");

        when(pedidoRepository.somarReceitaMesAtual(empresaId)).thenReturn(receita);
        when(pedidoRepository.somarDespesasMesAtual(empresaId)).thenReturn(despesas);
        when(pedidoRepository.somarLucroMesAtual(empresaId)).thenReturn(lucro);
        when(pedidoRepository.somarValorAReceberMesAtual(empresaId)).thenReturn(aReceber);

        List<PedidoPorStatusDTO> pedidosPorStatus = List.of(
                new PedidoPorStatusDTO("shipped", 10L),
                new PedidoPorStatusDTO("ongoing", 5L)
        );
        when(pedidoRepository.contarPedidosPorStatus(empresaId)).thenReturn(pedidosPorStatus);

        List<ProdutoMaisVendidoMesDTO> produtosMaisVendidos = List.of(
                new ProdutoMaisVendidoMesDTO(1, new BigDecimal("50"), "Produto 1")
        );
        when(produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId)).thenReturn(produtosMaisVendidos);

        List<UsoMaterialCategoriaDTO> materiaisPorCategoria = List.of(
                new UsoMaterialCategoriaDTO(1, new BigDecimal("100.00"), "CENTIMETRO", "Material 1")
        );
        when(materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId)).thenReturn(materiaisPorCategoria);

        // Act
        HomeResponseDTO resultado = homeService.getHomeData(empresaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(receita, resultado.receitaKPIDTO().receita_total());
        assertEquals(despesas, resultado.despesaKPIDTO().despesa_total());
        assertEquals(lucro, resultado.lucroKPIDTO().lucro());
        assertEquals(aReceber, resultado.receberKPIDTO().valor_a_receber());

        verify(pedidoRepository, times(1)).somarReceitaMesAtual(empresaId);
        verify(pedidoRepository, times(1)).somarDespesasMesAtual(empresaId);
        verify(pedidoRepository, times(1)).somarLucroMesAtual(empresaId);
        verify(pedidoRepository, times(1)).somarValorAReceberMesAtual(empresaId);
    }

    @Test
    void getHomeData_DevePreencherStatusFaltantes() {
        // Arrange
        when(pedidoRepository.somarReceitaMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarDespesasMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarLucroMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarValorAReceberMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);

        // Retorna apenas alguns status
        List<PedidoPorStatusDTO> pedidosPorStatus = List.of(
                new PedidoPorStatusDTO("shipped", 10L)
        );
        when(pedidoRepository.contarPedidosPorStatus(empresaId)).thenReturn(pedidosPorStatus);
        when(produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId)).thenReturn(new ArrayList<>());
        when(materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId)).thenReturn(new ArrayList<>());

        // Act
        HomeResponseDTO resultado = homeService.getHomeData(empresaId);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.pedidosPorStatus());
        // Deve ter 4 status: shipped, ongoing, open, late
        assertEquals(4, resultado.pedidosPorStatus().size());

        // Verifica que os status esperados estão presentes
        List<String> statusPresentes = resultado.pedidosPorStatus().stream()
                .map(PedidoPorStatusDTO::status)
                .toList();
        assertTrue(statusPresentes.contains("shipped"));
        assertTrue(statusPresentes.contains("ongoing"));
        assertTrue(statusPresentes.contains("open"));
        assertTrue(statusPresentes.contains("late"));
    }

    @Test
    void getHomeData_DevePreencherCategoriasVazias() {
        // Arrange
        when(pedidoRepository.somarReceitaMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarDespesasMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarLucroMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarValorAReceberMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.contarPedidosPorStatus(empresaId)).thenReturn(new ArrayList<>());
        when(produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId)).thenReturn(new ArrayList<>());

        // Sem materiais usados
        when(materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId)).thenReturn(new ArrayList<>());

        // Act
        HomeResponseDTO resultado = homeService.getHomeData(empresaId);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.materiaisPorCategoria());

        // Deve ter placeholder para cada categoria esperada
        assertEquals(4, resultado.materiaisPorCategoria().size());

        // Todos devem ter "Sem consumo"
        resultado.materiaisPorCategoria().forEach(material -> {
            assertEquals("Sem consumo", material.nome());
            assertEquals(BigDecimal.ZERO, material.valorTotal());
        });
    }

    @Test
    void getHomeData_DeveRetornarProdutosMaisVendidos() {
        // Arrange
        when(pedidoRepository.somarReceitaMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarDespesasMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarLucroMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarValorAReceberMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.contarPedidosPorStatus(empresaId)).thenReturn(new ArrayList<>());
        when(materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId)).thenReturn(new ArrayList<>());

        List<ProdutoMaisVendidoMesDTO> produtosMaisVendidos = List.of(
                new ProdutoMaisVendidoMesDTO(1, new BigDecimal("100"), "Produto A"),
                new ProdutoMaisVendidoMesDTO(2, new BigDecimal("50"), "Produto B"),
                new ProdutoMaisVendidoMesDTO(3, new BigDecimal("25"), "Produto C")
        );
        when(produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId)).thenReturn(produtosMaisVendidos);

        // Act
        HomeResponseDTO resultado = homeService.getHomeData(empresaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.produtosMaisVendidos().size());
        assertEquals("Produto A", resultado.produtosMaisVendidos().getFirst().nome());
        assertEquals(new BigDecimal("100"), resultado.produtosMaisVendidos().getFirst().quantidade());
    }

    @Test
    void getHomeData_DeveTratarValoresNulos() {
        // Arrange
        when(pedidoRepository.somarReceitaMesAtual(empresaId)).thenReturn(null);
        when(pedidoRepository.somarDespesasMesAtual(empresaId)).thenReturn(null);
        when(pedidoRepository.somarLucroMesAtual(empresaId)).thenReturn(null);
        when(pedidoRepository.somarValorAReceberMesAtual(empresaId)).thenReturn(null);
        when(pedidoRepository.contarPedidosPorStatus(empresaId)).thenReturn(new ArrayList<>());
        when(produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId)).thenReturn(new ArrayList<>());
        when(materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId)).thenReturn(new ArrayList<>());

        // Act
        HomeResponseDTO resultado = homeService.getHomeData(empresaId);

        // Assert
        assertNotNull(resultado);
        // Os valores nulos devem ser tratados
        assertNotNull(resultado.receitaKPIDTO());
        assertNotNull(resultado.despesaKPIDTO());
        assertNotNull(resultado.lucroKPIDTO());
        assertNotNull(resultado.receberKPIDTO());
    }

    @Test
    void getHomeData_DeveRetornarMateriaisPorCategoria() {
        // Arrange
        when(pedidoRepository.somarReceitaMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarDespesasMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarLucroMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.somarValorAReceberMesAtual(empresaId)).thenReturn(BigDecimal.ZERO);
        when(pedidoRepository.contarPedidosPorStatus(empresaId)).thenReturn(new ArrayList<>());
        when(produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId)).thenReturn(new ArrayList<>());

        List<UsoMaterialCategoriaDTO> materiaisPorCategoria = List.of(
                new UsoMaterialCategoriaDTO(1, new BigDecimal("100.00"), "CENTIMETRO", "Linha"),
                new UsoMaterialCategoriaDTO(2, new BigDecimal("50.00"), "GRAMA", "Algodão"),
                new UsoMaterialCategoriaDTO(3, new BigDecimal("25.00"), "MILILITRO", "Tinta"),
                new UsoMaterialCategoriaDTO(4, new BigDecimal("10.00"), "INTEIRO", "Botão")
        );
        when(materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId)).thenReturn(materiaisPorCategoria);

        // Act
        HomeResponseDTO resultado = homeService.getHomeData(empresaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(4, resultado.materiaisPorCategoria().size());

        // Verifica se todas as categorias esperadas estão presentes
        List<String> categorias = resultado.materiaisPorCategoria().stream()
                .map(UsoMaterialCategoriaDTO::categoria)
                .toList();
        assertTrue(categorias.contains("CENTIMETRO"));
        assertTrue(categorias.contains("GRAMA"));
        assertTrue(categorias.contains("MILILITRO"));
        assertTrue(categorias.contains("INTEIRO"));
    }
}





