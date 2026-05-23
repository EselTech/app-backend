package com.eseltech.appbackendatelie.service;

import com.eseltech.appbackendatelie.DTO.home.*;
import com.eseltech.appbackendatelie.repository.PedidoRepository;
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

    @Operation(summary = "Obter dados consolidados da Home", description = "Busca as métricas financeiras, gráfico de status e receita anual")
    public HomeResponseDTO getHomeData(Integer empresaId) {

        // 1. KPIs
        BigDecimal receita = repository.somarReceitaMesAtual(empresaId);
        BigDecimal despesas = repository.somarDespesasMesAtual(empresaId);
        BigDecimal lucro = receita.subtract(despesas);
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

        // 3. Gráfico: Receita Anual (12 meses com zeros preenchidos)
        List<ReceitaAnualPorMesDTO> resultadosReceitaAnual = repository.buscarReceitaAnual(empresaId);

        List<ReceitaAnualPorMesDTO> receitaAnual = IntStream.rangeClosed(1, 12).mapToObj(mes -> {
                    return resultadosReceitaAnual.stream()
                            .filter(r -> r.mes().equals(mes))
                            .findFirst()
                            .orElse(new ReceitaAnualPorMesDTO(mes, BigDecimal.ZERO));
                })
                .collect(Collectors.toList());

        // 4. Retorno consolidado
        return new HomeResponseDTO(
                new DespesaKPIDTO(despesas),
                new LucroKPIDTO(lucro),
                new ReceberKPIDTO(aReceber),
                new ReceitaKPIDTO(receita),
                pedidosPorStatus,
                receitaAnual
        );
    }
}