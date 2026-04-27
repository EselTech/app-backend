package com.eseltech.appbackendatelie.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de estoque de produto na Shopee")
public class AtualizarEstoqueRequestDTO {

    @NotNull(message = "O campo 'stock' é obrigatório")
    @Min(value = 0, message = "A quantidade em estoque deve ser maior ou igual a zero")
    @Schema(description = "Nova quantidade em estoque", example = "100", required = true)
    private Integer stock;

    public AtualizarEstoqueRequestDTO() {
    }

    public AtualizarEstoqueRequestDTO(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}

