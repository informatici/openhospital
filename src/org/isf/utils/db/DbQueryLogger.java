package org.isf.utils.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that executes a query using the connection DbSingleConn The various
 * methods that open a connection with the autocommit flag set to false have the
 * responsability of doing the commit/rollback operation.
 * the autocommit flag work with the static field transactionState. 
 */
public class DbQueryLogger {

	protected Logger logger = LoggerFactory.getLogger(DbQueryLogger.class);

	/**
	 * This field helps handle transactions
	 * A caller can start a globally managed transaction by calling the static function beginTransaction function
	 * which return a boolean that will help the caller to commit the transaction.
	 * when this field is false, the application run as usual.
	 * 
	 */
	private static boolean transactionState = false;

	/**
	 * Return a boolean that will help the caller commit a transaction
	 * @return true no transaction is in progress
	 */
	public static boolean beginTrasaction() {
		if (transactionState) {
			return false;
		}
		transactionState=true;
		return true;
	}
	
	/**
	 * Reset the transaction state flag to false.
	 * Calling assume that the trsaction has been commit or rollbacked by the function that started it.
	 */
	public static void releaseTrasaction(boolean state) {
		if (state) {
			transactionState= false;
		}
	}

	/**
	 * method that executes a query and returns a resultset
	 * 
	 * @param aQuery
	 * @param autocommit
	 * @return "ResultSet"
	 * @throws SQLException
	 * @throws IOException
	 */
	public ResultSet getData(String aQuery, boolean autocommit) throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
		try {
			Connection conn = DbSingleConn.getConnection();
			conn.setAutoCommit(autocommit && !transactionState);
			Statement stat = conn.createStatement();
			return stat.executeQuery(aQuery);
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * method that executes a PreparedStatement with params and returns a
	 * resultset
	 * 
	 * @param aQuery
	 * @param params
	 * @param autocommit
	 * @return
	 * @throws OHException
	 */
	public ResultSet getDataWithParams(String aQuery, List<?> params, boolean autocommit) throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
			if (!params.isEmpty())
				logger.trace("	parameters : " + sanitize(params));
		}
		ResultSet results = null;
		Connection conn = null;
		try {
			conn = DbSingleConn.getConnection();
			conn.setAutoCommit(autocommit && !transactionState);
			PreparedStatement pstmt = conn.prepareStatement(aQuery);
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}
			results = pstmt.executeQuery();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
		return results;
	}

	/**
	 * method that executes an insert-update-delete query and returns true or
	 * false depending on the success of the operation
	 * 
	 * @param String
	 *            aQuery
	 * @param boolean
	 *            autocommit
	 * @return Boolean True/False
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean setData(String aQuery, boolean autocommit) throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
		try {
			Connection conn = DbSingleConn.getConnection();
			conn.setAutoCommit(autocommit && !transactionState);
			Statement stat = conn.createStatement();
			return stat.executeUpdate(aQuery) > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.theselecteditemisstillusedsomewhere"), e);
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * method that executes an insert-update-delete query and returns true or
	 * false depending on the success of the operation
	 * 
	 * @param String
	 *            aQuery
	 * @param boolean
	 *            autocommit
	 * @return Boolean True/False
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean execute(String aQuery, boolean autocommit) throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
		try {
			Connection conn = DbSingleConn.getConnection();
			conn.setAutoCommit(autocommit && !transactionState);
			Statement stat = conn.createStatement();
			return stat.execute(aQuery);
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.theselecteditemisstillusedsomewhere"), e);
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * method that executes an insert-update-delete PreparedStatement with
	 * params and returns true or false depending on the success of the
	 * operation
	 * 
	 * @param aQuery
	 * @param params
	 * @param autocommit
	 * @return
	 * @throws OHException
	 */
	public boolean setDataWithParams(String aQuery, List<?> params, boolean autocommit) throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
			if (!params.isEmpty())
				logger.trace("	parameters : " + sanitize(params));
		}
		Connection conn = null;
		try {
			conn = DbSingleConn.getConnection();
			conn.setAutoCommit(autocommit && !transactionState);
			PreparedStatement pstmt = conn.prepareStatement(aQuery);
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}
			return pstmt.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.theselecteditemisstillusedsomewhere"), e);
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * method that executes an insert-update-delete query and returns A
	 * ResultSet containing the autogenerated key (integer counter) if no key
	 * has been generated the ResultSet will be empty
	 * 
	 * @param String
	 *            aQuery
	 * @param boolean
	 *            autocommit
	 * @return ResultSet
	 * @throws SQLException
	 * @throws IOException
	 */
	public ResultSet setDataReturnGeneratedKey(String aQuery, boolean autocommit) throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
		try {
			Connection conn = DbSingleConn.getConnection();
			conn.setAutoCommit(autocommit && !transactionState);
			Statement stat = conn.createStatement();
			stat.execute(aQuery, Statement.RETURN_GENERATED_KEYS);
			return stat.getGeneratedKeys();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * method that executes an insert-update-delete PreparedStatement with
	 * params and returns A ResultSet containing the autogenerated key (integer
	 * counter) if no key has been generated the ResultSet will be empty
	 * 
	 * @param aQuery
	 * @param params
	 * @param autocommit
	 * @return
	 * @throws OHException
	 */
	public ResultSet setDataReturnGeneratedKeyWithParams(String aQuery, List<?> params, boolean autocommit)
			throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
			if (!params.isEmpty())
				logger.trace("	parameters : " + sanitize(params));
		}
		try {
			Connection conn = DbSingleConn.getConnection();
			conn.setAutoCommit(autocommit && !transactionState);
			PreparedStatement pstmt = conn.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(i + 1, params.get(i));
			}
			pstmt.execute();
			return pstmt.getGeneratedKeys();
		} catch (SQLException e) {

			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * method that executes a query and returns true or false depending on the
	 * existence of records or not in the Recordset
	 * 
	 * @param String
	 *            aQuery
	 * @return Boolean True/False
	 * @throws SQLException
	 * @throws IOException
	 */
	public boolean isData(String aQuery) throws OHException {
		if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
		try {
			Connection conn = DbSingleConn.getConnection();
			Statement stat = conn.createStatement();
			ResultSet set = stat.executeQuery(aQuery);
			return set.first();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} catch (IOException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * 
	 * @throws OHException
	 */
	public void closeConnection() throws OHException {
		try {
			if (!transactionState)
				DbSingleConn.closeConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}
	
	public void closeConnection(boolean state) throws OHException {
		try {
			if (transactionState && state)
				DbSingleConn.closeConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}
	

	public void releaseConnection() throws OHException {
		try {
			if (!transactionState)
				DbSingleConn.releaseConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}
	
	public void releaseConnection(boolean state) throws OHException {
		try {
			if (transactionState && state)
				DbSingleConn.releaseConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	public void commit() throws OHException {
		try {
			if (!transactionState)
				DbSingleConn.commitConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	public void commit(boolean state) throws OHException {
		try {
			if (transactionState && state)
				DbSingleConn.commitConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	public void rollback() throws OHException {
		try {
			if (!transactionState)
				DbSingleConn.rollbackConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	public void rollback(boolean state) throws OHException {
		try {
			if (transactionState && state)
				DbSingleConn.rollbackConnection();
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e);
		}
	}

	/**
	 * method for sanitize every String object in the list for logging purpose
	 * 
	 * @param params
	 *            - the list of objects
	 * @return the list sanitized
	 */
	private List<?> sanitize(List<?> params) {
		List<Object> saneParams = new ArrayList<Object>();
		for (Object param : params) {
			if (param instanceof String) {
				saneParams.add(sanitize((String) param));
			} else {
				saneParams.add(param);
			}
		}
		return saneParams;
	}

	/**
	 * method for sanitize a String object for logging purpose
	 * 
	 * @param aString
	 *            - the String object
	 * @return the string sanitized
	 */
	private String sanitize(String aString) {
		return aString.replace("'", "\\'");
	}
}
