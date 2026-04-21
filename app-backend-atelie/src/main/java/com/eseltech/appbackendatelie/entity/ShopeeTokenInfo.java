package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Table(name = "shopee_token_info")
@Entity
@Schema(description = "Entidade que armazena as credenciais de acesso à API da Shopee")
public class ShopeeTokenInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do registro de token", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Column(name = "shop_id", nullable = false, unique = true)
    @Schema(description = "ID da loja na Shopee", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long shopId;

    @NotNull
    @Column(name = "partner_id", nullable = false)
    @Schema(description = "ID do parceiro na Shopee", example = "987654321", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long partnerId;

    @NotNull
    @Column(name = "access_token", nullable = false, length = 500)
    @Schema(description = "Token de acesso à API (válido por 4 horas)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accessToken;

    @NotNull
    @Column(name = "refresh_token", nullable = false, length = 500)
    @Schema(description = "Token de renovação (válido por 30 dias)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    @Schema(description = "Data e hora de expiração do access token", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Data e hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Data e hora da última atualização", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    public ShopeeTokenInfo() {
    }

    public ShopeeTokenInfo(Long shopId, Long partnerId, String accessToken, String refreshToken, LocalDateTime expiresAt) {
        this.shopId = shopId;
        this.partnerId = partnerId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
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

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

