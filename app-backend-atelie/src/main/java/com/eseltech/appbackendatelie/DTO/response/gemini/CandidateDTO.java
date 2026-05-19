
package com.eseltech.appbackendatelie.DTO.response.gemini;

public class CandidateDTO {
    private ContentDTO content;
    private String finishReason;
    private Integer index;

    // Getters e Setters
    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}