package com.eseltech.appbackendatelie.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Table(name = "materialProduto")
@Entity
@Schema(description = "Entidade que representa a Ficha Técnica: quantidade de um material específico usado para confeccionar um produto")
public class MaterialProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkMaterial", nullable = false)
    private Material material;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkProduto", nullable = false)
    private Produto produto;

    @Column(name = "quantidade", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Quantidade deste material necessária para produzir uma unidade do produto")
    private BigDecimal quantidade;

    public MaterialProduto() {}

    public MaterialProduto(Material material, Produto produto, BigDecimal quantidade) {
        this.material = material;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
}