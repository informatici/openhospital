package org.isf.hospital.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.hospital.model.Hospital;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class offers the io operations for recovering and
 * managing hospital record from the database
 * 
 * @author Fin8, Furla, Thoia
 * 
 */
@Component
public class HospitalIoOperations {
	@Autowired
	private DbJpaUtil jpa;
	
	/**
	 * Reads from database hospital informations
	 * 
	 * @return {@link Hospital} object
	 * @throws OHException 
	 */
    @SuppressWarnings("unchecked")
	public Hospital getHospital() throws OHException 
	{
		
		ArrayList<Hospital> hospitals = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM HOSPITAL";
		jpa.createQuery(query, Hospital.class, false);
		List<Hospital> hospitalList = (List<Hospital>)jpa.getList();
		hospitals = new ArrayList<Hospital>(hospitalList);			
		
		jpa.commitTransaction();

		return hospitals.get(0);
	}
	
	/**
	 * Reads from database currency cod
	 * @return currency cod
	 * @throws OHException
	 */
	public String getHospitalCurrencyCod() throws OHException
	{
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
	public boolean updateHospital(
			Hospital hospital) throws OHException 
	{
		
		boolean result = true;
		
		hospital.setLock(hospital.getLock()+1);
		
		jpa.beginTransaction();	
		jpa.merge(hospital);
    	jpa.commitTransaction();
		
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
		String result = null;
		
		
		if (value != null) 
		{
			result = value.trim().replaceAll("'", "''");
		}
		
		return result;
	}
}