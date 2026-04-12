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
            HistoricoImposto historico = historicoImpostoRepository.acharRecentePorImpostoId(imposto.getId());
            if (historico != null) {
                listHistorico.add(historico);
            }
        }

        return toDTO(listHistorico);
    }

    public List<HistoricoImpostoDTO> toDTO(List<HistoricoImposto> listaHistorico) {
        List<HistoricoImpostoDTO> lista = new ArrayList<>();
        for (HistoricoImposto historicoImposto : listaHistorico) {
            HistoricoImpostoDTO dto = new HistoricoImpostoDTO(
                    historicoImposto.getImposto().getNomeImposto(),
                    historicoImposto.getValor(),
                    historicoImposto.getDataRegistro()
            );
            lista.add(dto);
        }
        return lista;
    }

    public String inserirImposto(Imposto imposto) {

        impostoRepository.save(imposto);
        return "Salvo com sucesso!";

    }

    public void deletarImposto(Integer id) {
        historicoImpostoRepository.deleteByImpostoId(id);
        impostoRepository.deleteById(id);
    }

    public List<Imposto> buscarTodosImpostos() {
        return impostoRepository.findAll();
    }

    public void atualizarImposto(Imposto imposto, Integer id) {
        Imposto impostoExistente = impostoRepository.findById(id).orElseThrow(() -> new RuntimeException("Imposto não encontrado com id: " + id));
        impostoExistente.setNomeImposto(imposto.getNomeImposto());
        impostoExistente.setCodigoSgs(imposto.getCodigoSgs());
        historicoImpostoRepository.deleteByImpostoId(id);
        impostoRepository.save(impostoExistente);
    }

    public List<Imposto> buscarPorNome(String nome) {
        return impostoRepository.findByNomeImpostoContainingIgnoreCase(nome);
    }
}
