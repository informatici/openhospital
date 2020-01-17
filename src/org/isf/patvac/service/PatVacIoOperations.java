package org.isf.patvac.service;

/*------------------------------------------
 * IoOperations  - Patient Vaccine Io operations
 * -----------------------------------------
 * modification history
 * 25/08/2011 - claudia - first beta version
 * 20/10/2011 - insert vaccine type management
 * 14/11/2011 - claudia - inserted search condtion on date
 *------------------------------------------*/
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.isf.patvac.model.PatientVaccine;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class PatVacIoOperations {

	@Autowired
	private PatVacIoOperationRepository repository;
	
	/**
	 * returns all {@link PatientVaccine}s of today or one week ago
	 * 
	 * @param minusOneWeek - if <code>true</code> return the last week
	 * @return the list of {@link PatientVaccine}s
	 * @throws OHServiceException 
	 */
	public ArrayList<PatientVaccine> getPatientVaccine(
			boolean minusOneWeek) throws OHServiceException 
	{
		GregorianCalendar timeTo = new GregorianCalendar();
		GregorianCalendar timeFrom = new GregorianCalendar();
	
		
		if (minusOneWeek)
		{
			timeFrom.add(GregorianCalendar.WEEK_OF_YEAR, -1);			
		}
		
		return getPatientVaccine(null, null, timeFrom, timeTo, 'A', 0, 0);
	}

	/**
	 * returns all {@link PatientVaccine}s within <code>dateFrom</code> and
	 * <code>dateTo</code>
	 * 
	 * @param vaccineTypeCode
	 * @param vaccineCode
	 * @param dateFrom
	 * @param dateTo
	 * @param sex
	 * @param ageFrom
	 * @param ageTo
	 * @return the list of {@link PatientVaccine}s
	 * @throws OHServiceException 
	 */
	public ArrayList<PatientVaccine> getPatientVaccine(
			String vaccineTypeCode, 
			String vaccineCode, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo, 
			char sex, 
			int ageFrom, 
			int ageTo) throws OHServiceException 
	{
		ArrayList<Integer> pPatientVaccineCode = null;
		ArrayList<PatientVaccine> pPatientVaccine = new ArrayList<PatientVaccine>();
		
		
		pPatientVaccineCode = (ArrayList<Integer>) repository.findAllByCodesAndDatesAndSexAndAges(
				vaccineTypeCode, vaccineCode, dateFrom, dateTo, sex, ageFrom, ageTo);
		for (int i=0; i<pPatientVaccineCode.size(); i++)
		{
			Integer code = pPatientVaccineCode.get(i);
			PatientVaccine patientVaccine = repository.findOne(code);
			
			
			pPatientVaccine.add(i, patientVaccine);
		}

		return pPatientVaccine;
	}

	/**
	 * inserts a {@link PatientVaccine} in the DB
	 * 
	 * @param patVac - the {@link PatientVaccine} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise 
	 * @throws OHServiceException 
	 */
	public boolean newPatientVaccine(
			PatientVaccine patVac) throws OHServiceException 
	{
		boolean result = true;
	

		PatientVaccine savedVaccine = repository.save(patVac);
		result = (savedVaccine != null);
		
		return result;
	}

	/**
	 * updates a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise 
	 * @throws OHServiceException 
	 */
	public boolean updatePatientVaccine(
			PatientVaccine patVac) throws OHServiceException 
	{
		boolean result = true;
	

		PatientVaccine savedVaccine = repository.save(patVac);
		result = (savedVaccine != null);
		
		return result;
	}

	/**
	 * deletes a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise 
	 * @throws OHServiceException 
	 */
	public boolean deletePatientVaccine(
			PatientVaccine patVac) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(patVac);
		
		return result;	
	}

	/**
	 * Returns the max progressive number within specified year or within current year if <code>0</code>.
	 * 
	 * @param year
	 * @return <code>int</code> - the progressive number in the year
	 * @throws OHServiceException 
	 */
	public int getProgYear(
			int year) throws OHServiceException 
	{
		Integer progYear = 0;

		
		if (year != 0) 
		{
			progYear = repository.findMaxCodeWhereVaccineDate(year);
		}
		else
		{
			progYear = repository.findMaxCode();
		}
		
		return progYear == null ? new Integer(0) : progYear;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the patient vaccine code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHServiceException 
	 */
	public boolean isCodePresent(
			Integer code) throws OHServiceException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;	
	}
}
