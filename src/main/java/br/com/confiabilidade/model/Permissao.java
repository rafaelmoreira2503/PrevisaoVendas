package br.com.confiabilidade.model;


public enum Permissao {
	
	OMC("omc"),
	GERENTE("Gerente"),
	ENCARREGADO("Encarregado"),
	TECNICO("Tecnico")
	;
	
	
	private String descricao;
	
	Permissao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

}
