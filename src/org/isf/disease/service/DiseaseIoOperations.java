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

import java.util.ArrayList;

import org.isf.disease.model.Disease;
import org.isf.disease.repository.DiseaseIoOperationRepository;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class offers the io operations for recovering and managing
 * diseases records from the database
 * 
 * @author Rick, Vero
 */
@Component
public class DiseaseIoOperations {

	@Autowired
	private DiseaseIoOperationRepository repository;
	
	/**
	 * Gets a {@link Disease} with the specified code.
	 * @param code the disease code.
	 * @return the found disease, <code>null</code> if no disease has found.
	 * @throws OHException if an error occurred getting the disease.
	 */
	public Disease getDiseaseByCode(
			int code) throws OHException 
	{
		return repository.findOneByCode(code);
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
	public ArrayList<Disease> getDiseases(
			String disTypeCode, 
			boolean opd, 
			boolean ipdIn, 
			boolean ipdOut) throws OHException 
	{
		ArrayList<Disease> diseases = null;
    	
		
		if (disTypeCode != null) 
		{
			if (opd) 
			{
				if (ipdIn) 
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCodeAndOpdAndIpdInAndIpdOut(disTypeCode));
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCodeAndOpdAndIpdIn(disTypeCode));						
					}
				}
				else
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCodeAndOpdAndIpdOut(disTypeCode));
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCodeAndOpd(disTypeCode));						
					}					
				}
			}		
			else
			{

				if (ipdIn) 
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCodeAndIpdInAndIpdOut(disTypeCode));
						
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCodeAndIpdIn(disTypeCode));						
					}
				}
				else
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCodeAndIpdOut(disTypeCode));
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAllByDiseaseTypeCode(disTypeCode));						
					}					
				}
			}
		}
		else
		{
			if (opd) 
			{
				if (ipdIn) 
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByOpdAndIpdInAndIpdOut());
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAllByOpdAndIpdIn());						
					}
				}
				else
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByOpdAndIpdOut());
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAllByOpd());						
					}					
				}
			}		
			else
			{

				if (ipdIn) 
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByIpdInAndIpdOut());
						
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAllByIpdIn());						
					}
				}
				else
				{
					if (ipdOut)
					{
						diseases = (ArrayList<Disease>)(repository.findAllByIpdOut());
					}
					else
					{
						diseases = (ArrayList<Disease>)(repository.findAll());						
					}					
				}
			}
		}

		return diseases;
	}
	
	/**
	 * Stores the specified {@link Disease}. 
	 * @param disease the disease to store.
	 * @return <code>true</code> if the disease has been stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs storing the disease.
	 */
	public boolean newDisease(
			Disease disease) throws OHException
	{
		boolean result = true;
	
		
		repository.save(disease);
		
		return result;
	}

	/**
	 * Updates the specified {@link Disease}.
	 * @param disease the {@link Disease} to update.
	 * @return <code>true</code> if the disease has been updated, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateDisease(
			Disease disease) throws OHException 
	{
		boolean result = true;
	
		
		disease.setLock(disease.getLock() + 1);
		repository.save(disease);
		
		return result;
	}

	/**
	 * Checks if the specified {@link Disease} has been modified.
	 * @param disease the disease to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the check.
	 */
	public boolean hasDiseaseModified(
			Disease disease) throws OHException
	{ 
		Disease foundDisease = repository.findOne(disease.getCode()); 
		boolean result = false;
		
		
		if (foundDisease.getLock() != disease.getLock())
		{
			result = true;
		}
		
		return result;
	}

	/**
	 * Mark as deleted the specified {@link Disease}.
	 * @param disease the disease to make delete.
	 * @return <code>true</code> if the disease has been marked, <code>false</code> otherwise.
	 * @throws OHException if an error occurred during the delete operation.
	 */
	public boolean deleteDisease(
			Disease disease) throws OHException
	{
		boolean result = true;
	
		
		disease.setOpdInclude(false);
		disease.setIpdInInclude(false);
		disease.setIpdOutInclude(false);
		repository.save(disease);
		
		return result;
	}

	/**
	 * Check if the specified code is used by other {@link Disease}s.
	 * @param code the code to check.
	 * @return <code>true</code> if it is already used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		boolean result = true;
	
		
		result = repository.exists(code);
		
		return result;
	}

	/**
	 * Checks if the specified description is used by a disease with the specified type code.
	 * @param description the description to check.
	 * @param typeCode the disease type code.
	 * @return <code>true</code> if is used, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isDescriptionPresent(
			String description, 
			String typeCode) throws OHException
	{
		boolean present = false;
		
		
		Disease foundDisease = repository.findOneByDescriptionAndTypeCode(description, typeCode);			
		if (foundDisease != null && foundDisease.getDescription().compareTo(description) == 0)
		{
			present = true;
		}
		
		return present;
	}
}
