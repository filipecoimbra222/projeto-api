package com.projetoapi.projeto_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "logradouro", column = @Column(name = "logradouro")),
        @AttributeOverride(name = "numero", column = @Column(name = "numero")),
        @AttributeOverride(name = "complemento", column = @Column(name = "complemento")),
        @AttributeOverride(name = "bairro", column = @Column(name = "bairro")),
        @AttributeOverride(name = "cep", column = @Column(name = "cep")),
        @AttributeOverride(name = "cidade", column = @Column(name = "cidade")),
        @AttributeOverride(name = "estado", column = @Column(name = "estado"))
    })
    private Endereco endereco;

    @JsonIgnore
    @Transient
    public boolean isInativo() {
        return !this.ativo;
    }
}
