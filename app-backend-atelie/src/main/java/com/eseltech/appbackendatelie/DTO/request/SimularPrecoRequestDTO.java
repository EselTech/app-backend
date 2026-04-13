package com.eseltech.appbackendatelie.DTO.request;

import com.eseltech.appbackendatelie.DTO.MaterialProdutoDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Validated
public record SimularPrecoRequestDTO(
        @NotNull(message = "A lista de materiais é obrigatória")
        @NotEmpty(message = "A lista de materiais não deve estar vazia")
        List<MaterialProdutoDTO> materiais,

        @NotNull(message = "O custo de mão de obra é obrigatório")
        @Positive(message = "O custo de mão de obra deve ser positivo")
        BigDecimal custoMaoDeObra,

        @NotNull(message = "A margem de lucro é obrigatória")
        @Positive(message = "A margem de lucro deve ser positiva")
        BigDecimal margemLucroPercentual // Ex: 30 para 30%
) {}
