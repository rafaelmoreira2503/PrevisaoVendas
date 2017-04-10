package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class Especialidades implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public void guardar(Especialidade especialidade) {

		 manager.merge(especialidade);
	}

	public Especialidade porId(Long id) {
		return manager.find(Especialidade.class, id);
	}

	public List<Especialidade> todasEspecialidades() {
		try {
			return manager.createNamedQuery(Especialidade.ALL,
					Especialidade.class).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Especialidade porNome(String descricao) {
		try {
			return manager
					.createNamedQuery(Especialidade.DESCRICAO,
							Especialidade.class)
					.setParameter("descricao", descricao.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public int countEspecialidadesTotal() {

		Query query = manager.createNamedQuery(Especialidade.ALL,
				Especialidade.class);

		Number result = (Number) query.getSingleResult();

		return result.intValue();

	}

	@Transactional
	public void remover(Especialidade especialidade) {
		try {
			especialidade = porId(especialidade.getCodigo());
			manager.remove(especialidade);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Especialidade não pode ser excluída.");
		}
	}

	public int countAll(Map<String, String> filtros) {
		String jpql = Especialidade.COUNT;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Especialidade> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {
		String jpql = Especialidade.ALL;

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY e." + ordernarPeloCampo + " "
					+ ordernarAscOuDesc;
		}
		// metodo buscarPorPaginacao
		TypedQuery<Especialidade> query = manager.createNamedQuery(jpql,
				Especialidade.class);

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
