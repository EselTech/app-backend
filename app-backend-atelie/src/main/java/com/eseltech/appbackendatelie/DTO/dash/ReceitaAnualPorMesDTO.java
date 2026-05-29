package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

public record ReceitaAnualPorMesDTO(
        Integer mes,
        BigDecimal valor
) {}
