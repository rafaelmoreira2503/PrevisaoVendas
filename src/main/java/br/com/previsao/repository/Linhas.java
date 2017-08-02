package br.com.previsao.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.previsao.model.Linha;
import br.com.previsao.service.NegocioException;
import br.com.previsao.util.jpa.Transactional;

public class Linhas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Linha porId(Long id) {

		return manager.find(Linha.class, id);

	}

	public void guardar(Linha linha) {

		manager.merge(linha);
	}

	@Transactional
	public void remover(Linha linha) {
		try {
			linha = porId(linha.getCodigo());
			manager.remove(linha);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Linha não pode ser excluída.");
		}
	}

	public Linha porNome(String nome) {
		try {
			return manager.createNamedQuery(Linha.DESCRICAO, Linha.class).setParameter("descricao", nome.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public int countAll(Map<String, String> filtros) {
		String jpql = Linha.COUNT;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);

		popularParametros(query, filtros);

		System.out.print(query.getSingleResult().intValue());
		return query.getSingleResult().intValue();
	}

	public List<Linha> buscaPorPaginacao(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {
		String jpql = Linha.ALL;

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY a." + ordernarPeloCampo + " " + ordernarAscOuDesc;
		}

		// metodo buscarPorPaginacao
		TypedQuery<Linha> query = manager.createNamedQuery(jpql, Linha.class);

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

	public List<Linha> carregaLinhas(Long codigo) {
		try {
			return manager.createQuery("Select l from Linha l where l.familia.empresa.codigo=:codigo ", Linha.class)
					.setParameter("codigo", codigo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Linha> carregaLinhasComFamilia(Long codigo) {
		try {
			return manager.createQuery("Select l from Linha l where l.familia.codigo=:codigo ", Linha.class)
					.setParameter("codigo", codigo).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
}
