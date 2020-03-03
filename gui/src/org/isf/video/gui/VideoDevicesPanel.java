package org.isf.video.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.isf.generaldata.MessageBundle;
import org.isf.video.manager.VideoManager;
import org.isf.video.model.VideoDevice;

class SingleDevicePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JRadioButton videoDeviceRadioButton;
	public JLabel videoDeviceStatusIcon;
	public JLabel videoDeviceInfoIcon;
	
	SingleDevicePanel(JRadioButton radio, JLabel statusIcon, JLabel infoIcon)	{
		videoDeviceRadioButton = radio;
		videoDeviceStatusIcon = statusIcon;
		videoDeviceInfoIcon = infoIcon;
	}
}


public class VideoDevicesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Box deviceBox;
	
	private JPanel resolutionPanel;
	private JPanel resolutionComboBoxPanel;
	private JPanel rescanResolutionsPanel;
	
	public String selectedResolution;
	
	public int idSelectedRadio = -1;
	private JComboBox resolutionsComboBox;
	private JLabel lblResolution;
	private JButton btnRescanResolutions;
	
	private Hashtable<Integer,SingleDevicePanel> devicePanelTable = new Hashtable<Integer,SingleDevicePanel>();
	
	private ImageIcon playStatusIcon = new ImageIcon("rsc/icons/greenlight_label.png");
	private ImageIcon waitStatusIcon = new ImageIcon("rsc/icons/yellowlight_label.png");
	private ImageIcon stopStatusIcon = new ImageIcon("rsc/icons/redlight_label.png");
	private ImageIcon deviceInfoIcon = new ImageIcon("rsc/icons/info_button.png");
		
	private JFrame infoFrame;
	
	public VideoDevicesPanel() {
		TitledBorder titleVideoDevice;
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		titleVideoDevice = BorderFactory.createTitledBorder(raisedetched, MessageBundle.getMessage("angal.video.devices"));
		titleVideoDevice.setTitleJustification(TitledBorder.LEFT);
		setBorder(titleVideoDevice);
		
		setPreferredSize(new Dimension(250, (int)this.getPreferredSize().getHeight()));
		setLayout(new BorderLayout());
		
		deviceBox = Box.createVerticalBox();
				
		initResolutionPanel();
		
		add(deviceBox, BorderLayout.NORTH);		
		add(resolutionPanel, BorderLayout.SOUTH);
	}
	
	
	private void initResolutionPanel()	{
		
		resolutionPanel = new JPanel();
		resolutionPanel.setLayout(new BoxLayout(resolutionPanel, BoxLayout.Y_AXIS));
		
		resolutionComboBoxPanel = new JPanel();
		resolutionComboBoxPanel.setLayout(new BoxLayout(resolutionComboBoxPanel, BoxLayout.X_AXIS));
		
		lblResolution = new JLabel(MessageBundle.getMessage("angal.video.resolution") + ":");
		
		resolutionsComboBox = new JComboBox();
		
		resolutionsComboBox.setMaximumSize(new Dimension(150,30));
		
		resolutionsComboBox.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ie) {
				
				if ((((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents == false)	&&	(idSelectedRadio >= 0))	{
					((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents = true;
					
					selectedResolution = (String)((JComboBox)ie.getSource()).getSelectedItem();
					
					if (selectedResolution != null	&&	selectedResolution != "")	{
						//System.out.println("[resolutionComboBox] Debug: Selecting resolution from combobox, idradio: " + idSelectedRadio);
						
						String [] dimensions = selectedResolution.split("x");
						
						int resWidth = Integer.parseInt(dimensions[0]);
						int resHeight = Integer.parseInt(dimensions[1]);
						
						if (resWidth > 0	&&	resHeight > 0)	{
							int appletHeight = ((VideoFrame) VideoManager.getFrame()).calculateHeightForApplet();
							int appletWidth = appletHeight * resWidth / resHeight;
							
							VideoManager.setCurrentResolutionAsDefault(resWidth, resHeight, 
									VideoManager.getVideoDevice(idSelectedRadio).deviceIdentificationString);
							
							((VideoFrame) VideoManager.getFrame()).showVideoDeviceStream(idSelectedRadio, resWidth, resHeight,
																		appletWidth, appletHeight);
						}
					}
					
					((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents = false;
				}
			}
		});
		
		
		rescanResolutionsPanel = new JPanel();
		btnRescanResolutions = new JButton(MessageBundle.getMessage("angal.video.rescanresolutions"));
		rescanResolutionsPanel.add(btnRescanResolutions);
		rescanResolutionsPanel.setMinimumSize(new Dimension(180, (int)rescanResolutionsPanel.getPreferredSize().getHeight()));
		
		btnRescanResolutions.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ie) {
	        	boolean ignoreFile = true;
	        	
	        	((VideoFrame) VideoManager.getFrame()).checkResolutions(VideoManager.getVideoDevice(idSelectedRadio), ignoreFile);
	        }
		});
		
		
		resolutionComboBoxPanel.add(lblResolution);
		resolutionComboBoxPanel.add(Box.createHorizontalStrut(5));
		resolutionComboBoxPanel.add(resolutionsComboBox);
		resolutionComboBoxPanel.add(Box.createHorizontalStrut(5));
		resolutionPanel.add(resolutionComboBoxPanel);
		resolutionPanel.add(Box.createVerticalStrut(5));
		resolutionPanel.add(rescanResolutionsPanel);
	}
	
	
	public void addDevice(VideoDevice videoDevice) {
		final int id = videoDevice.id;
		
		//lstDevicesId.add(id);
		
		String product = "";
		
		if (videoDevice.productName != null)	{
			String productName = videoDevice.productName;
			
			if (productName.length() > 31)	{
				productName = productName.substring(0, 30) + "...";
			}
			
			product = productName;
		}
		else if (videoDevice.vendorName != null)	{
			String vendorName = videoDevice.vendorName;
			
			if (vendorName.length() > 31)	{
				vendorName = vendorName.substring(0, 30) + "...";
			}
			
			product = vendorName;
		}
		else if (videoDevice.productDescription != null)	{
			String productDescription = videoDevice.productDescription;
			
			if (productDescription.length() > 31)	{
				productDescription = productDescription.substring(0, 30) + "...";
			}
			
			product = productDescription;
		}
		
		//if (product() > 20)
		//	product = product.substring(0,17) + "...";
		
		String label =	"<html>"
				//	+		"<br>"
					+		product
					+	"</html>";
		
		JRadioButton deviceRadio = new JRadioButton(label);
		
		//lstVideoDeviceRadioButtons.add(deviceRadio);
		
		deviceRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)	{  
				
				if (((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents == false)	{
					((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents = true;
					
					System.out.println("[deviceRadio] Debug: Clicked radio button, id: " + id);
					
					selectDevice(id);
					
					selectedResolution = (String)resolutionsComboBox.getSelectedItem();
					
					if (selectedResolution != null	&&	selectedResolution != "")	{
						//System.out.println("[deviceRadio] Debug: Going to show videoDevice stream, device id: " + lstDevicesId.get(id));
						
						String [] dimensions = selectedResolution.split("x");
						
						int resWidth = Integer.parseInt(dimensions[0]);
						int resHeight = Integer.parseInt(dimensions[1]);
						
						int appletHeight = ((VideoFrame) VideoManager.getFrame()).calculateHeightForApplet();
						int appletWidth = appletHeight * resWidth / resHeight;
						
						((VideoFrame) VideoManager.getFrame()).showVideoDeviceStream(id, resWidth, resHeight, appletWidth, appletHeight);
					}
					
					((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents = false;
				}
			}
		});
		
		//JLabel deviceLabel = new JLabel(label);
		//lstVideoDeviceLabels.add(deviceLabel);
		
		JLabel statusLabel = new JLabel();
		statusLabel.setIcon(stopStatusIcon);
		//lstVideoDeviceStatusIcon.add(statusLabel);
		
		JLabel infoLabel = new JLabel();
		infoLabel.setIcon(deviceInfoIcon);
		infoLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me)	{  
				Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();
				
				infoFrame = new JFrame("Device info");
				infoFrame.setResizable(false);
				infoFrame.setMaximumSize(infoFrame.getPreferredSize());
				infoFrame.add(getDeviceInfoPanel(id));
				infoFrame.pack();
				
				int x = (int) (ssize.getWidth() - infoFrame.getWidth()) / 2;
				int y = (int) (ssize.getHeight() - infoFrame.getHeight()) / 2;
				
				infoFrame.setLocation(x, y);
				infoFrame.setVisible(true);
			}
		});
		//lstVideoDeviceInfoIcon.add(infoLabel);
		
		SingleDevicePanel horizontalPanel = new SingleDevicePanel(deviceRadio, statusLabel, infoLabel);
		
		devicePanelTable.put(id, horizontalPanel);
		
		horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
		horizontalPanel.add(statusLabel);
		horizontalPanel.add(Box.createHorizontalStrut(5));
		horizontalPanel.add(deviceRadio);
		horizontalPanel.add(Box.createHorizontalStrut(5));
		horizontalPanel.add(infoLabel);
		
		deviceBox.add(Box.createVerticalStrut(10));
		deviceBox.add(horizontalPanel);
	}
	
	
	public void selectResolution(int index)	{
		resolutionsComboBox.setSelectedIndex(index);
	}
	
	// TODO: verify if exists a device with the specified ID
	public void selectDevice(int id)	{
		System.out.println("Selected device " + id + "...");
		
		if (idSelectedRadio >= 0)	{
			devicePanelTable.get(idSelectedRadio).videoDeviceRadioButton.setSelected(false);
		}
		
		devicePanelTable.get(id).videoDeviceRadioButton.setSelected(true);
		
		idSelectedRadio = id;
		
		loadVideoDeviceResolutions(VideoManager.getVideoDevice(id));
	}
	
	
	public void removeAllResolutions()	{
		resolutionsComboBox.removeAllItems();
		System.out.println("Cleared resolutionsComboBox");
		btnRescanResolutions.setEnabled(false);
	}
	
	
	public void loadVideoDeviceResolutions(VideoDevice videoDevice)	{
		removeAllResolutions();
		
		System.out.print("Loading resolution for selected videoDevice (id: " + videoDevice.id + "), found: ");
		int nres = videoDevice.getAvailableResolutionsCount();
		
		if (nres > 0)	{
			for (int i = 0; i < nres; i++)	{
				String curRes = videoDevice.getResolutionWidth(i) + "x" + videoDevice.getResolutionHeight(i);
				
				resolutionsComboBox.addItem(curRes);
				
				System.out.print(curRes + ",");
			}
			System.out.println();
		}
		
		if (resolutionsComboBox.getItemCount() > 0)
		{
			((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents = true;
			resolutionsComboBox.setSelectedIndex(0);
		}
		
		((VideoFrame) VideoManager.getFrame()).ignoreNonUserEvents = false;
	}
	
	
	public void updateDeviceStatus(int id, String status)	{
		//System.out.println("# " + id + " * " + status + " #");
		
		ImageIcon statusIcon;
		
		if (status.contains("play"))	{
			statusIcon = playStatusIcon;
		}
		else if (status.contains("wait"))	{
			statusIcon = waitStatusIcon;
		}
		else {
			statusIcon = stopStatusIcon;
		}
				
		devicePanelTable.get(id).videoDeviceStatusIcon.setIcon(statusIcon);
	}
	
	
	public void enableInteraction(boolean enabled, boolean deviceSelected)	{
		for (SingleDevicePanel panel : devicePanelTable.values())	{
			panel.videoDeviceRadioButton.setEnabled(enabled);
		}
		
		resolutionsComboBox.setEnabled(deviceSelected);
		btnRescanResolutions.setEnabled(deviceSelected);
	}
	

	public JPanel getDeviceInfoPanel(int id)	{
		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		
		VideoDevice videoDevice = VideoManager.getVideoDevice(id);
		
		if (videoDevice != null)	{
			String info = "<html><pre>";
			
			if ((videoDevice.productName != null) && (! videoDevice.productName.trim().equals("")))	{
				String productName = videoDevice.productName;
				
				if (productName.length() > 31)	{
					productName = productName.substring(0, 30) + "...";
				}
				info += "Product:\t" + productName	+	"<br><br>";
			}
			
			if ((videoDevice.productDescription != null) && (! videoDevice.productDescription.trim().equals("")))	{
				String productDescription = videoDevice.productDescription;
				
				if (productDescription.length() > 31)	{
					productDescription = productDescription.substring(0, 30) + "...";
				}
				info += "Description:\t"	+	productDescription	+	"<br><br>";
			}
			
			if ((videoDevice.vendorName != null) && (! videoDevice.vendorName.trim().equals("")))	{
				String vendorName = videoDevice.vendorName;
				
				if (vendorName.length() > 31)	{
					vendorName = vendorName.substring(0, 30) + "...";
				}
				
				info += "Vendor:\t\t"	+	vendorName;
			}
			
			info += "</pre></html>";
			
			JLabel infoLabel = new JLabel(info);
			JButton closePopupButton = new JButton("close");
			
			JPanel closePopupButtonPanel = new JPanel();
			closePopupButton.addActionListener(new ActionListener()	{
				public void actionPerformed(ActionEvent e) {
					
					infoFrame.setVisible(false);
				}
			});
			closePopupButton.setPreferredSize(new Dimension(100, (int)closePopupButton.getPreferredSize().getHeight()));
			closePopupButton.setMaximumSize(new Dimension(100, (int)closePopupButton.getPreferredSize().getHeight()));
			
			closePopupButtonPanel.add(closePopupButton);
			
			panel.add(infoLabel, BorderLayout.NORTH);
			panel.add(closePopupButtonPanel, BorderLayout.SOUTH);
		}
		
		return panel;
	}
	
	
	public void removeDevice(VideoDevice videoDevice)	{
		int index = videoDevice.id;
		
		if (index == idSelectedRadio){
			((VideoFrame) VideoManager.getFrame()).removeApplet();
			idSelectedRadio = -1;
		}
		
		SingleDevicePanel panel = devicePanelTable.get(index);
		panel.videoDeviceRadioButton.setSelected(false);
		
		panel.remove(panel.videoDeviceRadioButton);
		panel.remove(panel.videoDeviceInfoIcon);
		panel.remove(panel.videoDeviceStatusIcon);
		remove(panel);
		
		System.out.println("idSelectedRadio = " + idSelectedRadio);
		
		if (idSelectedRadio == -1)	{
			btnRescanResolutions.setEnabled(false);
		}
	}
	
	
	public void reset()	{
		for (SingleDevicePanel panel : devicePanelTable.values()){
			panel.videoDeviceRadioButton.setSelected(false);
			
			panel.remove(panel.videoDeviceRadioButton);
			panel.remove(panel.videoDeviceInfoIcon);
			panel.remove(panel.videoDeviceStatusIcon);
			remove(panel);
		}
		
		idSelectedRadio = -1;
	}
}
