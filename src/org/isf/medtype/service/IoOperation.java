package org.isf.medtype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.medtype.model.MedicalType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

/**
 * Persistence class for the medical type module.
 */
public class IoOperation {


	/**
	 * Retrieves all the stored {@link MedicalType}s.
	 * @return the stored medical types.
	 * @throws OHException if an error occurs retrieving the medical types.
	 */
	public ArrayList<MedicalType> getMedicalTypes() throws OHException {
		ArrayList<MedicalType> medicaltypes = null;
		String query = "SELECT MDSRT_ID_A,MDSRT_DESC FROM MEDICALDSRTYPE ORDER BY MDSRT_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();

		try{
			ResultSet resultSet = dbQuery.getData(query,true);
			medicaltypes = new ArrayList<MedicalType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				medicaltypes.add(new MedicalType(resultSet.getString("MDSRT_ID_A"), resultSet.getString("MDSRT_DESC")));
			}

		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return medicaltypes;
	}

	/**
	 * Updates the specified {@link MedicalType}.
	 * @param medicalType the medical type to update.
	 * @return <code>true</code> if the medical type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs updating the medical type.
	 */
	public boolean updateMedicalType(MedicalType medicalType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(3);
			parameters.add(medicalType.getCode());
			parameters.add(medicalType.getDescription());
			parameters.add(medicalType.getCode());

			String query = "UPDATE MEDICALDSRTYPE SET " +
					"MDSRT_ID_A = ?,"+
					"MDSRT_DESC = ? "+
					"WHERE MDSRT_ID_A= ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}	
		return result;
	}

	/**
	 * Stores the specified {@link MedicalType}.
	 * @param medicalType the medical type to store.
	 * @return <code>true</code> if the medical type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the new medical type.
	 */
	public boolean newMedicalType(MedicalType medicalType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(medicalType.getCode());
			parameters.add(medicalType.getDescription());
			String query = "INSERT INTO MEDICALDSRTYPE (MDSRT_ID_A,MDSRT_DESC) VALUES (?,?)";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Deletes the specified {@link MedicalType}.
	 * @param medicalType the medical type to delete.
	 * @return <code>true</code> if the medical type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs deleting the medical type.
	 */
	public boolean deleteMedicalType(MedicalType medicalType) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(medicalType.getCode());
		String query = "DELETE FROM MEDICALDSRTYPE WHERE MDSRT_ID_A = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Checks if the specified {@link MedicalType} code is already stored.
	 * @param code the {@link MedicalType} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "SELECT MDSRT_ID_A FROM MEDICALDSRTYPE where MDSRT_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			if(set.first()) present = true;

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return present;
	}
}
