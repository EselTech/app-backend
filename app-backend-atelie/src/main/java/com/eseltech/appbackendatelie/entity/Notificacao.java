package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Table(name = "notificacao")
@Entity
@Schema(description = "Entidade que representa uma notificação cadastrada no sistema")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    private Empresa empresa;

    @Size(max = 45)
    @NotNull
    @Column(name = "topico", nullable = false, length = 45)
    @Schema(description = "Tópico da notificação", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 45)
    private String topico;

    @Size(max = 500)
    @NotNull
    @Column(name = "mensagem", nullable = false, length = 500)
    @Schema(description = "Mensagem da notificação", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 500)
    private String mensagem;

    @NotNull
    @Column(name = "dtEnvio", nullable = false)
    @Schema(description = "Data em que a mensagem foi enviada", example = "06/04/2026", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime dtEnvio;

    public Notificacao() {
    }

    public Notificacao(Empresa empresa, String topico, String mensagem, LocalDateTime dtEnvio) {
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