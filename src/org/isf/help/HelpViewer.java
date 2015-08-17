/**
 * HelpViewer.java - 27/nov/2012
 */
package org.isf.help;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;

/**
 * @author Mwithi
 * 
 */
public class HelpViewer extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String MANUAL_PDF_FILE = "doc/UserManual.pdf";

	/**
	 * 
	 */
	public HelpViewer() {
		File file = new File(MANUAL_PDF_FILE);
		if (file != null) {
			if (Desktop.isDesktopSupported()) { //Try to find system PDF viewer
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(HelpViewer.this, MessageBundle.getMessage("angal.help.userguidenotfound"));
				}
			} else if (!GeneralData.INTERNALVIEWER) { //Try specified PDF viewer, if any
				try {
					Runtime rt = Runtime.getRuntime();
					rt.exec(GeneralData.VIEWER + " " + file);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(HelpViewer.this, MessageBundle.getMessage("angal.help.pdfviewernotfoundoruserguidenotfound"));
				}
			} else { //abort operation
				JOptionPane.showMessageDialog(HelpViewer.this, MessageBundle.getMessage("angal.help.pdfviewernotfound"));
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new HelpViewer();
	}
}
