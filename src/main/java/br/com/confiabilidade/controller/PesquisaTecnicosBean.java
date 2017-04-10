package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.modelolazy.TecnicoLazyList;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaTecnicosBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios tecnicos;

	private List<Usuario> listaDeTecnicos = new ArrayList<>();

	private Usuario tecnicoSelecionado;

	private TecnicoLazyList lazyTecnicos;

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

	public List<Usuario> getTecnicos() {
		return listaDeTecnicos;
	}

	public void pesquisar() {
		lazyTecnicos = new TecnicoLazyList(tecnicos, empresaSelecionada);

	}

	public void inicializar() {

		listaDeEmpresas = empresas.todasEmpresas();

	}

	public void excluir() {
		tecnicos.remover(tecnicoSelecionado);
		listaDeTecnicos.remove(tecnicoSelecionado);

		FacesUtil.addInfoMessage("Técnico " + tecnicoSelecionado.getNome()
				+ " excluído com sucesso.");
	}

	public Usuario getTecnicoSelecionado() {
		return tecnicoSelecionado;
	}

	public void setTecnicoSelecionado(Usuario tecnicoSelecionado) {
		this.tecnicoSelecionado = tecnicoSelecionado;
	}

	public TecnicoLazyList getLazyModel() {
		return lazyTecnicos;
	}
}
