package org.isf.lab.service;

/*------------------------------------------
 * lab.service.IoOperations - laboratory exam database io operations
 * -----------------------------------------
 * modification history
 * 02/03/2006 - theo - first beta version
 * 10/11/2006 - ross - added editing capability. 
 * 					   new fields data esame, sex, age, material, inout flag added
 * 21/06/2008 - ross - do not add 1 to toDate!. 
 *                     the selection date switched to exam date, 
 * 04/01/2009 - ross - do not use roll, use add(week,-1)!
 *                     roll does not change the year!
 * 16/11/2012 - mwithi - added logging capability
 * 					   - to do lock management
 * 04/02/2013 - mwithi - lock management done
 *------------------------------------------*/


import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.model.LaboratoryRow;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class LabIoOperations {

	/**
	 * Return a list of results ({@link LaboratoryRow}s) for passed lab entry.
	 * @param code - the {@link Laboratory} record ID.
	 * @return the list of {@link LaboratoryRow}s. It could be <code>empty</code>
	 * @throws OHException
	 */
	public ArrayList<LaboratoryRow> getLabRow(
			Integer code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<LaboratoryRow> laboratoryRows = new ArrayList<LaboratoryRow>();
		LaboratoryRow laboratoryRow = null;
				
		try {
			jpa.beginTransaction();

			laboratoryRow = (LaboratoryRow) jpa.find(LaboratoryRow.class, code);
			laboratoryRows.add(laboratoryRow);

			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return laboratoryRows;
	}

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<Laboratory> getLaboratory(String aCode) {
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		// 04/1/2009 ross: no roll, use add!!
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratory(aCode, time1, time2);
	}*/
	
	/**
	 * Return the whole list of exams ({@link Laboratory}s) within last year.
	 * @return the list of {@link Laboratory}s 
	 * @throws OHException
	 */
	public ArrayList<Laboratory> getLaboratory() throws OHException 
	{
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		// 04/1/2009 ross: no roll, use add!!
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratory(null, time1, time2);
	}

	/**
	 * Return a list of exams ({@link Laboratory}s) between specified dates and matching passed exam name
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link Laboratory}s 
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Laboratory> getLaboratory(
			String exam,	
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Laboratory> laboritories = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();

			String query = "SELECT * FROM LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A";
			query += " WHERE LAB_EXAM_DATE >= ? AND LAB_EXAM_DATE <= ?";
			params.add(dateFrom);
			params.add(dateTo);
			if (exam != null) {
				query += " AND EXA_DESC = ?";
				params.add(exam);
			}
			query += " ORDER BY LAB_EXAM_DATE";
			jpa.createQuery(query, Laboratory.class, false);
			jpa.setParameters(params, false);
			List<Laboratory> laboratoryList = (List<Laboratory>) jpa.getList();
			laboritories = new ArrayList<Laboratory>(laboratoryList);

			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return laboritories;
	}
	
	/**
	 * Return a list of exams ({@link Laboratory}s) related to a {@link Patient}.
	 * @param aPatient - the {@link Patient}.
	 * @return the list of {@link Laboratory}s related to the {@link Patient}.
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Laboratory> getLaboratory(
			Patient aPatient) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Laboratory> laboritories = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();

			String query = "SELECT * FROM (LABORATORY JOIN EXAM ON LAB_EXA_ID_A=EXA_ID_A)"
					+ " LEFT JOIN LABORATORYROW ON LABR_LAB_ID = LAB_ID WHERE LAB_PAT_ID = ? "
					+ " ORDER BY LAB_DATE, LAB_ID";
			params.add(aPatient.getCode());
			jpa.createQuery(query, Laboratory.class, false);
			jpa.setParameters(params, false);
			List<Laboratory> laboratoryList = (List<Laboratory>) jpa.getList();
			laboritories = new ArrayList<Laboratory>(laboratoryList);

			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return laboritories;
	}
	
	/**
	 * Return a list of exams suitable for printing ({@link LaboratoryForPrint}s) 
	 * within last year
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link LaboratoryForPrint}s 
	 * @throws OHException
	 */
	public ArrayList<LaboratoryForPrint> getLaboratoryForPrint() throws OHException 
	{
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratoryForPrint(null, time1, time2);
	}

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(String exam,	String result) {
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratoryForPrint(exam, time1, time2);
	}*/
	
	/**
	 * Return a list of exams suitable for printing ({@link LaboratoryForPrint}s) 
	 * between specified dates and matching passed exam name
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link LaboratoryForPrint}s 
	 * @throws OHException
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(
			String exam, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Laboratory> laboritories = null;
		ArrayList<LaboratoryForPrint> pLaboratory = new ArrayList<LaboratoryForPrint>();
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();

			String query = "SELECT * FROM (LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A)"
					+ " JOIN EXAMTYPE ON EXC_ID_A = EXA_EXC_ID_A" + " WHERE LAB_DATE >= ? AND LAB_DATE <= ?";
			params.add(dateFrom);
			params.add(dateTo);
			if (exam != null) {
				query += " AND EXA_DESC LIKE ?";
				params.add('%' + exam + '%');
			}
			query += " ORDER BY EXC_DESC";
			jpa.createQuery(query, Laboratory.class, false);
			jpa.setParameters(params, false);
			List<Laboratory> laboratoryList = (List<Laboratory>) jpa.getList();
			laboritories = new ArrayList<Laboratory>(laboratoryList);

			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		
		for (Laboratory laboratory : laboritories) 
		{
			pLaboratory.add(new LaboratoryForPrint(
					laboratory.getCode(),
					laboratory.getExam(),
					laboratory.getDate(),
					laboratory.getResult()
				)
			);
		}

		return pLaboratory;	
	}
	
	/**
	 * Insert a Laboratory exam {@link Laboratory} and return generated key. No commit is performed.
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param dbQuery - the connection manager
	 * @return the generated key
	 * @throws OHException
	 */
	private Integer newLaboratory(
			Laboratory laboratory) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		
		try {
			jpa.beginTransaction();
			jpa.persist(laboratory);
			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return laboratory.getCode();	
	}
	
	/**
	 * Inserts one Laboratory exam {@link Laboratory} (Procedure One)
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param dbQuery - the connection manager
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean newLabFirstProcedure(
			Laboratory laboratory) throws OHException 
	{
		boolean result = false;
		
		
		int newCode = newLaboratory(laboratory);
		if (newCode > 0)
		{
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Inserts one Laboratory exam {@link Laboratory} with multiple results (Procedure Two) 
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param labRow - the list of results ({@link String}s)
	 * @return <code>true</code> if the exam has been inserted with all its results, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean newLabSecondProcedure(
			Laboratory laboratory, 
			ArrayList<String> labRow) throws OHException 
	{
		boolean result = false;
		DbJpaUtil jpa = new DbJpaUtil(); 
		
		
		int newCode = newLaboratory(laboratory);
		if (newCode > 0) 
		{
			try {
				jpa.beginTransaction();
				LaboratoryRow laboratoryRow = new LaboratoryRow();
				laboratoryRow.setLabId(laboratory);
				laboratoryRow.setDescription(labRow.get(0));
				jpa.persist(laboratoryRow);
				jpa.commitTransaction();
			} catch (OHException e) {
				// DbJpaUtil managed exception
				jpa.rollbackTransaction();
				throw e;
			}

			result = true;
		}
		
		return result;
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory}. No commit is performed.
	 * @param laboratory - the {@link Laboratory} to update
	 * @param dbQuery - the connection manager
	 * @return <code>true</code> if the exam has been updated with all its results, <code>false</code> otherwise
	 * @throws OHException
	 */
	private boolean updateLaboratory(
			Laboratory laboratory) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try {
			jpa.beginTransaction();
			jpa.merge(laboratory);
			jpa.commitTransaction();
		} catch (OHException e) {
			// DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure One).
	 * If old exam was Procedure Two all its releated result are deleted.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean updateLabFirstProcedure(
			Laboratory laboratory) throws OHException 
	{
		boolean result = updateLaboratory(laboratory);
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		
		try {
			jpa.beginTransaction();
			jpa.createQuery("DELETE FROM LABORATORYROW WHERE LABR_LAB_ID = ?", LaboratoryRow.class, false);
			params.add(laboratory.getCode());
			jpa.setParameters(params, false);
			jpa.executeUpdate();
			jpa.commitTransaction();
		}  catch (OHException e) {
			result = false;
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		return result;
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure Two).
	 * Previous results are deleted and replaced with new ones.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated with all its results, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean editLabSecondProcedure(
			Laboratory laboratory, 
			ArrayList<String> labRow) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = updateLabFirstProcedure(laboratory);
		
		
		if (result == true)
		{		
			try {
				jpa.beginTransaction();
				LaboratoryRow laboratoryRow = new LaboratoryRow();
				laboratoryRow.setLabId(laboratory);
				laboratoryRow.setDescription(labRow.get(0));
				jpa.persist(laboratoryRow);
				jpa.commitTransaction();
			} catch (OHException e) {
				// DbJpaUtil managed exception
				jpa.rollbackTransaction();
				throw e;
			}
		}
		
		return result;
	}

	/**
	 * Delete a Laboratory exam {@link Laboratory} (Procedure One or Two).
	 * Previous results, if any, are deleted as well.
	 * @param laboratory - the {@link Laboratory} to delete
	 * @return <code>true</code> if the exam has been deleted with all its results, if any. <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean deleteLaboratory(
			Laboratory aLaboratory) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		jpa.beginTransaction();
		
		Laboratory objToRemove = (Laboratory) jpa.find(Laboratory.class, aLaboratory.getCode());
		
		try {
			if (objToRemove.getExam().getProcedure() == 2) 
			{
				try {
					jpa.createQuery("DELETE FROM LABORATORYROW WHERE LABR_LAB_ID = ?", LaboratoryRow.class, false);
					params.add(objToRemove.getCode());
					jpa.setParameters(params, false);
					jpa.executeUpdate();
				}  catch (OHException e) {
					//DbJpaUtil managed exception
					jpa.rollbackTransaction();
					result = false;
					throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
				}
			}
			jpa.remove(objToRemove);
	    	jpa.commitTransaction();
	    	
		}  catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		
		return result;
	}
}
