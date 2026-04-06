package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Table(name = "materialProduto")
@Entity
@Schema(description = "Entidade que representa uma lista de materiais para confecção de um produto cadastrado no sistema")
public class MaterialProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do materialProduto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    private Material material;

    private Produto produto;

    public MaterialProduto() {
    }

    public MaterialProduto(Material material, Produto produto) {
        this.material = material;
        this.produto = produto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
