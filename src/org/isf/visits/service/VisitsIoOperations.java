package org.isf.visits.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.visits.model.Visit;
import org.springframework.stereotype.Component;

@Component
public class VisitsIoOperations {

	/**
	 * returns the list of all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID. If <code>0</code> return the list of all {@link Visit}s
	 * @return the list of {@link Visit}s
	 * @throws OHException 
	 */
	public ArrayList<Visit> getVisits(Integer patID) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Visit> visits = null;
		List<Object> parameters = Collections.<Object>singletonList(patID);
		try {
			StringBuilder query = new StringBuilder("SELECT * FROM VISITS");
			if (patID != 0) query.append(" WHERE VST_PAT_ID = ?");
			query.append(" ORDER BY VST_PAT_ID, VST_DATE");
			
			ResultSet resultSet = null;
			if (patID != 0) {
				resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			} else {
				resultSet = dbQuery.getData(query.toString(), true);
			}
			visits = new ArrayList<Visit>(resultSet.getFetchSize());
			while (resultSet.next()) {
				visits.add(toVisit(resultSet));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return visits;
	}

	/**
	 * Insert a new {@link Visit} for a patID
	 * 
	 * @param visit - the {@link Visit} related to patID. 
	 * @return the visitID
	 * @throws OHException 
	 */
	public int newVisit(Visit visit) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> parameters = new ArrayList<Object>();
		int visitID = 0;
		try {
			String query = "INSERT INTO VISITS (VST_ID, VST_PAT_ID, VST_DATE, VST_NOTE, VST_SMS) VALUES (?, ?, ?, ?, ?)";
			parameters.add(visit.getVisitID());
			parameters.add(visit.getPatID());
			parameters.add(visit.getTime());
			parameters.add(visit.getNote());
			parameters.add(visit.isSms() ? 1 : 0);
			
			ResultSet result = dbQuery.setDataReturnGeneratedKeyWithParams(query, parameters, true);

			if (result.next()) visitID = result.getInt(1);
			
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return visitID;
	}
	
	/**
	 * Deletes all {@link Visit}s related to a patID
	 * 
	 * @param patID - the {@link Patient} ID
	 * @return <code>true</code> if the list has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteAllVisits(int patID) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(patID);
		boolean result = true;
		try {
			String query = "DELETE FROM VISITS WHERE VST_PAT_ID = ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}
	
	private Visit toVisit(ResultSet resultSet) throws SQLException {
		Visit visit = new Visit();
		visit.setVisitID(resultSet.getInt("VST_ID"));
		visit.setPatID(resultSet.getInt("VST_PAT_ID"));
		visit.setTime(resultSet.getTimestamp("VST_DATE"));
		visit.setNote(resultSet.getString("VST_NOTE"));
		visit.setSms(resultSet.getBoolean("VST_SMS"));
		return visit;
	}
}
