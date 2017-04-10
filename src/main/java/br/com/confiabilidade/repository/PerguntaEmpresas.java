package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.model.PerguntaEmpresa;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class PerguntaEmpresas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Inject
	private EntityManager manager;

	public PerguntaEmpresa porId(Long id) {
		return manager.find(PerguntaEmpresa.class, id);
	}

	public void guardar(PerguntaEmpresa perguntaempresa) {

		manager.merge(perguntaempresa);
	}

	@Transactional
	public void remover(PerguntaEmpresa perguntaempresa) {
		try {
			perguntaempresa = porId(perguntaempresa.getCodigo());
			manager.remove(perguntaempresa);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("PerguntaEmpresa não pode ser excluída.");
		}
	}

	public List<PerguntaEmpresa> todasPerguntaEmpresas(Long codigo) {

		return manager
				.createNamedQuery(PerguntaEmpresa.TODAS, PerguntaEmpresa.class)
				.setParameter("codigo", codigo)

				.getResultList();

	}

	public PerguntaEmpresa buscaItemExistente(Long codigoEmpresa,
			Long codigoPergunta, Long codigoItem, Long codigoArea) {

		try {
			return manager
					.createNamedQuery(PerguntaEmpresa.PERGUNTACOMITEM,
							PerguntaEmpresa.class)
					.setParameter("codigo", codigoEmpresa)
					.setParameter("codigoPergunta", codigoPergunta)
					.setParameter("codigoItem", codigoItem)
					.setParameter("codigoArea", codigoArea).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public PerguntaEmpresa buscaPerguntaExistente(Empresa empresa,
			Pergunta pergunta) {

		try {
			return manager
					.createNamedQuery(PerguntaEmpresa.PERGUNTAEXISTENTE,
							PerguntaEmpresa.class)
					.setParameter("empresa", empresa)
					.setParameter("pergunta", pergunta).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}


}