package org.isf.video.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.isf.generaldata.MessageBundle;
import org.isf.video.manager.VideoDeviceStreamApplet;
import org.isf.video.manager.VideoDeviceStreamAppletManager;
import org.isf.video.manager.VideoDevicesManager;
import org.isf.video.manager.VideoManager;

public class MiddlePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel streamPanel;
	
	//private JPanel blackPanel;
	private JLabel pleaseWaitLabel;
	
	public int standardWidth = 320;
	public int standardHeight = 240;
	
	public int standardHorMargin = 40;
	public int standardVerMargin = 40;
		
	private JPanel commandsPanel;
	private JPanel currentStreamInfoPanel;
	
	private JLabel streamInfoLabel;
	
	private JButton pauseOrResumeStreamButton;
	private JButton shootPhotoButton;
	private JButton fullScreenButton;
	private JButton normalSizeScreenButton;
	
	private ImageIcon playIcon = new ImageIcon("rsc/icons/play_button.png");
	private ImageIcon pauseIcon = new ImageIcon("rsc/icons/pause_button.png");
	private ImageIcon saveIcon = new ImageIcon("rsc/icons/shoot_button.png");
	
	private ImageIcon fullScreenIcon = new ImageIcon("rsc/icons/fullscreen_button.png");
	private ImageIcon normalSizeScreenIcon = new ImageIcon("rsc/icons/normalsizescreen_button.png");

	private String streamStatus;
	
	public MiddlePanel()	{
		
		setLayout(new BorderLayout());
		
		//setBackground(Color.black);

		//setMinimumSize(new Dimension(standardWidth + standardHorMargin, standardHeight + standardVerMargin));
		//setMaximumSize(new Dimension(700,450));
		
		initStreamPanel();
		
		initPleaseWaitLabel();
		
		streamInfoLabel = new JLabel("");
		streamInfoLabel.setForeground(Color.white);
		
		currentStreamInfoPanel = new JPanel();
		currentStreamInfoPanel.setBackground(Color.black);
		currentStreamInfoPanel.add(streamInfoLabel);
		
		initCommandPanel();
		
		add(commandsPanel, BorderLayout.SOUTH);
		add(streamPanel, BorderLayout.CENTER);
		add(currentStreamInfoPanel, BorderLayout.NORTH);
		
		streamStatus = "No stream detected";
		
		updateCurrentStreamInfo();
	}
	

	private void initStreamPanel() {
		streamPanel = new JPanel();
		streamPanel.setLayout(new GridBagLayout());
		streamPanel.setBackground(Color.black);
	}
	
	
	public void initCommandPanel()	{
		commandsPanel = new JPanel();
		commandsPanel.setBackground(Color.black);
		//commandsPanel.setMinimumSize(new Dimension(225, 25));
		//commandsPanel.setMaximumSize(new Dimension(225, 25));
		
		//int buttonWidth = 75;
		//int buttonHeight = 75;
		
		final String pauseText = MessageBundle.getMessage("angal.video.pausestream");
		final String resumeText = MessageBundle.getMessage("angal.video.resumestream");
		
		pauseOrResumeStreamButton = new JButton(pauseText, pauseIcon);
		//pauseOrResumeStreamButton.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
		//pauseOrResumeStreamButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		pauseOrResumeStreamButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		//pauseOrResumeStreamButton.setMnemonic(KeyEvent.VK_F);
		pauseOrResumeStreamButton.setMargin(new Insets(0, 0, 0, 0));
		pauseOrResumeStreamButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (streamStatus.equals("Playing"))	{
					VideoDeviceStreamAppletManager.currentStreamApplet.freeze();
					streamStatus = "Paused";
					updateCurrentStreamInfo();
					
					pauseOrResumeStreamButton.setText(resumeText);
					pauseOrResumeStreamButton.setIcon(playIcon);
				}
				else if ((streamStatus.equals("No stream"))	||	(streamStatus.equals("Paused")))	{
					VideoDeviceStreamAppletManager.currentStreamApplet.activate();
					streamStatus = "Playing";
					updateCurrentStreamInfo();
					
					pauseOrResumeStreamButton.setText(pauseText);
					pauseOrResumeStreamButton.setIcon(pauseIcon);
				}			
			}
		});
		
		
		shootPhotoButton = new JButton(MessageBundle.getMessage("angal.video.shootaphoto"), saveIcon);
		//shootPhotoButton.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
		//shootPhotoButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		shootPhotoButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		//shootPhotoButton.setMnemonic(KeyEvent.VK_S);
		shootPhotoButton.setMargin(new Insets(0, 0, 0, 0));
		shootPhotoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date ms = new Date();
				
				String filename = String.valueOf(ms.getTime());
				
				String generatedFileName = "";
				if (! filename.equals(""))
					generatedFileName = ((VideoFrame) VideoManager.getFrame()).savePhoto(filename);
				
				if ((! generatedFileName.equals(""))	&&	((VideoFrame) VideoManager.getFrame()).fullScreenMode)	{
					// The creation of a popup window is done by the FullScreanStreamFrame object
					// because have to be shown on top
					((VideoFrame) VideoManager.getFrame()).fullScreenStreamFrame.showPhotoPreview(generatedFileName);
				}
			}
		});
		
		
		fullScreenButton = new JButton(MessageBundle.getMessage("angal.video.setfullscreen"), fullScreenIcon);
		//fullScreenButton.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
		//fullScreenButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		fullScreenButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		//fullScreenButton.setMnemonic(KeyEvent.VK_U);
		fullScreenButton.setMargin(new Insets(0, 0, 0, 0));
		fullScreenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((VideoFrame) VideoManager.getFrame()).setStreamPanelFullScreen();
			}
		});
		
		
		commandsPanel.add(pauseOrResumeStreamButton);
		commandsPanel.add(Box.createVerticalStrut(5));
		commandsPanel.add(shootPhotoButton);
		commandsPanel.add(Box.createVerticalStrut(5));
		commandsPanel.add(fullScreenButton);
		
		
		normalSizeScreenButton = new JButton("Back to normal size", normalSizeScreenIcon);
		//normalSizeScreenButton.setPreferredSize(new Dimension(buttonWidth,buttonHeight));
		//normalSizeScreenButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		normalSizeScreenButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		//normalSizeScreenButton.setMnemonic(KeyEvent.VK_N);
		normalSizeScreenButton.setMargin(new Insets(0, 0, 0, 0));
		
		normalSizeScreenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((VideoFrame) VideoManager.getFrame()).restoreNormalSizeFrame();
			}
		});
	}
	
	
	public void updateCurrentStreamInfo()	{
		String labelText = "<html>";
		
		if (! streamStatus.equals("No stream detected"))	{
			String productName = VideoDevicesManager.currentVideoDevice.productName;
			
			if (productName.length() > 28)
				productName = productName.substring(0,25) + "...";
			
			labelText += productName
			+	"<br>Resolution: "	+ VideoDeviceStreamAppletManager.currentStreamApplet.resolutionWidth	+	"x"
			+ 	VideoDeviceStreamAppletManager.currentStreamApplet.resolutionHeight
			+	"<br>";
		}
		
		labelText += "Stream status: " + streamStatus + "</html>";
		
		streamInfoLabel.setText(labelText);
	}
	
	
	public void initPleaseWaitLabel()	{
		pleaseWaitLabel = new JLabel ("");
		
		pleaseWaitLabel.setForeground(Color.white);
		pleaseWaitLabel.setVerticalTextPosition(JLabel.CENTER);
		pleaseWaitLabel.setHorizontalTextPosition(JLabel.CENTER);
		
		pleaseWaitLabel.setPreferredSize(new Dimension(standardWidth + standardHorMargin, standardHeight + standardVerMargin));
	}
	
	
	public void addApplet(VideoDeviceStreamApplet applet)	{
		pleaseWaitLabel.setVisible(false);
		
		streamStatus = "Playing";
		updateCurrentStreamInfo();
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		streamPanel.add(applet, c);
		
		//int width = applet.getWidth();
		//int height = applet.getHeight();
		
		//streamPanel.setMinimumSize(new Dimension(width, height));
		
		//System.out.println("width: " + width + ", height:" + height);
		
		enableInteraction(true);
	}
	
	
	public void removeApplet()	{
		streamPanel.remove(VideoDeviceStreamAppletManager.currentStreamApplet);
		pleaseWaitLabel.setText("<html>Stream interrupted</html>");
		pleaseWaitLabel.setVisible(true);
		
		streamStatus = "No stream detected";
		updateCurrentStreamInfo();
	}
	
	
	public void enableInteraction(boolean enabled)	{
		pauseOrResumeStreamButton.setEnabled(enabled);
		shootPhotoButton.setEnabled(enabled);
		// in realtà solo uno per volta dei due seguenti viene visualizzato
		fullScreenButton.setEnabled(enabled);
		normalSizeScreenButton.setEnabled(enabled);
	}
	
	
	public void showBlackPanel(int width, int height, boolean problem)	{
		
		if (problem == false)	{
			if (width > 0	&&	height > 0)	{
				
				streamStatus = "No stream detected";
				updateCurrentStreamInfo();
				
				pleaseWaitLabel.setText("<html><center>Please wait..."
						+	"<br><br>Checking device stream at " + width + "x" + height
						+	"</center></html>");
				
				pleaseWaitLabel.setMinimumSize(new Dimension(width, height));
			}
			else	{
				pleaseWaitLabel.setPreferredSize(new Dimension(standardWidth + standardHorMargin, standardHeight + standardVerMargin));
			}
		}
		else	{
			pleaseWaitLabel.setText("<html><center>Sorry, there was some problem" +
					"<br>getting a stream from the device" +
					"<br>at the selected resolution.</center></html>");
		}
		
		pleaseWaitLabel.setHorizontalAlignment(JLabel.CENTER);
		
		streamPanel.add(pleaseWaitLabel);
	}
	
	
	public void setFullScreenMode()	{
		enableInteraction(false);
		
		VideoDeviceStreamApplet applet = VideoDeviceStreamAppletManager.currentStreamApplet;
				
		//System.out.println("resw: " + resolutionWidth + "resh:" + resolutionHeight);
		//System.out.println("realw: " + applet.getWidth() + "realh:" + applet.getHeight());
		
		int resWidth = applet.resolutionWidth;
		int resHeight = applet.resolutionHeight;
		
		Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int appletHeight = (int)ssize.getHeight() - 200;
		int appletWidth = appletHeight * resWidth / resHeight;
		
		((VideoFrame) VideoManager.getFrame()).showVideoDeviceStream(VideoDevicesManager.currentVideoDevice.id, resWidth, resHeight, appletWidth, appletHeight);
		
		commandsPanel.remove(fullScreenButton);
		commandsPanel.add(normalSizeScreenButton);
		
		enableInteraction(true);
	}
	
	
	public void setNormalSizeScreenMode()	{
		VideoDeviceStreamApplet applet = VideoDeviceStreamAppletManager.currentStreamApplet;
		
		int resWidth = applet.resolutionWidth;
		int resHeight = applet.resolutionHeight;
		
		int appletHeight = ((VideoFrame) VideoManager.getFrame()).calculateHeightForApplet();
		int appletWidth = appletHeight * resWidth / resHeight;
		
		((VideoFrame) VideoManager.getFrame()).showVideoDeviceStream(VideoDevicesManager.currentVideoDevice.id, resWidth, resHeight, appletWidth, appletHeight);
		
		commandsPanel.remove(normalSizeScreenButton);
		commandsPanel.add(fullScreenButton);
	}
}