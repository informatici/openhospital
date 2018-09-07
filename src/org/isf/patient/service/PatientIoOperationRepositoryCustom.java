package org.isf.patient.service;


import java.util.List;


public interface PatientIoOperationRepositoryCustom {

	List<Integer> findAllByHeightAndWeight(String regex);
	
}
