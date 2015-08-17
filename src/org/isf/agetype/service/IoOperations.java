package org.isf.agetype.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.agetype.model.AgeType;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

/**
 * Persistence class for agetype module.
 *
 */
public class IoOperations {

	/**
	 * Returns all available age types.
	 * @return a list of {@link AgeType}.
	 * @throws OHException if an error occurs retrieving the age types.
	 */
	public ArrayList<AgeType> getAgeType() throws OHException {
		ArrayList<AgeType> ageTypes = null;
		String query = "select * from AGETYPE order by AT_CODE";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(query,true);
			ageTypes = new ArrayList<AgeType>(resultSet.getFetchSize());
			while (resultSet.next()) {
				ageTypes.add(new AgeType(resultSet.getString("AT_CODE"),
						resultSet.getInt("AT_FROM"),
						resultSet.getInt("AT_TO"),
						resultSet.getString("AT_DESC")));
			}

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return ageTypes;
	}

	/**
	 * Updates the list of {@link AgeType}s.
	 * @param ageType the {@link AgeType} to update.
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateAgeType(ArrayList<AgeType> ageTypes) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean result = true;
		List<Object> parameters = new ArrayList<Object>(3);
		String query = "UPDATE AGETYPE SET AT_FROM = ?, AT_TO = ? WHERE AT_CODE = ?";
		for (AgeType ageType : ageTypes) {
			parameters.clear();
			parameters.add(ageType.getFrom());
			parameters.add(ageType.getTo());
			parameters.add(ageType.getCode());
			result = result && dbQuery.setDataWithParams(query, parameters, false);
		}
		if (result) dbQuery.commit();
		else dbQuery.rollback();
		dbQuery.releaseConnection();
		return result;
	}

	/**
	 * Gets the {@link AgeType} from the code index.
	 * @param index the code index.
	 * @return the retrieved element, <code>null</code> otherwise.
	 * @throws OHException if an error occurs retrieving the item.
	 */
	public AgeType getAgeTypeByCode(int index) throws OHException {
		AgeType ageType = null;
		DbQueryLogger dbQuery = new DbQueryLogger();
		try{
			String code = "d"+String.valueOf(index-1);
			List<Object> parameters = Collections.<Object>singletonList(code);
			String sqlstring = "SELECT * FROM AGETYPE where AT_CODE = ?";
			ResultSet set = dbQuery.getDataWithParams(sqlstring, parameters, true);
			set.next();
			ageType = new AgeType(set.getString("AT_CODE"),
					set.getInt("AT_FROM"),
					set.getInt("AT_TO"),
					set.getString("AT_DESC"));	

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}
		return ageType;
	}

}
