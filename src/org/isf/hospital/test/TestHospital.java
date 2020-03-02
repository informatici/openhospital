package org.isf.hospital.test;

import static org.junit.Assert.assertEquals;

import org.isf.hospital.model.Hospital;
import org.isf.utils.exception.OHException;

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
    
			
	public Hospital setup(
			boolean usingSet) throws OHException 
	{
		Hospital hospital;
	
				
		if (usingSet)
		{
			hospital = new Hospital();
			_setParameters(hospital);
		}
		else
		{
			// Create Hospital with all parameters 
			hospital = new Hospital(code, description, address, city, telephone, fax, email, currencyCod);
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
		
		return;
	}
}
