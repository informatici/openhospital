package org.isf.lab.service;

/*------------------------------------------
 * lab.service.IoOperations - laboratory exam database io operations
 * -----------------------------------------
 * modification history
 * 02/03/2006 - theo - first beta version
 * 10/11/2006 - ross - added editing capability. 
 * 					   new fields data esame, sex, age, material, inout flag added
 * 21/06/2008 - ross - do not add 1 to toDate!. 
 *                     the selection date switched to exam date, 
 * 04/01/2009 - ross - do not use roll, use add(week,-1)!
 *                     roll does not change the year!
 * 16/11/2012 - mwithi - added logging capability
 * 					   - to do lock management
 * 04/02/2013 - mwithi - lock management done
 *------------------------------------------*/


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.model.LaboratoryRow;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperations {

	/**
	 * Return a list of results ({@link LaboratoryRow}s) for passed lab entry.
	 * @param code - the {@link Laboratory} record ID.
	 * @return the list of {@link LaboratoryRow}s. It could be <code>empty</code>
	 * @throws OHException
	 */
	public ArrayList<LaboratoryRow> getLabRow(Integer code) throws OHException {
		ArrayList<LaboratoryRow> row = new ArrayList<LaboratoryRow>();
		String query = "SELECT * FROM LABORATORYROW WHERE LABR_LAB_ID = ? ORDER BY LABR_DESC";
		List<Object> params = Collections.<Object>singletonList(code);
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query, params, true);
			while (resultSet.next()) {
				row.add(new LaboratoryRow(
						resultSet.getInt("LABR_ID"),
						resultSet.getInt("LABR_LAB_ID"), 
						resultSet.getString("LABR_DESC")
						)
				);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return row;

	}

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<Laboratory> getLaboratory(String aCode) {
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		// 04/1/2009 ross: no roll, use add!!
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratory(aCode, time1, time2);
	}*/
	
	/**
	 * Return the whole list of exams ({@link Laboratory}s) within last year.
	 * @return the list of {@link Laboratory}s 
	 * @throws OHException
	 */
	public ArrayList<Laboratory> getLaboratory() throws OHException {
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		// 04/1/2009 ross: no roll, use add!!
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratory(null, time1, time2);
	}

	/**
	 * Return a list of exams ({@link Laboratory}s) between specified dates and matching passed exam name
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link Laboratory}s 
	 * @throws OHException
	 */
	public ArrayList<Laboratory> getLaboratory(String exam,	GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHException {
		
		ArrayList<Laboratory> pLaboratory = new ArrayList<Laboratory>();
		// 21/6/2008 ross: no rolling !!
		//dateTo.roll(GregorianCalendar.DAY_OF_YEAR, true);
		
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder query = new StringBuilder("SELECT * FROM LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A");
		query.append(" WHERE LAB_EXAM_DATE >= ? AND LAB_EXAM_DATE <= ?");
		params.add(convertToSQLDateLimited(dateFrom));	//+ " LAB_EXAM_DATE >='" + convertToSQLDateLimited(dateFrom) + "'"
		params.add(convertToSQLDateLimited(dateTo));		//+ " and LAB_EXAM_DATE <='" + convertToSQLDateLimited(dateTo) + "'";
		if (exam != null) {
			query.append(" AND EXA_DESC = ?");
			params.add(exam);
		}
		query.append(" ORDER BY LAB_EXAM_DATE");
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), params, true);
			while (resultSet.next()) {
				Laboratory lab =
					new Laboratory(resultSet.getInt("LAB_ID"),
							new Exam(resultSet.getString("EXA_ID_A"), 
									resultSet.getString("EXA_DESC"),
									new ExamType("", ""), 
									resultSet.getInt("EXA_PROC"), 
									resultSet.getString("EXA_DEFAULT"), 0),
							convertToGregorianDate((Date) resultSet.getObject("LAB_DATE")), 
							resultSet.getString("LAB_RES"), 
							resultSet.getInt("LAB_LOCK"), 
							resultSet.getString(("LAB_NOTE")), 
							resultSet.getInt("LAB_PAT_ID"), 
							resultSet.getString("LAB_PAT_NAME"));
				lab.setAge(resultSet.getInt("LAB_AGE"));
				lab.setSex(resultSet.getString("LAB_SEX"));
				lab.setMaterial(resultSet.getString("LAB_MATERIAL"));
				lab.setInOutPatient(resultSet.getString("LAB_PAT_INOUT"));
				GregorianCalendar examDate = new GregorianCalendar();
				if (resultSet.getDate("LAB_EXAM_DATE")==null) 
					examDate = null;
				else
					examDate.setTime(resultSet.getDate("LAB_EXAM_DATE"));
				lab.setExamDate(examDate);
				pLaboratory.add(lab);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pLaboratory;
	}
	
	/**
	 * Return a list of exams ({@link Laboratory}s) related to a {@link Patient}.
	 * @param aPatient - the {@link Patient}.
	 * @return the list of {@link Laboratory}s related to the {@link Patient}.
	 * @throws OHException
	 */
	public ArrayList<Laboratory> getLaboratory(Patient aPatient) throws OHException {
		ArrayList<Laboratory> pLaboratory = new ArrayList<Laboratory>();
		String query = "SELECT * FROM (LABORATORY JOIN EXAM ON LAB_EXA_ID_A=EXA_ID_A)" +
				 " LEFT JOIN LABORATORYROW ON LABR_LAB_ID = LAB_ID WHERE LAB_PAT_ID = ? " +
				 " ORDER BY LAB_DATE, LAB_ID";
		List<Object> params = Collections.<Object>singletonList(aPatient.getCode());
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query, params, true);
			while (resultSet.next()) {
				String procedure = resultSet.getString("LAB_RES");
				if (procedure != null && procedure.equals(MessageBundle.getMessage("angal.lab.multipleresults"))){
					procedure = resultSet.getString("LABR_DESC");
					if  (procedure == null || (procedure != null && procedure.trim().equals(""))){
						procedure = MessageBundle.getMessage("angal.lab.allnegative");
					}else{
						procedure = procedure + " " + MessageBundle.getMessage("angal.lab.positive");
					}
				}
				Laboratory lab =
					new Laboratory(resultSet.getInt("LAB_ID"),
							new Exam(resultSet.getString("EXA_ID_A"), 
									resultSet.getString("EXA_DESC"),
									new ExamType("", ""), 
									resultSet.getInt("EXA_PROC"), 
									resultSet.getString("EXA_DEFAULT"), 0),
							convertToGregorianDate((Date) resultSet.getObject("LAB_DATE")), 
							procedure, 
							resultSet.getInt("LAB_LOCK"), 
							resultSet.getString(("LAB_NOTE")), 
							resultSet.getInt("LAB_PAT_ID"), 
							resultSet.getString("LAB_PAT_NAME"));
				lab.setAge(resultSet.getInt("LAB_AGE"));
				lab.setSex(resultSet.getString("LAB_SEX"));
				lab.setMaterial(resultSet.getString("LAB_MATERIAL"));
				lab.setInOutPatient(resultSet.getString("LAB_PAT_INOUT"));
				GregorianCalendar examDate = new GregorianCalendar();
				if (resultSet.getDate("LAB_EXAM_DATE")==null) 
					examDate = null;
				else
					examDate.setTime(resultSet.getDate("LAB_EXAM_DATE"));
				lab.setExamDate(examDate);
				pLaboratory.add(lab);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pLaboratory;
	}
	
	/**
	 * Return a list of exams suitable for printing ({@link LaboratoryForPrint}s) 
	 * within last year
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link LaboratoryForPrint}s 
	 * @throws OHException
	 */
	public ArrayList<LaboratoryForPrint> getLaboratoryForPrint() throws OHException {
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratoryForPrint(null, time1, time2);
	}

	/*
	 * NO LONGER USED
	 * 
	 * public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(String exam,	String result) {
		GregorianCalendar time1 = new GregorianCalendar();
		GregorianCalendar time2 = new GregorianCalendar();
		//time1.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time1.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		// 21/6/2008 ross: no rolling !!
		//time2.roll(GregorianCalendar.DAY_OF_YEAR, true);
		return getLaboratoryForPrint(exam, time1, time2);
	}*/
	
	/**
	 * Return a list of exams suitable for printing ({@link LaboratoryForPrint}s) 
	 * between specified dates and matching passed exam name
	 * @param exam - the exam name as <code>String</code>
	 * @param dateFrom - the lower date for the range
	 * @param dateTo - the highest date for the range
	 * @return the list of {@link LaboratoryForPrint}s 
	 * @throws OHException
	 */
	public ArrayList<LaboratoryForPrint> getLaboratoryForPrint(String exam, GregorianCalendar dateFrom, GregorianCalendar dateTo) throws OHException {
		ArrayList<LaboratoryForPrint> pLaboratory = new ArrayList<LaboratoryForPrint>();;
		dateTo.roll(GregorianCalendar.DAY_OF_YEAR, true);
		
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder query = new StringBuilder("SELECT * FROM (LABORATORY JOIN EXAM ON LAB_EXA_ID_A = EXA_ID_A) JOIN EXAMTYPE ON EXC_ID_A = EXA_EXC_ID_A");
		query.append(" WHERE LAB_DATE >= ? AND LAB_DATE <= ?");
		params.add(convertToSQLDateLimited(dateFrom));	//+ " LAB_EXAM_DATE >='" + convertToSQLDateLimited(dateFrom) + "'"
		params.add(convertToSQLDateLimited(dateTo));		//+ " and LAB_EXAM_DATE <='" + convertToSQLDateLimited(dateTo) + "'";
		if (exam != null) {
			query.append(" AND EXA_DESC LIKE = ?");
			params.add('%' + exam + '%');
		}
		query.append(" ORDER BY EXC_DESC");
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), params, true);
			while (resultSet.next()) {
				pLaboratory.add(new LaboratoryForPrint(
						resultSet.getInt("LAB_ID"),
						new Exam(resultSet.getString("EXA_ID_A"), 
								resultSet.getString("EXA_DESC"), 
								new ExamType("", ""),
								resultSet.getInt("EXA_PROC"), 
								resultSet.getString("EXA_DEFAULT"), 
								0),
						convertToGregorianDate((Date) resultSet.getObject("LAB_DATE")), 
						resultSet.getString("LAB_RES")
					)
				);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pLaboratory;
	}
	
	/**
	 * Insert a Laboratory exam {@link Laboratory} and return generated key. No commit is performed.
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param dbQuery - the connection manager
	 * @return the generated key
	 * @throws OHException
	 */
	private Integer newLaboratory(Laboratory laboratory, DbQueryLogger dbQuery ) throws OHException {
		Integer newCode = -1;
		ArrayList<Object> params = new ArrayList<Object>();
		try {
			String sqlString = "INSERT INTO LABORATORY (LAB_EXA_ID_A ,LAB_DATE, " +
					"LAB_RES, LAB_NOTE, LAB_PAT_NAME, " +
					"LAB_PAT_ID, LAB_AGE, LAB_SEX, " +
					"LAB_MATERIAL, LAB_EXAM_DATE, LAB_PAT_INOUT) values (?,?,?,?,?,?,?,?,?,?,?)";
			params.add(laboratory.getExam().getCode());
			params.add(new java.sql.Timestamp(laboratory.getDate().getTime().getTime()));
			params.add(laboratory.getResult());
			params.add(laboratory.getNote());
			params.add(laboratory.getPatName());
			params.add(laboratory.getPatId() > 0 ? "" + laboratory.getPatId() : null);
			params.add(laboratory.getAge());
			params.add(laboratory.getSex());
			params.add(laboratory.getMaterial());
			params.add(new java.sql.Date(laboratory.getExamDate().getTime().getTime()));
			params.add(laboratory.getInOutPatient());
		
			ResultSet result = dbQuery.setDataReturnGeneratedKeyWithParams(sqlString, params, false);
			if (result.next()) {
				newCode = result.getInt(1);
			} else return -1;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 
		return newCode;
	}
	
	/**
	 * Inserts one Laboratory exam {@link Laboratory} (Procedure One)
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param dbQuery - the connection manager
	 * @return <code>true</code> if the exam has been inserted, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean newLabFirstProcedure(Laboratory laboratory) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			Integer newCode = newLaboratory(laboratory, dbQuery);
			if (newCode> 0)
				dbQuery.commit();
				laboratory.setCode(newCode);
			return (newCode>0);
			
		} finally {
			dbQuery.releaseConnection();
		}
	}
	
	/**
	 * Inserts one Laboratory exam {@link Laboratory} with multiple results (Procedure Two) 
	 * @param laboratory - the {@link Laboratory} to insert
	 * @param labRow - the list of results ({@link String}s)
	 * @return <code>true</code> if the exam has been inserted with all its results, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean newLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> params;
		boolean result = false;
		try {
			Integer newCode = newLaboratory(laboratory, dbQuery);
			if (newCode> 0) {
				result = true;
				laboratory.setCode(newCode);
					
				String query = "INSERT INTO LABORATORYROW (LABR_LAB_ID,LABR_DESC) VALUES (?,?)";
				for (String row : labRow) {
					params = new ArrayList<Object>(2);
					params.add(laboratory.getCode());
					params.add(row);
					result = dbQuery.setDataWithParams(query, params, false);
				}
				
				if (result) {
					dbQuery.commit();
				}
			}
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory}. No commit is performed.
	 * @param laboratory - the {@link Laboratory} to update
	 * @param dbQuery - the connection manager
	 * @return <code>true</code> if the exam has been updated with all its results, <code>false</code> otherwise
	 * @throws OHException
	 */
	private boolean updateLaboratory(Laboratory laboratory, DbQueryLogger dbQuery) throws OHException {
		String query = "UPDATE LABORATORY SET " +
			    "LAB_EXA_ID_A = ?, " +
			    "LAB_DATE = ?, " +
				"LAB_RES = ?, " +
				"LAB_NOTE = ?, " +
				"LAB_PAT_NAME = ?, " +
				"LAB_PAT_ID = ?, " +
				"LAB_AGE = ?, " +
				"LAB_SEX = ?, " +
				"LAB_MATERIAL = ?, " +
				"LAB_EXAM_DATE = ?, " +
				"LAB_PAT_INOUT = ? " +
				"WHERE LAB_ID = ?";
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(laboratory.getExam().getCode());
		params.add(new java.sql.Timestamp(laboratory.getDate().getTime().getTime()));
		params.add(laboratory.getResult());
		params.add(laboratory.getNote());
		params.add(laboratory.getPatName());
		params.add(laboratory.getPatId() > 0 ? "" + laboratory.getPatId() : null);
		params.add(laboratory.getAge());
		params.add(laboratory.getSex());
		params.add(laboratory.getMaterial());
		params.add(new java.sql.Date(laboratory.getExamDate().getTime().getTime()));
		params.add(laboratory.getInOutPatient());
		params.add(laboratory.getCode());

		return dbQuery.setDataWithParams(query, params, false);
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure One).
	 * If old exam was Procedure Two all its releated result are deleted.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean updateLabFirstProcedure(Laboratory laboratory) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = updateLaboratory(laboratory, dbQuery);
		//se cambio da procedura 2 a procedura 1
		try {
			String query = "SELECT * FROM LABORATORYROW WHERE LABR_LAB_ID = ?";
			List<Object> params = Collections.<Object>singletonList(laboratory.getCode());
			ResultSet resultSet = dbQuery.getDataWithParams(query, params, false);
			
			if (resultSet.next()) {
				query = "DELETE FROM LABORATORYROW WHERE LABR_LAB_ID = ?";
				result = dbQuery.setDataWithParams(query, params, false);
			}
			if (result) {
				dbQuery.commit();
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Update an already existing Laboratory exam {@link Laboratory} (Procedure Two).
	 * Previous results are deleted and replaced with new ones.
	 * @param laboratory - the {@link Laboratory} to update
	 * @return <code>true</code> if the exam has been updated with all its results, <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean editLabSecondProcedure(Laboratory laboratory, ArrayList<String> labRow) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = updateLaboratory(laboratory, dbQuery);
		try {
			if (result) {
				
				String query = "DELETE FROM LABORATORYROW WHERE LABR_LAB_ID = ?";
				List<Object> params = Collections.<Object>singletonList(laboratory.getCode());
				
				dbQuery.setDataWithParams(query, params, false);
								
				query = "INSERT INTO LABORATORYROW (LABR_LAB_ID, LABR_DESC) values (?,?)";
				
				for (String row : labRow) {
					params = new ArrayList<Object>(2);
					params.add(laboratory.getCode());
					params.add(row);
					
					result = dbQuery.setDataWithParams(query, params, false);
				}
				if (result) {
					dbQuery.commit();
				}
			} else return false;
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Delete a Laboratory exam {@link Laboratory} (Procedure One or Two).
	 * Previous results, if any, are deleted as well.
	 * @param laboratory - the {@link Laboratory} to delete
	 * @return <code>true</code> if the exam has been deleted with all its results, if any. <code>false</code> otherwise
	 * @throws OHException
	 */
	public boolean deleteLaboratory(Laboratory aLaboratory) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = true;
		try {
			String query = "";
			List<Object> params = Collections.<Object>singletonList(aLaboratory.getCode());
			
			if (aLaboratory.getExam().getProcedure() == 2) {
				query = "DELETE FROM LABORATORYROW WHERE LABR_LAB_ID = ?";
				dbQuery.setDataWithParams(query, params, false);
			}
			
			query = "DELETE FROM LABORATORY WHERE LAB_ID = ?";
			result = dbQuery.setDataWithParams(query, params, false);
			
			dbQuery.commit();
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * This method express a date saved in a GregorianCalendar object in a
	 * String form, understandable by MySql It contains also the hours,minutes
	 * and seconds
	 * 
	 * @param time
	 *            (GregorianCalendar)
	 * @return String
	 */
	public String convertToSQLDate(GregorianCalendar time) {
		return String.valueOf(time.get(GregorianCalendar.YEAR)) + "-"
				+ String.valueOf(time.get(GregorianCalendar.MONTH) + 1) + "-"
				+ time.get(GregorianCalendar.DAY_OF_MONTH) + " "
				+ time.get(GregorianCalendar.HOUR_OF_DAY) + ":"
				+ time.get(GregorianCalendar.MINUTE) + ":"
				+ time.get(GregorianCalendar.SECOND);
	}

	/**
	 * This method express a date saved in a GregorianCalendar object in a
	 * String form, understandable by MySql It doesn't contain also the
	 * hours,minutes and seconds
	 * 
	 * @param time
	 *            (GregorianCalendar)
	 * @return String
	 */
	public String convertToSQLDateLimited(GregorianCalendar time) {
		return time.get(GregorianCalendar.YEAR) + "-"
				+ String.valueOf(time.get(GregorianCalendar.MONTH) + 1) + "-"
				+ time.get(GregorianCalendar.DAY_OF_MONTH);
	}

	/**
	 * It sets a date contained in a Date object into a GregorianCalendar object
	 * When we load the date from the database, in fact, we get a Date object
	 * 
	 * @param aDate
	 *            (Date)
	 * @return GregorianCalendar
	 */
	public GregorianCalendar convertToGregorianDate(Date aDate) {
		GregorianCalendar time = new GregorianCalendar();
		time.setTime(aDate);
		return time;
	}
}
