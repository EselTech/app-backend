package com.eseltech.appbackendatelie.entity;

import java.math.BigDecimal;
import java.util.List;

public class Produto {

    private Integer id;
    private Empresa empresa;
    private String nome;
    private String descricao;
    private BigDecimal custo;
    private BigDecimal preco;
    private List<MaterialProduto> listaMateriais;

    public Produto() {
    }

    public Produto(Integer id, Empresa empresa, String nome, String descricao, BigDecimal custo, BigDecimal preco, List<MaterialProduto> listaMateriais) {
        this.id = id;
        this.empresa = empresa;
        this.nome = nome;
        this.descricao = descricao;
        this.custo = custo;
        this.preco = preco;
        this.listaMateriais = listaMateriais;
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
