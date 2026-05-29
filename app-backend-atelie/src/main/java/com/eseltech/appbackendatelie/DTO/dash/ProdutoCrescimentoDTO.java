package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

// Para Gráficos de Crescimento (CAGR)
public record ProdutoCrescimentoDTO(
        Long id,
        String nome,
        BigDecimal preco,
        BigDecimal vendasMesAnterior,
        BigDecimal vendasMesAtual,
        BigDecimal taxaCrescimentoPercentual
) {}
