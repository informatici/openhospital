package org.isf.medicalstockward.service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.medtype.model.MedicalType;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;

/**
 * @author mwithi
 */
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
		
		String query = "SELECT * FROM ((((MEDICALDSRSTOCKMOVWARD LEFT JOIN " +
						"(PATIENT LEFT JOIN (SELECT PEX_PAT_ID, PEX_HEIGHT AS PAT_HEIGHT, PEX_WEIGHT AS PAT_WEIGHT FROM PATIENTEXAMINATION GROUP BY PEX_PAT_ID ORDER BY PEX_DATE DESC) AS HW ON PAT_ID = HW.PEX_PAT_ID) ON MMVN_PAT_ID = PAT_ID) JOIN " +
						"WARD ON MMVN_WRD_ID_A = WRD_ID_A)) JOIN " +
						"MEDICALDSR ON MMVN_MDSR_ID = MDSR_ID) JOIN " +
						"MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A ";
		if (wardId!=null || dateFrom!=null || dateTo!=null) 
		{
			query += "WHERE ";
		}
		if (wardId != null && !wardId.equals("")) 
		{
			if (params.size() != 0) 
			{
				query += "AND ";
			}
			query += "WRD_ID_A = ? ";
			params.add(wardId);
		}
		if ((dateFrom != null) && (dateTo != null)) 
		{
			if (params.size() != 0) 
			{
				query += "AND ";
			}
			query += "MMVN_DATE > ? AND MMVN_DATE < ?";
			params.add(dateFrom);
			params.add(dateTo);
		}
				
		jpa.createQuery(query, MovementWard.class, false);
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
			query = "SELECT SUM(MMV_QTY) MAIN FROM MEDICALDSRSTOCKMOV M WHERE MMV_MMVT_ID_A = 'discharge' AND MMV_MDSR_ID = ? ";
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

		return mainQuantity;	
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

		return dischargeQuantity;	
	}

	/**
	 * Stores the specified {@link Movement}.
	 * @param movement the movement to store.
	 * @return <code>true</code> if has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */
	
	
	
	
	
	public boolean newMovementWard(MovementWard movement) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			boolean stored = newMovementWard(dbQuery, movement);
			dbQuery.commit();
			return stored;
		} finally {
			dbQuery.releaseConnection();
		}
	}

	/**
	 * Stores the specified {@link Movement} list.
	 * @param movements the movement to store.
	 * @return <code>true</code> if the movements have been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */
	public boolean newMovementWard(ArrayList<MovementWard> movements) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			for (MovementWard movement:movements) {

				boolean inserted = newMovementWard(dbQuery, movement);
				if (!inserted) {
					dbQuery.rollback();
					return false;
				}
			}
			dbQuery.commit();
			return true;
		} finally {
			dbQuery.releaseConnection();
		}
	}

	/**
	 * Stores the specified {@link MovementWard}.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param movement the movement ward to store.
	 * @return <code>true</code> if has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs.
	 */
	protected boolean newMovementWard(DbQueryLogger dbQuery, MovementWard movement) throws OHException {
		String query = "INSERT INTO MEDICALDSRSTOCKMOVWARD (MMVN_WRD_ID_A, MMVN_DATE, MMVN_IS_PATIENT, MMVN_PAT_ID, MMVN_PAT_AGE, MMVN_PAT_WEIGHT, MMVN_DESC, MMVN_MDSR_ID, MMVN_MDSR_QTY, MMVN_MDSR_UNITS) VALUES (?,?,?,?,?,?,?,?,?,?)";
		List<Object> parameters = new ArrayList<Object>(10);

		parameters.add(movement.getWard().getCode());
		parameters.add(toTimestamp(new GregorianCalendar()));
		parameters.add(movement.isPatient());
		if (movement.isPatient()) {
			parameters.add(movement.getPatient().getCode());
			parameters.add(movement.getPatient().getAge());
			parameters.add(movement.getPatient().getWeight());
		} else {
			parameters.add("0");
			parameters.add(0);
			parameters.add(0);
		}
		parameters.add(movement.getDescription());
		parameters.add(movement.getMedical().getCode().toString());
		parameters.add(movement.getQuantity());
		parameters.add(movement.getUnits());

		boolean inserted = dbQuery.setDataWithParams(query, parameters, false);

		if (inserted) {
			boolean updated = updateStockWardQuantity(dbQuery, movement);
			return updated;
		}
		return false;
	}

	/**
	 * Updates the quantity for the specified movement ward.
	 * @param dbQuery the {@link DbQueryLogger} to use.
	 * @param movement the movement ward to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	protected boolean updateStockWardQuantity(DbQueryLogger dbQuery, MovementWard movement) throws OHException {

		List<Object> parameters = new ArrayList<Object>(3);
		String query = "UPDATE MEDICALDSRWARD SET MDSRWRD_OUT_QTI = MDSRWRD_OUT_QTI + ? WHERE MDSRWRD_WRD_ID_A = ? AND MDSRWRD_MDSR_ID = ?";
		parameters.add(movement.getQuantity());
		parameters.add(movement.getWard().getCode());
		parameters.add(movement.getMedical().getCode());

		boolean updated = dbQuery.setDataWithParams(query, parameters, false);

		if (!updated) dbQuery.rollback();

		return updated;
	}

	/**
	 * Updates the specified {@link MovementWard}.
	 * @param movement the movement ward to update.
	 * @return <code>true</code> if has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMovementWard(MovementWard movement) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {

			List<Object> parameters = new ArrayList<Object>(11);
			String query = "UPDATE MEDICALDSRSTOCKMOVWARD SET MMVN_WRD_ID_A = ?, " +
					"MMVN_DATE = ?, " +
					"MMVN_IS_PATIENT = ?, " +
					"MMVN_PAT_ID = ?, " +
					"MMVN_PAT_AGE = ?, " +
					"MMVN_PAT_WEIGHT = ?, " +
					"MMVN_DESC = ?, " +
					"MMVN_MDSR_ID = ?, " +
					"MMVN_MDSR_QTY = ?, " +
					"MMVN_MDSR_UNITS = ? " +
					"WHERE MMVN_ID = ?";

			parameters.add(movement.getWard().getCode());
			parameters.add(toTimestamp(movement.getDate()));
			parameters.add(movement.isPatient());
			if (movement.isPatient()) parameters.add(movement.getPatient().getCode().toString());
			else parameters.add("0");
			parameters.add( movement.getAge());
			parameters.add(movement.getWeight());
			parameters.add(movement.getDescription());
			parameters.add(movement.getMedical().getCode().toString());
			parameters.add(movement.getQuantity());
			parameters.add(movement.getUnits());
			parameters.add(movement.getCode());

			boolean updated = dbQuery.setDataWithParams(query, parameters, true);
			return updated;

		} finally {
			dbQuery.releaseConnection();
		}
	}

	/**
	 * Deletes the specified {@link MovementWard}.
	 * @param movement the movement ward to delete.
	 * @return <code>true</code> if the movement has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete.
	 */
	public boolean deleteMovementWard(MovementWard movement) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();

		try {
			List<Object> parameters = Collections.<Object>singletonList(movement.getCode());
			String query = "DELETE FROM MEDICALDSRSTOCKMOVWARD WHERE MMVN_ID = ?";

			boolean deleted = dbQuery.setDataWithParams(query, parameters, true);

			return deleted;

		} finally {
			dbQuery.releaseConnection();
		}
	}

	/**
	 * Gets all the {@link Medical}s associated to specified {@link Ward}.
	 * @param wardId the ward id.
	 * @return the retrieved medicals.
	 * @throws OHException if an error occurs during the medical retrieving.
	 */
	public ArrayList<MedicalWard> getMedicalsWard(String wardId) throws OHException {

		ArrayList<MedicalWard> medicalWards = new ArrayList<MedicalWard>();

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			List<Object> parameters = Collections.<Object>singletonList(wardId);
			StringBuilder query = new StringBuilder("SELECT MEDICALDSR.*, MDSRT_ID_A, MDSRT_DESC, MDSRWRD_WRD_ID_A, MDSRWRD_IN_QTI - MDSRWRD_OUT_QTI AS QTY ");
			query.append("FROM (MEDICALDSRWARD JOIN MEDICALDSR ON MDSRWRD_MDSR_ID = MDSR_ID) JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A ");
			query.append("WHERE MDSRWRD_WRD_ID_A = ? ");
			query.append("ORDER BY MDSR_DESC");


			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);

			while (resultSet.next()) {
				Medical medical = toMedical(resultSet);
				MedicalWard medicalWard = new MedicalWard(medical, resultSet.getDouble("QTY"));
				medicalWards.add(medicalWard);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}

		return medicalWards;
	}

	/**
	 * Converts a {@link GregorianCalendar} to a {@link Timestamp}.
	 * @param calendar the calendar to convert.
	 * @return the converted value or <code>null</code> if the passed value is <code>null</code>.
	 */
	protected Timestamp toTimestamp(GregorianCalendar calendar)
	{
		if (calendar == null) return null;
		return new Timestamp(calendar.getTime().getTime());
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
	 * Converts the specified {@link java.sql.Timestamp} to a {@link GregorianCalendar}.
	 * @param timestamp the timestamp to convert.
	 * @return the converted timestamp.
	 */
	public GregorianCalendar toCalendar(Timestamp timestamp) {
		if (timestamp == null) return null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(timestamp);
		return calendar;
	}
}
