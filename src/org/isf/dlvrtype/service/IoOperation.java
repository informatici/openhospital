package org.isf.dlvrtype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.dlvrtype.model.DeliveryType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

/**
 * The persistence class for the DeliveryType module.
 */
public class IoOperation {

	/**
	 * Returns all stored {@link DeliveryType}s.
	 * @return all stored delivery types.
	 * @throws OHException if an error occurs retrieving the delivery types. 
	 */
	public ArrayList<DeliveryType> getDeliveryType() throws OHException {
		ArrayList<DeliveryType> deliverytypes = null;
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			String query = "select * from DELIVERYTYPE order by DLT_DESC";
			ResultSet resultSet = dbQuery.getData(query,true);
			deliverytypes = new ArrayList<DeliveryType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				deliverytypes.add(new DeliveryType(resultSet.getString("DLT_ID_A"), resultSet.getString("DLT_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return deliverytypes;
	}

	/**
	 * Updates the specified {@link DeliveryType}.
	 * @param deliveryType the delivery type to update.
	 * @return <code>true</code> if the delivery type has been update.
	 * @throws OHException if an error occurs during the update operation.
	 */
	public boolean updateDeliveryType(DeliveryType deliveryType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(3);
			parameters.add(deliveryType.getCode());
			parameters.add(deliveryType.getDescription());
			parameters.add(deliveryType.getCode());
			String query = "update DELIVERYTYPE set DLT_ID_A=? , DLT_DESC=? where DLT_ID_A=?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Stores the specified {@link DeliveryType}.
	 * @param deliveryType the delivery type to store.
	 * @return <code>true</code> if the delivery type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the store operation.
	 */
	public boolean newDeliveryType(DeliveryType deliveryType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(deliveryType.getCode());
			parameters.add(deliveryType.getDescription());
			String query = "insert into DELIVERYTYPE (DLT_ID_A,DLT_DESC) values (?,?)";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Delete the specified {@link DeliveryType}.
	 * @param deliveryType the delivery type to delete.
	 * @return <code>true</code> if the delivery type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the delete operation.
	 */
	public boolean deleteDeliveryType(DeliveryType deliveryType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = Collections.<Object>singletonList(deliveryType.getCode());
			String query = "delete from DELIVERYTYPE where DLT_ID_A=?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Checks if the specified code is already used by others {@link DeliveryType}s.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "SELECT DLT_ID_A FROM DELIVERYTYPE where DLT_ID_A=?";
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
