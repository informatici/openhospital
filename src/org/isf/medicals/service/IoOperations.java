/**
 * 11-dec-2005
 * 14-jan-2006
 * author bob
 */
package org.isf.medicals.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.medicals.model.*;
import org.isf.medtype.model.MedicalType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;


/**
 * This class offers the io operations for recovering and managing
 * medical records from the database
 * 
 * @author bob 
 * 		   modified by alex:
 * 			- column product code
 * 			- column pieces per packet
 */
public class IoOperations {

	/**
	 * Retrieves the specified {@link Medical}.
	 * @param code the medical code
	 * @return the stored medical.
	 * @throws OHException if an error occurs retrieving the stored medical.
	 */
	public Medical getMedical(int code) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(code);
		String query = "select * from MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A = MDSRT_ID_A where MDSR_ID = ?";

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			if(resultSet.next()) {

				MedicalType medicalType = new MedicalType(
						resultSet.getString("MDSR_MDSRT_ID_A"), 
						resultSet.getString("MDSRT_DESC"));

				Medical medical = new Medical(
						resultSet.getInt("MDSR_ID"),
						medicalType, 
						resultSet.getString("MDSR_CODE"),
						resultSet.getString("MDSR_DESC"),
						resultSet.getDouble("MDSR_INI_STOCK_QTI"), 
						resultSet.getInt("MDSR_PCS_X_PCK"),
						resultSet.getDouble("MDSR_MIN_STOCK_QTI"),
						resultSet.getDouble("MDSR_IN_QTI"),
						resultSet.getDouble("MDSR_OUT_QTI"),
						resultSet.getInt("MDSR_LOCK"));
				return medical;
				
			} else return null;
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
	}

	/**
	 * Gets all stored {@link Medical}s.
	 * @return all the stored medicals.
	 * @throws OHException if an error occurs retrieving the stored medicals.
	 */
	public ArrayList<Medical> getMedicals() throws OHException {
		return getMedicals(null);
	}

	/**
	 * Retrieves all stored {@link Medical}s.
	 * If a description value is provides the medicals are filtered.
	 * @param description the medical description.
	 * @return the stored medicals.
	 * @throws OHException if an error occurs retrieving the stored medicals.
	 */
	public ArrayList<Medical> getMedicals(String description) throws OHException {
		ArrayList<Medical> medicals = null;

		List<Object> parameters = new ArrayList<Object>();

		StringBuilder query = new StringBuilder();
		query.append("select * from MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A = MDSRT_ID_A ");

		if (description!=null) {
			query.append("where MDSRT_DESC like ? ");
			parameters.add(description);
		}

		query.append("order BY MDSR_DESC");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			medicals = new ArrayList<Medical>(resultSet.getFetchSize());
			while (resultSet.next()) {
				medicals.add(new Medical(resultSet.getInt("MDSR_ID"),
						new MedicalType(resultSet.getString("MDSR_MDSRT_ID_A"), 
								resultSet.getString("MDSRT_DESC")), 
								resultSet.getString("MDSR_CODE"),
								resultSet.getString("MDSR_DESC"),
								resultSet.getDouble("MDSR_INI_STOCK_QTI"), 
								resultSet.getInt("MDSR_PCS_X_PCK"),
								resultSet.getDouble("MDSR_MIN_STOCK_QTI"),
								resultSet.getDouble("MDSR_IN_QTI"),
								resultSet.getDouble("MDSR_OUT_QTI"),
								resultSet.getInt("MDSR_LOCK")));
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return medicals;
	}

	/**
	 * Retrieves the stored {@link Medical}s based on the specified filter criteria.
	 * @param description the medical description or <code>null</code>
	 * @param type the medical type or <code>null</code>
	 * @param expiring <code>true</code> if include only expiring medicals.
	 * @return the retrieved medicals.
	 * @throws OHException if an error occurs retrieving the medicals.
	 */
	public ArrayList<Medical> getMedicals(String description, String type, boolean expiring) throws OHException {
		ArrayList<Medical> medicals = null;

		List<Object> parameters = new ArrayList<Object>();
		StringBuilder query = new StringBuilder();
		query.append("select * from MEDICALDSR join MEDICALDSRTYPE on MDSR_MDSRT_ID_A = MDSRT_ID_A ");

		if (description!=null) {
			query.append("where ");
			query.append("(MDSR_DESC like ? OR MDSR_CODE like ?) ");
			parameters.add("%"+description+"%");
			parameters.add("%"+description+"%");
		}

		if(type!=null){
			if (parameters.size()==0) query.append("where ");
			else query.append("and ");

			query.append("(MDSRT_ID_A=?) ");
			parameters.add(type);
		}

		if(expiring){
			if (parameters.size()==0) query.append("where ");
			else query.append("and ");

			query.append("((MDSR_INI_STOCK_QTI+MDSR_IN_QTI-MDSR_OUT_QTI)<MDSR_MIN_STOCK_QTI) ");
		}

		query.append("order BY MDSR_MDSRT_ID_A, MDSR_DESC");

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query.toString(), parameters, true);
			medicals = new ArrayList<Medical>(resultSet.getFetchSize());
			while (resultSet.next()) {
				medicals.add(new Medical(resultSet.getInt("MDSR_ID"),
						new MedicalType(resultSet.getString("MDSR_MDSRT_ID_A"), 
								resultSet.getString("MDSRT_DESC")), 
								resultSet.getString("MDSR_CODE"),
								resultSet.getString("MDSR_DESC"),
								resultSet.getDouble("MDSR_INI_STOCK_QTI"), 
								resultSet.getInt("MDSR_PCS_X_PCK"),
								resultSet.getDouble("MDSR_MIN_STOCK_QTI"),
								resultSet.getDouble("MDSR_IN_QTI"),
								resultSet.getDouble("MDSR_OUT_QTI"),
								resultSet.getInt("MDSR_LOCK")));
			}
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return medicals;
	}

	/**
	 * Checks if the specified {@link Medical} exists or not.
	 * @param medical the medical to check.
	 * @return <code>true</code> if exists <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean medicalExists(Medical medical) throws OHException
	{
		boolean result = false;
		List<Object> parameters = new ArrayList<Object>(2);
		parameters.add(medical.getType().getCode());
		parameters.add(medical.getDescription());
		String query = "select MDSR_ID from MEDICALDSR where MDSR_MDSRT_ID_A = ? and MDSR_DESC = ?";		
		DbQueryLogger dbQuery = new DbQueryLogger();

		try{
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			result = set.first();
		}catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return result;
	}

	/**
	 * Stores the specified {@link Medical}.
	 * @param medical the medical to store.
	 * @return <code>true</code> if the medical has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the medical.
	 */
	public boolean newMedical(Medical medical) throws OHException {

		List<Object> parameters = new ArrayList<Object>(5);
		parameters.add(medical.getType().getCode());
		parameters.add(medical.getProd_code());
		parameters.add(medical.getDescription());
		parameters.add(medical.getMinqty());
		parameters.add(medical.getPcsperpck());

		String query = "insert into MEDICALDSR (MDSR_MDSRT_ID_A , MDSR_CODE, MDSR_DESC, MDSR_MIN_STOCK_QTI, MDSR_PCS_X_PCK) " +
				"values (?, ?, ?, ?, ?)";

		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try{
			ResultSet rs = dbQuery.setDataReturnGeneratedKeyWithParams(query, parameters, true);	
			if (rs.first())	{
				medical.setCode(rs.getInt(1));
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
	 * Returns the stored medical lock value.
	 * @param code the medical code.
	 * @return the stored lock value.
	 * @throws OHException if an error occurs retrieving the lock value.
	 */
	public int getMedicalLock(int code) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(code);

		String query = "select MDSR_LOCK from MEDICALDSR where MDSR_ID = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			if(set.first()) return set.getInt(1);
			else return -1;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
	}

	/**
	 * Updates the specified {@link Medical}.
	 * @param medical the medical to update.
	 * @return <code>true</code> if the medical has been updated <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMedical(Medical medical) throws OHException {

		boolean result = false;

		List<Object> parameters = new ArrayList<Object>(5);
		parameters.add(medical.getDescription());
		parameters.add(medical.getProd_code());
		parameters.add(medical.getPcsperpck());
		parameters.add(medical.getMinqty());
		parameters.add(medical.getCode());

		String query = "update MEDICALDSR set MDSR_DESC = ?, MDSR_CODE = ?, MDSR_PCS_X_PCK = ?, " +
				"MDSR_LOCK = MDSR_LOCK + 1 , MDSR_MIN_STOCK_QTI = ? where MDSR_ID = ?";

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}	
		if (result) medical.setLock(getMedicalLock(medical.getCode()));
		return result;
	}

	/**
	 * Checks if the specified {@link Medical} is referenced in stock movement.
	 * @param code the medical code.
	 * @return <code>true</code> if the medical is referenced, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isMedicalReferencedInStockMovement(int code) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(code);
		String query = "select * from MEDICALDSRSTOCKMOV where MMV_MDSR_ID = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet rs = dbQuery.getDataWithParams(query, parameters, true);
			return rs.first();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
	}

	/**
	 * Deletes the specified {@link Medical}.
	 * @param medical the medical to delete.
	 * @return <code>true</code> if the medical has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the medical deletion.
	 */
	public boolean deleteMedical(Medical medical) throws OHException{

		List<Object> parameters = Collections.<Object>singletonList(medical.getCode());
		String query = "delete from MEDICALDSR where MDSR_ID = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try{
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}	
		return result;
	}
}
