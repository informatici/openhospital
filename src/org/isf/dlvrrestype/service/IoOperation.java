package org.isf.dlvrrestype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.dlvrrestype.model.DeliveryResultType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

/**
 * Persistence class for DeliveryResultType module.
 */
public class IoOperation {

	/**
	 * Returns all stored {@link DeliveryResultType}s.
	 * @return the stored {@link DeliveryResultType}s.
	 * @throws OHException if an error occurs retrieving the stored delivery result types.
	 */
	public ArrayList<DeliveryResultType> getDeliveryResultType() throws OHException {
		ArrayList<DeliveryResultType> deliveryresulttypes = null;
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			String query = "select * from DELIVERYRESULTTYPE order by DRT_DESC";
			ResultSet resultSet = dbQuery.getData(query,true);
			deliveryresulttypes = new ArrayList<DeliveryResultType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				deliveryresulttypes.add(new DeliveryResultType(resultSet.getString("DRT_ID_A"), resultSet.getString("DRT_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return deliveryresulttypes;
	}

	/**
	 * Updates the specified {@link DeliveryResultType}.
	 * @param deliveryresultType the delivery result type to update.
	 * @return <code>true</code> if the delivery result type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateDeliveryResultType(DeliveryResultType deliveryresultType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(3);
			parameters.add(deliveryresultType.getCode());
			parameters.add(deliveryresultType.getDescription());
			parameters.add(deliveryresultType.getCode());
			String query = "update DELIVERYRESULTTYPE set DRT_ID_A=? , DRT_DESC=? where DRT_ID_A=?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Stores the specified {@link DeliveryResultType}.
	 * @param deliveryresultType the delivery result type to store.
	 * @return <code>true</code> if the delivery result type has been stored. 
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newDeliveryResultType(DeliveryResultType deliveryresultType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(deliveryresultType.getCode());
			parameters.add(deliveryresultType.getDescription());
			String query = "insert into DELIVERYRESULTTYPE (DRT_ID_A,DRT_DESC) values (?,?)";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Deletes the specified {@link DeliveryResultType}.
	 * @param deliveryresultType the delivery result type to delete.
	 * @return <code>true</code> if the delivery result type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete operation.
	 */
	public boolean deleteDeliveryResultType(DeliveryResultType deliveryresultType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = Collections.<Object>singletonList(deliveryresultType.getCode());
			String query = "delete from DELIVERYRESULTTYPE where DRT_ID_A = ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Checks if the specified code is already used by others {@link DeliveryResultType}s.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "SELECT DRT_ID_A FROM DELIVERYRESULTTYPE where DRT_ID_A = ?";
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
