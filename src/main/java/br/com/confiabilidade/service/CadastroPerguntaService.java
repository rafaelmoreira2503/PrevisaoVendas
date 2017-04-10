package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.repository.Perguntas;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroPerguntaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Perguntas perguntas;

	@Transactional
	public void salvar(Pergunta pergunta) {
		try {
			this.perguntas.guardar(pergunta);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("concorrencia_pergunta"));

		}
		
	}

}