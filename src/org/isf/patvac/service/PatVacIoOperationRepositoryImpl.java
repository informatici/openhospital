package org.isf.patvac.service;


import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;


@Transactional
public class PatVacIoOperationRepositoryImpl implements PatVacIoOperationRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findAllByCodesAndDatesAndSexAndAges(
			String vaccineTypeCode, 
			String vaccineCode, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo, 
			char sex, 
			int ageFrom, 
			int ageTo) {
		return this.entityManager.
				createNativeQuery(_getPatientVaccineQuery(
						vaccineTypeCode, vaccineCode, dateFrom, dateTo,
						sex, ageFrom, ageTo)).
					getResultList();
	}	

	
	private String _getPatientVaccineQuery(
			String vaccineTypeCode, 
			String vaccineCode, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo, 
			char sex, 
			int ageFrom, 
			int ageTo) 
	{
		StringBuilder query = new StringBuilder();
		String clause = " WHERE";
	
		
		query.append("SELECT PAV_ID"
				+ " FROM PATIENTVACCINE JOIN VACCINE ON PAV_VAC_ID_A=VAC_ID_A"
				+ " JOIN VACCINETYPE ON VAC_VACT_ID_A = VACT_ID_A"
				+ " JOIN PATIENT ON PAV_PAT_ID = PAT_ID");
		if (dateFrom != null || dateTo != null) {
			if (dateFrom != null) {
				query.append(clause).append(" DATE_FORMAT(PAV_DATE,'%Y-%m-%d') >= \"" + _convertToSQLDateLimited(dateFrom) + "\"");
				clause = " AND";
			}
			if (dateTo != null) {
				query.append(clause).append(" DATE_FORMAT(PAV_DATE,'%Y-%m-%d') <= \"" + _convertToSQLDateLimited(dateTo) + "\"");
				clause = " AND";
			}
		}
		if (vaccineTypeCode != null) {
			query.append(clause).append(" VACT_ID_A = \"" + vaccineTypeCode + "\"");
			clause = " AND";
		}
		if (vaccineCode != null) {
			query.append(clause).append(" VAC_ID_A = \"" + vaccineCode + "\"");
			clause = " AND";
		}
		if (sex != 'A') {
			query.append(clause).append(" PAT_SEX = \"" + sex + "\"");
			clause = " AND";
		}		
		if (ageFrom != 0 || ageTo != 0) {
			query.append(clause).append(" PAT_AGE BETWEEN \"" + ageFrom + "\" AND \"" + ageTo + "\"");
			clause = " AND";
		}		
		query.append(" ORDER BY PAV_DATE DESC, PAV_ID");
		System.out.println(query.toString());

		return query.toString();
	}
	
	/**
	 * return a String representing the date in format <code>yyyy-MM-dd</code>
	 * 
	 * @param date
	 * @return the date in format <code>yyyy-MM-dd</code>
	 */
	private String _convertToSQLDateLimited(GregorianCalendar date) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
		return sdf.format(date.getTime());
	}
}