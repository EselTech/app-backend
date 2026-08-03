package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.BcbDataResponseDTO;
import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.entity.HistoricoImposto;
import com.eseltech.appbackendatelie.entity.Imposto;
import com.eseltech.appbackendatelie.repository.HistoricoImpostoRepository;
import com.eseltech.appbackendatelie.repository.ImpostoRepository;
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
class ImpostoServiceTest {

    @Mock
    private ImpostoRepository impostoRepository;

    @Mock
    private HistoricoImpostoRepository historicoImpostoRepository;

    @Mock
    private BcbService bcbService;

    private ImpostoService impostoService;

    private Imposto imposto;
    private HistoricoImposto historicoImposto;

    @BeforeEach
    void setUp() {
        // Criar o service manualmente com todos os mocks
        impostoService = new ImpostoService(impostoRepository, bcbService, historicoImpostoRepository);

        // Setup Imposto
        imposto = new Imposto();
        imposto.setId(1);
        imposto.setNomeImposto("IPCA");
        imposto.setCodigoSgs(433);

        // Setup HistoricoImposto
        historicoImposto = new HistoricoImposto();
        historicoImposto.setId(1);
        historicoImposto.setImposto(imposto);
        historicoImposto.setValor(new BigDecimal("4.50"));
        historicoImposto.setDataRegistro(LocalDate.of(2026, 5, 1));
    }

    @Test
    void atualizarImpostos_DeveAtualizarComSucesso() {
        // Arrange
        BcbDataResponseDTO bcbData = new BcbDataResponseDTO();
        bcbData.setImposto(imposto);
        bcbData.setValor("4.50");
        bcbData.setData(LocalDate.of(2026, 5, 1));

        List<BcbDataResponseDTO> dadosBcb = List.of(bcbData);

        when(bcbService.buscarPorImposto()).thenReturn(dadosBcb);
        doNothing().when(historicoImpostoRepository).deleteAll();
        when(historicoImpostoRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        // Act
        impostoService.atualizarImpostos();

        // Assert
        verify(bcbService, times(1)).buscarPorImposto();
        verify(historicoImpostoRepository, times(1)).deleteAll();
        verify(historicoImpostoRepository, times(1)).saveAll(anyList());
    }

    @Test
    void atualizarImpostos_DeveConverterCorretamente() {
        // Arrange
        BcbDataResponseDTO bcbData1 = new BcbDataResponseDTO();
        bcbData1.setImposto(imposto);
        bcbData1.setValor("4.50");
        bcbData1.setData(LocalDate.of(2026, 5, 1));

        Imposto imposto2 = new Imposto();
        imposto2.setId(2);
        imposto2.setNomeImposto("SELIC");
        imposto2.setCodigoSgs(4390);

        BcbDataResponseDTO bcbData2 = new BcbDataResponseDTO();
        bcbData2.setImposto(imposto2);
        bcbData2.setValor("10.75");
        bcbData2.setData(LocalDate.of(2026, 5, 1));

        when(bcbService.buscarPorImposto()).thenReturn(List.of(bcbData1, bcbData2));
        doNothing().when(historicoImpostoRepository).deleteAll();
        when(historicoImpostoRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        // Act
        impostoService.atualizarImpostos();

        // Assert
        verify(historicoImpostoRepository).saveAll(argThat(list -> {
            List<HistoricoImposto> historicos = (List<HistoricoImposto>) list;
            return historicos.size() == 2 &&
                   historicos.getFirst().getValor().equals(new BigDecimal("4.50")) &&
                   historicos.get(1).getValor().equals(new BigDecimal("10.75"));
        }));
    }

    @Test
    void buscarTodos_DeveRetornarListaComDados() {
        // Arrange
        when(impostoRepository.findAll()).thenReturn(List.of(imposto));
        when(historicoImpostoRepository.acharRecentePorImpostoId(1)).thenReturn(historicoImposto);

        // Act
        List<HistoricoImpostoDTO> resultado = impostoService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("IPCA", resultado.getFirst().nomeImposto());
        assertEquals(new BigDecimal("4.50"), resultado.getFirst().valor());
        assertEquals(LocalDate.of(2026, 5, 1), resultado.getFirst().dataRegistro());
        verify(impostoRepository, times(1)).findAll();
    }

    @Test
    void buscarTodos_DeveRetornarListaVaziaQuandoSemHistorico() {
        // Arrange
        when(impostoRepository.findAll()).thenReturn(List.of(imposto));
        when(historicoImpostoRepository.acharRecentePorImpostoId(1)).thenReturn(null);

        // Act
        List<HistoricoImpostoDTO> resultado = impostoService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarTodos_DeveRetornarListaVaziaQuandoSemImpostos() {
        // Arrange
        when(impostoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<HistoricoImpostoDTO> resultado = impostoService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(historicoImpostoRepository, never()).acharRecentePorImpostoId(anyInt());
    }

    @Test
    void buscarTodos_DeveIgnorarImpostosSemHistorico() {
        // Arrange
        Imposto imposto2 = new Imposto();
        imposto2.setId(2);
        imposto2.setNomeImposto("SELIC");

        when(impostoRepository.findAll()).thenReturn(List.of(imposto, imposto2));
        when(historicoImpostoRepository.acharRecentePorImpostoId(1)).thenReturn(historicoImposto);
        when(historicoImpostoRepository.acharRecentePorImpostoId(2)).thenReturn(null);

        // Act
        List<HistoricoImpostoDTO> resultado = impostoService.buscarTodos();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("IPCA", resultado.getFirst().nomeImposto());
    }

    @Test
    void toDTO_DeveConverterCorretamente() {
        // Arrange
        List<HistoricoImposto> historicos = List.of(historicoImposto);

        // Act
        List<HistoricoImpostoDTO> resultado = impostoService.toDTO(historicos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("IPCA", resultado.getFirst().nomeImposto());
        assertEquals(new BigDecimal("4.50"), resultado.getFirst().valor());
        assertEquals(LocalDate.of(2026, 5, 1), resultado.getFirst().dataRegistro());
    }

    @Test
    void toDTO_DeveConverterListaVazia() {
        // Arrange
        List<HistoricoImposto> historicos = new ArrayList<>();

        // Act
        List<HistoricoImpostoDTO> resultado = impostoService.toDTO(historicos);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void toDTO_DeveConverterMultiplosHistoricos() {
        // Arrange
        Imposto imposto2 = new Imposto();
        imposto2.setId(2);
        imposto2.setNomeImposto("SELIC");

        HistoricoImposto historico2 = new HistoricoImposto();
        historico2.setId(2);
        historico2.setImposto(imposto2);
        historico2.setValor(new BigDecimal("10.75"));
        historico2.setDataRegistro(LocalDate.of(2026, 5, 1));

        List<HistoricoImposto> historicos = List.of(historicoImposto, historico2);

        // Act
        List<HistoricoImpostoDTO> resultado = impostoService.toDTO(historicos);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("IPCA", resultado.getFirst().nomeImposto());
        assertEquals("SELIC", resultado.get(1).nomeImposto());
    }

    @Test
    void inserirImposto_DeveSalvarComSucesso() {
        // Arrange
        when(impostoRepository.save(any(Imposto.class))).thenReturn(imposto);

        // Act
        String resultado = impostoService.inserirImposto(imposto);

        // Assert
        assertEquals("Salvo com sucesso!", resultado);
        verify(impostoRepository, times(1)).save(imposto);
    }

    @Test
    void deletarImposto_DeveDeletarComSucesso() {
        // Arrange
        doNothing().when(historicoImpostoRepository).deleteByImpostoId(1);
        doNothing().when(impostoRepository).deleteById(1);

        // Act
        impostoService.deletarImposto(1);

        // Assert
        verify(historicoImpostoRepository, times(1)).deleteByImpostoId(1);
        verify(impostoRepository, times(1)).deleteById(1);
    }

    @Test
    void buscarTodosImpostos_DeveRetornarTodos() {
        // Arrange
        Imposto imposto2 = new Imposto();
        imposto2.setId(2);
        imposto2.setNomeImposto("SELIC");

        when(impostoRepository.findAll()).thenReturn(List.of(imposto, imposto2));

        // Act
        List<Imposto> resultado = impostoService.buscarTodosImpostos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(impostoRepository, times(1)).findAll();
    }

    @Test
    void atualizarImposto_DeveAtualizarComSucesso() {
        // Arrange
        Imposto impostoAtualizado = new Imposto();
        impostoAtualizado.setNomeImposto("IPCA Atualizado");
        impostoAtualizado.setCodigoSgs(434);

        when(impostoRepository.findById(1)).thenReturn(Optional.of(imposto));
        doNothing().when(historicoImpostoRepository).deleteByImpostoId(1);
        when(impostoRepository.save(any(Imposto.class))).thenReturn(imposto);

        // Act
        impostoService.atualizarImposto(impostoAtualizado, 1);

        // Assert
        verify(impostoRepository, times(1)).findById(1);
        verify(historicoImpostoRepository, times(1)).deleteByImpostoId(1);
        verify(impostoRepository, times(1)).save(any(Imposto.class));
        assertEquals("IPCA Atualizado", imposto.getNomeImposto());
        assertEquals(434, imposto.getCodigoSgs());
    }

    @Test
    void atualizarImposto_DeveLancarExcecao_QuandoNaoExiste() {
        // Arrange
        Imposto impostoAtualizado = new Imposto();
        lenient().when(impostoRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> impostoService.atualizarImposto(impostoAtualizado, 999));
        verify(impostoRepository, never()).save(any(Imposto.class));
    }

    @Test
    void buscarPorNome_DeveRetornarImpostosFiltrados() {
        // Arrange
        when(impostoRepository.findByNomeImpostoContainingIgnoreCase("ipca"))
                .thenReturn(List.of(imposto));

        // Act
        List<Imposto> resultado = impostoService.buscarPorNome("ipca");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("IPCA", resultado.getFirst().getNomeImposto());
        verify(impostoRepository, times(1)).findByNomeImpostoContainingIgnoreCase("ipca");
    }

    @Test
    void buscarPorNome_DeveRetornarListaVazia_QuandoNaoEncontra() {
        // Arrange
        when(impostoRepository.findByNomeImpostoContainingIgnoreCase("xyz"))
                .thenReturn(new ArrayList<>());

        // Act
        List<Imposto> resultado = impostoService.buscarPorNome("xyz");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void atualizarImpostos_DeveLimparHistoricoAntes() {
        // Arrange
        BcbDataResponseDTO bcbData = new BcbDataResponseDTO();
        bcbData.setImposto(imposto);
        bcbData.setValor("5.00");
        bcbData.setData(LocalDate.of(2026, 5, 2));

        when(bcbService.buscarPorImposto()).thenReturn(List.of(bcbData));
        doNothing().when(historicoImpostoRepository).deleteAll();
        when(historicoImpostoRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        // Act
        impostoService.atualizarImpostos();

        // Assert
        // Verificar que deleteAll é chamado antes de saveAll
        var inOrder = inOrder(historicoImpostoRepository);
        inOrder.verify(historicoImpostoRepository).deleteAll();
        inOrder.verify(historicoImpostoRepository).saveAll(anyList());
    }
}




