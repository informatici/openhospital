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
 * Class that executes a query using the connection DbSingleJpaConn
 * The various methods that open a connection with the 
 * autocommit flag set to false have the responsability
 * of doing the commit/rollback operation
 */
public class DbQueryLogger {
	
	protected Logger logger = LoggerFactory.getLogger(DbQueryLogger.class);
    
	/**
     * method that executes a query and returns a resultset
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
    	try{
	        Connection conn = DbSingleJpaConn.getConnection();
	        conn.setAutoCommit(autocommit);
	        Statement stat = conn.createStatement();
	        return stat.executeQuery(aQuery);
        }catch (OHException e){
            throw e;
    	} catch (SQLException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e); //, e); // + ": " + aQuery, e);
    	} catch (Exception e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e); 
    	}
    }
    
    /**
     * method that executes a PreparedStatement with params and returns a resultset
     * @param aQuery
     * @param params
     * @param autocommit
     * @return
     * @throws OHException
     */
    public ResultSet getDataWithParams(String aQuery, List<?> params, boolean autocommit) throws OHException {
    	if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
			if (!params.isEmpty()) logger.trace("	parameters : " + sanitize(params));
		}
    	ResultSet results = null;
    	Connection conn = null;
    	try {
    		conn = DbSingleJpaConn.getConnection();
    		conn.setAutoCommit(autocommit);
    		PreparedStatement pstmt = conn.prepareStatement(aQuery);
    		for (int i = 0; i < params.size(); i++) {
    			pstmt.setObject(i + 1, params.get(i));
    		}
    		results = pstmt.executeQuery();
        }catch (OHException e){
            throw e;
    	} catch (SQLException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e); // + ": " + aQuery, e);
    	} catch (Exception e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e); 
    	}
    	return results;
    }

    /**
     * method that executes an insert-update-delete query and returns true or false
     * depending on the success of the operation
     * @param String aQuery
     * @param boolean autocommit
     * @return Boolean True/False
     * @throws SQLException
     * @throws IOException
     */
    public boolean setData(String aQuery, boolean autocommit) throws OHException {
    	if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
    	try{
	        Connection conn = DbSingleJpaConn.getConnection();
	        conn.setAutoCommit(autocommit);
	        Statement stat = conn.createStatement();
	        return stat.executeUpdate(aQuery) > 0;
        }catch (OHException e){
            throw e;
    	} catch (SQLIntegrityConstraintViolationException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.theselecteditemisstillusedsomewhere"), e); // + ": " + aQuery, e);
    	} catch (SQLException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e); // + ": " + aQuery, e);
    	} catch (Exception e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e); 
    	}
    }
    
    /**
     * method that executes an insert-update-delete PreparedStatement with params and returns true or false
     * depending on the success of the operation
     * @param aQuery
     * @param params
     * @param autocommit
     * @return
     * @throws OHException
     */
    public boolean setDataWithParams(String aQuery, List<?> params, boolean autocommit) throws OHException {
    	if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
			if (!params.isEmpty()) logger.trace("	parameters : " + sanitize(params));
		}
    	Connection conn = null;
    	try {
    		conn = DbSingleJpaConn.getConnection();
    		conn.setAutoCommit(autocommit);
    		PreparedStatement pstmt = conn.prepareStatement(aQuery);
    		for (int i = 0; i < params.size(); i++) {
    			pstmt.setObject(i+1, params.get(i));
    		}
    		return pstmt.executeUpdate() > 0;
        }catch (OHException e){
            throw e;
    	} catch (SQLIntegrityConstraintViolationException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.theselecteditemisstillusedsomewhere"), e); // + ": " + aQuery, e);
    	} catch (SQLException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e); // + ": " + aQuery, e);
    	} catch (Exception e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e); 
    	}
    }
    
	/**
     * method that executes an insert-update-delete query and returns A ResultSet
     * containing the autogenerated key (integer counter)
     * if no key has been generated the ResultSet will be empty
     * @param String aQuery
     * @param boolean autocommit
     * @return ResultSet
     * @throws SQLException
     * @throws IOException
     */
    public ResultSet setDataReturnGeneratedKey(String aQuery, boolean autocommit) throws OHException {
    	if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
    	try{
	        Connection conn = DbSingleJpaConn.getConnection();
	        conn.setAutoCommit(autocommit);
	        Statement stat = conn.createStatement();
	        stat.execute(aQuery,Statement.RETURN_GENERATED_KEYS);
	        return stat.getGeneratedKeys();
        }catch (OHException e){
            throw e;
    	} catch (SQLException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e); // + ": " + aQuery, e);
    	} catch (Exception e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e); 
    	}
    }
    
    /**
     * method that executes an insert-update-delete PreparedStatement with params and returns A ResultSet
     * containing the autogenerated key (integer counter)
     * if no key has been generated the ResultSet will be empty
     * @param aQuery
     * @param params
     * @param autocommit
     * @return
     * @throws OHException
     */
    public ResultSet setDataReturnGeneratedKeyWithParams(String aQuery, List<?> params, boolean autocommit) throws OHException {
    	if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
			if (!params.isEmpty()) logger.trace("	parameters : " + sanitize(params));
		}
    	try{
	        Connection conn = DbSingleJpaConn.getConnection();
	        conn.setAutoCommit(autocommit);
	        PreparedStatement pstmt = conn.prepareStatement(aQuery, Statement.RETURN_GENERATED_KEYS);
    		for (int i = 0; i < params.size(); i++) {
    			pstmt.setObject(i+1, params.get(i));
    		}
    		pstmt.execute();
	        return pstmt.getGeneratedKeys();
        }catch (OHException e){
            throw e;
    	} catch (SQLException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e); // + ": " + aQuery, e);
    	} catch (Exception e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e); 
    	}
    }
    
    /**
     * method that executes a query and returns true or false
     * depending on the existence of records or not in
     * the Recordset
     * @param String aQuery
     * @return Boolean True/False
     * @throws SQLException
     * @throws IOException
     */
    public boolean isData(String aQuery) throws OHException {
    	if (logger.isDebugEnabled()) {
			logger.debug("Query " + sanitize(aQuery));
		}
    	try {
            Connection conn = DbSingleJpaConn.getConnection();
            Statement stat = conn.createStatement();
            ResultSet set = stat.executeQuery(aQuery);
            return set.first();
        }catch (OHException e){
    	    throw e;
    	} catch (SQLException e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e); // + ": " + aQuery, e);
    	} catch (Exception e) {
    		throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"), e); 
    	}
    }
    
    /**
     * method for sanitize every String object in the list for logging purpose
     * @param params - the list of objects
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
     * @param aString - the String object
     * @return the string sanitized
     */
    private String sanitize(String aString) {
    	return aString.replace("'", "\\'");
	}
}
