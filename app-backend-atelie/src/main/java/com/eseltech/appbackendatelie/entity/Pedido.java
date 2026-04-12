package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Table(name = "pedido")
@Entity
@Schema(description = "Entidade que representa um pedido cadastrado no sistema")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @ManyToOne
    private Empresa empresa;

    @Size(max = 100)
    @NotNull
    @Column(name = "nome", nullable = false, length = 100)
    @Schema(description = "Nome do pedido", example = "Pedido de Agendas - Sirlene", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String nome;

    @Size(max = 100)
    @NotNull
    @Column(name = "descricao", nullable = false, length = 100)
    @Schema(description = "Descrição do pedido", example = "Pedido de 50x agendas escolares para a escola Frei Caneca realizado por Sirlene", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String descricao;

    @Positive
    @Column(name = "valor", nullable = false)
    @Schema(description = "Valor do pedido", example = "R$92,90", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal valor;

    @Size(max = 50)
    @NotNull
    @Column(name = "status", nullable = false, length = 50)
    @Schema(description = "Situação do pedido", example = "Atrasado", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
    private String status;

    @NotNull
    @Column(name = "prazo", nullable = false)
    @Schema(description = "Prazo do pedido", example = "06/04/2024", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50)
    private LocalDate prazo;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ProdutosPedido> listaProdutos;

    public Pedido() {
    }

    public Pedido(Empresa empresa, String nome, String descricao, BigDecimal valor, String status, LocalDate prazo, List<ProdutosPedido> listaProdutos) {
        this.empresa = empresa;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.status = status;
        this.prazo = prazo;
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

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public List<ProdutosPedido> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(List<ProdutosPedido> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }
}
