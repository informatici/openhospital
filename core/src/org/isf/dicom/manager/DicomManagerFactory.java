package org.isf.dicom.manager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for instantiate DicomManager
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 */
public class DicomManagerFactory {

	private final static Logger logger = LoggerFactory.getLogger(DicomManagerFactory.class);
	
	private static DicomManagerInterface instance = null;

	private static Properties props = new Properties();

	/**
	 * return the manager for DICOM acquired files
	 * @throws OHServiceException 
	 */
	public synchronized static DicomManagerInterface getManager() throws OHServiceException {

		if (instance == null) {

			try {
				init();

				instance = (DicomManagerInterface) Context.getApplicationContext().getBean(Class.forName(props.getProperty("dicom.manager.impl"))); //.getConstructor(Class.forName("java.util.Properties")).newInstance(props);
			} catch(OHServiceException e){
				//Already managed, ready to return OHServiceException
				logger.error("", e);
				throw e;
			}catch(Exception e){
				//Any exception
				logger.error("", e);
				throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
						props.getProperty("dicom.manager.impl") + " " + MessageBundle.getMessage("angal.dicom.manager.noimpl"), OHSeverityLevel.ERROR));
			}
		}

		return instance;
	}

	private static void init() throws OHServiceException {
		try {

			File f = new File("rsc/dicom.properties");

			if (!f.exists()) {
				throw new OHServiceException( new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
						MessageBundle.getMessage("angal.dicom.nofile") + " rsc/dicom.manager.properties", OHSeverityLevel.ERROR));
			}

			FileInputStream in = new FileInputStream("rsc/dicom.properties");

			props.load(in);

			in.close();
		} catch (Exception ecc) {
			throw new OHServiceException(ecc, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"), 
					MessageBundle.getMessage("angal.dicom.manager.err") + " " + ecc.getMessage(), OHSeverityLevel.ERROR));
		}
	}
}
