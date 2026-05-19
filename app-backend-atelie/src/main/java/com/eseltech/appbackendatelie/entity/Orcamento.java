package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Table(name = "orcamento")
@Entity
@Schema(description = "Entidade que representa um orcamento cadastrado no sistema")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do orçamento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Size(max = 100)
    @NotNull
    @Column(name = "titulo", nullable = false, length = 100)
    @Schema(description = "Título do orçamento", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String titulo;

    @Size(max = 45)
    @NotNull
    @Column(name = "cliente", nullable = false, length = 45)
    @Schema(description = "Nome do(a) cliente", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 45)
    private String cliente;

    @NotNull
    @Column(name = "valor", nullable = false)
    @Schema(description = "Mensagem da conversa", example = "44,99")
    private BigDecimal valor;

    public Orcamento() {
    }

    public Orcamento(Integer id, Empresa empresa, String titulo, String cliente, BigDecimal valor) {
        this.id = id;
        this.empresa = empresa;
        this.titulo = titulo;
        this.cliente = cliente;
        this.valor = valor;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
