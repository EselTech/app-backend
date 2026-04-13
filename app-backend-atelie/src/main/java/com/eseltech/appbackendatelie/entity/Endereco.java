package com.eseltech.appbackendatelie.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Table(name = "endereco")
@Entity
@Schema(description = "Entidade que representa um endereço cadastrado no sistema")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identificador único do endereço", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @OneToOne
    private Empresa empresa;

    @Size(max = 200)
    @NotNull
    @Column(name = "logradouro", nullable = false, length = 200)
    @Schema(description = "Informação do logradouro", example = "Rua Governador Carvalho Pinto", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String logradouro;

    @Size(max = 15)
    @NotNull
    @Column(name = "numero", nullable = false, length = 15)
    @Schema(description = "15", example = "402", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 15)
    private String numero;

    @Size(max = 100)
    @Column(name = "complemento", length = 100)
    @Schema(description = "Complemento do endereço, informações extras", example = "apartamento 353", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String complemento;

    @Size(max = 150)
    @NotNull
    @Column(name = "bairro", nullable = false, length = 150)
    @Schema(description = "Identificador único do bairro", example = "Penha de França", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
    private String bairro;

    @Size(max = 150)
    @NotNull
    @Column(name = "cidade", nullable = false, length = 150)
    @Schema(description = "Identificador único do cidade", example = "Rio de Janeiro", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
    private String cidade;

    @Size(max = 2)
    @NotNull
    @Column(name = "UF", nullable = false, length = 2)
    @Schema(description = "Identificador único do estado", example = "Rio de Janeiro", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 2)
    private String estado;

    @Size(max = 2)
    @NotNull
    @Column(name = "CEP", nullable = false, length = 2)
    @Schema(description = "Identificador único do cep", example = "65062-200", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 2)
    private String cep;

    public Endereco() {
    }

    public Endereco(Empresa empresa, String logradouro, String numero, String complemento, String bairro, String cidade, String estado, String cep) {
        this.empresa = empresa;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
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

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
