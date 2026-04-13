package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar a relação entre produtos e pedido")
public record ProdutosPedidoDTO(
        @Schema(
                description = "Identificador único do produtoPedido",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID do pedido é obrigatório")
        @Schema(
                description = "Identificador do pedido",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer pedidoId,

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

