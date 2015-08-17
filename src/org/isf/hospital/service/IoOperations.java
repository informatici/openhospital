package org.isf.hospital.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.isf.generaldata.MessageBundle;
import org.isf.hospital.model.Hospital;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

/**
 * This class offers the io operations for recovering and
 * managing hospital record from the database
 * 
 * @author Fin8, Furla, Thoia
 * 
 */
public class IoOperations {
	
	/**
	 * Reads from database hospital informations
	 * 
	 * @return {@link Hospital} object
	 * @throws OHException 
	 */
	public Hospital getHospital() throws OHException {
		Hospital pHospital = null;
		String string = "SELECT * FROM HOSPITAL";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {	
			ResultSet resultSet = dbQuery.getData(string, true);
			pHospital = new Hospital();
			if (resultSet.first()) {
				pHospital.setCode(resultSet.getString("HOS_ID_A"));
				pHospital.setDescription(resultSet.getString("HOS_NAME"));
				pHospital.setAddress(resultSet.getString("HOS_ADDR"));
				pHospital.setCity(resultSet.getString("HOS_CITY"));
				pHospital.setTelephone(resultSet.getString("HOS_TELE"));
				pHospital.setFax(resultSet.getString("HOS_FAX"));
				pHospital.setEmail(resultSet.getString("HOS_EMAIL"));
				pHospital.setCurrencyCod(resultSet.getString("HOS_CURR_COD"));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return pHospital;
	}
	
	/**
	 * Reads from database currency cod
	 * @return currency cod
	 * @throws OHException
	 */
	public String getHospitalCurrencyCod() throws OHException{
		String query = "SELECT HOS_CURR_COD FROM HOSPITAL";
		String currencyCod = "";
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {	
			ResultSet resultSet = dbQuery.getData(query, true);
			if (resultSet.first()) {
				currencyCod = resultSet.getString("HOS_CURR_COD");
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		
		return currencyCod;
	}
	
	/**
	 * updates hospital informations
	 * 
	 * @return <code>true</code> if the hospital informations have been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateHospital(Hospital hospital) throws OHException {
		
		boolean result = false;
		String query= "UPDATE HOSPITAL SET " +
		"HOS_NAME = ?, " +
		"HOS_ADDR = ?, " +
		"HOS_CITY = ?, " +
		"HOS_TELE = ?, " +
		"HOS_FAX = ?, " +
		"HOS_EMAIL = ?, " +
		"HOS_CURR_COD = ?";
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(sanitize(hospital.getDescription()));
		params.add(sanitize(hospital.getAddress()));
		params.add(sanitize(hospital.getCity()));
		params.add(sanitize(hospital.getTelephone()));
		params.add(sanitize(hospital.getFax()));
		params.add(sanitize(hospital.getEmail()));
		params.add(sanitize(hospital.getCurrencyCod()));
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {			
			result = dbQuery.setDataWithParams(query, params, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;	
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