package com.eseltech.appbackendatelie.DTO.home;

import java.math.BigDecimal;

public record ProdutoMaisVendidoMesDTO(
        Integer id,
        BigDecimal quantidade,
        String nome
) {}