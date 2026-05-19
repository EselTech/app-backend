package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.OrcamentoDTO;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.entity.Orcamento;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import com.eseltech.appbackendatelie.repository.OrcamentoRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
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
class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private OrcamentoService orcamentoService;

    private Empresa empresa;
    private Orcamento orcamento;

    @BeforeEach
    void setUp() {
        // Setup Empresa
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setRazaoSocial("Empresa Teste");
        empresa.setCnpj("12345678901234");

        // Setup Orcamento
        orcamento = new Orcamento();
        orcamento.setId(1);
        orcamento.setTitulo("Orçamento Teste");
        orcamento.setCliente("Cliente Teste");
        orcamento.setValor(new BigDecimal("500.00"));
        orcamento.setEmpresa(empresa);
    }

    @Test
    void salvaOrcamento_DeveSalvarComSucesso() {
        // Arrange
        OrcamentoDTO dto = new OrcamentoDTO(
                null,
                1L,
                "Orçamento Novo",
                "Cliente ABC",
                new BigDecimal("750.00")
        );

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(i -> {
            Orcamento o = i.getArgument(0);
            o.setId(2);
            return o;
        });

        // Act
        Orcamento resultado = orcamentoService.salvaOrcamento(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Orçamento Novo", resultado.getTitulo());
        assertEquals("Cliente ABC", resultado.getCliente());
        assertEquals(new BigDecimal("750.00"), resultado.getValor());
        assertEquals(empresa, resultado.getEmpresa());
        verify(orcamentoRepository, times(1)).save(any(Orcamento.class));
    }

    @Test
    void salvaOrcamento_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        OrcamentoDTO dto = new OrcamentoDTO(
                null, 999L, "Orçamento", "Cliente",
                BigDecimal.TEN
        );

        when(empresaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orcamentoService.salvaOrcamento(dto));
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    void findById_DeveRetornarOrcamento_QuandoExiste() {
        // Arrange
        when(orcamentoRepository.findById(1L)).thenReturn(Optional.of(orcamento));

        // Act
        Orcamento resultado = orcamentoService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Orçamento Teste", resultado.getTitulo());
        verify(orcamentoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(orcamentoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orcamentoService.findById(999L));
        verify(orcamentoRepository, times(1)).findById(999L);
    }

    @Test
    void findAll_DeveRetornarLista_QuandoExistemOrcamentos() {
        // Arrange
        List<Orcamento> orcamentos = List.of(orcamento);
        when(orcamentoRepository.findAll()).thenReturn(orcamentos);

        // Act
        List<Orcamento> resultado = orcamentoService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orcamentoRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveLancarExcecao_QuandoListaVazia() {
        // Arrange
        when(orcamentoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orcamentoService.findAll());
        verify(orcamentoRepository, times(1)).findAll();
    }

    @Test
    void atualizarOrcamento_DeveAtualizarComSucesso() {
        // Arrange
        OrcamentoDTO dto = new OrcamentoDTO(
                1L,
                1L,
                "Orçamento Atualizado",
                "Cliente Atualizado",
                new BigDecimal("1000.00")
        );

        when(orcamentoRepository.findById(1L)).thenReturn(Optional.of(orcamento));
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Orcamento resultado = orcamentoService.atualizarOrcamento(1L, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Orçamento Atualizado", resultado.getTitulo());
        assertEquals("Cliente Atualizado", resultado.getCliente());
        assertEquals(new BigDecimal("1000.00"), resultado.getValor());
        verify(orcamentoRepository, times(1)).findById(1L);
        verify(orcamentoRepository, times(1)).save(any(Orcamento.class));
    }

    @Test
    void atualizarOrcamento_DeveLancarExcecao_QuandoOrcamentoNaoExiste() {
        // Arrange
        OrcamentoDTO dto = new OrcamentoDTO(
                999L, 1L, "Orçamento", "Cliente",
                BigDecimal.TEN
        );

        when(orcamentoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orcamentoService.atualizarOrcamento(999L, dto));
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    void atualizarOrcamento_DeveLancarExcecao_QuandoEmpresaNaoExiste() {
        // Arrange
        OrcamentoDTO dto = new OrcamentoDTO(
                1L, 999L, "Orçamento", "Cliente",
                BigDecimal.TEN
        );

        when(orcamentoRepository.findById(1L)).thenReturn(Optional.of(orcamento));
        when(empresaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orcamentoService.atualizarOrcamento(1L, dto));
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    void removerOrcamento_DeveRemover_QuandoExiste() {
        // Arrange
        when(orcamentoRepository.findById(1L)).thenReturn(Optional.of(orcamento));
        doNothing().when(orcamentoRepository).deleteById(1L);

        // Act
        orcamentoService.removerOrcamento(1L);

        // Assert
        verify(orcamentoRepository, times(1)).findById(1L);
        verify(orcamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void removerOrcamento_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        when(orcamentoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orcamentoService.removerOrcamento(999L));
        verify(orcamentoRepository, times(1)).findById(999L);
        verify(orcamentoRepository, never()).deleteById(anyLong());
    }

    @Test
    void salvaOrcamento_DeveAceitarValoresDecimais() {
        // Arrange
        OrcamentoDTO dto = new OrcamentoDTO(
                null, 1L, "Orçamento Decimal",
                "Cliente", new BigDecimal("1234.56")
        );

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Orcamento resultado = orcamentoService.salvaOrcamento(dto);

        // Assert
        assertEquals(new BigDecimal("1234.56"), resultado.getValor());
    }

    @Test
    void atualizarOrcamento_DeveManterEmpresaOriginalSeNecessario() {
        // Arrange
        Empresa novaEmpresa = new Empresa();
        novaEmpresa.setId(2L);
        novaEmpresa.setRazaoSocial("Nova Empresa");
        novaEmpresa.setCnpj("98765432109876");

        OrcamentoDTO dto = new OrcamentoDTO(
                1L, 2L, "Orçamento", "Cliente",
                new BigDecimal("500.00")
        );

        when(orcamentoRepository.findById(1L)).thenReturn(Optional.of(orcamento));
        when(empresaRepository.findById(2L)).thenReturn(Optional.of(novaEmpresa));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Orcamento resultado = orcamentoService.atualizarOrcamento(1L, dto);

        // Assert
        assertEquals(novaEmpresa, resultado.getEmpresa());
        assertEquals(2L, resultado.getEmpresa().getId());
    }
}



