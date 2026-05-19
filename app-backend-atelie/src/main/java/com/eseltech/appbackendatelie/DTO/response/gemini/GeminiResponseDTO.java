
package com.eseltech.appbackendatelie.DTO.response.gemini;

import java.util.List;

public class GeminiResponseDTO {
    private List<CandidateDTO> candidates;
    private UsageMetadataDTO usageMetadata;
    private String modelVersion;
    private String responseId;

    public List<CandidateDTO> getCandidates() {
        return candidates;
    }

    public String getResposta() {
        return candidates.getFirst().getContent().getParts().getFirst().getText();
    }

    public void setCandidates(List<CandidateDTO> candidates) {
        this.candidates = candidates;
    }

    public UsageMetadataDTO getUsageMetadata() {
        return usageMetadata;
    }

    public void setUsageMetadata(UsageMetadataDTO usageMetadata) {
        this.usageMetadata = usageMetadata;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }
}