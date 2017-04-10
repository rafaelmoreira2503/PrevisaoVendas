package br.com.confiabilidade.model;

public enum Justificativa {

	OBSOLETO("O Item se tornou Obsoleto", "OBSOLETO") {

	},
	OUTRAAREA("Item de outra Área", "OUTRA ÁREA") {

	},
	NAOIDENTIFICADO("Item não identificado", "NÃO IDENTIFICADO") {

	},
	FALTAINFORMACAO("Falta informação sobre o Item", "FALTA INFORMAÇÃO") {

	};

	private String descricao;
	private String descAbrev;

	Justificativa(String descricao, String descAbrev) {
		this.descricao = descricao;
		this.descAbrev = descAbrev;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public String getDescAbrev() {
		return this.descAbrev;
	}

}
