package com.eseltech.appbackendatelie.DTO.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Valid
public record AtualizarSenhaRequest(
      @NotNull(message = "A senha antiga é obrigatória")
      @NotBlank(message = "A senha antiga não pode estar em branco")
      String senhaAntiga,

      @NotNull(message = "A nova senha é obrigatória")
      @NotBlank(message = " A nova senha não pode estar em branco")
      String novaSenha,

      @NotNull(message = "O ID do usuário é obrigatório")
      Integer userID
) {}
