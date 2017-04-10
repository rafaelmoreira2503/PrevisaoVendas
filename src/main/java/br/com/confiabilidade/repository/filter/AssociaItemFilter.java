package br.com.confiabilidade.repository.filter;

import java.io.Serializable;

import br.com.confiabilidade.model.StatusItem;

public class AssociaItemFilter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private StatusItem[] statuses;
	
	private String descricao;

	public StatusItem[] getStatuses() {
		return statuses;
	}

	public void setStatuses(StatusItem[] statuses) {
		this.statuses = statuses;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}
