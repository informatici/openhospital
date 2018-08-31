package org.isf.medicalstock.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.springframework.stereotype.Component;
import org.isf.medicalstockward.model.MedicalWard;

/**
 * Persistence class for MedicalStock module.
 * 		   modified by alex:
 * 			- reflection from Medicals product code
 * 			- reflection from Medicals pieces per packet
 * 			- added complete Ward and Movement construction in getMovement()
 */
@Component
public class MedicalStockIoOperations {
	
	public enum MovementOrder {
		DATE, WARD, PHARMACEUTICAL_TYPE, TYPE;
	}

	/**
	 * Checks if we are in automatic lot mode.
	 * @return <code>true</code> if automatic lot mode, <code>false</code> otherwise.
	 */
	private boolean isAutomaticLotMode() {
		return GeneralData.AUTOMATICLOT;
	}

	/**
	 * Retrieves all medicals referencing the specified code.
	 * @param lotCode the lot code.
	 * @return the ids of medicals referencing the specified lot.
	 * @throws OHException if an error occurs retrieving the referencing medicals.
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getMedicalsFromLot(
			String lotCode) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();
			
			String query = "select distinct MDSR_ID from " +
					"((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
					"join MEDICALDSR  on MMV_MDSR_ID=MDSR_ID ) " +
					"join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A where LT_ID_A=?";
			params.add(lotCode);
			jpa.createQuery(query, null, false);
			jpa.setParameters(params, false);
			List<Integer> medicalIds = (List<Integer>)jpa.getList();			
			
			jpa.commitTransaction();
			
			return medicalIds;
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
	}
	
	/**
	 * Store the specified {@link Movement} by using automatically the most old lots
	 * @param movement - the {@link Movement} to store
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean newAutomaticDischargingMovement(
			Movement movement) throws OHException 
	{
		boolean result = false;
	
		
		try
		{			
			ArrayList<Lot> lots = getLotsByMedical(movement.getMedical());

			int qty = movement.getQuantity();			
			for (Lot lot : lots) 
			{
				int qtLot = lot.getQuantity();
				if (qtLot < qty) 
				{
					movement.setQuantity(qtLot);
					result = storeMovement(movement, lot.getCode());
					if (result) 
					{
						//medical stock movement inserted updates quantity of the medical
						result = updateStockQuantity(movement);
					}
					qty = qty - qtLot;
				} 
				else 
				{
					movement.setQuantity(qty);
					result = storeMovement(movement, lot.getCode());
					if (result) 
					{
						//medical stock movement inserted updates quantity of the medical
						result = updateStockQuantity(movement);
					}
					break;
				}
			}
		}
		catch (Exception e)
		{
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);			
		}
		
		return result;
	}
		
	/**
	 * Stores the specified {@link Movement}.
	 * @param movement - the movement to store.
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newMovement(
			Movement movement) throws OHException 
	{
		String lotCode = null;
		
		
		if (movement.getLot() != null)
		{
			lotCode = movement.getLot().getCode();
		}

		try 
		{
			//we have to manage the Lot
			if (movement.getType().getType().contains("+")) 
			{
				//if is in automatic lot mode then we have to generate a new lot code
				if (isAutomaticLotMode() || lotCode.equals("")) 
				{
					lotCode = generateLotCode();
				}

				boolean lotExists = lotExists(lotCode);
				if (!lotExists) 
				{
					boolean lotStored = storeLot(lotCode, movement.getLot());
					if (!lotStored) 
					{
						return false;
					}
				}
			}

			boolean movementStored = storeMovement(movement, lotCode);
			if (movementStored) 
			{
				//medical stock movement inserted updates quantity of the medical
				boolean stockQuantityUpdated = updateStockQuantity(movement);
				if (stockQuantityUpdated) 
				{
					return true;
				}
			}

			//something is failed
			return false;
		} 
		finally 
		{
			//Nothing to do
		}
	}
	
	/**
	 * Prepare the insert of the specified {@link Movement} (no commit)
	 * @param dbQuery - the session with the DB
	 * @param movement - the movement to store.
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean prepareChargingMovement(
			Movement movement) throws OHException 
	{ 
		return newMovement(movement);
	}
	
	/**
	 * Prepare the insert of the specified {@link Movement} (no commit)
	 * @param dbQuery - the session with the DB
	 * @param movement - the movement to store.
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean prepareDischargingwMovement(
			Movement movement) throws OHException 
	{
		String lotCode = null;
		
		
		if (movement.getLot() != null)
		{
			lotCode = movement.getLot().getCode();
		}

		boolean movementStored = storeMovement(movement, lotCode);

		//medical stock movement inserted
		if (movementStored) 
		{
			// updates quantity of the medical
			boolean stockQuantityUpdated = updateStockQuantity(movement);
			if (stockQuantityUpdated) 
			{
				return true;
			}
		}

		//something is failed
		return false;
	}

	/**
	 * Stores the specified {@link Movement}.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param movement the movement to store.
	 * @param lotCode the {@link Lot} code to use.
	 * @return <code>true</code> if the movement has stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the movement.
	 */
	protected boolean storeMovement(
			Movement movement, 
			String lotCode) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
				
		try {
			jpa.beginTransaction();	
			Lot lot = (Lot)jpa.find(Lot.class, lotCode); 
			movement.setLot(lot);
			jpa.merge(movement);
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		
		return result;
	}

	/**
	 * Creates a new unique lot code.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @return the new unique code.
	 * @throws OHException if an error occurs during the code generation.
	 */
	protected String generateLotCode() throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Random random = new Random();
		long candidateCode;
		
		Lot lot = null;
		
		try {
			do {
				jpa.beginTransaction();	
				
				candidateCode = Math.abs(random.nextLong());
				lot = (Lot)jpa.find(Lot.class, String.valueOf(candidateCode));
				
				jpa.commitTransaction();
				
			} while (lot != null);
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		} catch (Exception e) {
			jpa.rollbackTransaction();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		
		return String.valueOf(candidateCode);
	}

	/**
	 * Checks if the specified {@link Lot} exists.
	 * @param dbQuery the {@link DbQueryLogger} to use for the check.
	 * @param lotCode the lot code.
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean lotExists(
			String lotCode) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Lot lot = null;
		boolean result = false;
		
		
		try 
		{
			jpa.beginTransaction();	
			lot = (Lot)jpa.find(Lot.class, lotCode); 
			jpa.commitTransaction();
			
			if (lot != null)
			{
				result = true;
			}
		}
		catch (OHException e) 
		{
			jpa.rollbackTransaction();
			throw e;
		} 
		catch (Exception e) {
			jpa.rollbackTransaction();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		return result;
	}

	/**
	 * Stores the specified {@link Lot}.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param lotCode the {@link Lot} code.
	 * @param lot the lot to store.
	 * @return <code>true</code> if the lot has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurred storing the lot.
	 */
	protected boolean storeLot(
			String lotCode, 
			Lot lot) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = false;

		
		try 
		{
			jpa.beginTransaction();	
			lot.setCode(lotCode);
			jpa.persist(lot); 
			jpa.commitTransaction();
			result = true;	
		} 
		catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		catch (Exception e) {
			jpa.rollbackTransaction();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 
		
		return result;
	}

	/**
	 * Updated {@link Medical} stock quantity for the specified {@link Movement}.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param movement the movement.
	 * @return <code>true</code> if the quantity has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateStockQuantity(
			Movement movement) throws OHException 
	{
		if (movement.getType().getType().contains("+")) 
		{
			//incoming medical stock
			int medicalCode = movement.getMedical().getCode();
			boolean updated = updateMedicalIncomingQuantity(medicalCode, movement.getQuantity());
			
			return updated;
		} 
		else 
		{
			//outgoing medical stock
			int medicalCode = movement.getMedical().getCode();
			boolean updated = updateMedicalOutcomingQuantity(medicalCode, movement.getQuantity());
			if (!updated)
			{				
				return false;
			}
			else 
			{
				Ward ward = movement.getWard();
				if (ward != null) 
				{
					//updates stock quantity for wards
					return updateMedicalWardQuantity(ward.getCode(), medicalCode, movement.getQuantity());

				} 
				else 
				{
					return true;
				}
			}
		}
	}

	/**
	 * Updates the incoming quantity for the specified medical.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param medicalCode the medical code.
	 * @param incrementQuantity the quantity to add.
	 * @return <code>true</code> if the quantity has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateMedicalIncomingQuantity(
			int medicalCode, 
			double incrementQuantity) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
				
		try {
			jpa.beginTransaction();	
			Medical medical = (Medical)jpa.find(Medical.class, medicalCode); 
			medical.setInqty(medical.getInqty()+incrementQuantity);
			jpa.merge(medical);
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		return result;
	}

	/**
	 * Updates the outcoming quantity for the specified medicinal.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param medicalCode the medical code.
	 * @param incrementQuantity the quantity to add to the current outcoming quantity.
	 * @return <code>true</code> if the outcoming quantity has been updated <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateMedicalOutcomingQuantity(
			int medicalCode, 
			double incrementQuantity) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
				
		try {
			jpa.beginTransaction();	
			Medical medical = (Medical)jpa.find(Medical.class, medicalCode); 
			medical.setOutqty(medical.getOutqty()+incrementQuantity);
			jpa.merge(medical);
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		return result;
	}

	/**
	 * Updates medical quantity for the specified ward.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param wardCode the ward code.
	 * @param medicalCode the medical code.
	 * @param quantity the quantity to add to the current medical quantity.
	 * @return <code>true</code> if the quantity has been updated/inserted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	@SuppressWarnings("unchecked")
	protected boolean updateMedicalWardQuantity(
			String wardCode, 
			int medicalCode, 
			int quantity) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		
		List<MedicalWard> medicalWards = new ArrayList<MedicalWard>();
		
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM MEDICALDSRWARD WHERE MDSRWRD_WRD_ID_A = ? AND MDSRWRD_MDSR_ID = ?";
			params.add(wardCode);
			params.add(medicalCode);
			jpa.createQuery(query, MedicalWard.class, false);
			jpa.setParameters(params, false);
			medicalWards.addAll((List<MedicalWard>)jpa.getList());
			
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		
		if (!medicalWards.isEmpty())
		{			
			for (MedicalWard medicalWard : medicalWards)
			{
				try {
					jpa.beginTransaction();
					medicalWard.setInQuantity(medicalWard.getInQuantity()+quantity);
					jpa.merge(medicalWard);
					jpa.commitTransaction();
				} catch (OHException e) {
					jpa.rollbackTransaction();
					throw e;
				}
			}
		}
		else
		{
			try {
				jpa.beginTransaction();
				MedicalWard medicalWard = new MedicalWard(wardCode.charAt(0), medicalCode, quantity, 0);
				jpa.persist(medicalWard);
				jpa.commitTransaction();
			} catch (OHException e) {
				jpa.rollbackTransaction();
				throw e;
			}
		}
		
		return true;
	}

	/**
	 * Gets all the stored {@link Movement}.
	 * @return all retrieved movement
	 * @throws OHException if an error occurs retrieving the movements.
	 */
	public ArrayList<Movement> getMovements() throws OHException 
	{
		return getMovements(null, null, null);
	}

	/**
	 * Retrieves all the stored {@link Movement}s for the specified {@link Ward}.
	 * @param wardId the ward id.
	 * @param dateTo 
	 * @param dateFrom 
	 * @return the list of retrieved movements.
	 * @throws OHException if an error occurs retrieving the movements.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Movement> getMovements(
			String wardId, 
			GregorianCalendar dateFrom, 
			GregorianCalendar dateTo) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Movement> movements = null;
		
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM (" + 
							"(MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
							"JOIN (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID ) " +
							"LEFT JOIN MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A " +
							"LEFT JOIN WARD ON MMV_WRD_ID_A = WRD_ID_A " +
							"LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID ";
			if ((dateFrom != null) && (dateTo != null)) 
			{
				query += "WHERE DATE(MMV_DATE) BETWEEN DATE(?) and DATE(?) ";
				params.add(dateFrom);
				params.add(dateTo);
			}
			if (wardId != null && !wardId.equals("")) 
			{
				if (params.size() == 0) 
				{
					query += "WHERE ";
				}
				else 
				{
					query += "AND ";
				}
				query += "WRD_ID_A = ? ";
				params.add(wardId);
			}
			query += "ORDER BY MMV_DATE DESC, MMV_REFNO DESC";		
			jpa.createQuery(query, Movement.class, false);
			jpa.setParameters(params, false);
			List<Movement> movementList = (List<Movement>)jpa.getList();
			movements = new ArrayList<Movement>(movementList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		return movements;
	}

	/**
	 * Retrieves all the stored {@link Movement} with the specified criteria.
	 * @param medicalCode the medical code.
	 * @param medicalType the medical type.
	 * @param wardId the ward type.
	 * @param movType the movement type.
	 * @param movFrom the lower bound for the movement date range.
	 * @param movTo the upper bound for the movement date range.
	 * @param lotPrepFrom the lower bound for the lot preparation date range.
	 * @param lotPrepTo the upper bound for the lot preparation date range.
	 * @param lotDueFrom the lower bound for the lot due date range.
	 * @param lotDueTo the lower bound for the lot due date range.
	 * @return all the retrieved movements.
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Movement> getMovements(
			Integer medicalCode,
			String medicalType, 
			String wardId, 
			String movType,
			GregorianCalendar movFrom, 
			GregorianCalendar movTo,
			GregorianCalendar lotPrepFrom, 
			GregorianCalendar lotPrepTo,
			GregorianCalendar lotDueFrom, 
			GregorianCalendar lotDueTo) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Movement> movements = null;
		String query = "";
				
		try {
			jpa.beginTransaction();
	
			if (lotPrepFrom != null || lotDueFrom != null) 
			{
				query = "select * from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) "
						+ "join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID )"
						+ " left join WARD on MMV_WRD_ID_A=WRD_ID_A "
						+ " join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A "
						+ " LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID "
						+ " where ";
			} 
			else 
			{
				query = "select * from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) "
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
					query += "(MDSR_MDSRT_ID_A=?) ";
					params.add(medicalType);
				} else if (medicalType == null)
				{
					query += "(MDSR_ID=?) ";
					params.add(medicalCode);
				}
			}
			if ((movFrom != null) && (movTo != null)) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(DATE(MMV_DATE) between DATE(?) and DATE(?)) ";
				params.add(movFrom);
				params.add(movTo);
			}
			if ((lotPrepFrom != null) && (lotPrepTo != null)) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(DATE(LT_PREP_DATE) between DATE(?) and DATE(?)) ";
				params.add(lotPrepFrom);
				params.add(lotPrepTo);
			}
			if ((lotDueFrom != null) && (lotDueTo != null)) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(DATE(LT_DUE_DATE) between DATE(?) and DATE(?)) ";
				params.add(lotDueFrom);
				params.add(lotDueTo);
			}
			if (movType != null) {
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(MMVT_ID_A=?) ";
				params.add(movType);
			}
			if (wardId != null) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(WRD_ID_A=?) ";
				params.add(wardId);
			}
			query += " ORDER BY MMV_DATE DESC, MMV_REFNO DESC";
			
			jpa.createQuery(query, Movement.class, false);
			jpa.setParameters(params, false);
			List<Movement> movementList = (List<Movement>)jpa.getList();
			movements = new ArrayList<Movement>(movementList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		
		return movements;
	}

	/**
	 * Retrieves {@link Movement}s for printing using specified filtering criteria.
	 * @param medicalDescription the medical description.
	 * @param medicalTypeCode the medical type code.
	 * @param wardId the ward id.
	 * @param movType the movement type.
	 * @param movFrom the lower bound for the movement date range.
	 * @param movTo the upper bound for the movement date range.
	 * @param lotCode the lot code.
	 * @param order the result order.
	 * @return the retrieved movements.
	 * @throws OHException if an error occurs retrieving the movements.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Movement> getMovementForPrint(
			String medicalDescription,
			String medicalTypeCode, 
			String wardId, 
			String movType,
			GregorianCalendar movFrom, 
			GregorianCalendar movTo, 
			String lotCode,
			MovementOrder order) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Movement> movements = null;
		String query = "";
				
		try {
			jpa.beginTransaction();
	
			query = "select * from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
					"join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID) " +
					"left join WARD on MMV_WRD_ID_A=WRD_ID_A " +
					"left join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A " +
					"LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID " +
					"where ";
	
			if ((medicalDescription != null) || (medicalTypeCode != null)) 
			{
				if (medicalDescription == null) 
				{
					query += "(MDSR_MDSRT_ID_A = ?) ";
					params.add(medicalTypeCode);
				} 
				else if (medicalTypeCode == null) 
				{
					query += "(MDSR_DESC like ?) ";
					params.add("%" + medicalDescription + "%");
				}
			}
			if (lotCode != null) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(LT_ID_A like ?) ";
				params.add("%" + lotCode + "%");
			}
			if ((movFrom != null) && (movTo != null)) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(DATE(MMV_DATE) between DATE(?) and DATE(?)) ";
				params.add(movFrom);
				params.add(movTo);
			}		
			if (movType != null) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(MMVT_ID_A=?) ";
				params.add(movType);
			}
			if (wardId != null) 
			{
				if (params.size()!=0) 
				{
					query += "and ";
				}
				query += "(WRD_ID_A=?) ";
				params.add(wardId);
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
	
			jpa.createQuery(query, Movement.class, false);
			jpa.setParameters(params, false);
			List<Movement> movementList = (List<Movement>)jpa.getList();
			movements = new ArrayList<Movement>(movementList);			
			
			jpa.commitTransaction();		
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		return movements;
	}

	/**
	 * Retrieves lot referred to the specified {@link Medical}.
	 * @param medical the medical.
	 * @return a list of {@link Lot}.
	 * @throws OHException if an error occurs retrieving the lot list.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Lot> getLotsByMedical(Medical medical) throws OHException {
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Lot> lots = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();
			
			String query = "select LT_ID_A,LT_PREP_DATE,LT_DUE_DATE,LT_COST,"
					+ "SUM(IF(MMVT_TYPE LIKE '+%',MMV_QTY,-MMV_QTY)) as quantity from "
					+ "((MEDICALDSRLOT join MEDICALDSRSTOCKMOV on MMV_LT_ID_A=LT_ID_A) join MEDICALDSR on MMV_MDSR_ID=MDSR_ID)"
					+ " join MEDICALDSRSTOCKMOVTYPE on MMV_MMVT_ID_A=MMVT_ID_A "
					+ "where MDSR_ID=? group by LT_ID_A order by LT_DUE_DATE";
			params.add(medical.getCode());
			jpa.createQuery(query, null, false);
			jpa.setParameters(params, false);
			List<Object[]> lotList = (List<Object[]>)jpa.getList();	
			lots = new ArrayList<Lot>();
			for (Object[] object: lotList)
			{
				Lot lot = _convertObjectToLot(object);
				
				lots.add(lot);
			}
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		return lots;
	}	

	private Lot _convertObjectToLot(Object[] object)
	{

		Lot lot = new Lot();
		lot.setCode((String)object[0]);
		lot.setPreparationDate(_convertTimestampToCalendar((Timestamp)object[1]));
		lot.setDueDate(_convertTimestampToCalendar((Timestamp)object[2]));
		lot.setCost((Double)object[3]);
		lot.setQuantity(((Double)object[4]).intValue());
		
		return lot;
	}
	
	private GregorianCalendar _convertTimestampToCalendar(Timestamp time)
	{
		GregorianCalendar calendar = null;
		
		if (time != null) 
		{
			calendar = new GregorianCalendar();
			calendar.setTimeInMillis(time.getTime());
		}
		
		return calendar;
	}
		
	/**
	 * returns the date of the last movement
	 * @return 
	 * @throws OHException
	 */
	public GregorianCalendar getLastMovementDate() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		String query = null;
		GregorianCalendar gc = new GregorianCalendar();
		
		try {
			jpa.beginTransaction();		
			
			query = "SELECT MAX(MMV_DATE) AS DATE FROM MEDICALDSRSTOCKMOV";
			jpa.createQuery(query, null, false);
			Timestamp time = (Timestamp)jpa.getResult();
			if (time != null) 
			{
				gc.setTime(time);
			}
			else
			{
				gc = null;
			}
			
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
	

		return gc;
	}
	
	/**
	 * check if the reference number is already used
	 * @return <code>true</code> if is already used, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean refNoExists(
			String refNo) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
		String query = null;
		boolean result = false;
		ArrayList<Object> params = new ArrayList<Object>();
		
		try {
			jpa.beginTransaction();		
		
			query = "SELECT MMV_REFNO FROM MEDICALDSRSTOCKMOV WHERE MMV_REFNO LIKE ?";
			jpa.createQuery(query, null, false);
			params.add(refNo);
			jpa.setParameters(params, false);
			if (jpa.getList().size() > 0)
			{
				result = true;
			}
			
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		} 				
	
		
		return result;
	}

	/**
	 * Retrieves all the movement associated to the specified reference number.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param refNo the reference number.
	 * @return the retrieved movements.
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Movement> getMovementsByReference(
			String refNo) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Movement> movements = null;
		String query = "";
				
		try {
			jpa.beginTransaction();
	
			query = "SELECT * FROM (" +
					"(MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
					"JOIN (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID ) " +
					"LEFT JOIN MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A " +
					"LEFT JOIN WARD ON MMV_WRD_ID_A = WRD_ID_A " +
					"WHERE MMV_REFNO = ? " +
					"ORDER BY MMV_DATE DESC, MMV_REFNO DESC";
	
			jpa.createQuery(query, Movement.class, false);
			params.add(refNo);
			jpa.setParameters(params, false);
			List<Movement> movementList = (List<Movement>)jpa.getList();
			movements = new ArrayList<Movement>(movementList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			jpa.rollbackTransaction();
			throw e;
		}
		return movements;
	}
}
