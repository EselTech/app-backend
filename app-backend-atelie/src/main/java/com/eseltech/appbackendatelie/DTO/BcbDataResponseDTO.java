package com.eseltech.appbackendatelie.DTO;

import com.eseltech.appbackendatelie.entity.Imposto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO de resposta da API do Banco Central do Brasil contendo dados de impostos")
public class BcbDataResponseDTO {
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(description = "Data de referência do valor do imposto", example = "06/03/2024", type = "string", format = "date")
    private LocalDate data;

    @Schema(description = "Valor do imposto em formato string", example = "13.75")
    private String valor;

    @Schema(description = "Imposto relacionado aos dados retornados")
    private Imposto imposto;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public Imposto getImposto() {
        return imposto;
    }

    public void setImposto(Imposto imposto) {
        this.imposto = imposto;
    }
}
