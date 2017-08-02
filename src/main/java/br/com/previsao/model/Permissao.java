package br.com.previsao.model;


public enum Permissao {
	
	OMC("omc"),
	DIRETOR("Diretor"),
	GERENTEREGIONAL("GerenteRegional"),
	GERENTEFILIAL("GerenteFilial")
	;
	
	
	private String descricao;
	
	Permissao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

}
