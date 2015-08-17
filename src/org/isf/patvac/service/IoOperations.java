package org.isf.patvac.service;

/*------------------------------------------
 * IoOperations  - Patient Vaccine Io operations
 * -----------------------------------------
 * modification history
 * 25/08/2011 - claudia - first beta version
 * 20/10/2011 - insert vaccine type management
 * 14/11/2011 - claudia - inserted search condtion on date
 *------------------------------------------*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.patvac.model.PatientVaccine;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vactype.model.VaccineType;

public class IoOperations {

	/**
	 * returns all {@link PatientVaccine}s of today or one week ago
	 * 
	 * @param minusOneWeek - if <code>true</code> return the last week
	 * @return the list of {@link PatientVaccine}s
	 * @throws OHException 
	 */
	public ArrayList<PatientVaccine> getPatientVaccine(boolean minusOneWeek) throws OHException {
		GregorianCalendar timeTo = new GregorianCalendar();
		GregorianCalendar timeFrom = new GregorianCalendar();
		if (minusOneWeek)
			timeFrom.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		return getPatientVaccine(null, null, timeFrom, timeTo, 'A', 0, 0);
	}

	/**
	 * returns all {@link PatientVaccine}s within <code>dateFrom</code> and
	 * <code>dateTo</code>
	 * 
	 * @param vaccineTypeCode
	 * @param vaccineCode
	 * @param dateFrom
	 * @param dateTo
	 * @param sex
	 * @param ageFrom
	 * @param ageTo
	 * @return the list of {@link PatientVaccine}s
	 * @throws OHException 
	 */
	public ArrayList<PatientVaccine> getPatientVaccine(String vaccineTypeCode, String vaccineCode, GregorianCalendar dateFrom, GregorianCalendar dateTo, char sex, int ageFrom, int ageTo) throws OHException {
		ArrayList<PatientVaccine> patVacList = null;
		StringBuilder sqlString = new StringBuilder();
		List<Object> parameters = new ArrayList<Object>();

		sqlString.append("SELECT PV.*, V.*, P.PAT_SNAME, P.PAT_FNAME, P.PAT_AGE, P.PAT_SEX , VT.VACT_ID_A, VT.VACT_DESC"
				+ " FROM PATIENTVACCINE PV JOIN VACCINE V ON PAV_VAC_ID_A=VAC_ID_A"
				+ " JOIN VACCINETYPE VT ON VAC_VACT_ID_A = VACT_ID_A"
				+ " JOIN PATIENT P ON PAV_PAT_ID = PAT_ID");

		sqlString.append(" WHERE DATE_FORMAT(PAV_DATE,'%Y-%m-%d') >= ?" +
				" AND DATE_FORMAT(PAV_DATE,'%Y-%m-%d') <= ?");
		parameters.add(convertToSQLDateLimited(dateFrom));
		parameters.add(convertToSQLDateLimited(dateTo));

		if (vaccineTypeCode != null) {
			sqlString.append(" AND VACT_ID_A = ?");
			parameters.add(vaccineTypeCode);
		}
		if (vaccineCode != null) {
			sqlString.append(" AND VAC_ID_A = ?");
			parameters.add(vaccineCode);
		}
		if ('A' != sex) {
			sqlString.append(" AND PAT_SEX = ?");
			parameters.add(String.valueOf(sex));
		}
		if (ageFrom != 0 || ageTo != 0) {
			sqlString.append(" AND OPD_AGE BETWEEN ? AND ?");
			parameters.add(ageFrom);
			parameters.add(ageTo);
		}

		sqlString.append(" ORDER BY PV.PAV_DATE, PV.PAV_ID");

		//System.out.println("getPatientVaccine: sql=" + sqlString.toString());
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(sqlString.toString(), parameters, true);
			patVacList = new ArrayList<PatientVaccine>(resultSet.getFetchSize());
			while (resultSet.next()) {
				PatientVaccine pv = new PatientVaccine(
						resultSet.getInt("PAV_ID"), 
						resultSet.getInt("PAV_YPROG"), 
						convertToGregorianDate((Date) resultSet.getObject("PAV_DATE")), 
						resultSet.getInt("PAV_PAT_ID"), 
						new Vaccine(resultSet.getString("VAC_ID_A"), 
									resultSet.getString("VAC_DESC"), 
									new VaccineType(resultSet.getString("VACT_ID_A"), 
													resultSet.getString("VACT_DESC")), 
									resultSet.getInt("VAC_LOCK")), 
						resultSet.getInt("PAV_LOCK"), 
						resultSet.getString("PAT_SNAME") + " " + resultSet.getString("PAT_FNAME"), 
						resultSet.getInt("PAT_AGE"), 
						resultSet.getString("PAT_SEX"));
				patVacList.add(pv);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return patVacList;
	}

	/**
	 * inserts a {@link PatientVaccine} in the DB
	 * 
	 * @param patVac - the {@link PatientVaccine} to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise 
	 * @throws OHException 
	 */
	public boolean newPatientVaccine(PatientVaccine patVac) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlString = "INSERT INTO PATIENTVACCINE" +
				" (PAV_YPROG, PAV_DATE, PAV_PAT_ID, PAV_VAC_ID_A,PAV_LOCK) VALUES" +
				" (?, ?, ?, ?, ?)";

			parameters.add(patVac.getProgr());
			parameters.add(new java.sql.Timestamp(patVac.getVaccineDate().getTime().getTime()));
			parameters.add(patVac.getPatId());
			parameters.add(patVac.getVaccine().getCode());
			parameters.add(patVac.getLock());

			ResultSet res = dbQuery.setDataReturnGeneratedKeyWithParams(sqlString, parameters, true);
			if (res.first()) {
				patVac.setCode(res.getInt(1));
				result = true;
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * updates a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise 
	 * @throws OHException 
	 */
	public boolean updatePatientVaccine(PatientVaccine patVac) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlString = "UPDATE PATIENTVACCINE SET" + 
				" PAV_YPROG = ?," + 
				" PAV_DATE = ?," + 
				" PAV_PAT_ID = ?," + 
				" PAV_VAC_ID_A = ?," + 
				" PAV_LOCK = ? " + 
				" WHERE PAV_ID = ? ";

			parameters.add(patVac.getProgr());
			parameters.add(new java.sql.Timestamp(patVac.getVaccineDate().getTime().getTime()));
			parameters.add(patVac.getPatId());
			parameters.add(patVac.getVaccine().getCode());
			parameters.add(patVac.getLock());
			parameters.add(patVac.getCode());

			result = dbQuery.setDataWithParams(sqlString, parameters, true);
			
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * deletes a {@link PatientVaccine} 
	 * 
	 * @param patVac - the {@link PatientVaccine} to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise 
	 * @throws OHException 
	 */
	public boolean deletePatientVaccine(PatientVaccine patVac) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(patVac.getCode());
		boolean result = false;
		try {
			String sqlString = "DELETE FROM PATIENTVACCINE WHERE PAV_ID = ?";
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Returns the max progressive number within specified year or within current year if <code>0</code>.
	 * 
	 * @param year
	 * @return <code>int</code> - the progressive number in the year
	 * @throws OHException 
	 */
	public int getProgYear(int year) throws OHException {
		int progYear=0;
		DbQueryLogger dbQuery = new DbQueryLogger();
		ResultSet resultSet;
		StringBuilder sqlString = new StringBuilder("SELECT MAX(PAV_YPROG) FROM PATIENTVACCINE");
		
		if (year == 0) {
			resultSet = dbQuery.getData(sqlString.toString(), true);
		} else {
			sqlString.append(" WHERE YEAR(PAV_DATE) = ?");
			List<Object> parameters = Collections.<Object>singletonList(year);
			resultSet = dbQuery.getDataWithParams(sqlString.toString(), parameters, true);
		}
		
		try {
			resultSet.next();
			progYear = resultSet.getInt("MAX(PAV_YPROG)");
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return progYear;
	}
	
	/**
	 * return a String representing the date in format <code>yyyy-MM-dd</code>
	 * 
	 * @param date
	 * @return the date in format <code>yyyy-MM-dd</code>
	 */
	private String convertToSQLDateLimited(GregorianCalendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date.getTime());
	}
	
	/**
	 * convert passed {@link Date} to a {@link GregorianCalendar}
	 * @param aDate - the {@link Date} to convert
	 * @return {@link GregorianCalendar}
	 */
	public GregorianCalendar convertToGregorianDate(Date aDate) {
		GregorianCalendar time = new GregorianCalendar();
		time.setTime(aDate);
		return time;
	}
}
