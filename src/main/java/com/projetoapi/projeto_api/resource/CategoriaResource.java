package com.projetoapi.projeto_api.resource;

import java.util.List;
import java.util.stream.Collectors;

import com.projetoapi.projeto_api.dto.CategoriaInputDTO;
import com.projetoapi.projeto_api.dto.CategoriaOutputDTO;
import com.projetoapi.projeto_api.event.RecursoCriadoEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.projetoapi.projeto_api.model.Categoria;
import com.projetoapi.projeto_api.repository.CategoriaRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping
    public List<CategoriaOutputDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<CategoriaOutputDTO> criar(
            @Valid @RequestBody CategoriaInputDTO dto,
            HttpServletResponse response) {
        
        Categoria categoria = converterParaEntidade(dto);
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        
        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converterParaDTO(categoriaSalva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaOutputDTO> buscarPeloId(@PathVariable Long id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Categoria não encontrada com o id: " + id
            ));
        return ResponseEntity.ok(converterParaDTO(categoria));
    }
    
    private Categoria converterParaEntidade(CategoriaInputDTO dto) {
        Categoria categoria = new Categoria();
        BeanUtils.copyProperties(dto, categoria);
        return categoria;
    }

    private CategoriaOutputDTO converterParaDTO(Categoria categoria) {
        CategoriaOutputDTO dto = new CategoriaOutputDTO();
        BeanUtils.copyProperties(categoria, dto);
        return dto;
    }
}
