package org.isf.operation.service;

/*----------------------------------------------------------
 * modification history
 * ====================
 * 13/02/09 - Alex - modified query for ordering resultset
 *                   by description only
 * 13/02/09 - Alex - added Major/Minor control
 -----------------------------------------------------------*/

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.operation.model.Operation;
import org.isf.opetype.model.OperationType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

/**
 * This class offers the io operations for recovering and managing
 * operations records from the database
 * 
 * @author Rick, Vero, pupo
 */
public class IoOperations {
	
	/**
	 * return the {@link Operation}s whose type matches specified string
	 * 
	 * @param typeDescription - a type description
	 * @return the list of {@link Operation}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHException 
	 */
	public ArrayList<Operation> getOperation(String typeDescription) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Operation> operationList = null;
		ResultSet resultSet;
		
		if (typeDescription == null) {
			String sqlString = "SELECT * FROM OPERATION JOIN OPERATIONTYPE ON OPE_OCL_ID_A = OCL_ID_A ORDER BY OPE_DESC";
			resultSet = dbQuery.getData(sqlString,true);
		} else {
			String sqlString = "SELECT * FROM OPERATION JOIN OPERATIONTYPE ON OPE_OCL_ID_A = OCL_ID_A WHERE OCL_DESC LIKE CONCAT('%', ? , '%') ORDER BY OPE_DESC";
			List<Object> parameters = Collections.<Object>singletonList(typeDescription);
			resultSet = dbQuery.getDataWithParams(sqlString, parameters, true);
		}
		try {
			operationList = new ArrayList<Operation>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Operation operation = new Operation(
						resultSet.getString("OPE_ID_A"), 
						resultSet.getString("OPE_DESC"), 
						new OperationType(
								resultSet.getString("OPE_OCL_ID_A"),
								resultSet.getString("OCL_DESC")),
						resultSet.getInt("OPE_STAT"), 
						resultSet.getInt("OPE_LOCK"));
				operationList.add(operation);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		}
		finally{
			dbQuery.releaseConnection();
		}
		return operationList;
	}
	
	/**
	 * insert an {@link Operation} in the DB
	 * 
	 * @param operation - the {@link Operation} to insert
	 * @return <code>true</code> if the operation has been inserted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean newOperation(Operation operation) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		String sqlString = "INSERT INTO OPERATION (OPE_ID_A, OPE_OCL_ID_A, OPE_DESC, OPE_STAT) VALUES (?, ?, ?, ?)";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(operation.getCode());
		parameters.add(operation.getType().getCode());
		parameters.add(operation.getDescription());
		parameters.add(operation.getMajor());
		
		return dbQuery.setDataWithParams(sqlString, parameters, true);
	}
	
	/**
	 * Checks if the specified {@link Operation} has been modified.
	 * @param operation - the {@link Operation} to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean hasOperationModified(Operation operation) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;

		// we establish if someone else has updated/deleted the record since the last read
		String query = "SELECT OPE_LOCK FROM OPERATION WHERE OPE_ID_A = ?";
		List<Object> parameters = Collections.<Object>singletonList(operation.getCode());

		try {
			// we use manual commit of the transaction
			ResultSet resultSet =  dbQuery.getDataWithParams(query, parameters, true);
			if (resultSet.first()) { 
				// ok the record is present, it was not deleted
				result = resultSet.getInt("OPE_LOCK") != operation.getLock();
			} else {
				throw new OHException(MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/** 
	 * updates an {@link Operation} in the DB
	 * 
	 * @param operation - the {@link Operation} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateOperation(Operation operation) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		
		try {
			String sqlString = "UPDATE OPERATION set " +
			" OPE_DESC = ?," +
			" OPE_LOCK = OPE_LOCK + 1, " +
			" OPE_STAT = ?" +
			" WHERE OPE_ID_A = ?";
			
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(operation.getDescription());
			parameters.add(operation.getMajor());
			parameters.add(operation.getCode());
			
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
			if (result) operation.setLock(operation.getLock()+1);
			
		}finally{
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/** 
	 * Delete a {@link Operation} in the DB
	 * @param operation - the {@link Operation} to delete
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteOperation(Operation operation) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		String sqlString = "DELETE FROM OPERATION WHERE OPE_ID_A = ?";
		List<Object> parameters = Collections.<Object>singletonList(operation.getCode());
		
		return dbQuery.setDataWithParams(sqlString, parameters, true);
	}
	
	/**
	 * checks if an {@link Operation} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try {
			String sqlstring = "SELECT OPE_ID_A FROM OPERATION WHERE OPE_ID_A = ? ";
			List<Object> parameters = Collections.<Object>singletonList(code);
			ResultSet set = dbQuery.getDataWithParams(sqlstring, parameters, true);
			if(set.first()) present = true;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
	
	/**
	 * checks if an {@link Operation} description has already been used within the specified {@link OperationType} 
	 * 
	 * @param description - the {@link Operation} description
	 * @param typeCode - the {@link OperationType} code
	 * @return <code>true</code> if the description is already in use, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean isDescriptionPresent(String description, String typeCode) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present=false;
		try {
			String sqlstring = "SELECT OPE_DESC FROM OPERATION WHERE OPE_DESC = ? AND OPE_OCL_ID_A = ?";
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(description);
			parameters.add(typeCode);
			ResultSet set = dbQuery.getDataWithParams(sqlstring, parameters, true);
			if(set.first()) present=true;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
}


