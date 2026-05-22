package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

// Para Gráfico de Materiais com Menor Margem
public record MaterialEstoqueDTO(
        Long id,
        String nome,
        BigDecimal estoqueAtual,
        BigDecimal preco,
        String categoria,
        BigDecimal consumoUltimoMes,
        Double margemEstoquePercentual
) {}
