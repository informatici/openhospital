package org.isf.dicom.manager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

import org.isf.generaldata.MessageBundle;

/**
 * Factory for instantiate DicomManager
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 */
public class DicomManagerFactory {

	private static DicomManagerInterface instance = null;

	private static Properties props = new Properties();

	/**
	 * return the manager for DICOM acquired files
	 */
	public synchronized static DicomManagerInterface getManager() {

		if (instance == null) {

			init();

			try {
				instance = (DicomManagerInterface) Class.forName(props.getProperty("dicom.manager.impl")).getConstructor(Class.forName("java.util.Properties")).newInstance(props);
			} catch (Exception ecc) {
				JOptionPane.showMessageDialog(null, props.getProperty("dicom.manager.impl") + " " + MessageBundle.getMessage("angal.dicom.manager.noimpl"));

				System.exit(-100);
			}
		}

		return instance;
	}

	private static void init() {
		try {

			File f = new File("rsc/dicom.properties");

			if (!f.exists()) {
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.dicom.nofile") + " rsc/dicom.manager.properties");

				System.exit(-100);
			}

			FileInputStream in = new FileInputStream("rsc/dicom.properties");

			props.load(in);

			in.close();
		} catch (Exception ecc) {
			ecc.printStackTrace();

			JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.dicom.manager.err") + " " + ecc.getMessage());

			System.exit(-100);
		}
	}
}
