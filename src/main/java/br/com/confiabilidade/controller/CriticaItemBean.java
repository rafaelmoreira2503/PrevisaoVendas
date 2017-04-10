package br.com.confiabilidade.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.model.PerguntaEmpresa;
import br.com.confiabilidade.model.Resposta;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.repository.PerguntaEmpresas;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.CadastroItemService;
import br.com.confiabilidade.service.CadastroPerguntaEmpresaService;
import br.com.confiabilidade.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CriticaItemBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Item item;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Inject
	private PerguntaEmpresas perguntaEmpresas;

	private List<PerguntaEmpresa> perguntasdaCritica;

	private PerguntaEmpresa perguntaEmpresa;

	private List<Pergunta> perguntasDaEmpresa;

	@NotEmpty
	private List<Resposta> Respostas;

	@Inject
	CadastroPerguntaEmpresaService cadastroPerguntaEmpresaService;

	@Inject
	private CadastroItemService cadastroItemService;

	public CriticaItemBean() {
		limpar();
	}

	private void limpar() {

		perguntaEmpresa = new PerguntaEmpresa();

	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {

			perguntasdaCritica = perguntaEmpresas.todasPerguntaEmpresas(usuario
					.getEmpresa().getCodigo());

		}

	}

	public int total() {
		return perguntasdaCritica.size();
	}

	public void criticar() throws IOException {

		if (calculouCriticidade() == true) {

			cadastroItemService.criticar(this.item);

			FacesUtil.addInfoMessage("Item : " + item.getCodigoItem()
					+ " Criticado com sucesso!");

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context
					.getExternalContext().getRequest();
			context.getExternalContext().redirect(
					request.getContextPath()
							+ "/omc/items/ControleCriticidade.xhtml");

		}
	}

	public void associar(PerguntaEmpresa perguntaEmpresa) {

		cadastroPerguntaEmpresaService.associar(perguntaEmpresa, item);

	}

	public boolean isEditando() {
		return this.item.getCodigo() != null;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;

	}

	// no método get da sua lista você coloca a chamada dos valores que a
	// popularam:

	@Enumerated(EnumType.STRING)
	// não retorna a posição e sim o que está escrito ou definido como valor
	public List<Resposta> getRespostas() {
		return Arrays.asList(Resposta.values());
	}

	// @NotNull(message = "Deve ser Informada!!!")
	// public Resposta[] getRespostas() {
	// return Resposta.values();
	// }

	public boolean calculouCriticidade() {
		int somatorio = 0;

		boolean handle = true;

		for (PerguntaEmpresa pergunta : perguntasdaCritica) {

			if (pergunta.getCodigo_pergunta().getCodigo() != null) {

				System.out.println("Pergunta : "
						+ pergunta.getCodigo_pergunta().getDescricao());
				if (pergunta.getResposta() == null) {
					FacesUtil.addErrorMessage("Pergunta : "
							+ pergunta.getCodigo_pergunta().getDescricao()
							+ " deve Ser Respondida!!!");

					handle = false;// O USUARIO DEIXOU DE RESPONDER UMA PERGUNTA
				} else {
					System.out.println("Somatorio :  " + somatorio);

					somatorio += pergunta.getResposta().getValor();
				}
			}
		}

		System.out.println("criticidade: " + somatorio);

		if (somatorio <= 16) {
			item.setCriticidade("Z");
		} else if (somatorio >= 17 && somatorio <= 34) {

			item.setCriticidade("Y");

		} else if (somatorio >= 35) {

			item.setCriticidade("X");

		}
		return handle;
	}

	public PerguntaEmpresa getPerguntaEmpresa() {
		return perguntaEmpresa;
	}

	public void setPerguntaEmpresa(PerguntaEmpresa perguntaEmpresa) {
		this.perguntaEmpresa = perguntaEmpresa;
	}

	public List<PerguntaEmpresa> getPerguntasDaCritica() {
		return perguntasdaCritica;
	}

	public List<Pergunta> getPerguntasDaEmpresa() {
		return perguntasDaEmpresa;
	}

}
