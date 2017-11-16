package org.isf.patient.service;


import java.util.List;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;


public interface PatientIoOperationRepositoryCustom {

	public List<Patient> findAllPatientsWithHeightAndWeight(String regex) throws OHException;

	public void updatePatientLock(Patient patient, int lock) throws OHException;

}

