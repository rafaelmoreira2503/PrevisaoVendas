package br.com.previsao.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

import br.com.previsao.model.Usuario;
import br.com.previsao.security.UsuarioLogado;
import br.com.previsao.util.jsf.FacesUtil;
import br.com.previsao.util.mail.Mailer;

@Named
@RequestScoped
public class EnvioEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Mailer mailer;

	@Inject
	@UsuarioLogado
	private Usuario tecnico;

	public void enviarEmail() {

		MailMessage message = mailer.novaMensagem();

		message.to("otaciliojm@gmail.com")
				.from("contato@omcconsult.com.br")
				.subject(
						FacesUtil.getMensagemI18n("quantidade_items")
								+ this.tecnico.getNome())
				.bodyHtml(
						new VelocityTemplate(getClass().getResourceAsStream(
								"/emails/email.template")))
				.put("tecnico", this.tecnico)
				.put("locale", new Locale("pt", "BR")).send();

		FacesUtil.addInfoMessage(FacesUtil.getMensagemI18n("email_enviado"));
	}

}
