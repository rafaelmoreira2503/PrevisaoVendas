package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.modelolazy.EmpresaLazyList;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.Perguntas;
import br.com.confiabilidade.service.CadastroEmpresaService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaEmpresasBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;

	@Inject
	private Perguntas perguntas;

	@Inject
	CadastroEmpresaService cadastrarEmpresaService;

	

	private List<Pergunta> perguntasDaEmpresa;

	private List<Empresa> listaDeEmpresas = new ArrayList<>();

	private Empresa empresaSelecionada;

	private EmpresaLazyList lazyEmpresas;

	public List<Empresa> getEmpresas() {
		return listaDeEmpresas;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			perguntasDaEmpresa = perguntas.todasPerguntas();
		}

		lazyEmpresas = new EmpresaLazyList(empresas);

	}

	public void associarPergunta() {

		cadastrarEmpresaService.associarPergunta(empresaSelecionada);

		FacesUtil.addInfoMessage("Operação realizada com sucesso!");
	}

	public void excluir() {
		empresas.remover(empresaSelecionada);
		this.listaDeEmpresas.remove(empresaSelecionada);
		FacesUtil.addInfoMessage("Empresa " + empresaSelecionada.getNome()
				+ " excluída com sucesso.");
	}

	public Empresa getEmpresaSelecionada() {
		return empresaSelecionada;
	}

	public void setEmpresaSelecionada(Empresa empresaSelecionada) {
		this.empresaSelecionada = empresaSelecionada;
	}

	public EmpresaLazyList getLazyModel() {
		return lazyEmpresas;
	}

	public List<Pergunta> getPerguntasDaEmpresa() {
		return perguntasDaEmpresa;
	}

}
