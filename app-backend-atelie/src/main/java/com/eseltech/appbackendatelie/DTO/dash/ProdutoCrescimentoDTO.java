package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

// Para Gráficos de Crescimento (CAGR)
public record ProdutoCrescimentoDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer vendasMesAnterior,
        Integer vendasMesAtual,
        Double taxaCrescimentoPercentual,
        Integer variacaoAbsoluta
) {}
