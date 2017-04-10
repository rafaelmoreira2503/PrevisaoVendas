package br.com.confiabilidade.security;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.confiabilidade.model.Usuario;

@Named
@RequestScoped
public class Seguranca implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ExternalContext externalContext;

	public String getNomeUsuario() {
		String nome = null;

		UsuarioSistema usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			nome = usuarioLogado.getUsuario().getNome();
		}

		return nome;
	}

	public UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;

		// Classe do Spring-Security
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext
				.getCurrentInstance().getExternalContext().getUserPrincipal(); // Pega
																				// uma
																				// instancia
																				// do
																				// usuaruio
																				// logado

		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal(); // Retorna objeto do
															// tipo
															// usuarioSistema
		}

		return usuario;
	}

	@Produces
	@SessionScoped
	@UsuarioLogado
	public Usuario produzUsuarioLogado() {
		UsuarioSistema usuarioLogado = getUsuarioLogado();

		return usuarioLogado.getUsuario();
	}

	public boolean isCadastrarUsuarioPermitido() {

		return externalContext.isUserInRole("OMC");
	}

	public boolean isEncarregado() {

		return externalContext.isUserInRole("ENCARREGADO");
	}

	public boolean isTecnico() {

		return externalContext.isUserInRole("TECNICO");
	}

	public boolean isGerenteDoProjeto() {

		return externalContext.isUserInRole("GERENTE");
	}
	
	public boolean isGerenteDoProjetoOuEncarregado() {

		return externalContext.isUserInRole("GERENTE") ||
				externalContext.isUserInRole("ENCARREGADO");
	}

	
}
