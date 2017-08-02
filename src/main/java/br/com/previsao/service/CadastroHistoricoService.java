package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.previsao.model.Historico;
import br.com.previsao.repository.Historicos;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class CadastroHistoricoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Historicos historicos;

	@Transactional
	public void salvar(Historico historico) {

		System.out.println(historico.getQuantidade());

		try {
			this.historicos.guardar(historico);
		} catch (OptimisticLockException e) {

			throw new NegocioException(FacesUtil.getMensagemI18n("concorrencia_familia"));

		}

	}
}