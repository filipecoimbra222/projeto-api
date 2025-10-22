package com.projetoapi.projeto_api.dto;

import com.projetoapi.projeto_api.model.TipoLancamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoOutputDTO {
    private Long id;
    private String descricao;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String observacao;
    private TipoLancamento tipo;
    private Long idPessoa;
    private String nomePessoa;
    private Long idCategoria;
    private String nomeCategoria;
}
