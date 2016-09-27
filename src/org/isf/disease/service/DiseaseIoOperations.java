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
import java.util.List;

import org.isf.disease.model.Disease;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;

/**
 * This class offers the io operations for recovering and managing
 * diseases records from the database
 * 
 * @author Rick, Vero
 */
public class DiseaseIoOperations {

	/**
	 * Gets a {@link Disease} with the specified code.
	 * @param code the disease code.
	 * @return the found disease, <code>null</code> if no disease has found.
	 * @throws OHException if an error occurred getting the disease.
	 */
	public Disease getDiseaseByCode(
			int code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Disease disease = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM DISEASE JOIN DISEASETYPE ON DIS_DCL_ID_A = DCL_ID_A WHERE DIS_ID_A = ?";
		jpa.createQuery(query, Disease.class, false);
		params.add(code);
		jpa.setParameters(params, false);
		disease = (Disease)jpa.getResult();		
		
		jpa.commitTransaction();

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
    @SuppressWarnings("unchecked")
	public ArrayList<Disease> getDiseases(
			String disTypeCode, 
			boolean opd, 
			boolean ipdIn, 
			boolean ipdOut) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Disease> diseases = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = _calculateGetDiseasesQuery(disTypeCode, opd, ipdIn, ipdOut); 
		jpa.createQuery(query, Disease.class, false);		
		if (disTypeCode != null) 
		{
			params.add(disTypeCode);		
			jpa.setParameters(params, false);
		}
		List<Disease> diseaseList = (List<Disease>)jpa.getList();
		diseases = new ArrayList<Disease>(diseaseList);			
		
		jpa.commitTransaction();

		return diseases;
	}
	
	private String _calculateGetDiseasesQuery(
			String disTypeCode, 
			boolean opd, 
			boolean ipdIn, 
			boolean ipdOut) 
	{		
		String selectClause = "SELECT * FROM DISEASE JOIN DISEASETYPE ON DIS_DCL_ID_A = DCL_ID_A";			
		String whereClause = "";
		if (disTypeCode != null) 
		{
			whereClause= " where DCL_ID_A like ?";
		}		
		if (opd) 
		{
			whereClause = _initWherClause(whereClause);
			whereClause +=" (DIS_OPD_INCLUDE=1 or DIS_OPD_INCLUDE is null) ";
		}		
		if (ipdIn) 
		{
			whereClause = _initWherClause(whereClause);
			whereClause +=" (DIS_IPD_IN_INCLUDE=1 or DIS_IPD_IN_INCLUDE is null) ";
		}		
		if (ipdOut) 
		{
			whereClause = _initWherClause(whereClause);
			whereClause +=" (DIS_IPD_OUT_INCLUDE=1 or DIS_IPD_OUT_INCLUDE is null) ";
		}		
		String orderClause = " order BY DIS_DESC";
		
		String query = selectClause + whereClause + orderClause;

		return query;
	}
	
	private String _initWherClause(
			String whereClause)
	{		
		if (whereClause.equals("")) 
		{
			whereClause =" where "; 
		}
		else
		{
			whereClause +=" and  "; 
		}
		
		return whereClause;
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(disease);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		disease.setLock(disease.getLock() + 1);
		jpa.merge(disease);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		Disease foundDisease = (Disease)jpa.find(Disease.class, disease.getCode());
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		disease.setOpdInclude(false);
		disease.setIpdInInclude(false);
		disease.setIpdOutInclude(false);
		jpa.merge(disease);
    	jpa.commitTransaction();
    	
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
		DbJpaUtil jpa = new DbJpaUtil(); 
		Disease foundDisease = (Disease)jpa.find(Disease.class, code);
		boolean present = false;

		
		if (foundDisease != null)
		{
			present = true;
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
	public boolean isDescriptionPresent(
			String description, 
			String typeCode) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Disease foundDisease = (Disease)jpa.find(Disease.class, typeCode);
		boolean present = false;

		
		if (foundDisease.getDescription().compareTo(description) == 0)
		{
			present = true;
		}
		
		return present;
	}
}
