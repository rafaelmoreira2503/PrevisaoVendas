package br.com.previsao.security;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.previsao.model.Usuario;

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

	public boolean isGerenteRegional() {

		return externalContext.isUserInRole("GERENTEREGIONAL");
	}

	public boolean isGerenteFilial() {

		return externalContext.isUserInRole("GERENTEFILIAL");
	}

	public boolean isDiretor() {

		return externalContext.isUserInRole("DIRETOR");
	}
	
	public boolean isGerenteDoProjetoOuEncarregado() {

		return externalContext.isUserInRole("DIRETOR") ||
				externalContext.isUserInRole("GERENTEREGIONAL");
	}

	
}
