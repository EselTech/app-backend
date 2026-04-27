package com.eseltech.appbackendatelie.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para requisição de importação de produtos da Shopee")
public record ImportarProdutosShopeeRequestDTO(
        @NotNull(message = "O ID da loja Shopee é obrigatório")
        @Schema(description = "ID da loja na Shopee", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
        Long shopId,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(description = "ID da empresa para associar os produtos importados", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long empresaId,

        @Positive(message = "O tamanho da página deve ser positivo")
        @Schema(description = "Quantidade de produtos a importar por vez (padrão: 100)", example = "100")
        Integer pageSize
) {
}

