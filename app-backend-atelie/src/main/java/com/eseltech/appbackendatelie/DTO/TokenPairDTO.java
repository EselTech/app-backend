package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO contendo o par de tokens JWT (access e refresh)")
public record TokenPairDTO(
        @NotNull(message = "O access token é obrigatório")
        @NotBlank(message = "O access token não pode estar em branco")
        @Schema(
                description = "Token de acesso JWT para autenticação nas requisições. Válido por 2 horas.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcHAtYmFja2VuZC1hdGVsaWUiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTcxMjM0NTY3OH0.abc123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String accessToken,

        @NotNull(message = "O refresh token é obrigatório")
        @NotBlank(message = "O refresh token não pode estar em branco")
        @Schema(
                description = "Token de atualização JWT para renovar o access token. Válido por 7 dias.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcHAtYmFja2VuZC1hdGVsaWUiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTcxMjk1MDQ3OH0.xyz789",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String refreshToken
) {
}
