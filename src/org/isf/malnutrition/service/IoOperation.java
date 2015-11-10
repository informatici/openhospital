package org.isf.malnutrition.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * Persistence class for the malnutrition module.
 */
@Component
public class IoOperation {

	/**
	 * Returns all the available {@link Malnutrition} for the specified admission id.
	 * @param admissionId the admission id
	 * @return the retrieved malnutrition.
	 * @throws OHException if an error occurs retrieving the malnutrition list.
	 */
	public ArrayList<Malnutrition> getMalnutritions(String admissionId) throws OHException{

		ArrayList<Malnutrition> malnutritions = new ArrayList<Malnutrition>();
		DbQueryLogger dbQuery = new DbQueryLogger();

		try{
			String query = "select * from MALNUTRITIONCONTROL where MLN_ADM_ID = ? ORDER BY MLN_DATE_SUPP";
			List<Object> parameters = Collections.<Object>singletonList(admissionId);
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);

			malnutritions = new ArrayList<Malnutrition>(resultSet.getFetchSize());

			while (resultSet.next()) {
				Malnutrition malnutrition = new Malnutrition(
						resultSet.getInt("MLN_ID"),
						toCalendar(resultSet.getDate("MLN_DATE_SUPP")),
						toCalendar(resultSet.getDate("MNL_DATE_CONF")),
						resultSet.getInt("MLN_ADM_ID"),
						resultSet.getFloat("MLN_HEIGHT"),
						resultSet.getFloat("MLN_WEIGHT"),
						resultSet.getInt("MLN_LOCK"));
				malnutritions.add(malnutrition);
			}

		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return malnutritions;
	}

	/**
	 * Stores a new {@link Malnutrition}. The malnutrition object is updated with the generated id.
	 * @param malnutrition the malnutrition to store.
	 * @return <code>true</code> if the malnutrition has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the malnutrition.
	 */
	public boolean newMalnutrition(Malnutrition malnutrition) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try{
			String query = "INSERT INTO MALNUTRITIONCONTROL " +
					"(MLN_DATE_SUPP, MNL_DATE_CONF, MLN_ADM_ID, MLN_HEIGHT, MLN_WEIGHT) VALUES " +
					"(?,?,?,?,?)";

			List<Object> parameters = new ArrayList<Object>(6);
			parameters.add(toDate(malnutrition.getDateSupp()));
			parameters.add(toDate(malnutrition.getDateConf()));
			parameters.add(malnutrition.getAdmId());
			parameters.add(malnutrition.getHeight());
			parameters.add(malnutrition.getWeight());

			ResultSet rs = dbQuery.setDataReturnGeneratedKeyWithParams(query, parameters, true);
			if (rs.first()){
				malnutrition.setCode(rs.getInt(1));
				result = true;
			}

		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;	
	}

	/**
	 * Retrieves the lock value for the specified {@link Malnutrition} code.
	 * @param malnutritionCode the {@link Malnutrition} code.
	 * @return the retrieved code or -1 if no malnutrition has been found. 
	 * @throws OHException if an error occurs retrieving the code.
	 */
	public int getMalnutritionLock(int malnutritionCode) throws OHException
	{
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			String query = "SELECT MLN_LOCK FROM MALNUTRITIONCONTROL where MLN_ID = ?";
			List<Object> parameters = Collections.<Object>singletonList(malnutritionCode);
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			if (set.first()) return set.getInt("MLN_LOCK");	
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return -1;
	}

	/**
	 * Updates the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to update.
	 * @return <code>true</code> if the malnutrition has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs updating the malnutrition.
	 */
	public boolean updateMalnutrition(Malnutrition malnutrition) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try{
			String query = "UPDATE MALNUTRITIONCONTROL SET " +
					" MLN_DATE_SUPP = ?," +
					" MNL_DATE_CONF = ?,"+
					" MLN_HEIGHT = ?,"+
					" MLN_WEIGHT = ?,"+
					" MLN_LOCK = MLN_LOCK + 1" +
					" WHERE MLN_ID = ?";

			List<Object> parameters = new ArrayList<Object>(5);
			parameters.add(toDate(malnutrition.getDateSupp()));
			parameters.add(toDate(malnutrition.getDateConf()));
			parameters.add(malnutrition.getHeight());
			parameters.add(malnutrition.getWeight());
			parameters.add(malnutrition.getCode());
			
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		
		if (result) malnutrition.setLock(getMalnutritionLock(malnutrition.getCode()));
		return result;	
	}
	
	/**
	 * returns the last {@link Malnutrition} entry for specified patient ID
	 * @param patientID - the patient ID
	 * @return the last {@link Malnutrition} for specified patient ID. <code>null</code> if none.
	 * @throws OHException
	 */
	public Malnutrition getLastMalnutrition(int patientID) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(patientID);
		Malnutrition theMalnutrition = null;
		String query = "SELECT * FROM MALNUTRITIONCONTROL WHERE MLN_ADM_ID = ? ORDER BY MLN_DATE_SUPP DESC LIMIT 1";
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			if (resultSet.first()) {
				theMalnutrition = new Malnutrition(
						resultSet.getInt("MLN_ID"),
						toCalendar(resultSet.getDate("MLN_DATE_SUPP")),
						toCalendar(resultSet.getDate("MNL_DATE_CONF")),
						resultSet.getInt("MLN_ADM_ID"),
						resultSet.getFloat("MLN_HEIGHT"),
						resultSet.getFloat("MLN_WEIGHT"),
						resultSet.getInt("MLN_LOCK"));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return theMalnutrition;
	}

	/**
	 * Deletes the specified {@link Malnutrition}.
	 * @param malnutrition the malnutrition to delete.
	 * @return <code>true</code> if the malnutrition has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs deleting the specified malnutrition.
	 */
	public boolean deleteMalnutrition(Malnutrition malnutrition) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			String query = "DELETE FROM MALNUTRITIONCONTROL WHERE MLN_ID = ?";
			List<Object> parameters = Collections.<Object>singletonList(malnutrition.getCode());
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;	
	}

	/**
	 * Converts a {@link GregorianCalendar} to a {@link Date}.
	 * @param calendar the calendar to convert.
	 * @return the converted value or <code>null</code> if the passed value is <code>null</code>.
	 */
	protected Date toDate(GregorianCalendar calendar)
	{
		if (calendar == null) return null;
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Converts the specified {@link java.sql.Date} to a {@link GregorianCalendar}.
	 * @param date the date to convert.
	 * @return the converted date.
	 */
	protected GregorianCalendar toCalendar(Date date){
		if (date == null) return null;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}
}
