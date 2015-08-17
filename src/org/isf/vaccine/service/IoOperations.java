package org.isf.vaccine.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vactype.model.VaccineType;

/**
 * This class offers the io operations for recovering and managing
 * vaccine records from the database
 *
 * @author Eva
 * 
 * modification history
 * 20/10/2011 - Cla - insert vaccinetype managment
 *
 */


public class IoOperations {

	/**
	 * returns the list of {@link Vaccine}s based on vaccine type code
	 *
	 * @param vaccineTypeCode - the type code. If <code>null</code> returns all {@link Vaccine}s in the DB
	 * @return the list of {@link Vaccine}s
	 * @throws OHException 
	 */
	public ArrayList<Vaccine> getVaccine(String vaccineTypeCode) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Vaccine> pVaccine = null;
		ResultSet resultSet = null;
		List<Object> parameters = Collections.<Object>singletonList(vaccineTypeCode);
		StringBuilder sqlString = new StringBuilder("SELECT * FROM VACCINE JOIN VACCINETYPE ON VAC_VACT_ID_A = VACT_ID_A");
		if (vaccineTypeCode != null) {
			sqlString.append(" WHERE VAC_VACT_ID_A = ?");
		}
		sqlString.append(" ORDER BY VAC_DESC");
		
		try {
			if (vaccineTypeCode != null) {
				resultSet = dbQuery.getDataWithParams(sqlString.toString(), parameters, true);
			} else {
				resultSet = dbQuery.getData(sqlString.toString(), true);
			}
			pVaccine = new ArrayList<Vaccine>(resultSet.getFetchSize());
			while (resultSet.next()) {
				pVaccine.add(new Vaccine (resultSet.getString("VAC_ID_A"),
						                  resultSet.getString("VAC_DESC"),
						                  new VaccineType(resultSet.getString("VACT_ID_A"),
						                		          resultSet.getString("VACT_DESC")),
						                  resultSet.getInt("VAC_LOCK")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return pVaccine;
	}

	/**
	 * inserts a new {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newVaccine(Vaccine vaccine) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlstring = "INSERT INTO VACCINE (VAC_ID_A, VAC_DESC, VAC_VACT_ID_A) VALUES (?, ?, ?)";
			parameters.add(vaccine.getCode());
			parameters.add(vaccine.getDescription());
			parameters.add(vaccine.getVaccineType().getCode());
			
			result = dbQuery.setDataWithParams(sqlstring, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * checks if the specified {@link Vaccine} has been modified.
	 * 
	 * @param vaccine - the {@link Vaccine} to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean hasVaccineModified(Vaccine vaccine) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;

		// we establish if someone else has updated/deleted the record since the last read
		String query = "SELECT VAC_LOCK FROM VACCINE WHERE VAC_ID_A = ?";
		List<Object> parameters = Collections.<Object>singletonList(vaccine.getCode());

		try {
			// we use manual commit of the transaction
			ResultSet resultSet =  dbQuery.getDataWithParams(query, parameters, true);
			if (resultSet.first()) { 
				// ok the record is present, it was not deleted
				result = resultSet.getInt("VAC_LOCK") != vaccine.getLock();
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
	 * updates a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateVaccine(Vaccine vaccine) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlstring = "UPDATE VACCINE SET" +
			" VAC_DESC = ?," +
			" VAC_VACT_ID_A = ?," +
			" VAC_LOCK = VAC_LOCK + 1" +
			" WHERE VAC_ID_A = ?";
			
			parameters.add(vaccine.getDescription());
			parameters.add(vaccine.getVaccineType().getCode());
			parameters.add(vaccine.getCode());
			
			result = dbQuery.setDataWithParams(sqlstring, parameters, true);
			if (result) vaccine.setLock(vaccine.getLock()+1);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * deletes a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteVaccine(Vaccine vaccine) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(vaccine.getCode());
		boolean result = false;
		try {
			String string = "DELETE FROM VACCINE WHERE VAC_ID_A = ?";
			result = dbQuery.setDataWithParams(string, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the vaccine code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(code);
		boolean present=false;
		try {

			String sqlstring = "SELECT VAC_ID_A FROM VACCINE WHERE VAC_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(sqlstring, parameters, true);
			if(set.first()) present=true;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
}


