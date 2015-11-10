package org.isf.distype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.distype.model.DiseaseType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;
import org.isf.generaldata.MessageBundle;

/**
 * Persistence class for the DisType module.
 */
@Component
public class IoOperation {

	/**
	 * Returns all the stored {@link DiseaseType}s.
	 * @return a list of disease type.
	 * @throws OHException if an error occurs retrieving the diseases list.
	 */
	public ArrayList<DiseaseType> getDiseaseTypes() throws OHException {
		ArrayList<DiseaseType> diseaseTypes = null;
		String query = "select * from DISEASETYPE order by DCL_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			ResultSet resultSet = dbQuery.getData(query,true);
			diseaseTypes = new ArrayList<DiseaseType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				diseaseTypes.add(new DiseaseType(resultSet.getString("DCL_ID_A"), resultSet.getString("DCL_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return diseaseTypes;
	}

	/**
	 * Updates the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to update.
	 * @return <code>true</code> if the disease type has been updated, false otherwise.
	 * @throws OHException if an error occurs during the update operation.
	 */
	public boolean updateDiseaseType(DiseaseType diseaseType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(3);
			parameters.add(diseaseType.getCode());
			parameters.add(diseaseType.getDescription());
			parameters.add(diseaseType.getCode());
			String query = "update DISEASETYPE set DCL_ID_A=?, DCL_DESC=? where DCL_ID_A=?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Store the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to store.
	 * @return <code>true</code> if the {@link DiseaseType} has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newDiseaseType(DiseaseType diseaseType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(2);
			parameters.add(diseaseType.getCode());
			parameters.add(diseaseType.getDescription());
			String query = "insert into DISEASETYPE (DCL_ID_A,DCL_DESC) values (?,?)";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Deletes the specified {@link DiseaseType}.
	 * @param diseaseType the disease type to remove.
	 * @return <code>true</code> if the disease has been removed, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete procedure.
	 */
	public boolean deleteDiseaseType(DiseaseType diseaseType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = Collections.<Object>singletonList(diseaseType.getCode());
			String query = "delete from DISEASETYPE where DCL_ID_A = ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Checks if the specified code is already used by any {@link DiseaseType}.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, false otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException{
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present=false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "SELECT DCL_ID_A FROM DISEASETYPE where DCL_ID_A = ?";
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
