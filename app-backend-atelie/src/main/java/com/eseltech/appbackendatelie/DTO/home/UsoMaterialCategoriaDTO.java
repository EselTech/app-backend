package com.eseltech.appbackendatelie.DTO.home;

import java.math.BigDecimal;

public record UsoMaterialCategoriaDTO(
        Long id,
        BigDecimal valorTotal,
        String categoria,
        String nome
) {}