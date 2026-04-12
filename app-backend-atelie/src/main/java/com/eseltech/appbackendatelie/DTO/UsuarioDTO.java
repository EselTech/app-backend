package com.eseltech.appbackendatelie.DTO;

import com.eseltech.appbackendatelie.entity.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar um usuário do sistema")
public record UsuarioDTO(
        @Schema(
                description = "Identificador único do usuário",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O nome é obrigatório")
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(max = 40, message = "O nome deve ter no máximo 40 caracteres")
        @Schema(
                description = "Nome do usuário",
                example = "João Silva",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 40
        )
        String nome,

        @NotNull(message = "O username é obrigatório")
        @NotBlank(message = "O username não pode estar em branco")
        @Size(max = 40, message = "O username deve ter no máximo 40 caracteres")
        @Schema(
                description = "Nome de usuário para login",
                example = "joao.silva",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 40
        )
        String username,

        @NotNull(message = "O email é obrigatório")
        @NotBlank(message = "O email não pode estar em branco")
        @Email(message = "O email deve ser válido")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
        @Schema(
                description = "Email do usuário",
                example = "joao.silva@email.com",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        String email,

        @NotNull(message = "A role é obrigatória")
        @Schema(
                description = "Perfil de acesso do usuário",
                example = "USER",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"ADMIN", "USER"}
        )
        UserRole role,

        @Schema(
                description = "Identificador da empresa associada ao usuário",
                example = "1"
        )
        Integer empresaId
) {
}

