package br.com.confiabilidade.model.vo;

import java.io.Serializable;

public class UsuarioValor implements Serializable {

	private static final long serialVersionUID = 1L;

	String nomeUsuario;

	Long quantidade;

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

}
