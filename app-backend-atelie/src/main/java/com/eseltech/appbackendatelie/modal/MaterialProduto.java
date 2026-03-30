package com.eseltech.appbackendatelie.modal;

public class MaterialProduto {

    private Integer id;
    private Material material;
    private Produto produto;

    public MaterialProduto() {
    }

    public MaterialProduto(Integer id, Material material, Produto produto) {
        this.id = id;
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
