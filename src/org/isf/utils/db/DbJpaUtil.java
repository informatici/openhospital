package org.isf.utils.db;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Class that executes a query using JPA
 */
@Component
public class DbJpaUtil {
	private static final String PERSISTENCE_UNIT = "OhJpa";
	// private static EntityManagerFactory entityManagerFactory =
	// Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private static Query query;
	
	/**
	 * constructor that initialize the entity Manager
	 * 
	 * @throws ClassNotFoundException
	 * @throws OHException
	 */
	
	public DbJpaUtil() {
	}
	

	/**
	 * constructor that initialize the entity Manager
	 * 
	 * @throws OHException
	 */
	public void open() throws OHException {
		try {
			entityManager = entityManagerFactory.createEntityManager();
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * method to persist an object
	 * 
	 * @throws OHException
	 */
	public void persist(Object entity) throws OHException {
		try {
			System.out.println("Persist: " + entity);
			entityManager.persist(entity);
		} catch (EntityExistsException e) {
			System.out.println("EntityExistsException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (TransactionRequiredException e) {
			System.out.println("TransactionRequiredException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method to merge an object
	 * 
	 * @throws OHException
	 */
	public Object merge(Object entity) throws OHException {
		Object mergedEntity = null;

		try {
			mergedEntity = entityManager.merge(entity);
			System.out.println("Merge: " + mergedEntity);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (TransactionRequiredException e) {
			System.out.println("TransactionRequiredException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return mergedEntity;
	}

	/**
	 * method to find an object
	 * 
	 * @return merged entity
	 * @throws OHException
	 */
	public Object find(Class<?> entityClass, Object primaryKey) throws OHException {
		Object entity = null;

		try {
			entity = entityManager.find(entityClass, primaryKey);
			System.out.println("Find: " + entity);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return entity;
	}

	/**
	 * method to remove an object
	 * 
	 * @return
	 * @throws OHException
	 */
	public void remove(Object entity) throws OHException {
		try {
			System.out.println("Remove: " + entity);
			entityManager.remove(entity);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (TransactionRequiredException e) {
			System.out.println("TransactionRequiredException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method to start a JPA transaction
	 * 
	 * @throws OHException
	 */
	public void beginTransaction() throws OHException {
		try {
			if (getEntityManager() == null)
				open();
			entityManager.getTransaction().begin();

		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method to create a query
	 * 
	 * @param aQuery
	 * @param aClass
	 * @param jpql
	 * @return "Query"
	 * @throws OHException
	 */
	public void createQuery(String aQuery, Class<?> aClass, boolean jpql) throws OHException {
		if (jpql == true) {
			_createJPQLQuery(aQuery, aClass);
		} else {
			_createNativeQuery(aQuery, aClass);
		}

		return;
	}

	/**
	 * method that executes a query and returns a list
	 * 
	 * @param parameters
	 * @param jpql
	 * @throws OHException
	 */
	public void setParameters(List<?> parameters, boolean jpql) throws OHException {
		try {
			for (int i = 0; i < parameters.size(); i++) {
				query.setParameter((i + 1), parameters.get(i));
			}
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method that executes a query and returns a list
	 * 
	 * @return List of objects
	 * @throws OHException
	 */
	public List<?> getList() throws OHException {
		List<?> list = null;

		try {
			list = query.getResultList();
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (QueryTimeoutException e) {
			System.out.println("QueryTimeoutException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (TransactionRequiredException e) {
			System.out.println("TransactionRequiredException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (PessimisticLockException e) {
			System.out.println("PessimisticLockException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (LockTimeoutException e) {
			System.out.println("LockTimeoutException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (PersistenceException e) {
			System.out.println("PersistenceException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("StringIndexOutOfBoundsException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return list;
	}

	/**
	 * method that executes a query and return an object
	 * 
	 * @return Object
	 * @throws OHException
	 */
	public Object getResult() throws OHException {
		Object result = null;

		try {
			result = query.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("NoResultException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (NonUniqueResultException e) {
			System.out.println("NonUniqueResultException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (QueryTimeoutException e) {
			System.out.println("QueryTimeoutException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (TransactionRequiredException e) {
			System.out.println("TransactionRequiredException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (PessimisticLockException e) {
			System.out.println("PessimisticLockException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (LockTimeoutException e) {
			System.out.println("LockTimeoutException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (PersistenceException e) {
			System.out.println("PersistenceException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (Exception e) {
			System.out.println("UnknownException");
			System.out.println(e);
		}

		return result;
	}

	/**
	 * method that executes a query and returns a list
	 * 
	 * @throws OHException
	 */
	public void executeUpdate() throws OHException {
		try {
			query.executeUpdate();
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (QueryTimeoutException e) {
			System.out.println("QueryTimeoutException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (TransactionRequiredException e) {
			System.out.println("TransactionRequiredException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (PessimisticLockException e) {
			System.out.println("PessimisticLockException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (PersistenceException e) {
			System.out.println("PersistenceException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method to commit a JPA transactions
	 * 
	 * @throws OHException
	 */
	public void commitTransaction() throws OHException {
		try {
			entityManager.getTransaction().commit();
			entityManager.clear();
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (RollbackException e) {
			System.out.println("RollbackException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method to flush the JPA transactions
	 * 
	 * @throws OHException
	 */
	public void flush() throws OHException {
		try {
			entityManager.getTransaction().begin();
			entityManager.flush();
			entityManager.getTransaction().commit();
			entityManager.clear();
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (TransactionRequiredException e) {
			System.out.println("TransactionRequiredException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (RollbackException e) {
			System.out.println("RollbackException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method to close the JPA entity manager
	 * 
	 * @throws OHException
	 */
	public void close() throws OHException {
		try {
			entityManager.close();
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	/**
	 * method to destroy the factory
	 * 
	 * @throws OHException
	 */
	public void destroy() throws OHException {
		try {
			entityManagerFactory.close();
		} catch (IllegalStateException e) {
			System.out.println("IllegalStateException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	private void _createJPQLQuery(String aQuery, Class<?> aClass) throws OHException {
		try {
			if (aClass == null) {
				query = entityManager.createQuery(aQuery);
			} else {
				query = entityManager.createQuery(aQuery, aClass);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			System.out.println(e);
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}

		return;
	}

	private void _createNativeQuery(String aQuery, Class<?> aClass) {
		// Native SQL query
		if (aClass == null) {
			query = entityManager.createNativeQuery(aQuery);
		} else {
			query = entityManager.createNativeQuery(aQuery, aClass);
		}

		return;
	}
}
