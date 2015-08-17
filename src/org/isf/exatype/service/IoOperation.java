package org.isf.exatype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperation {
	
	/**
	 * Return the list of {@link ExamType}s.
	 * @return the list of {@link ExamType}s.
	 * @throws OHException
	 */
	public ArrayList<ExamType> getExamType() throws OHException {
		ArrayList<ExamType> pexamtype = null;
		String string = "SELECT * FROM EXAMTYPE ORDER BY EXC_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(string, true);
			pexamtype = new ArrayList<ExamType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				pexamtype.add(new ExamType(resultSet.getString("EXC_ID_A"), resultSet.getString("EXC_DESC")));
			}
			
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 
		finally{
			dbQuery.releaseConnection();
		}
		return pexamtype;
	}
	
	/**
	 * Update an already existing {@link ExamType}.
	 * @param examType - the {@link ExamType} to update
	 * @return <code>true</code> if the examType has been updated, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean updateExamType(ExamType examType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String query = "UPDATE EXAMTYPE SET EXC_DESC = ? WHERE EXC_ID_A = ?";
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(examType.getDescription());
			params.add(examType.getCode());
			result = dbQuery.setDataWithParams(query, params, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * Insert a new {@link ExamType} in the DB.
	 * @param examType - the {@link ExamType} to insert.
	 * @return <code>true</code> if the examType has been inserted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean newExamType(ExamType examType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String query = "INSERT INTO EXAMTYPE (EXC_ID_A, EXC_DESC) VALUES (?, ?)";
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(examType.getCode());
			params.add(examType.getDescription());
			result = dbQuery.setDataWithParams(query, params, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * Delete the passed {@link ExamType}.
	 * @param examType - the {@link ExamType} to delete.
	 * @return <code>true</code> if the examType has been deleted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean deleteExamType(ExamType examType) throws OHException {
		String query = "DELETE FROM EXAMTYPE WHERE EXC_ID_A = ?";
		List<Object> params = Collections.<Object>singletonList(examType.getCode());
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			result = dbQuery.setDataWithParams(query, params, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * This function controls the presence of a record with the same code as in
	 * the parameter.
	 * @param code - the code
	 * @return <code>true</code> if the code is present, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String sqlstring = "SELECT EXC_ID_A FROM EXAMTYPE WHERE EXC_ID_A = ?";
			List<Object> params = Collections.<Object>singletonList(code);
			ResultSet set = dbQuery.getDataWithParams(sqlstring, params, true);
			if(set.first()){
				result = true;
			}					
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}
}
