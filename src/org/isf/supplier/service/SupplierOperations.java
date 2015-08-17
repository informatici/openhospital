/**
 * 
 */
package org.isf.supplier.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.isf.generaldata.ExaminationParameters;
import org.isf.supplier.model.Supplier;
import org.isf.utils.db.HybernateSessions;

/**
 * @author Mwithi
 * 
 */
public class SupplierOperations {

	private static Session session;

	/**
	 * 
	 */
	public SupplierOperations() {
		ExaminationParameters.getExaminationParameters();
	}

	/**
	 * Save or Update a {@link Supplier}
	 * @param supplier - the {@link Supplier} to save or update
	 * return <code>true</code> if data has been saved, <code>false</code> otherwise. 
	 */
	public boolean saveOrUpdate(Supplier supplier) {
		session = HybernateSessions.getSession("h8.properties");
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(supplier);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Returns a {@link Supplier} with specified ID
	 * @param ID - supplier ID
	 * @return supplier - the supplier with specified ID
	 */
	public Supplier getByID(int ID) {
		session = HybernateSessions.getSession("h8.properties");
		Supplier sup = null;
		Transaction tx = session.beginTransaction();
		try {
			sup = (Supplier) session.get(Supplier.class, ID);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return sup;
	}
	
	/**
	 * Returns the list of all {@link Supplier}s, active and inactive
	 * @return supList - the list of {@link Supplier}s
	 */
	@SuppressWarnings("unchecked")
	public List<Supplier> getAll() {
		session = HybernateSessions.getSession("h8.properties");
		List<Supplier> supList = null;
		Transaction tx = session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Supplier.class);
			supList = (List<Supplier>) crit.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return supList;
	}

	/**
	 * Returns the list of active {@link Supplier}s
	 * @return supList - the list of {@link Supplier}s
	 */
	@SuppressWarnings("unchecked")
	public List<Supplier> getList() {
		session = HybernateSessions.getSession("h8.properties");
		List<Supplier> supList = null;
		Transaction tx = session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Supplier.class);
			crit.add(Restrictions.eq("supDeleted", 'N'));
			supList = (List<Supplier>) crit.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return supList;
	}
}
