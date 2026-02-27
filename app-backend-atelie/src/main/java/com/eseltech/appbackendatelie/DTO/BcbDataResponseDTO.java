package com.eseltech.appbackendatelie.DTO;

import com.eseltech.appbackendatelie.entity.Imposto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class BcbDataResponseDTO {
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;
    private String valor;
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
