package org.isf.priceslist.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import org.isf.generaldata.MessageBundle;
import org.isf.priceslist.model.List;
import org.isf.priceslist.model.Price;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperations {

	/**
	 * return the list of {@link List}s in the DB
	 * 
	 * @return the list of {@link List}s
	 * @throws OHException 
	 */
	public ArrayList<List> getLists() throws OHException {
		ArrayList<List> lists = null;
		String string = "SELECT * FROM PRICELISTS";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(string, true);
			lists = new ArrayList<List>(resultSet.getFetchSize());
			while (resultSet.next()) {
				lists.add(new List(resultSet.getInt("LST_ID"),
									 resultSet.getString("LST_CODE"),
									 resultSet.getString("LST_NAME"),
									 resultSet.getString("LST_DESC"),
									 resultSet.getString("LST_CURRENCY")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return lists;
	}
	
	/**
	 * return the list of {@link Price}s in the DB
	 * 
	 * @return the list of {@link Price}s
	 * @throws OHException 
	 */
	public ArrayList<Price> getPrices() throws OHException {
		ArrayList<Price> prices = null;
		String string = "SELECT * FROM PRICES ORDER BY PRC_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(string,true);
			prices = new ArrayList<Price>(resultSet.getFetchSize());
			while (resultSet.next()) {
				prices.add(new Price(resultSet.getInt("PRC_ID"),
									 resultSet.getInt("PRC_LST_ID"),
									 resultSet.getString("PRC_GRP"),
									 resultSet.getString("PRC_ITEM"),
									 resultSet.getString("PRC_DESC"),
									 resultSet.getDouble("PRC_PRICE")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return prices;
	}

	/**
	 * updates all {@link Price}s in the specified {@link List}
	 * 
	 * @param list - the {@link List}
	 * @param prices - the list of {@link Price}s
	 * @return <code>true</code> if the list has been replaced, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updatePrices(List list, ArrayList<Price> prices) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String query = "DELETE FROM PRICES WHERE PRC_LST_ID = ?";
			parameters.add(list.getId());
			result = dbQuery.setDataWithParams(query, parameters, false);
			
			query = "INSERT INTO PRICES (PRC_LST_ID, PRC_GRP, PRC_ITEM, PRC_DESC, PRC_PRICE) VALUES (?,?,?,?,?)";
			parameters.clear();
			for (Price price : prices) {
				parameters.add(list.getId());
				parameters.add(price.getGroup());
				parameters.add(price.getItem());
				parameters.add(price.getDesc());
				parameters.add(price.getPrice());
			
				result = result && dbQuery.setDataWithParams(query, parameters, false);
				parameters.clear();
			}
			
			if (result) {
				dbQuery.commit();
			} else {
				dbQuery.rollback();
			}
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * insert a new {@link List} in the DB
	 * 
	 * @param list - the {@link List}
	 * @return <code>true</code> if the list has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newList(List list) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String query = "INSERT INTO PRICELISTS (LST_CODE, LST_NAME, LST_DESC, LST_CURRENCY) VALUES (?,?,?,?)";
			parameters.add(list.getCode());
			parameters.add(list.getName());
			parameters.add(list.getDescription());
			parameters.add(list.getCurrency());
			
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * update a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to update
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateList(List list) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		java.util.List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String query = "UPDATE PRICELISTS SET LST_CODE = ?, " +
						   "LST_NAME = ?, " +
						   "LST_DESC = ?, " +
						   "LST_CURRENCY = ? "+
						   "WHERE LST_ID = ?";

			parameters.add(list.getCode());
			parameters.add(list.getName());
			parameters.add(list.getDescription());
			parameters.add(list.getCurrency());
			parameters.add(list.getId());
			
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * delete a {@link List} in the DB
	 * 
	 * @param list - the {@link List} to delete
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteList(List list) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		java.util.List<Object> parameters = Collections.<Object>singletonList(list.getId());
		boolean result = false;
		try {
			String query = "DELETE FROM PRICELISTS WHERE LST_ID = ? ";
			result = dbQuery.setDataWithParams(query, parameters, true);
			
			/*
			 * If FOREIGN KEYS are working this is not needed.
			 * Execute for safe. 
			 */
			query = "DELETE FROM PRICES WHERE PRC_LST_ID = ? ";
			dbQuery.setDataWithParams(query, parameters, true);
			
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * duplicate {@link list} multiplying by <code>factor</code> and rounding by <code>step</code>
	 * 
	 * @param list - the {@link list} to be duplicated
	 * @param factor - the multiplying factor
	 * @param step - the rounding step
	 * @return <code>true</code> if the list has been duplicated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean copyList(List list, double factor, double step) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		java.util.List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		int newID;
		try {
			String query = "INSERT INTO PRICELISTS (LST_CODE, LST_NAME, LST_DESC, LST_CURRENCY) VALUES (?,?,?,?)";
			parameters.add(list.getCode());
			parameters.add(list.getName());
			parameters.add(list.getDescription());
			parameters.add(list.getCurrency());

			ResultSet set = dbQuery.setDataReturnGeneratedKeyWithParams(query, parameters, false);

			if(set.first()){
				newID = set.getInt(1);
				parameters.clear();
				
				if (step > 0.) {
					query = "INSERT INTO PRICES (PRC_LST_ID, PRC_GRP, PRC_ITEM, PRC_DESC, PRC_PRICE) "+
					        "SELECT ?, PRC_GRP, PRC_ITEM, PRC_DESC, ROUND((PRC_PRICE * ?) / ?) * ? AS PRC_PRICE FROM PRICES WHERE PRC_LST_ID = ?";
					parameters.add(newID);
					parameters.add(factor);
					parameters.add(step);
					parameters.add(step);
					parameters.add(list.getId());
				} else {
					query = "INSERT INTO PRICES (PRC_LST_ID, PRC_GRP, PRC_ITEM, PRC_DESC, PRC_PRICE) "+
			        "SELECT ?, PRC_GRP, PRC_ITEM, PRC_DESC, ROUND((PRC_PRICE * ?) AS PRC_PRICE FROM PRICES WHERE PRC_LST_ID = ?";
					parameters.add(newID);
					parameters.add(factor);
					parameters.add(list.getId());
				}
				result = dbQuery.setDataWithParams(query, parameters, false);
				if (result) {
					dbQuery.commit();
					list.setId(newID);
				}
			} else {
				dbQuery.rollback();
				result = false;
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
}