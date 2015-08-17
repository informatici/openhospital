package org.isf.opetype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.opetype.model.OperationType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperation {

	/**
	 * return the list of {@link OperationType}s
	 * 
	 * @return the list of {@link OperationType}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHException 
	 */
	public ArrayList<OperationType> getOperationType() throws OHException {
		ArrayList<OperationType> operationTypeList = null;
		String sqlString = "SELECT OCL_ID_A, OCL_DESC FROM OPERATIONTYPE ORDER BY OCL_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(sqlString, true);
			operationTypeList = new ArrayList<OperationType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				OperationType opType = new OperationType(
						resultSet.getString("OCL_ID_A"), 
						resultSet.getString("OCL_DESC"));
				operationTypeList.add(opType);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return operationTypeList;
	}
	
	/**
	 * insert an {@link OperationType} in the DB
	 * 
	 * @param operationType - the {@link OperationType} to insert
	 * @return <code>true</code> if the {@link OperationType} has been inserted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean newOperationType(OperationType operationType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String sqlString = "INSERT INTO OPERATIONTYPE (OCL_ID_A,OCL_DESC) VALUES (?, ?)";
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(operationType.getCode());
			parameters.add(operationType.getDescription());
			
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * update an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to update
	 * @return <code>true</code> if the {@link OperationType} has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateOperationType(OperationType operationType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String string = "UPDATE OPERATIONTYPE SET " +
			"OCL_DESC = ?" +
			"WHERE OCL_ID_A= ?";
			
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(operationType.getDescription());
			parameters.add(operationType.getCode());
			
			result = dbQuery.setDataWithParams(string, parameters, true);
			
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * delete an {@link OperationType}
	 * 
	 * @param operationType - the {@link OperationType} to delete
	 * @return <code>true</code> if the {@link OperationType} has been delete, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteOperationType(OperationType operationType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		String sqlString = "DELETE FROM OPERATIONTYPE WHERE OCL_ID_A = ?";
		List<Object> parameters = Collections.<Object>singletonList(operationType.getCode());
		
		try {
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * checks if an {@link OperationType} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try {
			String sqlString = "SELECT OCL_ID_A FROM OPERATIONTYPE WHERE OCL_ID_A = ?";
			List<Object> parameters = Collections.<Object>singletonList(code);
			ResultSet set = dbQuery.getDataWithParams(sqlString, parameters, true);
			if(set.first()) present=true;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return present;
	}
}
