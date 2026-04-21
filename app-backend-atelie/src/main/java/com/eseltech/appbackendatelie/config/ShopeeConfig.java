package com.eseltech.appbackendatelie.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shopee.api")
public class ShopeeConfig {

    private String baseUrl = "https://partner.shopeemobile.com";

    private Long partnerId;

    private String partnerKey;

    private String refreshTokenPath = "/api/v2/auth/access_token/get";

    private Integer refreshMarginMinutes = 15;

    public ShopeeConfig() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public void setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey;
    }

    public String getRefreshTokenPath() {
        return refreshTokenPath;
    }

    public void setRefreshTokenPath(String refreshTokenPath) {
        this.refreshTokenPath = refreshTokenPath;
    }

    public Integer getRefreshMarginMinutes() {
        return refreshMarginMinutes;
    }

    public void setRefreshMarginMinutes(Integer refreshMarginMinutes) {
        this.refreshMarginMinutes = refreshMarginMinutes;
    }

    public String getRefreshTokenUrl() {
        return baseUrl + refreshTokenPath;
    }
}

