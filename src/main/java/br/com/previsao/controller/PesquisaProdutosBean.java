package br.com.previsao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Produto;
import br.com.previsao.modelolazy.ProdutoLazyList;
import br.com.previsao.repository.Empresas;
import br.com.previsao.repository.Linhas;
import br.com.previsao.repository.Produtos;
import br.com.previsao.service.CadastroProdutoService;
import br.com.previsao.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaProdutosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Produtos produtos;

	@Inject
	private Linhas linhas;

	@Inject
	private CadastroProdutoService cadastroProdutoService;

	private List<Linha> linhasDaEmpresa;

	private List<Produto> listaDeProdutos = new ArrayList<>();

	private Produto produtoSelecionado = new Produto();

	private ProdutoLazyList lazyProdutos;

	public List<Produto> getProdutos() {
		return listaDeProdutos;
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
	private List<Empresa> listaDeEmpresas;

	public List<Empresa> getListaDeEmpresas() {
		return listaDeEmpresas;
	}

	public void inicializar() {

		listaDeEmpresas = empresas.todasEmpresas();

	}

	public void pesquisar() {

		linhasDaEmpresa = linhas.carregaLinhas(empresaSelecionada.getCodigo());

		lazyProdutos = new ProdutoLazyList(produtos, empresaSelecionada);

	}

	public void excluir() {
		produtos.remover(produtoSelecionado);
		this.listaDeProdutos.remove(produtoSelecionado);
		FacesUtil.addInfoMessage("Produto " + produtoSelecionado.getDescricao() + " excluído com sucesso.");
	}

	public void associar() {

		FacesUtil.addInfoMessage("Produto " + produtoSelecionado.getDescricao());

		cadastroProdutoService.salvar(this.produtoSelecionado);

		FacesUtil.addInfoMessage("Produto " + FacesUtil.getMensagemI18n("salvo"));

	}

	public List<Linha> getLinhasDaEmpresa() {
		return linhasDaEmpresa;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public ProdutoLazyList getLazyModel() {
		return lazyProdutos;
	}

}
