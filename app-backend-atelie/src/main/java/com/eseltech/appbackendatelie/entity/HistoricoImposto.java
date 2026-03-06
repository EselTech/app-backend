package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que representa o histórico de valores de um imposto.
 * Armazena os valores obtidos da API do Banco Central ao longo do tempo.
 */
@Entity
@Table(name = "historico_imposto")
@Schema(description = "Entidade que representa o histórico de valores de um imposto")
public class HistoricoImposto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do registro de histórico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "imposto_id", nullable = false)
    @Schema(description = "Imposto relacionado a este registro de histórico")
    private Imposto imposto;

    @Column(name = "valor", nullable = false, precision = 10, scale = 4)
    @Schema(description = "Valor do imposto na data de registro", example = "13.7500")
    private BigDecimal valor;

    @Column(name = "data_registro", nullable = false)
    @Schema(description = "Data em que o valor foi registrado", example = "2024-03-06")
    private LocalDate dataRegistro;

    public HistoricoImposto(Imposto imposto, BigDecimal valor, LocalDate dataRegistro) {
        this.imposto = imposto;
        this.valor = valor;
        this.dataRegistro = dataRegistro;
    }

    public HistoricoImposto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Imposto getImposto() {
        return imposto;
    }

    public void setImposto(Imposto imposto) {
        this.imposto = imposto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

}