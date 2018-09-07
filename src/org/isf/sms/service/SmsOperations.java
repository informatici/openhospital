package org.isf.sms.service;

import java.util.ArrayList;

// Generated 31-gen-2014 15.39.04 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.List;

import org.isf.sms.model.Sms;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see org.isf.sms.model.Sms
 * @author Mwithi
 */
@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class SmsOperations {

	@Autowired
	private SmsIoOperationRepository repository;
	
	/**
	 * 
	 */
	public SmsOperations() {}
	
	/**
	 * Save or Update a {@link Sms}
	 * @param supplier - the {@link Sms} to save or update
	 * return <code>true</code> if data has been saved, <code>false</code> otherwise. 
	 * @throws OHException 
	 */
	public boolean saveOrUpdate(
			Sms sms) throws OHException 
	{
		boolean result = true;
	

		Sms savedSms = repository.save(sms);
		result = (savedSms != null);
		
		return result;
	}
	
	/**
	 * Returns a {@link Sms} with specified ID
	 * @param ID - sms ID
	 * @return sms - the sms with specified ID
	 * @throws OHException 
	 */
	public Sms getByID(
			int ID) throws OHException 
	{
		Sms foundSms = repository.findOne(ID);;
    	
		return foundSms;
	}
	
	/**
	 * Returns the list of all {@link Sms}s, sent and not sent, between the two dates
	 * @return smsList - the list of {@link Sms}s
	 * @throws OHException 
	 */
	public List<Sms> getAll(
			Date dateFrom, 
			Date dateTo) throws OHException 
	{		
		ArrayList<Sms> smsList = new ArrayList<Sms>(repository.findAllWhereBetweenDatesByOrderDate(dateFrom, dateTo));
		
		return smsList;
	}
	
	/**
	 * Returns the list of not sent {@link Sms}s between the two dates
	 * @return smsList - the list of {@link Sms}s
	 * @throws OHException 
	 */
	public List<Sms> getList(
			Date dateFrom, 
			Date dateTo) throws OHException 
	{
		ArrayList<Sms> smsList = new ArrayList<Sms>(repository.findAllWhereSentNotNullBetweenDatesByOrderDate(dateFrom, dateTo));
		
		return smsList;
	}
	
	/**
	 * Returns the list of not sent {@link Sms}s
	 * @return smsList - the list of {@link Sms}s
	 * @throws OHException 
	 */
	public List<Sms> getList() throws OHException 
	{
		ArrayList<Sms> smsList = new ArrayList<Sms>(repository.findAllWhereSentNotNullByOrderDate());
		
		return smsList;
	}

	/**
	 * Delete the specified {@link Sms}
	 * @param sms - the {@link Sms} to delete
	 * @throws OHException 
	 */
	public void delete(
			Sms sms) throws OHException 
	{		
		repository.delete(sms);
		
		return;	
	}

	/**
	 * Delete the specified {@link Sms}s if not already sent
	 * @param module - the module name which generated the {@link Sms}s
	 * @param moduleID - the module ID within its generated {@link Sms}s
	 * @throws OHException 
	 */
	public void deleteByModuleModuleID(
			String module, 
			String moduleID) throws OHException 
	{

		repository.deleteWhereModuleAndId(module, moduleID);	
		
        return;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the Sms code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			Integer code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
}
