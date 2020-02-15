package org.isf.therapy.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.sms.manager.SmsManager;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.therapy.model.Therapy;
import org.isf.therapy.model.TherapyRow;
import org.isf.therapy.service.TherapyIoOperations;
import org.isf.utils.exception.OHServiceException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TherapyManager {
	
	private final Logger logger = LoggerFactory.getLogger(TherapyManager.class);
	
	public TherapyManager(){}
	
	@Autowired
	private TherapyIoOperations ioOperations;
	
	@Autowired
	private SmsOperations smsOp;
	
	@Autowired
	private PatientBrowserManager patientManager;
	
	@Autowired
	private MedicalBrowsingManager medManager;
	
	/**
	 * Returns a {@link Therapy} object from a {@link TherapyRow} (DB record)
	 * @param th - the {@link TherapyRow}
	 * @return the {@link Therapy}
	 * @throws OHServiceException
	 */
	public Therapy createTherapy(TherapyRow th) throws OHServiceException {
		return createTherapy(th.getTherapyID(), th.getPatID().getCode(), th.getMedical(), th.getQty(),
				th.getStartDate(), th.getEndDate(), th.getFreqInPeriod(), th.getFreqInDay(), 
				th.getNote(), th.isNotify(), th.isSms());
	}
	
	/**
	 * Creates a {@link Therapy} from its parameters, fecthing the {@link Medical} 
	 * and building the array of Dates ({@link GregorianCalendar})
	 * @param therapyID
	 * @param patID
	 * @param medId
	 * @param qty
	 * @param startDate
	 * @param endDate
	 * @param freqInPeriod
	 * @param freqInDay
	 * @param note
	 * @param notify
	 * @param sms
	 * @return the {@link Therapy}
	 */
	private Therapy createTherapy(int therapyID, int patID, Integer medId, Double qty,
			GregorianCalendar startDate, GregorianCalendar endDate, int freqInPeriod,
			int freqInDay, String note, boolean notify, boolean sms) throws OHServiceException {
		
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
		
		Medical med = medManager.getMedical(medId);
		Therapy th = new Therapy(therapyID,	patID, dates, med, qty, "", freqInDay, note, notify, sms);
		datesArray.clear();
		dates = null;
		
		return th;
	}
	
	/**
	 * Returns a list of {@link Therapy}s from a list of {@link TherapyRow}s (DB records)
	 * @param thRows - the list of {@link TherapyRow}s
	 * @return the list of {@link Therapy}s
	 * @throws OHServiceException
	 */
	public ArrayList<Therapy> getTherapies(ArrayList<TherapyRow> thRows) throws OHServiceException {
		
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
	 * @param patient - the Patient ID
	 * @return the list of {@link TherapyRow}s (therapies)
	 * @throws OHServiceException 
	 */
	public ArrayList<TherapyRow> getTherapyRows(int code) throws OHServiceException {
		return ioOperations.getTherapyRows(code);
	}
	
	/**
	 * insert a new {@link TherapyRow} (therapy) for related Patient
	 * 
	 * @param thRow - the {@link TherapyRow}s (therapy)
	 * @return the therapyID
	 * @throws OHServiceException 
	 */
	public TherapyRow newTherapy(TherapyRow thRow) throws OHServiceException {
		return ioOperations.newTherapy(thRow);
	}
	
	/**
	 * replace all {@link TherapyRow}s (therapies) for related Patient
	 * 
	 * @param thRows - the list of {@link TherapyRow}s (therapies)
	 * @return <code>true</code> if the row has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean newTherapies(ArrayList<TherapyRow> thRows) throws OHServiceException {
		if (!thRows.isEmpty()) {
			DateTime now = new DateTime();
			
			
			int patID = thRows.get(0).getPatID().getCode();
			ioOperations.deleteAllTherapies(patID);
			smsOp.deleteByModuleModuleID("therapy", String.valueOf(patID));

			for (TherapyRow thRow : thRows) {

				ioOperations.newTherapy(thRow);
				if (thRow.isSms()) {
					Therapy th = createTherapy(thRow);
					GregorianCalendar[] dates = th.getDates();						
					for (GregorianCalendar date : dates) {
						date.set(Calendar.HOUR_OF_DAY, 8);
						if (date.after(now.toDateMidnight().toGregorianCalendar())) {
							Patient pat = patientManager.getPatient(thRow.getPatID().getName());

							Sms sms = new Sms();
							sms.setSmsDateSched(date.getTime());
							sms.setSmsNumber(pat.getTelephone());
							sms.setSmsText(prepareSmsFromTherapy(th));
							sms.setSmsUser(UserBrowsingManager.getCurrentUser());
							sms.setModule("therapy");
							sms.setModuleID(String.valueOf(patID));
							smsOp.saveOrUpdate(sms);
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Builds the {@link Sms} text for the specified {@link Therapy}
	 * If lenght exceed {@link SmsManager.MAX_LENGHT} the message will be cropped
	 * (example: 
	 * "REMINDER: {@link Medical} 3pcs - 2pd - {@link Thearapy.getNote()}")
	 * @param th - the {@link Therapy}s
	 * @return a string containing the text
	 */
	private String prepareSmsFromTherapy(Therapy th) {

		String note = th.getNote();
		StringBuilder sb = new StringBuilder(MessageBundle.getMessage("angal.therapy.reminderm")).append(": ");
		sb.append(th.getMedical().toString()).append(" - ");
		sb.append(th.getQty()).append(th.getUnits()).append(" - ");
		sb.append(th.getFreqInDay()).append("pd");
		if (note != null && !note.equals("")) {
			sb.append(" - ").append(note);
		}
		if (sb.toString().length() > SmsManager.MAX_LENGHT) {
		    return sb.toString().substring(0, SmsManager.MAX_LENGHT);
		}
		return sb.toString();
	}

	/**
	 * delete all {@link TherapyRow}s (therapies) for specified Patient ID
	 * 
	 * @param patient - the Patient ID
	 * @return <code>true</code> if the therapies have been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public boolean deleteAllTherapies(Integer code) throws OHServiceException {
		ioOperations.deleteAllTherapies(code);
		smsOp.deleteByModuleModuleID("therapy", String.valueOf(code));
		return true;
	}

	/**
	 * Returns the {@link Medical}s that are not available for the specified list of {@link Therapy}s
	 * @param therapies - the list of {@link Therapy}s
	 * @return the list of {@link Medical}s out of stock
	 * @throws OHServiceException
	 */
	@Transactional(rollbackFor=OHServiceException.class)
	public ArrayList<Medical> getMedicalsOutOfStock(ArrayList<Therapy> therapies) throws OHServiceException {
		MovWardBrowserManager wardManager = new MovWardBrowserManager();
		MedicalBrowsingManager medManager = new MedicalBrowsingManager();
		ArrayList<Medical> medOutStock = new ArrayList<Medical>();

		ArrayList<Medical> medArray = medManager.getMedicals();

		Double neededQty = 0.;
		Double actualQty = 0.;

		for (Therapy th : therapies) {

			// CALCULATING NEEDINGS
			Double qty = th.getQty();
			int freq = th.getFreqInDay();
			GregorianCalendar now = new GregorianCalendar();
			GregorianCalendar todayDate = new GregorianCalendar(now
					.get(GregorianCalendar.YEAR), now
					.get(GregorianCalendar.MONTH), now
					.get(GregorianCalendar.DAY_OF_MONTH));

			// todayDate.set(2010, 0, 1);
			int dayCount = 0;
			for (GregorianCalendar date : th.getDates()) {
				if (date.after(todayDate) || date.equals(todayDate))
					dayCount++;
			}

			if (dayCount != 0) {

				neededQty = qty * freq * dayCount;

				// CALCULATING STOCK QUANTITIES
				Medical med = medArray.get(medArray.indexOf(th.getMedical()));
				actualQty = med.getInitialqty() + med.getInqty() - med.getOutqty(); // MAIN STORE
				int currentQuantity = wardManager.getCurrentQuantityInWard(null, med);
				actualQty += currentQuantity;

				if (neededQty > actualQty) {
					if(!medOutStock.contains(med)){
						medOutStock.add(med);
					}
				}
			}
		}
		return medOutStock;
	}

	public TherapyRow newTherapy(int therapyID, int patID, GregorianCalendar startDate, GregorianCalendar endDate,
		Medical medical, Double qty, int unitID, int freqInDay, int freqInPeriod, String note, boolean notify,
		boolean sms) throws OHServiceException {
		
		Patient patient = patientManager.getPatient(patID);
		TherapyRow thRow = new TherapyRow(therapyID, patient, startDate, endDate, medical, qty, unitID, freqInDay, freqInPeriod, note, notify, sms);
		return newTherapy(thRow);
	}

}
