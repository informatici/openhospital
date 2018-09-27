package org.isf.sms.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.gui.Menu;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsOperations;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;

public class SmsManager {

	private final int MAX_LENGHT = 160;
	private final String NUMBER_REGEX = "^\\+?\\d+$"; //$NON-NLS-1$
	
	private SmsOperations smsOperations = Menu.getApplicationContext().getBean(SmsOperations.class);
	
	public SmsManager(){}
	
	private List<OHExceptionMessage> validateSms(Sms sms) {
		List<OHExceptionMessage> errors = new ArrayList<OHExceptionMessage>();
		String number = sms.getSmsNumber();
		String text = sms.getSmsText();
		
		if (!number.matches(NUMBER_REGEX)) {
			errors.add(new OHExceptionMessage("numberError", 
	        		MessageBundle.getMessage("angal.sms.pleaseinsertavalidtelephonenumber"), 
	        		OHSeverityLevel.ERROR));
		}
		if (text.isEmpty()) {
			errors.add(new OHExceptionMessage("emptyTextError", 
	        		MessageBundle.getMessage("angal.sms.pleaseinsertatext"), 
	        		OHSeverityLevel.ERROR));
		}
		return errors;
	}

	public List<Sms> getAll(Date from, Date to) throws OHServiceException {
		return smsOperations.getAll(from, to);
	}

	/**
	 * Save or Update a {@link Sms}. If the sms's text lenght is greater than 
	 * {@code MAX_LENGHT} it will throw a {@code testMaxLenghtError} error if
	 * {@code split} parameter is set to {@code false}
	 * @param sms - the {@link Sms} to save or update
	 * @param split - specify if to split sms's text longer than {@code MAX_LENGHT}
	 * @throws OHServiceException 
	 * TODO enable GSM Multipart Feature
	 */
	public void saveOrUpdate(Sms smsToSend, boolean split) throws OHServiceException {
		List<OHExceptionMessage> errors = validateSms(smsToSend);
		if(!errors.isEmpty()){
            throw new OHServiceException(errors);
        }
		
		List<Sms> smsList = new ArrayList<Sms>();
		String text = smsToSend.getSmsText();
		int textLenght = text.length();
		if (textLenght > MAX_LENGHT && !split) {
			
			StringBuilder message = new StringBuilder();
			message.append(MessageBundle.getMessage("angal.sms.themessageislongerthen"))
				.append(" ")
				.append(MAX_LENGHT)
				.append(" ")
				.append(MessageBundle.getMessage("angal.sms.chars"));
			throw new OHServiceException(new OHExceptionMessage("testMaxLenghtError", 
					message.toString(), 
					OHSeverityLevel.ERROR));
			
		} else if (textLenght > MAX_LENGHT && split) {
			
			String[] parts = split(text);
			String number = smsToSend.getSmsNumber();
			Date schedDate = smsToSend.getSmsDateSched();
			
			for (String part : parts) {
				Sms sms = new Sms();
				sms.setSmsNumber(number);
				sms.setSmsDateSched(schedDate);
				sms.setSmsUser(MainMenu.getUser());
				sms.setSmsText(part);
				sms.setModule("smsmanager");
				sms.setModuleID(null);
				
				smsList.add(sms);
			}
			
		} else {
			smsList.add(smsToSend);
		}
		smsOperations.saveOrUpdate(smsList);
	}
	
	public void delete(List<Sms> smsToDelete) throws OHServiceException {
		smsOperations.delete(smsToDelete);
	}

	public int getMAX_LENGHT() {
		return MAX_LENGHT;
	}

	public String getNUMBER_REGEX() {
		return NUMBER_REGEX;
	}
	
	private String[] split(String text) {
		int len = text.length();
		if (len <= MAX_LENGHT) {
			String[] messages = {text};
			return messages;
		}
		
		// Number of parts
	    int nParts = (len + MAX_LENGHT - 1) / MAX_LENGHT;
	    String parts[] = new String[nParts];

	    // Break into parts
	    int offset= 0;
	    int i = 0;
	    while (i < nParts)
	    {
	        parts[i] = text.substring(offset, Math.min(offset + MAX_LENGHT, len));
	        offset += MAX_LENGHT;
	        i++;
	    }
		return parts;
	}
}
