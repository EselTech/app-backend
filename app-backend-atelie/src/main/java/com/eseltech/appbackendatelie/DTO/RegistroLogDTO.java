package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
@Schema(description = "DTO para representar um registro de log do sistema")
public record RegistroLogDTO(
        @Schema(
                description = "Identificador único do registro de log",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "A categoria é obrigatória")
        @NotBlank(message = "A categoria não pode estar em branco")
        @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres")
        @Schema(
                description = "Categoria do registro de log",
                example = "INFO",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 50
        )
        String categoria,

        @NotNull(message = "A descrição é obrigatória")
        @NotBlank(message = "A descrição não pode estar em branco")
        @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
        @Schema(
                description = "Descrição do evento registrado",
                example = "Usuário realizou login no sistema",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 500
        )
        String descricao,

        @NotNull(message = "A data/hora é obrigatória")
        @Schema(
                description = "Data e hora do registro do log",
                example = "2026-04-06T10:15:30",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "date-time"
        )
        LocalDateTime dtHora
) {
}

