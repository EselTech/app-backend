package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de resposta para operações de autenticação.
 */
@Schema(description = "Resposta genérica para operações de autenticação")
public record AuthResponseDTO(
        @Schema(description = "Mensagem de resposta", example = "Login realizado com sucesso")
        String message
) {
}
