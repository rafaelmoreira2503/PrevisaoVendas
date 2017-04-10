package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.model.PerguntaEmpresa;
import br.com.confiabilidade.repository.Empresas;
import br.com.confiabilidade.repository.PerguntaEmpresas;
import br.com.confiabilidade.repository.Perguntas;
import br.com.confiabilidade.service.CadastroPerguntaEmpresaService;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class AssociarPerguntaEmpresaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;

	@Inject
	private Perguntas perguntas;

	@Inject
	private PerguntaEmpresas perguntaEmpresas;

	private Pergunta perguntaSelecionada;

	private Pergunta perguntaSelecionadaAssociada;

	private List<Pergunta> perguntasNaoAssociadas;

	private List<Pergunta> perguntasAssociadas;

	private PerguntaEmpresa perguntaEmpresa;

	@Inject
	private CadastroPerguntaEmpresaService cadastroPerguntaEmpresaService;

	private List<Empresa> todasEmpresas;

	public AssociarPerguntaEmpresaBean() {
		limpar();

	}

	private void limpar() {

		perguntaEmpresa = new PerguntaEmpresa();

	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			todasEmpresas = empresas.empresasParaCriticar();
			

		}
	}

	public void carregarPerguntaAssociada() {

		perguntasAssociadas = perguntas
				.todasPerguntasAssociadasEmpresa(perguntaEmpresa
						.getCodigo_empresa().getCodigo());
	}

	public void carregarPerguntaNaoAssociada() {

		perguntasNaoAssociadas = perguntas
				.todasPerguntasNaoAssociadasEmpresa(perguntaEmpresa
						.getCodigo_empresa().getCodigo());
	}

	public void adicionarPergunta() {

		if (perguntaSelecionada != null) {

			if (perguntasAssociadas.size() >= 8) {
				throw new NegocioException(
						"A Empresa não pode ter mais que 8 perguntas!");

			}

			perguntaEmpresa.setCodigo_pergunta(perguntaSelecionada);

			cadastroPerguntaEmpresaService.salvar(this.perguntaEmpresa);
			perguntasNaoAssociadas.remove(perguntaSelecionada);
			perguntasAssociadas.add(perguntaSelecionada);

			FacesUtil.addInfoMessage("Pergunta "
					+ perguntaSelecionada.getDescricao()
					+ " Associada com Sucesso!!!");

		} else {

			FacesUtil.addErrorMessage("Selecione uma Pergunta!");
		}
	}

	public void removerPergunta() {

		if (perguntaSelecionadaAssociada != null) {

			PerguntaEmpresa perguntaEmpresa = perguntaEmpresas
					.buscaPerguntaExistente(
							this.perguntaEmpresa.getCodigo_empresa(),
							perguntaSelecionadaAssociada);

			perguntaEmpresas.remover(perguntaEmpresa);

			perguntasNaoAssociadas.add(perguntaSelecionadaAssociada);
			perguntasAssociadas.remove(perguntaSelecionadaAssociada);

			FacesUtil.addInfoMessage("Pergunta :"
					+ perguntaSelecionadaAssociada.getDescricao()
					+ " Desassociada com sucesso!!!");
		}

	}

	public List<Empresa> getTodasEmpresas() {
		return todasEmpresas;
	}

	public PerguntaEmpresa getPerguntaEmpresa() {
		return perguntaEmpresa;
	}

	public void setPerguntaEmpresa(PerguntaEmpresa perguntaEmpresa) {
		this.perguntaEmpresa = perguntaEmpresa;
	}

	public Pergunta getPerguntaSelecionada() {
		return perguntaSelecionada;
	}

	public void setPerguntaSelecionada(Pergunta perguntaSelecionada) {
		this.perguntaSelecionada = perguntaSelecionada;
	}

	public Pergunta getPerguntaSelecionadaAssociada() {
		return perguntaSelecionadaAssociada;
	}

	public void setPerguntaSelecionadaAssociada(
			Pergunta perguntaSelecionadaAssociada) {
		this.perguntaSelecionadaAssociada = perguntaSelecionadaAssociada;
	}

	public List<Pergunta> getPerguntasNaoAssociadas() {
		return perguntasNaoAssociadas;
	}

	public List<Pergunta> getPerguntasAssociadas() {
		return perguntasAssociadas;
	}

}
