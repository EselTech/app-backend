package com.eseltech.appbackendatelie.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para transferência de dados do histórico de impostos.
 * Utilizado para retornar informações formatadas sobre o histórico de valores dos impostos.
 */
@Schema(description = "DTO contendo informações do histórico de um imposto")
public class HistoricoImpostoDTO {
    @Schema(description = "Identificador único do registro de histórico", example = "1")
    private Integer id;

    @Schema(description = "Nome do imposto", example = "SELIC")
    private String nomeImposto;

    @Schema(description = "Identificador do imposto relacionado", example = "1")
    private Integer impostoId;

    @Schema(description = "Valor do imposto", example = "13.7500")
    private BigDecimal valor;

    @Schema(description = "Data do registro do valor", example = "2024-03-06", type = "string", format = "date")
    private LocalDate dataRegistro;

    public HistoricoImpostoDTO() {
    }

    public HistoricoImpostoDTO(Integer id, Integer impostoId, BigDecimal valor, LocalDate dataRegistro) {
        this.id = id;
        this.impostoId = impostoId;
        this.valor = valor;
        this.dataRegistro = dataRegistro;
    }

    public HistoricoImpostoDTO(Integer impostoId, String nomeImposto, BigDecimal valor, LocalDate dataRegistro) {
        this.impostoId = impostoId;
        this.nomeImposto = nomeImposto;
        this.valor = valor;
        this.dataRegistro = dataRegistro;
    }

    public HistoricoImpostoDTO(LocalDate dataRegistro, BigDecimal valor) {
        this.dataRegistro = dataRegistro;
        this.valor = valor;
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

    public void setImpostoId(Integer impostoId) {
        this.impostoId = impostoId;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
