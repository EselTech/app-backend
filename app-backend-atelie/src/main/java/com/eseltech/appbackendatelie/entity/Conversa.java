package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Table(name = "conversa")
@Entity
@Schema(description = "Entidade que representa uma conversa cadastrada no sistema")
public class Conversa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único da conversa", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Size(max = 500)
    @NotNull
    @Column(name = "mensagem", nullable = false, length = 500)
    @Schema(description = "Mensagem da conversa", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 500)
    private String mensagem;

    @NotNull
    @Column(name = "emissor", nullable = false)
    @Schema(description = "Quem enviou a mensagem", example = "1 = true = beneficiária", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean emissor;

    @NotNull
    @Column(name = "dtHoraConversa", nullable = false)
    @Schema(description = "Data e hora que a mensagem foi enviada", example = "06/04/2026 10:33", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime dtHoraConversa;

    public Conversa() {
    }

    public Conversa(Empresa empresa, String mensagem, Boolean emissor, LocalDateTime dtHoraConversa) {
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
