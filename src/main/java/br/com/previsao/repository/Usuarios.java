package br.com.previsao.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.previsao.model.Empresa;
import br.com.previsao.model.Permissao;
import br.com.previsao.model.Usuario;
import br.com.previsao.security.UsuarioLogado;
import br.com.previsao.service.NegocioException;
import br.com.previsao.util.jpa.Transactional;

public class Usuarios implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	@UsuarioLogado
	private Usuario usuario;

	public Usuario guardar(Usuario usuario) {

		return manager.merge(usuario);
	}

	public Usuario porId(Long id) {

		return manager.find(Usuario.class, id);

	}

	@Transactional
	public void remover(Usuario usuario) throws NegocioException {
		usuario = porId(usuario.getCodigo());

		try {
			manager.remove(usuario);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Usuario não pode ser excluído.");
		}
	}

	public Usuario porEmail(String email) {
		Usuario usuario = null;

		try {
			usuario = this.manager.createNamedQuery(Usuario.POREMAIL, Usuario.class)
					.setParameter("email", email.toLowerCase()).getSingleResult(); // Não
																					// pode
																					// ter
																					// 2
																					// usuarios
																					// com
																					// o
																					// mesmo
																					// email
		} catch (NoResultException e) {

			// throw new
			// NegocioException("nenhum usuário encontrado com o e-mail
			// informado!!!");
		}

		return usuario;
	}

	// Todos os Gerentes da Empresa

	public List<Usuario> carregaDiretor(Long codigo) {
		return manager.createNamedQuery(Usuario.CARREGARDIRETOR, Usuario.class).setParameter("codigo", codigo)

				.getResultList();
	}

	public List<Usuario> carregarGerentesFiliais(Long codigo) {
		try {
			return manager.createNamedQuery(Usuario.CARREGARGERENTEFILIAL, Usuario.class).setParameter("codigo", codigo)

					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Usuario> carregarGerentesRegionais(Long codigo) {
		return manager.createNamedQuery(Usuario.CARREGARENCARGERENTEREGIONAL, Usuario.class).setParameter("codigo", codigo)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> usuariosOmcs() {
		return manager.createNativeQuery("{ CALL procedure() }", Usuario.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaPorPaginacaoGerente(int first, int pageSize, String sortField, String sortOrder,
			Empresa filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.isNull("chefe"));

		criteria.add(Restrictions.eq("empresa", filtro));

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

	public int countAllGerente(Empresa filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class).setProjection(Projections.rowCount());
		criteria.add(Restrictions.isNull("chefe"));

		criteria.add(Restrictions.eq("empresa", filtro));

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaPorPaginacaoEncarregado(int first, int pageSize, String sortField, String sortOrder,
			Empresa filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("permissao", Permissao.GERENTEREGIONAL));

		criteria.add(Restrictions.eq("empresa", filtro));

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

	public int countAllEncarregado(Empresa filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class).setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("permissao", Permissao.GERENTEREGIONAL));

		criteria.add(Restrictions.eq("empresa", filtro));

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaPorPaginacaoTecnico(int first, int pageSize, String sortField, String sortOrder,
			Empresa filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("permissao", Permissao.GERENTEFILIAL));

		criteria.add(Restrictions.eq("empresa", filtro));

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

	public int countAllTecnico(Empresa filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class).setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("permissao", Permissao.GERENTEFILIAL));

		criteria.add(Restrictions.eq("empresa", filtro));

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

}
