package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.ConversaDTO;
import com.eseltech.appbackendatelie.entity.Conversa;
import com.eseltech.appbackendatelie.entity.Empresa;
import com.eseltech.appbackendatelie.repository.ConversaRepository;
import com.eseltech.appbackendatelie.repository.EmpresaRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversaService {

    @Autowired
    private ConversaRepository conversaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Operation(summary = "Salvar nova mensagem", description = "Persiste a mensagem enviada pelo usuário ou sistema no banco de dados vinculado à empresa")
    public ConversaDTO salvarMensagem(ConversaDTO dto) {

        Empresa empresa = empresaRepository.findById(dto.empresaId().longValue())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Conversa conversa = new Conversa(
                empresa,
                dto.mensagem(),
                dto.emissor(),
                dto.dtHoraConversa()
        );

        Conversa salva = conversaRepository.save(conversa);

        return new ConversaDTO(
                salva.getId(),
                salva.getEmpresa().getId().intValue(),
                salva.getMensagem(),
                salva.getEmissor(),
                salva.getDtHoraConversa()
        );
    }

    @Operation(summary = "Buscar histórico", description = "Retorna a lista completa de mensagens de uma empresa ordenadas cronologicamente")
    public List<ConversaDTO> buscarHistorico(Integer empresaId) {
        return conversaRepository.findByEmpresaIdOrderByDtHoraConversaAsc(empresaId)
                .stream()
                .map(c -> new ConversaDTO(
                        c.getId(),
                        c.getEmpresa().getId().intValue(),
                        c.getMensagem(),
                        c.getEmissor(),
                        c.getDtHoraConversa()
                ))
                .collect(Collectors.toList());
    }
}