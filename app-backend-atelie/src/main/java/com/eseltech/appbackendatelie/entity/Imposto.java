package com.eseltech.appbackendatelie.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "imposto")
public class Imposto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nome_imposto", nullable = false, length = 100)
    private String nomeImposto;

    @Column(name = "codigo_sgs", nullable = false)
    private Integer codigoSgs;

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