package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.confiabilidade.model.ItemAbc;
import br.com.confiabilidade.model.TempoDeReposicao;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class TempoDeReposicoes implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	@UsuarioLogado
	private Usuario usuario;
	
	@Inject
	private EntityManager manager;

	public TempoDeReposicao porId(Long id) {

		return manager.find(TempoDeReposicao.class, id);

	}

	public void guardar(TempoDeReposicao TempoReposicao) {

		manager.merge(TempoReposicao);
	}

	@Transactional
	public void remover(TempoDeReposicao TempoReposicao) {
		try {
			TempoReposicao = porId(TempoReposicao.getCodigo());
			manager.remove(TempoReposicao);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Área não pode ser excluída.");
		}
	}

	@SuppressWarnings("unchecked")
	public List<TempoDeReposicao> buscaPorPaginacaoTempoDeReposicao(int first,
			int pageSize, String sortField, String sortOrder, ItemAbc filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(TempoDeReposicao.class);

		criteria.createAlias("itemAbc", "i", Criteria.LEFT_JOIN);

		criteria.createAlias("i.empresa", "e");

//		if (StringUtils.isNotBlank(filtro.getCodigoItemAbc())) {
//			// acessamos o nome do cliente associado ao pedido pelo alias "c",
//			// criado anteriormente
//			criteria.add(Restrictions.ilike("i.codigoItemAbc",
//					filtro.getCodigoItemAbc(), MatchMode.ANYWHERE));
//		}

		criteria.add(Restrictions.eq("i.Codigo", filtro));
		criteria.add(Restrictions.eq("e", usuario.getEmpresa()));

		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize);

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (sortField != null && !sortField.isEmpty()) {
			if (sortOrder.contains("ASC")) {
				return criteria.addOrder(Order.asc(sortField)).list();
			} else {
				return criteria.addOrder(Order.desc(sortField)).list();
			}
		} else {
			return criteria.list();
		}

	}

	public int countAllTempoDeReposiucoes(ItemAbc filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(TempoDeReposicao.class)
				.setProjection(Projections.rowCount());

		criteria.createAlias("itemAbc", "i");

		criteria.createAlias("i.empresa", "e");

		criteria.add(Restrictions.eq("i", filtro));
		criteria.add(Restrictions.eq("e", usuario.getEmpresa()));

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

}
