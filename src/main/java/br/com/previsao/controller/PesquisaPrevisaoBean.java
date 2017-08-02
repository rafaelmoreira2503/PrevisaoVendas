package br.com.previsao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.previsao.model.Familia;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Produto;
import br.com.previsao.model.Usuario;
import br.com.previsao.modelolazy.PrevisaoLazyList;
import br.com.previsao.repository.Familias;
import br.com.previsao.repository.Linhas;
import br.com.previsao.repository.Produtos;
import br.com.previsao.security.UsuarioLogado;
import br.com.previsao.service.CadastroProdutoService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaPrevisaoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Produtos previsoes;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Inject
	private Familias familias;

	@Inject
	private Linhas linhas;

	private Linha linhaSelecionada;

	private Familia familiaSelecionada;

	private List<Linha> linhasDaEmpresa;

	private List<Familia> familiasDaEmpresa;

	private List<Produto> listaDePrevisoes = new ArrayList<>();

	private Produto previsaoSelecionada;

	@Inject
	private CadastroProdutoService cadastroProdutoService;

	private PrevisaoLazyList lazyPrevisoes;

	public List<Produto> getPrevisoes() {
		return listaDePrevisoes;
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			lazyPrevisoes = new PrevisaoLazyList(previsoes, linhaSelecionada);
			familiasDaEmpresa = familias.carregaFamilia(usuario.getEmpresa().getCodigo());
			if (this.familiaSelecionada != null) {
				carregarLinhasComFamilia();

			}

		}
	}

	public void pesquisar() {

		lazyPrevisoes = new PrevisaoLazyList(previsoes, linhaSelecionada);

	}

	public void limpar() {
		linhaSelecionada = new Linha();
		familiaSelecionada = new Familia();
	}

	public void carregarLinhasComFamilia() {

		linhasDaEmpresa = linhas.carregaLinhasComFamilia(familiaSelecionada.getCodigo());
	}

	public void excluir() {
		previsoes.remover(previsaoSelecionada);
		this.listaDePrevisoes.remove(previsaoSelecionada);
		FacesUtil.addInfoMessage("Provisão " + " excluída com sucesso.");
	}

	public void alterarPrevisao(ActionEvent actionEvent) {

		FacesUtil.addInfoMessage("Previsão " + previsaoSelecionada.getQuantidadeRecente());

		cadastroProdutoService.salvar(previsaoSelecionada);
	}

	public boolean isEditando() {
		return this.previsaoSelecionada.getCodigo() != null;
	}
	
	
	public Produto getPrevisaoSelecionada() {
		return previsaoSelecionada;
	}

	public void setPrevisaoSelecionada(Produto previsaoSelecionada) {
		this.previsaoSelecionada = previsaoSelecionada;
	}

	public Linha getLinhaSelecionada() {
		return linhaSelecionada;
	}

	public Familia getFamiliaSelecionada() {
		return familiaSelecionada;
	}

	public void setLinhaSelecionada(Linha linhaSelecionada) {
		this.linhaSelecionada = linhaSelecionada;
	}

	public void setFamiliaSelecionada(Familia familiaSelecionada) {
		this.familiaSelecionada = familiaSelecionada;
	}

	public List<Linha> getLinhasDaEmpresa() {
		return linhasDaEmpresa;
	}

	public List<Familia> getFamiliasDaEmpresa() {
		return familiasDaEmpresa;
	}

	public PrevisaoLazyList getLazyModel() {
		return lazyPrevisoes;
	}

	public List<Produto> getListaDePrevisoes() {
		return listaDePrevisoes;
	}

}
