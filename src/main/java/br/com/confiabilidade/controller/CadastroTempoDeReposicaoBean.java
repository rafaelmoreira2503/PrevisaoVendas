package br.com.confiabilidade.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.ItemAbc;
import br.com.confiabilidade.model.TempoDeReposicao;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.modelolazy.AbcLazyItems;
import br.com.confiabilidade.repository.ItemsAbc;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.CadastroItemAbcService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroTempoDeReposicaoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private ItemsAbc itemsAbc;

	private TempoDeReposicao tempoDeReposicao;

	private AbcLazyItems lazyItems;

	private ItemAbc itemSelecionado;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	public String getEmpresa() {
		return usuario.getEmpresa().getNomeFantasia();
	}

	@Inject
	private CadastroItemAbcService cadastroItemAbcService;

	public CadastroTempoDeReposicaoBean() {

		limpar();

	}

	private void limpar() {

	}

	public void salvar() {
		try {

			cadastroItemAbcService.salvarTempoDeReposicao(itemSelecionado);

			FacesUtil.addInfoMessage("Tempo de Reposição Salvo com Sucesso.");
		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getMessage());

		}
	}

	public TempoDeReposicao getTempoDeReposicao() {

		return tempoDeReposicao;
	}

	public void setTempoDeReposicao(TempoDeReposicao tempoDeReposicao) {
		this.tempoDeReposicao = tempoDeReposicao;
	}

	public ItemAbc getItemSelecionado() {
		return itemSelecionado;
	}

	public void buscarItemComTempoDeReposicao() {
		if (itemSelecionado.getTempoDeReposicao() == null) {

			itemSelecionado.setTempoDeReposicao(new TempoDeReposicao());

		}

	}

	public void setItemSelecionado(ItemAbc itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	@PostConstruct
	public void inicializar() {

		lazyItems = new AbcLazyItems(itemsAbc);

	}

	public AbcLazyItems getLazyModel() {
		return lazyItems;
	}

}
