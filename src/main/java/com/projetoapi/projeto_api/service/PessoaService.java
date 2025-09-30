package com.projetoapi.projeto_api.service;

import com.projetoapi.projeto_api.model.Pessoa;
import com.projetoapi.projeto_api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    PessoaRepository pessoaRepository;

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
        return getPessoa(codigo, pessoa);
    }

    private Pessoa getPessoa(Long codigo, Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);

        if (pessoaSalva == null) {
            throw new EmptyResultDataAccessException(1);
        }

        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
        Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    private Pessoa buscarPessoaPeloCodigo(Long codigo) {
        Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);

        if (pessoaSalva == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return pessoaSalva;
    }
}
