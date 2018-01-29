package org.isf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	org.isf.accounting.test.Tests.class,
	org.isf.admission.test.Tests.class,
	org.isf.admtype.test.Tests.class,
	org.isf.agetype.test.Tests.class,
	org.isf.dicom.test.Tests.class,
	org.isf.disctype.test.Tests.class,
	org.isf.disease.test.Tests.class,
	org.isf.distype.test.Tests.class,	
	org.isf.dlvrrestype.test.Tests.class,
	org.isf.dlvrtype.test.Tests.class,
	org.isf.exa.test.Tests.class,
	org.isf.examination.test.Tests.class,	
	org.isf.exatype.test.Tests.class,	
	org.isf.hospital.test.Tests.class,
	org.isf.lab.test.Tests.class,
	org.isf.malnutrition.test.Tests.class,
	org.isf.medicals.test.Tests.class,
	org.isf.medicalstock.test.Tests.class,
	org.isf.medicalstockward.test.Tests.class,	
	org.isf.medstockmovtype.test.Tests.class,
	org.isf.medtype.test.Tests.class,
	org.isf.menu.test.Tests.class,
	org.isf.opd.test.Tests.class,	
	org.isf.operation.test.Tests.class,
	org.isf.opetype.test.Tests.class,	
	org.isf.patient.test.Tests.class,
	org.isf.patvac.test.Tests.class,
	org.isf.pregtreattype.test.Tests.class,	
	org.isf.priceslist.test.Tests.class,
	org.isf.pricesothers.test.Tests.class,
	org.isf.sms.test.Tests.class,
	org.isf.supplier.test.Tests.class,
	org.isf.therapy.test.Tests.class,
	org.isf.vaccine.test.Tests.class,	
	org.isf.vactype.test.Tests.class,
	org.isf.visits.test.Tests.class,
	org.isf.ward.test.Tests.class
	})
public class AllTests {

}