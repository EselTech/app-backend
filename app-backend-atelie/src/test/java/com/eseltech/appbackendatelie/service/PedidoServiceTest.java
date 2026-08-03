package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.PedidoDTO;
import com.eseltech.appbackendatelie.DTO.ProdutosPedidoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Pedido;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.exceptions.ResourceNotFoundException;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
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
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Empresa empresa;
    private Produto produto1;
    private Produto produto2;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        // Setup Empresa
        empresa = new Empresa();
        empresa.setId(1);
        empresa.setRazaoSocial("Empresa Teste");
        empresa.setCnpj("12345678901234");

        // Setup Produtos
        produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Produto 1");
        produto1.setEmpresa(empresa);

        produto2 = new Produto();
        produto2.setId(2);
        produto2.setNome("Produto 2");
        produto2.setEmpresa(empresa);

        // Setup Pedido
        pedido = new Pedido();
        pedido.setId(1);
        pedido.setNome("Pedido Teste");
        pedido.setDescricao("Descrição do pedido teste");
        pedido.setValor(new BigDecimal("100.00"));
        pedido.setStatus("Em andamento");
        pedido.setPrazo(LocalDate.of(2026, 6, 15));
        pedido.setEmpresa(empresa);
        pedido.setListaProdutos(new ArrayList<>());
    }

    @Test
    void salvarPedido_DeveSalvarSemProdutos() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(
                null,
                1,
                "Pedido Novo",
                "Descrição do pedido",
                new BigDecimal("150.00"),
                "Pendente",
                LocalDate.of(2026, 6, 20),
                null // Sem produtos
        );

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(1);
            return p;
        });

        // Act
        Pedido resultado = pedidoService.salvarPedido(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pedido Novo", resultado.getNome());
        assertEquals("Descrição do pedido", resultado.getDescricao());
        assertEquals(new BigDecimal("150.00"), resultado.getValor());
        assertEquals("Pendente", resultado.getStatus());
        assertNotNull(resultado.getListaProdutos());
        assertTrue(resultado.getListaProdutos().isEmpty());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void salvarPedido_DeveSalvarComMultiplosProdutos() {
        // Arrange
        ProdutosPedidoDTO produtoDTO1 = new ProdutosPedidoDTO(null, null, 1, 10);
        ProdutosPedidoDTO produtoDTO2 = new ProdutosPedidoDTO(null, null, 2, 5);

        PedidoDTO dto = new PedidoDTO(
                null,
                1,
                "Pedido com Produtos",
                "Pedido contendo múltiplos produtos",
                new BigDecimal("200.00"),
                "Em produção",
                LocalDate.of(2026, 7, 1),
                List.of(produtoDTO1, produtoDTO2)
        );

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById(2)).thenReturn(Optional.of(produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(2);
            return p;
        });

        // Act
        Pedido resultado = pedidoService.salvarPedido(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pedido com Produtos", resultado.getNome());
        assertEquals(2, resultado.getListaProdutos().size());
        assertEquals(10, resultado.getListaProdutos().get(0).getQtdProduto());
        assertEquals(5, resultado.getListaProdutos().get(1).getQtdProduto());
        verify(produtoRepository, times(1)).findById(1);
        verify(produtoRepository, times(1)).findById(2);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void salvarPedido_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(
                null, 999, "Pedido", "Descrição",
                BigDecimal.TEN, "Status", LocalDate.now(),
                null
        );

        when(empresaRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.salvarPedido(dto));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void salvarPedido_DeveLancarExcecao_QuandoProdutoNaoExiste() {
        // Arrange
        ProdutosPedidoDTO produtoDTO = new ProdutosPedidoDTO(null, null, 999, 10);
        PedidoDTO dto = new PedidoDTO(
                null, 1, "Pedido", "Descrição",
                BigDecimal.TEN, "Status", LocalDate.now(),
                List.of(produtoDTO)
        );

        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(produtoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.salvarPedido(dto));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void findById_DeveRetornarPedido_QuandoExiste() {
        // Arrange
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));

        // Act
        Pedido resultado = pedidoService.findById(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Pedido Teste", resultado.getNome());
        verify(pedidoRepository, times(1)).findById(1);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(pedidoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.findById(999));
        verify(pedidoRepository, times(1)).findById(999);
    }

    @Test
    void findAll_DeveRetornarLista_QuandoExistemPedidos() {
        // Arrange
        List<Pedido> pedidos = List.of(pedido);
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        // Act
        List<Pedido> resultado = pedidoService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveLancarExcecao_QuandoListaVazia() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.findAll());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void removerPedido_DeveRemover_QuandoExiste() {
        // Arrange
        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        doNothing().when(pedidoRepository).deleteById(1);

        // Act
        pedidoService.removerPedido(1);

        // Assert
        verify(pedidoRepository, times(1)).findById(1);
        verify(pedidoRepository, times(1)).deleteById(1);
    }

    @Test
    void removerPedido_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(pedidoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.removerPedido(999));
        verify(pedidoRepository, times(1)).findById(999);
        verify(pedidoRepository, never()).deleteById(anyInt());
    }

    @Test
    void atualizarPedido_DeveAtualizarComSucesso() {
        // Arrange
        ProdutosPedidoDTO produtoDTO = new ProdutosPedidoDTO(null, null, 1, 20);
        PedidoDTO dto = new PedidoDTO(
                1,
                1,
                "Pedido Atualizado",
                "Descrição atualizada",
                new BigDecimal("250.00"),
                "Concluído",
                LocalDate.of(2026, 8, 1),
                List.of(produtoDTO)
        );

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto1));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Pedido resultado = pedidoService.atualizarPedido(1, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pedido Atualizado", resultado.getNome());
        assertEquals("Descrição atualizada", resultado.getDescricao());
        assertEquals(new BigDecimal("250.00"), resultado.getValor());
        assertEquals("Concluído", resultado.getStatus());
        assertEquals(1, resultado.getListaProdutos().size());
        verify(pedidoRepository, times(1)).findById(1);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void atualizarPedido_DeveLimparProdutosAntesDeAdicionar() {
        // Arrange
        // Adicionar produtos existentes ao pedido
        pedido.getListaProdutos().add(null); // Simular produto antigo
        
        ProdutosPedidoDTO produtoDTO = new ProdutosPedidoDTO(null, null, 2, 15);
        PedidoDTO dto = new PedidoDTO(
                1, 1, "Pedido", "Descrição",
                BigDecimal.TEN, "Status", LocalDate.now(),
                List.of(produtoDTO)
        );

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(empresaRepository.findById(1)).thenReturn(Optional.of(empresa));
        when(produtoRepository.findById(2)).thenReturn(Optional.of(produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Pedido resultado = pedidoService.atualizarPedido(1, dto);

        // Assert
        // Verificar que a lista foi limpa e repovoada
        assertEquals(1, resultado.getListaProdutos().size());
        assertEquals(produto2, resultado.getListaProdutos().getFirst().getProduto());
    }

    @Test
    void atualizarPedido_DeveLancarExcecao_QuandoPedidoNaoExiste() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(
                999, 1, "Pedido", "Descrição",
                BigDecimal.TEN, "Status", LocalDate.now(),
                null
        );

        when(pedidoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.atualizarPedido(999, dto));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void atualizarPedido_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        PedidoDTO dto = new PedidoDTO(
                1, 999, "Pedido", "Descrição",
                BigDecimal.TEN, "Status", LocalDate.now(),
                null
        );

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedido));
        when(empresaRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> pedidoService.atualizarPedido(1, dto));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}








