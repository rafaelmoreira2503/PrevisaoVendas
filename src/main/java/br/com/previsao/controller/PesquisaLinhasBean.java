package br.com.previsao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.previsao.model.Linha;
import br.com.previsao.modelolazy.LinhaLazyList;
import br.com.previsao.repository.Linhas;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaLinhasBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Linhas linhas;

	private List<Linha> listaDeLinhas = new ArrayList<>();
	private Linha linhaSelecionada;

	private LinhaLazyList lazyLinhas;

	public List<Linha> getLinhas() {
		return listaDeLinhas;
	}

	public void inicializar() {

		lazyLinhas = new LinhaLazyList(linhas);

	}

	public void excluir() {
		linhas.remover(linhaSelecionada);
		this.listaDeLinhas.remove(linhaSelecionada);
		FacesUtil.addInfoMessage("Linha " + linhaSelecionada.getDescricao() + " excluída com sucesso.");
	}

	public Linha getLinhaSelecionada() {
		return linhaSelecionada;
	}

	public void setLinhaSelecionada(Linha linhaSelecionada) {
		this.linhaSelecionada = linhaSelecionada;
	}

	public LinhaLazyList getLazyModel() {
		return lazyLinhas;
	}

}
