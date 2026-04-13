package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "Dados para autenticação do usuário")
public record AuthenticationDTO(

        @NotNull(message = "O username é obrigatório")
        @NotBlank(message = "O username não pode estar em branco")
        @Size(max = 40, message = "O username deve ter no máximo 40 caracteres")
        @Schema(
                description = "Nome de usuário para login",
                example = "admin",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 40
        )
        String username,

        @NotNull(message = "A senha é obrigatória")
        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 8, max = 200, message = "A senha deve conter entre 8 e 200 caracteres")
        @Schema(
                description = "Senha do usuário",
                example = "senha123",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 8,
                maxLength = 200
        )
        String senha
) {
}
