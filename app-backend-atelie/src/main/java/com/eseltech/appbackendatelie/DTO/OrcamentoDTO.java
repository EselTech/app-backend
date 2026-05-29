package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Schema(description = "DTO para representar um orçamento cadastrado no sistema")
public record OrcamentoDTO(
        @Schema(
                description = "Identificador único do orçamento",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(
                description = "Identificador da empresa associada ao orçamento",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer empresaId,

        @NotNull(message = "O título é obrigatório")
        @NotBlank(message = "O título não pode estar em branco")
        @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
        @Schema(
                description = "Título do produto",
                example = "Orçamento do projeto de sacolas para festas de casamento",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String titulo,

        @NotNull(message = "O nome do cliente é obrigatório")
        @NotBlank(message = "O nome do cliente não pode estar em branco")
        @Size(max = 45, message = "O nome do cliente deve ter no máximo 45 caracteres")
        @Schema(
                description = "O nome do cliente",
                example = "Wanda",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 45
        )
        String cliente,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser um valor positivo")
        @Schema(
                description = "Valor do orçamento do pedido",
                example = "25.90",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal valor
) {
}
