package org.isf.ward.test;


import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;

import static org.junit.Assert.assertEquals;

public class TestWard 
{	
    private String code = "Z";
    private String description = "TestDescription";
    private String telephone = "TestTelephone";
    private String fax = "TestFac";
    private String email = "TestEmail";
    private Integer beds = 100;
    private Integer nurs = 101;
    private Integer docs = 102;   
    private boolean isPharmacy = true;    
    private boolean isFemale = false;  
    private boolean isMale = true;
    private Integer lock = 0;
    
			
	public Ward setup(
			boolean usingSet) throws OHException 
	{
		Ward ward;
	
				
		if (usingSet == true)
		{
			ward = new Ward();
			_setParameters(ward);
		}
		else
		{
			// Create Ward with all parameters 
			ward = new Ward(code, description, telephone, fax, email, beds, nurs, docs,
					isPharmacy, isMale, isFemale, lock);
		}
				    	
		return ward;
	}
	
	public void _setParameters(
			Ward ward) 
	{	
		ward.setCode(code);
		ward.setBeds(beds);
		ward.setDescription(description);
		ward.setDocs(docs);
		ward.setEmail(email);
		ward.setFax(fax);
		ward.setFemale(isFemale);
		ward.setLock(lock);
		ward.setMale(isMale);
		ward.setNurs(nurs);
		ward.setPharmacy(isPharmacy);
		ward.setTelephone(telephone);
		
		return;
	}
	
	public void check(
			Ward ward) 
	{		
    	System.out.println("Check Ward: " + ward.getCode());
    	assertEquals(code, ward.getCode());
    	assertEquals(beds, ward.getBeds());
    	assertEquals(description, ward.getDescription());
    	assertEquals(docs, ward.getDocs());
    	assertEquals(email, ward.getEmail());
    	assertEquals(fax, ward.getFax());
    	assertEquals(isFemale, ward.isFemale());
    	assertEquals(lock, ward.getLock());
    	assertEquals(isMale, ward.isMale());
    	assertEquals(nurs, ward.getNurs());
    	assertEquals(isPharmacy, ward.isPharmacy());
    	assertEquals(telephone, ward.getTelephone());
		
		return;
	}
}
