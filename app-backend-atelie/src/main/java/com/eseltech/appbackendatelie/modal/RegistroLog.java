package com.eseltech.appbackendatelie.modal;

import java.time.LocalDateTime;

public class RegistroLog {

    private Integer id;
    private String categoria;
    private String descricao;
    private LocalDateTime dtHora;

    public RegistroLog() {
    }

    public RegistroLog(Integer id, String categoria, String descricao, LocalDateTime dtHora) {
        this.id = id;
        this.categoria = categoria;
        this.descricao = descricao;
        this.dtHora = dtHora;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDtHora() {
        return dtHora;
    }

    public void setDtHora(LocalDateTime dtHora) {
        this.dtHora = dtHora;
    }
}
