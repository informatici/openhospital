/**
 * 11-dec-2005
 * 14-jan-2006
 * author bob
 */
package org.isf.medicals.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.isf.medicals.model.Medical;
import org.isf.medicalstock.model.Movement;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;


/**
 * This class offers the io operations for recovering and managing
 * medical records from the database
 * 
 * @author bob 
 * 		   modified by alex:
 * 			- column product code
 * 			- column pieces per packet
 */
@Component
public class MedicalsIoOperations {

	/**
	 * Retrieves the specified {@link Medical}.
	 * @param code the medical code
	 * @return the stored medical.
	 * @throws OHException if an error occurs retrieving the stored medical.
	 */
	public Medical getMedical(
			int code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Medical medical = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A WHERE MDSR_ID = ?";
			params.add(code);
			jpa.createQuery(query, Medical.class, false);
			jpa.setParameters(params, false);
			medical = (Medical)jpa.getResult();	
			
			jpa.commitTransaction();
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}

		return medical;
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
    @SuppressWarnings("unchecked")
	public ArrayList<Medical> getMedicals(
			String description) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Medical> medicals = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A ";
			if (description!=null) {
				query += "where MDSRT_DESC like ? ";
				params.add(description);
			}
			query += "order BY MDSR_DESC";
			jpa.createQuery(query, Medical.class, false);
			jpa.setParameters(params, false);
			List<Medical> medicalList = (List<Medical>)jpa.getList();
			medicals = new ArrayList<Medical>(medicalList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
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
    @SuppressWarnings("unchecked")
	public ArrayList<Medical> getMedicals(
			String description, 
			String type, 
			boolean expiring) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Medical> medicals = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		try {
			jpa.beginTransaction();
			
			String query = "SELECT * FROM MEDICALDSR JOIN MEDICALDSRTYPE ON MDSR_MDSRT_ID_A = MDSRT_ID_A ";
			if (description!=null) {
				query += "where ";
				query += "(MDSR_DESC like ? OR MDSR_CODE like ?) ";
				params.add("%"+description+"%");
				params.add("%"+description+"%");
			}
			if(type!=null){
				if (params.size() == 0) 
				{
					query += "where ";				
				}
				else 
				{
					query += "and ";
				}
				query += "(MDSRT_ID_A=?) ";
				params.add(type);
			}
			if(expiring){
				if (params.size() == 0) 
				{
					query += "where ";
				}
				else 
				{
					query += "and ";
				}
				query += "((MDSR_INI_STOCK_QTI+MDSR_IN_QTI-MDSR_OUT_QTI)<MDSR_MIN_STOCK_QTI) ";
			}
			query += "order BY MDSR_MDSRT_ID_A, MDSR_DESC";
			jpa.createQuery(query, Medical.class, false);
			jpa.setParameters(params, false);
			List<Medical> medicalList = (List<Medical>)jpa.getList();
			medicals = new ArrayList<Medical>(medicalList);			
			
			jpa.commitTransaction();
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return medicals;
	}

	/**
	 * Checks if the specified {@link Medical} exists or not.
	 * @param medical the medical to check.
	 * @return <code>true</code> if exists <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean medicalExists(Medical medical) throws OHException {
		
		DbJpaUtil jpa = new DbJpaUtil();
		ArrayList<Object> params = new ArrayList<Object>();
		
		boolean result;
		
		try {
			jpa.beginTransaction();

			String query = "SELECT * FROM MEDICALDSR WHERE MDSR_MDSRT_ID_A = ? AND MDSR_DESC = ?";
			params.add(medical.getType().getCode());
			params.add(medical.getDescription());
			jpa.createQuery(query, Medical.class, false);
			jpa.setParameters(params, false);
			Medical foundMedical = (Medical) jpa.getResult();
		
			if (foundMedical != null) {
				result = true;
			} else {
				result = false;
			}
			
			jpa.commitTransaction();
		} catch (OHException e) {
			if (e.getCause().getClass().equals(NoResultException.class)) {
				result = false;
				jpa.commitTransaction();
			} else {
				//DbJpaUtil managed exception
				jpa.rollbackTransaction();
				throw e;
			}
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
		DbJpaUtil jpa = new DbJpaUtil();
		boolean result = true;

		try {
			jpa.beginTransaction();
			jpa.persist(medical);
			jpa.commitTransaction();
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		
		return result;
	}

	/**
	 * Returns the stored medical lock value.
	 * @param code the medical code.
	 * @return the stored lock value.
	 * @throws OHException if an error occurs retrieving the lock value.
	 */
	public int getMedicalLock(
			int code) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Medical medical = null;
		int lock = -1;
				
		try {
			jpa.beginTransaction();
			
			medical = (Medical)jpa.find(Medical.class, code); 
			if (medical != null)
			{
				lock = medical.getLock();
			}
			
			jpa.commitTransaction();
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return lock;
	}

	/**
	 * Updates the specified {@link Medical}.
	 * @param medical the medical to update.
	 * @return <code>true</code> if the medical has been updated <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the update.
	 */
	public boolean updateMedical(Medical medical) throws OHException {
		DbJpaUtil jpa = new DbJpaUtil();
		boolean result = true;
		
		medical.setLock(new Integer(medical.getLock().intValue() + 1));

		try {
			jpa.beginTransaction();
			jpa.merge(medical);
			jpa.commitTransaction();	
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}

		return result;
	}

	/**
	 * Checks if the specified {@link Medical} is referenced in stock movement.
	 * @param code the medical code.
	 * @return <code>true</code> if the medical is referenced, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isMedicalReferencedInStockMovement(
			int code) throws OHException 
 {
		DbJpaUtil jpa = new DbJpaUtil();
		boolean result;
		ArrayList<Object> params = new ArrayList<Object>();

		try {
			jpa.beginTransaction();
	
			String query = "select * from MEDICALDSRSTOCKMOV where MMV_MDSR_ID = ?";
			params.add(code);
			jpa.createQuery(query, Movement.class, false);
			jpa.setParameters(params, false);
			Movement foundMovement = (Movement) jpa.getResult();
			if (foundMovement.getMedical().getCode() == code) {
				result = true;
			} else {
				result = false;
			}
			jpa.commitTransaction();
		} catch (OHException e) {
			if (e.getCause().getClass().equals(NoResultException.class)) {
				result = false;
				jpa.commitTransaction();
			} else {
				//DbJpaUtil managed exception
				jpa.rollbackTransaction();
				throw e;
			}
		}

		return result;
	}

	/**
	 * Deletes the specified {@link Medical}.
	 * @param medical the medical to delete.
	 * @return <code>true</code> if the medical has been deleted, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the medical deletion.
	 */
	public boolean deleteMedical(
			Medical medical) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		try {
			jpa.beginTransaction();	
			Medical objToRemove = (Medical) jpa.find(Medical.class, medical.getCode());
			jpa.remove(objToRemove);
	    	jpa.commitTransaction();
		} catch (OHException e) {
			//DbJpaUtil managed exception
			jpa.rollbackTransaction();
			throw e;
		}
		return result;
	}
}
