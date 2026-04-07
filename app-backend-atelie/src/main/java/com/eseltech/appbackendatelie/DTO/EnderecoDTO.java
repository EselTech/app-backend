package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
@Schema(description = "DTO para representar um endereço cadastrado no sistema")
public record EnderecoDTO(
        @Schema(
                description = "Identificador único do endereço",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer id,

        @NotNull(message = "O ID da empresa é obrigatório")
        @Schema(
                description = "Identificador da empresa associada ao endereço",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer empresaId,

        @NotNull(message = "O logradouro é obrigatório")
        @NotBlank(message = "O logradouro não pode estar em branco")
        @Size(max = 200, message = "O logradouro deve ter no máximo 200 caracteres")
        @Schema(
                description = "Logradouro do endereço",
                example = "Rua Governador Carvalho Pinto",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 200
        )
        String logradouro,

        @NotNull(message = "O número é obrigatório")
        @NotBlank(message = "O número não pode estar em branco")
        @Size(max = 15, message = "O número deve ter no máximo 15 caracteres")
        @Schema(
                description = "Número do endereço",
                example = "402",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 15
        )
        String numero,

        @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres")
        @Schema(
                description = "Complemento do endereço",
                example = "Apartamento 353",
                maxLength = 100
        )
        String complemento,

        @NotNull(message = "O bairro é obrigatório")
        @NotBlank(message = "O bairro não pode estar em branco")
        @Size(max = 150, message = "O bairro deve ter no máximo 150 caracteres")
        @Schema(
                description = "Bairro do endereço",
                example = "Penha de França",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 150
        )
        String bairro,

        @NotNull(message = "A cidade é obrigatória")
        @NotBlank(message = "A cidade não pode estar em branco")
        @Size(max = 150, message = "A cidade deve ter no máximo 150 caracteres")
        @Schema(
                description = "Cidade do endereço",
                example = "São Paulo",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 150
        )
        String cidade,

        @NotNull(message = "O estado é obrigatório")
        @NotBlank(message = "O estado não pode estar em branco")
        @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres (UF)")
        @Schema(
                description = "Unidade Federativa (UF) do endereço",
                example = "SP",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 2,
                maxLength = 2
        )
        String estado,

        @NotNull(message = "O CEP é obrigatório")
        @NotBlank(message = "O CEP não pode estar em branco")
        @Size(max = 9, message = "O CEP deve ter no máximo 9 caracteres")
        @Schema(
                description = "CEP do endereço",
                example = "65062-200",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 9
        )
        String cep
) {
}

