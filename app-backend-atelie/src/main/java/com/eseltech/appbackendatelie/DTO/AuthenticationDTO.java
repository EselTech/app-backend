package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para autenticação do usuário")
public record AuthenticationDTO(
        @Schema(
                description = "Nome de usuário para login",
                example = "admin",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String username,

        @Schema(
                description = "Senha do usuário",
                example = "senha123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String senha
) {
}
