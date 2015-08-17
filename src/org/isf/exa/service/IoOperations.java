package org.isf.exa.service;
/*------------------------------------------
 * IoOperations - provides the io operations for recovering and managing exam records from the database.
 * -----------------------------------------
 * modification history
 * ??/??/2005 - Davide/Theo - first beta version 
 * 07/11/2006 - ross - modified to accept, within the description, the character quote (')
 *                     (to do this, just double every quote. replaceall("'","''") 
 *                     when record locked all data is saved now, not only descritpion
 *------------------------------------------*/

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperations {

	/**
	 * Returns a list of {@link ExamRow}s that matches passed exam code and description
	 * @param aExamCode - the exam code
	 * @param aDescription - the exam description
	 * @return the list of {@link ExamRow}s
	 * @throws OHException
	 */
	public ArrayList<ExamRow> getExamRow(String aExamCode, String aDescription) throws OHException {
		ArrayList<ExamRow> pRow = null;
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> params = new ArrayList<Object>();
		
		StringBuilder query = new StringBuilder("SELECT * FROM EXAMROW");
		if (aExamCode != null) {
			query.append(" WHERE EXR_EXA_ID_A = ?");
			params.add(aExamCode);
		}
		if (aDescription != null) {
			query.append(" AND EXR_DESC = ?");
			params.add(aDescription);
		}
		query.append(" ORDER BY EXR_EXA_ID_A, EXR_DESC");
		
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), params, true);
			pRow = new ArrayList<ExamRow>(resultSet.getFetchSize());
			while (resultSet.next()) {
				pRow.add(new ExamRow(resultSet.getString("EXR_ID"),
						resultSet.getString("EXR_EXA_ID_A"), resultSet
								.getString("EXR_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pRow;
	}

	/**
	 * Returns the list of {@link Exam}s
	 * @return the list of {@link Exam}s
	 * @throws OHException
	 */
	public ArrayList<Exam> getExams() throws OHException {
		return getExamsByDesc(null);
	}
	
	/**
	 * Returns the list of {@link Exam}s that matches passed description
	 * @param description - the exam description
	 * @return the list of {@link Exam}s
	 * @throws OHException
	 */
	public ArrayList<Exam> getExamsByDesc(String description) throws OHException {
		ArrayList<Exam> pExams = null;
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> params = new ArrayList<Object>(1);
		
		StringBuilder query = new StringBuilder("SELECT * FROM EXAM JOIN EXAMTYPE ON EXA_EXC_ID_A = EXC_ID_A");
		if (description != null) {
			query.append(" WHERE EXC_DESC LIKE ?");
			params.add('%'+description+'%');
		}
		query.append(" ORDER BY EXC_DESC, EXA_DESC");
		
		try {
			ResultSet resultSet = null;
			if (description != null) resultSet = dbQuery.getDataWithParams(query.toString(), params, true);
			else resultSet = dbQuery.getData(query.toString(), true);
			pExams = new ArrayList<Exam>(resultSet.getFetchSize());
			while (resultSet.next()) {
				pExams.add(new Exam(resultSet.getString("EXA_ID_A"),
						resultSet.getString("EXA_DESC"), new ExamType(
								resultSet.getString("EXC_ID_A"), resultSet
										.getString("EXC_DESC")), resultSet
								.getInt("EXA_PROC"), resultSet
								.getString("EXA_DEFAULT"), resultSet
								.getInt("EXA_LOCK")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pExams;
	}

	/**
	 * Returns the list of {@link ExamType}s
	 * @return the list of {@link ExamType}s
	 * @throws OHException
	 */
	public ArrayList<ExamType> getExamType() throws OHException {
		ArrayList<ExamType> pexamtype = null;
		String query = "SELECT EXC_ID_A, EXC_DESC FROM EXAMTYPE ORDER BY EXC_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(query, true);
			pexamtype = new ArrayList<ExamType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				pexamtype.add(new ExamType(resultSet.getString(1),
						resultSet.getString(2)));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pexamtype;
	}

	/**
	 * Insert a new {@link Exam} in the DB.
	 * 
	 * @param exam - the {@link Exam} to insert
	 * @return <code>true</code> if the {@link Exam} has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newExam(Exam exam) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = "INSERT INTO EXAM (EXA_ID_A , EXA_DESC, EXA_EXC_ID_A, EXA_PROC, EXA_DEFAULT ) VALUES (?, ?, ?, ?, ?)";
		
		params.add(exam.getCode());
		params.add(sanitize(exam.getDescription()));
		params.add(exam.getExamtype().getCode());
		params.add(exam.getProcedure());
		params.add(sanitize(exam.getDefaultResult()));
		
		boolean result = dbQuery.setDataWithParams(query, params, true);
		
		dbQuery.releaseConnection();
		return result;
	}

	/**
	 * Insert a new {@link ExamRow} in the DB.
	 * 
	 * @param examRow - the {@link ExamRow} to insert
	 * @return <code>true</code> if the {@link ExamRow} has been inserted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean newExamRow(ExamRow examRow) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> params = new ArrayList<Object>();
		String query = "INSERT INTO EXAMROW (EXR_EXA_ID_A , EXR_DESC) VALUES (?, ?)";
		
		params.add(examRow.getExamCode());
		params.add(sanitize(examRow.getDescription()));
		boolean result = false;
		try {
			result = dbQuery.setDataWithParams(query, params, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Update an already existing {@link Exam}.
	 * @param exam - the {@link Exam} to update
	 * @param check - if <code>true</code> it will perform an integrity check
	 * @return <code>true</code> if the {@link Exam} has been updated, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean updateExam(Exam exam, boolean check) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		ResultSet set = null;
		int lock = 0;

		try {
			if (check) { 
				// we establish if someone else has updated/deleted the record 
				// since the last read
				String query = "SELECT EXA_LOCK FROM EXAM WHERE EXA_ID_A = ?";
				List<Object> params = Collections.<Object>singletonList(exam.getCode());
				set = dbQuery.getDataWithParams(query, params, false); //we use manual commit of the transaction
				if (set.first()) { 
					lock = set.getInt("EXA_LOCK");
					// ok the record is present, it was not deleted
					if (lock != exam.getLock()) { 
						// it was updated by someone else
						return false;
					}
				} else { // the record was deleted since the last read
					throw new OHException(MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"));
				}
			}
			
			String query = "UPDATE EXAM SET EXA_DESC = ?, EXA_LOCK = ?, EXA_PROC = ?, EXA_DEFAULT = ? WHERE EXA_ID_A = ?";
			ArrayList<Object> params = new ArrayList<Object>();
			
			params.add(sanitize(exam.getDescription()));
			params.add(lock + 1);
			params.add(exam.getProcedure());
			params.add(sanitize(exam.getDefaultResult()));
			params.add(exam.getCode());
			
			result = dbQuery.setDataWithParams(query, params, true);
			if (result)
				exam.setLock(exam.getLock() + 1);
			else throw new OHException(MessageBundle.getMessage("angal.sql.thedataisnomorepresent")); // the record has been deleted since the last read
				
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Delete an {@link Exam}
	 * @param exam - the {@link Exam} to delete
	 * @return <code>true</code> if the {@link Exam} has been deleted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean deleteExam(Exam exam) throws OHException {
		String query;
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = true;
		try {
			query = "DELETE FROM EXAMROW WHERE EXR_EXA_ID_A = ?";
			List<Object> params = Collections.<Object>singletonList(exam.getCode());
			dbQuery.setDataWithParams(query, params, false);
		
			query = "DELETE FROM EXAM WHERE EXA_ID_A = ?";
			dbQuery.setDataWithParams(query, params, false);
		
		} finally {
			dbQuery.commit();
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Delete an {@link ExamRow}.
	 * @param examRow - the {@link ExamRow} to delete
	 * @return <code>true</code> if the {@link ExamRow} has been deleted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean deleteExamRow(ExamRow examRow) throws OHException {

		String query = "DELETE FROM EXAMROW WHERE EXR_ID = ?";
		List<Object> params = Collections.<Object>singletonList(examRow.getCode());
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
	 * This function controls the presence of a record with the same key as in
	 * the parameter; Returns false if the query finds no record, else returns
	 * true
	 * 
	 * @param the {@link Exam}
	 * @return <code>true</code> if the Exam code has already been used, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isKeyPresent(Exam exam) throws OHException {
		String query = "SELECT * FROM EXAM WHERE EXA_ID_A = ?";
		List<Object> params = Collections.<Object>singletonList(exam.getCode());
		DbQueryLogger dbQuery = new DbQueryLogger();
		ResultSet result;
		try {
			result = dbQuery.getDataWithParams(query, params, true);
			if (result.first()) {
				return true;
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return false;
	}
	
	/**
	 * Sanitize the given {@link String} value. 
	 * This method is maintained only for backward compatibility.
	 * @param value the value to sanitize.
	 * @return the sanitized value or <code>null</code> if the passed value is <code>null</code>.
	 */
	protected String sanitize(String value)
	{
		if (value == null) return null;
		return value.trim().replaceAll("'", "''");
	}
}
