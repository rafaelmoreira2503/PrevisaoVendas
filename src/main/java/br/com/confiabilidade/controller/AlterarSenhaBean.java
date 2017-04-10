package br.com.confiabilidade.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotBlank;

import br.com.confiabilidade.service.AlteracaoSenhaUsuario;

@Named
@RequestScoped
public class AlterarSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AlteracaoSenhaUsuario alteracaoSenhaUsuario;
	
	@NotBlank
	private String novaSenha;
	@NotBlank
	private String senhaAntiga;

	public void alteraSenha() {

		alteracaoSenhaUsuario.alterarSenha(senhaAntiga, novaSenha);

	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}
}
