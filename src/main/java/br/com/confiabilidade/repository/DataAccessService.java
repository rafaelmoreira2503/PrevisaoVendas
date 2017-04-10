package br.com.confiabilidade.repository;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

/**
 * Implementation of the generic Data Access Service All CRUD (create, read,
 * update, delete) basic data access operations for any persistent object are
 * performed in this class.
 * 
 * @author Emre Simtay <emre@simtay.com>
 */

public interface DataAccessService<T> {

	// @Inject
	// private EntityManager em;
	//
	// public DataAccessService() {
	// }
	//
	// private Class<T> type;
	//
	// // if you put @Override annotation above equals method than compiler will
	// // verify if this method actually overrides a super class or interface
	// // method or not.
	// // if its not then it throw compilation error like "method does not
	// override
	// // or implement a method from a super type.
	// // In short @Override annotation saves lot of debugging effort by
	// avoiding
	// // this severe mistake in Java.
	// // This single reason is enough to convince programmer to always use
	// // @Override annotation while implementing super type methods.
	//
	// // That's all on @Override annotation in Java. It's one of the best java
	// // coding practice to use @Override annotation while overriding any
	// method
	// // from super class or interface.
	//
	// /**
	// * Default constructor
	// *
	// * @param type
	// * entity class
	// */
	// public DataAccessService(Class<T> type) {
	// this.type = type;
	// }

	// Using the unchecked because JPA does not have a
	// em.getCriteriaBuilder().createQuery()<T> method

	public List<T> findAll();

	// Using the unchecked because JPA does not have a
	// ery.getSingleResult()<T> method

	public T findOneResult(String namedQuery, Map<String, Object> parameters);

	// Using the unchecked because JPA does not have a
	// ery.getSingleResult()<T> method

	public List<T> findManyResults(String namedQuery,
			Map<String, Object> parameters);

	// between the session beans and my LazyDataModel.

	public List<T> load(int first, int count, String sortField,
			SortOrder sortOrder, Map<String, String> filters);

	public int count();

	public T create(T t);

	public T find(Object id);

	public void delete(Object id);

	public void excluir(Object id, Class<T> classe);

	public void remove(T entity);

	public T update(T entity);;

	public List findWithNamedQuery(String namedQueryName);

	public List findWithNamedQuery(String namedQueryName, Map parameters);

	public List findWithNamedQuery(String queryName, int resultLimit);

	public List<T> findByNativeQuery(String sql);

	public int countTotalRecord(String namedQueryName);

	public List findWithNamedQuery(String namedQueryName, Map parameters,
			int resultLimit);

	public List findWithNamedQuery(String namedQueryName, int start, int end);
}