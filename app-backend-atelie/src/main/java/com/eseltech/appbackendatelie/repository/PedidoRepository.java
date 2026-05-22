package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Pedido;
import com.eseltech.appbackendatelie.DTO.dash.ProdutoCrescimentoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query(value = """
        SELECT p.id, p.nome, p.descricao, p.preco, 
               COALESCE(vendas_mes_anterior.total, 0) AS vendas_mes_anterior, 
               COALESCE(vendas_mes_atual.total, 0) AS vendas_mes_atual, 
               CASE WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 AND COALESCE(vendas_mes_atual.total, 0) > 0 THEN 100.00 
                    WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 THEN 0.00 
                    ELSE ROUND(((vendas_mes_atual.total - vendas_mes_anterior.total) / vendas_mes_anterior.total) * 100, 2) END AS taxa_crescimento_percentual 
        FROM produto p 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtdProduto) AS total FROM produtospedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') GROUP BY pp.produto_id) AS vendas_mes_anterior ON p.id = vendas_mes_anterior.produto_id 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtdProduto) AS total FROM produtospedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH GROUP BY pp.produto_id) AS vendas_mes_atual ON p.id = vendas_mes_atual.produto_id 
        WHERE p.empresa_id = :empresaId 
          AND (vendas_mes_anterior.total IS NOT NULL OR vendas_mes_atual.total IS NOT NULL) 
        ORDER BY taxa_crescimento_percentual DESC LIMIT 10
    """, nativeQuery = true)
    List<ProdutoCrescimentoDTO> buscarMaiorCrescimento(@Param("empresaId") Long empresaId);

    @Query(value = """
        SELECT p.id, p.nome, p.descricao, p.preco, 
               COALESCE(vendas_mes_anterior.total, 0) AS vendas_mes_anterior, 
               COALESCE(vendas_mes_atual.total, 0) AS vendas_mes_atual, 
               CASE WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 AND COALESCE(vendas_mes_atual.total, 0) > 0 THEN 100.00 
                    WHEN COALESCE(vendas_mes_anterior.total, 0) = 0 THEN 0.00 
                    ELSE ROUND(((vendas_mes_atual.total - vendas_mes_anterior.total) / vendas_mes_anterior.total) * 100, 2) END AS taxa_crescimento_percentual 
        FROM produto p 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtdProduto) AS total FROM produtospedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') GROUP BY pp.produto_id) AS vendas_mes_anterior ON p.id = vendas_mes_anterior.produto_id 
        LEFT JOIN (SELECT pp.produto_id, SUM(pp.qtdProduto) AS total FROM produtospedido pp JOIN pedido ped ON pp.pedido_id = ped.id WHERE ped.empresa_id = :empresaId AND ped.prazo >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH GROUP BY pp.produto_id) AS vendas_mes_atual ON p.id = vendas_mes_atual.produto_id 
        WHERE p.empresa_id = :empresaId 
          AND (vendas_mes_anterior.total IS NOT NULL OR vendas_mes_atual.total IS NOT NULL) 
        ORDER BY taxa_crescimento_percentual ASC LIMIT 10
    """, nativeQuery = true)
    List<ProdutoCrescimentoDTO> buscarMenorCrescimento(@Param("empresaId") Long empresaId);
}