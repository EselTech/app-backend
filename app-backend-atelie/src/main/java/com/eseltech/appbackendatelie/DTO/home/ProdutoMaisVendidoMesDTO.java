package com.eseltech.appbackendatelie.DTO.home;

import java.math.BigDecimal;

public record ProdutoMaisVendidoMesDTO(
        Long id,
        BigDecimal quantidade,
        String nome
) {}