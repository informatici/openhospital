package org.isf.agetype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.agetype.model.AgeType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * Persistence class for agetype module.
 *
 */
@Component
public class AgeTypeIoOperations 
{
	/**
	 * Returns all available age types.
	 * @return a list of {@link AgeType}.
	 * @throws OHException if an error occurs retrieving the age types.
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<AgeType> getAgeType() throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<AgeType> padmissiontype = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM AGETYPE ORDER BY AT_CODE";
		jpa.createQuery(query, AgeType.class, false);
		List<AgeType> ageTypeList = (List<AgeType>)jpa.getList();
		padmissiontype = new ArrayList<AgeType>(ageTypeList);			
		
		jpa.commitTransaction();

		return padmissiontype;
	}

	/**
	 * Updates the list of {@link AgeType}s.
	 * @param ageType the {@link AgeType} to update.
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateAgeType(
			ArrayList<AgeType> ageTypes) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		for (AgeType ageType : ageTypes) 
		{
			jpa.merge(ageType);
		}
    	jpa.commitTransaction();
    	
		return result;	
	}

	/**
	 * Gets the {@link AgeType} from the code index.
	 * @param index the code index.
	 * @return the retrieved element, <code>null</code> otherwise.
	 * @throws OHException if an error occurs retrieving the item.
	 */
	public AgeType getAgeTypeByCode(
			int index) throws OHException 
	{	
		DbJpaUtil jpa = new DbJpaUtil(); 
		String code = "";
		AgeType ageType = null;
				
		
		jpa.beginTransaction();
		
		code = "d" + String.valueOf(index-1);
		ageType = (AgeType)jpa.find(AgeType.class, code); 
		
		jpa.commitTransaction();

		return ageType;
	}
}
