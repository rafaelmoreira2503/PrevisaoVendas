package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.confiabilidade.model.Pergunta;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class Perguntas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	@Inject
	private EntityManager manager;

	public Pergunta porId(Long id) {
		return manager.find(Pergunta.class, id);
	}

	public void guardar(Pergunta pergunta) {

		manager.merge(pergunta);
	}

	@Transactional
	public void remover(Pergunta pergunta) {
		try {
			pergunta = porId(pergunta.getCodigo());
			manager.remove(pergunta);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Pergunta não pode ser excluída.");
		}
	}

	public List<Pergunta> todasPerguntasAssociadasEmpresa(Long codigo) {

		return manager.createNamedQuery(Pergunta.ASSOCIADAS, Pergunta.class)
				.setParameter("codigo", codigo)

				.getResultList();

	}

	public List<Pergunta> todasPerguntasNaoAssociadasEmpresa(Long codigo) {

		return manager.createNamedQuery(Pergunta.NAOASSOCIADAS, Pergunta.class)
				.setParameter("codigo", codigo).getResultList();

	}

	public List<Pergunta> todasPerguntas() {

		try {
			return manager.createNamedQuery(Pergunta.TODAS, Pergunta.class)

			.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Implementando o Lazy Loading

	public int countAll(Map<String, String> filtros) {
		String jpql = Pergunta.TOTAL;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Pergunta> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {
		String jpql = Pergunta.TODAS;

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY p." + ordernarPeloCampo + " "
					+ ordernarAscOuDesc;
		}
		// metodo buscarPorPaginacao
		TypedQuery<Pergunta> query = manager.createNamedQuery(jpql,
				Pergunta.class);

		popularParametros(query, filtros);

		query.setFirstResult(posicaoPrimeiraLinha);
		query.setMaxResults(maximoPorPagina);
		return query.getResultList();
	}

	// Evitando SQL Injection
	private void popularParametros(TypedQuery<?> query,
			Map<String, String> filtros) {
		for (Map.Entry<String, String> entry : filtros.entrySet()) {
			query.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
		}
	}

	private String adicionarParametros(Map<String, String> filtros, String jpql) {
		if (filtros != null && !filtros.isEmpty()) {
			jpql += " where ";
			for (Map.Entry<String, String> entry : filtros.entrySet()) {
				jpql += entry.getKey() + " like :" + entry.getKey() + " and ";
			}

			// remove ultimo and desnecessario
			jpql = jpql.substring(0, jpql.length() - 4);
		}

		return jpql;
	}

}