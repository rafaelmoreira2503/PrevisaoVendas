package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.Justificativa;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.modelolazy.ItemLazyList;
import br.com.confiabilidade.repository.Items;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.CadastroItemService;
import br.com.confiabilidade.util.jsf.FacesUtil;
import br.com.confiabilidade.util.mail.Mailer;

import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@ViewScoped
public class ControleCriticidadeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Items items;

	@Inject
	private Mailer mailer;

	@Inject
	@UsuarioLogado
	private Usuario tecnico;

	@Inject
	private CadastroItemService cadastroItemService;

	private Item itemSelecionado;

	private Justificativa justificativaSelecioinada;

	private ItemLazyList lazyItems;

	public ControleCriticidadeBean() {

		itemSelecionado = new Item();

	}

	public void inicializar() {

		lazyItems = new ItemLazyList(items);

	}

	public void limpar() {
		cadastroItemService.limpar(this.itemSelecionado);

		FacesUtil.addInfoMessage("Item : " + itemSelecionado.getCodigoItem()
				+ " Disponível para Avaliação.");

	}

	public Long getTotalFeito() {
		return items.buscaTotalFeitoParaTecnico(tecnico.getCodigo());

	}

	public Long getTotalAssociado() {
		return items.buscaTotalAssociado(tecnico.getCodigo());

	}

	public void pegaJustificativaSelecionada(Justificativa justificativa) {

	}

	public void justificar() {

		cadastroItemService.justificar(this.itemSelecionado);

		FacesUtil.addInfoMessage("Item : "
				+ FacesUtil.getMensagemI18n("justificado"));

		if (this.itemSelecionado.getJustificativa().equals(
				Justificativa.OUTRAAREA)) {
			MailMessage message = mailer.novaMensagem();

			message.to("otaciliojm@gmail.com")
					.from("contato@omcconsult.com.br")
					.subject("Item Justificado para Outra Àrea :  ")
					.bodyHtml(
							new VelocityTemplate(getClass()
									.getResourceAsStream(
											"/emails/email.template")))
					.put("item", this.itemSelecionado)

					.put("locale",
							FacesContext.getCurrentInstance().getViewRoot()
									.getLocale()).send();

			FacesUtil.addInfoMessage("Email enviado com sucesso!");

		}
		if (this.itemSelecionado.getJustificativa().equals(
				Justificativa.NAOIDENTIFICADO)) {
			MailMessage message = mailer.novaMensagem();

			message.to("rafaelcarvalhomoreira@gmail.com")
					.from("contato@omcconsult.com.br")
					.subject("Item Não Identificado :  ")
					.bodyHtml(
							new VelocityTemplate(getClass()
									.getResourceAsStream(
											"/emails/email.template")))
					.put("item", this.itemSelecionado)

					.put("locale",
							FacesContext.getCurrentInstance().getViewRoot()
									.getLocale()).send();

			FacesUtil.addInfoMessage("Email enviado com sucesso!");

		}

	}

	public Item getItemSelecionado() {

		return itemSelecionado;
	}

	public void setItemSelecionado(Item itemSelecionado) {

		this.itemSelecionado = itemSelecionado;
	}

	public ItemLazyList getLazyModel() {
		return lazyItems;
	}

	// no método get da sua lista você coloca a chamada dos valores que a
	// popularam:

	@Enumerated(EnumType.STRING)
	// não retorna a posição e sim o que está escrito ou definido como valor
	public List<Justificativa> getJustificativas() {
		return Arrays.asList(Justificativa.values());
	}

	// public Justificativa[] getJustificativas() {
	// return Justificativa.values();
	// }

	public boolean isEditando() {

		return this.itemSelecionado.getCodigo() != null;
	}

	public Justificativa getJustificativaSelecioinada() {
		return justificativaSelecioinada;
	}

	public void setJustificativaSelecioinada(
			Justificativa justificativaSelecioinada) {
		this.justificativaSelecioinada = justificativaSelecioinada;
	}
}
