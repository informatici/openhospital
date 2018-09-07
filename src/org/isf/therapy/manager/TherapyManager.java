package org.isf.therapy.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicals.service.MedicalsIoOperations;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
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
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TherapyManager {
	
	private final Logger logger = LoggerFactory.getLogger(TherapyManager.class);
	
	private TherapyIoOperations ioOperations = Menu.getApplicationContext().getBean(TherapyIoOperations.class);
	private SmsOperations smsOp = Menu.getApplicationContext().getBean(SmsOperations.class);
	
	public Therapy createTherapy(TherapyRow th) throws OHServiceException {
		try{
			return createTherapy(th.getTherapyID(), th.getPatID().getCode(), th.getMedical(), th.getQty(),
					th.getStartDate(), th.getEndDate(), th.getFreqInPeriod(), th.getFreqInDay(), 
					th.getNote(), th.isNotify(), th.isSms());
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.therapy.notavailable"), OHSeverityLevel.ERROR));
		}
	}
	
	private Therapy createTherapy(int therapyID, int patID, Integer medId, Double qty,
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
		
		MedicalsIoOperations medicalIoOperations = Menu.getApplicationContext().getBean(MedicalsIoOperations.class);
		Medical med = null;
		try {
			med = medicalIoOperations.getMedical(medId);
		} catch (OHException e) {
			logger.warn("", e);
		}
		Therapy th = new Therapy(therapyID,	patID, dates, med, qty, "", freqInDay, note, notify, sms);
		datesArray.clear();
		dates = null;
		
		return th;
	}

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
		try {
			return ioOperations.getTherapyRows(code);
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
					MessageBundle.getMessage("angal.therapy.notavailable"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * insert a new {@link TherapyRow} (therapy) for related Patient
	 * 
	 * @param thRow - the {@link TherapyRow}s (therapy)
	 * @return the therapyID
	 * @throws OHServiceException 
	 */
	public TherapyRow newTherapy(TherapyRow thRow) throws OHServiceException {
		try {
			return ioOperations.newTherapy(thRow);
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
					MessageBundle.getMessage("angal.therapy.patienttherapiescouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}
	
	/**
	 * replace all {@link TherapyRow}s (therapies) for related Patient
	 * 
	 * @param thRows - the list of {@link TherapyRow}s (therapies)
	 * @return <code>true</code> if the row has been inserted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean newTherapies(ArrayList<TherapyRow> thRows) throws OHServiceException {
		if (!thRows.isEmpty()) {
			DateTime now = new DateTime();
			
			PatientBrowserManager patMan = new PatientBrowserManager();
			try {
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
								Patient pat = patMan.getPatient(thRow.getPatID().getName());


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
						MessageBundle.getMessage("angal.therapy.patienttherapiescouldnotbesaved"), OHSeverityLevel.ERROR));
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
	 * @param patient - the Patient ID
	 * @return <code>true</code> if the therapies have been deleted, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean deleteAllTherapies(Integer code) throws OHServiceException {
		try {
			ioOperations.deleteAllTherapies(code);
			smsOp.deleteByModuleModuleID("therapy", String.valueOf(code));
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

	public ArrayList<Medical> getMedicalsOutOfStock(ArrayList<Therapy> therapies) throws OHServiceException {
		MovWardBrowserManager wardManager = new MovWardBrowserManager();
		MedicalBrowsingManager medManager = new MedicalBrowsingManager();
		ArrayList<Medical> medOutStock = new ArrayList<Medical>();

		try{
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
					int currentQuantity = wardManager.getCurrentQuantity(null, med);
					actualQty += currentQuantity;

					if (neededQty > actualQty) {
						if(!medOutStock.contains(med)){
							medOutStock.add(med);
						}
					}
				}
			}
			return medOutStock;
		} catch (OHServiceException e) {
			throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.utils.dbserverconnectionfailure"), OHSeverityLevel.ERROR));
		}
	}

	public TherapyRow newTherapy(int therapyID, int patID, GregorianCalendar startDate, GregorianCalendar endDate,
			Medical medical, Double qty, int unitID, int freqInDay, int freqInPeriod, String note, boolean notify,
			boolean sms) throws OHServiceException {
		try{
			PatientBrowserManager patientManager = new PatientBrowserManager();
			Patient patient = patientManager.getPatient(patID);
			TherapyRow thRow = new TherapyRow(therapyID, patient, startDate, endDate, medical, qty, unitID, freqInDay, freqInPeriod, note, notify, sms);
			return newTherapy(thRow);
		} catch (OHServiceException e) {
			throw e;
		}catch(Exception e){
			//Any exception
			logger.error("", e);
			throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
		}
	}

}
