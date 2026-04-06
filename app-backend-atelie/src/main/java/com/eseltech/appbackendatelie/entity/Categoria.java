package com.eseltech.appbackendatelie.entity;

public class Categoria {

    private Integer id;
    private String metragem;

    public Categoria() {
    }

    public Categoria(Integer id, String metragem) {
        this.id = id;
        this.metragem = metragem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetragem() {
        return metragem;
    }

    public void setMetragem(String metragem) {
        this.metragem = metragem;
    }
}
