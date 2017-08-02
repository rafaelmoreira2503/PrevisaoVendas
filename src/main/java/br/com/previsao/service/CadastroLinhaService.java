package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.previsao.model.Linha;
import br.com.previsao.repository.Linhas;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class CadastroLinhaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Linhas linhas;

//	@Inject
//	private LinhaDAO linhaDAO;

	@Transactional
	public void salvar(Linha linha) {

		Linha linhaExistente = linhas.porNome(linha.getDescricao());
		if (linhaExistente != null && !linhaExistente.equals(linha)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("linha_existente"));
		}
		try {
			this.linhas.guardar(linha);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("concorrencia_linha"));

		}

		
	}
}