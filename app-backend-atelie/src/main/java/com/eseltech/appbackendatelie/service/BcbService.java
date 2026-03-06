package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.BcbDataResponseDTO;
import com.eseltech.appbackendatelie.entity.Imposto;
import com.eseltech.appbackendatelie.repository.ImpostoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela integração com a API do Banco Central do Brasil (BCB).
 * Utiliza o Sistema Gerenciador de Séries Temporais (SGS) para consultar valores de impostos.
 *
 * <p>Este serviço realiza chamadas HTTP à API pública do BCB para obter dados
 * atualizados de séries temporais relacionadas a impostos e indicadores econômicos.</p>
 *
 * @author EselTech
 * @version 1.0.0
 * @since 2024
 * @see <a href="https://api.bcb.gov.br">API do Banco Central do Brasil</a>
 */
@Service
public class BcbService {
    @Autowired
    private ImpostoRepository impostoRepository;
    private final WebClient webClient;

    /**
     * Construtor do serviço BCB.
     *
     * @param webClient Cliente HTTP reativo para realizar chamadas à API do BCB
     */
    public BcbService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Busca os valores atualizados de todos os impostos cadastrados na API do BCB.
     *
     * <p>Este método:</p>
     * <ol>
     *   <li>Recupera todos os impostos cadastrados no banco de dados</li>
     *   <li>Para cada imposto, consulta o código SGS na API do BCB</li>
     *   <li>Retorna os valores do último mês para cada imposto</li>
     * </ol>
     *
     * @return Lista de DTOs contendo os dados de resposta da API do BCB para cada imposto
     * @throws RuntimeException se houver erro ao acessar a API do BCB
     */
    public List<BcbDataResponseDTO> buscarPorImposto() {
        List<Imposto> todosOsImpostos = impostoRepository.findAll();
        LocalDate dataFim = LocalDate.now();
        LocalDate dataInicio = dataFim.minusMonths(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<BcbDataResponseDTO> responseDTOS = new ArrayList<>();

        for (Imposto imposto : todosOsImpostos) {
            List<BcbDataResponseDTO> dtos = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/dados/serie/bcdata.sgs.{codigo}/dados")
                            .queryParam("formato", "json")
                            .queryParam("dataInicial", dataInicio.format(formatter))
                            .queryParam("dataFinal", dataFim.format(formatter))
                            .build(imposto.getCodigoSgs()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            Mono.error(new RuntimeException("Erro ao acessar API do BCB: " + response.statusCode())))
                    .bodyToFlux(BcbDataResponseDTO.class)
                    .collectList()
                    .block();
            
            for (BcbDataResponseDTO dto : dtos) {
                dto.setImposto(imposto);
                responseDTOS.add(dto);
            }
        }

        return responseDTOS;
    }
}
