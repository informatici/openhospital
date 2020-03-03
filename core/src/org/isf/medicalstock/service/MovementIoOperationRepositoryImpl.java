package org.isf.medicalstock.service;


import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.isf.medicalstock.service.MedicalStockIoOperations.MovementOrder;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class MovementIoOperationRepositoryImpl implements MovementIoOperationRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findtMovementWhereDatesAndId(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) {
		return this.entityManager.
				createNativeQuery(_getMovementWhereDatesAndId(wardId, dateFrom, dateTo)).
					getResultList();
	}	
	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findtMovementWhereData(
			Integer medicalCode,
			String medicalType, 
			String wardId, 
			String movType,
			GregorianCalendar movFrom, 
			GregorianCalendar movTo,
			GregorianCalendar lotPrepFrom, 
			GregorianCalendar lotPrepTo,
			GregorianCalendar lotDueFrom, 
			GregorianCalendar lotDueTo) {
		return this.entityManager.
				createNativeQuery(_getMovementWhereData(
						medicalCode, medicalType, wardId, 
						movType, movFrom, movTo,
						lotPrepFrom, lotPrepTo, lotDueFrom, lotDueTo)).
					getResultList();
	}		

	@SuppressWarnings("unchecked")	
	@Override
	public List<Integer> findtMovementForPrint(
			String medicalDescription,
			String medicalTypeCode, 
			String wardId, 
			String movType,
			GregorianCalendar movFrom, 
			GregorianCalendar movTo, 
			String lotCode,
			MovementOrder order) {
		return this.entityManager.
				createNativeQuery(_getMovementForPrint(
						medicalDescription, medicalTypeCode, wardId, 
						movType, movFrom, movTo,
						lotCode, order)).
					getResultList();
	}	

		
	private String _getMovementWhereDatesAndId(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo)
	{	
		String query = "SELECT MMV_ID FROM (" + 
				"(MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
				"JOIN (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID ) " +
				"LEFT JOIN MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A " +
				"LEFT JOIN WARD ON MMV_WRD_ID_A = WRD_ID_A " +
				"LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID ";
		boolean dateQuery = false;
		
		
		if ((dateFrom != null) && (dateTo != null)) 
		{
			query += "WHERE DATE(MMV_DATE) BETWEEN DATE(\"" + _convertToSQLDateLimited(dateFrom) + "\") and DATE(\"" + _convertToSQLDateLimited(dateTo) + "\") ";
			dateQuery = true;
		}
		if (wardId != null && !wardId.equals("")) 
		{
			if (dateQuery) 
			{
				query += "AND ";
			}
			else 
			{
				query += "WHERE ";
			}
			query += "WRD_ID_A = \"" + wardId + "\" ";
		}
		query += "ORDER BY MMV_DATE DESC, MMV_REFNO DESC";	

		return query;
	}
	
	private String _getMovementWhereData(
			Integer medicalCode,
			String medicalType, 
			String wardId, 
			String movType,
			GregorianCalendar movFrom, 
			GregorianCalendar movTo,
			GregorianCalendar lotPrepFrom, 
			GregorianCalendar lotPrepTo,
			GregorianCalendar lotDueFrom, 
			GregorianCalendar lotDueTo) {
		String query = "";
		boolean paramQuery = false;
				
		
		if (lotPrepFrom != null || lotDueFrom != null) 
		{
			query = "select MMV_ID from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) "
					+ "join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID )"
					+ " left join WARD on MMV_WRD_ID_A=WRD_ID_A "
					+ " join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A "
					+ " LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID "
					+ " where ";
		} 
		else 
		{
			query = "select MMV_ID from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) "
					+ "join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID )"
					+ " left join WARD on MMV_WRD_ID_A=WRD_ID_A "
					+ " left join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A "
					+ " LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID "
					+ " where ";
		}
		if ((medicalCode != null) || (medicalType != null)) 
		{
			if (medicalCode == null) 
			{
				query += "(MDSR_MDSRT_ID_A=\"" + medicalType + "\") ";
				paramQuery = true;
			} else if (medicalType == null)
			{
				query += "(MDSR_ID=\"" + medicalCode + "\") ";
				paramQuery = true;
			}
		}
		if ((movFrom != null) && (movTo != null)) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(DATE(MMV_DATE) between DATE(\"" + _convertToSQLDateLimited(movFrom) + "\") and DATE(\"" + _convertToSQLDateLimited(movTo) + "\")) ";
			paramQuery = true;
		}
		if ((lotPrepFrom != null) && (lotPrepTo != null)) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(DATE(LT_PREP_DATE) between DATE(\"" + _convertToSQLDateLimited(lotPrepFrom) + "\") and DATE(\"" + _convertToSQLDateLimited(lotPrepTo) + "\")) ";
			paramQuery = true;
		}
		if ((lotDueFrom != null) && (lotDueTo != null)) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(DATE(LT_DUE_DATE) between DATE(\"" + _convertToSQLDateLimited(lotDueFrom) + "\") and DATE(\"" + _convertToSQLDateLimited(lotDueTo) + "\")) ";
			paramQuery = true;
		}
		if (movType != null) {
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(MMVT_ID_A=\"" + movType + "\") ";
			paramQuery = true;
		}
		if (wardId != null) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(WRD_ID_A=\"" + wardId + "\") ";
			paramQuery = true;
		}
		query += " ORDER BY MMV_DATE DESC, MMV_REFNO DESC";
		
		return query;
	}	
	
	private String _getMovementForPrint(
			String medicalDescription,
			String medicalTypeCode, 
			String wardId, 
			String movType,
			GregorianCalendar movFrom, 
			GregorianCalendar movTo, 
			String lotCode,
			MovementOrder order) {
		String query = "";
		boolean paramQuery = false;
		
		
		query = "select MMV_ID from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
				"join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID) " +
				"left join WARD on MMV_WRD_ID_A=WRD_ID_A " +
				"left join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A " +
				"LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID " +
				"where ";

		if ((medicalDescription != null) || (medicalTypeCode != null)) 
		{
			if (medicalDescription == null) 
			{
				query += "(MDSR_MDSRT_ID_A = \"" + medicalTypeCode + "\") ";
				paramQuery = true;
			} 
			else if (medicalTypeCode == null) 
			{
				query += "(MDSR_DESC like \"%" + medicalDescription + "%\") ";
				paramQuery = true;
			}
		}
		if (lotCode != null) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(LT_ID_A like \"%" + lotCode + "%\") ";
			paramQuery = true;
		}
		if ((movFrom != null) && (movTo != null)) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(DATE(MMV_DATE) between DATE(\"" + _convertToSQLDateLimited(movFrom) + "\") and DATE(\"" + _convertToSQLDateLimited(movTo) + "\")) ";
			paramQuery = true;
		}		
		if (movType != null) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(MMVT_ID_A=\"" + movType + "\") ";
			paramQuery = true;
		}
		if (wardId != null) 
		{
			if (paramQuery) 
			{
				query += "and ";
			}
			query += "(WRD_ID_A=\"" + wardId + "\") ";
			paramQuery = true;
		}
		switch (order) {
			case DATE:
				query += " ORDER BY MMV_DATE DESC, MMV_REFNO DESC";
				break;
			case WARD:
				query += " order by MMV_REFNO DESC, WRD_NAME desc";
				break;
			case PHARMACEUTICAL_TYPE:
				query += " order by MMV_REFNO DESC, MDSR_MDSRT_ID_A,MDSR_DESC";
				break;
			case TYPE:
				query += " order by MMV_REFNO DESC, MMVT_DESC";
				break;
		}
		
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