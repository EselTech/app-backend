package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

// Para Gráfico de Produtos vendidos anualmente por trimestre
public record ProdutosVendidosTrimestreDTO(
        Long id,
        BigDecimal trimestre1,
        BigDecimal trimestre2,
        BigDecimal trimestre3,
        BigDecimal trimestre4
) {}
