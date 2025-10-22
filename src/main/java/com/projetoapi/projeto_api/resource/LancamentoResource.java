package com.projetoapi.projeto_api.resource;

import com.projetoapi.projeto_api.dto.LancamentoInputDTO;
import com.projetoapi.projeto_api.dto.LancamentoOutputDTO;
import com.projetoapi.projeto_api.event.RecursoCriadoEvent;
import com.projetoapi.projeto_api.model.Categoria;
import com.projetoapi.projeto_api.model.Lancamento;
import com.projetoapi.projeto_api.model.Pessoa;
import com.projetoapi.projeto_api.repository.LancamentoRepository;
import com.projetoapi.projeto_api.service.LancamentoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    private Lancamento converterParaEntidade(LancamentoInputDTO dto) {
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setDataVencimento(dto.getDataVencimento());
        lancamento.setDataPagamento(dto.getDataPagamento());
        lancamento.setValor(dto.getValor());
        lancamento.setTipo(dto.getTipo());

        Pessoa pessoa = new Pessoa();
        pessoa.setId(dto.getIdPessoa());
        lancamento.setPessoa(pessoa);

        Categoria categoria = new Categoria();
        categoria.setId(dto.getIdCategoria());
        lancamento.setCategoria(categoria);
        return lancamento;
    }

    private LancamentoOutputDTO converterParaDTO(Lancamento lancamento) {
        LancamentoOutputDTO dto = new LancamentoOutputDTO();
        dto.setNomePessoa(lancamento.getPessoa().getNome());
        dto.setId(lancamento.getId());
        dto.setDescricao(lancamento.getDescricao());
        dto.setDataVencimento(lancamento.getDataVencimento());
        dto.setValor(lancamento.getValor());
        dto.setTipo(lancamento.getTipo());
        dto.setDataPagamento(lancamento.getDataPagamento());

        dto.setIdPessoa(lancamento.getPessoa().getId());

        dto.setIdCategoria(lancamento.getCategoria().getId());
        dto.setNomeCategoria(lancamento.getCategoria().getNome());

        return dto;
    }


    @GetMapping("/{codigo}")
    public LancamentoOutputDTO buscarPeloCodigo(@PathVariable Long codigo) {
        Lancamento lancamento = lancamentoRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lancamento não encontrado"));

        return converterParaDTO(lancamento);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LancamentoOutputDTO> salvar(@Valid @RequestBody LancamentoInputDTO dto, HttpServletResponse response) {
        Lancamento lancamento = converterParaEntidade(dto);
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        LancamentoOutputDTO lancamentoOutputDTO = converterParaDTO(lancamentoSalvo);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoOutputDTO);
    }
}
