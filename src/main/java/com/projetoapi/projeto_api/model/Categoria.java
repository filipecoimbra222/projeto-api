package com.projetoapi.projeto_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Classe que representa uma Categoria no sistema.
 */
@Data
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long id;

    @NotNull(message = "O nome da categoria não pode ser nulo")
    @Column(name = "nome")
    private String nome;
}
