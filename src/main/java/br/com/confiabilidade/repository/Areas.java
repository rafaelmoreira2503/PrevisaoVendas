package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.confiabilidade.controller.FiltroArea;
import br.com.confiabilidade.model.Area;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class Areas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Area porId(Long id) {

		return manager.find(Area.class, id);

	}

	public void guardar(Area area) {

		manager.merge(area);
	}

	@Transactional
	public void remover(Area area) {
		try {
			area = porId(area.getCodigo());
			manager.remove(area);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Área não pode ser excluída.");
		}
	}

	@Transactional
	public void removerAreas(List<Area> areas) {

		for (Area area : areas) {

			try {
				area = porId(area.getCodigo());
				manager.remove(area);
				manager.flush();
			} catch (PersistenceException e) {
				throw new NegocioException("Área não pode ser excluída.");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<Area> filtrados(FiltroArea filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}
		
		return criteria.list();
	}
	
	public int quantidadeFiltrados(FiltroArea filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setProjection(Projections.rowCount());
		
		return ((Number) criteria.uniqueResult()).intValue();
	}
	
	private Criteria criarCriteriaParaFiltro(FiltroArea filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Area.class);
		
		if (StringUtils.isNotEmpty(filtro.getDescricao())) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
		}
		
		return criteria;
	}

	
	public Area porNome(String nome) {
		try {
			return manager.createNamedQuery(Area.DESCRICAO, Area.class)
					.setParameter("descricao", nome.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Area> temComoArea() {
		try {
			return manager.createQuery(" from Area", Area.class)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	

	public int countAll(Map<String, String> filtros) {
		String jpql = Area.COUNT;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Area> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {
		String jpql = Area.ALL;

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY a." + ordernarPeloCampo + " "
					+ ordernarAscOuDesc;
		}

		// metodo buscarPorPaginacao
		TypedQuery<Area> query = manager.createNamedQuery(jpql, Area.class);

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
