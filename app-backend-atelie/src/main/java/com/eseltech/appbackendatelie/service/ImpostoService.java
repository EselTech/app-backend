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
import java.util.ArrayList;
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
        List<HistoricoImposto> listHistorico = new ArrayList<>();

        List<Imposto> listImposto = impostoRepository.findAll();

        for (Imposto imposto : listImposto) {

            listHistorico.add(historicoImpostoRepository.acharRecentePorImpostoId(imposto.getId()));

        }

        return toDTO(listHistorico);
    }

    public List<HistoricoImpostoDTO> toDTO(List<HistoricoImposto> listaHistorico) {
        List<HistoricoImpostoDTO> lista = new ArrayList<>();
        for (HistoricoImposto historicoImposto : listaHistorico) {
            HistoricoImpostoDTO dto = new HistoricoImpostoDTO();

            dto.setImpostoId(historicoImposto.getId());

            dto.setNomeImposto(historicoImposto.getImposto().getNomeImposto());
            dto.setDataRegistro(historicoImposto.getDataRegistro());
            dto.setValor(historicoImposto.getValor());
            dto.setId(historicoImposto.getId());
            lista.add(dto);
        }
        return lista;
    }

    public String inserirImposto(Imposto imposto) {

        impostoRepository.save(imposto);
        return "Salvo com sucesso!";

    }
}
