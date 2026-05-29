package com.eseltech.appbackendatelie.DTO;

import com.eseltech.appbackendatelie.entity.enums.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Schema(description = "DTO para representar um material cadastrado no sistema")
public record MaterialDTO(
        @Schema(
                description = "Identificador único do material",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(
                description = "Identificador da empresa associada ao material",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer empresaId,

        @NotNull(message = "A categoria é obrigatória")
        @Schema(
                description = "Categoria de medida do material",
                example = "CENTIMETRO",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"CENTIMETRO", "MILILITRO", "INTEIRO", "GRAMA"}
        )
        Categoria categoria,

        @NotNull(message = "O nome é obrigatório")
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        @Schema(
                description = "Nome do material",
                example = "Papel cartão vermelho",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String nome,

        @NotNull(message = "A descrição é obrigatória")
        @NotBlank(message = "A descrição não pode estar em branco")
        @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
        @Schema(
                description = "Descrição do material",
                example = "Papel cartão da cor vermelha geralmente usado para fazer decorações",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String descricao,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Positive(message = "A quantidade em estoque deve ser positiva")
        @Schema(
                description = "Quantidade do material em estoque",
                example = "50.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal qtdEstoque,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        @Schema(
                description = "Preço do material",
                example = "12.90",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal preco
) {
}

