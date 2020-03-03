package org.isf.agetype.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.agetype.model.AgeType;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Persistence class for agetype module.
 *
 */
@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class AgeTypeIoOperations 
{
	@Autowired
	private AgeTypeIoOperationRepository repository;
	
	/**
	 * Returns all available age types.
	 * @return a list of {@link AgeType}.
	 * @throws OHServiceException if an error occurs retrieving the age types.
	 */
	public ArrayList<AgeType> getAgeType() throws OHServiceException 
	{
		return new ArrayList<AgeType>(repository.findAllByOrderByCodeAsc());
	}

	/**
	 * Updates the list of {@link AgeType}s.
	 * @param ageType the {@link AgeType} to update.
	 * @return <code>true</code> if the list has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the update.
	 */
	public boolean updateAgeType(
			ArrayList<AgeType> ageType) throws OHServiceException 
	{
		boolean result = true;
	
		
		List<AgeType> savedAgeType = repository.save(ageType);
		result = (savedAgeType != null);
		
		return result;
	}

	/**
	 * Gets the {@link AgeType} from the code index.
	 * @param index the code index.
	 * @return the retrieved element, <code>null</code> otherwise.
	 * @throws OHServiceException if an error occurs retrieving the item.
	 */
	public AgeType getAgeTypeByCode(
			int index) throws OHServiceException 
	{	
		String code = "";
		AgeType ageType = null;
				
		
		code = "d" + (index - 1);
		ageType = repository.findOneByCode(code); 

		return ageType;
	}
}
