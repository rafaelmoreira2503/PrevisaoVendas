package br.com.previsao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.primefaces.event.RowEditEvent;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Historico;
import br.com.previsao.modelolazy.HistoricoLazyList;
import br.com.previsao.repository.Empresas;
import br.com.previsao.repository.Historicos;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaHistoricosBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Historicos historicos;

	private List<Historico> listaDeHistoricos;

	private Historico historicoSelecionado;

	private HistoricoLazyList lazyHistoricos;

	public List<Historico> getHistoricos() {
		return listaDeHistoricos;
	}

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
	private List<Empresa> listaDeEmpresas = new ArrayList<>();

	public List<Empresa> getListaDeEmpresas() {
		return listaDeEmpresas;
	}

	public void inicializar() {

		listaDeEmpresas = empresas.todasEmpresas();

	}
	public void pesquisar() {

		lazyHistoricos = new HistoricoLazyList(historicos);

	}

	public void excluir() {
		historicos.remover(historicoSelecionado);
		this.listaDeHistoricos.remove(historicoSelecionado);
		FacesUtil.addInfoMessage("Historico " + " excluída com sucesso.");
	}

	public Historico getHistoricoSelecionado() {
		return historicoSelecionado;
	}

	public void setHistoricoSelecionado(Historico historicoSelecionado) {
		this.historicoSelecionado = historicoSelecionado;
	}

	public HistoricoLazyList getLazyModel() {
		return lazyHistoricos;
	}

	public void onRowEdit(RowEditEvent event) {
		System.out.println(historicoSelecionado.getQuantidade());

		historicoSelecionado = ((Historico) event.getObject());

		System.out.println(historicoSelecionado.getQuantidade());
	}

}
