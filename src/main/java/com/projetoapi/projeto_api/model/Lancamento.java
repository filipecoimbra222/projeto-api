package com.projetoapi.projeto_api.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table (name = "lancamento")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;
    private String descricao;

    @NotNull
    @Column (name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column (name = "data_pagamento")
    private LocalDate dataPagamento;

    @NotNull
    private BigDecimal valor;

    private String observacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_categoria")
    private Categoria categoria;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_pessoa")
    private Pessoa pessoa;

}
