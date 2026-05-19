package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.DTO.MaterialProdutoDTO;
import com.eseltech.appbackendatelie.DTO.ProdutoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.entity.enums.Categoria;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private ImpostoService impostoService;

    @Mock
    private TwilioService twilioService;

    @InjectMocks
    private ProdutoService produtoService;

    private Empresa empresa;
    private Material material;
    private Produto produto;

    @BeforeEach
    void setUp() {
        // Setup Empresa
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setRazaoSocial("Empresa Teste");
        empresa.setCnpj("12345678901234");

        // Setup Material
        material = new Material();
        material.setId(1L);
        material.setNome("Papel Cartão");
        material.setDescricao("Papel cartão vermelho");
        material.setCategoria(Categoria.CENTIMETRO);
        material.setQtdEstoque(new BigDecimal("100.00"));
        material.setPreco(new BigDecimal("50.00"));
        material.setEmpresa(empresa);

        // Setup Produto
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Sacola Personalizada");
        produto.setDescricao("Sacola com estampa");
        produto.setEmpresa(empresa);
        produto.setCusto(new BigDecimal("15.00"));
        produto.setPreco(new BigDecimal("30.00"));
        produto.setListaMateriais(new ArrayList<>());
    }

    @Test
    void salvarProduto_DeveRetornarProdutoComCalculoCorreto() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1L, new BigDecimal("10.00"));
        List<MaterialProdutoDTO> materiais = List.of(materialDTO);

        ProdutoDTO dto = new ProdutoDTO(
                null,
                1L,
                "Sacola Personalizada",
                "Sacola com estampa",
                new BigDecimal("15.00"),
                new BigDecimal("30.00"),
                new BigDecimal("5.00"), // custoMaoDeObra
                new BigDecimal("50.00"), // margemLucroPercentual
                materiais
        );

        HistoricoImpostoDTO impostoDTO = new HistoricoImpostoDTO(
                "IPCA",
                new BigDecimal("4.50"), // 4.5%
                LocalDate.of(2026, 5, 1)
        );

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(impostoService.buscarTodos()).thenReturn(List.of(impostoDTO));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> {
            Produto p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });
        doNothing().when(twilioService).sendMessage(anyString());

        // Act
        Produto resultado = produtoService.salvarProduto(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Sacola Personalizada", resultado.getNome());
        assertEquals(1, resultado.getListaMateriais().size());

        // Validar cálculo: custoMaterial = (50/100)*10 = 5.00
        // custoTotalBase = 5.00 + 5.00 = 10.00
        // impostos = 10.00 * 0.045 = 0.45
        // custoComImpostos = 10.00 + 0.45 = 10.45
        // precoSugerido = 10.45 * 1.50 = 15.675 -> 15.68
        BigDecimal custoEsperado = new BigDecimal("10.45");
        BigDecimal precoEsperado = new BigDecimal("15.68");

        assertEquals(custoEsperado, resultado.getCusto());
        assertEquals(precoEsperado, resultado.getPreco());

        verify(produtoRepository, times(1)).save(any(Produto.class));
        verify(twilioService, times(1)).sendMessage(anyString());
    }

    @Test
    void salvarProduto_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        ProdutoDTO dto = new ProdutoDTO(
                null, 999L, "Produto", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                BigDecimal.ZERO, new ArrayList<>()
        );

        when(empresaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.salvarProduto(dto));
        verify(produtoRepository, never()).save(any(Produto.class));
        verify(twilioService, never()).sendMessage(anyString());
    }

    @Test
    void salvarProduto_DeveLancarExcecao_QuandoMaterialNaoExiste() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(999L, new BigDecimal("10.00"));
        ProdutoDTO dto = new ProdutoDTO(
                null, 1L, "Produto", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                BigDecimal.ZERO, List.of(materialDTO)
        );

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(materialRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.salvarProduto(dto));
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void salvarProduto_DeveTratarCustoMaoDeObraEMargemNulos() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1L, new BigDecimal("10.00"));
        ProdutoDTO dto = new ProdutoDTO(
                null, 1L, "Produto", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN,
                null, // custoMaoDeObra nulo
                null, // margemLucroPercentual nulo
                List.of(materialDTO)
        );

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(twilioService).sendMessage(anyString());

        // Act
        Produto resultado = produtoService.salvarProduto(dto);

        // Assert
        assertNotNull(resultado);
        // custoMaterial = (50/100)*10 = 5.00
        // custoTotalBase = 5.00 + 0 = 5.00
        // sem IPCA: custoComImpostos = 5.00
        // sem margem (1.00): precoSugerido = 5.00
        assertEquals(new BigDecimal("5.00"), resultado.getCusto());
        assertEquals(new BigDecimal("5.00"), resultado.getPreco());
    }

    @Test
    void salvarProduto_DeveCalcularComValoresDecimaisPequenos() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1L, new BigDecimal("0.01"));
        ProdutoDTO dto = new ProdutoDTO(
                null, 1L, "Produto", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN,
                new BigDecimal("0.01"),
                new BigDecimal("0.01"),
                List.of(materialDTO)
        );

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));
        doNothing().when(twilioService).sendMessage(anyString());

        // Act
        Produto resultado = produtoService.salvarProduto(dto);

        // Assert
        assertNotNull(resultado);
        // Verificar que o cálculo foi feito e arredondado corretamente
        assertEquals(2, resultado.getCusto().scale());
        assertEquals(2, resultado.getPreco().scale());
    }

    @Test
    void findById_DeveRetornarProduto_QuandoExiste() {
        // Arrange
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // Act
        Produto resultado = produtoService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Sacola Personalizada", resultado.getNome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.findById(999L));
        verify(produtoRepository, times(1)).findById(999L);
    }

    @Test
    void findAll_DeveRetornarLista_QuandoExistemProdutos() {
        // Arrange
        List<Produto> produtos = List.of(produto);
        when(produtoRepository.findAll()).thenReturn(produtos);

        // Act
        List<Produto> resultado = produtoService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveLancarExcecao_QuandoListaVazia() {
        // Arrange
        when(produtoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.findAll());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void removerProduto_DeveRemover_QuandoExiste() {
        // Arrange
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).deleteById(1L);

        // Act
        produtoService.removerProduto(1L);

        // Assert
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void removerProduto_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.removerProduto(999L));
        verify(produtoRepository, times(1)).findById(999L);
        verify(produtoRepository, never()).deleteById(anyLong());
    }

    @Test
    void atualizarProduto_DeveAtualizarComSucesso() {
        // Arrange
        MaterialProdutoDTO materialDTO = new MaterialProdutoDTO(1L, new BigDecimal("15.00"));
        ProdutoDTO dto = new ProdutoDTO(
                1L, 1L, "Produto Atualizado", "Nova descrição",
                BigDecimal.TEN, BigDecimal.TEN,
                new BigDecimal("10.00"),
                new BigDecimal("30.00"),
                List.of(materialDTO)
        );

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        when(impostoService.buscarTodos()).thenReturn(new ArrayList<>());
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Produto resultado = produtoService.atualizarProduto(1L, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Atualizado", resultado.getNome());
        assertEquals("Nova descrição", resultado.getDescricao());
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void atualizarProduto_DeveLancarExcecao_QuandoProdutoNaoExiste() {
        // Arrange
        ProdutoDTO dto = new ProdutoDTO(
                999L, 1L, "Produto", "Descrição",
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                BigDecimal.ZERO, new ArrayList<>()
        );

        when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> produtoService.atualizarProduto(999L, dto));
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void salvarProdutoSimplificado_DeveSalvarComSucesso() {
        // Arrange
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> {
            Produto p = i.getArgument(0);
            p.setId(2L);
            return p;
        });

        // Act
        Produto resultado = produtoService.salvarProdutoSimplificado(
                1L, "Produto Simples", "Descrição simples",
                new BigDecimal("10.00"), new BigDecimal("20.00")
        );

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Simples", resultado.getNome());
        assertEquals(new BigDecimal("10.00"), resultado.getCusto());
        assertEquals(new BigDecimal("20.00"), resultado.getPreco());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void salvarProdutoShopee_DeveSalvarComCustoEstimado() {
        // Arrange
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> {
            Produto p = i.getArgument(0);
            p.setId(3L);
            return p;
        });

        // Act
        Produto resultado = produtoService.salvarProdutoShopee(
                1L, "Produto Shopee", "Da Shopee",
                new BigDecimal("100.00")
        );

        // Assert
        assertNotNull(resultado);
        assertEquals("Produto Shopee", resultado.getNome());
        assertEquals(new BigDecimal("100.00"), resultado.getPreco());
        // Custo estimado = preco  * 0.8 = 100 * 0.8 = 80.000 (com 3 casas decimais)
        assertEquals(new BigDecimal("80.000"), resultado.getCusto());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }
}







