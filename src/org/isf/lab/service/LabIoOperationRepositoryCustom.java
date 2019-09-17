package org.isf.lab.service;


import java.util.List;


public interface LabIoOperationRepositoryCustom {

	List<Object[]> findAllBySearch(String searchTerms);
	
}
