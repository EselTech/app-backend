package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.DTO.home.ProdutoMaisVendidoMesDTO;
import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.DTO.dash.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query(value = """
        SELECT p.id, p.nome, p.custo, p.preco, 
               (p.preco - p.custo) AS lucro_unitario, 
               ROUND(((p.preco - p.custo) / p.custo) * 100, 2) AS margem_lucro_percentual, 
               COALESCE(SUM(pp.qtd_produto), 0) AS total_vendido_mes, 
               COALESCE(SUM(pp.qtd_produto * (p.preco - p.custo)), 0) AS lucro_total_mes 
        FROM produto p 
        LEFT JOIN produtos_pedido pp ON p.id = pp.produto_id 
        LEFT JOIN pedido ped ON pp.pedido_id = ped.id 
        WHERE p.empresa_id = :empresaId 
          AND (ped.prazo IS NULL OR ped.prazo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) 
        GROUP BY p.id, p.nome, p.custo, p.preco 
        HAVING total_vendido_mes > 0 
        ORDER BY lucro_total_mes DESC LIMIT 10
    """, nativeQuery = true)
    List<ProdutoLucroDTO> buscarProdutosMaiorLucro(@Param("empresaId") Integer empresaId);

    @Query(value = """
        SELECT p.id, p.nome, p.preco, 
               COUNT(DISTINCT ped.id) AS total_pedidos, 
               SUM(pp.qtd_produto) AS total_unidades_vendidas, 
               SUM(pp.qtd_produto * p.preco) AS receita_total, 
               ROUND(SUM(pp.qtd_produto) / COUNT(DISTINCT ped.id), 2) AS media_unidades_por_pedido 
        FROM produto p 
        JOIN produtos_pedido pp ON p.id = pp.produto_id 
        JOIN pedido ped ON pp.pedido_id = ped.id 
        WHERE p.empresa_id = :empresaId 
          AND ped.prazo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) 
        GROUP BY p.id, p.nome, p.preco 
        ORDER BY total_unidades_vendidas DESC LIMIT 1
    """, nativeQuery = true)
    ProdutoKpiDTO buscarProdutoMaisEncomendado(@Param("empresaId") Integer empresaId);

    @Query(value = """
        SELECT p.id, p.nome, p.preco, 
               COUNT(DISTINCT ped.id) AS total_pedidos, 
               SUM(pp.qtd_produto) AS total_unidades_vendidas, 
               SUM(pp.qtd_produto * p.preco) AS receita_total, 
               ROUND(SUM(pp.qtd_produto) / COUNT(DISTINCT ped.id), 2) AS media_unidades_por_pedido 
        FROM produto p 
        JOIN produtos_pedido pp ON p.id = pp.produto_id 
        JOIN pedido ped ON pp.pedido_id = ped.id 
        WHERE p.empresa_id = :empresaId 
          AND ped.prazo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) 
        GROUP BY p.id, p.nome, p.preco 
        ORDER BY total_unidades_vendidas ASC LIMIT 1
    """, nativeQuery = true)
    ProdutoKpiDTO buscarProdutoMenosEncomendado(@Param("empresaId") Integer empresaId);

    @Query(value = """
        SELECT 
            p.id AS id,
            COALESCE(SUM(pp.qtd_produto), 0) AS quantidade,
            p.nome AS nome
        FROM produtos_pedido pp
        INNER JOIN pedido ped ON pp.pedido_id = ped.id
        INNER JOIN produto p ON pp.produto_id = p.id
        WHERE ped.empresa_id = :empresaId
          AND ped.status = 'shipped'
          AND ped.prazo >= DATE_FORMAT(CURDATE(), '%Y-%m-01')
          AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH
        GROUP BY p.id, p.nome
        ORDER BY quantidade DESC
        LIMIT 5
    """, nativeQuery = true)
    List<ProdutoMaisVendidoMesDTO> buscarProdutosMaisVendidosNoMes(@Param("empresaId") Integer empresaId);
}