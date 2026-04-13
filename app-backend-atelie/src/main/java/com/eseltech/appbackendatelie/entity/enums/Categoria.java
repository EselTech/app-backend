package com.eseltech.appbackendatelie.entity.enums;

public enum Categoria {
    CENTIMETRO("centímetro"),
    MILILITRO("mililitro"),
    INTEIRO("inteiro"),
    GRAMA("grama");

    private final String metragem;

    Categoria(String metragem) {
        this.metragem = metragem;
    }

    public String getMetragem() {
        return metragem;
    }
}
