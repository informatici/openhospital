package org.isf.pregtreattype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.pregtreattype.model.PregnantTreatmentType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperation {

	/**
	 * return the list of {@link PregnantTreatmentType}s
	 * 
	 * @return the list of {@link PregnantTreatmentType}s
	 * @throws OHException 
	 */
	public ArrayList<PregnantTreatmentType> getPregnantTreatmentType() throws OHException {
		ArrayList<PregnantTreatmentType> pregnantTreatmentTypeList = null;
		String string = "SELECT * FROM PREGNANTTREATMENTTYPE ORDER BY PTT_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
			try {
				
				ResultSet resultSet = dbQuery.getData(string, true);
				pregnantTreatmentTypeList = new ArrayList<PregnantTreatmentType>(resultSet.getFetchSize());
				while (resultSet.next()) {
					pregnantTreatmentTypeList.add(new PregnantTreatmentType(resultSet.getString("PTT_ID_A"), resultSet.getString("PTT_DESC")));
				}
				
			} catch (SQLException e) {
				throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
			} finally{
				dbQuery.releaseConnection();
			}
		return pregnantTreatmentTypeList;
	}
	
	/**
	 * insert a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newPregnantTreatmentType(PregnantTreatmentType pregnantTreatmentType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlString = "INSERT INTO PREGNANTTREATMENTTYPE (PTT_ID_A, PTT_DESC) values (?, ?)";
			parameters.add(pregnantTreatmentType.getCode());
			parameters.add(pregnantTreatmentType.getDescription());
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * update a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updatePregnantTreatmentType(PregnantTreatmentType pregnantTreatmentType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlString = "UPDATE PREGNANTTREATMENTTYPE SET PTT_DESC = ? WHERE PTT_ID_A = ?";
			parameters.add(pregnantTreatmentType.getDescription());
			parameters.add(pregnantTreatmentType.getCode());
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * delete a {@link PregnantTreatmentType} in the DB
	 * 
	 * @param pregnantTreatmentType - the {@link PregnantTreatmentType} to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deletePregnantTreatmentType(PregnantTreatmentType pregnantTreatmentType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(pregnantTreatmentType.getCode());
		String sqlString = "DELETE FROM PREGNANTTREATMENTTYPE WHERE PTT_ID_A = ?";
		boolean result = false;
		try {
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * check if the code is already in use
	 * 
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(code);
		boolean present = false;
		try {
			String sqlString = "SELECT PTT_ID_A FROM PREGNANTTREATMENTTYPE where PTT_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(sqlString, parameters, true);
			if(set.first()) present = true;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
}
