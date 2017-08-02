package br.com.previsao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Usuario;
import br.com.previsao.modelolazy.GerenteLazyList;
import br.com.previsao.repository.Empresas;
import br.com.previsao.repository.Usuarios;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaGerentesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios gerentes;

	@Inject
	private Empresas empresas;

	@NotNull
	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	private Empresa empresaSelecionada;
	private List<Empresa> listaDeEmpresas;

	public List<Empresa> getListaDeEmpresas() {
		return listaDeEmpresas;
	}

	private List<Usuario> listaDeGerentes = new ArrayList<>();

	private Usuario gerenteSelecionado;

	private GerenteLazyList lazyGerentes;

	public PesquisaGerentesBean() {
		empresaSelecionada = new Empresa();
	}

	public List<Usuario> getGerentes() {
		return listaDeGerentes;
	}

	public void pesquisar() {

		lazyGerentes = new GerenteLazyList(gerentes, empresaSelecionada);
	}

	public void inicializar() {
		listaDeEmpresas = empresas.todasEmpresas();

	}

	public void excluir() {
		gerentes.remover(gerenteSelecionado);
		listaDeGerentes.remove(gerenteSelecionado);

		FacesUtil.addInfoMessage("Gerente " + gerenteSelecionado.getNome());
		// + FacesUtil.getMensagemI18n("excluido"));
	}

	public Usuario getGerenteSelecionado() {
		return gerenteSelecionado;
	}

	public void setGerenteSelecionado(Usuario gerenteSelecionado) {
		this.gerenteSelecionado = gerenteSelecionado;
	}

	public GerenteLazyList getLazyModel() {
		return lazyGerentes;
	}
}
