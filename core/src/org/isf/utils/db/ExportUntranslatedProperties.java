/**
 * ExportUntraslatedProperties.java - 04/set/2013
 */
package org.isf.utils.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Mwithi
 *
 */
public class ExportUntranslatedProperties {

	private static ResourceBundle resourceBundleIT = null;
	private static ResourceBundle resourceBundleEN = null;
	private static ResourceBundle resourceBundleFR = null;
	private static ResourceBundle resourceBundleSW = null;
	private static ResourceBundle resourceBundleDE = null;
	private static ResourceBundle resourceBundleAR = null;
	private static ResourceBundle resourceBundleES = null;
	private static ResourceBundle resourceBundlePT = null;
	
	private static boolean viewKeys = false;
	
	/**
	 * This utility creates in the specified path
	 * a file for each language with untraslated keys (empty or unchanged from english)
	 */
	public ExportUntranslatedProperties() {
		
		try {
			resourceBundleEN = ResourceBundle.getBundle("language", new Locale("en"));
			resourceBundleIT = ResourceBundle.getBundle("language", new Locale("it"));
			resourceBundleFR = ResourceBundle.getBundle("language", new Locale("fr"));
			resourceBundleSW = ResourceBundle.getBundle("language", new Locale("sw"));
			resourceBundleDE = ResourceBundle.getBundle("language", new Locale("de"));
			resourceBundleAR = ResourceBundle.getBundle("language", new Locale("ar"));
			resourceBundleES = ResourceBundle.getBundle("language", new Locale("es"));
			resourceBundlePT = ResourceBundle.getBundle("language", new Locale("pt"));
			System.out.println(">> Resources bundles loaded.");
		} catch (MissingResourceException e) {
			System.out.println(">> any missing resource bundle? Please comment out in case...");
			System.exit(1);
			//throw new RuntimeException("no resource bundle found.");
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ExportUntranslatedProperties();
		/*
		 * path where to put extracted keys
		 */
		String path = "E:\\translations\\toTranslators\\";
		/*
		 * new lines warning string
		 */
		String warning = "#### WARNING #### \\n --> new line: please keep it as is in the translation";
		// create files
		File fileAR = new File(path + "language_ar_delta.properties");
		File fileDE = new File(path + "language_de_delta.properties");
		File fileIT = new File(path + "language_it_delta.properties");
		File fileFR = new File(path + "language_fr_delta.properties");
		File fileSW = new File(path + "language_sw_delta.properties");
		File fileES = new File(path + "language_es_delta.properties");
		File filePT = new File(path + "language_pt_delta.properties");
		
		// open files
		PrintWriter writerAR = null;
		PrintWriter writerDE = null;
		PrintWriter writerIT = null;
		PrintWriter writerFR = null;
		PrintWriter writerSW = null;
		PrintWriter writerES = null;
		PrintWriter writerPT = null;
		try {
			writerAR = new PrintWriter(fileAR, "UTF-8"); writerAR.println(warning);
			writerDE = new PrintWriter(fileDE, "UTF-8"); writerDE.println(warning);
			writerIT = new PrintWriter(fileIT, "UTF-8"); writerIT.println(warning);
			writerFR = new PrintWriter(fileFR, "UTF-8"); writerFR.println(warning);
			writerSW = new PrintWriter(fileSW, "UTF-8"); writerSW.println(warning);
			writerES = new PrintWriter(fileES, "UTF-8"); writerES.println(warning);
			writerPT = new PrintWriter(filePT, "UTF-8"); writerPT.println(warning);
		
			ArrayList<String> keys = new ArrayList<String>(resourceBundleEN.keySet());
			Collections.sort(keys);
			System.out.println("Found " + keys.size() + " keys.");
			
			for (String key : keys) {
				String value = resourceBundleEN.getString(key);
				if (value.equalsIgnoreCase("ok")) continue; 
				
				if (viewKeys) System.out.println(">> " + key);
				if (!resourceBundleAR.containsKey(key) || 
						resourceBundleAR.getString(key).equalsIgnoreCase(value) || 
						resourceBundleAR.getString(key).equals("")) {
					writerAR.println(key + " = " + value.replace("\n", "\\n"));
				} 
				if (!resourceBundleDE.containsKey(key) || 
						resourceBundleDE.getString(key).equalsIgnoreCase(value) ||
						resourceBundleDE.getString(key).equals("")) {
					writerDE.println(key + " = " + value.replace("\n", "\\n"));
				}
				if (!resourceBundleIT.containsKey(key) || 
						resourceBundleIT.getString(key).equalsIgnoreCase(value) ||
						resourceBundleIT.getString(key).equals("")) {
					writerIT.println(key + " = " + value.replace("\n", "\\n"));
				}
				if (!resourceBundleFR.containsKey(key) || 
						resourceBundleFR.getString(key).equalsIgnoreCase(value) ||
						resourceBundleFR.getString(key).equals("")) {
					writerFR.println(key + " = " + value.replace("\n", "\\n"));
				} 
				if (!resourceBundleSW.containsKey(key) || 
						resourceBundleSW.getString(key).equalsIgnoreCase(value) ||
						resourceBundleSW.getString(key).equals("")) {
					writerSW.println(key + " = " + value.replace("\n", "\\n"));
				} 
				if (!resourceBundleES.containsKey(key) || 
						resourceBundleES.getString(key).equalsIgnoreCase(value) ||
						resourceBundleES.getString(key).equals("")) {
					writerES.println(key + " = " + value.replace("\n", "\\n"));
				}
				if (!resourceBundlePT.containsKey(key) || 
						resourceBundlePT.getString(key).equalsIgnoreCase(value) ||
						resourceBundlePT.getString(key).equals("")) {
					writerPT.println(key + " = " + value.replace("\n", "\\n"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Done.");
			writerAR.close();
			writerDE.close();
			writerIT.close();
			writerFR.close();
			writerSW.close();
			writerES.close();
			writerPT.close();
		}
	}
}
