package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Validated
@Schema(description = "DTO para representar um produto cadastrado no sistema")
public record ProdutoDTO(
        @Schema(
                description = "Identificador único do produto",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Long id,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(
                description = "Identificador da empresa associada ao produto",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long empresaId,

        @NotNull(message = "O nome é obrigatório")
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        @Schema(
                description = "Nome do produto",
                example = "Sacola temática",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String nome,

        @NotNull(message = "A descrição é obrigatória")
        @NotBlank(message = "A descrição não pode estar em branco")
        @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
        @Schema(
                description = "Descrição do produto",
                example = "Agendas para anotações escolares",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String descricao,

        @NotNull(message = "O custo é obrigatório")
        @Positive(message = "O custo deve ser um valor positivo")
        @Schema(
                description = "Custo do produto",
                example = "12.90",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal custo,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser um valor positivo")
        @Schema(
                description = "Preço de venda do produto",
                example = "25.90",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal preco,

        @Schema(description = "Custo da mão de obra para produção do produto")
        BigDecimal custoMaoDeObra,

        @Schema(description = "Margem de lucro do produto")
        BigDecimal margemLucroPercentual,

        @Schema(description = "Lista de materiais e quantidades que compõem o produto")
        List<MaterialProdutoDTO> materiais

) {
}

