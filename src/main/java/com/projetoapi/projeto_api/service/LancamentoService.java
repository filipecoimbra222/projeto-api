package com.projetoapi.projeto_api.service;

import com.projetoapi.projeto_api.model.Lancamento;
import com.projetoapi.projeto_api.model.Pessoa;
import com.projetoapi.projeto_api.repository.LancamentoRepository;
import com.projetoapi.projeto_api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LancamentoService {
    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    private Lancamento buscarLancamentoPeloCodigo(Long codigo) {
       Lancamento lancamentoSalva = lancamentoRepository.findById(codigo).orElse(null);

        if (lancamentoSalva == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return lancamentoSalva;
    }
    
    public Lancamento salvar(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getId())
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
            
        if (pessoa == null || !pessoa.getAtivo()) {
            throw new PessoaInexistenteOuInativaException();
        }
        
        return lancamentoRepository.save(lancamento);
    }

    public Lancamento buscarLancamentoPeloId(Long id) {
        return lancamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Lançamento não encontrado."));
    }
}
