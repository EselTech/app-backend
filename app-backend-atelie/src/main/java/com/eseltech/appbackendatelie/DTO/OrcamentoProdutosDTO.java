package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar a relação entre produtos e orcamento")
public record OrcamentoProdutosDTO(
        @Schema(
                description = "Identificador único do orcamentoProdutos",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID do orçamento é obrigatório")
        @Schema(
                description = "Identificador do pedido",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer orcamentoId,

        @NotNull(message = "O ID do produto é obrigatório")
        @Schema(
                description = "Identificador do produto",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer produtoId,

        @NotNull(message = "A quantidade do produto é obrigatória")
        @Positive(message = "A quantidade deve ser positiva")
        @Schema(
                description = "Quantidade do produto no pedido",
                example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer qtdProduto
) {
}
