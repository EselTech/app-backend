
package com.eseltech.appbackendatelie.DTO.response.gemini;

import java.util.List;

public class UsageMetadataDTO {
    private Integer promptTokenCount;
    private Integer candidatesTokenCount;
    private Integer totalTokenCount;
    private List<TokenDetailsDTO> promptTokensDetails;
    private Integer thoughtsTokenCount;
    private String serviceTier;

    // Getters e Setters
    public Integer getPromptTokenCount() {
        return promptTokenCount;
    }

    public void setPromptTokenCount(Integer promptTokenCount) {
        this.promptTokenCount = promptTokenCount;
    }

    public Integer getCandidatesTokenCount() {
        return candidatesTokenCount;
    }

    public void setCandidatesTokenCount(Integer candidatesTokenCount) {
        this.candidatesTokenCount = candidatesTokenCount;
    }

    public Integer getTotalTokenCount() {
        return totalTokenCount;
    }

    public void setTotalTokenCount(Integer totalTokenCount) {
        this.totalTokenCount = totalTokenCount;
    }

    public List<TokenDetailsDTO> getPromptTokensDetails() {
        return promptTokensDetails;
    }

    public void setPromptTokensDetails(List<TokenDetailsDTO> promptTokensDetails) {
        this.promptTokensDetails = promptTokensDetails;
    }

    public Integer getThoughtsTokenCount() {
        return thoughtsTokenCount;
    }

    public void setThoughtsTokenCount(Integer thoughtsTokenCount) {
        this.thoughtsTokenCount = thoughtsTokenCount;
    }

    public String getServiceTier() {
        return serviceTier;
    }

    public void setServiceTier(String serviceTier) {
        this.serviceTier = serviceTier;
    }
}