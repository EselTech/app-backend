package com.eseltech.appbackendatelie.DTO.dash;

import java.util.List;

public record DashboardResponseDTO(

        MaterialKpiDTO kpiMaterialMaisUtilizado,
        MaterialKpiDTO kpiMaterialMenosUtilizado,
        ProdutoKpiDTO kpiProdutoMaisEncomendado,
        ProdutoKpiDTO kpiProdutoMenosEncomendado,
        ProdutosVendidosTrimestreDTO produtosVendidosTrimestre,

        List<ProdutoLucroDTO> graficoProdutosLucro,
        List<ProdutoCrescimentoDTO> graficoMaiorCrescimento,
        List<ProdutoCrescimentoDTO> graficoMenorCrescimento
) {}