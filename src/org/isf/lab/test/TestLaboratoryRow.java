package org.isf.lab.test;


import static org.junit.Assert.assertEquals;

import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryRow;
import org.isf.utils.exception.OHException;

public class TestLaboratoryRow 
{	
	private Integer code = 0;
    private String description = "TestDescription";
    
			
	public LaboratoryRow setup(
			Laboratory laboratory,
			boolean usingSet) throws OHException 
	{
		LaboratoryRow laboratoryRow;
	
				
		if (usingSet)
		{
			laboratoryRow = new LaboratoryRow();
			_setParameters(laboratoryRow, laboratory);
		}
		else
		{
			// Create LaboratoryRow with all parameters 
			laboratoryRow = new LaboratoryRow(laboratory, description);
		}
				    	
		return laboratoryRow;
	}
	
	public void _setParameters(
			LaboratoryRow laboratoryRow,
			Laboratory laboratory) 
	{	
		laboratoryRow.setDescription(description);
		laboratoryRow.setLabId(laboratory);
		
		return;
	}
	
	public void check(
			LaboratoryRow laboratoryRow) 
	{		
    	assertEquals(description, laboratoryRow.getDescription());
		
		return;
	}
}
