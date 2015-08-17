/**
 * ExportUntraslatedProperties.java - 04/set/2013
 */
package org.isf.utils.db;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Mwithi
 * 
 */
public class ImportTranslatedProperties {

	/**
	 * This utility import from specified path
	 * the keys in the original language properties.
	 * 
	 * Results are unsorted and unaligned so a refresh in
	 * ResourceBundleEditor plugin is neeed before commit
	 */
	public ImportTranslatedProperties() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * path where to find new translations
		 */
		String pathOriginal = "bundle/";
		String pathIn = "E:\\translations\\fromTranslators\\"; //translations from translators
		String pathOut = "E:\\translations\\bundle\\"; //new generated bundle to copy (replace) in OH
		File folderIn = new File(pathIn);
		
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return !file.isDirectory();
			}
		};
		
		for (final File file : folderIn.listFiles(fileFilter)) {
			String filename = file.getName();
			System.out.println("Processing... " + filename);

			// if (true) continue;

			//FileInputStream in;
			InputStream inputStream;
			Reader in;
			FileOutputStream out;
			try {

				//in = new FileInputStream(pathOriginal + filename);
				inputStream = new FileInputStream(pathOriginal + filename);
				in = new InputStreamReader(inputStream, "UTF-8");
				Properties propsOri = new Properties();
				propsOri.load(in);
				in.close();
				Enumeration<?> oriKeys = propsOri.propertyNames();

				//in = new FileInputStream(pathIn + filename);
				inputStream = new FileInputStream(pathIn + filename);
				in = new InputStreamReader(inputStream, "UTF-8");
				Properties propsIn = new Properties();
				propsIn.load(in);
				in.close();
				Enumeration<?> newKeys = propsIn.propertyNames();

				out = new FileOutputStream(pathOut + filename);
				// keys already present
				while (oriKeys.hasMoreElements()) {
					String key = (String) oriKeys.nextElement();
					String value = propsOri.getProperty(key);

					if (propsIn.containsKey(key)) {
						value = propsIn.getProperty(key);
					}

					
					propsOri.setProperty(key, value);
					
				}
				
				// new keys
				while (newKeys.hasMoreElements()) {
					String key = (String) newKeys.nextElement();
					
					if (!propsOri.containsKey(key)) {
						String value = propsIn.getProperty(key);

						//System.out.println(key + " = " + value);
						propsOri.setProperty(key, value);
						
						
					}
				}
				propsOri.store(out, null);
				out.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done.");
	}
}
