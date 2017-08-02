package br.com.previsao.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.previsao.model.Familia;
import br.com.previsao.repository.Familias;
import br.com.previsao.util.jpa.Transactional;
import br.com.previsao.util.jsf.FacesUtil;

public class CadastroFamiliaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Familias Familias;

	// @Inject
	// private FamiliaDAO familiaDAO;

	@Transactional
	public void salvar(Familia familia) {

		Familia familiaExistente = Familias.porNome(familia.getDescricao());
		if (familiaExistente != null && !familiaExistente.equals(familia)) {
			throw new NegocioException(FacesUtil.getMensagemI18n("familia_existente"));
		}
		try {
			this.Familias.guardar(familia);
		} catch (OptimisticLockException e) {

			throw new NegocioException(FacesUtil.getMensagemI18n("concorrencia_familia"));

		}

	}
}