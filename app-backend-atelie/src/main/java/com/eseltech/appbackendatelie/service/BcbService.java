package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.BcbDataResponseDTO;
import com.eseltech.appbackendatelie.DTO.response.BcbMicroserviceResponseDTO;
import com.eseltech.appbackendatelie.entity.Imposto;
import com.eseltech.appbackendatelie.repository.ImpostoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
    private final RestTemplate restTemplate;

    public BcbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BcbDataResponseDTO> buscarPorImposto() {
        List<Imposto> todosOsImpostos = impostoRepository.findAll();
        List<BcbDataResponseDTO> responseDTOS = new ArrayList<>();

        for (Imposto imposto : todosOsImpostos) {
            String url = "http://localhost:8081/api/bcb/impostos/" + imposto.getCodigoSgs();

            BcbMicroserviceResponseDTO responseDTO = restTemplate.getForObject(url, BcbMicroserviceResponseDTO.class);

            if (responseDTO != null) {
                BcbDataResponseDTO bcbDataResponseDTO = new BcbDataResponseDTO();
                bcbDataResponseDTO.setImposto(imposto);
                bcbDataResponseDTO.setValor(responseDTO.valorTaxa() + "");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataAtualizacao = LocalDate.parse(responseDTO.dataAtualizacao(), formatter);
                bcbDataResponseDTO.setData(dataAtualizacao);
                responseDTOS.add(bcbDataResponseDTO);
            }
        }
        return responseDTOS;
    }
}
