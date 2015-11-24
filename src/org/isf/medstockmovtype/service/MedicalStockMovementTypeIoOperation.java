package org.isf.medstockmovtype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.medstockmovtype.model.MovementType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;
import org.isf.generaldata.MessageBundle;

/**
 * Persistence class for the medstockmovtype module.
 *
 */
@Component
public class MedicalStockMovementTypeIoOperation {

	/**
	 * Retrieves all the stored {@link MovementType}.
	 * @return all the stored {@link MovementType}s.
	 * @throws OHException if an error occurs retrieving the medical stock movement types.
	 */
	public ArrayList<MovementType> getMedicaldsrstockmovType() throws OHException {
		ArrayList<MovementType> medicaldsrstockmovtypes = null;
		String query = "select * from MEDICALDSRSTOCKMOVTYPE order by MMVT_DESC";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{

			ResultSet resultSet = dbQuery.getData(query, true);
			medicaldsrstockmovtypes = new ArrayList<MovementType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				MovementType movementType = new MovementType();
				movementType.setCode(resultSet.getString("MMVT_ID_A"));
				movementType.setDescription(resultSet.getString("MMVT_DESC"));
				movementType.setType(resultSet.getString("MMVT_TYPE"));
				medicaldsrstockmovtypes.add(movementType);
			}

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return medicaldsrstockmovtypes;
	}

	/**
	 * Updates the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to update.
	 * @return <code>true</code> if the specified stock movement type has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(4);
			parameters.add(medicaldsrstockmovType.getCode());
			parameters.add(medicaldsrstockmovType.getDescription());
			parameters.add(medicaldsrstockmovType.getType());
			parameters.add(medicaldsrstockmovType.getCode());

			String query = "update MEDICALDSRSTOCKMOVTYPE set " +
					"MMVT_ID_A= ?, "+
					"MMVT_DESC= ?, "+
					"MMVT_TYPE= ? "+
					"where MMVT_ID_A= ?";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}	
		return result;
	}

	/**
	 * Stores the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to store.
	 * @return <code>true</code> if the medical movement type has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the store operation.
	 */
	public boolean newMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			List<Object> parameters = new ArrayList<Object>(3);
			parameters.add(medicaldsrstockmovType.getCode());
			parameters.add(medicaldsrstockmovType.getDescription());
			parameters.add(medicaldsrstockmovType.getType());
			String query = "insert into MEDICALDSRSTOCKMOVTYPE (MMVT_ID_A,MMVT_DESC,MMVT_TYPE) " +
					"values (?, ?, ?)";
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}	
		return result;
	}

	/**
	 * Deletes the specified {@link MovementType}.
	 * @param medicaldsrstockmovType the medical stock movement type to delete.
	 * @return <code>true</code> if the medical stock movement type has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the delete operation.
	 */
	public boolean deleteMedicaldsrstockmovType(MovementType medicaldsrstockmovType) throws OHException {

		List<Object> parameters = Collections.<Object>singletonList(medicaldsrstockmovType.getCode());
		String query = "delete from MEDICALDSRSTOCKMOVTYPE where MMVT_ID_A = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = false;
		try {
			result = dbQuery.setDataWithParams(query, parameters, true);
		} finally{
			dbQuery.releaseConnection();
		}
		return result;
	}

	/**
	 * Checks if the specified medical stock movement type is already used.
	 * @param code the code to check.
	 * @return <code>true</code> if the code is used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(String code) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present=false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(code);
			String query = "SELECT MMVT_ID_A FROM MEDICALDSRSTOCKMOVTYPE where MMVT_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			if(set.first()) present = true;
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return present;
	}
}
