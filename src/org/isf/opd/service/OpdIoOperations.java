package org.isf.opd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.NoResultException;

import org.isf.generaldata.MessageBundle;
import org.isf.opd.model.Opd;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

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
public class OpdIoOperations {
	
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
	@SuppressWarnings("unchecked")
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Opd> opds = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try{
			jpa.beginTransaction();

			String query = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID LEFT JOIN DISEASE ON OPD_DIS_ID_A = DIS_ID_A LEFT JOIN DISEASETYPE ON DIS_DCL_ID_A = DCL_ID_A WHERE 1";
			if (!(diseaseTypeCode.equals(MessageBundle.getMessage("angal.opd.alltype")))) {
				query += " AND DIS_DCL_ID_A = ?";
				params.add(diseaseTypeCode);
			}
			if(!diseaseCode.equals(MessageBundle.getMessage("angal.opd.alldisease"))) {
				query += " AND DIS_ID_A = ?";
				params.add(diseaseCode);
			}
			if (ageFrom != 0 || ageTo != 0) {
				query += " AND OPD_AGE BETWEEN ? AND ?";
				params.add(ageFrom);
				params.add(ageTo);
			}
			if (sex != 'A') {
				query += " AND OPD_SEX = ?";
				params.add(String.valueOf(sex));
			}
			if (newPatient != 'A') {
				query += " AND OPD_NEW_PAT = ?";
				params.add(newPatient);
			}
			query += " AND OPD_DATE_VIS BETWEEN DATE(?) AND DATE(?)";
			params.add(dateFrom);
			params.add(dateTo);
			query += " ORDER BY OPD_DATE_VIS";
			jpa.createQuery(query, Opd.class, false);
			jpa.setParameters(params, false);	
			List<Opd> opdList = (List<Opd>)jpa.getList();
			opds = new ArrayList<Opd>(opdList);			

			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
		return opds;
	}
	
	/**
	 * returns all {@link Opd}s associated to specified patient ID
	 * 
	 * @param patID - the patient ID
	 * @return the list of {@link Opd}s associated to specified patient ID.
	 * 		   the whole list of {@link Opd}s if <code>0</code> is passed.
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Opd> getOpdList(
			int patID) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Opd> opds = null;
		ArrayList<Object> params = new ArrayList<Object>();
		String query = "";
				
		try{
			jpa.beginTransaction();		

			if (patID == 0) {
				query = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID ORDER BY OPD_PROG_YEAR DESC";
			} else {
				query = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID WHERE OPD_PAT_ID = ? ORDER BY OPD_PROG_YEAR DESC";
				params.add(patID);
			}
			jpa.createQuery(query, Opd.class, false);
			jpa.setParameters(params, false);	
			List<Opd> opdList = (List<Opd>)jpa.getList();
			opds = new ArrayList<Opd>(opdList);			

			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
		return opds;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		opd.setDate(new Date());
		try{
			jpa.beginTransaction();	
			jpa.persist(opd);
			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
		return result;	
	}
	
	/**
	 * Checks if the specified {@link Opd} has been modified.
	 * @param opd - the {@link Opd} to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean hasOpdModified(
			Opd opd) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Opd foundOpd;
		boolean result = false;
		
		try{
			jpa.beginTransaction();	
			foundOpd = (Opd)jpa.find(Opd.class, opd.getCode());
			result = foundOpd.getLock() != opd.getLock();
			jpa.commitTransaction(); 
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		opd.setLock(opd.getLock()+1);
		try{
			jpa.beginTransaction();	
			jpa.merge(opd);
			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try{
			jpa.beginTransaction();	
			jpa.remove(opd);
			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		} 
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
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		Integer progYear = 0;
				
		try{
			jpa.beginTransaction();		

			query = "SELECT MAX(OPD_PROG_YEAR) FROM OPD";
			if (year != 0) {
				query += " WHERE YEAR(OPD_DATE) = ?";
				params.add(year);
			}
			jpa.createQuery(query, null, false);
			jpa.setParameters(params, false);	
			progYear = (Integer)jpa.getResult();
			jpa.commitTransaction();
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		String query = "";
				
		
		Opd opd;
		try {
			jpa.beginTransaction();		
			
			query = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID WHERE OPD_PAT_ID = ? ORDER BY OPD_DATE DESC LIMIT 1";
			jpa.createQuery(query, Opd.class, false);
			params.add(patID);
			jpa.setParameters(params, false);	
			opd = (Opd)jpa.getResult();			
			jpa.commitTransaction();

		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			if (e.getCause().getClass().equals(NoResultException.class))
				return null;
			else throw e;
		} 
		
		return opd;
	}	
}
