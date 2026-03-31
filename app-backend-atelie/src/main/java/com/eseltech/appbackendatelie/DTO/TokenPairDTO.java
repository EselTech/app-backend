package com.eseltech.appbackendatelie.DTO;

/**
 * DTO que contém o par de tokens (Access e Refresh) gerados após autenticação.
 * Usado internamente para transferir os tokens entre service e controller.
 */
public record TokenPairDTO(
        String accessToken,
        String refreshToken
) {
}
