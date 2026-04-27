package com.eseltech.appbackendatelie.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para renovação de access token na API da Shopee")
public class ShopeeRefreshTokenRequestDTO {

    @JsonProperty("shop_id")
    @Schema(description = "ID da loja na Shopee", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long shopId;

    @JsonProperty("refresh_token")
    @Schema(description = "Token de renovação", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    @JsonProperty("partner_id")
    @Schema(description = "ID do parceiro na Shopee", example = "987654321", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long partnerId;

    public ShopeeRefreshTokenRequestDTO() {
    }

    public ShopeeRefreshTokenRequestDTO(Long shopId, String refreshToken, Long partnerId) {
        this.shopId = shopId;
        this.refreshToken = refreshToken;
        this.partnerId = partnerId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
}

