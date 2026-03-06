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

/**
 * Serviço responsável pela lógica de negócio relacionada aos impostos.
 * Gerencia operações de CRUD de impostos e integração com o Banco Central do Brasil.
 *
 * @author EselTech
 * @version 1.0.0
 * @since 2024
 */
@Service
public class ImpostoService {
    @Autowired
    private ImpostoRepository impostoRepository;
    private final BcbService bcbService;
    private final HistoricoImpostoRepository historicoImpostoRepository;

    /**
     * Construtor do serviço de impostos.
     *
     * @param bcbService Serviço de integração com o BCB
     * @param HistoricoImpostoRepository Repositório de histórico de impostos
     */
    public ImpostoService(BcbService bcbService, HistoricoImpostoRepository HistoricoImpostoRepository) {
        this.bcbService = bcbService;

        this.historicoImpostoRepository = HistoricoImpostoRepository;
    }

    /**
     * Atualiza os valores dos impostos consultando a API do Banco Central do Brasil.
     * Remove todos os registros existentes e insere os novos valores obtidos.
     *
     * Este método:
     * 1. Busca os dados atualizados de todos os impostos via API do BCB
     * 2. Converte os dados para entidades de histórico
     * 3. Limpa o histórico existente
     * 4. Salva os novos registros
     */
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

    /**
     * Busca todos os impostos cadastrados com seus valores mais recentes.
     *
     * @return Lista de DTOs contendo informações dos impostos com valores atualizados
     */
    public List<HistoricoImpostoDTO> buscarTodos() {
        List<HistoricoImposto> listHistorico = new ArrayList<>();

        List<Imposto> listImposto = impostoRepository.findAll();

        for (Imposto imposto : listImposto) {

            listHistorico.add(historicoImpostoRepository.acharRecentePorImpostoId(imposto.getId()));

        }

        return toDTO(listHistorico);
    }

    /**
     * Converte uma lista de entidades HistoricoImposto para uma lista de DTOs.
     *
     * @param listaHistorico Lista de entidades a serem convertidas
     * @return Lista de DTOs com os dados formatados
     */
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

    /**
     * Insere um novo imposto no sistema.
     *
     * @param imposto Entidade do imposto a ser inserido
     * @return Mensagem de confirmação do salvamento
     */
    public String inserirImposto(Imposto imposto) {

        impostoRepository.save(imposto);
        return "Salvo com sucesso!";

    }

    /**
     * Remove um imposto do sistema pelo seu ID.
     * Remove também todo o histórico associado ao imposto.
     *
     * @param id Identificador único do imposto a ser removido
     */
    public void deletarImposto(Integer id) {
        historicoImpostoRepository.deleteByImpostoId(id);
        impostoRepository.deleteById(id);
    }
}
