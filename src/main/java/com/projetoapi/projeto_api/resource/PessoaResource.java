package com.projetoapi.projeto_api.resource;

import com.projetoapi.projeto_api.dto.PessoaInputDTO;
import com.projetoapi.projeto_api.dto.PessoaOutputDTO;
import com.projetoapi.projeto_api.event.RecursoCriadoEvent;
import com.projetoapi.projeto_api.model.Pessoa;
import com.projetoapi.projeto_api.repository.PessoaRepository;
import com.projetoapi.projeto_api.service.PessoaService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public List<PessoaOutputDTO> listar() {
        return pessoaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<PessoaOutputDTO> criar(@Valid @RequestBody PessoaInputDTO dto, HttpServletResponse response) {
        Pessoa pessoa = converterParaEntidade(dto);
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getId()));
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(pessoaSalva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaOutputDTO> buscarPeloId(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.buscarPessoaPeloId(id);
        return ResponseEntity.ok(converterParaDTO(pessoa));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.buscarPessoaPeloId(id);
        pessoaRepository.delete(pessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaOutputDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaInputDTO dto) {
        Pessoa pessoaAtualizada = pessoaService.atualizar(id, converterParaEntidade(dto));
        return ResponseEntity.ok(converterParaDTO(pessoaAtualizada));
    }

    @PutMapping("/{id}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarStatus(@PathVariable Long id, @RequestBody Boolean ativo) {
        pessoaService.atualizarPropriedadeAtivo(id, ativo);
    }


    private Pessoa converterParaEntidade(PessoaInputDTO dto) {
        Pessoa pessoa = new Pessoa();
        BeanUtils.copyProperties(dto, pessoa);
        return pessoa;
    }

    private PessoaOutputDTO converterParaDTO(Pessoa pessoa) {
        PessoaOutputDTO dto = new PessoaOutputDTO();
        BeanUtils.copyProperties(pessoa, dto);
        return dto;
    }
}
