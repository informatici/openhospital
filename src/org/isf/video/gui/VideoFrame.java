package org.isf.video.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import org.isf.video.manager.AbstractVideoFrame;
import org.isf.video.manager.VideoDeviceStreamApplet;
import org.isf.video.manager.VideoDeviceStreamAppletManager;
import org.isf.video.manager.VideoDevicesManager;
import org.isf.video.manager.VideoManager;
import org.isf.video.model.VideoDevice;

public class VideoFrame extends AbstractVideoFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MiddlePanel middlePanel;
	private VideoDevicesPanel devicesPanel;
	private BottomPanel bottomPanel;
	
	public String selectedPhotoFileName = "";
	
	public FullScreenStreamFrame fullScreenStreamFrame;
	
	public boolean fullScreenMode = false;
	
	public boolean ignoreNonUserEvents = false;
	
	private boolean dontclose = false;
	
	public VideoFrame(JDialog owner) {
		super(owner, true);
		setTitle("Open-take-a-picture");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (dontclose == false)	{
					closeFrame();
				}
			}
		});
		
		devicesPanel = new VideoDevicesPanel();
		middlePanel = new MiddlePanel();
		bottomPanel = new BottomPanel();
		
		middlePanel.showBlackPanel(-1, -1, false);
		
		boolean deviceSelected = true;
		enableInteraction(false, deviceSelected);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(devicesPanel, BorderLayout.WEST);
		contentPane.add(middlePanel, BorderLayout.CENTER);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		adjustSizeAndPosition();
		
		addComponentListener(new java.awt.event.ComponentAdapter()	{
			public void componentResized(ComponentEvent e)	{
				int width = ((JDialog)e.getSource()).getWidth();
	        	
	    		bottomPanel.setSize(width);
			}
		});
		
		VideoManager.init(this);
	}
	
	
	public void init(final boolean update_device_list)	{
		
		selectedPhotoFileName = "";
		
		boolean deviceSelected = true;
		enableInteraction(false, deviceSelected);
		
		setVisible(true);
		
		new Thread (new Runnable() {
			public void run() {
				
				if (update_device_list)
					VideoManager.updateDeviceList();
				
				final int videoDevicesCount = VideoManager.getVideoDevicesCount();
				
				EventQueue.invokeLater (new Runnable()	{
					public void run() {
						
						if (videoDevicesCount > 0)
						{
							VideoDevice firstVideoDevice = VideoManager.getFirstVideoDevice();
							
							int videoDeviceId = firstVideoDevice.id;
							
							int resCount = firstVideoDevice.getAvailableResolutionsCount();
							
							if (resCount > 0)
							{
								int resIndex = firstVideoDevice.getDefaultResolutionIndex();
								
								// no default resolution found: select the first one
								if (resIndex == -1)
								{
									System.out.println("No default resolution found.");
									resIndex = 0;
								}
								
								int selectedWidth = firstVideoDevice.getResolutionWidth(resIndex);
								int selectedHeight = firstVideoDevice.getResolutionHeight(resIndex);
								
								updateGui(firstVideoDevice, resIndex);
								
								int appletHeight = calculateHeightForApplet();
								//int appletHeight = 350;
								int appletWidth = appletHeight * selectedWidth / selectedHeight;
								
								showVideoDeviceStream (videoDeviceId, selectedWidth, selectedHeight, appletWidth, appletHeight);
							}
							else
							{
								System.out.println("No resolution was found for current device.");
							}
						}
						else	{
							System.out.println("No videoDevice was found among the connected devices.");
						}
					}
				});
			}
		}).start();
	}
	
	
	public int calculateHeightForApplet()	{
		Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int sheight = (int)ssize.getHeight();
		int botpanheight = bottomPanel.getHeight();
		
		// screen height - (low panel height + buttons space and label in middlepanel) - extra space
		int appletHeight = sheight - (botpanheight + 120) - 100;
		//System.out.println("appletHeight : "+ appletHeight);
		return appletHeight;
	}
	
	
	public void adjustSizeAndPosition() {
		pack();
		
		bottomPanel.setSize(getWidth());
		
		adjustPosition();
	}

	
	public void adjustPosition()	{
		pack();
		
		Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int x = (int) (ssize.getWidth() - getWidth()) / 2;
		int y = (int) (ssize.getHeight() - getHeight()) / 2;
		setLocation(x, y);
	}
	
	
	public void addDevice(VideoDevice videoDevice) {
		devicesPanel.addDevice(videoDevice);
		
		pack();
	}
	
	
	public void removeDevice(VideoDevice videoDevice)	{
		devicesPanel.removeDevice(videoDevice);
		removeAllResolutions();
		pack();
	}
	
	
	public void removeApplet()	{
		updateStatus(VideoDevicesManager.idCurrentVideoDevice, "stop", -1, -1, -1, -1);
		
		middlePanel.removeApplet();
	}
	
	
	public void addApplet(int id, VideoDeviceStreamApplet applet)	{
		//adjustSizeAndPosition(applet.width + standardHorMargin, applet.height + standardVerMargin);
		
		updateStatus(id, "play", -1, -1, -1, -1);
		
		middlePanel.addApplet(applet);
		
		int minWidth = devicesPanel.getWidth() + applet.appletWidth + 50;
		int minHeight =  bottomPanel.getHeight() + /*applet.appletHeight*/ middlePanel.getHeight() + 70;
		
		Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
		
		if (minWidth > ssize.getWidth())
			minWidth = (int)ssize.getWidth();
		
		if (minHeight > ssize.getHeight())
			minHeight = (int)ssize.getHeight() - 50;
		
		//System.out.println("Frame minimum size: " + minWidth + "x" + minHeight);
		setMinimumSize(new Dimension (minWidth, minHeight));
		setPreferredSize(new Dimension (minWidth, minHeight));
	}
	
	
	// creates an applet with the given id and dimensions 
	public void showVideoDeviceStream(final int id, final int resolutionWidth, final int resolutionHeight,
										final int appletWidth, final int appletHeight) {
		
		if (id >= 0) {
			/*
			 * If another videoDevice stream (or the stream of the selected videoDevice at a different resolution)
			 * is currently being shown, then its applet have to be removed.
			 */
			if ((VideoDeviceStreamAppletManager.currentStreamApplet != null) && (VideoDevicesManager.currentVideoDevice != null))	{
				removeApplet();
			}
			
			updateStatus(id, "wait", resolutionWidth, resolutionHeight, appletWidth, appletHeight);
			adjustPosition();
			
			/* Switches the two applets in a different thread */
			new Thread (new Runnable() {
				public void run() {
					//System.out.println("resw: " + resolutionWidth + "resh:" + resolutionHeight);
					//System.out.println("realw: " + appletWidth + "realh:" + appletHeight);
					
					boolean deviceSelected = true;
					enableInteraction(false, deviceSelected);
					
					VideoDevice device = VideoManager.getVideoDevice(id);
					
					final boolean switchOk = VideoDeviceStreamAppletManager
								.switchApplets(device, resolutionWidth, resolutionHeight, appletWidth, appletHeight);
					
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							if (switchOk)	{						
								addApplet(id, VideoDeviceStreamAppletManager.currentStreamApplet);
								
								updateCurrentStreamInfo();
							}
							else	{
								// shows error message in middlePanel
								middlePanel.showBlackPanel(-1, -1, true);
							}
							
							boolean deviceSelected = true;
							enableInteraction(true, deviceSelected);
						}
					});
				}
			}).start();
		}
	}
	
	
	public void updateGui(VideoDevice videoDevice, int resId)	{
		System.out.println("Updating GUI... (device id: " + videoDevice.id + ")");
		ignoreNonUserEvents = true;
		devicesPanel.selectDevice(videoDevice.id);
		
		ignoreNonUserEvents = true;
		selectResolution(resId);
		
		ignoreNonUserEvents = false;
	}


	public void selectResolution(int index)	{
		devicesPanel.selectResolution(index);
	}
	
	
	public int getSelectedVideoDeviceId()	{
		return devicesPanel.idSelectedRadio;
	}
	
	
	public String getSelectedResolution()	{
		return devicesPanel.selectedResolution;
	}
	
	
	public void checkResolutions(final VideoDevice videoDevice, final boolean ignoreFile)	{
		boolean deviceSelected = true;
		enableInteraction(false, deviceSelected);
		
		final JDialog waitForResolutionCheckDialog = new JDialog();
		waitForResolutionCheckDialog.setResizable(false);
		waitForResolutionCheckDialog.setMaximumSize(new Dimension(310,160));
		
		Box resCheckBox = Box.createVerticalBox();
		resCheckBox.setPreferredSize(new Dimension(300, 150));
		
		String product = "";
		
		if (videoDevice.vendorName != null)	{
			product = videoDevice.vendorName;
		}
		
		if (videoDevice.productName != null) {
			if (! product.equals(""))	{
				product +=  " ";
			}
			
			product += videoDevice.productName;
		}
		else if (videoDevice.productDescription != null)	{
			if (! product.equals(""))	{
				product +=  " ";
			}
			
			product += videoDevice.productDescription;
		}
		
		JLabel resCheckLabel = new JLabel("<html><center>" +
				"Getting resolutions for <br>" + product + 
				"<br><br>Please wait...</center></html>");
		
		resCheckLabel.setPreferredSize(new Dimension(300, 150));
		resCheckLabel.setMaximumSize(new Dimension(300, 150));
		resCheckLabel.setHorizontalAlignment(JLabel.CENTER);
		resCheckLabel.setVerticalAlignment(JLabel.CENTER);
		resCheckBox.add(resCheckLabel);
		
		waitForResolutionCheckDialog.getContentPane().add(resCheckBox);
		waitForResolutionCheckDialog.pack();
		
		Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int x = (int) (ssize.getWidth() - waitForResolutionCheckDialog.getWidth()) / 2;
		int y = (int) (ssize.getHeight() - waitForResolutionCheckDialog.getHeight()) / 2;
		
		waitForResolutionCheckDialog.setLocation(x, y);
		waitForResolutionCheckDialog.setVisible(true);
		waitForResolutionCheckDialog.setAlwaysOnTop(true);
		waitForResolutionCheckDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		new Thread (new Runnable() {
			
			public void run() {
				dontclose = true;
				setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				
				System.out.println("Start checking resolutions...");
				videoDevice.checkResolutions(ignoreFile);
				
				try {
					Thread.sleep(700);
				}
				catch(InterruptedException ie)	{
					System.out.println(ie);
				}
				waitForResolutionCheckDialog.setVisible(false);
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
						boolean deviceSelected = false;
						enableInteraction(true, deviceSelected);
						dontclose = false;
					}
				});
			}
		}).start();
	}
	
	
	public void removeAllResolutions()	{
		devicesPanel.removeAllResolutions();
	}
	
	
	public String savePhoto(String filename)	{
		
		String generatedFileName = VideoManager.savePhoto(
			VideoDeviceStreamAppletManager.currentStreamApplet, filename);
		
		if (generatedFileName != "")
		{
			String tempDir = VideoManager.shotPhotosTempDir;
			String extension = VideoManager.shotPhotosExtension;
		
			String path = tempDir + generatedFileName + extension; 
			
			bottomPanel.addPreview(path);
			
			pack();
		}
		
		return generatedFileName;
	}
	
	
	public JPanel getFilenameInputPanel()	{
		JPanel panel = new JPanel();
		
		JTextField txtFilename = new JTextField(30);
		JButton closeButton = new JButton("Confirm");
		
		panel.add(txtFilename);
		panel.add(closeButton);
		
		return panel;
	}
	
	
	public void updateStatus(int id, String status, int resWidth, int resHeight, int appletWidth, int appletHeight)	{
		if (status.equals("wait"))	{
			adjustSizeAndPosition();
			
			int minWidth = devicesPanel.getWidth() + appletWidth + 80;
			int minHeight =  bottomPanel.getHeight() + appletHeight + 120;
			
			Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
			
			if (minWidth > ssize.getWidth())
				minWidth = (int)ssize.getWidth();
			
			if (minHeight > ssize.getHeight())
				minHeight = (int)ssize.getHeight() - 50;
			
			//System.out.println("width: " + devicesPanel.getWidth() + " + " + appletWidth);
			//System.out.println("height: " + bottomPanel.getHeight() + " + " + appletHeight);
			
			//System.out.println("Frame minimum size: " + minWidth + "x" + minHeight);
			
			setMinimumSize(new Dimension (minWidth, minHeight));
			setPreferredSize(new Dimension (minWidth, minHeight));
			
			middlePanel.showBlackPanel(resWidth, resHeight, false);
		}
		
		devicesPanel.updateDeviceStatus(id, status);
	}
	
	
	public void updateCurrentStreamInfo()	{
		middlePanel.updateCurrentStreamInfo();
	}
	
	
	public void enableInteraction(boolean enabled, boolean deviceSelected) {
		devicesPanel.enableInteraction(enabled, deviceSelected);
		
		if(VideoDeviceStreamAppletManager.currentStreamApplet != null && enabled == true)
			middlePanel.enableInteraction(true);
		else
			middlePanel.enableInteraction(false);
	}

	
	public void setStreamPanelFullScreen()	{
		setVisible(false);
		
		fullScreenStreamFrame = new FullScreenStreamFrame();
		fullScreenStreamFrame.setVisible(false);
		
		fullScreenStreamFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				restoreNormalSizeFrame();
			}
		});
		
		fullScreenStreamFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");

		fullScreenStreamFrame.getRootPane().getActionMap().put("Cancel", new AbstractAction()	{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)	{
				restoreNormalSizeFrame();
			}
		});
		
		fullScreenStreamFrame.showFrame(middlePanel);
		
		fullScreenMode = true;
	}
	
	
	public void restoreNormalSizeFrame()	{
		fullScreenStreamFrame.hideFrame();
		fullScreenStreamFrame = null;
		
		middlePanel.setNormalSizeScreenMode();
		
		getContentPane().add(middlePanel, BorderLayout.CENTER);
		
		adjustSizeAndPosition();
		setVisible(true);
		fullScreenMode = false;
	}
	
	
	public void resetAll()	{
		if (VideoDeviceStreamAppletManager.currentStreamApplet != null
			&&	VideoDeviceStreamAppletManager.currentStreamApplet.isNowWorking())	{
			
			VideoDeviceStreamAppletManager.currentStreamApplet.freeze();
			
			removeApplet();
		}
		
		bottomPanel.reset();
		devicesPanel.reset();
		
		VideoManager.reset(true);
	}
	
	public void closeFrame() {
		resetAll();
		
		setVisible(false);
	}
}
