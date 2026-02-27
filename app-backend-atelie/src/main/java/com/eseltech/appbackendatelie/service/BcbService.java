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

@Service
public class BcbService {
    @Autowired
    private ImpostoRepository impostoRepository;
    private final WebClient webClient;

    public BcbService(WebClient webClient) {
        this.webClient = webClient;
    }

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
