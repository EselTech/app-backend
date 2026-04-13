package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

@Table(name = "produtosPedido")
@Entity
@Schema(description = "Entidade que representa uma lista de produtos dentro de um pedido cadastrado no sistema")
public class ProdutosPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do produtoPedido", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Positive
    @Column(name = "qtdProduto", nullable = false)
    @Schema(description = "Quantidade do produto na lista", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
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
