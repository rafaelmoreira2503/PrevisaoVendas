package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Item;
import br.com.confiabilidade.model.ItemAbc;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class ItemsAbc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	public ItemAbc porId(Long id) {
		return manager.find(ItemAbc.class, id);
	}

	public ItemAbc guardar(ItemAbc item) {

		return manager.merge(item);
	}

	@Transactional
	public void update(Item item) {

		manager.createQuery(
				"update ItemAbc i set i.item=:item where i.codigoItemAbc=:codigoItem and i.empresa.codigo=:codigo")
				.setParameter("item", item)
				.setParameter("codigoItem", item.getCodigoItem())
				.setParameter("codigo", item.getEmpresa().getCodigo())
				.executeUpdate();
	}

	@Transactional
	public void remover(ItemAbc item) {
		try {
			item = porId(item.getCodigo());
			manager.remove(item);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Item não pode ser excluído.");
		}
	}

	public ItemAbc buscaItemAbcExistente(ItemAbc itemAbc) {
		try {
			return manager
					.createQuery(
							"select i from ItemAbc i where i.empresa=:empresa and i.codigoItemAbc =:codigoAbc",
							ItemAbc.class)
					.setParameter("empresa", itemAbc.getEmpresa())
					.setParameter("codigoAbc", itemAbc.getCodigoItemAbc())

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public List<ItemAbc> buscaItemOrdenado(Empresa empresa) {

		return manager.createNamedQuery(ItemAbc.ITEMABCORDENADO, ItemAbc.class)
				.setParameter("empresa", empresa)

				.getResultList();

	}

	public List<ItemAbc> criaPlanilha(Empresa empresa) {

		return manager
				.createQuery(
						"select i from ItemAbc i where i.empresa=:empresa order by i.abc",
						ItemAbc.class).setParameter("empresa", empresa)

				.getResultList();

	}

	public List<ItemAbc> buscaPorPaginacaoItemsAbc(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo,
			String ordernarAscOuDesc, Map<String, String> filtros) {

		// Todos os items onde a area do item é a area do encarregado
		String jpql = "select i from ItemAbc i where i.empresa=:empresa";

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernarAscOuDesc.contains("DESC")) {
				ordernarAscOuDesc = "DESC";
			} else {
				ordernarAscOuDesc = "ASC";
			}

			jpql += " ORDER BY i." + ordernarPeloCampo + " "
					+ ordernarAscOuDesc;
		}
		// metodo buscarPorPaginacao
		TypedQuery<ItemAbc> query = manager.createQuery(jpql, ItemAbc.class)
				.setParameter("empresa", usuario.getEmpresa());

		popularParametros(query, filtros);

		query.setFirstResult(posicaoPrimeiraLinha);
		query.setMaxResults(maximoPorPagina);
		return query.getResultList();
	}

	// Evitando SQL Injection
	private void popularParametros(TypedQuery<?> query,
			Map<String, String> filtros) {
		for (Map.Entry<String, String> entry : filtros.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue() + "%");
		}
	}

	private String adicionarParametros(Map<String, String> filtros, String jpql) {
		if (filtros != null && !filtros.isEmpty()) {
			if (!jpql.contains("where")) {
				jpql += " where ";
			} else {
				jpql += " and i. ";
			}
			for (Map.Entry<String, String> entry : filtros.entrySet()) {
				jpql += entry.getKey() + " like :" + entry.getKey() + " and ";
			}

			// remove ultimo and desnecessario
			jpql = jpql.substring(0, jpql.length() - 4);
		}

		return jpql;
	}

	// Evitando SQL Injection

	public int countAllItemsAbc(Map<String, String> filtros) {

		String jpql = "select count(i) from ItemAbc i where i.empresa=:empresa";
		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = manager.createQuery(jpql, Long.class)
				.setParameter("empresa", usuario.getEmpresa());
		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public int VerificaCriticadosXAbcde() {
		String jpql = ItemAbc.VERIFICAITEMSABCXCRITICADOS;

		TypedQuery<Long> query = manager.createNamedQuery(jpql, Long.class)
				.setParameter("empresa", usuario.getEmpresa());

		return query.getSingleResult().intValue();
	}

	public ItemAbc buscarItemComTempoDeReposicao(Long codigo) {
		return (ItemAbc) manager
				.createQuery(
						"select i from ItemAbc i where i.codigo = ?")
				.setParameter(1, codigo).getSingleResult();
	}

}
