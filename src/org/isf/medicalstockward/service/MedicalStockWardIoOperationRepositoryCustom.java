package org.isf.medicalstockward.service;


import org.springframework.stereotype.Repository;

import java.util.GregorianCalendar;
import java.util.List;

@Repository
public interface MedicalStockWardIoOperationRepositoryCustom {

	List<Integer> findAllWardMovement(String wardId, GregorianCalendar dateFrom, GregorianCalendar dateTo);
	
}
