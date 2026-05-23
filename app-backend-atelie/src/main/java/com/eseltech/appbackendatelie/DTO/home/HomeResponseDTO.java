package com.eseltech.appbackendatelie.DTO.home;

import java.util.List;

public record HomeResponseDTO(
        DespesaKPIDTO despesaKPIDTO,
        LucroKPIDTO lucroKPIDTO,
        ReceberKPIDTO receberKPIDTO,
        ReceitaKPIDTO receitaKPIDTO,
        List<PedidoPorStatusDTO> pedidosPorStatus,
        List<ReceitaAnualPorMesDTO> receitaAnual
) {}