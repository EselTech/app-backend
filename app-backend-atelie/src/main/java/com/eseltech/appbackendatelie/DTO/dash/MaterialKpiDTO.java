package com.eseltech.appbackendatelie.DTO.dash;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Para KPIs de Material
public record MaterialKpiDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal qtdEstoque,
        String categoria,
        Integer totalAlertasReposicao,
        LocalDateTime ultimoAlerta
) {}
