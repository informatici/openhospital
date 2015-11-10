package org.isf.ward.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.springframework.stereotype.Component;

/**
 * This class offers the io operations for recovering and managing
 * ward records from the database
 * 
 * @author Rick
 * 
 */
@Component
public class WardIoOperations {
	
	/**
	 * Retrieves the number of patients currently admitted in the {@link Ward}
	 * @param ward - the ward
	 * @return the number of patients currently admitted
	 * @throws OHException
	 */
	public int getCurrentOccupation(Ward ward) throws OHException {
		int n = 0;
		List<Object> params = Collections.<Object>singletonList(ward.getCode());
		String string = "SELECT COUNT(*) FROM admission WHERE ADM_IN = 1 AND ADM_WRD_ID_A = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet result = dbQuery.getDataWithParams(string, params, true);
			result.next();
			n = result.getInt(1);
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return n;
	}
	
	/**
	 * Retrieves all stored {@link Ward}s with flag maternity equals <code>false</code>.
	 * @return the retrieved wards.
	 * @throws OHException if an error occurs retrieving the diseases.
	 */
	public ArrayList<Ward> getWardsNoMaternity() throws OHException {
		ArrayList<Ward> wards = null;
		String string = "SELECT * FROM WARD WHERE WRD_ID_A <> 'M' ";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(string, true);
			wards = new ArrayList<Ward>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Ward ward = toWard(resultSet);
				wards.add(ward);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return wards;
	}
	
	/**
	 * Retrieves all stored {@link Ward}s with the specified ward ID.
	 * @param wardID - the ward ID, can be <code>null</code>
	 * @return the retrieved wards.
	 * @throws OHException if an error occurs retrieving the wards.
	 */
	public ArrayList<Ward> getWards(String wardID) throws OHException {
		ArrayList<Ward> wards = null;
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder sqlString = new StringBuilder("SELECT * FROM WARD");
		
		if (wardID != null && wardID.trim().length() > 0) {
			sqlString.append(" WHERE WRD_ID_A LIKE ?");
			params.add(wardID);
		}
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(sqlString.toString(), params, true);
			wards = new ArrayList<Ward>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Ward ward = toWard(resultSet);
				wards.add(ward);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return wards;
	}
	
	/**
	 * Stores the specified {@link Ward}. 
	 * @param ward the ward to store.
	 * @return <code>true</code> if the ward has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the ward.
	 */
	public boolean newWard(Ward ward) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String sqlstring = "INSERT INTO WARD (WRD_ID_A, WRD_NAME, WRD_TELE, WRD_FAX, WRD_EMAIL," +
					" WRD_NBEDS, WRD_NQUA_NURS, WRD_NDOC, WRD_IS_PHARMACY, WRD_IS_MALE, WRD_IS_FEMALE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			Object[] objects = {ward.getCode(),ward.getDescription(),ward.getTelephone(),ward.getFax(),ward.getEmail(),ward.getBeds(),ward.getNurs(),ward.getDocs(),ward.isPharmacy()? 1:0,ward.isMale()? 1:0,ward.isFemale()? 1:0};
			List<Object> params = Arrays.asList(objects);
			result = dbQuery.setDataWithParams(sqlstring, params, true);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * Updates the specified {@link Ward}.
	 * @param disease the {@link Ward} to update.
	 * @param isConfirmedOverwriteRecord if the user has confirmed she wants to overwrite the record
	 * @return <code>true</code> if the ward has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateWard(Ward ward,boolean isConfirmedOverwriteRecord) throws OHException{
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		List<Object> params = null;
		String sqlstring = null;
		if(isCodePresent(ward.getCode())) {			//ok the record is present, it was not deleted
			try {
				//we establish if someone else has updated/deleted the record since the 
				Object[] objects = {ward.getDescription(),ward.getTelephone(),ward.getFax(),ward.getEmail(),ward.getBeds(),ward.getNurs(),ward.getDocs(),ward.isPharmacy()? 1:0,ward.getCode()};
				params = Arrays.asList(objects);
				if(isLockWard(ward)){		//ok it was not updated
					sqlstring = "UPDATE WARD SET " +
					" WRD_NAME = ?," +
					" WRD_TELE = ?," +
					" WRD_FAX = ?," +
					" WRD_EMAIL = ?," +
					" WRD_NBEDS = ?," +
					" WRD_NQUA_NURS = ?," +
					" WRD_NDOC = ?," +
					" WRD_IS_PHARMACY = ?," +
					" WRD_LOCK = WRD_LOCK + 1 " +
					" WHERE WRD_ID_A = ?";
					result = dbQuery.setDataWithParams(sqlstring,params,false);
					if (result) ward.setLock(ward.getLock()+1);
				} else {							//it was updated by someone else
					if (isConfirmedOverwriteRecord){
						//the user has confirmed she wants to overwrite the record
						sqlstring = "UPDATE WARD SET " +
						" WRD_NAME = ?," +
						" WRD_TELE = ?," +
						" WRD_FAX = ?," +
						" WRD_EMAIL = ?," +
						" WRD_NBEDS = ?," +
						" WRD_NQUA_NURS = ?," +
						" WRD_NDOC = ?," +
						" WRD_IS_PHARMACY = ?," +
						" WRD_LOCK = WRD_LOCK + 1 " +
						" WHERE WRD_ID_A = ?";
						result = dbQuery.setDataWithParams(sqlstring,params,false);
						if (result) ward.setLock(ward.getLock()+1);
					}
				}			
				dbQuery.commit();
			} finally{
				dbQuery.releaseConnection();
			}
		} else {				//the record was deleted since the last read
			throw new OHException(MessageBundle.getMessage("angal.ward.thedataisnomorepresent"));
		}
		return result;
	}
	
	/**
	 * Mark as deleted the specified {@link Ward}.
	 * @param ward the ward to make delete.
	 * @return <code>true</code> if the ward has been marked, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the delete operation.
	 */
	public boolean deleteWard(Ward ward) throws OHException{
		String string = "DELETE FROM WARD WHERE WRD_ID_A = ?";
		List<Object> params = Collections.<Object>singletonList(ward.getCode());
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			result = dbQuery.setDataWithParams(string,params,true);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * Check if the specified code is used by other {@link Ward}s.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present=false;
		try {
			String sqlstring = "SELECT WRD_ID_A FROM WARD WHERE WRD_ID_A = ?";
			List<Object> params = Collections.<Object>singletonList(code);
			ResultSet set = dbQuery.getDataWithParams(sqlstring, params, false);
			if(set.first()){
				present=true;
			}
			dbQuery.commit();					
		} catch (SQLException e) {					
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
	
	
	/**
	 * Check if the maternity ward exist
	 * @return <code>true</code> if is exist, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isMaternityPresent() throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present=false;
		try {
			String sqlstring = "SELECT WRD_ID_A FROM WARD WHERE WRD_ID_A = 'M'";
			ResultSet set = dbQuery.getData(sqlstring,false);
			if(set.first()){
				present=true;
			}
			dbQuery.commit();
			
		} catch (SQLException e) {			
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
	
	/**
	 * Check if the ward is locked
	 * @return <code>true</code> if the ward is locked <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isLockWard(Ward ward) throws OHException {
		boolean isLockWard = false;
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			String sqlstring = "SELECT WRD_LOCK FROM WARD WHERE WRD_ID_A = ? ";
			List<Object> params = Collections.<Object>singletonList(ward.getCode());
			ResultSet set = dbQuery.getDataWithParams(sqlstring,params,false);
			if(set.first()){
				isLockWard = (set.getInt("WRD_LOCK")==ward.getLock());
			}
		} catch (SQLException e) {			
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return isLockWard;
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
}


