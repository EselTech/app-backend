package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar a relação entre material e produto")
public record MaterialProdutoDTO(
        @Schema(
                description = "Identificador único do materialProduto",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID do material é obrigatório")
        @Schema(
                description = "Identificador do material",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer materialId,

        @NotNull(message = "O ID do produto é obrigatório")
        @Schema(
                description = "Identificador do produto",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer produtoId
) {
}

