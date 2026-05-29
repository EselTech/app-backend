package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.dash.*;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DashboardService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public DashboardResponseDTO montarDashboard(Integer empresaId) {

        // 1. Materiais (Apenas os KPIs de alertas)
        MaterialKpiDTO materialMais = materialRepository.buscarMaterialMaisUtilizado(empresaId);
        MaterialKpiDTO materialMenos = materialRepository.buscarMaterialMenosUtilizado(empresaId);

        // 2. Produtos
        ProdutoKpiDTO produtoMais = produtoRepository.buscarProdutoMaisEncomendado(empresaId);
        ProdutoKpiDTO produtoMenos = produtoRepository.buscarProdutoMenosEncomendado(empresaId);
        List<ProdutoLucroDTO> produtosLucro = produtoRepository.buscarProdutosMaiorLucro(empresaId);

        // 3. Pedidos e Gráfico Trimestral
        ProdutosVendidosTrimestreDTO produtosPorTrimestre = pedidoRepository.buscarQuantidadeDeProdutosVendidosPorTrimestreNesteAno(empresaId);
        List<ProdutoCrescimentoDTO> maiorCrescimento = pedidoRepository.buscarMaiorCrescimento(empresaId);
        List<ReceitaAnualPorMesDTO> resultadosReceitaAnual = pedidoRepository.buscarReceitaAnual(empresaId);

        List<ReceitaAnualPorMesDTO> receitaAnual = IntStream.rangeClosed(1, 12).mapToObj(mes -> {
                    return resultadosReceitaAnual.stream()
                            .filter(r -> r.mes().equals(mes))
                            .findFirst()
                            .orElse(new ReceitaAnualPorMesDTO(mes, BigDecimal.ZERO));
                })
                .collect(Collectors.toList());

        // Retorna o DTO Pai com a nova estrutura montada
        return new DashboardResponseDTO(
                materialMais,
                materialMenos,
                produtoMais,
                produtoMenos,
                produtosPorTrimestre,
                produtosLucro,
                maiorCrescimento,
                receitaAnual
        );
    }
}