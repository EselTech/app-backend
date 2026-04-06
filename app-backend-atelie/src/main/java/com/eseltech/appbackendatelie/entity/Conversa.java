package com.eseltech.appbackendatelie.entity;

import java.time.LocalDateTime;

public class Conversa {

    private Integer id;
    private Empresa empresa;
    private String mensagem;
    private Boolean emissor;
    private LocalDateTime dtHoraConversa;

    public Conversa() {
    }

    public Conversa(Integer id, Empresa empresa, String mensagem, Boolean emissor, LocalDateTime dtHoraConversa) {
        this.id = id;
        this.empresa = empresa;
        this.mensagem = mensagem;
        this.emissor = emissor;
        this.dtHoraConversa = dtHoraConversa;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getEmissor() {
        return emissor;
    }

    public void setEmissor(Boolean emissor) {
        this.emissor = emissor;
    }

    public LocalDateTime getDtHoraConversa() {
        return dtHoraConversa;
    }

    public void setDtHoraConversa(LocalDateTime dtHoraConversa) {
        this.dtHoraConversa = dtHoraConversa;
    }
}
