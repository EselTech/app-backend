package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.dash.ReceitaAnualPorMesDTO;
import com.eseltech.appbackendatelie.DTO.home.*;
import com.eseltech.appbackendatelie.entity.enums.Categoria;
import com.eseltech.appbackendatelie.repository.MaterialRepository;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
import com.eseltech.appbackendatelie.repository.ProdutoRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HomeService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Operation(summary = "Obter dados consolidados da Home", description = "Busca as métricas financeiras, gráfico de status e receita anual")
    public HomeResponseDTO getHomeData(Integer empresaId) {

        // 1. KPIs
        BigDecimal receita = repository.somarReceitaMesAtual(empresaId);
        BigDecimal despesas = repository.somarDespesasMesAtual(empresaId);
        BigDecimal lucro = repository.somarLucroMesAtual(empresaId);
        BigDecimal aReceber = repository.somarValorAReceberMesAtual(empresaId);

        // 2. Gráfico: Pedidos por Status
        List<PedidoPorStatusDTO> resultadosGrafico = repository.contarPedidosPorStatus(empresaId);
        List<String> statusEsperados = List.of("shipped", "ongoing", "open", "late");

        List<PedidoPorStatusDTO> pedidosPorStatus = statusEsperados.stream().map(status -> {
            return resultadosGrafico.stream()
                    .filter(r -> r.status().equals(status))
                    .findFirst()
                    .orElse(new PedidoPorStatusDTO(status, 0L));
        }).collect(Collectors.toList());

        // 3. Gráfico: Produtos Mais Vendidos no Mês (pegar a quantidade) e Materiais Mais Usados no Mês
        List<ProdutoMaisVendidoMesDTO> produtosMaisVendidos = produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId);

        List<UsoMaterialCategoriaDTO> resultadosMateriais = materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId);
        List<Categoria> categoriasEsperadas = List.of(Categoria.CENTIMETRO, Categoria.MILILITRO, Categoria.INTEIRO, Categoria.GRAMA);

        List<UsoMaterialCategoriaDTO> materiaisPorCategoria = IntStream.range(0, categoriasEsperadas.size()).mapToObj(index -> {
            Categoria cat = categoriasEsperadas.get(index);
            Long id = (long) (index + 1);

            return resultadosMateriais.stream()
                    .filter(r -> r.categoria().equalsIgnoreCase(cat.name()))
                    .findFirst()
                    .map(r -> new UsoMaterialCategoriaDTO(id, r.valorTotal(), cat.name(), r.nome()))
                    .orElse(new UsoMaterialCategoriaDTO(id, BigDecimal.ZERO, cat.name(), "Sem consumo"));
        }).collect(Collectors.toList());


        // 5. Retorno consolidado
        return new HomeResponseDTO(
                new DespesaKPIDTO(despesas),
                new LucroKPIDTO(lucro),
                new ReceberKPIDTO(aReceber),
                new ReceitaKPIDTO(receita),
                pedidosPorStatus,
                produtosMaisVendidos,
                materiaisPorCategoria
        );
    }
}