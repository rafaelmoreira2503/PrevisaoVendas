package br.com.confiabilidade.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.com.confiabilidade.model.Empresa;
import br.com.confiabilidade.model.Especialidade;
import br.com.confiabilidade.model.Permissao;
import br.com.confiabilidade.model.Usuario;
import br.com.confiabilidade.security.UsuarioLogado;
import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

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
			usuario = this.manager
					.createNamedQuery(Usuario.POREMAIL, Usuario.class)
					.setParameter("email", email.toLowerCase())
					.getSingleResult(); // Não pode ter 2 usuarios com o mesmo
										// email
		} catch (NoResultException e) {

			// throw new
			// NegocioException("nenhum usuário encontrado com o e-mail informado!!!");
		}

		return usuario;
	}

	// Verifica se o encarregado já posui a área
	public Usuario porArea(Long codigoArea, Long codigo) {
		try {
			return manager

			.createNamedQuery(Usuario.BUSCAEAENCARREGADOEXISTENTE,

			Usuario.class).setParameter("codigoArea", codigoArea)
					.setParameter("codigo", codigo)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	// Verifica se a Especialidade já possui Técnico
	public Usuario porEspecialidade(Usuario tecnico, Especialidade especialidade) {
		try {
			return manager
					.createNamedQuery(Usuario.PORESPECIALIDADE, Usuario.class)
					.setParameter("chefe", tecnico.getChefe())
					.setParameter("especialidade", especialidade)

					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			throw new NegocioException(
					"Só pode existir uma Especialidade para o Técnico ao seu Encarregado");
		}

	}

	// Todos os Gerentes da Empresa

	public List<Usuario> carregaGerente(Long codigo) {
		return manager.createNamedQuery(Usuario.CARREGARGERENTE, Usuario.class)
				.setParameter("codigo", codigo)

				.getResultList();
	}

	public List<Usuario> tecnicosdoEncarregado(Long codigo) {
		try {
			return manager
					.createNamedQuery(Usuario.CARREGARTECNICO, Usuario.class)
					.setParameter("codigo", codigo)

					.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Usuario> carregarEncarregado(Long codigo) {
		return manager
				.createNamedQuery(Usuario.CARREGARENCARREGADO, Usuario.class)
				.setParameter("codigo", codigo).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> usuariosOmcs() {
		return manager.createNativeQuery("{ CALL procedure() }", Usuario.class)
				.getResultList();
	}

	public List<Usuario> buscaAreaEncarregado(Long codigo) {

		return manager
				.createNamedQuery(Usuario.BUSCAAREAENCARREGADO, Usuario.class)
				.setParameter("codigo", codigo).getResultList();

	}

	public Usuario buscaAreaParaVerificarImportacao(Long codigo) {
		try {

			return manager
					.createNamedQuery(Usuario.BUSCAAREAENCARREGADO,
							Usuario.class).setParameter("codigo", codigo)
							
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaPorPaginacaoGerente(int first, int pageSize,
			String sortField, String sortOrder, Empresa filtro) {

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
		Criteria criteria = session.createCriteria(Usuario.class)
				.setProjection(Projections.rowCount());
		criteria.add(Restrictions.isNull("chefe"));

		criteria.add(Restrictions.eq("empresa", filtro));

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaPorPaginacaoEncarregado(int first, int pageSize,
			String sortField, String sortOrder, Empresa filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("permissao", Permissao.ENCARREGADO));

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
		Criteria criteria = session.createCriteria(Usuario.class)
				.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("permissao", Permissao.ENCARREGADO));

		criteria.add(Restrictions.eq("empresa", filtro));

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscaPorPaginacaoTecnico(int first, int pageSize,
			String sortField, String sortOrder, Empresa filtro) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("permissao", Permissao.TECNICO));

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
		Criteria criteria = session.createCriteria(Usuario.class)
				.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("permissao",  Permissao.TECNICO));

		criteria.add(Restrictions.eq("empresa", filtro));

		// eliminando resultados repetidos
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		int total = ((Number) criteria.uniqueResult()).intValue();

		return total;
	}

}
