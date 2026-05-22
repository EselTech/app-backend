package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.entity.Material;
import com.eseltech.appbackendatelie.DTO.dash.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query(value = """
        SELECT m.id, m.nome, m.qtd_estoque AS estoque_atual, m.preco, m.metragem AS categoria, 
               COALESCE(SUM(mp.quantidade * pp.qtd_produto), 0) AS consumo_ultimo_mes, 
               CASE WHEN COALESCE(SUM(mp.quantidade * pp.qtd_produto), 0) = 0 THEN 100 
                    ELSE (m.qtd_estoque / SUM(mp.quantidade * pp.qtd_produto)) * 100 END AS margem_estoque_percentual 
        FROM material m 
        LEFT JOIN material_produto mp ON m.id = mp.fk_material 
        LEFT JOIN produto p ON mp.fk_produto = p.id 
        LEFT JOIN produtos_pedido pp ON p.id = pp.produto_id 
        LEFT JOIN pedido ped ON pp.pedido_id = ped.id 
        WHERE m.empresa_id = :empresaId 
          AND (ped.prazo IS NULL OR ped.prazo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH))
        GROUP BY m.id, m.nome, m.qtd_estoque, m.preco, m.metragem 
        HAVING consumo_ultimo_mes > 0 
        ORDER BY margem_estoque_percentual ASC, estoque_atual ASC LIMIT 10
    """, nativeQuery = true)
    List<MaterialEstoqueDTO> buscarMateriaisMenorMargem(@Param("empresaId") Integer empresaId);

    @Query(value = """
        SELECT m.id, m.nome, m.descricao, m.qtd_estoque, m.metragem AS categoria, 
               COUNT(n.id) AS total_alertas_reposicao, MAX(n.dt_envio) AS ultimo_alerta 
        FROM material m 
        JOIN notificacao n ON n.mensagem LIKE CONCAT('%', m.nome, '%') 
        WHERE m.empresa_id = :empresaId 
          AND n.empresa_id = :empresaId 
          AND n.topico = 'Alerta de Estoque' 
          AND n.dt_envio >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) 
        GROUP BY m.id, m.nome, m.descricao, m.qtd_estoque, m.metragem 
        ORDER BY total_alertas_reposicao DESC LIMIT 1
    """, nativeQuery = true)
    MaterialKpiDTO buscarMaterialMaisUtilizado(@Param("empresaId") Integer empresaId);

    @Query(value = """
        SELECT m.id, m.nome, m.descricao, m.qtd_estoque, m.metragem AS categoria, 
               COALESCE(COUNT(n.id), 0) AS total_alertas_reposicao, MAX(n.dt_envio) AS ultimo_alerta 
        FROM material m 
        LEFT JOIN notificacao n ON n.mensagem LIKE CONCAT('%', m.nome, '%') 
             AND n.topico = 'Alerta de Estoque' 
             AND n.dt_envio >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) 
             AND n.empresa_id = :empresaId 
        WHERE m.empresa_id = :empresaId 
        GROUP BY m.id, m.nome, m.descricao, m.qtd_estoque, m.metragem 
        ORDER BY total_alertas_reposicao ASC, m.qtd_estoque DESC LIMIT 1
    """, nativeQuery = true)
    MaterialKpiDTO buscarMaterialMenosUtilizado(@Param("empresaId") Integer empresaId);
}