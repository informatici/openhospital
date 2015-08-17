package org.isf.sms.service;

// Generated 31-gen-2014 15.39.04 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.isf.sms.model.Sms;
import org.isf.utils.db.HybernateSessions;

/**
 * @see org.isf.sms.model.Sms
 * @author Mwithi
 */
public class SmsOperations {

	private static Session session;

	/**
	 * 
	 */
	public SmsOperations() {}
	
	/**
	 * Save or Update a {@link Sms}
	 * @param supplier - the {@link Sms} to save or update
	 * return <code>true</code> if data has been saved, <code>false</code> otherwise. 
	 */
	public boolean saveOrUpdate(Sms sms) {
		session = HybernateSessions.getSession("h8.properties");
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(sms);
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
	 * Returns a {@link Sms} with specified ID
	 * @param ID - sms ID
	 * @return sms - the sms with specified ID
	 */
	public Sms getByID(int ID) {
		session = HybernateSessions.getSession("h8.properties");
		Sms sup = null;
		Transaction tx = session.beginTransaction();
		try {
			sup = (Sms) session.get(Sms.class, ID);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return sup;
	}
	
	/**
	 * Returns the list of all {@link Sms}s, sent and not sent, between the two dates
	 * @return smsList - the list of {@link Sms}s
	 */
	@SuppressWarnings("unchecked")
	public List<Sms> getAll(Date dateFrom, Date dateTo) {
		session = HybernateSessions.getSession("h8.properties");
		List<Sms> smsList = null;
		Transaction tx = session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Sms.class);
			crit.add(Restrictions.between("smsDateSched", dateFrom, dateTo));
			crit.addOrder(Order.asc("smsDateSched"));
			smsList = (List<Sms>) crit.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return smsList;
	}
	
	/**
	 * Returns the list of not sent {@link Sms}s between the two dates
	 * @return smsList - the list of {@link Sms}s
	 */
	@SuppressWarnings("unchecked")
	public List<Sms> getList(Date dateFrom, Date dateTo) {
		session = HybernateSessions.getSession("h8.properties");
		List<Sms> smsList = null;
		Transaction tx = session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Sms.class);
			crit.add(Restrictions.between("smsDate", dateFrom, dateTo));
			crit.add(Restrictions.isNull("smsDateSent"));
			crit.addOrder(Order.asc("smsDateSched"));
			smsList = (List<Sms>) crit.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return smsList;
	}
	
	/**
	 * Returns the list of not sent {@link Sms}s
	 * @return smsList - the list of {@link Sms}s
	 */
	@SuppressWarnings("unchecked")
	public List<Sms> getList() {
		session = HybernateSessions.getSession("h8.properties");
		List<Sms> smsList = null;
		Transaction tx = session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Sms.class);
			crit.add(Restrictions.isNull("smsDateSent"));
			crit.addOrder(Order.asc("smsDateSched"));
			smsList = (List<Sms>) crit.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return smsList;
	}

	/**
	 * Delete the specified {@link Sms}
	 * @param sms - the {@link Sms} to delete
	 */
	public void delete(Sms sms) {
		session = HybernateSessions.getSession("h8.properties");
		Transaction tx = session.beginTransaction();
		try {
			session.delete(sms);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	/**
	 * Delete the specified {@link Sms}s if not already sent
	 * @param module - the module name which generated the {@link Sms}s
	 * @param moduleID - the module ID within its generated {@link Sms}s
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteByModuleModuleID(String module, String moduleID) {
		session = HybernateSessions.getSession("h8.properties");
		Transaction tx = session.beginTransaction();
		try {
			
			Criteria crit = session.createCriteria(Sms.class);
			crit.add(Restrictions.isNull("smsDateSent"));
			crit.add(Restrictions.eq("module", module));
			crit.add(Restrictions.eq("moduleID", moduleID));
			List<Sms> smsToDelete = (List<Sms>) crit.list();
			
			Iterator it = smsToDelete.iterator();
		    while(it.hasNext()){ 
		        Object obj = it.next();
		        session.delete(obj);
		    }
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}
}
