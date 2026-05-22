package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Produto;
import com.eseltech.appbackendatelie.DTO.dash.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(value = """
        SELECT p.id, p.nome, p.descricao, p.custo, p.preco, 
               (p.preco - p.custo) AS lucro_unitario, 
               ROUND(((p.preco - p.custo) / p.custo) * 100, 2) AS margem_lucro_percentual, 
               COALESCE(SUM(pp.qtdProduto), 0) AS total_vendido_mes, 
               COALESCE(SUM(pp.qtdProduto * (p.preco - p.custo)), 0) AS lucro_total_mes 
        FROM produto p 
        LEFT JOIN produtospedido pp ON p.id = pp.produto_id 
        LEFT JOIN pedido ped ON pp.pedido_id = ped.id 
        WHERE p.empresa_id = :empresaId 
          AND (ped.prazo IS NULL OR ped.prazo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) 
        GROUP BY p.id, p.nome, p.descricao, p.custo, p.preco 
        HAVING total_vendido_mes > 0 
        ORDER BY lucro_total_mes DESC LIMIT 10
    """, nativeQuery = true)
    List<ProdutoLucroDTO> buscarProdutosMaiorLucro(@Param("empresaId") Long empresaId);

    @Query(value = """
        SELECT p.id, p.nome, p.descricao, p.preco, 
               COUNT(DISTINCT ped.id) AS total_pedidos, 
               SUM(pp.qtdProduto) AS total_unidades_vendidas, 
               SUM(pp.qtdProduto * p.preco) AS receita_total, 
               ROUND(SUM(pp.qtdProduto) / COUNT(DISTINCT ped.id), 2) AS media_unidades_por_pedido 
        FROM produto p 
        JOIN produtospedido pp ON p.id = pp.produto_id 
        JOIN pedido ped ON pp.pedido_id = ped.id 
        WHERE p.empresa_id = :empresaId 
          AND ped.prazo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) 
        GROUP BY p.id, p.nome, p.descricao, p.preco 
        ORDER BY total_unidades_vendidas DESC LIMIT 1
    """, nativeQuery = true)
    ProdutoKpiDTO buscarProdutoMaisEncomendado(@Param("empresaId") Long empresaId);

    @Query(value = """
        SELECT p.id, p.nome, p.descricao, p.preco, 
               COUNT(DISTINCT ped.id) AS total_pedidos, 
               SUM(pp.qtdProduto) AS total_unidades_vendidas, 
               SUM(pp.qtdProduto * p.preco) AS receita_total, 
               ROUND(SUM(pp.qtdProduto) / COUNT(DISTINCT ped.id), 2) AS media_unidades_por_pedido 
        FROM produto p 
        JOIN produtospedido pp ON p.id = pp.produto_id 
        JOIN pedido ped ON pp.pedido_id = ped.id 
        WHERE p.empresa_id = :empresaId 
          AND ped.prazo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) 
        GROUP BY p.id, p.nome, p.descricao, p.preco 
        ORDER BY total_unidades_vendidas ASC LIMIT 1
    """, nativeQuery = true)
    ProdutoKpiDTO buscarProdutoMenosEncomendado(@Param("empresaId") Long empresaId);
}