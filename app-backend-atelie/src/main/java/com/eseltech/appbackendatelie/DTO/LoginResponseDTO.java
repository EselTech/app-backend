package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta contendo o token JWT após autenticação bem-sucedida.
 */
@Schema(description = "Resposta de autenticação contendo o token JWT")
public record LoginResponseDTO(
        @Schema(
                description = "Token JWT para autenticação nas requisições subsequentes. Válido por 2 horas.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcHAtYmFja2VuZC1hdGVsaWUiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTcxMjM0NTY3OH0.abc123"
        )
        String token
) {
}
