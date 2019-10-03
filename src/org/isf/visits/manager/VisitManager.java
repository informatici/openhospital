/**
 * 
 */
package org.isf.visits.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.sms.manager.SmsManager;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.therapy.model.Therapy;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.visits.model.Visit;
import org.isf.visits.service.VisitsIoOperations;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mwithi
 *
 */
@Component
public class VisitManager {
	
	@Autowired
	private VisitsIoOperations ioOperations;
	
	@Autowired
	private SmsOperations smsOp;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 * @throws OHServiceException 
	 */
	public ArrayList<Visit> getVisits(int patID) throws OHServiceException {
		return ioOperations.getVisits(patID);
	}
	
	/**
	 * insert a new {@link Visit} for related Patient
	 * 
	 * @param visit - the {@link Visit}
	 * @return the visitID
	 * @throws OHServiceException 
	 */
	public int newVisit(Visit visit) throws OHServiceException {
		return ioOperations.newVisit(visit);
	}

	/**
	 * inserts or replaces all {@link Visit}s related to a patID
	 * 
	 * @param patient - the {@link Patient} ID
	 * @param visits - the list of {@link Visit}s related to patID. 
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHServiceException 
	 * @throws OHException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean newVisits(ArrayList<Visit> visits) throws OHServiceException {
		if (!visits.isEmpty()) {
			DateTime now = new DateTime();
			PatientBrowserManager patMan = this.applicationContext.getBean(PatientBrowserManager.class);
			int patID = visits.get(0).getPatient().getCode();
			ioOperations.deleteAllVisits(patID);
			smsOp.deleteByModuleModuleID("visit", String.valueOf(patID));

			for (Visit visit : visits) {
				
				visit.setVisitID(0); //reset ID in order to persist again (otherwise JPA think data is already persisted)
				int visitID = ioOperations.newVisit(visit);
				if (visitID == 0) return false;
				
				visit.setVisitID(visitID);
				if (visit.isSms()) {
					GregorianCalendar date = (GregorianCalendar) visit.getDate().clone(); 
					date.add(Calendar.DAY_OF_MONTH, -1);
					if (visit.getDate().after(now.toDateMidnight().toGregorianCalendar())) {
						Patient pat = patMan.getPatient(visit.getPatient().getName());
						
						Sms sms = new Sms();
						sms.setSmsDateSched(date.getTime());
						sms.setSmsNumber(pat.getTelephone());
						sms.setSmsText(prepareSmsFromVisit(visit));
						sms.setSmsUser(UserBrowsingManager.getCurrentUser());
						sms.setModule("visit");
						sms.setModuleID(String.valueOf(patID));
						smsOp.saveOrUpdate(sms);
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * deletes all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 * @throws OHException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean deleteAllVisits(int patID) throws OHServiceException {
		ioOperations.deleteAllVisits(patID);
		smsOp.deleteByModuleModuleID("visit", String.valueOf(patID));
		return true;
	}
	
	/**
	 * Builds the {@link Sms} text for the specified {@link Visit}
	 * If lenght exceed {@link SmsManager.MAX_LENGHT} the message will be cropped
	 * (example: 
	 * "REMINDER: dd/MM/yy - HH:mm:ss - {@link Visit.getNote()}")
	 * @param th - the {@link Therapy}s
	 * @return a string containing the text
	 */
	private String prepareSmsFromVisit(Visit visit) {
		
		String note = visit.getNote();
		StringBuilder sb = new StringBuilder(MessageBundle.getMessage("angal.visit.reminderm")).append(": ");
		sb.append(visit.toStringSMS());
		if (note != null && !note.equals("")) {
			sb.append(" - ").append(note);
		}
		if (sb.toString().length() > SmsManager.MAX_LENGHT) {
		    return sb.toString().substring(0, SmsManager.MAX_LENGHT);
		}
		return sb.toString();
	}
}
