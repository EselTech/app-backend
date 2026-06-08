package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.dash.*;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do DashboardService")
class DashboardServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private MaterialKpiDTO materialMaisUtilizado;
    private MaterialKpiDTO materialMenosUtilizado;
    private ProdutoKpiDTO produtoMaisEncomendado;
    private ProdutoKpiDTO produtoMenosEncomendado;
    private ProdutosVendidosTrimestreDTO produtosVendidosTrimestre;
    private List<ProdutoLucroDTO> produtosLucro;
    private List<ProdutoCrescimentoDTO> produtosCrescimento;
    private List<ReceitaAnualPorMesDTO> receitaAnual;

    @BeforeEach
    void setUp() {
        materialMaisUtilizado = new MaterialKpiDTO(
                1,
                "Tecido Algodão",
                "Tecido 100% algodão",
                new BigDecimal("50.00"),
                "METRO",
                5L,
                LocalDateTime.now()
        );

        materialMenosUtilizado = new MaterialKpiDTO(
                2,
                "Linha de Seda",
                "Linha especial",
                new BigDecimal("10.00"),
                "METRO",
                0L,
                null
        );

        produtoMaisEncomendado = new ProdutoKpiDTO(
                1,
                "Vestido Floral",
                new BigDecimal("150.00"),
                50L,
                new BigDecimal("100"),
                new BigDecimal("15000.00"),
                new BigDecimal("2.0")
        );

        produtoMenosEncomendado = new ProdutoKpiDTO(
                2,
                "Camisa Básica",
                new BigDecimal("80.00"),
                5L,
                new BigDecimal("5"),
                new BigDecimal("400.00"),
                new BigDecimal("1.0")
        );

        produtosVendidosTrimestre = new ProdutosVendidosTrimestreDTO(
                1L,
                new BigDecimal("100"),
                new BigDecimal("150"),
                new BigDecimal("200"),
                new BigDecimal("180")
        );

        produtosLucro = Arrays.asList(
                new ProdutoLucroDTO(
                        1,
                        "Vestido Floral",
                        new BigDecimal("80.00"),
                        new BigDecimal("150.00"),
                        new BigDecimal("70.00"),
                        new BigDecimal("46.67"),
                        new BigDecimal("50"),
                        new BigDecimal("3500.00")
                ),
                new ProdutoLucroDTO(
                        2,
                        "Saia Midi",
                        new BigDecimal("50.00"),
                        new BigDecimal("100.00"),
                        new BigDecimal("50.00"),
                        new BigDecimal("50.00"),
                        new BigDecimal("30"),
                        new BigDecimal("1500.00")
                )
        );

        produtosCrescimento = Arrays.asList(
                new ProdutoCrescimentoDTO(
                        1,
                        "Vestido Floral",
                        new BigDecimal("150.00"),
                        new BigDecimal("40"),
                        new BigDecimal("50"),
                        new BigDecimal("25.00")
                )
        );

        receitaAnual = Arrays.asList(
                new ReceitaAnualPorMesDTO(1, new BigDecimal("1000.00")),
                new ReceitaAnualPorMesDTO(2, new BigDecimal("1500.00")),
                new ReceitaAnualPorMesDTO(3, new BigDecimal("2000.00"))
        );
    }

    @Test
    @DisplayName("montarDashboard - Deve montar dashboard completo com sucesso")
    void montarDashboard_DeveMontarDashboardCompleto_QuandoDadosValidos() {
        // Arrange
        Integer empresaId = 1;

        when(materialRepository.buscarMaterialMaisUtilizado(anyInt())).thenReturn(materialMaisUtilizado);
        when(materialRepository.buscarMaterialMenosUtilizado(anyInt())).thenReturn(materialMenosUtilizado);
        when(produtoRepository.buscarProdutoMaisEncomendado(anyInt())).thenReturn(produtoMaisEncomendado);
        when(produtoRepository.buscarProdutoMenosEncomendado(anyInt())).thenReturn(produtoMenosEncomendado);
        when(produtoRepository.buscarProdutosMaiorLucro(anyInt())).thenReturn(produtosLucro);
        when(pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(anyInt()))
                .thenReturn(produtosVendidosTrimestre);
        when(pedidoRepository.buscarMaiorCrescimento(anyInt())).thenReturn(produtosCrescimento);
        when(pedidoRepository.buscarReceitaAnual(anyInt())).thenReturn(receitaAnual);

        // Act
        DashboardResponseDTO resultado = dashboardService.montarDashboard(empresaId);

        // Assert
        assertNotNull(resultado);

        // Verifica KPIs de Material
        assertEquals(materialMaisUtilizado, resultado.kpiMaterialMaisUtilizado());
        assertEquals(materialMenosUtilizado, resultado.kpiMaterialMenosUtilizado());

        // Verifica KPIs de Produto
        assertEquals(produtoMaisEncomendado, resultado.kpiProdutoMaisEncomendado());
        assertEquals(produtoMenosEncomendado, resultado.kpiProdutoMenosEncomendado());

        // Verifica produtos vendidos por trimestre
        assertEquals(produtosVendidosTrimestre, resultado.produtosVendidosTrimestre());

        // Verifica gráfico de lucro
        assertEquals(2, resultado.graficoProdutosLucro().size());
        assertEquals(produtosLucro, resultado.graficoProdutosLucro());

        // Verifica gráfico de crescimento
        assertEquals(1, resultado.graficoMaiorCrescimento().size());
        assertEquals(produtosCrescimento, resultado.graficoMaiorCrescimento());

        // Verifica receita anual (deve ter 12 meses)
        assertEquals(12, resultado.receitaAnual().size());

        // Verifica que todos os repositories foram chamados
        verify(materialRepository, times(1)).buscarMaterialMaisUtilizado(empresaId);
        verify(materialRepository, times(1)).buscarMaterialMenosUtilizado(empresaId);
        verify(produtoRepository, times(1)).buscarProdutoMaisEncomendado(empresaId);
        verify(produtoRepository, times(1)).buscarProdutoMenosEncomendado(empresaId);
        verify(produtoRepository, times(1)).buscarProdutosMaiorLucro(empresaId);
        verify(pedidoRepository, times(1)).buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(empresaId);
        verify(pedidoRepository, times(1)).buscarMaiorCrescimento(empresaId);
        verify(pedidoRepository, times(1)).buscarReceitaAnual(empresaId);
    }

    @Test
    @DisplayName("montarDashboard - Deve preencher receita com zeros para meses sem dados")
    void montarDashboard_DevePreencherComZeros_QuandoMesesSemDados() {
        // Arrange
        Integer empresaId = 1;

        // Receita apenas para janeiro e fevereiro
        List<ReceitaAnualPorMesDTO> receitaParcial = Arrays.asList(
                new ReceitaAnualPorMesDTO(1, new BigDecimal("1000.00")),
                new ReceitaAnualPorMesDTO(2, new BigDecimal("1500.00"))
        );

        when(materialRepository.buscarMaterialMaisUtilizado(anyInt())).thenReturn(materialMaisUtilizado);
        when(materialRepository.buscarMaterialMenosUtilizado(anyInt())).thenReturn(materialMenosUtilizado);
        when(produtoRepository.buscarProdutoMaisEncomendado(anyInt())).thenReturn(produtoMaisEncomendado);
        when(produtoRepository.buscarProdutoMenosEncomendado(anyInt())).thenReturn(produtoMenosEncomendado);
        when(produtoRepository.buscarProdutosMaiorLucro(anyInt())).thenReturn(produtosLucro);
        when(pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(anyInt()))
                .thenReturn(produtosVendidosTrimestre);
        when(pedidoRepository.buscarMaiorCrescimento(anyInt())).thenReturn(produtosCrescimento);
        when(pedidoRepository.buscarReceitaAnual(anyInt())).thenReturn(receitaParcial);

        // Act
        DashboardResponseDTO resultado = dashboardService.montarDashboard(empresaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(12, resultado.receitaAnual().size());

        // Verifica que janeiro e fevereiro têm valores
        assertEquals(new BigDecimal("1000.00"), resultado.receitaAnual().get(0).valor());
        assertEquals(new BigDecimal("1500.00"), resultado.receitaAnual().get(1).valor());

        // Verifica que os outros meses têm zero
        for (int i = 2; i < 12; i++) {
            assertEquals(BigDecimal.ZERO, resultado.receitaAnual().get(i).valor());
            assertEquals(i + 1, resultado.receitaAnual().get(i).mes());
        }
    }

    @Test
    @DisplayName("montarDashboard - Deve funcionar com listas vazias de produtos")
    void montarDashboard_DeveFuncionar_QuandoListasVazias() {
        // Arrange
        Integer empresaId = 1;

        when(materialRepository.buscarMaterialMaisUtilizado(anyInt())).thenReturn(materialMaisUtilizado);
        when(materialRepository.buscarMaterialMenosUtilizado(anyInt())).thenReturn(materialMenosUtilizado);
        when(produtoRepository.buscarProdutoMaisEncomendado(anyInt())).thenReturn(produtoMaisEncomendado);
        when(produtoRepository.buscarProdutoMenosEncomendado(anyInt())).thenReturn(produtoMenosEncomendado);
        when(produtoRepository.buscarProdutosMaiorLucro(anyInt())).thenReturn(Collections.emptyList());
        when(pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(anyInt()))
                .thenReturn(produtosVendidosTrimestre);
        when(pedidoRepository.buscarMaiorCrescimento(anyInt())).thenReturn(Collections.emptyList());
        when(pedidoRepository.buscarReceitaAnual(anyInt())).thenReturn(Collections.emptyList());

        // Act
        DashboardResponseDTO resultado = dashboardService.montarDashboard(empresaId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.graficoProdutosLucro().isEmpty());
        assertTrue(resultado.graficoMaiorCrescimento().isEmpty());
        assertEquals(12, resultado.receitaAnual().size());
    }

    @Test
    @DisplayName("montarDashboard - Deve manter ordem correta dos meses na receita anual")
    void montarDashboard_DeveManterOrdemMeses_QuandoReceitaAnual() {
        // Arrange
        Integer empresaId = 1;

        // Receita desordenada
        List<ReceitaAnualPorMesDTO> receitaDesordenada = Arrays.asList(
                new ReceitaAnualPorMesDTO(12, new BigDecimal("5000.00")),
                new ReceitaAnualPorMesDTO(6, new BigDecimal("3000.00")),
                new ReceitaAnualPorMesDTO(1, new BigDecimal("1000.00"))
        );

        when(materialRepository.buscarMaterialMaisUtilizado(anyInt())).thenReturn(materialMaisUtilizado);
        when(materialRepository.buscarMaterialMenosUtilizado(anyInt())).thenReturn(materialMenosUtilizado);
        when(produtoRepository.buscarProdutoMaisEncomendado(anyInt())).thenReturn(produtoMaisEncomendado);
        when(produtoRepository.buscarProdutoMenosEncomendado(anyInt())).thenReturn(produtoMenosEncomendado);
        when(produtoRepository.buscarProdutosMaiorLucro(anyInt())).thenReturn(produtosLucro);
        when(pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(anyInt()))
                .thenReturn(produtosVendidosTrimestre);
        when(pedidoRepository.buscarMaiorCrescimento(anyInt())).thenReturn(produtosCrescimento);
        when(pedidoRepository.buscarReceitaAnual(anyInt())).thenReturn(receitaDesordenada);

        // Act
        DashboardResponseDTO resultado = dashboardService.montarDashboard(empresaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(12, resultado.receitaAnual().size());

        // Verifica ordem sequencial dos meses (1 a 12)
        for (int i = 0; i < 12; i++) {
            assertEquals(i + 1, resultado.receitaAnual().get(i).mes());
        }

        // Verifica valores específicos
        assertEquals(new BigDecimal("1000.00"), resultado.receitaAnual().get(0).valor()); // Janeiro
        assertEquals(new BigDecimal("3000.00"), resultado.receitaAnual().get(5).valor()); // Junho
        assertEquals(new BigDecimal("5000.00"), resultado.receitaAnual().get(11).valor()); // Dezembro
    }

    @Test
    @DisplayName("montarDashboard - Deve incluir todos os KPIs no resultado")
    void montarDashboard_DeveIncluirTodosKPIs_QuandoChamado() {
        // Arrange
        Integer empresaId = 1;

        when(materialRepository.buscarMaterialMaisUtilizado(anyInt())).thenReturn(materialMaisUtilizado);
        when(materialRepository.buscarMaterialMenosUtilizado(anyInt())).thenReturn(materialMenosUtilizado);
        when(produtoRepository.buscarProdutoMaisEncomendado(anyInt())).thenReturn(produtoMaisEncomendado);
        when(produtoRepository.buscarProdutoMenosEncomendado(anyInt())).thenReturn(produtoMenosEncomendado);
        when(produtoRepository.buscarProdutosMaiorLucro(anyInt())).thenReturn(produtosLucro);
        when(pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(anyInt()))
                .thenReturn(produtosVendidosTrimestre);
        when(pedidoRepository.buscarMaiorCrescimento(anyInt())).thenReturn(produtosCrescimento);
        when(pedidoRepository.buscarReceitaAnual(anyInt())).thenReturn(receitaAnual);

        // Act
        DashboardResponseDTO resultado = dashboardService.montarDashboard(empresaId);

        // Assert
        assertNotNull(resultado);

        // Verifica que os KPIs de Material estão presentes e corretos
        assertNotNull(resultado.kpiMaterialMaisUtilizado());
        assertEquals("Tecido Algodão", resultado.kpiMaterialMaisUtilizado().nome());
        assertEquals(5L, resultado.kpiMaterialMaisUtilizado().total_alertas_reposicao());

        assertNotNull(resultado.kpiMaterialMenosUtilizado());
        assertEquals("Linha de Seda", resultado.kpiMaterialMenosUtilizado().nome());
        assertEquals(0L, resultado.kpiMaterialMenosUtilizado().total_alertas_reposicao());

        // Verifica que os KPIs de Produto estão presentes e corretos
        assertNotNull(resultado.kpiProdutoMaisEncomendado());
        assertEquals("Vestido Floral", resultado.kpiProdutoMaisEncomendado().nome());
        assertEquals(50L, resultado.kpiProdutoMaisEncomendado().totalPedidos());

        assertNotNull(resultado.kpiProdutoMenosEncomendado());
        assertEquals("Camisa Básica", resultado.kpiProdutoMenosEncomendado().nome());
        assertEquals(5L, resultado.kpiProdutoMenosEncomendado().totalPedidos());
    }

    @Test
    @DisplayName("montarDashboard - Deve retornar todos os trimestres com valores corretos")
    void montarDashboard_DeveRetornarTrimestres_QuandoChamado() {
        // Arrange
        Integer empresaId = 1;

        ProdutosVendidosTrimestreDTO trimestres = new ProdutosVendidosTrimestreDTO(
                1L,
                new BigDecimal("250.5"),
                new BigDecimal("300.75"),
                new BigDecimal("275.25"),
                new BigDecimal("320.00")
        );

        when(materialRepository.buscarMaterialMaisUtilizado(anyInt())).thenReturn(materialMaisUtilizado);
        when(materialRepository.buscarMaterialMenosUtilizado(anyInt())).thenReturn(materialMenosUtilizado);
        when(produtoRepository.buscarProdutoMaisEncomendado(anyInt())).thenReturn(produtoMaisEncomendado);
        when(produtoRepository.buscarProdutoMenosEncomendado(anyInt())).thenReturn(produtoMenosEncomendado);
        when(produtoRepository.buscarProdutosMaiorLucro(anyInt())).thenReturn(produtosLucro);
        when(pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(anyInt()))
                .thenReturn(trimestres);
        when(pedidoRepository.buscarMaiorCrescimento(anyInt())).thenReturn(produtosCrescimento);
        when(pedidoRepository.buscarReceitaAnual(anyInt())).thenReturn(receitaAnual);

        // Act
        DashboardResponseDTO resultado = dashboardService.montarDashboard(empresaId);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.produtosVendidosTrimestre());
        assertEquals(new BigDecimal("250.5"), resultado.produtosVendidosTrimestre().trimestre1());
        assertEquals(new BigDecimal("300.75"), resultado.produtosVendidosTrimestre().trimestre2());
        assertEquals(new BigDecimal("275.25"), resultado.produtosVendidosTrimestre().trimestre3());
        assertEquals(new BigDecimal("320.00"), resultado.produtosVendidosTrimestre().trimestre4());
    }

    @Test
    @DisplayName("montarDashboard - Deve incluir dados de crescimento dos produtos")
    void montarDashboard_DeveIncluirCrescimento_QuandoChamado() {
        // Arrange
        Integer empresaId = 1;

        List<ProdutoCrescimentoDTO> crescimentos = Arrays.asList(
                new ProdutoCrescimentoDTO(
                        1,
                        "Vestido Floral",
                        new BigDecimal("150.00"),
                        new BigDecimal("40"),
                        new BigDecimal("50"),
                        new BigDecimal("25.00")
                ),
                new ProdutoCrescimentoDTO(
                        2,
                        "Saia Midi",
                        new BigDecimal("100.00"),
                        new BigDecimal("20"),
                        new BigDecimal("30"),
                        new BigDecimal("50.00")
                )
        );

        when(materialRepository.buscarMaterialMaisUtilizado(anyInt())).thenReturn(materialMaisUtilizado);
        when(materialRepository.buscarMaterialMenosUtilizado(anyInt())).thenReturn(materialMenosUtilizado);
        when(produtoRepository.buscarProdutoMaisEncomendado(anyInt())).thenReturn(produtoMaisEncomendado);
        when(produtoRepository.buscarProdutoMenosEncomendado(anyInt())).thenReturn(produtoMenosEncomendado);
        when(produtoRepository.buscarProdutosMaiorLucro(anyInt())).thenReturn(produtosLucro);
        when(pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(anyInt()))
                .thenReturn(produtosVendidosTrimestre);
        when(pedidoRepository.buscarMaiorCrescimento(anyInt())).thenReturn(crescimentos);
        when(pedidoRepository.buscarReceitaAnual(anyInt())).thenReturn(receitaAnual);

        // Act
        DashboardResponseDTO resultado = dashboardService.montarDashboard(empresaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.graficoMaiorCrescimento().size());
        assertEquals(new BigDecimal("25.00"), resultado.graficoMaiorCrescimento().get(0).taxaCrescimentoPercentual());
        assertEquals(new BigDecimal("50.00"), resultado.graficoMaiorCrescimento().get(1).taxaCrescimentoPercentual());
    }
}

