package org.isf.dicom.manager;

import java.awt.Window;

import javax.swing.JFrame;

/**
 * Abstrac Component for Progress loading
 * 
 * @author Mwithi
 * @version 1.0.0
 */
public abstract class AbstractDicomLoader extends Window {
	
	public AbstractDicomLoader(JFrame owner) {
		super(owner);
	}

	public AbstractDicomLoader(int numfiles, JFrame owner) {
		super(owner);
	}

	public void setLoaded(int loaded) {
	}
	
}
