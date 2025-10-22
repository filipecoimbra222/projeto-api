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
    private long id;
    private String descricao;

    @NotNull(message = "Data de vencimento é obrigatória")
    @Column (name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column (name = "data_pagamento")
    private LocalDate dataPagamento;

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;

    private String observacao;

    @NotNull(message = "Tipo de lançamento é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @NotNull(message = "Categoria é obrigatória")
    @ManyToOne
    @JoinColumn(name = "codigo_categoria")
    private Categoria categoria;

    @NotNull(message = "Pessoa é obrigatória")
    @ManyToOne
    @JoinColumn(name = "codigo_pessoa")
    private Pessoa pessoa;

}
