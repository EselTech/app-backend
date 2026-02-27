package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.entity.Imposto;
import org.springframework.beans.factory.annotation.Autowired;
import com.eseltech.appbackendatelie.DTO.BcbDataResponseDTO;
import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.repository.HistoricoImpostoRepository;
import com.eseltech.appbackendatelie.entity.HistoricoImposto;
import org.springframework.stereotype.Service;
import com.eseltech.appbackendatelie.repository.ImpostoRepository;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ImpostoService {
    @Autowired
    private ImpostoRepository impostoRepository;
    private final BcbService bcbService;
    private final HistoricoImpostoRepository historicoImpostoRepository;

    public ImpostoService(BcbService bcbService, HistoricoImpostoRepository HistoricoImpostoRepository) {
        this.bcbService = bcbService;

        this.historicoImpostoRepository = HistoricoImpostoRepository;
    }

    public void atualizarImpostos() {

        List<BcbDataResponseDTO> rawData = bcbService.buscarPorImposto();

        List<HistoricoImposto> entidades = rawData.stream()
                .map(registro -> new HistoricoImposto(
                        registro.getImposto(),
                        new BigDecimal(registro.getValor()),
                        registro.getData()
                )).toList();

        historicoImpostoRepository.deleteAll();
        historicoImpostoRepository.saveAll(entidades);
    }

    public List<HistoricoImpostoDTO> buscarTodos() {
        return historicoImpostoRepository.findAll().stream()
                .map(entity -> new HistoricoImpostoDTO(
                        entity.getImposto().getId(),
                        entity.getImposto().getNomeImposto(),
                        entity.getValor(),
                        entity.getDataRegistro()
                )).toList();
    }

    public String inserirImposto(Imposto imposto) {

        impostoRepository.save(imposto);
        return "Salvo com sucesso!";

    }
}
