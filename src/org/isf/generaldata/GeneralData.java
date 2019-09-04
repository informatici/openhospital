package org.isf.generaldata;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*-------------------------------------------
 *    12/2007 - isf bari - added resource bundle for internationalization
 * 19/06/2008 - isf bari - added patientsheet jasper report name
 * 20/12/2008 - isf bari - added patientextended
 * 01/01/2009 - Fabrizio - added OPDEXTENDED
 * 20/01/2009 - Chiara   - added attribute MATERNITYRESTARTINJUNE to reset progressive number of maternity ward
 * 25/02/2011 - Claudia  - added attribute MAINMENUALWAYSONTOP to handle main men� always on Top 
 * 01/05/2011 - Vito 	 - added attribute VIDEOMODULEENABLED to enable/disable video module
 * 10/08/2011 - Claudia  - added PATIENTVACCINEEXTENDED to show patient on Patient Vaccine 
 * 19/10/2011 - Mwithi   - GeneralData 2.0: catching exception on single property and assign DEFAULT value  
 * 29/12/2011 - Nicola   - added XMPPMODULEENABLED to enable/disable communication module
 -------------------------------------------*/

public class GeneralData {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String FILE_PROPERTIES = "generalData.properties";

	public static String LANGUAGE;
	public static boolean SINGLEUSER;
	public static boolean AUTOMATICLOT;
	public static boolean LOTWITHCOST;
	public static String PATIENTSHEET;
	public static String OPDCHART;
	public static String ADMCHART;
	public static String DISCHART;
	public static String PATIENTBILL;
	public static String BILLSREPORT;
	public static String BILLSREPORTMONTH;
	public static String PHARMACEUTICALORDER;
	public static String PHARMACEUTICALSTOCK;
	public static String PHARMACEUTICALAMC;
	public static boolean PATIENTEXTENDED;
	public static boolean OPDEXTENDED;
	public static boolean MATERNITYRESTARTINJUNE;
	public static boolean LABEXTENDED;
	public static boolean INTERNALVIEWER;
	public static boolean LABMULTIPLEINSERT;
	public static boolean INTERNALPHARMACIES;
	public static boolean MERGEFUNCTION;
	public static boolean SMSENABLED;
	public static String VIEWER;
	public static boolean MAINMENUALWAYSONTOP;
	public static boolean RECEIPTPRINTER;
	public static boolean VIDEOMODULEENABLED;
	public static boolean DEBUG;
	public static boolean PATIENTVACCINEEXTENDED;
	public static boolean ENHANCEDSEARCH;
	public static boolean XMPPMODULEENABLED;
	public static boolean DICOMMODULEENABLED;
	public static boolean ALLOWMULTIPLEOPENEDBILL;
	
	private static String DEFAULT_LANGUAGE = "en";
	private static boolean DEFAULT_SINGLEUSER = false;
	private static boolean DEFAULT_AUTOMATICLOT = true;
	private static boolean DEFAULT_LOTWITHCOST = false;
	private static String DEFAULT_PATIENTSHEET = "patient_clinical_sheet";
	private static String DEFAULT_OPDCHART = "patient_opd_chart";
	private static String DEFAULT_ADMCHART = "patient_adm_chart";
	private static String DEFAULT_DISCHART = "patient_dis_chart";
	private static String DEFAULT_PATIENTBILL = "PatientBill";
	private static String DEFAULT_BILLSREPORT = "BillsReport";
	private static String DEFAULT_BILLSREPORTMONTH = "BillsReportMonth";
	private static String DEFAULT_PHARMACEUTICALORDER = "PharmaceuticalOrder";
	private static String DEFAULT_PHARMACEUTICALSTOCK = "PharmaceuticalStock";
	private static String DEFAULT_PHARMACEUTICALAMC = "PharmaceuticalAMC";
	private static boolean DEFAULT_PATIENTEXTENDED = false;
	private static boolean DEFAULT_OPDEXTENDED = false;
	private static boolean DEFAULT_MATERNITYRESTARTINJUNE = false;
	private static boolean DEFAULT_LABEXTENDED = false;
	private static boolean DEFAULT_INTERNALVIEWER = true;
	private static boolean DEFAULT_LABMULTIPLEINSERT = false;
	private static boolean DEFAULT_INTERNALPHARMACIES = false;
	private static boolean DEFAULT_MERGEFUNCTION = false;
	private static boolean DEFAULT_SMSENABLED = false;
	private static boolean DEFAULT_MAINMENUALWAYSONTOP = false;
	private static boolean DEFAULT_RECEIPTPRINTER = false;
	private static boolean DEFAULT_VIDEOMODULEENABLED = false;
	private static boolean DEFAULT_DEBUG = false;
	private static boolean DEFAULT_PATIENTVACCINEEXTENDED = false;
	private static boolean DEFAULT_ENHANCEDSEARCH = false;
	private static boolean DEFAULT_XMPPMODULEENABLED=false;
    private static boolean DEFAULT_DICOMMODULEENABLED=false;
    private static boolean DEFAULT_ALLOWMULTIPLEOPENEDBILL=false;

	private static GeneralData mySingleData;
	private Properties p;

	private GeneralData() {
		try {
			p = new Properties();
			p.load(new FileInputStream("rsc" + File.separator + FILE_PROPERTIES));
			logger.info("File generalData.properties loaded. ");
			LANGUAGE = myGetProperty("LANGUAGE", DEFAULT_LANGUAGE);
			SINGLEUSER = myGetProperty("SINGLEUSER", DEFAULT_SINGLEUSER);
			AUTOMATICLOT = myGetProperty("AUTOMATICLOT", DEFAULT_AUTOMATICLOT);
			LOTWITHCOST = myGetProperty("LOTWITHCOST", DEFAULT_LOTWITHCOST);
			PATIENTSHEET = myGetProperty("PATIENTSHEET", DEFAULT_PATIENTSHEET);
			OPDCHART = myGetProperty("OPDCHART", DEFAULT_OPDCHART);
			ADMCHART = myGetProperty("ADMCHART", DEFAULT_ADMCHART);
			DISCHART = myGetProperty("DISCHART", DEFAULT_DISCHART);
			PATIENTBILL = myGetProperty("PATIENTBILL", DEFAULT_PATIENTBILL);
			BILLSREPORT = myGetProperty("BILLSREPORT", DEFAULT_BILLSREPORT);
			BILLSREPORTMONTH = myGetProperty("BILLSREPORTMONTH", DEFAULT_BILLSREPORTMONTH);
			PHARMACEUTICALORDER = myGetProperty("PHARMACEUTICALORDER", DEFAULT_PHARMACEUTICALORDER);
			PHARMACEUTICALSTOCK = myGetProperty("PHARMACEUTICALSTOCK", DEFAULT_PHARMACEUTICALSTOCK);
			PHARMACEUTICALAMC = myGetProperty("PHARMACEUTICALAMC", DEFAULT_PHARMACEUTICALAMC);
			PATIENTEXTENDED = myGetProperty("PATIENTEXTENDED", DEFAULT_PATIENTEXTENDED);
			OPDEXTENDED = myGetProperty("OPDEXTENDED", DEFAULT_OPDEXTENDED);
			MATERNITYRESTARTINJUNE = myGetProperty("MATERNITYRESTARTINJUNE", DEFAULT_MATERNITYRESTARTINJUNE);
			LABEXTENDED = myGetProperty("LABEXTENDED", DEFAULT_LABEXTENDED);
			LABMULTIPLEINSERT = myGetProperty("LABMULTIPLEINSERT", DEFAULT_LABMULTIPLEINSERT);
			INTERNALPHARMACIES = myGetProperty("INTERNALPHARMACIES", DEFAULT_INTERNALPHARMACIES);
			INTERNALVIEWER = myGetProperty("INTERNALVIEWER", DEFAULT_INTERNALVIEWER);
			if (!INTERNALVIEWER) VIEWER = p.getProperty("INTERNALVIEWER");
			MERGEFUNCTION = myGetProperty("MERGEFUNCTION", DEFAULT_MERGEFUNCTION);
			SMSENABLED = myGetProperty("SMSENABLED", DEFAULT_SMSENABLED);
			MAINMENUALWAYSONTOP = myGetProperty("MAINMENUALWAYSONTOP", DEFAULT_MAINMENUALWAYSONTOP);
			RECEIPTPRINTER = myGetProperty("RECEIPTPRINTER", DEFAULT_RECEIPTPRINTER);
			VIDEOMODULEENABLED = myGetProperty("VIDEOMODULEENABLED", DEFAULT_VIDEOMODULEENABLED);
			DEBUG = myGetProperty("DEBUG", DEFAULT_DEBUG);
			PATIENTVACCINEEXTENDED = myGetProperty("PATIENTVACCINEEXTENDED", DEFAULT_PATIENTVACCINEEXTENDED);
			ENHANCEDSEARCH = myGetProperty("ENHANCEDSEARCH", DEFAULT_ENHANCEDSEARCH);
			XMPPMODULEENABLED = myGetProperty("XMPPMODULEENABLED", DEFAULT_XMPPMODULEENABLED);
			DICOMMODULEENABLED = myGetProperty("DICOMMODULEENABLED", DEFAULT_DICOMMODULEENABLED);
			ALLOWMULTIPLEOPENEDBILL = myGetProperty("ALLOWMULTIPLEOPENEDBILL", DEFAULT_ALLOWMULTIPLEOPENEDBILL);
			
		} catch (Exception e) { //no file
			logger.error(">> " + FILE_PROPERTIES + " file not found.");
			System.exit(1);
		}
		MessageBundle.initialize();
	}
	
	/**
	 * 
	 * Method to retrieve a boolean property
	 * 
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	private boolean myGetProperty(String property, boolean defaultValue) {
		boolean value;
		try {
			value = p.getProperty(property).equalsIgnoreCase("YES");
		} catch (Exception e) {
			logger.warn(">> " + property + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		return value;
	}

	/**
	 * 
	 * Method to retrieve a string property
	 * 
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	private String myGetProperty(String property, String defaultValue) {
		String value;
		value = p.getProperty(property);
		if (value == null) {
			logger.warn(">> " + property + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		return value;
	}

	public static GeneralData getGeneralData() {
		if (mySingleData == null) {
			mySingleData = new GeneralData();
		}
		return mySingleData;
	}
}

