package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Validated
@Schema(description = "DTO para representar um pedido cadastrado no sistema")
public record PedidoDTO(
        @Schema(
                description = "Identificador único do pedido",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(
                description = "Identificador da empresa associada ao pedido",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer empresaId,

        @NotNull(message = "O nome é obrigatório")
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        @Schema(
                description = "Nome do pedido",
                example = "Pedido de Agendas - Sirlene",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String nome,

        @NotNull(message = "A descrição é obrigatória")
        @NotBlank(message = "A descrição não pode estar em branco")
        @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
        @Schema(
                description = "Descrição do pedido",
                example = "Pedido de 50x agendas escolares para a escola Frei Caneca realizado por Sirlene",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        @Schema(
                description = "Valor total do pedido",
                example = "92.90",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal valor,

        @NotNull(message = "O status é obrigatório")
        @NotBlank(message = "O status não pode estar em branco")
        @Size(max = 50, message = "O status deve ter no máximo 50 caracteres")
        @Schema(
                description = "Situação atual do pedido",
                example = "Em andamento",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 50
        )
        String status,

        @NotNull(message = "O prazo é obrigatório")
        @Schema(
                description = "Data de prazo para entrega do pedido",
                example = "2026-04-15",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "date"
        )
        LocalDate prazo,

        @Schema(
                description = "Lista de produtos do pedido"
        )
        List<ProdutosPedidoDTO> listaProdutos
) {
}

