package br.com.confiabilidade.model;

public enum Resposta {

	SEMPRE(5, "SEMPRE"),

	QUASESEMPRE(3, "QUASE SEMPRE"),

	RARAMENTE(2, "RARAMENTE"),

	NUNCA(1, "NUNCA");

	private String descricao;

	private Integer valor;

	Resposta(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return this.descricao;
	}
	
	

}