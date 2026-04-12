package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.HistoricoImpostoDTO;
import com.eseltech.appbackendatelie.DTO.MaterialProdutoDTO;
import com.eseltech.appbackendatelie.DTO.request.SimularPrecoRequestDTO;
import com.eseltech.appbackendatelie.DTO.response.SimularPrecoResponseDTO;
import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SimulacaoService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ImpostoService impostoService;

    public SimularPrecoResponseDTO simularPreco(SimularPrecoRequestDTO request) {

        BigDecimal custoMateriais = BigDecimal.ZERO;
        for (MaterialProdutoDTO materialProdutoDTO : request.materiais()) {
            Material material = materialRepository.findById(materialProdutoDTO.materialId()).orElseThrow();
            BigDecimal custoDesteMaterial = material.getPreco()
                    .divide(material.getQtdEstoque(), 4, RoundingMode.HALF_UP)
                    .multiply(materialProdutoDTO.quantidade());
            custoMateriais = custoMateriais.add(custoDesteMaterial);
        }

        BigDecimal custoTotalBase = custoMateriais.add(request.custoMaoDeObra());

        List<HistoricoImpostoDTO> impostos = impostoService.buscarTodos();

        BigDecimal taxaIpca = impostos.stream()
                .filter(i -> i.nomeImposto().contains("IPCA"))
                .map(HistoricoImpostoDTO::valor) // Traz o valor salvo, ex: 0.45%
                .findFirst()
                .orElse(BigDecimal.ZERO);

        BigDecimal multiplicadorImposto = taxaIpca.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        BigDecimal valorImpostos = custoTotalBase.multiply(multiplicadorImposto);

        BigDecimal custoComImposto = custoTotalBase.add(valorImpostos);
        BigDecimal multiplicadorLucro = request.margemLucroPercentual().divide(new BigDecimal("100"), RoundingMode.HALF_UP).add(BigDecimal.ONE); // Ex: 30% vira 1.30

        BigDecimal precoSugerido = custoComImposto.multiply(multiplicadorLucro).setScale(2, RoundingMode.HALF_UP);

        return new SimularPrecoResponseDTO(
                custoMateriais.setScale(2, RoundingMode.HALF_UP),
                request.custoMaoDeObra().setScale(2, RoundingMode.HALF_UP),
                valorImpostos.setScale(2, RoundingMode.HALF_UP),
                custoComImposto.setScale(2, RoundingMode.HALF_UP),
                precoSugerido
        );
    }
}
