package com.projetoapi.projeto_api.dto;

import com.projetoapi.projeto_api.model.Endereco;
import lombok.Data;

@Data
public class PessoaOutputDTO {
    private Long id;
    private String nome;
    private Boolean ativo;
    private Endereco endereco;
}
