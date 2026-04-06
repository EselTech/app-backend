package com.eseltech.appbackendatelie.entity;

import com.eseltech.appbackendatelie.entity.enums.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Table(name = "material")
@Entity
@Schema(description = "Entidade que representa um material cadastrado no sistema")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do material", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "metragem", nullable = false)
    private Categoria categoria;

    @Size(max = 100)
    @NotNull
    @Column(name = "nome", nullable = false, length = 100)
    @Schema(description = "Nome do material", example = "Papel cartão vermelho", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String nome;

    @Size(max = 100)
    @NotNull
    @Column(name = "descricao", length = 100)
    @Schema(description = "Descrição do material", example = "Papel cartão da cor vermelha geralmente usado para fazer decorações", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String descricao;

    @Positive
    @Column(name = "qtdEstoque", nullable = false)
    @Schema(description = "Quantidade do material em estoque de acordo com a categoria", example = "50ml", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal qtdEstoque;

    @Positive
    @Column(name = "preco", nullable = false)
    @Schema(description = "Preço do material", example = "R$12,90", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal preco;

    public Material() {
    }

    public Material(Empresa empresa, Categoria categoria, String nome, String descricao, BigDecimal qtdEstoque, BigDecimal preco) {
        this.empresa = empresa;
        this.categoria = categoria;
        this.nome = nome;
        this.descricao = descricao;
        this.qtdEstoque = qtdEstoque;
        this.preco = preco;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(BigDecimal qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
