package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
@Schema(description = "DTO para representar uma conversa cadastrada no sistema")
public record ConversaDTO(
        @Schema(
                description = "Identificador único da conversa",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(
                description = "Identificador da empresa associada à conversa",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer empresaId,

        @NotNull(message = "A mensagem é obrigatória")
        @NotBlank(message = "A mensagem não pode estar em branco")
        @Size(max = 500, message = "A mensagem deve ter no máximo 500 caracteres")
        @Schema(
                description = "Conteúdo da mensagem",
                example = "Olá, gostaria de saber mais sobre os produtos disponíveis.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 500
        )
        String mensagem,

        @NotNull(message = "O emissor é obrigatório")
        @Schema(
                description = "Indica quem enviou a mensagem (true = beneficiária, false = sistema/empresa)",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Boolean emissor,

        @NotNull(message = "A data/hora da conversa é obrigatória")
        @Schema(
                description = "Data e hora em que a mensagem foi enviada",
                example = "2026-04-06T10:33:00",
                requiredMode = Schema.RequiredMode.REQUIRED,
                type = "string",
                format = "date-time"
        )
        LocalDateTime dtHoraConversa
) {
}

