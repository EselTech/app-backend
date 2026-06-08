package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.ConversaDTO;
import com.eseltech.appbackendatelie.entity.Conversa;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.repository.ConversaRepository;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários do ConversaService")
class ConversaServiceTest {

    @Mock
    private ConversaRepository conversaRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private ConversaService conversaService;

    private Empresa empresa;
    private LocalDateTime dataHora;

    @BeforeEach
    void setUp() {
        dataHora = LocalDateTime.now();

        empresa = new Empresa("EselTech Ltda", "12345678000190");
        empresa.setId(1); // Empresa usa Integer como ID
    }

    @Test
    @DisplayName("salvarMensagem - Deve lançar exceção quando empresa não encontrada")
    void salvarMensagem_DeveLancarExcecao_QuandoEmpresaNaoEncontrada() {
        // Arrange
        ConversaDTO conversaDTO = new ConversaDTO(null, 1, "Olá, como posso ajudar?", 1, dataHora);
        when(empresaRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            conversaService.salvarMensagem(conversaDTO);
        });

        assertEquals("Empresa não encontrada", exception.getMessage());
        verify(empresaRepository, times(1)).findById(1);
        verify(conversaRepository, never()).save(any(Conversa.class));
    }

    @Test
    @DisplayName("buscarHistorico - Deve retornar lista vazia quando não há conversas")
    void buscarHistorico_DeveRetornarListaVazia_QuandoNaoHaConversas() {
        // Arrange
        when(conversaRepository.findByEmpresaIdOrderByDtHoraConversaAsc(anyInt())).thenReturn(Arrays.asList());

        // Act
        List<ConversaDTO> resultado = conversaService.buscarHistorico(1);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(conversaRepository, times(1)).findByEmpresaIdOrderByDtHoraConversaAsc(1);
    }

    @Test
    @DisplayName("buscarHistorico - Deve retornar conversas quando existem")
    void buscarHistorico_DeveRetornarConversas_QuandoExistem() {
        // Arrange
        Conversa conversa1 = mock(Conversa.class);
        when(conversa1.getId()).thenReturn(1);
        when(conversa1.getEmpresa()).thenReturn(empresa);
        when(conversa1.getMensagem()).thenReturn("Mensagem 1");
        when(conversa1.getEmissor()).thenReturn(1); // Entidade usa Integer
        when(conversa1.getDtHoraConversa()).thenReturn(dataHora);

        List<Conversa> conversas = Arrays.asList(conversa1);
        when(conversaRepository.findByEmpresaIdOrderByDtHoraConversaAsc(anyInt())).thenReturn(conversas);

        // Act
        List<ConversaDTO> resultado = conversaService.buscarHistorico(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Mensagem 1", resultado.get(0).mensagem());

        verify(conversaRepository, times(1)).findByEmpresaIdOrderByDtHoraConversaAsc(1);
    }

    @Test
    @DisplayName("buscarHistorico - Deve chamar repository com ID correto")
    void buscarHistorico_DeveChamarRepositoryComIdCorreto_QuandoChamado() {
        // Arrange
        when(conversaRepository.findByEmpresaIdOrderByDtHoraConversaAsc(anyInt())).thenReturn(Arrays.asList());

        // Act
        conversaService.buscarHistorico(5);

        // Assert
        verify(conversaRepository, times(1)).findByEmpresaIdOrderByDtHoraConversaAsc(5);
    }
}






