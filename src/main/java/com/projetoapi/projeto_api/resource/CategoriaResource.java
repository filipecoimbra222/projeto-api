package com.projetoapi.projeto_api.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projetoapi.projeto_api.model.Categoria;
import com.projetoapi.projeto_api.repository.CategoriaRepository;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador REST para gerenciar operações relacionadas a Categorias.
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Lista todas as categorias cadastradas.
     * 
     * @return Lista de categorias
     */
    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    /**
     * Cria uma nova categoria.
     * 
     * @param categoria Dados da categoria a ser criada
     * @param response Objeto de resposta HTTP
     * @return A categoria criada
     */
    @PostMapping
    public ResponseEntity<Categoria> criar(
            @Validated @RequestBody Categoria categoria,
            HttpServletResponse response) {
        
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        
        URI uri = ServletUriComponentsBuilder
            .fromCurrentRequestUri()
            .path("/{codigo}")
            .buildAndExpand(categoriaSalva.getCodigo())
            .toUri();
            
        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(categoriaSalva);
    }

    /**
     * Busca uma categoria pelo seu código.
     * 
     * @param codigo Código da categoria
     * @return A categoria encontrada
     * @throws ResponseStatusException Se a categoria não for encontrada
     */
    @GetMapping("/{codigo}")
    public Categoria buscarPeloCodigo(@PathVariable Long codigo) {
        return categoriaRepository.findById(codigo)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Categoria não encontrada com o código: " + codigo
            ));
    }
}
