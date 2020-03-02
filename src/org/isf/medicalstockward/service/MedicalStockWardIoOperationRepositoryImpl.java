package org.isf.medicalstockward.service;


import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;


@Transactional
public class MedicalStockWardIoOperationRepositoryImpl implements MedicalStockWardIoOperationRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findAllWardMovement(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) {
		return this.entityManager.
				createNativeQuery(_getWardMovementQuery(
						wardId, dateFrom, dateTo)).
					getResultList();
	}	
		

	public String _getWardMovementQuery(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo)
	{	
		StringBuilder query = new StringBuilder();
		boolean firstParam = true;
		
		
		query.append("SELECT MMVN_ID FROM ((((MEDICALDSRSTOCKMOVWARD LEFT JOIN " +
						"(PATIENT LEFT JOIN (SELECT PEX_PAT_ID, PEX_HEIGHT AS PAT_HEIGHT, PEX_WEIGHT AS PAT_WEIGHT FROM PATIENTEXAMINATION GROUP BY PEX_PAT_ID ORDER BY PEX_DATE DESC) AS HW ON PAT_ID = HW.PEX_PAT_ID) ON MMVN_PAT_ID = PAT_ID) JOIN " +
						"WARD ON MMVN_WRD_ID_A = WRD_ID_A)) JOIN " +
						"MEDICALDSR ON MMVN_MDSR_ID = MDSR_ID) JOIN " +
						"MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A ");
		if (wardId!=null || dateFrom!=null || dateTo!=null) 
		{
			query.append("WHERE ");
		}
		if (wardId != null && !wardId.equals("")) 
		{
			firstParam = false;
			query.append("WRD_ID_A = \"" + wardId + "\" ");
		}
		if ((dateFrom != null) && (dateTo != null)) 
		{
			if (!firstParam)
			{
				query.append("AND ");
			}
			query.append("DATE(MMVN_DATE) BETWEEN DATE(\"" + _convertToSQLDateLimited(dateFrom) + "\") and DATE(\"" + _convertToSQLDateLimited(dateTo) + "\") ");
		}
		query.append(" ORDER BY MMVN_DATE ASC");
		
		String result = query.toString();

		return result;
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