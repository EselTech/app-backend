package com.eseltech.appbackendatelie.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Pedido {

    private Integer id;
    private Empresa empresa;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private String status;
    private LocalDate data;
    private List<ProdutosPedido> listaProdutos;

    public Pedido() {
    }

    public Pedido(Integer id, Empresa empresa, String nome, String descricao, BigDecimal valor, String status, LocalDate data, List<ProdutosPedido> listaProdutos) {
        this.id = id;
        this.empresa = empresa;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.status = status;
        this.data = data;
        this.listaProdutos = listaProdutos;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ProdutosPedido> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(List<ProdutosPedido> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }
}
