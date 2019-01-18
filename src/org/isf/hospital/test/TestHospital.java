package org.isf.hospital.test;

import org.isf.utils.exception.OHException;
import org.isf.hospital.model.Hospital;

import static org.junit.Assert.assertEquals;

public class TestHospital 
{	
    private String code = "ZZ";
    private String description = "TestDescription";    
    private String address = "TestAddress";
    private String city = "TestCity";
    private String telephone = "Testtelephone";
    private String fax = "TestFax";
    private String email = "TestEmail";
    private String currencyCod = "Cod";
	private Integer lock = 1;
    
			
	public Hospital setup(
			boolean usingSet) throws OHException 
	{
		Hospital hospital;
	
				
		if (usingSet == true)
		{
			hospital = new Hospital();
			_setParameters(hospital);
		}
		else
		{
			// Create Hospital with all parameters 
			hospital = new Hospital(code, description, address, city, telephone, fax, email, currencyCod, lock);
		}
				    	
		return hospital;
	}
	
	public void _setParameters(
			Hospital hospital) 
	{	
		hospital.setCode(code);
		hospital.setDescription(description);
		hospital.setAddress(address);
		hospital.setCity(city);
		hospital.setTelephone(telephone);
		hospital.setEmail(email);
		hospital.setFax(fax);
		hospital.setCurrencyCod(currencyCod);
		hospital.setLock(lock);
		
		return;
	}
	
	public void check(
			Hospital hospital) 
	{		
    	assertEquals(code, hospital.getCode());
    	assertEquals(description, hospital.getDescription());
    	assertEquals(address, hospital.getAddress());
    	assertEquals(city, hospital.getCity());
    	assertEquals(telephone, hospital.getTelephone());
    	assertEquals(email, hospital.getEmail());
    	assertEquals(fax, hospital.getFax());
    	assertEquals(currencyCod, hospital.getCurrencyCod());
    	assertEquals(lock, hospital.getLock());
		
		return;
	}
}
