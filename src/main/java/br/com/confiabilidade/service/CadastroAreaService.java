package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.repository.AreaDAO;
import br.com.confiabilidade.repository.Areas;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroAreaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Areas areas;

	@Inject
	private AreaDAO areaDAO;

	@Transactional
	public void salvar(Area area) {

		Area areaExistente = areas.porNome(area.getDescricao());
		if (areaExistente != null && !areaExistente.equals(area)) {
			throw new NegocioException(
					FacesUtil.getMensagemI18n("area_existente"));
		}
		try {
			this.areas.guardar(area);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("concorrencia_area"));

		}

		
	}
}