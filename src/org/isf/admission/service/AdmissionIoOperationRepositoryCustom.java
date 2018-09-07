package org.isf.admission.service;


import java.util.List;


public interface AdmissionIoOperationRepositoryCustom {

	List<Object[]> findAllBySearch(String searchTerms);
	
}
