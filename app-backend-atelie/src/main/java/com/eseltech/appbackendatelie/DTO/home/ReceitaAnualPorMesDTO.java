package com.eseltech.appbackendatelie.DTO.home;

import java.math.BigDecimal;

public record ReceitaAnualPorMesDTO(
        Integer mes,
        BigDecimal valor
) {}
