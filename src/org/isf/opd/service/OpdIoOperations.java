package org.isf.opd.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.isf.generaldata.MessageBundle;
import org.isf.opd.model.Opd;
import org.isf.utils.db.TranslateOHException;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*----------------------------------------------------
 * (org.isf.opd.service)IoOperations - services for opd class
 * ---------------------------------------------------
 * modification history
 * 11/12/2005 - Vero, Rick  - first beta version 
 * 03/01/2008 - ross - selection for opd browser is performed on OPD_DATE_VIS instead of OPD_DATE
 *                   - selection now is less than or equal, before was only less than
 * 21/06/2008 - ross - for multilanguage version, the test for "all type" and "all disease"
 *                     must be done on the translated resource, not in english
 *                   - fix:  getSurgery() method should not add 1 day to toDate
 * 05/09/2008 - alex - added method for patient related OPD query
 * 05/01/2009 - ross - fix: in insert, referralfrom was written both in referralfrom and referralto
 * 09/01/2009 - fabrizio - Modified queried to accomodate type change of date field in Opd class.
 *                         Modified construction of queries, concatenation is performed with
 *                         StringBuilders instead than operator +. Removed some nested try-catch
 *                         blocks. Modified methods to format dates.                          
 *------------------------------------------*/

@Component
@Transactional(rollbackFor=OHException.class)
@TranslateOHException
public class OpdIoOperations {

	@Autowired
	private OpdIoOperationRepository repository;
	
	/**
	 * return all OPDs of today or one week ago
	 * 
	 * @param oneWeek - if <code>true</code> return the last week, only today otherwise.
	 * @return the list of OPDs. It could be <code>empty</code>.
	 * @throws OHException 
	 */
	public ArrayList<Opd> getOpdList(
			boolean oneWeek) throws OHException
	{
		GregorianCalendar dateFrom=new GregorianCalendar();
		GregorianCalendar dateTo=new GregorianCalendar();
		
		
		if (oneWeek) 
		{
			dateFrom.add(GregorianCalendar.WEEK_OF_YEAR,-1);
		}
		
		return getOpdList(MessageBundle.getMessage("angal.opd.alltype"),MessageBundle.getMessage("angal.opd.alldisease"),dateFrom,dateTo,0,0,'A','A');
	}
	
	/**
	 * 
	 * return all OPDs within specified dates
	 * 
	 * @param diseaseTypeCode
	 * @param diseaseCode
	 * @param dateFrom
	 * @param dateTo
	 * @param ageFrom
	 * @param ageTo
	 * @param sex
	 * @param newPatient
	 * @return the list of OPDs. It could be <code>empty</code>.
	 * @throws OHException 
	 */
	public ArrayList<Opd> getOpdList(
			String diseaseTypeCode,
			String diseaseCode, 
			GregorianCalendar dateFrom,
			GregorianCalendar dateTo,
			int ageFrom, 
			int ageTo,
			char sex,
			char newPatient) throws OHException
	{
		ArrayList<Integer> pOpdCode = null;
		ArrayList<Opd> pOpd = new ArrayList<Opd>();
		
		
		pOpdCode = new ArrayList<Integer>(repository.findAllOpdWhereParams(
				diseaseTypeCode, diseaseCode, dateFrom, dateTo,
				ageFrom, ageTo, sex, newPatient));			
		for (int i=0; i<pOpdCode.size(); i++)
		{
			Integer code = pOpdCode.get(i);
			Opd opd = repository.findOne(code);
			
			
			pOpd.add(i, opd);
		}
		
		return pOpd;	
	}
	
	/**
	 * returns all {@link Opd}s associated to specified patient ID
	 * 
	 * @param patID - the patient ID
	 * @return the list of {@link Opd}s associated to specified patient ID.
	 * 		   the whole list of {@link Opd}s if <code>0</code> is passed.
	 * @throws OHException 
	 */
	public ArrayList<Opd> getOpdList(
			int patID) throws OHException 
	{
		ArrayList<Opd> opdList = null;
		
		
		if (patID == 0) {
			opdList = (ArrayList<Opd>) repository.findAllByOrderByProgYearDesc();
		} else {
			opdList = (ArrayList<Opd>) repository.findAllWherePatIdByOrderByProgYearDesc(patID);
		}
		
		return opdList;
	}
		
	/**
	 * insert a new item in the db
	 * 
	 * @param an {@link OPD}
	 * @return <code>true</code> if the item has been inserted
	 * @throws OHException 
	 */
	public boolean newOpd(
			Opd opd) throws OHException
	{
		boolean result = true;
	

		Opd savedOpd = repository.save(opd);
		result = (savedOpd != null);
		
		return result;	
	}
	
	/**
	 * modify an {@link OPD} in the db
	 * 
	 * @param an {@link OPD}
	 * @return <code>true</code> if the item has been updated. <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateOpd(
			Opd opd) throws OHException 
	{
		boolean result = true;
	

		Opd savedOpd = repository.save(opd);
		result = (savedOpd != null);
		
		return result;
	}
	
	/**
	 * delete an {@link OPD} from the db
	 * 
	 * @param opd - the {@link OPD} to delete
	 * @return <code>true</code> if the item has been deleted. <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteOpd(
			Opd opd) throws OHException 
	{
		boolean result = true;
	
		
		repository.delete(opd);
		
		return result;	
	}
	
	/**
	 * Returns the max progressive number within specified year or within current year if <code>0</code>.
	 * 
	 * @param year
	 * @return <code>int</code> - the progressive number in the year
	 * @throws OHException 
	 */
	public int getProgYear(
			int year) throws OHException 
	{
		Integer progYear = 0;
			
		
		if (year == 0) {
			progYear = repository.findMaxProgYear();
		} else {
			progYear = repository.findMaxProgYearWhereDate(year);
		}
				
		return progYear == null ? new Integer(0) : progYear;
	}
	
	/**
	 * return the last Opd in time associated with specified patient ID. 
	 * 
	 * @param patID - the patient ID
	 * @return last Opd associated with specified patient ID or <code>null</code>
	 * @throws OHException 
	 */
	public Opd getLastOpd(
			int patID) throws OHException 
	{
		ArrayList<Opd> opdList = null;
		
		
		opdList = (ArrayList<Opd>) repository.findAllWherePatIdByOrderByDateDescLimit1(patID);
		
		return opdList.get(0);
	}	

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the opd code
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
