package br.com.previsao.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.previsao.model.Familia;
import br.com.previsao.service.NegocioException;
import br.com.previsao.util.jpa.Transactional;

public class Familias implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Familia porId(Long id) {

		return manager.find(Familia.class, id);

	}

	public void guardar(Familia familia) {

		manager.merge(familia);
	}

	@Transactional
	public void remover(Familia familia) {
		try {
			familia = porId(familia.getCodigo());
			manager.remove(familia);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Área não pode ser excluída.");
		}
	}

	@Transactional
	public void removerFamilias(List<Familia> familias) {

		for (Familia familia : familias) {

			try {
				familia = porId(familia.getCodigo());
				manager.remove(familia);
				manager.flush();
			} catch (PersistenceException e) {
				throw new NegocioException("Familia não pode ser excluída.");
			}
		}
	}

	public Familia porNome(String nome) {
		try {
			return manager.createNamedQuery(Familia.DESCRICAO, Familia.class)
					.setParameter("descricao", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Familia> temComoFamilia() {
		try {
			return manager.createQuery(" from Familia", Familia.class).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public int countAll(Map<String, String> filtros) {
		String jpql = Familia.COUNT;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Familia> buscaPorPaginacao(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {
		String jpql = Familia.ALL;

		jpql += adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY a." + ordernarPeloCampo + " " + ordernarAscOuDesc;
		}

		// metodo buscarPorPaginacao
		TypedQuery<Familia> query = manager.createNamedQuery(jpql, Familia.class);

		popularParametros(query, filtros);

		query.setFirstResult(posicaoPrimeiraLinha);
		query.setMaxResults(maximoPorPagina);
		return query.getResultList();
	}

	// Evitando SQL Injection
	private void popularParametros(TypedQuery<?> query, Map<String, String> filtros) {
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

	public List<Familia> carregaFamilia(Long codigo) {
		try {
			return manager.createQuery("Select f from Familia f where f.empresa.codigo=:codigo ", Familia.class)
					.setParameter("codigo", codigo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
}
