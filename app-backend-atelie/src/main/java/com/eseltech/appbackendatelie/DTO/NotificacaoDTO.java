package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
@Schema(description = "DTO para representar uma notificação cadastrada no sistema")
public record NotificacaoDTO(
        @Schema(
                description = "Identificador único da notificação",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(
                description = "Identificador da empresa associada à notificação",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer empresaId,

        @NotNull(message = "O tópico é obrigatório")
        @NotBlank(message = "O tópico não pode estar em branco")
        @Size(max = 45, message = "O tópico deve ter no máximo 45 caracteres")
        @Schema(
                description = "Tópico da notificação",
                example = "Novo Pedido",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 45
        )
        String topico,

        @NotNull(message = "A mensagem é obrigatória")
        @NotBlank(message = "A mensagem não pode estar em branco")
        @Size(max = 500, message = "A mensagem deve ter no máximo 500 caracteres")
        @Schema(
                description = "Mensagem da notificação",
                example = "Um novo pedido foi realizado para sua empresa.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 500
        )
        String mensagem,

        @NotNull(message = "A data de envio é obrigatória")
        @Schema(
                description = "Data e hora em que a notificação foi enviada",
                example = "2026-04-06T14:30:00",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "date-time"
        )
        LocalDateTime dtEnvio
) {
}

