package com.projetoapi.projeto_api.repository;

import com.projetoapi.projeto_api.model.Lancamento;
import com.projetoapi.projeto_api.resource.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface LancamentoRepositoryQuery {

    public Page<Lancamento> filtrar (LancamentoFilter lancamentoFilter, Pageable pageable);
}
