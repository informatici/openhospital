package org.isf.admission.service;


import java.util.GregorianCalendar;
import java.util.List;


public interface AdmissionIoOperationRepositoryCustom {

	List<Object[]> findAllBySearch(String searchTerms);
	
	List<Object[]> findAllBySearchAndDateRanges(String searchTerms, GregorianCalendar[] admissionRange,
			GregorianCalendar[] dischargeRange);
	
}
