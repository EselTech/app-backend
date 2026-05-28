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