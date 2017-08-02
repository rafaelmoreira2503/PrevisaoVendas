package br.com.previsao.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.previsao.model.Historico;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Usuario;
import br.com.previsao.security.UsuarioLogado;
import br.com.previsao.service.NegocioException;
import br.com.previsao.util.jpa.Transactional;

public class Historicos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Historico porId(Long id) {

		return manager.find(Historico.class, id);

	}

	@Inject
	@UsuarioLogado
	private Usuario usuario;
	
	public void guardar(Historico historico) {

		manager.merge(historico);
	}

	@Transactional
	public void remover(Historico historico) {
		try {
			historico = porId(historico.getCodigo());
			manager.remove(historico);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Área não pode ser excluída.");
		}
	}

	public Historico porNome(String nome) {
		try {
			return manager.createNamedQuery(Historico.DESCRICAO, Historico.class)
					.setParameter("descricao", nome.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	
	public int countAll(Map<String, String> filtros) {
		String jpql = Historico.COUNT;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Historico> buscaPorPaginacao(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {
		String jpql = Historico.ALL;

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY h." + ordernarPeloCampo + " " + ordernarAscOuDesc;
		}

		// metodo buscarPorPaginacao
		TypedQuery<Historico> query = manager.createNamedQuery(jpql, Historico.class);

		popularParametros(query, filtros);

		query.setFirstResult(posicaoPrimeiraLinha);
		query.setMaxResults(maximoPorPagina);
		return query.getResultList();
	}

	public int countAllPrevisao(Map<String, String> filtros) {
		String jpql = Historico.COUNTPREVISAO;

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class);
		query.setParameter("codigo", usuario.getCodigo());

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Historico> buscaPorPaginacaoPrevisao(int posicaoPrimeiraLinha, int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Linha filtros) {
		String jpql = "select (p) from Produto p where p.gerenteFilial.codigo=:codigo)";

		if (filtros != null){
			jpql +="and p.linha.codigo=:codigolinha";
		}
		//jpql = adicionarParametros(filtros, jpql);
		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY h." + ordernarPeloCampo + " " + ordernarAscOuDesc;
		}
		

		// metodo buscarPorPaginacao
		TypedQuery<Historico> query = manager.createQuery(jpql, Historico.class);
		query.setParameter("codigo", usuario.getCodigo());
		if (filtros != null){
			query.setParameter("codigolinha",filtros.getCodigo());
		}

		
		//popularParametros(query, filtros);

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
	
	public List<Historico> carregaHistorico() {
		try {
			return manager.createNamedQuery(Historico.ALL, Historico.class)
					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
