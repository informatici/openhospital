package org.isf.opd.service;


import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.isf.generaldata.MessageBundle;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class OpdIoOperationRepositoryImpl implements OpdIoOperationRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findAllOpdWhereParams(
			String diseaseTypeCode,
			String diseaseCode, 
			GregorianCalendar dateFrom,
			GregorianCalendar dateTo,
			int ageFrom, 
			int ageTo,
			char sex,
			char newPatient) {
		return this.entityManager.
				createNativeQuery(_getOpdQuery(
						diseaseTypeCode, diseaseCode, dateFrom, dateTo,
						ageFrom, ageTo, sex, newPatient)).
					getResultList();
	}	

		

	public String _getOpdQuery(
			String diseaseTypeCode,
			String diseaseCode, 
			GregorianCalendar dateFrom,
			GregorianCalendar dateTo,
			int ageFrom, 
			int ageTo,
			char sex,
			char newPatient)
	{	
		String query = "SELECT OPD_ID FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID LEFT JOIN DISEASE ON OPD_DIS_ID_A = DIS_ID_A LEFT JOIN DISEASETYPE ON DIS_DCL_ID_A = DCL_ID_A WHERE 1";
		if (!(diseaseTypeCode.equals(MessageBundle.getMessage("angal.opd.alltype")))) {
			query += " AND DIS_DCL_ID_A = \"" + diseaseTypeCode + "\"";
		}
		if(!diseaseCode.equals(MessageBundle.getMessage("angal.opd.alldisease"))) {
			query += " AND DIS_ID_A = \"" + diseaseCode + "\"";
		}
		if (ageFrom != 0 || ageTo != 0) {
			query += " AND OPD_AGE BETWEEN \"" + ageFrom + "\" AND \"" + ageTo + "\"";
		}
		if (sex != 'A') {
			query += " AND OPD_SEX =  \"" + sex + "\"";
		}
		if (newPatient != 'A') {
			query += " AND OPD_NEW_PAT =  \"" + newPatient + "\"";
		}
		query += " AND OPD_DATE_VIS BETWEEN  \"" + _convertToSQLDateLimited(dateFrom) + "\" AND \"" + _convertToSQLDateLimited(dateTo) + "\"";

		return query;
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