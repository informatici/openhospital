package org.isf.disease.service;

/*------------------------------------------
 * disease.service.IoOperations 
 * 			This class offers the io operations for recovering and managing
 * 			diseases records from the database
 * -----------------------------------------
 * modification history
 * 25/01/2006 - Rick, Vero, Pupo  - first beta version 
 * 08/11/2006 - ross - added support for OPD and IPD flags
 * 09/06/2007 - ross - when updating, now the user can change the "dis type" also
 * 02/09/2008 - alex - added method for getting a Disease by his code
 * 					   added method for getting a DiseaseType by his code
 * 13/02/2009 - alex - modified query for ordering resultset
 *                     by description only	
 *------------------------------------------*/

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.disease.model.Disease;
import org.isf.distype.model.DiseaseType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * This class offers the io operations for recovering and managing
 * diseases records from the database
 * 
 * @author Rick, Vero
 */
@Component
public class DiseaseIoOperations {

	/**
	 * Gets a {@link Disease} with the specified code.
	 * @param code the disease code.
	 * @return the found disease, <code>null</code> if no disease has found.
	 * @throws OHException if an error occurred getting the disease.
	 */
	public Disease getDiseaseByCode(int code) throws OHException {
		Disease disease = null;
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "select * from DISEASE join DISEASETYPE on DIS_DCL_ID_A = DCL_ID_A WHERE DIS_ID_A = ?";

			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			while (resultSet.next()) {
				disease = new Disease(resultSet.getString("DIS_ID_A"), 
						resultSet.getString("DIS_DESC"), 
						new DiseaseType(resultSet.getString("DIS_DCL_ID_A"), resultSet.getString("DCL_DESC")),
						resultSet.getInt("DIS_LOCK"));
				disease.setOpdInclude(resultSet.getInt("DIS_OPD_INCLUDE")==1);
				disease.setIpdInInclude(resultSet.getInt("DIS_IPD_IN_INCLUDE")==1);
				disease.setIpdOutInclude(resultSet.getInt("DIS_IPD_OUT_INCLUDE")==1);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return disease;
	}
	
	/**
	 * Retrieves stored disease with the specified search parameters.
	 * @param disTypeCode - not <code>null</code> apply to disease type
	 * @param opd - select only diseases related to out patient
	 * @param ipdIn - select only diseases related to in patient admission
	 * @param ipdOut - select only diseases related to in patient outcome
	 * @return the retrieved diseases.
	 * @throws OHException if an error occurs retrieving the diseases.
	 */
	public ArrayList<Disease> getDiseases(String disTypeCode, boolean opd, boolean ipdIn, boolean ipdOut) throws OHException {
		ArrayList<Disease> diseases = null;
		String selectClause = "select * from DISEASE join DISEASETYPE on DIS_DCL_ID_A = DCL_ID_A";
		String whereClause = "";

		List<Object> parameters = new ArrayList<Object>(1);

		if (disTypeCode != null) {
			whereClause= " where DCL_ID_A like ?";
			parameters.add(disTypeCode);
		}

		if (opd) {
			if (whereClause.equals("")) whereClause=" where "; else whereClause+=" and  "; 
			whereClause+=" (DIS_OPD_INCLUDE=1 or DIS_OPD_INCLUDE is null) ";
		}

		if (ipdIn) {
			if (whereClause.equals("")) whereClause=" where "; else whereClause+=" and  "; 
			whereClause+=" (DIS_IPD_IN_INCLUDE=1 or DIS_IPD_IN_INCLUDE is null) ";
		}
		
		if (ipdOut) {
			if (whereClause.equals("")) whereClause=" where "; else whereClause+=" and  "; 
			whereClause+=" (DIS_IPD_OUT_INCLUDE=1 or DIS_IPD_OUT_INCLUDE is null) ";
		}
		
		String orderClause = " order BY DIS_DESC";

		String query =  selectClause + whereClause + orderClause;

		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			diseases = new ArrayList<Disease>(resultSet.getFetchSize());
			while (resultSet.next()) {
				Disease disease = new Disease(resultSet.getString("DIS_ID_A"), 
						resultSet.getString("DIS_DESC"), 
						new DiseaseType(resultSet.getString("DIS_DCL_ID_A"), resultSet.getString("DCL_DESC")),
						resultSet.getInt("DIS_LOCK"));
				disease.setOpdInclude(resultSet.getInt("DIS_OPD_INCLUDE")==1);
				disease.setIpdInInclude(resultSet.getInt("DIS_IPD_IN_INCLUDE")==1);
				disease.setIpdOutInclude(resultSet.getInt("DIS_IPD_OUT_INCLUDE")==1);

				diseases.add(disease);
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return diseases;
	}


	/**
	 * Stores the specified {@link Disease}. 
	 * @param disease the disease to store.
	 * @return <code>true</code> if the disease has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the disease.
	 */
	public boolean newDisease(Disease disease) throws OHException{

		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try{

			List<Object> parameters = new ArrayList<Object>(5);
			parameters.add(disease.getCode());
			parameters.add(disease.getType().getCode());
			parameters.add(disease.getDescription());
			parameters.add(disease.getOpdInclude());
			parameters.add(disease.getIpdInInclude());
			parameters.add(disease.getIpdOutInclude());

			String query = "insert into DISEASE (DIS_ID_A, DIS_DCL_ID_A, DIS_DESC, DIS_OPD_INCLUDE, DIS_IPD_IN_INCLUDE, DIS_IPD_OUT_INCLUDE) values (?, ?, ?, ?, ?, ?)";
			result = dbQuery.setDataWithParams(query, parameters, true);
			//retrieve the primary key
			parameters.clear();
			parameters.add(disease.getType().getCode());
			parameters.add(disease.getDescription());
			query = "select DIS_ID_A from DISEASE where DIS_DCL_ID_A = ? and DIS_DESC = ?";
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			if (resultSet.first()) disease.setCode(resultSet.getString(1));

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Updates the specified {@link Disease}.
	 * @param disease the {@link Disease} to update.
	 * @return <code>true</code> if the disease has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateDisease(Disease disease) throws OHException {

		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;

		try{
			List<Object> parameters = new ArrayList<Object>(5);
			parameters.add(disease.getType().getCode());
			parameters.add(disease.getDescription());
			parameters.add(disease.getOpdInclude());
			parameters.add(disease.getIpdInInclude());
			parameters.add(disease.getIpdOutInclude());
			parameters.add(disease.getCode());

			String query = "UPDATE DISEASE SET DIS_DCL_ID_A = ?, DIS_DESC = ?, DIS_OPD_INCLUDE = ?, DIS_IPD_IN_INCLUDE = ?, DIS_IPD_OUT_INCLUDE = ?," +
					" DIS_LOCK = DIS_LOCK + 1 where DIS_ID_A = ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
			if (result) disease.setLock(disease.getLock()+1);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Checks if the specified {@link Disease} has been modified.
	 * @param disease the disease to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the check.
	 */
	public boolean hasDiseaseModified(Disease disease) throws OHException
	{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(disease.getCode());
			String query = "select DIS_LOCK from DISEASE where DIS_ID_A = ?";
			ResultSet resultSet = dbQuery.getDataWithParams(query, parameters, true);
			if (resultSet.first()) {
				result = resultSet.getInt("DIS_LOCK")!=disease.getLock();
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Mark as deleted the specified {@link Disease}.
	 * @param disease the disease to make delete.
	 * @return <code>true</code> if the disease has been marked, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the delete operation.
	 */
	public boolean deleteDisease(Disease disease) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(disease.getCode());
			String query = "UPDATE DISEASE SET DIS_OPD_INCLUDE = 0, DIS_IPD_IN_INCLUDE = 0, DIS_IPD_OUT_INCLUDE = 0 WHERE DIS_ID_A = ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Check if the specified code is used by other {@link Disease}s.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "SELECT DIS_ID_A FROM DISEASE where DIS_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			present = set.first();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}

	/**
	 * Checks if the specified description is used by a disease with the specified type code.
	 * @param description the description to check.
	 * @param typeCode the disease type code.
	 * @return <code>true</code> if is used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isDescriptionPresent(String description, String typeCode) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try{
			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(description);
			parameters.add(typeCode);
			String query = "SELECT DIS_ID_A FROM DISEASE where DIS_DESC = ? and DIS_DCL_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			present = set.first();
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return present;
	}
}
