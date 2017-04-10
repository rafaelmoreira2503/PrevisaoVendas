package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.confiabilidade.model.TempoDeReposicao;
import br.com.confiabilidade.repository.ItemsAbc;
import br.com.confiabilidade.repository.TempoDeReposicoes;
import br.com.confiabilidade.util.jpa.Transactional;

public class CadastroTempoDeReposicaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TempoDeReposicoes tempoDeReposicoes;
	@Inject
	private ItemsAbc itemsAbc;

	@Transactional
	public void salvar(TempoDeReposicao tempoDeReposicao) {

	
		
		if (tempoDeReposicao.getTempoReposicaoImportado() == null) {
			tempoDeReposicao.setTempoReposicaoImportado(0);
		}

		tempoDeReposicao.setTempoReposicao((double) tempoDeReposicao
				.getTempoReposicaoCompras()
				+ tempoDeReposicao.getTempoReposicaoFornecedor()
				+ tempoDeReposicao.getTempoReposicaoImportado()
				+ tempoDeReposicao.getTempoReposicaoRecebimento()
				+ tempoDeReposicao.getTempoReposicaoRequisicao()
				+ tempoDeReposicao.getTempoReposicaoTransporte() / 30.0);

		this.tempoDeReposicoes.guardar(tempoDeReposicao);

	}

}
