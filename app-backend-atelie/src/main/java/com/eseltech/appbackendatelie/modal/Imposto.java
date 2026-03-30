package com.eseltech.appbackendatelie.modal;

public class Imposto {

    private Integer id;
    private String nomeImposto;
    private Integer codigoSgs;

    public Imposto() {
    }

    public Imposto(Integer id, String nomeImposto, Integer codigoSgs) {
        this.id = id;
        this.nomeImposto = nomeImposto;
        this.codigoSgs = codigoSgs;
    }

    public Integer getId() {
        return id;
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

    public Integer getCodigoSgs() {
        return codigoSgs;
    }

    public void setCodigoSgs(Integer codigoSgs) {
        this.codigoSgs = codigoSgs;
    }
}
