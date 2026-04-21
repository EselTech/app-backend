package com.eseltech.appbackendatelie.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da API da Shopee para renovação de token")
public class ShopeeRefreshTokenResponseDTO {

    @JsonProperty("access_token")
    @Schema(description = "Novo access token")
    private String accessToken;

    @JsonProperty("refresh_token")
    @Schema(description = "Novo refresh token")
    private String refreshToken;

    @JsonProperty("expire_in")
    @Schema(description = "Tempo de validade do access token em segundos", example = "14400")
    private Integer expireIn;

    @JsonProperty("error")
    @Schema(description = "Código de erro, se houver")
    private String error;

    @JsonProperty("message")
    @Schema(description = "Mensagem de erro, se houver")
    private String message;

    public ShopeeRefreshTokenResponseDTO() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Integer expireIn) {
        this.expireIn = expireIn;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }
}

