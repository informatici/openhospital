/**
 * 
 */
package org.isf.visits.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.gui.Menu;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.therapy.manager.TherapyManager;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.isf.visits.model.Visit;
import org.isf.visits.service.VisitsIoOperations;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mwithi
 *
 */
public class VisitManager {
	
	private final Logger logger = LoggerFactory.getLogger(TherapyManager.class);
	
	private VisitsIoOperations ioOperations = Menu.getApplicationContext().getBean(VisitsIoOperations.class);
	private SmsOperations smsOp = Menu.getApplicationContext().getBean(SmsOperations.class);
	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 * @throws OHServiceException 
	 */
	public ArrayList<Visit> getVisits(int patID) throws OHServiceException {
		try {
			return ioOperations.getVisits(patID);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * insert a new {@link Visit} for related Patient
	 * 
	 * @param visit - the {@link Visit}
	 * @return the visitID
	 * @throws OHServiceException 
	 */
	public int newVisit(Visit visit) throws OHServiceException {
		try {
			return ioOperations.newVisit(visit);
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
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
	public boolean newVisits(ArrayList<Visit> visits) throws OHServiceException {
		if (!visits.isEmpty()) {
			DateTime now = new DateTime();
			PatientBrowserManager patMan = new PatientBrowserManager();
			try {
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
							sms.setSmsUser(MainMenu.getUser());
							sms.setModule("visit");
							sms.setModuleID(String.valueOf(patID));
							smsOp.saveOrUpdate(sms);
						}
					}
				}
			} catch (OHException e) {
				/*Already cached exception with OH specific error message - 
				 * create ready to return OHServiceException and keep existing error message
				 */
				logger.error("", e);
				throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
						e.getMessage(), OHSeverityLevel.ERROR));
			}catch(Exception e){
				//Any exception
				logger.error("", e);
				throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
						MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
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
	public boolean deleteAllVisits(int patID) throws OHServiceException {
		try {
			ioOperations.deleteAllVisits(patID);
			smsOp.deleteByModuleModuleID("visit", String.valueOf(patID));
			return true;
		} catch (OHException e) {
			/*Already cached exception with OH specific error message - 
			 * create ready to return OHServiceException and keep existing error message
			 */
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					e.getMessage(), OHSeverityLevel.ERROR));
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}
	
	private String prepareSmsFromVisit(Visit visit) {
		
		String note = visit.getNote();
		StringBuilder sb = new StringBuilder(MessageBundle.getMessage("angal.visit.reminderm")).append(": ");
		sb.append(visit.toString());
		if (note != null && !note.equals("")) {
			sb.append(" - ").append(note);
		}
		if (sb.toString().length() > 160) {
		    return sb.toString().substring(0, 160);
		}
		return sb.toString();
	}
}
