package com.eseltech.appbackendatelie.entity;

import java.time.LocalDateTime;

public class Notificacao {

    private Integer id;
    private Empresa empresa;
    private String topico;
    private String mensagem;
    private LocalDateTime dtEnvio;

    public Notificacao() {
    }

    public Notificacao(Integer id, Empresa empresa, String topico, String mensagem, LocalDateTime dtEnvio) {
        this.id = id;
        this.empresa = empresa;
        this.topico = topico;
        this.mensagem = mensagem;
        this.dtEnvio = dtEnvio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getTopico() {
        return topico;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDtEnvio() {
        return dtEnvio;
    }

    public void setDtEnvio(LocalDateTime dtEnvio) {
        this.dtEnvio = dtEnvio;
    }
}