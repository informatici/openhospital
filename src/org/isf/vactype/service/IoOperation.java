package org.isf.vactype.service;

/*------------------------------------------
 * IoOperation - methods to interact with DB
 * -----------------------------------------
 * modification history
 * 19/10/2011 - Cla - version is now 1.0
 *------------------------------------------*/


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.vactype.model.VaccineType;


public class IoOperation {
	
	/**
	 * returns all {@link VaccineType}s from DB	
	 * 	
	 * @return the list of {@link VaccineType}s
	 * @throws OHException 
	 */
	public ArrayList<VaccineType> getVaccineType() throws OHException {
		ArrayList<VaccineType> pVT = null;
		String sqlString = "SELECT * FROM VACCINETYPE ORDER BY VACT_ID_A";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(sqlString, true);
			pVT = new ArrayList<VaccineType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				pVT.add(new VaccineType(resultSet.getString("VACT_ID_A"), resultSet.getString("VACT_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return pVT;
	}
	
	/**
	 * inserts a new {@link VaccineType} into DB
	 * 
	 * @param vaccineType - the {@link VaccineType} to insert 
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newVaccineType(VaccineType vaccineType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlString = "INSERT INTO VACCINETYPE (VACT_ID_A, VACT_DESC) VALUES (?, ?)";
			parameters.add(vaccineType.getCode());
			parameters.add(vaccineType.getDescription());
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * updates a {@link VaccineType} in the DB
	 *
	 * @param vaccineType - the item to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateVaccineType(VaccineType vaccineType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		try {
			String sqlString = "UPDATE VACCINETYPE SET VACT_DESC = ? WHERE VACT_ID_A = ?";
			parameters.add(vaccineType.getDescription());
			parameters.add(vaccineType.getCode());
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	/**
	 * deletes a {@link VaccineType} in the DB
	 *
	 * @param vaccineType - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteVaccineType(VaccineType vaccineType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(vaccineType.getCode());
		boolean result = false;
		try {
			String sqlString = "DELETE FROM VACCINETYPE WHERE VACT_ID_A = ?";
			result = dbQuery.setDataWithParams(sqlString, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	
	/**
	 * checks if the code is already in use
	 *
	 * @param code - the {@link VaccineType} code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(code);
		boolean present = false;
		try {
			String sqlstring = "SELECT VACT_ID_A FROM VACCINETYPE WHERE VACT_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(sqlstring, parameters, true);
			if(set.first()) present = true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.vactype.problemsoccuredwithserverconnection"));
			dbQuery.rollback();
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
}
