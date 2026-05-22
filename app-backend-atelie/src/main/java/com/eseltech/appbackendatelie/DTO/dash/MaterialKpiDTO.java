package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Para KPIs de Material
public record MaterialKpiDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal qtd_estoque,
        String categoria,
        Long total_alertas_reposicao, // Mude de Integer para Long
        LocalDateTime ultimo_alerta // ou o tipo de data que estiver usando
) {}
