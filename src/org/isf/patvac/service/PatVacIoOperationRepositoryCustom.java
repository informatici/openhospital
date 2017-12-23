package org.isf.patvac.service;


import java.util.GregorianCalendar;
import java.util.List;


public interface PatVacIoOperationRepositoryCustom {
	
	List<Integer> findAllByCodesAndDatesAndSexAndAges(String vaccineTypeCode, String vaccineCode,
			GregorianCalendar dateFrom, GregorianCalendar dateTo, char sex, int ageFrom, int ageTo);	
}
