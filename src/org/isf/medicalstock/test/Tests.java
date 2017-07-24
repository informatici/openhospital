package org.isf.medicalstock.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.isf.supplier.model.Supplier;
import org.isf.supplier.test.TestSupplier;
import org.isf.supplier.test.TestSupplierContext;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.isf.ward.model.Ward;
import org.isf.ward.test.TestWard;
import org.isf.ward.test.TestWardContext;
import org.isf.medicals.model.Medical;
import org.isf.medicals.test.TestMedical;
import org.isf.medicals.test.TestMedicalContext;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medicalstock.service.MedicalStockIoOperations;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medicalstock.service.MedicalStockIoOperations.MovementOrder;
import org.isf.medstockmovtype.test.TestMovementType;
import org.isf.medstockmovtype.test.TestMovementTypeContext;
import org.isf.medtype.model.MedicalType;
import org.isf.medtype.test.TestMedicalType;
import org.isf.medtype.test.TestMedicalTypeContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestLot testLot;
	private static TestLotContext testLotContext;
	private static TestMovement testMovement;
	private static TestMovementContext testMovementContext;
	private static TestMedical testMedical;
	private static TestMedicalContext testMedicalContext;
	private static TestMedicalType testMedicalType;
	private static TestMedicalTypeContext testMedicalTypeContext;
	private static TestMovementType testMovementType;
	private static TestMovementTypeContext testMovementTypeContext;
	private static TestWard testWard;
	private static TestWardContext testWardContext;
	private static TestSupplier testSupplier;
	private static TestSupplierContext testSupplierContext;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testLot = new TestLot();
    	testLotContext = new TestLotContext();
    	testMovement = new TestMovement();
    	testMovementContext = new TestMovementContext();
    	testMedical = new TestMedical();
    	testMedicalContext = new TestMedicalContext();
    	testMedicalType = new TestMedicalType();
    	testMedicalTypeContext = new TestMedicalTypeContext();
    	testMovementType = new TestMovementType();
    	testMovementTypeContext = new TestMovementTypeContext();
    	testWard = new TestWard();
    	testWardContext = new TestWardContext();
    	testSupplier = new TestSupplier();
    	testSupplierContext = new TestSupplierContext();
    	
        return;
    }

    @Before
    public void setUp() throws OHException
    {
        jpa.open();
        
        testLot.setup(false);
        
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
    	testLot = null;
    	testLotContext = null;
    	testMovement = null;
    	testMovementContext = null;
    	testMedical = null;
    	testMedicalContext = null;
    	testMedicalType = null;
    	testMedicalTypeContext = null;
    	testMovementType = null;
    	testMovementTypeContext = null;
    	testWard = null;
    	testWardContext = null;
    	testSupplier = null;
    	testSupplierContext = null;

    	return;
    }
	
	
	@Test
	public void testLotGets() 
	{
		String code = "";
			
		
		try 
		{		
			code = _setupTestLot(false);
			_checkLotIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testLotSets() 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestLot(true);
			_checkLotIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testMovementGets() 
	{
		int code = 0;
			
		
		try 
		{		
			code = _setupTestMovement(false);
			_checkMovementIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testMovementSets() 
	{
		int code = 0;
			

		try 
		{		
			code = _setupTestMovement(true);
			_checkMovementIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}

	@Test
	public void testIoGetMedicalsFromLot() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			List<Integer> medicalIds = ioOperations.getMedicalsFromLot(foundMovement.getLot().getCode());

			assertEquals(foundMovement.getMedical().getCode(), medicalIds.get(0));
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetLotsByMedical() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			List<Lot> lots = ioOperations.getLotsByMedical(foundMovement.getMedical());

			assertEquals(foundMovement.getLot().getCode(), lots.get(0).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewAutomaticDischargingMovement() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			boolean result = ioOperations.newAutomaticDischargingMovement(foundMovement);

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
	public void testIoNewMovement() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			boolean result = ioOperations.newMovement(foundMovement);

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
	public void testIoPrepareChargingMovement() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			boolean result = ioOperations.prepareChargingMovement(foundMovement);

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
	public void testIoPrepareDischargingMovement() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			boolean result = ioOperations.prepareDischargingwMovement(foundMovement);

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
	public void testIoLotExists() 
	{
		String code = "";
		boolean result = false;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestLot(false);
			result = ioOperations.lotExists(code);
			
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
	public void testIoGetMovements() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			ArrayList<Movement> movements = ioOperations.getMovements();

			assertEquals(foundMovement.getCode(), movements.get(0).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetMovementsWihParameters() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar fromDate = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);
		GregorianCalendar toDate = new GregorianCalendar(now.get(Calendar.YEAR), 3, 3);
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			ArrayList<Movement> movements = ioOperations.getMovements(
					foundMovement.getWard().getCode(),
					fromDate,
					toDate);

			assertEquals(foundMovement.getCode(), movements.get(0).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetMovementsWihAllParameters() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar fromDate = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);
		GregorianCalendar toDate = new GregorianCalendar(now.get(Calendar.YEAR), 3, 3);
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			ArrayList<Movement> movements = ioOperations.getMovements(
					foundMovement.getMedical().getCode(),
					foundMovement.getMedical().getType().getCode(),
					foundMovement.getWard().getCode(), 
					foundMovement.getType().getCode(),
					fromDate, 
					toDate,
					fromDate, 
					toDate,
					fromDate, 
					toDate);

			assertEquals(foundMovement.getCode(), movements.get(0).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetMovementForPrint() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar fromDate = new GregorianCalendar(now.get(Calendar.YEAR), 1, 1);
		GregorianCalendar toDate = new GregorianCalendar(now.get(Calendar.YEAR), 3, 3);
		MovementOrder order = MedicalStockIoOperations.MovementOrder.DATE;
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			ArrayList<Movement> movements = ioOperations.getMovementForPrint(												
												foundMovement.getMedical().getDescription(), 
												null,
												foundMovement.getWard().getCode(),
												foundMovement.getType().getCode(),
												fromDate,
												toDate,
												foundMovement.getLot().getCode(),
												order
											);

			assertEquals(foundMovement.getCode(), movements.get(0).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	
	
	@Test
	public void testIoGetLastMovementDate() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			GregorianCalendar gc = ioOperations.getLastMovementDate();

			assertEquals(foundMovement.getDate(), gc);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoRefNoExists() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			boolean result = ioOperations.refNoExists(foundMovement.getRefNo());

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
	public void testIoGetMovementsByReference() 
	{
		int code = 0;
		MedicalStockIoOperations ioOperations = new MedicalStockIoOperations();
		
		
		try 
		{		
			code = _setupTestMovement(false);
			Movement foundMovement = (Movement)jpa.find(Movement.class, code); 
			ArrayList<Movement> movements = ioOperations.getMovementsByReference(foundMovement.getRefNo() );

			assertEquals(foundMovement.getCode(), movements.get(0).getCode());
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
		testLotContext.saveAll(jpa);
		testMovementContext.saveAll(jpa);
    	testMedicalContext.saveAll(jpa);
    	testMedicalTypeContext.saveAll(jpa);
    	testMovementTypeContext.saveAll(jpa);
    	testWardContext.saveAll(jpa);
    	testSupplierContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
		System.out.println(testLotContext.getAllSaved());
		System.out.println(testMovementContext.getAllSaved());
		testMovementContext.deleteNews(jpa);
		testMedicalContext.deleteNews(jpa);
		testLotContext.deleteNews(jpa);
    	testMedicalTypeContext.deleteNews(jpa);
    	testMovementTypeContext.deleteNews(jpa);
    	testWardContext.deleteNews(jpa);
    	testSupplierContext.deleteNews(jpa);
        
        return;
    }
    
	private String _setupTestLot(
			boolean usingSet) throws OHException 
	{
		Lot lot;
		
	
		jpa.beginTransaction();	
		lot = testLot.setup(usingSet);
		jpa.persist(lot);
		jpa.commitTransaction();
		
		return lot.getCode();
	}
		
	private void  _checkLotIntoDb(
			String code) throws OHException 
	{
		Lot foundLot;
		
	
		foundLot = (Lot)jpa.find(Lot.class, code); 
		testLot.check(foundLot);
		
		return;
	}	
    
	private int _setupTestMovement(
			boolean usingSet) throws OHException 
	{
		Movement movement;
		MedicalType medicalType = testMedicalType.setup(false);
		Medical medical = testMedical.setup(medicalType, false);
		MovementType movementType = testMovementType.setup(false);
		Ward ward = testWard.setup(false);
		Lot lot = testLot.setup(false);
		Supplier supplier = testSupplier.setup(false);
				
	
		jpa.beginTransaction();	
		movement = testMovement.setup(medical, movementType, ward, lot, supplier, usingSet);
		jpa.persist(supplier);
		jpa.persist(lot);
		jpa.persist(ward);
		jpa.persist(medicalType);
		jpa.persist(medical);
		jpa.persist(movementType);
		jpa.persist(movement);
		jpa.commitTransaction();
		
		return movement.getCode();
	}
		
	private void  _checkMovementIntoDb(
			int code) throws OHException 
	{
		Movement foundMovement;
		
	
		foundMovement = (Movement)jpa.find(Movement.class, code); 
		testMovement.check(foundMovement);
		
		return;
	}	
}