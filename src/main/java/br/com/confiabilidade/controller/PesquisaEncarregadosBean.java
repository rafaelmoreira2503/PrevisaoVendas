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
import br.com.confiabilidade.modelolazy.EncarregadoLazyList;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Usuarios;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEncarregadosBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios encarregados;

	private Usuario encarregado;

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

	public Usuario getEncarregado() {
		return encarregado;
	}

	public void setEncarregado(Usuario encarregado) {
		this.encarregado = encarregado;
	}

	private List<Usuario> listaDeEncarregados = new ArrayList<>();

	private Usuario encarregadoSelecionado;

	private EncarregadoLazyList lazyEncarregados;

	public List<Usuario> getEncarregados() {
		return listaDeEncarregados;
	}

	public void inicializar() {

		listaDeEmpresas = empresas.todasEmpresas();

	}

	public void pesquisar() {
		lazyEncarregados = new EncarregadoLazyList(encarregados,
				empresaSelecionada);

	}

	public void excluir() {
		encarregados.remover(encarregadoSelecionado);
		listaDeEncarregados.remove(encarregadoSelecionado);

		FacesUtil.addInfoMessage("Encarregado "
				+ encarregadoSelecionado.getNome() + " excluído com sucesso.");
	}

	public Usuario getEncarregadoSelecionado() {
		return encarregadoSelecionado;
	}

	public void setEncarregadoSelecionado(Usuario encarregadoSelecionado) {
		this.encarregadoSelecionado = encarregadoSelecionado;
	}

	public EncarregadoLazyList getLazyModel() {
		return lazyEncarregados;
	}
}
