package com.projetoapi.projeto_api.repository;

import com.projetoapi.projeto_api.model.Lancamento;
import com.projetoapi.projeto_api.resource.filter.LancamentoFilter;

import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<Lancamento>filtrar (LancamentoFilter lancamentoFilter);
}
