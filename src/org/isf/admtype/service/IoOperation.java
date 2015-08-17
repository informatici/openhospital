package org.isf.admtype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.admtype.model.AdmissionType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

/**
 * Persistence class for admtype module.
 */
public class IoOperation {

	/**
	 * Returns all the available {@link AdmissionType}s.
	 * @return a list of admission types.
	 * @throws OHException if an error occurs.
	 */
	public ArrayList<AdmissionType> getAdmissionType() throws OHException {
		ArrayList<AdmissionType> padmissiontype = null;
		String query = "select * from ADMISSIONTYPE order by ADMT_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getData(query,true);
			padmissiontype = new ArrayList<AdmissionType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				padmissiontype.add(new AdmissionType(resultSet.getString("ADMT_ID_A"), resultSet.getString("ADMT_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return padmissiontype;
	}

	/**
	 * Updates the specified {@link AdmissionType}.
	 * @param admissionType the admission type to update.
	 * @return <code>true</code> if the admission type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateAdmissionType(AdmissionType admissionType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(3);
			parameters.add(admissionType.getCode());
			parameters.add(admissionType.getDescription());
			parameters.add(admissionType.getCode());
			String query = "update ADMISSIONTYPE set ADMT_ID_A=? , ADMT_DESC=? where ADMT_ID_A= ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Stores a new {@link AdmissionType}.
	 * @param admissionType the admission type to store.
	 * @return <code>true</code> if the admission type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the storing operation.
	 */
	public boolean newAdmissionType(AdmissionType admissionType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(admissionType.getCode());
			parameters.add(admissionType.getDescription());
			String query = "insert into ADMISSIONTYPE (ADMT_ID_A,ADMT_DESC) values (?,?)";				
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Deletes the specified {@link AdmissionType}.
	 * @param admissionType the admission type to delete.
	 * @return <code>true</code> if the admission type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete operation.
	 */
	public boolean deleteAdmissionType(AdmissionType admissionType) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(admissionType.getCode());
		String query = "delete from ADMISSIONTYPE where ADMT_ID_A = ?";
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
	 * Checks if the specified Code is already used by others {@link AdmissionType}s.
	 * @param code the admission type code to check.
	 * @return <code>true</code> if the code is already used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present=false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "SELECT ADMT_ID_A FROM ADMISSIONTYPE where ADMT_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			present = set.first();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
}
