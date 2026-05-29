package com.eseltech.appbackendatelie.repository;

import com.eseltech.appbackendatelie.DTO.home.UsoMaterialCategoriaDTO;
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

    @Query(value = """
        SELECT 
            temp.id, 
            temp.categoria, 
            temp.valorTotal, 
            temp.nome
        FROM (
            SELECT 
                m.id AS id,
                m.metragem AS categoria, 
                m.nome AS nome,
                COALESCE(SUM(pp.qtd_produto * mp.quantidade), 0) AS valorTotal,
                ROW_NUMBER() OVER (
                    PARTITION BY m.metragem 
                    ORDER BY SUM(pp.qtd_produto * mp.quantidade) DESC
                ) AS rn 
            FROM pedido ped
            INNER JOIN produtos_pedido pp ON ped.id = pp.pedido_id
            INNER JOIN material_produto mp ON pp.produto_id = mp.fk_produto
            INNER JOIN material m ON mp.fk_material = m.id
            WHERE ped.empresa_id = :empresaId
              AND ped.status = 'shipped'
              AND ped.prazo >= DATE_FORMAT(CURDATE(), '%Y-%m-01')
              AND ped.prazo < DATE_FORMAT(CURDATE(), '%Y-%m-01') + INTERVAL 1 MONTH
            GROUP BY m.id, m.nome, m.metragem 
        ) AS temp
        WHERE temp.rn = 1
    """, nativeQuery = true)
    List<UsoMaterialCategoriaDTO> buscarMaterialMaisUsadoPorCategoria(@Param("empresaId") Integer empresaId);
}