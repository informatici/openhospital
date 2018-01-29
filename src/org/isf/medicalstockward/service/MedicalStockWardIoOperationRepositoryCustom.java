package org.isf.medicalstockward.service;


import java.util.GregorianCalendar;
import java.util.List;


public interface MedicalStockWardIoOperationRepositoryCustom {

	List<Integer> findAllWardMovement(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo);
	
}
