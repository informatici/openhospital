package org.isf.opd.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.opd.model.Opd;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/*----------------------------------------------------
 * (org.isf.opd.service)IoOperations - services for opd class
 * ---------------------------------------------------
 * modification history
 * 11/12/2005 - Vero, Rick  - first beta version 
 * 03/01/2008 - ross - selection for opd browser is performed on OPD_DATE_VIS instead of OPD_DATE
 *                   - selection now is less than or equal, before was only less than
 * 21/06/2008 - ross - for multilanguage version, the test for "all type" and "all disease"
 *                     must be done on the translated resource, not in english
 *                   - fix:  getSurgery() method should not add 1 day to toDate
 * 05/09/2008 - alex - added method for patient related OPD query
 * 05/01/2009 - ross - fix: in insert, referralfrom was written both in referralfrom and referralto
 * 09/01/2009 - fabrizio - Modified queried to accomodate type change of date field in Opd class.
 *                         Modified construction of queries, concatenation is performed with
 *                         StringBuilders instead than operator +. Removed some nested try-catch
 *                         blocks. Modified methods to format dates.                          
 *------------------------------------------*/

@Component
public class IoOperations {
	
	/**
	 * return all OPDs of today or one week ago
	 * 
	 * @param oneWeek - if <code>true</code> return the last week, only today otherwise.
	 * @return the list of OPDs. It could be <code>empty</code>.
	 * @throws OHException 
	 */
	public ArrayList<Opd> getOpdList(boolean oneWeek) throws OHException{
		GregorianCalendar dateFrom=new GregorianCalendar();
		GregorianCalendar dateTo=new GregorianCalendar();
		if (oneWeek) dateFrom.add(GregorianCalendar.WEEK_OF_YEAR,-1);
		return getOpdList(MessageBundle.getMessage("angal.opd.alltype"),MessageBundle.getMessage("angal.opd.alldisease"),dateFrom,dateTo,0,0,'A',"A");
	}
	
	/**
	 * 
	 * return all OPDs within specified dates
	 * 
	 * @param diseaseTypeCode
	 * @param diseaseCode
	 * @param dateFrom
	 * @param dateTo
	 * @param ageFrom
	 * @param ageTo
	 * @param sex
	 * @param newPatient
	 * @return the list of OPDs. It could be <code>empty</code>.
	 * @throws OHException 
	 */
	public ArrayList<Opd> getOpdList(String diseaseTypeCode,String diseaseCode, GregorianCalendar dateFrom,GregorianCalendar dateTo,int ageFrom, int ageTo,char sex,String newPatient) throws OHException{
		ArrayList<Opd> opdList = null;
		StringBuilder sqlString = new StringBuilder();
		List<Object> parameters = new ArrayList<Object>();
		
		sqlString.append("SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID LEFT JOIN DISEASE ON OPD_DIS_ID_A = DIS_ID_A LEFT JOIN DISEASETYPE ON DIS_DCL_ID_A = DCL_ID_A WHERE 1");
		if (!(diseaseTypeCode.equals(MessageBundle.getMessage("angal.opd.alltype")))) {
			sqlString.append(" AND DIS_DCL_ID_A = ?");
			parameters.add(diseaseTypeCode);
		}
		if(!diseaseCode.equals(MessageBundle.getMessage("angal.opd.alldisease"))) {
			sqlString.append(" AND DIS_ID_A = ?");
			parameters.add(diseaseCode);
		}
		if (ageFrom != 0 || ageTo != 0) {
			sqlString.append(" AND OPD_AGE BETWEEN ? AND ?");
			parameters.add(ageFrom);
			parameters.add(ageTo);
		}
		if (sex != 'A') {
			sqlString.append(" AND OPD_SEX = ?");
			parameters.add(String.valueOf(sex));
		}
		if (!newPatient.equals("A")) {
			sqlString.append(" AND OPD_NEW_PAT = ?");
			parameters.add(newPatient);
		}
		String stringDateFrom = convertToSQLDateLimited(dateFrom);
		String stringDateTo = convertToSQLDateLimited(dateTo);
		sqlString.append(" AND OPD_DATE_VIS BETWEEN ? AND ?");
		parameters.add(stringDateFrom);
		parameters.add(stringDateTo);
		sqlString.append(" ORDER BY OPD_DATE_VIS");
		
		//System.out.println(sqlString.toString());
		DbQueryLogger dbQuery = new DbQueryLogger();
		
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(sqlString.toString(), parameters, true);
			opdList = new ArrayList<Opd>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Opd opd = new Opd(resultSet.getInt("OPD_PROG_YEAR"),
						resultSet.getString("OPD_SEX").charAt(0),
						resultSet.getInt("OPD_AGE"),
						resultSet.getString("OPD_DIS_ID_A"),
						resultSet.getInt("OPD_LOCK")
				);
				opd.setCode(resultSet.getInt("OPD_ID"));
				opd.setDate(resultSet.getDate("OPD_DATE"));
					GregorianCalendar visitDate = new GregorianCalendar();
					visitDate.setTime(resultSet.getDate("OPD_DATE_VIS"));
				opd.setVisitDate(visitDate);
				opd.setDisease2(resultSet.getString("OPD_DIS_ID_A_2"));
				opd.setDisease3(resultSet.getString("OPD_DIS_ID_A_3"));
				opd.setDiseaseType(resultSet.getString("DIS_DCL_ID_A"));
				opd.setDiseaseDesc(resultSet.getString("DIS_DESC"));
				opd.setDiseaseTypeDesc(resultSet.getString("DCL_DESC"));
				opd.setNewPatient(resultSet.getString("OPD_NEW_PAT"));
				opd.setReferralFrom(resultSet.getString("OPD_REFERRAL_FROM"));
				opd.setReferralTo(resultSet.getString("OPD_REFERRAL_TO"));
				opd.setpatientCode(resultSet.getInt("OPD_PAT_ID"));	
				opd.setFullName(resultSet.getString("PAT_NAME"));
				opd.setNote(resultSet.getString("OPD_NOTE"));
				opd.setfirstName(resultSet.getString("PAT_FNAME"));
				opd.setsecondName(resultSet.getString("PAT_SNAME"));
				opd.setnextKin(resultSet.getString("PAT_NEXT_KIN"));
				opd.setaddress(resultSet.getString("PAT_ADDR"));
				opd.setcity(resultSet.getString("PAT_CITY"));
				opd.setUserID(resultSet.getString("OPD_USR_ID_A"));

				opdList.add(opd);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return opdList;
	}
	
	/**
	 * returns all {@link Opd}s associated to specified patient ID
	 * 
	 * @param patID - the patient ID
	 * @return the list of {@link Opd}s associated to specified patient ID.
	 * 		   the whole list of {@link Opd}s if <code>0</code> is passed.
	 * @throws OHException 
	 */
	public ArrayList<Opd> getOpdList(int patID) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Opd> opdList = null;
		ResultSet resultSet;
		
		if (patID == 0) {
			String sqlString = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID ORDER BY OPD_PROG_YEAR DESC";
			resultSet = dbQuery.getData(sqlString, true);
		} else {
			String sqlString = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID WHERE OPD_PAT_ID = ? ORDER BY OPD_PROG_YEAR DESC";
			List<Object> parameters = Collections.<Object>singletonList(patID);
			resultSet = dbQuery.getDataWithParams(sqlString, parameters, true);
		}
		try {
			opdList = new ArrayList<Opd>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Opd opd = new Opd(resultSet.getInt("OPD_PROG_YEAR"),
						resultSet.getString("OPD_SEX").charAt(0),
						resultSet.getInt("OPD_AGE"),
						resultSet.getString("OPD_DIS_ID_A"),
						resultSet.getInt("OPD_LOCK")
				);
				opd.setCode(resultSet.getInt("OPD_ID"));
				opd.setDate(resultSet.getDate("OPD_DATE"));
					GregorianCalendar visitDate = new GregorianCalendar();
					visitDate.setTime(resultSet.getDate("OPD_DATE_VIS"));
				opd.setVisitDate(visitDate);
				opd.setDisease2(resultSet.getString("OPD_DIS_ID_A_2"));
				opd.setDisease3(resultSet.getString("OPD_DIS_ID_A_3"));
				opd.setNewPatient(resultSet.getString("OPD_NEW_PAT"));
				opd.setReferralFrom(resultSet.getString("OPD_REFERRAL_FROM"));
				opd.setReferralTo(resultSet.getString("OPD_REFERRAL_TO"));
				opd.setpatientCode(resultSet.getInt("OPD_PAT_ID"));	
				opd.setFullName(resultSet.getString("PAT_NAME"));
				opd.setNote(resultSet.getString("OPD_NOTE"));
				opd.setfirstName(resultSet.getString("PAT_FNAME"));
				opd.setsecondName(resultSet.getString("PAT_SNAME"));
				opd.setnextKin(resultSet.getString("PAT_NEXT_KIN"));
				opd.setaddress(resultSet.getString("PAT_ADDR"));
				opd.setcity(resultSet.getString("PAT_CITY"));
				opd.setUserID(resultSet.getString("OPD_USR_ID_A"));

				opdList.add(opd);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}	
		
		return opdList;
	}
		
	/**
	 * insert a new item in the db
	 * 
	 * @param an {@link OPD}
	 * @return <code>true</code> if the item has been inserted
	 * @throws OHException 
	 */
	public boolean newOpd(Opd opd) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		
		Date visitDate = (opd.getVisitDate() == null ? null : new Date(opd.getVisitDate().getTimeInMillis()));
		GregorianCalendar now = new GregorianCalendar();//gets the current time
		
		StringBuilder sqlString = new StringBuilder();
		sqlString.append("INSERT INTO OPD (");
		sqlString.append(" OPD_DATE,");
		sqlString.append(" OPD_PROG_YEAR,");
		sqlString.append(" OPD_SEX, OPD_AGE,");
		sqlString.append(" OPD_DIS_ID_A, OPD_DIS_ID_A_2, OPD_DIS_ID_A_3,");
		sqlString.append(" OPD_NEW_PAT,");
		sqlString.append(" OPD_DATE_VIS,");
		sqlString.append(" OPD_REFERRAL_FROM,");
		sqlString.append(" OPD_REFERRAL_TO,");
		sqlString.append(" OPD_NOTE,");
		sqlString.append(" OPD_PAT_ID,");
		sqlString.append(" OPD_USR_ID_A");
		sqlString.append(") ");
		sqlString.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(convertToSQLDate(now));
		parameters.add(opd.getYear());
		parameters.add(String.valueOf(opd.getSex()));
		parameters.add(opd.getAge());
		parameters.add(opd.getDisease());
		parameters.add(opd.getDisease2());
		parameters.add(opd.getDisease3());
		parameters.add(opd.getNewPatient());
		parameters.add(convertToSQLDateLimited(visitDate));
		parameters.add(opd.getReferralFrom());
		parameters.add(opd.getReferralTo());
		parameters.add(sanitize(opd.getNote()));
		parameters.add(opd.getpatientCode());
		parameters.add(opd.getUserID());
			
		ResultSet r = dbQuery.setDataReturnGeneratedKeyWithParams(sqlString.toString(), parameters, true);
		try {
			if(r.first()){
				opd.setCode(r.getInt(1));
				opd.setDate(now.getTime());
				result = true;
			}   						
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * Checks if the specified {@link Opd} has been modified.
	 * @param opd - the {@link Opd} to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean hasOpdModified(Opd opd) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;

		// we establish if someone else has updated/deleted the record since the last read
		String query = "SELECT OPD_LOCK FROM OPD WHERE OPD_ID = ?";
		List<Object> parameters = Collections.<Object>singletonList(opd.getCode());

		try {
			// we use manual commit of the transaction
			ResultSet resultSet =  dbQuery.getDataWithParams(query, parameters, true);
			if (resultSet.first()) { 
				// ok the record is present, it was not deleted
				result = resultSet.getInt("OPD_LOCK") != opd.getLock();
			} else {
				throw new OHException(MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * modify an {@link OPD} in the db
	 * 
	 * @param an {@link OPD}
	 * @return <code>true</code> if the item has been updated. <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateOpd(Opd opd) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		Date visitDate = (opd.getVisitDate() == null ? null : new Date(opd.getVisitDate().getTimeInMillis()));
		
		try {
			StringBuilder sqlString = new StringBuilder();
			sqlString.append("UPDATE OPD SET");
			sqlString.append(" OPD_SEX = ?,");
			sqlString.append(" OPD_AGE = ?,");
			sqlString.append(" OPD_DIS_ID_A = ?,");
			sqlString.append(" OPD_DIS_ID_A_2 = ?,");
			sqlString.append(" OPD_DIS_ID_A_3 = ?,");
			sqlString.append(" OPD_NEW_PAT = ?,");
			sqlString.append(" OPD_DATE_VIS = ?,");
			sqlString.append(" OPD_REFERRAL_FROM = ?,");
			sqlString.append(" OPD_REFERRAL_TO = ?,");
			sqlString.append(" OPD_NOTE = ?,");
			sqlString.append(" OPD_PAT_ID = ?,");
			sqlString.append(" OPD_USR_ID_A = ?,");
			sqlString.append(" OPD_LOCK = OPD_LOCK + 1 ");
			sqlString.append(" WHERE OPD_ID = ?");
			
			List<Object> parameters = new ArrayList<Object>();
			parameters.add(String.valueOf(opd.getSex()));
			parameters.add(opd.getAge());
			parameters.add(opd.getDisease());
			parameters.add(opd.getDisease2());
			parameters.add(opd.getDisease3());
			parameters.add(opd.getNewPatient());
			parameters.add(convertToSQLDateLimited(visitDate));
			parameters.add(opd.getReferralFrom());
			parameters.add(opd.getReferralTo());
			parameters.add(sanitize(opd.getNote()));
			parameters.add(opd.getpatientCode());
			parameters.add(opd.getUserID());
			parameters.add(opd.getCode());
			
			//System.out.println(sqlstring);
			result = dbQuery.setDataWithParams(sqlString.toString(), parameters, true);
			if (result) opd.setLock(opd.getLock() + 1);
				
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * delete an {@link OPD} from the db
	 * 
	 * @param opd - the {@link OPD} to delete
	 * @return <code>true</code> if the item has been deleted. <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteOpd(Opd opd) throws OHException {
		String sqlString = "DELETE FROM OPD WHERE OPD_ID = ?";
		List<Object> parameters = Collections.<Object>singletonList(opd.getCode());
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		
		result = dbQuery.setDataWithParams(sqlString, parameters, true);
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
		StringBuilder sqlString = new StringBuilder("SELECT MAX(OPD_PROG_YEAR) FROM OPD");
		
		if (year == 0) {
			resultSet = dbQuery.getData(sqlString.toString(), true);
		} else {
			sqlString.append(" WHERE YEAR(OPD_DATE) = ?");
			List<Object> parameters = Collections.<Object>singletonList(year);
			resultSet = dbQuery.getDataWithParams(sqlString.toString(), parameters, true);
		}
		
		try {
			resultSet.next();
			progYear = resultSet.getInt("MAX(OPD_PROG_YEAR)");
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return progYear;
	}
	
	/**
	 * return the last Opd in time associated with specified patient ID. 
	 * 
	 * @param patID - the patient ID
	 * @return last Opd associated with specified patient ID or <code>null</code>
	 * @throws OHException 
	 */
	public Opd getLastOpd(int patID) throws OHException {
		String sqlString = "SELECT * FROM OPD LEFT JOIN PATIENT ON OPD_PAT_ID = PAT_ID WHERE OPD_PAT_ID = ?  ORDER BY OPD_DATE DESC";
		List<Object> parameters = Collections.<Object>singletonList(patID);
		
		Opd opd = null;
		DbQueryLogger dbQuery = new DbQueryLogger();
		
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(sqlString, parameters, true);
			if (resultSet.next()) {
				opd = new Opd(resultSet.getInt("OPD_PROG_YEAR"),
						resultSet.getString("OPD_SEX").charAt(0),
						resultSet.getInt("OPD_AGE"),
						resultSet.getString("OPD_DIS_ID_A"),
						resultSet.getInt("OPD_LOCK")
				);
				opd.setCode(resultSet.getInt("OPD_ID"));
				opd.setDate(resultSet.getDate("OPD_DATE"));
					GregorianCalendar visitDate = new GregorianCalendar();
					visitDate.setTime(resultSet.getDate("OPD_DATE_VIS"));
				opd.setVisitDate(visitDate);
				opd.setDisease2(resultSet.getString("OPD_DIS_ID_A_2"));
				opd.setDisease3(resultSet.getString("OPD_DIS_ID_A_3"));
				opd.setNewPatient(resultSet.getString("OPD_NEW_PAT"));
				opd.setReferralFrom(resultSet.getString("OPD_REFERRAL_FROM"));
				opd.setReferralTo(resultSet.getString("OPD_REFERRAL_TO"));
				opd.setpatientCode(resultSet.getInt("OPD_PAT_ID"));	
				opd.setFullName(resultSet.getString("PAT_NAME"));
				opd.setNote(resultSet.getString("OPD_NOTE"));
				opd.setfirstName(resultSet.getString("PAT_FNAME"));
				opd.setsecondName(resultSet.getString("PAT_SNAME"));
				opd.setnextKin(resultSet.getString("PAT_NEXT_KIN"));
				opd.setaddress(resultSet.getString("PAT_ADDR"));
				opd.setcity(resultSet.getString("PAT_CITY"));
				opd.setUserID(resultSet.getString("OPD_USR_ID_A"));
			}

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return opd;
	}
	
	/**
	 * return a String representing the date in format <code>yyyy-MM-dd HH:mm:ss</code>
	 * 
	 * @param datetime
	 * @return the date in format <code>yyyy-MM-dd HH:mm:ss</code>
	 */
	private String convertToSQLDate(GregorianCalendar datetime) {
		return convertToSQLDate(datetime.getTime());
	}
	
	/**
	 * return a String representing the date in format <code>yyyy-MM-dd HH:mm:ss</code>
	 * 
	 * @param datetime
	 * @return the date in format <code>yyyy-MM-dd HH:mm:ss</code>
	 */
	private String convertToSQLDate(Date datetime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(datetime);
	}
	
	/**
	 * return a String representing the date in format <code>yyyy-MM-dd</code>
	 * 
	 * @param date
	 * @return the date in format <code>yyyy-MM-dd</code>
	 */
	private String convertToSQLDateLimited(GregorianCalendar date) {
		return convertToSQLDateLimited(date.getTime());
	}
	
	/**
	 * return a String representing the date in format <code>yyyy-MM-dd</code>
	 * 
	 * @param date
	 * @return the date in format <code>yyyy-MM-dd</code>
	 */
	private String convertToSQLDateLimited(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
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
		return value.trim().replaceAll("'", "\'");
	}
	
}
