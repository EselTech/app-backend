package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

// Para Gráfico de Produtos com Maior Lucro
public record ProdutoLucroDTO(
        Long id,
        String nome,
        BigDecimal custo,
        BigDecimal preco,
        BigDecimal lucroUnitario,
        BigDecimal margemLucroPercentual,
        BigDecimal totalVendidoMes,
        BigDecimal lucroTotalMes
) {}
