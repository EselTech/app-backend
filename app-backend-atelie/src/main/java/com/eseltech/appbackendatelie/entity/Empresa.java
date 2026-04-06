package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Table(name = "empresa")
@Entity
@Schema(description = "Entidade que representa uma empresa cadastrada no sistema")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único da empresa", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "razaoSocial", nullable = false, length = 200)
    @Schema(description = "Nome da empresa", example = "EselTech", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String razaoSocial;

    @Size(max = 14, min = 14)
    @NotNull
    @Column(name = "cnpj", nullable = false, length = 14)
    @Schema(description = "CNPJ da empresa", example = "08.064.422/0001-32", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 14)
    private String cnpj;

    public Empresa() {
    }

    public Empresa(Integer id, String razaoSocial, String cnpj) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
