package com.eseltech.appbackendatelie.entity;

public class ProdutosPedido {

    private Integer id;
    private Pedido pedido;
    private Produto produto;
    private Integer qtdProduto;

    public ProdutosPedido() {
    }

    public ProdutosPedido(Integer id, Pedido pedido, Produto produto, Integer qtdProduto) {
        this.id = id;
        this.pedido = pedido;
        this.produto = produto;
        this.qtdProduto = qtdProduto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQtdProduto() {
        return qtdProduto;
    }

    public void setQtdProduto(Integer qtdProduto) {
        this.qtdProduto = qtdProduto;
    }
}
