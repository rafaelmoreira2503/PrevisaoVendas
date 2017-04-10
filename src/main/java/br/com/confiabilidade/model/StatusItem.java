package br.com.confiabilidade.model;

public enum StatusItem {
	
	CRITICADO("Criticado"),
	PENDENTE("Pendente"),
	JUSTIFICADO("Justificado"),
	ASSOCIADO("Associado"),
	NAOSEAPLICA("Não se Aplica"),
	;

	
	private String descricao;
	
	StatusItem(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

}
