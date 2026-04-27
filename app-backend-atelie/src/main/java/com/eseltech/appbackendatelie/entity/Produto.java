package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Table(name = "produto")
@Entity
@Schema(description = "Entidade que representa um produto cadastrado no sistema")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do produto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    private Empresa empresa;

    @Size(max = 100)
    @NotNull
    @Column(name = "nome", nullable = false, length = 100)
    @Schema(description = "Nome do produto", example = "Sacola temática", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String nome;

    @Size(max = 100)
    @NotNull
    @Column(name = "descricao", length = 100)
    @Schema(description = "Descrição do produto", example = "Agendas para anotações escolares", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String descricao;

    @Positive
    @Column(name = "custo", nullable = false)
    @Schema(description = "Custo do produto", example = "R$12,90", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal custo;

    @Positive
    @Column(name = "preco", nullable = false)
    @Schema(description = "Preço do produto", example = "R$12,90", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal preco;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialProduto> listaMateriais;

    public void adicionarMaterial(Material material, BigDecimal quantidade) {
        MaterialProduto materialProduto = new MaterialProduto();
        materialProduto.setProduto(this);
        materialProduto.setMaterial(material);
        materialProduto.setQuantidade(quantidade);
        this.listaMateriais.add(materialProduto);
    }

    public void removerMaterial(MaterialProduto materialProduto) {
        this.listaMateriais.remove(materialProduto);
        materialProduto.setProduto(null);
    }

    public Produto() {
    }

    public Produto(Empresa empresa, String nome, String descricao, BigDecimal custo, BigDecimal preco, List<MaterialProduto> listaMateriais) {
        this.empresa = empresa;
        this.nome = nome;
        this.descricao = descricao;
        this.custo = custo;
        this.preco = preco;
        this.listaMateriais = listaMateriais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public List<MaterialProduto> getListaMateriais() {
        return listaMateriais;
    }

    public void setListaMateriais(List<MaterialProduto> listaMateriais) {
        this.listaMateriais = listaMateriais;
    }
}
