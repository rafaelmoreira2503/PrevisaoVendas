package br.com.previsao.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.previsao.model.Empresa;
import br.com.previsao.service.NegocioException;
import br.com.previsao.util.jpa.Transactional;

public class Empresas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Empresa guardar(Empresa empresa) {

		return manager.merge(empresa);
	}

	public Empresa porId(Long id) {
		return manager.find(Empresa.class, id);
	}

	public List<Empresa> todasEmpresas() {

		return manager.createNamedQuery(Empresa.TODAS, Empresa.class)
				.getResultList();

	}

	public Empresa porCnpj(String cnpj) {
		try {
			return manager.createNamedQuery(Empresa.PORCNPJ, Empresa.class)
					.setParameter("cnpj", cnpj).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	

	@Transactional
	public void remover(Empresa empresa) {
		try {
			empresa = porId(empresa.getCodigo());
			manager.remove(empresa);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Empresa não pode ser excluída.");
		}
	}

	public int countAll(Map<String, String> filtros) {
		String jpql = Empresa.TOTAL;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Empresa> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {
		String jpql = Empresa.TODAS;

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
		TypedQuery<Empresa> query = manager.createNamedQuery(jpql,
				Empresa.class);

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

	public List<Empresa> empresasParaImportacao() {
		return manager.createNamedQuery(Empresa.TODAS,
				Empresa.class).getResultList();

	}

}