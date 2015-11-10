/**
 * 
 */
package org.isf.visits.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.gui.Menu;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;
import org.isf.visits.service.VisitsIoOperations;
import org.joda.time.DateTime;

/**
 * @author Mwithi
 *
 */
public class VisitManager {
	
	private VisitsIoOperations ioOperations = Menu.getApplicationContext().getBean(VisitsIoOperations.class);
	
	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 */
	public ArrayList<Visit> getVisits(int patID) {
		try {
			return ioOperations.getVisits(patID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * insert a new {@link Visit} for related Patient
	 * 
	 * @param visit - the {@link Visit}
	 * @return the visitID
	 */
	public int newVisit(Visit visit) {
		try {
			return ioOperations.newVisit(visit);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 0;
		}
	}

	/**
	 * inserts or replaces all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @param visits - the list of {@link Visit}s related to patID. 
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newVisits(ArrayList<Visit> visits) {
		if (!visits.isEmpty()) {
			DateTime now = new DateTime();
			SmsOperations smsOp = new SmsOperations();
			PatientBrowserManager patMan = new PatientBrowserManager();
			try {
				int patID = visits.get(0).getPatID();
				ioOperations.deleteAllVisits(patID);
				smsOp.deleteByModuleModuleID("visit", String.valueOf(patID));

				for (Visit visit : visits) {
					
					int visitID = ioOperations.newVisit(visit);
					if (visitID == 0) return false;
					
					visit.setVisitID(visitID);
					if (visit.isSms()) {
						GregorianCalendar date = (GregorianCalendar) visit.clone(); 
						date.add(Calendar.DAY_OF_MONTH, -1);
						if (visit.after(now.toDateMidnight().toGregorianCalendar())) {
							Patient pat = patMan.getPatient(visit.getPatID());
							
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
				JOptionPane.showMessageDialog(null, e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * deletes all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteAllVisits(int patID) {
		try {
			return ioOperations.deleteAllVisits(patID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
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
