package org.isf.video.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.isf.video.manager.VideoDeviceStreamAppletManager;
import org.isf.video.manager.VideoManager;

public class FullScreenStreamFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public FullScreenStreamFrame()	{
		//Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//setSize(new Dimension((int)ssize.getWidth(), (int)ssize.getHeight() -20));
		setResizable(false);
		
		//setDefaultLookAndFeelDecorated(false);
		setUndecorated(true);
	}
	
		
	public void showFrame(MiddlePanel middlePanel)	{
		middlePanel.setFullScreenMode();
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(middlePanel);
		contentPane.setBackground(Color.BLACK);
		contentPane.add(Box.createRigidArea(new Dimension(500,50)));
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		GraphicsDevice myDevice = gs[0];
		
		if (myDevice.isFullScreenSupported())	{
		    myDevice.setFullScreenWindow(this);
		    //System.out.println("fullscreeen");
		}
		
		setVisible(true);
	}
	
	
	public void showPhotoPreview(String fileName) {
		String path = VideoManager.shotPhotosTempDir + fileName + VideoManager.shotPhotosExtension;
		
		JPanel centerPanel = new JPanel();
		PhotoPreviewPanel imgPanel = new PhotoPreviewPanel(path);
		centerPanel.add(imgPanel);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JLabel imgLabel = new JLabel("<html><center>A photo was shoot.<br>Do you want to go back to normal screen mode?</center></html>");
		imgLabel.setPreferredSize(new Dimension((int)imgPanel.getPreferredSize().getWidth(), 50));
		
		panel.add(imgLabel,BorderLayout.NORTH);
		panel.add(centerPanel,BorderLayout.CENTER);
		
		VideoDeviceStreamAppletManager.currentStreamApplet.freeze();
		
		setVisible(false);
		
		int n = JOptionPane.showConfirmDialog(
			    this,
			    panel,
			    "Please confirm",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.PLAIN_MESSAGE,
			    null
			    );
		
		if (n == JOptionPane.YES_OPTION)	{
			VideoDeviceStreamAppletManager.currentStreamApplet.activate();
			((VideoFrame) VideoManager.getFrame()).restoreNormalSizeFrame();
		}
		else if (n == JOptionPane.NO_OPTION)	{
			VideoDeviceStreamAppletManager.currentStreamApplet.activate();
			setVisible(true);
			setState(NORMAL);
		}
	}
	
	
	public void hideFrame()	{
		setVisible(false);
	}
}
