package org.isf.dicom.manager;

import java.awt.Window;

import javax.swing.JFrame;

/**
 * Abstract Component for Progress loading
 * 
 * @author Mwithi
 * @version 1.0.0
 */
public abstract class AbstractDicomLoader extends Window {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractDicomLoader(JFrame owner) {
		super(owner);
	}

	public AbstractDicomLoader(int numfiles, JFrame owner) {
		super(owner);
	}

	public void setLoaded(int loaded) {
	}
	
}
