package com.projetoapi.projeto_api.repository;

import com.projetoapi.projeto_api.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
