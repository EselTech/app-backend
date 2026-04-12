package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar um imposto cadastrado no sistema")
public record ImpostoDTO(
        @Schema(
                description = "Identificador único do imposto",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O nome do imposto é obrigatório")
        @NotBlank(message = "O nome do imposto não pode estar em branco")
        @Size(max = 100, message = "O nome do imposto deve ter no máximo 100 caracteres")
        @Schema(
                description = "Nome do imposto",
                example = "SELIC",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String nomeImposto,

        @NotNull(message = "O código SGS é obrigatório")
        @Positive(message = "O código SGS deve ser positivo")
        @Schema(
                description = "Código SGS (Sistema Gerenciador de Séries) do Banco Central para consulta do valor do imposto",
                example = "432",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer codigoSgs
) {
}

