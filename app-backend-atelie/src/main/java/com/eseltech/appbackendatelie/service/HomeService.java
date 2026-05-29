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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // 3. Gráfico: Produtos Mais Vendidos no Mês
        List<ProdutoMaisVendidoMesDTO> produtosMaisVendidos = produtoRepository.buscarProdutosMaisVendidosNoMes(empresaId);

        // 4. Gráfico: Materiais Mais Usados no Mês (TOP 5 por categoria)
        List<UsoMaterialCategoriaDTO> resultadosMateriais = materialRepository.buscarMaterialMaisUsadoPorCategoria(empresaId);
        List<Categoria> categoriasEsperadas = List.of(Categoria.CENTIMETRO, Categoria.MILILITRO, Categoria.INTEIRO, Categoria.GRAMA);

        List<UsoMaterialCategoriaDTO> materiaisPorCategoria = new ArrayList<>();
        int fakeIdParaSemConsumo = 9999; // ID seguro para não dar conflito no front-end caso mapeiem key

        for (Categoria cat : categoriasEsperadas) {
            // Pega todos os materiais (até 5) que a query já filtrou para esta categoria
            List<UsoMaterialCategoriaDTO> materiaisDestaCategoria = resultadosMateriais.stream()
                    .filter(r -> r.categoria().equalsIgnoreCase(cat.name()))
                    .collect(Collectors.toList());

            if (materiaisDestaCategoria.isEmpty()) {
                // Se não tem material usado, cria o placeholder genérico
                materiaisPorCategoria.add(new UsoMaterialCategoriaDTO(fakeIdParaSemConsumo++, BigDecimal.ZERO, cat.name(), "Sem consumo"));
            } else {
                // Se tem, joga a lista toda (os DTOs puros que vieram do banco com seus IDs reais)
                materiaisPorCategoria.addAll(materiaisDestaCategoria);
            }
        }

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