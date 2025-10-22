package com.projetoapi.projeto_api.dto;

import com.projetoapi.projeto_api.model.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PessoaInputDTO {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotNull(message = "O status ativo é obrigatório")
    private Boolean ativo;
    
    @Valid
    private Endereco endereco;
}
