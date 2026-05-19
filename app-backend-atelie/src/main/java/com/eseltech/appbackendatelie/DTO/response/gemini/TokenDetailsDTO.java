
package com.eseltech.appbackendatelie.DTO.response.gemini;

public class TokenDetailsDTO {
    private String modality;
    private Integer tokenCount;

    // Getters e Setters
    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }
}