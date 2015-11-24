package org.isf.therapy.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.gui.Menu;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.therapy.model.Therapy;
import org.isf.therapy.model.TherapyRow;
import org.isf.therapy.service.TherapyIoOperations;
import org.isf.utils.exception.OHException;
import org.joda.time.DateTime;

public class TherapyManager {
	
	private TherapyIoOperations ioOperations = Menu.getApplicationContext().getBean(TherapyIoOperations.class);
	
	public Therapy createTherapy(TherapyRow th) {
		return createTherapy(th.getTherapyID(), th.getPatID(), th.getMedical(), th.getQty(),
				th.getStartDate(), th.getEndDate(), th.getFreqInPeriod(), th.getFreqInDay(), 
				th.getNote(), th.isNotify(), th.isSms());
	}
	
	public Therapy createTherapy(int therapyID, int patID, Medical med, Double qty,
			GregorianCalendar startDate, GregorianCalendar endDate, int freqInPeriod,
			int freqInDay, String note, boolean notify, boolean sms) {
		
		ArrayList<GregorianCalendar> datesArray = new ArrayList<GregorianCalendar>();
		
		GregorianCalendar stepDate = new GregorianCalendar();
		stepDate.setTime(startDate.getTime());
		datesArray.add(new GregorianCalendar(
				startDate.get(GregorianCalendar.YEAR),
				startDate.get(GregorianCalendar.MONTH),
				startDate.get(GregorianCalendar.DAY_OF_MONTH)));
		
		while (stepDate.before(endDate)) {
			
			stepDate.add(GregorianCalendar.DAY_OF_MONTH, freqInPeriod);
			datesArray.add(new GregorianCalendar(
					stepDate.get(GregorianCalendar.YEAR),
					stepDate.get(GregorianCalendar.MONTH),
					stepDate.get(GregorianCalendar.DAY_OF_MONTH))
			);
		}
		
		GregorianCalendar[] dates = new GregorianCalendar[datesArray.size()];
		
		for (int i = 0; i < datesArray.size(); i++) {
			//dates[i] = new GregorianCalendar();
			dates[i] = datesArray.get(i);
			//System.out.println(formatDate(dates[i]));
		}
		
		Therapy th = new Therapy(therapyID,	patID, dates, med, qty, "", freqInDay, note, notify, sms);
		datesArray.clear();
		dates = null;
		
		return th;
	}

	public ArrayList<Therapy> getTherapies(ArrayList<TherapyRow> thRows) {
		
		if (thRows != null) {
			ArrayList<Therapy> therapies = new ArrayList<Therapy>();
			
			for (TherapyRow thRow : thRows) {
				
				therapies.add(createTherapy(thRow));
			}
			return therapies;
		} else {
			return null;
		}
	}

	/**
	 * return the list of {@link TherapyRow}s (therapies) for specified Patient ID
	 * or
	 * return all {@link TherapyRow}s (therapies) if <code>0</code> is passed
	 * 
	 * @param patID - the Patient ID
	 * @return the list of {@link TherapyRow}s (therapies)
	 */
	public ArrayList<TherapyRow> getTherapyRows(int code) {
		try {
			return ioOperations.getTherapyRows(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * insert a new {@link TherapyRow} (therapy) for related Patient
	 * 
	 * @param thRow - the {@link TherapyRow}s (therapy)
	 * @return the therapyID
	 */
	public int newTherapy(TherapyRow thRow) {
		try {
			return ioOperations.newTherapy(thRow);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return 0;
		}
	}
	
	/**
	 * replace all {@link TherapyRow}s (therapies) for related Patient
	 * 
	 * @param thRows - the list of {@link TherapyRow}s (therapies)
	 * @return <code>true</code> if the row has been inserted, <code>false</code> otherwise
	 */
	public boolean newTherapies(ArrayList<TherapyRow> thRows) {
		if (!thRows.isEmpty()) {
			DateTime now = new DateTime();
			
			SmsOperations smsOp = new SmsOperations();
			PatientBrowserManager patMan = new PatientBrowserManager();
			try {
				int patID = thRows.get(0).getPatID();
				ioOperations.deleteAllTherapies(patID);
				smsOp.deleteByModuleModuleID("therapy", String.valueOf(patID));

				for (TherapyRow thRow : thRows) {

					int therapyID = ioOperations.newTherapy(thRow);
					if (therapyID == 0) return false;
					
					thRow.setTherapyID(therapyID);
					if (thRow.isSms()) {
						Therapy th = createTherapy(thRow);
						GregorianCalendar[] dates = th.getDates();						
						for (GregorianCalendar date : dates) {
							date.set(Calendar.HOUR_OF_DAY, 8);
							if (date.after(now.toDateMidnight().toGregorianCalendar())) {
								Patient pat = patMan.getPatient(thRow.getPatID());
								
								Sms sms = new Sms();
								sms.setSmsDateSched(date.getTime());
								sms.setSmsNumber(pat.getTelephone());
								sms.setSmsText(prepareSmsFromTherapy(th));
								sms.setSmsUser(MainMenu.getUser());
								sms.setModule("therapy");
								sms.setModuleID(String.valueOf(patID));
								smsOp.saveOrUpdate(sms);
							}
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

	private String prepareSmsFromTherapy(Therapy th) {

		String note = th.getNote();
		StringBuilder sb = new StringBuilder(MessageBundle.getMessage("angal.therapy.reminderm")).append(": ");
		sb.append(th.getMedical().toString());
		sb.append(th.getQty()).append(th.getUnits()).append(" - ");
		sb.append(th.getFreqInDay()).append("pd");
		if (note != null && !note.equals("")) {
			sb.append(" - ").append(note);
		}
		if (sb.toString().length() > 160) {
		    return sb.toString().substring(0, 160);
		}
		return sb.toString();
	}

	/**
	 * delete all {@link TherapyRow}s (therapies) for specified Patient ID
	 * 
	 * @param patID - the Patient ID
	 * @return <code>true</code> if the therapies have been deleted, <code>false</code> otherwise
	 */
	public boolean deleteAllTherapies(Integer code) {
		try {
			return ioOperations.deleteAllTherapies(code);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}

}
