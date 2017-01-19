package org.isf.medicalstockward.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NoResultException;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.springframework.stereotype.Component;

/**
 * @author mwithi
 */
@Component
public class MedicalStockWardIoOperations 
{
	/**
	 * Get all {@link MovementWard}s with the specified criteria.
	 * @param wardId the ward id.
	 * @param dateFrom the lower bound for the movement date range.
	 * @param dateTo the upper bound for the movement date range.
	 * @return the retrieved movements.
	 * @throws OHException if an error occurs retrieving the movements.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<MovementWard> getWardMovements(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHException 
	{

		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<MovementWard> movements = null;
				
		
		jpa.beginTransaction();
		
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT * FROM ((((MEDICALDSRSTOCKMOVWARD LEFT JOIN " +
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
			if (params.size() != 0) 
			{
				query.append("AND ");
			}
			query.append("WRD_ID_A = ? ");
			params.add(wardId);
		}
		if ((dateFrom != null) && (dateTo != null)) 
		{
			if (params.size() != 0) 
			{
				query.append("AND ");
			}
			query.append("MMVN_DATE > ? AND MMVN_DATE < ?");
			params.add(dateFrom);
			params.add(dateTo);
		}
		
		query.append(" ORDER BY MMVN_DATE ASC");
				
		jpa.createQuery(query.toString(), MovementWard.class, false);
		jpa.setParameters(params, false);
		List<MovementWard> movementList = (List<MovementWard>)jpa.getList();
		movements = new ArrayList<MovementWard>(movementList);			
		
		jpa.commitTransaction();
		
		return movements;
	}

	/**
	 * Gets the current quantity for the specified {@link Medical}.
	 * @param ward if specified medical are filtered by the {@link Ward}.
	 * @param medical the medical to check.
	 * @return the total quantity.
	 * @throws OHException if an error occurs retrieving the quantity.
	 */
	public int getCurrentQuantity(
			Ward ward, 
			Medical medical) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		Double mainQuantity = 0.0;
		Double dischargeQuantity = 0.0;
		int currentQuantity = 0;
		
		
		mainQuantity = _getMainQuantity(jpa, ward, medical);
		dischargeQuantity = _getDischargeQuantity(jpa, ward, medical);
		currentQuantity = (int)(mainQuantity - dischargeQuantity);
		
		return currentQuantity;	
	}
	
	private Double _getMainQuantity(
			DbJpaUtil jpa,
			Ward ward, 
			Medical medical) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		Double mainQuantity = 0.0;
				

		jpa.beginTransaction();		
		
		try {
			query = "SELECT SUM(MMV_QTY) MAIN FROM MEDICALDSRSTOCKMOV M WHERE MMV_MMVT_ID_A = 'testDisc' AND MMV_MDSR_ID = ? ";
			params.add(medical.getCode());
			if (ward!=null) {
				params.add(ward.getCode());
				query += " AND MMV_WRD_ID_A = ?";
			}
			jpa.createQuery(query, null, false);
			jpa.setParameters(params, false);
			mainQuantity = (Double)jpa.getResult();
		}  catch (OHException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();		

		return mainQuantity != null ? mainQuantity : 0.0;	
	}

	private Double _getDischargeQuantity(
			DbJpaUtil jpa,
			Ward ward, 
			Medical medical) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		Double dischargeQuantity = 0.0;
				

		jpa.beginTransaction();		
		
		try {
			query = "SELECT SUM(MMVN_MDSR_QTY) DISCHARGE FROM MEDICALDSRSTOCKMOVWARD WHERE MMVN_MDSR_ID = ?";
			params.add(medical.getCode());
			if (ward!=null) {
				params.add(ward.getCode());
				query += " AND MMVN_WRD_ID_A = ?";
			}
			jpa.createQuery(query, null, false);
			jpa.setParameters(params, false);
			dischargeQuantity = (Double)jpa.getResult();
		}  catch (OHException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();		

		return dischargeQuantity != null ? dischargeQuantity : 0.0;	
	}

	/**
	 * Stores the specified {@link Movement}.
	 * @param movement the movement to store.
	 * @return <code>true</code> if has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */	
	public boolean newMovementWard(
			MovementWard movement) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(movement);
		updateStockWardQuantity(jpa, movement);
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Stores the specified {@link Movement} list.
	 * @param movements the movement to store.
	 * @return <code>true</code> if the movements have been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */
	public boolean newMovementWard(
			ArrayList<MovementWard> movements) throws OHException 
	{
		for (MovementWard movement:movements) {

			boolean inserted = newMovementWard(movement);
			if (!inserted) 
			{
				return false;
			}
		}
		
		return true;		
	}

	/**
	 * Updates the specified {@link MovementWard}.
	 * @param movement the movement ward to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMovementWard(
			MovementWard movement) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.merge(movement);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Deletes the specified {@link MovementWard}.
	 * @param movement the movement ward to delete.
	 * @return <code>true</code> if the movement has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete.
	 */
	public boolean deleteMovementWard(
			MovementWard movement) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(movement);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * Updates the quantity for the specified movement ward.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param movement the movement ward to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateStockWardQuantity(
			DbJpaUtil jpa,
			MovementWard movement) throws OHException 
	{
		Double qty = movement.getQuantity();
		String ward = movement.getWard().getCode();
		Integer medical = movement.getMedical().getCode();
		
		boolean result = true;
		
		try {
		
			ArrayList<Object> params = new ArrayList<Object>(3);
			String query = "SELECT * FROM MEDICALDSRWARD WHERE MDSRWRD_WRD_ID_A = ? AND MDSRWRD_MDSR_ID = ?";
			jpa.createQuery(query, MovementWard.class, false);
			params.add(ward);
			params.add(medical);
			jpa.setParameters(params, false);
			jpa.getResult(); //if NoResultException -> insert in catch block
			
			//update
			params = new ArrayList<Object>(3);
			if (qty.doubleValue() < 0) {
				query = "UPDATE MEDICALDSRWARD SET MDSRWRD_IN_QTI = MDSRWRD_IN_QTI + ? WHERE MDSRWRD_WRD_ID_A = ? AND MDSRWRD_MDSR_ID = ?";
				params.add(-qty);
			} else {
				query = "UPDATE MEDICALDSRWARD SET MDSRWRD_OUT_QTI = MDSRWRD_OUT_QTI + ? WHERE MDSRWRD_WRD_ID_A = ? AND MDSRWRD_MDSR_ID = ?";
				params.add(qty);
			}
			jpa.createQuery(query, MovementWard.class, false);
			params.add(ward);
			params.add(medical);
			jpa.setParameters(params, false);
			jpa.executeUpdate();
		
		}  catch (NoResultException e) {
			//insert
			ArrayList<Object> parameters = new ArrayList<Object>(3);
			String query = "INSERT INTO MEDICALDSRWARD (MDSRWRD_WRD_ID_A, MDSRWRD_MDSR_ID, MDSRWRD_IN_QTI, MDSRWRD_OUT_QTI) " +
					"VALUES (?, ?, ?, '0')";
			jpa.createQuery(query, MovementWard.class, false);
			parameters.add(ward);
			parameters.add(medical);
			parameters.add(-qty);
			jpa.setParameters(parameters, false);
			jpa.executeUpdate();
			
		} 	catch (OHException e) {
			
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		
		} finally {
			jpa.commitTransaction();
		}
	
		return result;
	}

	/**
	 * Gets all the {@link Medical}s associated to specified {@link Ward}.
	 * @param wardId the ward id.
	 * @return the retrieved medicals.
	 * @throws OHException if an error occurs during the medical retrieving.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<MedicalWard> getMedicalsWard(
			char wardId) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		ArrayList<MedicalWard> medicalWards = new ArrayList<MedicalWard>();
		
				
		jpa.beginTransaction();		
		
		try {
			query = "SELECT mw FROM MedicalWard mw WHERE mw.id.ward_id=?";
			jpa.createQuery(query, MedicalWard.class, true);
			params.add(wardId);
			jpa.setParameters(params, true);
			List<MedicalWard> medicalWardList = (List<MedicalWard>)jpa.getList();
			Iterator<MedicalWard> medicalWardIterator = medicalWardList.iterator();
			while (medicalWardIterator.hasNext()) 
			{
				MedicalWard foudMedicalWard = medicalWardIterator.next();
				Medical medical = (Medical)jpa.find(Medical.class, foudMedicalWard.getId().getMedicalId()); 
				float qty = foudMedicalWard.getInQuantity() - foudMedicalWard.getOutQuantity();
				MedicalWard medicalWard = new MedicalWard(medical, (double)qty);
				medicalWards.add(medicalWard);
			}
		}  catch (OHException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();

		return medicalWards;
	}
}
