package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;

// Para KPIs de Produto
public record ProdutoKpiDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer totalPedidos,
        Integer totalUnidadesVendidas,
        BigDecimal receitaTotal,
        Double mediaUnidadesPorPedido
) {}
