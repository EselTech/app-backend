package com.eseltech.appbackendatelie.DTO.home;

import java.math.BigDecimal;

public record UsoMaterialCategoriaDTO(
        Integer id,
        BigDecimal valorTotal,
        String categoria,
        String nome
) {}