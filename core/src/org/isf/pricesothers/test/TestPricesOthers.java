package org.isf.pricesothers.test;


import static org.junit.Assert.assertEquals;

import org.isf.pricesothers.model.PricesOthers;
import org.isf.utils.exception.OHException;


public class TestPricesOthers 
{	
    private String Code = "TestCode";
    private String Description = "TestDescription";
	private boolean opdInclude = true;
	private boolean ipdInclude = false;
	private boolean daily = true;
	private boolean discharge = false;
	private boolean undefined = true;
    
			
	public PricesOthers setup(
			boolean usingSet) throws OHException 
	{
		PricesOthers pricesOthers;
	
				
		if (usingSet)
		{
			pricesOthers = new PricesOthers();
			_setParameters(pricesOthers);
		}
		else
		{
			// Create PricesOthers with all parameters 
			pricesOthers = new PricesOthers(0, Code, Description, opdInclude, ipdInclude, daily, discharge, undefined);
		}
				    	
		return pricesOthers;
	}
	
	public void _setParameters(
			PricesOthers pricesOthers) 
	{	
		pricesOthers.setCode(Code);
		pricesOthers.setDescription(Description);
		pricesOthers.setDaily(daily);
		pricesOthers.setDischarge(discharge);
		pricesOthers.setIpdInclude(ipdInclude);
		pricesOthers.setOpdInclude(opdInclude);
		pricesOthers.setUndefined(undefined);		
		
		return;
	}
	
	public void check(
			PricesOthers pricesOthers) 
	{		
    	assertEquals(Code, pricesOthers.getCode());
    	assertEquals(Description, pricesOthers.getDescription());
    	assertEquals(daily, pricesOthers.isDaily());
    	assertEquals(discharge, pricesOthers.isDischarge());
    	assertEquals(ipdInclude, pricesOthers.isIpdInclude());
    	assertEquals(opdInclude, pricesOthers.isOpdInclude());
    	assertEquals(undefined, pricesOthers.isUndefined());	
		
		return;
	}
}
