package br.com.confiabilidade.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.util.jsf.FacesUtil;
import br.com.confiabilidade.util.mail.Mailer;

import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

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
