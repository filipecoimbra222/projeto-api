package com.projetoapi.projeto_api.resource;

import com.projetoapi.projeto_api.dto.LancamentoInputDTO;
import com.projetoapi.projeto_api.dto.LancamentoOutputDTO;
import com.projetoapi.projeto_api.event.RecursoCriadoEvent;
import com.projetoapi.projeto_api.exceptionhandler.ApiExceptionHandler;
import com.projetoapi.projeto_api.model.Categoria;
import com.projetoapi.projeto_api.model.Lancamento;
import com.projetoapi.projeto_api.model.Pessoa;
import com.projetoapi.projeto_api.repository.LancamentoRepository;
import com.projetoapi.projeto_api.resource.filter.LancamentoFilter;
import com.projetoapi.projeto_api.service.LancamentoService;
import com.projetoapi.projeto_api.service.PessoaInexistenteOuInativaException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private MessageSource messageSource;

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

    @ExceptionHandler ({PessoaInexistenteOuInativaException.class})
            public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<ApiExceptionHandler.Error.ErrorData> erros = List.of(new ApiExceptionHandler.Error.ErrorData(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }

    @GetMapping
    public Page<Lancamento> pesquisar (LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
       Lancamento lancamento = lancamentoService.buscarLancamentoPeloId(id);
        lancamentoRepository.delete(lancamento);
    }
}
