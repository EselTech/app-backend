package com.eseltech.appbackendatelie.DTO;

import com.eseltech.appbackendatelie.entity.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registro de novo usuário no sistema")
public record RegisterDTO(
        @Schema(
                description = "Nome completo do usuário",
                example = "João Silva",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 40
        )
        String nome,

        @Schema(
                description = "Nome de usuário para login (deve ser único)",
                example = "joao.silva",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 40
        )
        String username,

        @Schema(
                description = "Email do usuário",
                example = "joao.silva@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String email,

        @Schema(
                description = "Senha do usuário (será criptografada com BCrypt)",
                example = "senha123",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 200
        )
        String senha,

        @Schema(
                description = "Perfil de acesso do usuário",
                example = "USER",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"ADMIN", "USER"}
        )
        UserRole role
) {
}
