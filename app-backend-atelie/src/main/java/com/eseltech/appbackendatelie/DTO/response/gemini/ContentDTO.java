
package com.eseltech.appbackendatelie.DTO.response.gemini;

import java.util.List;

public class ContentDTO {
    private List<PartDTO> parts;
    private String role;

    // Getters e Setters
    public List<PartDTO> getParts() {
        return parts;
    }

    public void setParts(List<PartDTO> parts) {
        this.parts = parts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}