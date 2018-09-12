package org.isf.opd.service;


import java.util.GregorianCalendar;
import java.util.List;


public interface OpdIoOperationRepositoryCustom {

	List<Integer> findAllOpdWhereParams(String diseaseTypeCode, String diseaseCode, GregorianCalendar dateFrom,
			GregorianCalendar dateTo, int ageFrom, int ageTo, char sex, char newPatient);
	
}
