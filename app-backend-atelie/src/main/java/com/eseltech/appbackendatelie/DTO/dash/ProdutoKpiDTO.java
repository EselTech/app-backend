package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

// Para KPIs de Produto
public record ProdutoKpiDTO(
        Long id,
        String nome,
        BigDecimal preco,
        Long totalPedidos,
        BigDecimal totalUnidadesVendidas,
        BigDecimal receitaTotal,
        BigDecimal mediaUnidadesPorPedido
) {}
