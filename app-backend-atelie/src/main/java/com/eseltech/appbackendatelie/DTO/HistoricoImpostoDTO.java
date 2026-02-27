package com.eseltech.appbackendatelie.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HistoricoImpostoDTO {
    private Integer id;
    private String nomeImposto;
    private Integer impostoId;
    private BigDecimal valor;
    private LocalDate dataRegistro;

    public HistoricoImpostoDTO() {
    }

    public HistoricoImpostoDTO(Integer id, Integer impostoId, BigDecimal valor, LocalDate dataRegistro) {
        this.id = id;
        this.impostoId = impostoId;
        this.valor = valor;
        this.dataRegistro = dataRegistro;
    }

    public HistoricoImpostoDTO(Integer impostoId, String nomeImposto, BigDecimal valor, LocalDate dataRegistro) {
        this.impostoId = impostoId;
        this.nomeImposto = nomeImposto;
        this.valor = valor;
        this.dataRegistro = dataRegistro;
    }

    public HistoricoImpostoDTO(LocalDate dataRegistro, BigDecimal valor) {
        this.dataRegistro = dataRegistro;
        this.valor = valor;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeImposto() {
        return nomeImposto;
    }

    public void setNomeImposto(String nomeImposto) {
        this.nomeImposto = nomeImposto;
    }

    public void setImpostoId(Integer impostoId) {
        this.impostoId = impostoId;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
