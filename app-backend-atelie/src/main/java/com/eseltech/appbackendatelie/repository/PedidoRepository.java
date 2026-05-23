package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.DTO.home.PedidoPorStatusDTO;
import com.eseltech.appbackendatelie.DTO.home.ReceitaAnualPorMesDTO;
import com.eseltech.appbackendatelie.entity.Pedido;
import com.eseltech.appbackendatelie.DTO.dash.ProdutoCrescimentoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query(value = """
        SELECT p.id, p.nome, p.preco, 
               COALESCE(vendas_mes_anterior.total, 0) AS vendas_mes_anterior, 
               COALESCE(vendas_mes_atual.total, 0) AS vendas_mes_atual, 
               CASE WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 AND COALESCE(vendas_mes_atual.total, 0) > 0 THEN 100.00 
                    WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 THEN 0.00 
                    ELSE ROUND(((vendas_mes_atual.total - vendas_mes_anterior.total) / vendas_mes_anterior.total) * 100, 2) END AS taxa_crescimento_percentual 
        FROM produto p 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtd_produto) AS total FROM produtos_pedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') GROUP BY pp.produto_id) AS vendas_mes_anterior ON p.id = vendas_mes_anterior.produto_id 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtd_produto) AS total FROM produtos_pedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH GROUP BY pp.produto_id) AS vendas_mes_atual ON p.id = vendas_mes_atual.produto_id 
        WHERE p.empresa_id = :empresaId 
          AND (vendas_mes_anterior.total IS NOT NULL OR vendas_mes_atual.total IS NOT NULL) 
        ORDER BY taxa_crescimento_percentual DESC LIMIT 10
    """, nativeQuery = true)
    List<ProdutoCrescimentoDTO> buscarMaiorCrescimento(@Param("empresaId") Integer empresaId);

    @Query(value = """
        SELECT p.id, p.nome, p.preco, 
               COALESCE(vendas_mes_anterior.total, 0) AS vendas_mes_anterior, 
               COALESCE(vendas_mes_atual.total, 0) AS vendas_mes_atual, 
               CASE WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 AND COALESCE(vendas_mes_atual.total, 0) > 0 THEN 100.00 
                    WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 THEN 0.00 
                    ELSE ROUND(((vendas_mes_atual.total - vendas_mes_anterior.total) / vendas_mes_anterior.total) * 100, 2) END AS taxa_crescimento_percentual 
        FROM produto p 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtd_produto) AS total FROM produtos_pedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') GROUP BY pp.produto_id) AS vendas_mes_anterior ON p.id = vendas_mes_anterior.produto_id 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtd_produto) AS total FROM produtos_pedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH GROUP BY pp.produto_id) AS vendas_mes_atual ON p.id = vendas_mes_atual.produto_id 
        WHERE p.empresa_id = :empresaId 
          AND (vendas_mes_anterior.total IS NOT NULL OR vendas_mes_atual.total IS NOT NULL) 
        ORDER BY taxa_crescimento_percentual ASC LIMIT 10
    """, nativeQuery = true)
    List<ProdutoCrescimentoDTO> buscarMenorCrescimento(@Param("empresaId") Integer empresaId);

    @Query(value = """
    SELECT COALESCE(SUM(p.valor), 0)
    FROM pedido p
    WHERE p.empresa_id = :empresaId 
      AND p.status = 'shipped' 
      AND p.prazo >= DATE_FORMAT(CURDATE(), '%Y-%m-01') 
      AND p.prazo <= CURDATE()
""", nativeQuery = true)
    BigDecimal somarReceitaMesAtual(@Param("empresaId") Integer empresaId);

    @Query(value = """
    SELECT COALESCE(SUM(pp.qtdProduto * p.custo), 0)
    FROM produtos_pedido pp
    JOIN pedido ped ON pp.pedido_id = ped.id
    JOIN produto p ON pp.produto_id = p.id
    WHERE ped.empresa_id = :empresaId
      AND ped.status IN ('shipped', 'ongoing', 'late')
      AND ped.prazo >= DATE_FORMAT(CURDATE(), '%Y-%m-01')
      AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH
""", nativeQuery = true)
    BigDecimal somarDespesasMesAtual(@Param("empresaId") Integer empresaId);

    @Query(value = """
    SELECT 
        (COALESCE(SUM(ped.valor), 0) - COALESCE(SUM(pp.qtdProduto * p.custo), 0)) AS lucro
    FROM pedido ped
    LEFT JOIN produtos_pedido pp ON ped.id = pp.pedido_id
    LEFT JOIN produto p ON pp.produto_id = p.id
    WHERE ped.empresa_id = :empresaId
      AND ped.status IN ('shipped', 'ongoing', 'late')
      AND ped.prazo >= DATE_FORMAT(CURDATE(), '%Y-%m-01')
      AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH
""", nativeQuery = true)
    BigDecimal somarLucroMesAtual(@Param("empresaId") Integer empresaId);

    @Query(value = """
    SELECT COALESCE(SUM(p.valor), 0) 
    FROM pedido p 
    WHERE p.empresa_id = :empresaId 
      AND p.status IN ('shipped', 'ongoing', 'open', 'late')
      AND p.prazo > CURDATE()
      AND p.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH
""", nativeQuery = true)
    BigDecimal somarValorAReceberMesAtual(@Param("empresaId") Integer empresaId);

    @Query(value = """
    SELECT 
        status, 
        COUNT(id) AS total
    FROM pedido
    WHERE empresa_id = :empresaId
      AND prazo >= DATE_FORMAT(CURDATE(), '%Y-%m-01')
      AND prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH
    GROUP BY status
""", nativeQuery = true)
    List<PedidoPorStatusDTO> contarPedidosPorStatus(@Param("empresaId") Integer empresaId);

    @Query(value = """
    SELECT 
        MONTH(ped.prazo) AS mes, 
        COALESCE(SUM(ped.valor), 0) AS total
    FROM pedido ped
    WHERE ped.empresa_id = :empresaId
      AND YEAR(ped.prazo) = YEAR(CURDATE())
      AND ped.status = 'shipped'
    GROUP BY MONTH(ped.prazo)
    ORDER BY mes ASC
""", nativeQuery = true)
    List<ReceitaAnualPorMesDTO> buscarReceitaAnual(@Param("empresaId") Integer empresaId);
}