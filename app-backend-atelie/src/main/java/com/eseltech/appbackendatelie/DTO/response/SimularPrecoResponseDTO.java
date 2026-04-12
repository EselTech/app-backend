package com.eseltech.appbackendatelie.DTO.response;

import java.math.BigDecimal;

public record SimularPrecoResponseDTO(
        BigDecimal custoMateriais,
        BigDecimal custoMaoDeObra,
        BigDecimal valorImpostos,
        BigDecimal custoTotal,
        BigDecimal precoSugerido // Preço final para venda
) {}
