package org.isf.dicom.test;


import static org.junit.Assert.assertEquals;

import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.dicom.model.FileDicom;
import org.isf.dicom.service.DicomIoOperations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestDicom testFileDicom;
	private static TestDicomContext testFileDicomContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testFileDicom = new TestDicom();
    	testFileDicomContext = new TestDicomContext();
    	
        return;
    } 

    @Before
    public void setUp() throws OHException
    {
        jpa.open();
        
        _saveContext();
		
		return;
    }
        
    @After
    public void tearDown() throws Exception 
    {
        _restoreContext();   
        
        jpa.flush();
        jpa.close();
                
        return;
    }
    
    @AfterClass
    public static void tearDownClass() throws OHException 
    {
    	//jpa.destroy();
    	testFileDicom = null;
    	testFileDicomContext = null;

    	return;
    }
	
		
	@Test
	public void testFileDicomGets() 
	{
		long code = 0;
			

		try 
		{		
			code = _setupTestFileDicom(false);
			_checkFileDicomIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	
	@Test
	public void testFileDicomSets()
	{
		long code = 0;
			

		try 
		{		
			code = _setupTestFileDicom(true);
			_checkFileDicomIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	
	@Test
	public void testIoGetSerieDetail() 
	{
		long code = 0;
		DicomIoOperations ioOperations = new DicomIoOperations();
		
		
		try 
		{		
			code = _setupTestFileDicom(false);
			FileDicom foundFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			Long[] dicoms = ioOperations.getSerieDetail(foundFileDicom.getPatId(), foundFileDicom.getDicomSeriesNumber());
			
			assertEquals(1, dicoms.length);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}


	@Test
	public void testIoDeleteSerie() 
	{
		long code = 0;
		DicomIoOperations ioOperations = new DicomIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestFileDicom(false);
			FileDicom foundFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			result = ioOperations.deleteSerie(foundFileDicom.getPatId(), foundFileDicom.getDicomSeriesNumber());
			
			assertEquals(true, result);
			FileDicom deletedFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			assertEquals(null, deletedFileDicom);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	
	@Test
	public void testIoLoadDettaglio() 
	{
		long code = 0;
		DicomIoOperations ioOperations = new DicomIoOperations();
		

		try 
		{		
			code = _setupTestFileDicom(false);
			FileDicom foundFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			FileDicom dicom = ioOperations.loadDettaglio(foundFileDicom.getIdFile(), foundFileDicom.getPatId(), foundFileDicom.getDicomSeriesNumber());
			
			assertEquals(foundFileDicom.getDicomSeriesDescription(), dicom.getDicomSeriesDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	
	
	
	@Test
	public void testIoLoadFilesPaziente() 
	{
		long code = 0;
		DicomIoOperations ioOperations = new DicomIoOperations();
		
		
		try 
		{		
			code = _setupTestFileDicom(false);
			FileDicom foundFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			FileDicom[] dicoms = ioOperations.loadFilesPaziente(foundFileDicom.getPatId());

			assertEquals(foundFileDicom.getDicomSeriesDescription(), dicoms[0].getDicomSeriesDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	
	@Test
	public void testIoExist() 
	{
		long code = 0;
		DicomIoOperations ioOperations = new DicomIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestFileDicom(false);
			FileDicom foundFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			result = ioOperations.exist(foundFileDicom);

			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		
		return;
	}
		
	
	@Test
	public void testIoGetImagesCounts() 
	{
		//TODO: function not yet ported to JPA. The test has to fail
		assertEquals(true, false);
	}
	
		
	@Test
	public void testIoSaveFile() 
	{
		long code = 0;
		DicomIoOperations ioOperations = new DicomIoOperations();
			
		
		try 
		{		
			code = _setupTestFileDicom(false);
			FileDicom foundFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			foundFileDicom.setDicomSeriesDescription("Update");
			ioOperations.saveFile(foundFileDicom);
			FileDicom updateFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
			
			assertEquals("Update", updateFileDicom.getDicomSeriesDescription());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
		
	
	
	private void _saveContext() throws OHException 
    {	
		testFileDicomContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testFileDicomContext.getAllSaved());
		testFileDicomContext.deleteNews(jpa);
        
        return;
    }
        
	private long _setupTestFileDicom(
			boolean usingSet) throws OHException 
	{
		FileDicom dicom;
		

    	jpa.beginTransaction();	
    	dicom = testFileDicom.setup(usingSet);
		jpa.persist(dicom);
    	jpa.commitTransaction();
    	
		return dicom.getIdFile();
	}
		
	private void  _checkFileDicomIntoDb(
			long code) throws OHException 
	{
		FileDicom foundFileDicom;
		

		foundFileDicom = (FileDicom)jpa.find(FileDicom.class, code); 
		testFileDicom.check(foundFileDicom);
		
		return;
	}	
}