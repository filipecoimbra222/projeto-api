package com.projetoapi.projeto_api.repository;

import com.projetoapi.projeto_api.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
