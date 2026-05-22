package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.dash.*;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public DashboardResponseDTO montarDashboard(Integer empresaId) {

        // 1. Materiais
        MaterialKpiDTO materialMais = materialRepository.buscarMaterialMaisUtilizado(empresaId);
        MaterialKpiDTO materialMenos = materialRepository.buscarMaterialMenosUtilizado(empresaId);
        List<MaterialEstoqueDTO> materiaisMargem = materialRepository.buscarMateriaisMenorMargem(empresaId);

        // 2. Produtos
        ProdutoKpiDTO produtoMais = produtoRepository.buscarProdutoMaisEncomendado(empresaId);
        ProdutoKpiDTO produtoMenos = produtoRepository.buscarProdutoMenosEncomendado(empresaId);
        List<ProdutoLucroDTO> produtosLucro = produtoRepository.buscarProdutosMaiorLucro(empresaId);

        // 3. Pedidos (Crescimento)
        List<ProdutoCrescimentoDTO> maiorCrescimento = pedidoRepository.buscarMaiorCrescimento(empresaId);
        List<ProdutoCrescimentoDTO> menorCrescimento = pedidoRepository.buscarMenorCrescimento(empresaId);


        return new DashboardResponseDTO(
                materialMais,
                materialMenos,
                produtoMais,
                produtoMenos,
                materiaisMargem,
                produtosLucro,
                maiorCrescimento,
                menorCrescimento
        );
    }
}