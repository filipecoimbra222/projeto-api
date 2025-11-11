package com.projetoapi.projeto_api.dto;

import com.projetoapi.projeto_api.model.TipoLancamento;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoInputDTO {
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "A data é obrigatória")
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

    @Positive
    private BigDecimal valor;

    private String observacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @NotNull
    private Long idPessoa;

    @NotNull
    private Long idCategoria;
}
