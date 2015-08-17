/**
 * 
 */
package org.isf.examination.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.isf.examination.model.PatientExamination;
import org.isf.generaldata.ExaminationParameters;
import org.isf.patient.model.Patient;
import org.isf.utils.db.HybernateSessions;

/**
 * @author Mwithi
 * 
 */
public class ExaminationOperations {

	private static Session session;

	/**
	 * 
	 */
	public ExaminationOperations() {
		ExaminationParameters.getExaminationParameters();
	}

	/**
	 * Default PatientExamination
	 */
	public PatientExamination getDefaultPatientExamination(Patient patient) {
		PatientExamination defaultPatient = new PatientExamination(new Timestamp(new Date().getTime()), patient, ExaminationParameters.HEIGHT_INIT, ExaminationParameters.WEIGHT_INIT,
				ExaminationParameters.AP_MIN, ExaminationParameters.AP_MAX, ExaminationParameters.HR_INIT, ExaminationParameters.TEMP_INIT, ExaminationParameters.SAT_INIT, "");
		return defaultPatient;
	}

	/**
	 * Get from last PatientExamination (only height, weight & note)
	 */
	public PatientExamination getFromLastPatientExamination(PatientExamination lastPatientExamination) {
		PatientExamination newPatientExamination = new PatientExamination(new Timestamp(new Date().getTime()), lastPatientExamination.getPatient(), lastPatientExamination.getPex_height(),
				lastPatientExamination.getPex_weight(), lastPatientExamination.getPex_pa_min(), lastPatientExamination.getPex_pa_max(), lastPatientExamination.getPex_fc(), 
				lastPatientExamination.getPex_temp(), lastPatientExamination.getPex_sat(), lastPatientExamination.getPex_note());
		return newPatientExamination;
	}

	/**
	 * 
	 * @param path
	 *            - the PatientHistory to save
	 */
	public void saveOrUpdate(PatientExamination patex) {
		session = HybernateSessions.getSession("h8.properties");
		Transaction tx = session.beginTransaction();
		try {
			session.saveOrUpdate(patex);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	public PatientExamination getByID(int ID) {
		session = HybernateSessions.getSession("h8.properties");
		PatientExamination patex = null;
		Transaction tx = session.beginTransaction();
		try {
			patex = (PatientExamination) session.get(PatientExamination.class, ID);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return patex;
	}

	@SuppressWarnings("unchecked")
	public PatientExamination getLastByPatID(int patID) {
		session = HybernateSessions.getSession("h8.properties");
		PatientExamination patex = null;
		Transaction tx = session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(PatientExamination.class);
			crit.add(Restrictions.eq("patient.code", patID));
			crit.addOrder(Order.desc("pex_date"));
			crit.setMaxResults(1);
			List<PatientExamination> list = crit.list();
			if (!list.isEmpty())
				patex = list.get(0);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return patex;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PatientExamination> getLastNByPatID(int patID, int number) {
		session = HybernateSessions.getSession("h8.properties");
		ArrayList<PatientExamination> patexList = new ArrayList<PatientExamination>();
		Transaction tx = session.beginTransaction();
		try {

			Criteria crit = session.createCriteria(PatientExamination.class);
			crit.add(Restrictions.eq("patient.code", patID));
			crit.addOrder(Order.desc("pex_date"));
			crit.setMaxResults(number);
			patexList = (ArrayList<PatientExamination>) crit.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return patexList;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PatientExamination> getByPatID(int patID) {
		session = HybernateSessions.getSession("h8.properties");
		ArrayList<PatientExamination> patexList = new ArrayList<PatientExamination>();
		Transaction tx = session.beginTransaction();
		try {

			Criteria crit = session.createCriteria(PatientExamination.class);
			crit.add(Restrictions.eq("patient.code", patID));
			crit.addOrder(Order.desc("pex_date"));
			patexList = (ArrayList<PatientExamination>) crit.list();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
		return patexList;

	}
}
