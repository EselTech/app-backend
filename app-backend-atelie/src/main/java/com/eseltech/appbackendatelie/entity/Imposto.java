package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * Entidade que representa um imposto no sistema.
 * Cada imposto possui um código SGS do Banco Central para consulta de valores.
 */
@Entity
@Table(name = "imposto")
@Schema(description = "Entidade que representa um imposto cadastrado no sistema")
public class Imposto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do imposto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(name = "nome_imposto", nullable = false, length = 100)
    @Schema(description = "Nome do imposto", example = "SELIC", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeImposto;

    @Column(name = "codigo_sgs", nullable = false)
    @Schema(description = "Código SGS (Sistema Gerenciador de Séries) do Banco Central para consulta do valor do imposto", example = "432", requiredMode = Schema.RequiredMode.REQUIRED)
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