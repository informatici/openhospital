package org.isf.pricesothers.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.pricesothers.model.PricesOthers;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperations {

	/**
	 * return the list of {@link PriceOthers}s in the DB
	 * 
	 * @return the list of {@link PriceOthers}s
	 * @throws OHException 
	 */
	public ArrayList<PricesOthers> getOthers() throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<PricesOthers> pricesOthers = null;
		String string = "SELECT * FROM PRICESOTHERS ORDER BY OTH_DESC";
		try {
			ResultSet resultSet = dbQuery.getData(string,true);
			pricesOthers = new ArrayList<PricesOthers>(resultSet.getFetchSize());
			while (resultSet.next()) {
				pricesOthers.add(new PricesOthers(resultSet.getInt("OTH_ID"),
									 resultSet.getString("OTH_CODE"),
									 resultSet.getString("OTH_DESC"),
									 resultSet.getBoolean("OTH_OPD_INCLUDE"),
									 resultSet.getBoolean("OTH_IPD_INCLUDE"),
									 resultSet.getBoolean("OTH_DAILY"),
									 resultSet.getBoolean("OTH_DISCHARGE"),
									 resultSet.getBoolean("OTH_UNDEFINED")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return pricesOthers;
	}

	/**
	 * insert a new {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to insert
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newOthers(PricesOthers other) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String query = "INSERT INTO PRICESOTHERS " +
				"(OTH_CODE, OTH_DESC, OTH_OPD_INCLUDE, OTH_IPD_INCLUDE, OTH_DAILY, OTH_DISCHARGE, OTH_UNDEFINED) VALUES " +
				"(?,?,?,?,?,?,?)";
			parameters.add(other.getCode());
			parameters.add(other.getDescription());
			parameters.add(other.isOpdInclude());
			parameters.add(other.isIpdInclude());
			parameters.add(other.isDaily());
			parameters.add(other.isDischarge());
			parameters.add(other.isUndefined());
			
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * delete a {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteOthers(PricesOthers other) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(other.getId());
		boolean result = false;
		try {
			String query = "DELETE FROM PRICESOTHERS WHERE OTH_ID = ? ";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * update a {@link PriceOthers} in the DB
	 * 
	 * @param other - the {@link PriceOthers} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateOther(PricesOthers other) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String query = "UPDATE PRICESOTHERS SET " +
					"OTH_CODE = ?," +
					"OTH_DESC = ?, " +
					"OTH_OPD_INCLUDE = ?, " +
					"OTH_IPD_INCLUDE = ?, "+
					"OTH_DAILY = ?, "+
					"OTH_DISCHARGE = ?, "+
					"OTH_UNDEFINED = ? "+
					"WHERE OTH_ID = ? ";
			parameters.add(other.getCode());
			parameters.add(other.getDescription());
			parameters.add(other.isOpdInclude());
			parameters.add(other.isIpdInclude());
			parameters.add(other.isDaily());
			parameters.add(other.isDischarge());
			parameters.add(other.isUndefined());
			parameters.add(other.getId());
			
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
}