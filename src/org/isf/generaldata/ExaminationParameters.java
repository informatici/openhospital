package org.isf.generaldata;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Mwithi
 * 
 * settings for Examination form
 *
 */
public class ExaminationParameters {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	

	public static int HEIGHT_MIN;
	private static int DEFAULT_HEIGHT_MIN = 0;
	
	public static int HEIGHT_MAX;
	private static int DEFAULT_HEIGHT_MAX = 250;
	
	public static int HEIGHT_INIT;
	private static int DEFAULT_HEIGHT_INIT = 170;
	
	public static String HEIGHT_UNIT;
	private static String DEFAULT_HEIGHT_UNIT = "cm";
	
	public static String WEIGHT_UNIT;
	private static String DEFAULT_WEIGHT_UNIT = "Kg";
	
	public static int WEIGHT_MIN;
	private static int DEFAULT_WEIGHT_MIN = 0;
	
	public static int WEIGHT_MAX;
	private static int DEFAULT_WEIGHT_MAX = 400;
	
	public static double WEIGHT_STEP;
	private static double DEFAULT_WEIGHT_STEP = 0.1;
	
	public static int WEIGHT_INIT;
	private static int DEFAULT_WEIGHT_INIT = 80;
	
	public static String AP_UNIT;
	private static String DEFAULT_AP_UNIT = "mmHg";
	
	public static int AP_MIN;
	private static int DEFAULT_AP_MIN = 80;
	
	public static int AP_MAX;
	private static int DEFAULT_AP_MAX = 120;
	
	public static String HR_UNIT;
	private static String DEFAULT_HR_UNIT = "bpm";
	
	public static int HR_MIN;
	private static int DEFAULT_HR_MIN = 0;
	
	public static int HR_MAX;
	private static int DEFAULT_HR_MAX = 240;
	
	public static int HR_INIT;
	private static int DEFAULT_HR_INIT = 60;
	
	public static String TEMP_UNIT;
	private static String DEFAULT_TEMP_UNIT = "°C";
	
	public static int TEMP_MIN;
	private static int DEFAULT_TEMP_MIN = 0;
	
	public static int TEMP_MAX;
	private static int DEFAULT_TEMP_MAX = 60;
	
	public static int TEMP_INIT;
	private static int DEFAULT_TEMP_INIT = 0;
	
	public static double TEMP_STEP;
	private static double DEFAULT_TEMP_STEP = 0.5;
	
	public static int SAT_MIN;
	private static int DEFAULT_SAT_MIN = 50;
	
	public static int SAT_MAX;
	private static int DEFAULT_SAT_MAX = 100;
	
	public static int SAT_INIT;
	private static int DEFAULT_SAT_INIT = 90;
	
	public static double SAT_STEP;
	private static double DEFAULT_SAT_STEP = 0.1;
	
	public static int LIST_SIZE;
	private static int DEFAULT_LIST_SIZE = 4;
	
	private static ExaminationParameters mySingleData;
	private Properties p;
	private PropertyReader propertyReader;

	private ExaminationParameters() {
		try {
			p = new Properties();
			p.load(new FileInputStream("rsc" + File.separator + "examination.properties"));
			propertyReader = new PropertyReader(p, logger);
			HEIGHT_MIN = myGetProperty("HEIGHT_MIN", DEFAULT_HEIGHT_MIN);
			HEIGHT_MAX = myGetProperty("HEIGHT_MAX", DEFAULT_HEIGHT_MAX);
			HEIGHT_INIT = myGetProperty("HEIGHT_INIT", DEFAULT_HEIGHT_INIT);
			WEIGHT_MIN = myGetProperty("WEIGHT_MIN", DEFAULT_WEIGHT_MIN);
			WEIGHT_MAX = myGetProperty("WEIGHT_MAX", DEFAULT_WEIGHT_MAX);
			WEIGHT_STEP = myGetProperty("WEIGHT_STEP", DEFAULT_WEIGHT_STEP);
			WEIGHT_INIT = myGetProperty("WEIGHT_INIT", DEFAULT_WEIGHT_INIT);
			AP_MIN = myGetProperty("AP_MIN", DEFAULT_AP_MIN);
			AP_MAX = myGetProperty("AP_MAX", DEFAULT_AP_MAX);
			HR_MIN = myGetProperty("HR_MIN", DEFAULT_HR_MIN);
			HR_MAX = myGetProperty("HR_MAX", DEFAULT_HR_MAX);
			HR_INIT = myGetProperty("HR_INIT", DEFAULT_HR_INIT);
			TEMP_MIN = myGetProperty("TEMP_MIN", DEFAULT_TEMP_MIN);
			TEMP_MAX = myGetProperty("TEMP_MAX", DEFAULT_TEMP_MAX);
			TEMP_INIT = myGetProperty("TEMP_INIT", DEFAULT_TEMP_INIT);
			TEMP_STEP = myGetProperty("TEMP_STEP", DEFAULT_TEMP_STEP);
			SAT_MIN = myGetProperty("SAT_MIN", DEFAULT_SAT_MIN);
			SAT_MAX = myGetProperty("SAT_MAX", DEFAULT_SAT_MAX);
			SAT_INIT = myGetProperty("SAT_INIT", DEFAULT_SAT_INIT);
			SAT_STEP = myGetProperty("SAT_STEP", DEFAULT_SAT_STEP);
			LIST_SIZE = myGetProperty("LIST_SIZE", DEFAULT_LIST_SIZE);
			HEIGHT_UNIT = propertyReader.readProperty("HEIGHT_UNIT", DEFAULT_HEIGHT_UNIT);
			WEIGHT_UNIT = propertyReader.readProperty("WEIGHT_UNIT", DEFAULT_WEIGHT_UNIT);
			AP_UNIT = propertyReader.readProperty("AP_UNIT", DEFAULT_AP_UNIT);
			HR_UNIT = propertyReader.readProperty("HR_UNIT", DEFAULT_HR_UNIT);
			TEMP_UNIT = propertyReader.readProperty("TEMP_UNIT", DEFAULT_TEMP_UNIT);
		} catch (Exception e) { //no file
			System.out.println("examination.properties file not found.");
		}
		//MessageBundle.initialize();
	}
	
	/**
     * Method to retrieve an integer property
     * 
     * @param property
     * @param defaultValue
     * @return
     */
    private int myGetProperty(String property, int defaultValue) {
    	int value;
		try {
			value = Integer.parseInt(p.getProperty(property));
		} catch (Exception e) {
			System.out.println(property + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		return value;
	}
    
    /**
     * Method to retrieve an integer property
     * 
     * @param property
     * @param defaultValue
     * @return
     */
    private double myGetProperty(String property, double defaultValue) {
    	double value;
		try {
			value = Double.parseDouble(p.getProperty(property));
		} catch (Exception e) {
			System.out.println(property + " property not found: default is " + defaultValue);
			return defaultValue;
		}
		return value;
	}
    
    public static ExaminationParameters getExaminationParameters() {
        if (mySingleData == null){ 
        	mySingleData = new ExaminationParameters();        	
        }
        return mySingleData;
    }
}

