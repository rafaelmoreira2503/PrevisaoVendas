package br.com.confiabilidade.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.repository.Especialidades;
import br.com.confiabilidade.util.jpa.Transactional;
import br.com.confiabilidade.util.jsf.FacesUtil;

public class CadastroEspecialidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Especialidades especialidades;

	@Transactional
	public void salvar(Especialidade especialidade) {

		Especialidade especialidadeExistente = especialidades
				.porNome(especialidade.getDescricao());

		if (especialidadeExistente != null // Se existe uma especialidade com o
											// criterio. Isto é válido para
											// cadastrar
				&& !especialidadeExistente.equals(especialidade)) { // e
																	// utilizado
																	// a mesma
																	// página
																	// para
																	// cadastro
																	// e
																	// Edição.Por
																	// isto:
			throw new NegocioException( // e a especialidade existente não é
										// igual a especialidade
					FacesUtil.getMensagemI18n("especialidade_existente"));
		}

		try {
			especialidades.guardar(especialidade);
		} catch (OptimisticLockException e) {

			throw new NegocioException(
					FacesUtil.getMensagemI18n("concorrencia_especialidade"));

		}

	}

}
