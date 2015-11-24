package org.isf.medicalstock.service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medtype.model.MedicalType;
import org.isf.supplier.model.Supplier;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.springframework.stereotype.Component;

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

	//Replaced by getMedical in MedicalBrowsingManager
	/*
	 * Gets the current quantity for the specified {@link Medical}.
	 * @param medical the medical to check.
	 * @return the current medical quantity.
	 * @throws OHException if an error occurs retrieving the medical quantity.

	public int getCurrentQuantity(Medical medical) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(medical.getCode());
		String query = "select MDSR_OUT_QTI, MDSR_IN_QTI, MDSR_INI_STOCK_QTI, MDSR_MIN_STOCK_QTI from MEDICALDSR where MDSR_ID=?";

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			int totQty = 0;
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			if (resultSet.first()) {
				int outQty = resultSet.getInt("MDSR_OUT_QTI");
				int inQty = resultSet.getInt("MDSR_IN_QTI");
				int iniQty = resultSet.getInt("MDSR_INI_STOCK_QTI");
				totQty = iniQty + inQty - outQty;
			}
			return totQty;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
	} */

	//Replaced by getMedical in MedicalBrowsingManager
	/*
	 * Checks if the provided quantity is under the medical limits.
	 * @param medicalSelected the selected medical.
	 * @param qtyProvided the quantity provided by the user.
	 * @return <code>true</code> if is under the limit, false otherwise.
	 * @throws OHException if an error occurs during the check.

	public boolean alertStockQuantity(Medical medicalSelected, int qtyProvided) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(medicalSelected.getCode());
		String query = "select MDSR_OUT_QTI,MDSR_IN_QTI,MDSR_INI_STOCK_QTI,MDSR_MIN_STOCK_QTI from MEDICALDSR where MDSR_ID=?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);

			if (resultSet.first()) {

				int outQty = resultSet.getInt("MDSR_OUT_QTI");
				outQty += qtyProvided;

				int minQty = resultSet.getInt("MDSR_MIN_STOCK_QTI");
				int inQty = resultSet.getInt("MDSR_IN_QTI");
				int iniQty = resultSet.getInt("MDSR_INI_STOCK_QTI");
				int totQty = iniQty + inQty - outQty;

				return totQty < minQty;
			}

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}

		return false;
	}	 */

	/**
	 * Retrieves all medicals referencing the specified code.
	 * @param lotCode the lot code.
	 * @return the ids of medicals referencing the specified lot.
	 * @throws OHException if an error occurs retrieving the referencing medicals.
	 */
	public List<Integer> getMedicalsFromLot(String lotCode) throws OHException
	{

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			List<Object> parameters = Collections.<Object>singletonList(lotCode);
			String query = "select distinct MDSR_ID from " +
					"((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
					"join MEDICALDSR  on MMV_MDSR_ID=MDSR_ID ) " +
					"join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A where LT_ID_A=?";
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, false);

			List<Integer> medicalIds = new ArrayList<Integer>();
			while(resultSet.next()) medicalIds.add(resultSet.getInt("MDSR_ID"));

			return medicalIds;
		} catch (SQLException e) {
			dbQuery.rollback();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
	}
	
	/**
	 * Store the specified {@link Movement} by using automatically the most old lots
	 * @param movement - the {@link Movement} to store
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean newAutomaticDischargingMovement(Movement movement) throws OHException {
		DbQueryLogger dbQuery =  new DbQueryLogger();
		boolean result = false;
		
		ArrayList<Lot> lots = getLotsByMedical(movement.getMedical());
		int qty = movement.getQuantity();
		
		for (Lot lot : lots) {
			int qtLot = lot.getQuantity();
			if (qtLot < qty) {
				movement.setQuantity(qtLot);
				result = storeMovement(dbQuery, movement, lot.getCode());
				if (result) {
					//medical stock movement inserted
					// updates quantity of the medical
					result = updateStockQuantity(dbQuery, movement);
				}
				qty = qty - qtLot;
			} else {
				movement.setQuantity(qty);
				result = storeMovement(dbQuery, movement, lot.getCode());
				if (result) {
					//medical stock movement inserted
					// updates quantity of the medical
					result = updateStockQuantity(dbQuery, movement);
				}
				break;
			}
		}
		if (result) {
			dbQuery.commit();
		}
		return result;
	}
	
	/**
	 * Prepare the insert for the the specified {@link Movement} by using automatically the most old lots (no commit)
	 * @param dbQuery - the connection with the DB
	 * @param movement - the {@link Movement} to store
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean newAutomaticDischargingMovement(DbQueryLogger dbQuery, Movement movement) throws OHException {
		boolean result = false;
		
		ArrayList<Lot> lots = getLotsByMedical(movement.getMedical());
		int qty = movement.getQuantity();
		
		for (Lot lot : lots) {
			int qtLot = lot.getQuantity();
			if (qtLot < qty) {
				movement.setQuantity(qtLot);
				result = storeMovement(dbQuery, movement, lot.getCode());
				if (result) {
					//medical stock movement inserted
					// updates quantity of the medical
					result = updateStockQuantity(dbQuery, movement);
				}
				qty = qty - qtLot;
			} else {
				movement.setQuantity(qty);
				result = storeMovement(dbQuery, movement, lot.getCode());
				if (result) {
					//medical stock movement inserted
					// updates quantity of the medical
					result = updateStockQuantity(dbQuery, movement);
				}
				break;
			}
		}
		return result;
	}
	
	/**
	 * Stores the specified {@link Movement}.
	 * @param movement - the movement to store.
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newMovement(Movement movement) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();

		String lotCode = (movement.getLot() != null)?movement.getLot().getCode():null;

		try {

			if (movement.getType().getType().equals("+")) {
				//we have to manage the Lot

				//if is in automatic lot mode then we have to generate a new lot code
				if (isAutomaticLotMode() || lotCode.equals("")) {
					lotCode = generateLotCode(dbQuery);
				}

				boolean lotExists = lotExists(dbQuery, lotCode);

				if (!lotExists) {
					boolean lotStored = storeLot(dbQuery, lotCode, movement.getLot());
					if (!lotStored) {
						dbQuery.rollback();
						return false;
					}
				}
			}

			boolean movementStored = storeMovement(dbQuery, movement, lotCode);

			if (movementStored) {
				//medical stock movement inserted

				// updates quantity of the medical
				boolean stockQuantityUpdated = updateStockQuantity(dbQuery, movement);

				if (stockQuantityUpdated) {
					dbQuery.commit();
					return true;
				}
			}

			//something is failed
			dbQuery.rollback();
			return false;
		} finally{
			dbQuery.releaseConnection();
		}
	}
	
	/**
	 * Prepare the insert of the specified {@link Movement} (no commit)
	 * @param dbQuery - the session with the DB
	 * @param movement - the movement to store.
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean prepareChargingMovement(DbQueryLogger dbQuery, Movement movement) throws OHException {

		String lotCode = (movement.getLot() != null) ? movement.getLot().getCode() : null;

		if (movement.getType().getType().equals("+")) {
			//we have to manage the Lot

			//if is in automatic lot mode then we have to generate a new lot code
			if (isAutomaticLotMode() || lotCode.equals("")) {
				lotCode = generateLotCode(dbQuery);
			}

			boolean lotExists = lotExists(dbQuery, lotCode);

			if (!lotExists) {
				boolean lotStored = storeLot(dbQuery, lotCode, movement.getLot());
				if (!lotStored) {
					dbQuery.rollback();
					return false;
				}
			}
		}

		boolean movementStored = storeMovement(dbQuery, movement, lotCode);

		if (movementStored) {
			//medical stock movement inserted

			// updates quantity of the medical
			boolean stockQuantityUpdated = updateStockQuantity(dbQuery, movement);

			if (stockQuantityUpdated) {
				return true;
			}
		}

		//something is failed
		dbQuery.rollback();
		return false;
	}
	
	/**
	 * Prepare the insert of the specified {@link Movement} (no commit)
	 * @param dbQuery - the session with the DB
	 * @param movement - the movement to store.
	 * @return <code>true</code> if the movement has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean prepareDischargingwMovement(DbQueryLogger dbQuery, Movement movement) throws OHException {

		String lotCode = (movement.getLot() != null)?movement.getLot().getCode():null;

		boolean movementStored = storeMovement(dbQuery, movement, lotCode);

		if (movementStored) {
			//medical stock movement inserted

			// updates quantity of the medical
			boolean stockQuantityUpdated = updateStockQuantity(dbQuery, movement);

			if (stockQuantityUpdated) {
				return true;
			}
		}

		//something is failed
		dbQuery.rollback();
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
	protected boolean storeMovement(DbQueryLogger dbQuery, Movement movement, String lotCode) throws OHException
	{
		try {
			String wardCode = null;
			if (movement.getWard() != null) wardCode = movement.getWard().getCode();

			Supplier origin = null;
			if (movement.getType().getType().equalsIgnoreCase("+")) origin = movement.getOrigin();

			List<Object> parameters = new ArrayList<Object>(7);
			parameters.add(movement.getMedical().getCode());
			parameters.add(movement.getType().getCode());
			parameters.add(wardCode);
			parameters.add(lotCode);
			parameters.add(new Timestamp(movement.getDate().getTimeInMillis()));
			parameters.add(movement.getQuantity());
			parameters.add(origin == null ? 0 : origin.getSupId());
			parameters.add(movement.getRefNo());

			String query = "insert into MEDICALDSRSTOCKMOV(MMV_MDSR_ID,MMV_MMVT_ID_A,MMV_WRD_ID_A,MMV_LT_ID_A, "
					+ "MMV_DATE,MMV_QTY,MMV_FROM,MMV_REFNO) values (?, ?, ?, ?, ?, ?, ?, ?)";

			ResultSet result = dbQuery.setDataReturnGeneratedKeyWithParams(query, parameters, false);

			if (result.first()) {
				movement.setCode(result.getInt(1));
				return true;
			} else return false;

		} catch (SQLException e) {
			dbQuery.rollback();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 
	}

	/**
	 * Creates a new unique lot code.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @return the new unique code.
	 * @throws OHException if an error occurs during the code generation.
	 */
	protected String generateLotCode(DbQueryLogger dbQuery) throws OHException
	{
		Random random = new Random();

		try {
			long candidateCode;
			ResultSet result;
			do {
				candidateCode = Math.abs(random.nextLong());
				List<Object> parameters = Collections.<Object>singletonList(candidateCode);
				String query = "select * from MEDICALDSRLOT where LT_ID_A=?";
				result = dbQuery.getDataWithParams(query, parameters, false);
			} while(result.first());

			return String.valueOf(candidateCode);
		} catch (SQLException e) {
			dbQuery.rollback();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 
	}

	/**
	 * Checks if the specified {@link Lot} exists.
	 * @param dbQuery the {@link DbQueryLogger} to use for the check.
	 * @param lotCode the lot code.
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean lotExists(DbQueryLogger dbQuery, String lotCode) throws OHException
	{
		try {
			List<Object> parameters = Collections.<Object>singletonList(lotCode);
			String query = "select LT_ID_A from MEDICALDSRLOT where LT_ID_A=?";
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, false);
			return resultSet.next();
		} catch (SQLException e) {
			dbQuery.rollback();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
	}

	/**
	 * Stores the specified {@link Lot}.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param lotCode the {@link Lot} code.
	 * @param lot the lot to store.
	 * @return <code>true</code> if the lot has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurred storing the lot.
	 */
	protected boolean storeLot(DbQueryLogger dbQuery, String lotCode, Lot lot) throws OHException {
		List<Object> parameters = new ArrayList<Object>(3);
		parameters.add(lotCode);
		parameters.add(toDate(lot.getPreparationDate()));
		parameters.add(toDate(lot.getDueDate()));
		parameters.add(lot.getCost());
		String query = "insert into MEDICALDSRLOT(LT_ID_A,LT_PREP_DATE,LT_DUE_DATE,LT_COST) values (?, ?, ?, ?)";
		boolean result = dbQuery.setDataWithParams(query, parameters, false);
		return result;
	}

	/**
	 * Updated {@link Medical} stock quantity for the specified {@link Movement}.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param movement the movement.
	 * @return <code>true</code> if the quantity has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateStockQuantity(DbQueryLogger dbQuery, Movement movement) throws OHException {

		if (movement.getType().getType().equals("+")) {

			//incoming medical stock
			int medicalCode = movement.getMedical().getCode();
			boolean updated = updateMedicalIncomingQuantity(dbQuery, medicalCode, movement.getQuantity());
			return updated;

		} else {

			//outgoing medical stock
			int medicalCode = movement.getMedical().getCode();
			boolean updated = updateMedicalOutcomingQuantity(dbQuery, medicalCode, movement.getQuantity());

			if (!updated) return false;
			else {

				Ward ward = movement.getWard();
				if (ward != null) {
					//updates stock quantity for wards
					return updateMedicalWardQuantity(dbQuery, ward.getCode(), medicalCode, movement.getQuantity());

				} else return true;
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
	protected boolean updateMedicalIncomingQuantity(DbQueryLogger dbQuery, int medicalCode, double incrementQuantity) throws OHException
	{
		List<Object> parameters = new ArrayList<Object>(2);
		parameters.add(incrementQuantity);
		parameters.add(medicalCode);

		String query = "update MEDICALDSR set MDSR_IN_QTI = MDSR_IN_QTI + ? where MDSR_ID = ?";
		if (!dbQuery.setDataWithParams(query, parameters, false)) {
			dbQuery.rollback();
			return false;
		} else return true;
	}

	/**
	 * Updates the outcoming quantity for the specified medicinal.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param medicalCode the medical code.
	 * @param incrementQuantity the quantity to add to the current outcoming quantity.
	 * @return <code>true</code> if the outcoming quantity has been updated <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateMedicalOutcomingQuantity(DbQueryLogger dbQuery, int medicalCode, double incrementQuantity) throws OHException
	{
		List<Object> parameters = new ArrayList<Object>(2);
		parameters.add(incrementQuantity);
		parameters.add(medicalCode);

		String query = "update MEDICALDSR set MDSR_OUT_QTI = MDSR_OUT_QTI + ? where MDSR_ID = ?";
		if (!dbQuery.setDataWithParams(query, parameters, false)) {
			dbQuery.rollback();
			return false;
		} else return true;
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
	protected boolean updateMedicalWardQuantity(DbQueryLogger dbQuery, String wardCode, int medicalCode, int quantity) throws OHException
	{
		List<Object> parameters = new ArrayList<Object>(2);
		parameters.add(wardCode);
		parameters.add(medicalCode);

		String query = "SELECT * FROM MEDICALDSRWARD WHERE MDSRWRD_WRD_ID_A = ? AND MDSRWRD_MDSR_ID = ?";
		ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, false);

		try {
			boolean result;
			if (resultSet.next()) {
				//update
				parameters = new ArrayList<Object>(3);
				parameters.add(quantity);
				parameters.add(wardCode);
				parameters.add(medicalCode);
				query = "UPDATE MEDICALDSRWARD SET MDSRWRD_IN_QTI = MDSRWRD_IN_QTI + ? WHERE " +
						"MDSRWRD_WRD_ID_A = ? AND MDSRWRD_MDSR_ID = ?";
				result = dbQuery.setDataWithParams(query, parameters, false);
			} else {
				//insert
				parameters = new ArrayList<Object>(3);
				parameters.add(wardCode);
				parameters.add(medicalCode);
				parameters.add(quantity);
				query = "INSERT INTO MEDICALDSRWARD (MDSRWRD_WRD_ID_A, MDSRWRD_MDSR_ID, MDSRWRD_IN_QTI, MDSRWRD_OUT_QTI) " +
						"VALUES (?, ?, ?, '0')";
				result = dbQuery.setDataWithParams(query, parameters, false);
			}
			if (!result) {
				dbQuery.rollback();
				return false;
			} else 
				return true;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
	}

	/*
	 * Moved to medstockmovtype module

	public ArrayList<MovementType> getMovementType() {
		ArrayList<MovementType> movementType = null;
		String string = "select MMVT_ID_A,MMVT_DESC,MMVT_TYPE from MEDICALDSRSTOCKMOVTYPE order by MMVT_ID_A";
		DbQuery dbQuery = new DbQuery();
		try {
			try {
				ResultSet resultSet = dbQuery.getData(string, true);
				movementType = new ArrayList<MovementType>(resultSet
						.getFetchSize());
				while (resultSet.next()) {
					movementType.add(new MovementType(resultSet
							.getString("MMVT_ID_A"), resultSet
							.getString("MMVT_DESC"), resultSet
							.getString("MMVT_TYPE")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				dbQuery.releaseConnection();
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return movementType;
	}
	 */

	/**
	 * Gets all the stored {@link Movement}.
	 * @return all retrieved movement
	 * @throws OHException if an error occurs retrieving the movements.
	 */
	public ArrayList<Movement> getMovements() throws OHException {
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
	public ArrayList<Movement> getMovements(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHException {

		ArrayList<Movement> movements = null;

		List<Object> parameters = new ArrayList<Object>();

		StringBuilder query = new StringBuilder("SELECT * FROM (");
		query.append("(MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) ");
		query.append("JOIN (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID ) ");
		query.append("LEFT JOIN MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A ");
		query.append("LEFT JOIN WARD ON MMV_WRD_ID_A = WRD_ID_A ");
		query.append("LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID ");

		if ((dateFrom != null) && (dateTo != null)) {
			query.append("WHERE DATE(MMV_DATE) BETWEEN DATE(?) and DATE(?) ");
			parameters.add(toDate(dateFrom));
			parameters.add(toDate(dateTo));
		}
		
		if (wardId != null && !wardId.equals("")) {
			if (parameters.size() == 0) query.append("WHERE ");
			else query.append("AND ");
			query.append("WRD_ID_A = ? ");
			parameters.add(wardId);
		}
		
		query.append("ORDER BY MMV_DATE DESC, MMV_REFNO DESC");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			movements = new ArrayList<Movement>(resultSet.getFetchSize());
			while (resultSet.next()) {

				MedicalType medicalType = new MedicalType(resultSet.getString("MDSRT_ID_A"), resultSet.getString("MDSRT_DESC"));
				Medical medical = new Medical(resultSet.getInt("MDSR_ID"), 
						medicalType, 
						resultSet.getString("MDSR_CODE"), 
						resultSet.getString("MDSR_DESC"), 
						resultSet.getDouble("MDSR_INI_STOCK_QTI"), 
						resultSet.getInt("MDSR_PCS_X_PCK"), 
						resultSet.getDouble("MDSR_MIN_STOCK_QTI"),
						resultSet.getDouble("MDSR_IN_QTI"),
						resultSet.getDouble("MDSR_OUT_QTI"),
						resultSet.getInt("MDSR_LOCK"));

				Ward ward = toWard(resultSet);
				
				Lot lot = toLot(resultSet);
				
				MovementType movementType = new MovementType(resultSet.getString("MMVT_ID_A"),
						resultSet.getString("MMVT_DESC"), 
						resultSet.getString("MMVT_TYPE"));
				
				Supplier supplier = new Supplier();
						supplier.setSupId(resultSet.getInt("MMV_FROM"));
						supplier.setSupName(resultSet.getString("SUP_NAME"));

				Movement movement = new Movement(medical, movementType, ward, lot,
						toCalendar(resultSet.getTimestamp("MMV_DATE").getTime()), 
						resultSet.getInt("MMV_QTY"), 
						supplier,
						resultSet.getString("MMV_REFNO"));
				
				movements.add(movement);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
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
	public ArrayList<Movement> getMovements(Integer medicalCode,
			String medicalType, String wardId, String movType,
			GregorianCalendar movFrom, GregorianCalendar movTo,
			GregorianCalendar lotPrepFrom, GregorianCalendar lotPrepTo,
			GregorianCalendar lotDueFrom, GregorianCalendar lotDueTo) throws OHException {

		ArrayList<Movement> movements = null;

		List<Object> parameters = new ArrayList<Object>();
		StringBuilder query = new StringBuilder();

		if (lotPrepFrom != null || lotDueFrom != null) {
			query.append("select * from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) "
					+ "join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID )"
					+ " left join WARD on MMV_WRD_ID_A=WRD_ID_A "
					+ " join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A "
					+ " LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID "
					+ " where ");
		} else {
			query.append("select * from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) "
					+ "join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID )"
					+ " left join WARD on MMV_WRD_ID_A=WRD_ID_A "
					+ " left join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A "
					+ " LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID "
					+ " where ");
		}

		if ((medicalCode != null) || (medicalType != null)) {
			if (medicalCode == null) {
				query.append("(MDSR_MDSRT_ID_A=?) ");
				parameters.add(medicalType);
			} else if (medicalType == null) {
				query.append("(MDSR_ID=?) ");
				parameters.add(medicalCode);
			}
		}

		if ((movFrom != null) && (movTo != null)) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(DATE(MMV_DATE) between DATE(?) and DATE(?)) ");
			parameters.add(toDate(movFrom));
			parameters.add(toDate(movTo));
		}

		if ((lotPrepFrom != null) && (lotPrepTo != null)) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(DATE(LT_PREP_DATE) between DATE(?) and DATE(?)) ");
			parameters.add(toDate(lotPrepFrom));
			parameters.add(toDate(lotPrepTo));
		}

		if ((lotDueFrom != null) && (lotDueTo != null)) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(DATE(LT_DUE_DATE) between DATE(?) and DATE(?)) ");
			parameters.add(toDate(lotDueFrom));
			parameters.add(toDate(lotDueTo));
		}

		if (movType != null) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(MMVT_ID_A=?) ");
			parameters.add(movType);
		}

		if (wardId != null) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(WRD_ID_A=?) ");
			parameters.add(wardId);
		}

		query.append(" ORDER BY MMV_DATE DESC, MMV_REFNO DESC");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			movements = new ArrayList<Movement>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Movement movement = toMovement(resultSet);
				movements.add(movement);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return movements;
	}

	/**
	 * Extracts a {@link Movement} from the current row in the {@link ResultSet}.
	 * @param resultSet the resultset.
	 * @return the {@link Movement} object.
	 * @throws SQLException if an error occurs during the retrieving.
	 */
	protected Movement toMovement(ResultSet resultSet) throws SQLException {
		MedicalType medicalType = new MedicalType("", resultSet.getString("MDSRT_DESC"));
		Medical medical = new Medical(0, medicalType, resultSet.getString("MDSR_CODE"), resultSet.getString("MDSR_DESC"), 0, 0, 0, 0, 0, 0);
		MovementType movementType = new MovementType(resultSet.getString("MMVT_ID_A"), resultSet.getString("MMVT_DESC"), resultSet.getString("MMVT_TYPE"));
		Ward ward = toWard(resultSet);
		Supplier supplier = new Supplier(resultSet.getInt("MMV_FROM"), resultSet.getString("SUP_NAME"), null, null, null, null, null, null);
		Lot lot = toLot(resultSet);
		Movement movement = new Movement(medical, movementType, ward, lot, toCalendar(resultSet.getTimestamp("MMV_DATE").getTime()), resultSet.getInt("MMV_QTY"), supplier, resultSet.getString("MMV_REFNO"));

		return movement;
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
	public ArrayList<Movement> getMovementForPrint(String medicalDescription,
			String medicalTypeCode, String wardId, String movType,
			GregorianCalendar movFrom, GregorianCalendar movTo, String lotCode,
			MovementOrder order) throws OHException {

		ArrayList<Movement> movements = null;

		List<Object> parameters = new ArrayList<Object>();
		StringBuilder query = new StringBuilder();

		query.append("select * from ((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) ");
		query.append("join (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID) ");
		query.append("left join WARD on MMV_WRD_ID_A=WRD_ID_A ");
		query.append("left join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A ");
		query.append("LEFT JOIN SUPPLIER ON MMV_FROM = SUP_ID ");
		query.append("where ");

		if ((medicalDescription != null) || (medicalTypeCode != null)) {
			if (medicalDescription == null) {
				query.append("(MDSR_MDSRT_ID_A = ?) ");
				parameters.add(medicalTypeCode);
			} else if (medicalTypeCode == null) {
				query.append("(MDSR_DESC like ?) ");
				parameters.add("%" + medicalDescription + "%");
			}
		}

		if (lotCode != null) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(LT_ID_A like ?) ");
			parameters.add("%" + lotCode + "%");
		}

		if ((movFrom != null) && (movTo != null)) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(DATE(MMV_DATE) between DATE(?) and DATE(?) ");
			parameters.add(toDate(movFrom));
			parameters.add(toDate(movTo));
		}
		
		if (movType != null) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(MMVT_ID_A=?) ");
			parameters.add(movType);
		}

		if (wardId != null) {
			if (parameters.size()!=0) query.append("and ");
			query.append("(WRD_ID_A=?) ");
			parameters.add(wardId);
		}

		switch (order) {
			case DATE:
				query.append(" ORDER BY MMV_DATE DESC, MMV_REFNO DESC");
				break;
			case WARD:
				query.append(" order by MMV_REFNO DESC, WRD_NAME desc");
				break;
			case PHARMACEUTICAL_TYPE:
				query.append(" order by MMV_REFNO DESC, MDSR_MDSRT_ID_A,MDSR_DESC");
				break;
			case TYPE:
				query.append(" order by MMV_REFNO DESC, MMVT_DESC");
				break;
		}
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			movements = new ArrayList<Movement>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Movement movement = toMovement(resultSet);
				movements.add(movement);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return movements;
	}
	
	/**
	 * Retrieves all medicals referencing the specified code.
	 * @param dbQuery - the connection with the DB
	 * @param lotCode the lot code.
	 * @return the ids of medicals referencing the specified lot.
	 * @throws OHException if an error occurs retrieving the referencing medicals.
	 */
	public List<Integer> getMedicalsFromLot(DbQueryLogger dbQuery, String lotCode) throws OHException {
		try {
			List<Object> parameters = Collections.<Object>singletonList(lotCode);
			String query = "select distinct MDSR_ID from " +
					"((MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) " +
					"join MEDICALDSR  on MMV_MDSR_ID=MDSR_ID ) " +
					"join MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A where LT_ID_A=?";
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, false);

			List<Integer> medicalIds = new ArrayList<Integer>();
			while(resultSet.next()) medicalIds.add(resultSet.getInt("MDSR_ID"));

			return medicalIds;
		} catch (SQLException e) {
			dbQuery.rollback();
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
		}
	}

	/**
	 * Retrieves lot referred to the specified {@link Medical}.
	 * @param medical the medical.
	 * @return a list of {@link Lot}.
	 * @throws OHException if an error occurs retrieving the lot list.
	 */
	public ArrayList<Lot> getLotsByMedical(Medical medical) throws OHException {
		ArrayList<Lot> lots = null;
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			List<Object> parameters = Collections.<Object>singletonList(medical.getCode());
			String chargeQuery = "select LT_ID_A,LT_PREP_DATE,LT_DUE_DATE,SUM(MMV_QTY) as qty,LT_COST from "
					+ "((MEDICALDSRLOT join MEDICALDSRSTOCKMOV on MMV_LT_ID_A=LT_ID_A) join MEDICALDSR on MMV_MDSR_ID=MDSR_ID)"
					+ " join MEDICALDSRSTOCKMOVTYPE on MMV_MMVT_ID_A=MMVT_ID_A "
					+ "where (MDSR_ID=? and MMVT_TYPE='+') group by LT_ID_A order by LT_DUE_DATE";

			ResultSet resultSet = dbQuery.getDataWithParams(chargeQuery, parameters, false);

			lots = new ArrayList<Lot>(resultSet.getFetchSize());

			while (resultSet.next()) {
				Lot lot = toLot(resultSet);
				lot.setQuantity(resultSet.getInt("qty"));
				lots.add(lot);
			}

			for (Lot lot : lots) {
				parameters = Collections.<Object>singletonList(lot.getCode());
				String dischargeQuery = "select SUM(MMV_QTY) as qty2 from "
						+ "MEDICALDSRSTOCKMOV join MEDICALDSRSTOCKMOVTYPE on MMV_MMVT_ID_A=MMVT_ID_A "
						+ "where (MMVT_TYPE='-') and (MMV_LT_ID_A=?) group by MMV_LT_ID_A order by MMV_QTY desc";
				ResultSet resultSet2 = dbQuery.getDataWithParams(dischargeQuery, parameters, false);
				resultSet2.next();
				if (resultSet2.first())	lot.setQuantity(lot.getQuantity()	- resultSet2.getInt("qty2"));
			}

			//remove empty lots
			ArrayList<Lot> emptyLots = new ArrayList<Lot>();
			for (Lot aLot : lots) {
				if (aLot.getQuantity() == 0)
					emptyLots.add(aLot);
			}
			lots.removeAll(emptyLots);

			dbQuery.commit();

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return lots;
	}
	
	/**
	 * returns the date of the last movement
	 * @return 
	 * @throws OHException
	 */
	public GregorianCalendar getLastMovementDate() throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		GregorianCalendar gc = new GregorianCalendar();
		String sqlString = "SELECT MAX(MMV_DATE) AS DATE FROM MEDICALDSRSTOCKMOV";
		ResultSet resultSet = dbQuery.getData(sqlString, true);
		try {
			while (resultSet.next()) {
				Date date = resultSet.getDate("DATE");
				if (date != null) gc.setTime(date);
				else return null; //no records
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return gc;
	}
	
	/**
	 * check if the reference number is already used
	 * @return <code>true</code> if is already used, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean refNoExists(String refNo) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			List<Object> parameters = Collections.<Object>singletonList(refNo);
			String query = "SELECT MMV_REFNO FROM MEDICALDSRSTOCKMOV WHERE MMV_REFNO LIKE ?";
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			return resultSet.next();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
	}

	/**
	 * Converts the specified {@link ResultSet} row into a {@link Lot} object.
	 * @param resultSet the result set.
	 * @return the resulting {@link Lot} object.
	 * @throws SQLException if an error occurs during the conversion.
	 */
	protected Lot toLot(ResultSet resultSet) throws SQLException
	{
		String code = resultSet.getString("LT_ID_A");
		GregorianCalendar preparationDate = toCalendar(resultSet.getDate("LT_PREP_DATE"));
		GregorianCalendar dueDate = toCalendar(resultSet.getDate("LT_DUE_DATE"));
		Double cost = resultSet.getDouble("LT_COST");
		return new Lot(code, preparationDate, dueDate, cost);
	}
	
	/**
	 * Converts a {@link ResultSet} row into an {@link Ward} object.
	 * @param resultSet the result set to read.
	 * @return the converted object.
	 * @throws SQLException if an error occurs.
	 */
	private Ward toWard(ResultSet resultSet) throws SQLException {
		Ward ward = new Ward(resultSet.getString("WRD_ID_A"), 
				resultSet.getString("WRD_NAME"), 
				resultSet.getString("WRD_TELE"),
				resultSet.getString("WRD_FAX"),
				resultSet.getString("WRD_EMAIL"),
				resultSet.getInt("WRD_NBEDS"),
				resultSet.getInt("WRD_NQUA_NURS"),
				resultSet.getInt("WRD_NDOC"),
				resultSet.getBoolean("WRD_IS_PHARMACY"), 
				resultSet.getBoolean("WRD_IS_MALE"),
				resultSet.getBoolean("WRD_IS_FEMALE"), 
				resultSet.getInt("WRD_LOCK"));
		return ward;
	}
	
	/**
	 * Converts a {@link GregorianCalendar} to a {@link Date}.
	 * @param calendar the calendar to convert.
	 * @return the converted value or <code>null</code> if the passed value is <code>null</code>.
	 */
	protected java.sql.Timestamp toDate(GregorianCalendar calendar)
	{
		if (calendar == null) return null;
		return new java.sql.Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * Converts the specified {@link java.sql.Date} to a {@link GregorianCalendar}.
	 * @param date the date to convert.
	 * @return the converted date.
	 */
	protected GregorianCalendar toCalendar(Date date){
		if (date == null) return null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
	
	/**
	 * Converts the specified time in milliseconds to a {@link GregorianCalendar}.
	 * @param date the date to convert.
	 * @return the converted date.
	 */
	protected GregorianCalendar toCalendar(Long time){
		if (time == null) return null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(time);
		return calendar;
	}

	/**
	 * Retrieves all the movement associated to the specified reference number.
	 * In case of error a message error is shown and a <code>null</code> value is returned.
	 * @param refNo the reference number.
	 * @return the retrieved movements.
	 * @throws OHException 
	 */
	public ArrayList<Movement> getMovementsByReference(String refNo) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Movement> movements = null;
		
		StringBuilder query = new StringBuilder("SELECT * FROM (");
		query.append("(MEDICALDSRSTOCKMOVTYPE join MEDICALDSRSTOCKMOV on MMVT_ID_A = MMV_MMVT_ID_A) ");
		query.append("JOIN (MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A=MDSRT_ID_A) on MMV_MDSR_ID=MDSR_ID ) ");
		query.append("LEFT JOIN MEDICALDSRLOT on MMV_LT_ID_A=LT_ID_A ");
		query.append("LEFT JOIN WARD ON MMV_WRD_ID_A = WRD_ID_A ");
		query.append("WHERE MMV_REFNO = ? ");
		query.append("ORDER BY MMV_DATE DESC, MMV_REFNO DESC");
		
		List<Object> parameters = Collections.<Object>singletonList(refNo);
		
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			movements = new ArrayList<Movement>(resultSet.getFetchSize());
			while (resultSet.next()) {
				
				MedicalType medicalType = new MedicalType(resultSet.getString("MDSRT_ID_A"), resultSet.getString("MDSRT_DESC"));
				Medical medical = new Medical(resultSet.getInt("MDSR_ID"), 
						medicalType, 
						resultSet.getString("MDSR_CODE"), 
						resultSet.getString("MDSR_DESC"), 
						resultSet.getDouble("MDSR_INI_STOCK_QTI"), 
						resultSet.getInt("MDSR_PCS_X_PCK"), 
						resultSet.getDouble("MDSR_MIN_STOCK_QTI"),
						resultSet.getDouble("MDSR_IN_QTI"),
						resultSet.getDouble("MDSR_OUT_QTI"),
						resultSet.getInt("MDSR_LOCK"));

				Ward ward = toWard(resultSet);
				
				Lot lot = toLot(resultSet);
				
				MovementType movementType = new MovementType(resultSet.getString("MMVT_ID_A"),
						resultSet.getString("MMVT_DESC"), 
						resultSet.getString("MMVT_TYPE"));
				
				Supplier supplier = new Supplier(resultSet.getInt("MMV_FROM"), resultSet.getString("SUP_NAME"), null, null, null, null, null, null);

				Movement movement = new Movement(medical, movementType, ward, lot,
						toCalendar(resultSet.getTimestamp("MMV_DATE").getTime()), 
						resultSet.getInt("MMV_QTY"), 
						supplier,
						resultSet.getString("MMV_REFNO"));
				
				movements.add(movement);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return movements;
	}
}
