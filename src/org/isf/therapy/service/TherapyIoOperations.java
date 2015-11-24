package org.isf.therapy.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medtype.model.MedicalType;
import org.isf.therapy.model.TherapyRow;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

@Component
public class TherapyIoOperations {

	/**
	 * insert a new {@link TherapyRow} (therapy) in the DB
	 * 
	 * @param thRow - the {@link TherapyRow} (therapy)
	 * @param numTherapy - the therapy progressive number for the patient
	 * @return the therapyID
	 * @throws OHException 
	 */
	public int newTherapy(TherapyRow thRow) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		ArrayList<Object> parameters = new ArrayList<Object>();
		int therapyID = 0;
		try {

			String query = "INSERT INTO THERAPIES (THR_ID, THR_PAT_ID, THR_STARTDATE, THR_ENDDATE, THR_MDSR_ID, THR_QTY, THR_UNT_ID, THR_FREQINDAY, THR_FREQINPRD, THR_NOTE, THR_NOTIFY, THR_SMS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			parameters.add(thRow.getTherapyID());
			parameters.add(thRow.getPatID());
			parameters.add(thRow.getStartDate().getTime());
			parameters.add(thRow.getEndDate().getTime());
			parameters.add(thRow.getMedical().getCode());
			parameters.add(thRow.getQty());
			parameters.add(thRow.getUnitID());
			parameters.add(thRow.getFreqInDay());
			parameters.add(thRow.getFreqInPeriod());
			parameters.add(thRow.getNote());
			parameters.add(thRow.isNotify() ? 1 : 0);
			parameters.add(thRow.isSms() ? 1 : 0);

			ResultSet result = dbQuery.setDataReturnGeneratedKeyWithParams(query, parameters, true);

			if (result.next()) therapyID = result.getInt(1);
			
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return therapyID;
	}

	/**
	 * return the list of {@link TherapyRow}s (therapies) for specified Patient ID
	 * or
	 * return all {@link TherapyRow}s (therapies) if <code>0</code> is passed
	 * 
	 * @param patID - the Patient ID
	 * @return the list of {@link TherapyRow}s (therapies)
	 * @throws OHException 
	 */
	public ArrayList<TherapyRow> getTherapyRows(int patID) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(patID);
		ArrayList<TherapyRow> thRows = null;
		ResultSet resultSet = null;
		try {

			StringBuilder query = new StringBuilder();
			query.append("SELECT * FROM THERAPIES JOIN (MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A) ON THR_MDSR_ID = MDSR_ID");
			if (patID != 0) query.append(" WHERE THR_PAT_ID = ?");
			query.append(" ORDER BY THR_PAT_ID, THR_ID");

			if (patID != 0) {
				resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			} else {
				resultSet = dbQuery.getData(query.toString(), true);
			}

			thRows = new ArrayList<TherapyRow>(resultSet.getFetchSize());
			while (resultSet.next()) {
				TherapyRow th = new TherapyRow(resultSet.getInt("THR_ID"), resultSet.getInt("THR_PAT_ID"), convertToGregorianCalendar(resultSet.getTimestamp("THR_STARTDATE")),
						convertToGregorianCalendar(resultSet.getTimestamp("THR_ENDDATE")), new Medical(resultSet.getInt("MDSR_ID"), new MedicalType(resultSet.getString("MDSR_MDSRT_ID_A"),
								resultSet.getString("MDSRT_DESC")), resultSet.getString("MDSR_CODE"), resultSet.getString("MDSR_DESC"), resultSet.getDouble("MDSR_INI_STOCK_QTI"), resultSet
								.getInt("MDSR_PCS_X_PCK"), resultSet.getDouble("MDSR_MIN_STOCK_QTI"), resultSet.getDouble("MDSR_IN_QTI"), resultSet.getDouble("MDSR_OUT_QTI"), resultSet
								.getInt("MDSR_LOCK")), resultSet.getDouble("THR_QTY"), resultSet.getInt("THR_UNT_ID"), resultSet.getInt("THR_FREQINDAY"), resultSet.getInt("THR_FREQINPRD"),
						resultSet.getString("THR_NOTE"), resultSet.getBoolean("THR_NOTIFY"), resultSet.getBoolean("THR_SMS"));

				thRows.add(th);
			}
		} catch (Exception e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return thRows;
	}
	
	/**
	 * delete all {@link TherapyRow}s (therapies) for specified Patient ID
	 * 
	 * @param patID - the Patient ID
	 * @return <code>true</code> if the therapies have been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteAllTherapies(int patID) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(patID);
		boolean result = false;
		try {
			String query = "DELETE FROM THERAPIES WHERE THR_PAT_ID = ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally {
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * convert passed {@link Date} to a {@link GregorianCalendar}
	 * @param aDate - the {@link Date} to convert
	 * @return {@link GregorianCalendar}
	 */
	public GregorianCalendar convertToGregorianCalendar(Timestamp aDate) {
		if (aDate == null)
			return null;
		GregorianCalendar time = new GregorianCalendar();
		time.setTime(aDate);
		return time;
	}
}
