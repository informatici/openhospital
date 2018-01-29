package org.isf.admission.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;


@Transactional
class AdmissionIoOperationRepositoryImpl implements AdmissionIoOperationRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")	
	@Override
	public List<Object[]> findAllBySearch(String searchTerms) {
		return this.entityManager.
				createNativeQuery(_getAdmissionsBySearch(searchTerms)).
					getResultList();
	}	

	
	private String _getAdmissionsBySearch(
			String searchTerms) 
	{
		String[] terms = _calculateAdmittedPatientsTerms(searchTerms);
		String query = _calculateAdmittedPatientsQuery(terms);
		
		
		return query;
	}
	
    private String[] _calculateAdmittedPatientsTerms(
			String searchTerms) 
	{
    	String[] terms = null;
    	
    	
    	if (searchTerms != null && !searchTerms.isEmpty()) 
		{
			searchTerms = searchTerms.trim().toLowerCase();
			terms = searchTerms.split(" ");
		}
    	
    	return terms;
	}
    
    private String _calculateAdmittedPatientsQuery(
    		String[] terms)
	{
    	String query = null;	

    	
    	query = "SELECT PAT.*, ADM.* " +
    			"FROM PATIENT PAT LEFT JOIN " +
    			"(SELECT * FROM ADMISSION WHERE (ADM_DELETED='N' or ADM_DELETED is null) AND ADM_IN = 1) ADM " +
    			"ON ADM.ADM_PAT_ID = PAT.PAT_ID " +
    			"WHERE (PAT.PAT_DELETED='N' or PAT.PAT_DELETED is null) ";
    	if (terms != null) 
		{
			for (String term:terms) 
			{
				query += " AND CONCAT(PAT_ID, LOWER(PAT_SNAME), LOWER(PAT_FNAME), LOWER(PAT_NOTE), LOWER(PAT_TAXCODE)) LIKE \"%" + term + "%\"";
			}
		}
		query += " ORDER BY PAT_ID DESC";
    	
    	return query;
	}
}