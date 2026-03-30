package com.eseltech.appbackendatelie.modal;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoricoImposto {

    private Integer id;
    private Imposto imposto;
    private BigDecimal valorImposto;
    private LocalDate dtRegistro;

    public HistoricoImposto() {
    }

    public HistoricoImposto(Integer id, Imposto imposto, BigDecimal valorImposto, LocalDate dtRegistro) {
        this.id = id;
        this.imposto = imposto;
        this.valorImposto = valorImposto;
        this.dtRegistro = dtRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Imposto getImposto() {
        return imposto;
    }

    public void setImposto(Imposto imposto) {
        this.imposto = imposto;
    }

    public BigDecimal getValorImposto() {
        return valorImposto;
    }

    public void setValorImposto(BigDecimal valorImposto) {
        this.valorImposto = valorImposto;
    }

    public LocalDate getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(LocalDate dtRegistro) {
        this.dtRegistro = dtRegistro;
    }
}
