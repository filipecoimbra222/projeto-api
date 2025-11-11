package com.projetoapi.projeto_api.service;

import com.projetoapi.projeto_api.model.Pessoa;
import com.projetoapi.projeto_api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa atualizar(Long id, Pessoa pessoaAtualizada) {
        Pessoa pessoaSalva = buscarPessoaPeloId(id);
        
        // Copia as propriedades, exceto o ID
        BeanUtils.copyProperties(pessoaAtualizada, pessoaSalva, "id");
        
        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
        Pessoa pessoaSalva = buscarPessoaPeloId(id);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    public Pessoa buscarPessoaPeloId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }
}
