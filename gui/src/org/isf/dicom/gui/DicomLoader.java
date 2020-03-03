package org.isf.dicom.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.isf.dicom.manager.AbstractDicomLoader;
import org.isf.generaldata.MessageBundle;

/**
 * Progress loading
 * 
 * @author Pietro Castellucci
 * @version 1.0.0
 */
public class DicomLoader extends AbstractDicomLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int numfiles = 0;
	private JLabel jLabelTitle = null;
	private String labelTitle = MessageBundle.getMessage("angal.dicom.loading");
	private	JProgressBar bar = null;
	private Color bkgColor = Color.BLUE;
	private Color fgColor = Color.WHITE;

	public DicomLoader(int numfiles, JFrame owner) {
		super(owner);
		jLabelTitle = new JLabel(labelTitle);
		jLabelTitle.setForeground(fgColor);
		this.numfiles = numfiles;
		JPanel jp = new JPanel(new BorderLayout());
		jp.setBackground(bkgColor);
		bar = new JProgressBar(0, numfiles);
		jp.add(jLabelTitle, BorderLayout.NORTH);
		jp.add(bar, BorderLayout.CENTER);
		add(jp);
		setVisible(true);
		pack();

		setLocationRelativeTo(owner);
		setVisible(true);
	}

	public void setLoaded(int loaded) {
		bar.setValue(loaded);
		jLabelTitle.setText(labelTitle + " [" + loaded + "/" + numfiles + "]");
	}
}
