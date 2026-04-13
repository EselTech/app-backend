package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Schema(description = "DTO para representar a relação entre material e produto")
public record MaterialProdutoDTO(
        Long materialId,

        @NotNull(message = "A quantidade utilizada é obrigatória")
        @Positive(message = "A quantidade deve ser positiva")
        BigDecimal quantidade
) {
}

