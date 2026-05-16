package com.eseltech.appbackendatelie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

@Table(name = "orcamentoProdutos")
@Entity
@Schema(description = "Entidade que representa uma lista de produtos dentro de um orcamento cadastrado no sistema")
public class OrcamentoProdutos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do orcamentoProdutos", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "orcamento_id")
    private Orcamento orcamento;

    @Positive
    @Column(name = "qtdProduto", nullable = false)
    @Schema(description = "Quantidade do produto na lista", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer qtdProduto;

    public OrcamentoProdutos() {
    }

    public OrcamentoProdutos(Integer id, Produto produto, Orcamento orcamento, Integer qtdProduto) {
        this.id = id;
        this.produto = produto;
        this.orcamento = orcamento;
        this.qtdProduto = qtdProduto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public Integer getQtdProduto() {
        return qtdProduto;
    }

    public void setQtdProduto(Integer qtdProduto) {
        this.qtdProduto = qtdProduto;
    }
}
