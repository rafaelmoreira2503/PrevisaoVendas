package br.com.confiabilidade.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.poi.ss.formula.functions.T;
import org.primefaces.model.SortOrder;

import br.com.confiabilidade.service.NegocioException;
import br.com.confiabilidade.util.jpa.Transactional;

public class HibernateGenericDAO<T> implements DataAccessService<T> {
	@Inject
	private EntityManager em;

	public HibernateGenericDAO() {
	}

	private Class<T> type;

	// if you put @Override annotation above equals method than compiler will
	// verify if this method actually overrides a super class or interface
	// method or not.
	// if its not then it throw compilation error like "method does not override
	// or implement a method from a super type.
	// In short @Override annotation saves lot of debugging effort by avoiding
	// this severe mistake in Java.
	// This single reason is enough to convince programmer to always use
	// @Override annotation while implementing super type methods.

	// That's all on @Override annotation in Java. It's one of the best java
	// coding practice to use @Override annotation while overriding any method
	// from super class or interface.

	/**
	 * Default constructor
	 * 
	 * @param type
	 *            entity class
	 */
	public HibernateGenericDAO(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() {
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(type));
		return em.createQuery(cq).getResultList();
	}

	// Using the unchecked because JPA does not have a
	// ery.getSingleResult()<T> method
	@SuppressWarnings("unchecked")
	public T findOneResult(String namedQuery, Map<String, Object> parameters) {
		T result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			// Method that will populate parameters if they are passed not null
			// and empty
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (T) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		}

		return result;
	}

	// Using the unchecked because JPA does not have a
	// ery.getSingleResult()<T> method
	@SuppressWarnings("unchecked")
	public List<T> findManyResults(String namedQuery,
			Map<String, Object> parameters) {
		List<T> result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			// Method that will populate parameters if they are passed not null
			// and empty
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = query.getResultList();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private void populateQueryParameters(Query query,
			Map<String, Object> parameters) {

		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	// between the session beans and my LazyDataModel.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> load(int first, int count, String sortField,
			SortOrder sortOrder, Map<String, String> filters) {
		javax.persistence.criteria.CriteriaBuilder builder = em
				.getCriteriaBuilder();

		javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
		Root root = cq.from(type);
		cq.select(root);
		if (sortField != null) {
			if (sortOrder == SortOrder.ASCENDING) {
				cq.orderBy(builder.asc(root.get(sortField)));
			} else if (sortOrder == SortOrder.DESCENDING) {
				cq.orderBy(builder.desc(root.get(sortField)));
			}
		}
		if (filters != null) {
			Set<Map.Entry<String, String>> entries = filters.entrySet();
			ArrayList<Predicate> predicatesList = new ArrayList<Predicate>(
					entries.size());
			for (Map.Entry<String, String> filter : entries) {
				predicatesList.add(builder.like(root.get(filter.getKey()),
						filter.getValue() + "%"));
			}
			cq.where(predicatesList
					.<Predicate> toArray(new Predicate[predicatesList.size()]));
		}
		javax.persistence.Query q = em.createQuery(cq);
		q.setFirstResult(first);
		q.setMaxResults(count);
		return q.getResultList();
	}

	public int count() {
		javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder()
				.createQuery();
		javax.persistence.criteria.Root<T> rt = cq.from(type);
		cq.select(em.getCriteriaBuilder().count(rt));
		javax.persistence.Query q = em.createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param T
	 *            Object
	 * @return
	 */
	public T create(T t) {
		this.em.persist(t);
		this.em.flush();
		this.em.refresh(t);
		return t;
	}

	/**
	 * Retrieves an entity instance that was previously persisted to the
	 * database
	 * 
	 * @param T
	 *            Object
	 * @param id
	 * @return
	 */
	public T find(Object id) {
		return this.em.find(this.type, id);
	}

	/**
	 * Removes the record that is associated with the entity instance
	 * 
	 * @param type
	 * @param id
	 */
	@Transactional
	public void delete(Object id) {
		Object ref = this.em.getReference(this.type, id);
		try {
			this.em.remove(ref);
			em.flush();

		} catch (PersistenceException e) {
			throw new NegocioException(ref.getClass().getName() + "+ não pode ser excluída.");
		}

	}

	// @Transactional
	// public void remover(Object object) {
	// try {
	// object = porId(object.getCodigo());
	// manager.remove(area);
	// manager.flush();
	// } catch (PersistenceException e) {
	// throw new NegocioException("Área não pode ser excluída.");
	// }
	// }
	@Transactional
	public void excluir(Object id, Class<T> classe) {
		T entityToBeRemoved = em.getReference(classe, id);
		try {
			em.remove(entityToBeRemoved);

			em.flush();
		} catch (PersistenceException e) {
			throw new NegocioException(entityToBeRemoved.getClass().getName()
					+ "+ não pode ser excluída.");
		}

	}

	@Transactional
	public void remove(T entity) {
		try {
			em.remove(em.merge(entity));

			em.flush();
		} catch (PersistenceException e) {
			throw new NegocioException(entity.getClass().getName()
					+ "+ não pode ser excluída.");
		}

	}

	/**
	 * Removes the number of entries from a table
	 * 
	 * @param <T>
	 * @param items
	 * @return
	 */
	// public boolean deleteItems(T[] items) {
	// for (T item : items) {
	// if( item instanceof User){
	// User user = (User)item;
	// if(user.getId() == 1){
	// continue;
	// }
	// }
	// em.remove(em.merge(item));
	// }
	// return true;
	// }
	public T update(T entity) {
		return em.merge(entity);
	}

	/**
	 * Updates the entity instance
	 * 
	 * @param <T>
	 * @param t
	 * @return the object that is updated
	 */
	// public T update(T item) {
	// if (item instanceof User) {
	// User user = (User) item;
	// if (user.getId() == 1) {
	// return item;
	// }
	// }
	// return (T) this.em.merge(item);
	//
	// }

	/**
	 * Returns the number of records that meet the criteria
	 * 
	 * @param namedQueryName
	 * @return List
	 */
	public List findWithNamedQuery(String namedQueryName) {
		return this.em.createNamedQuery(namedQueryName).getResultList();
	}

	/**
	 * Returns the number of records that meet the criteria
	 * 
	 * @param namedQueryName
	 * @param parameters
	 * @return List
	 */
	public List findWithNamedQuery(String namedQueryName, Map parameters) {
		return findWithNamedQuery(namedQueryName, parameters, 0);
	}

	/**
	 * Returns the number of records with result limit
	 * 
	 * @param queryName
	 * @param resultLimit
	 * @return List
	 */
	public List findWithNamedQuery(String queryName, int resultLimit) {
		return this.em.createNamedQuery(queryName).setMaxResults(resultLimit)
				.getResultList();
	}

	/**
	 * Returns the number of records that meet the criteria
	 * 
	 * @param <T>
	 * @param sql
	 * @param type
	 * @return List
	 */
	public List<T> findByNativeQuery(String sql) {
		return this.em.createNativeQuery(sql, type).getResultList();
	}

	/**
	 * Returns the number of total records
	 * 
	 * @param namedQueryName
	 * @return int
	 */
	public int countTotalRecord(String namedQueryName) {
		Query query = em.createNamedQuery(namedQueryName);
		Number result = (Number) query.getSingleResult();
		return result.intValue();
	}

	/**
	 * Returns the number of records that meet the criteria with parameter map
	 * and result limit
	 * 
	 * @param namedQueryName
	 * @param parameters
	 * @param resultLimit
	 * @return List
	 */
	public List findWithNamedQuery(String namedQueryName, Map parameters,
			int resultLimit) {
		Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
		Query query = this.em.createNamedQuery(namedQueryName);
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		for (Map.Entry<String, Object> entry : rawParameters) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	/**
	 * Returns the number of records that will be used with lazy loading /
	 * pagination
	 * 
	 * @param namedQueryName
	 * @param start
	 * @param end
	 * @return List
	 */
	public List findWithNamedQuery(String namedQueryName, int start, int end) {
		Query query = this.em.createNamedQuery(namedQueryName);
		query.setMaxResults(end - start);
		query.setFirstResult(start);
		return query.getResultList();
	}


}
