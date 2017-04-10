package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.modelolazy.PerguntaLazyList;
import br.com.confiabilidade.repository.Perguntas;
import br.com.confiabilidade.service.CadastroPerguntaService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPerguntaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Perguntas perguntas;

	private Pergunta pergunta;

	private List<Pergunta> listaDePerguntas = new ArrayList<>();

	private Pergunta perguntaSelecionada;

	private PerguntaLazyList lazyPerguntas;

	@Inject
	private CadastroPerguntaService cadastroPerguntaService;

	public CadastroPerguntaBean() {
		limpar();

	}

	private void limpar() {
		pergunta = new Pergunta();

	}

	public void inicializar() {

		lazyPerguntas = new PerguntaLazyList(perguntas);

	}

	public void excluir() {
		perguntas.remover(perguntaSelecionada);
		this.listaDePerguntas.remove(perguntaSelecionada);
		FacesUtil
				.addInfoMessage("Pergunta "
						+ perguntaSelecionada.getDescricao()
						+ " excluída com sucesso.");
	}

	public void salvar() {

		cadastroPerguntaService.salvar(this.pergunta);
		limpar();

		FacesUtil.addInfoMessage("Pergunta"
				+ FacesUtil.getMensagemI18n("salva"));
	}

	public boolean isEditando() {
		return this.pergunta.getCodigo() != null;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;

	}

	public List<Pergunta> getListaDePerguntas() {
		return listaDePerguntas;
	}

	public Pergunta getPerguntaSelecionada() {
		return perguntaSelecionada;
	}

	public void setPerguntaSelecionada(Pergunta perguntaSelecionada) {
		this.perguntaSelecionada = perguntaSelecionada;
	}

	public PerguntaLazyList getLazyModel() {
		return lazyPerguntas;
	}

}