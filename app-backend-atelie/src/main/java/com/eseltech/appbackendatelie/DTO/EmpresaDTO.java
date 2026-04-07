package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar uma empresa cadastrada no sistema")
public record EmpresaDTO(
        @Schema(
                description = "Identificador único da empresa",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "A razão social é obrigatória")
        @NotBlank(message = "A razão social não pode estar em branco")
        @Size(max = 200, message = "A razão social deve ter no máximo 200 caracteres")
        @Schema(
                description = "Razão social da empresa",
                example = "EselTech Ltda",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 200
        )
        String razaoSocial,

        @NotNull(message = "O CNPJ é obrigatório")
        @NotBlank(message = "O CNPJ não pode estar em branco")
        @Size(min = 14, max = 14, message = "O CNPJ deve ter exatamente 14 caracteres")
        @Schema(
                description = "CNPJ da empresa (somente números)",
                example = "08064422000132",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 14,
                maxLength = 14
        )
        String cnpj
) {
}

