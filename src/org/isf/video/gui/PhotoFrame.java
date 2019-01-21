package org.isf.video.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PhotoFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhotoFrame (String path, String device, String resolution)	{
		super("Photo preview");
		
		Container contentPane = getContentPane();
		
		contentPane.setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		PhotoPreviewPanel imgPanel = new PhotoPreviewPanel(path);
		centerPanel.add(imgPanel);
		
		contentPane.add(centerPanel, BorderLayout.CENTER);
		
		JLabel photoInfo = new JLabel("<html><center><br>Shoot with: " + device
									+ "<br><br>Original picture resolution: " + resolution 
									+ "<br><br></center></html>");		
		
		contentPane.add(photoInfo, BorderLayout.SOUTH);

		pack();
	}
}
