package org.isf.vaccine.service;

import java.util.ArrayList;
import java.util.List;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.vaccine.model.Vaccine;
import org.springframework.stereotype.Component;

/**
 * This class offers the io operations for recovering and managing
 * vaccine records from the database
 *
 * @author Eva
 * 
 * modification history
 * 20/10/2011 - Cla - insert vaccinetype managment
 *
 */

@Component
public class VaccineIoOperations {

	/**
	 * returns the list of {@link Vaccine}s based on vaccine type code
	 *
	 * @param vaccineTypeCode - the type code. If <code>null</code> returns all {@link Vaccine}s in the DB
	 * @return the list of {@link Vaccine}s
	 * @throws OHException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Vaccine> getVaccine(
			String vaccineTypeCode) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Vaccine> pvaccine = null;
		ArrayList<Object> params = new ArrayList<Object>();

        try{
            jpa.beginTransaction();

            String query = "SELECT * FROM VACCINE JOIN VACCINETYPE ON VAC_VACT_ID_A = VACT_ID_A";
            if (vaccineTypeCode != null) {
                query += " WHERE VAC_VACT_ID_A = ?";
                params.add(vaccineTypeCode);
            }
            query += " ORDER BY VAC_DESC";
            jpa.createQuery(query, Vaccine.class, false);
            jpa.setParameters(params, false);
            List<Vaccine> vaccineList = (List<Vaccine>)jpa.getList();
            pvaccine = new ArrayList<Vaccine>(vaccineList);

            jpa.commitTransaction();
        }catch (OHException e) {
            //DbJpaUtil managed exception
            jpa.rollbackTransaction();
            throw e;
        }
		return pvaccine;
	}

	/**
	 * inserts a new {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to insert
	 * @return <code>true</code> if the item has been inserted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean newVaccine(
			Vaccine vaccine) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
        try{
            jpa.beginTransaction();
            jpa.persist(vaccine);
            jpa.commitTransaction();
        }catch (OHException e) {
            //DbJpaUtil managed exception
            jpa.rollbackTransaction();
            throw e;
        }
		return result;	
	}
	
	/**
	 * checks if the specified {@link Vaccine} has been modified.
	 * 
	 * @param vaccine - the {@link Vaccine} to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean hasVaccineModified(
			Vaccine vaccine) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil();
        boolean result = false;
        try{
            jpa.beginTransaction();
            Vaccine foundVaccine = (Vaccine)jpa.find(Vaccine.class, vaccine.getCode());
            if (foundVaccine.getLock() != vaccine.getLock())
            {
                result = true;
            }
            jpa.commitTransaction();
        }catch (OHException e) {
            //DbJpaUtil managed exception
            jpa.rollbackTransaction();
            throw e;
        }



		return result;
	}

	/**
	 * updates a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean updateVaccine(
			Vaccine vaccine) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
        try{
            vaccine.setLock(vaccine.getLock()+1);

            jpa.beginTransaction();
            jpa.merge(vaccine);
            jpa.commitTransaction();
        }catch (OHException e) {
            //DbJpaUtil managed exception
            jpa.rollbackTransaction();
            throw e;
        }
		return result;	
	}

	/**
	 * deletes a {@link Vaccine} in the DB
	 *
	 * @param vaccine - the item to delete
	 * @return <code>true</code> if the item has been deleted, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean deleteVaccine(
			Vaccine vaccine) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;

        try{
            jpa.beginTransaction();
            Vaccine vaccineToRemove = (Vaccine) jpa.find(Vaccine.class, vaccine.getCode());
            jpa.remove(vaccineToRemove);
            jpa.commitTransaction();
        }catch (OHException e) {
            //DbJpaUtil managed exception
            jpa.rollbackTransaction();
            throw e;
        }
		return result;	
	}

	/**
	 * checks if the code is already in use
	 *
	 * @param code - the vaccine code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Vaccine vaccine;
		boolean result = false;

        try{
            jpa.beginTransaction();
            vaccine = (Vaccine)jpa.find(Vaccine.class, code);
            if (vaccine != null)
            {
                result = true;
            }
            jpa.commitTransaction();
        }catch (OHException e) {
            //DbJpaUtil managed exception
            jpa.rollbackTransaction();
            throw e;
        }
		return result;	
	}
}


