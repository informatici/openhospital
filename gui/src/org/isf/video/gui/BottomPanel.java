package org.isf.video.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.isf.video.manager.VideoDeviceStreamAppletManager;
import org.isf.video.manager.VideoDevicesManager;
import org.isf.video.manager.VideoManager;

public class BottomPanel extends JPanel {

	private static final long serialVersionUID = -4819764414668616333L;

	private JPanel previewsBox;
	private JPanel buttonsPanel;
	
	private JButton enlargePhotoButton;
	private JButton deletePhotoButton;
	private JButton importPhotoButton;
	
	private int photoCount = 0;
	
	private JScrollPane scroll;
	
	private PhotoPreviewBox selectedPreview = null;
	
	//private static ArrayList<JRadioButton> lstPhotoFramesRadio = new ArrayList<JRadioButton>();
	private static ArrayList<PhotoPreviewBox> lstPhotoPreviewBoxes = new ArrayList<PhotoPreviewBox>();
	
	public BottomPanel ()	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setPreferredSize(new Dimension((int)getPreferredSize().getWidth(), 160));
		
		//PhotoFramesBox = Box.createHorizontalBox();
		previewsBox = new JPanel();
		previewsBox.setLayout(new GridLayout(1,6,10,10));
		
		scroll = new JScrollPane(previewsBox);
		scroll.setBorder(null);
		
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder title = BorderFactory.createTitledBorder(raisedetched, "Photos taken");
		title.setTitleJustification(TitledBorder.LEFT);
		setBorder(title);
		
		add(scroll);
		
		initButtonsPanel();
		add(buttonsPanel);
	}
	
	
	private void initButtonsPanel()	{
		buttonsPanel = new JPanel();
		
		enlargePhotoButton = new JButton("Enlarge", new ImageIcon("rsc/icons/showphoto_button.png"));
		//enlargePhotoButton.setPreferredSize(new Dimension(45, 40));
		buttonsPanel.add(enlargePhotoButton);
		
		enlargePhotoButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				
				String path = selectedPreview.path;
				String device = selectedPreview.device;
				
				if (device.length() > 31)	{
					device = device.substring(0, 30) + "...";
				}
				
				String resolution = selectedPreview.resolution;
				
				PhotoFrame photoFrame = new PhotoFrame(path, device, resolution);
				photoFrame.setResizable(false);
				photoFrame.setMaximumSize(photoFrame.getPreferredSize());
				
				Dimension ssize = Toolkit.getDefaultToolkit().getScreenSize();		
				
				int x = (int) (ssize.getWidth() - photoFrame.getWidth()) / 2;
				int y = (int) (ssize.getHeight() - photoFrame.getHeight()) / 2;
				
				photoFrame.setLocation(x, y);
				photoFrame.setVisible(true);
			}
		});
		
		
		deletePhotoButton = new JButton("Remove", new ImageIcon("rsc/icons/trash_button.png"));
		//deletePhotoButton.setPreferredSize(new Dimension(45, 40));
		buttonsPanel.add(deletePhotoButton);
		
		deletePhotoButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				
				int n = JOptionPane.showConfirmDialog(
					    null,
					    "Do you really want to delete this photo?",
					    "Please confirm",
					    JOptionPane.YES_NO_OPTION);
				
				if (n == JOptionPane.YES_OPTION)
				{
					//selectedPreview.setVisible(false);
					removePreview();
				}
			}
		});	
		
		importPhotoButton = new JButton("Add to OpenHospital", new ImageIcon("rsc/icons/plus_button.png"));
		importPhotoButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				((VideoFrame) VideoManager.getFrame()).selectedPhotoFileName = selectedPreview.path;
				((VideoFrame) VideoManager.getFrame()).closeFrame();
			}
		});
		
		enlargePhotoButton.setEnabled(false);
		deletePhotoButton.setEnabled(false);
		importPhotoButton.setEnabled(false);
		
		buttonsPanel.add(importPhotoButton);
	}
	
	
	public void setSize (int width)	{
		//this.setPreferredSize(new Dimension(width, 195));
		//System.out.println(width);
		
		//remove(scroll);
		scroll.setPreferredSize(new Dimension(width-40, 110));
		//add(scroll);
	}
	
	
	public void addPreview(final String path)	{
		String currentResolution = VideoDeviceStreamAppletManager.currentStreamApplet.resolutionWidth
						+	"x"	+ VideoDeviceStreamAppletManager.currentStreamApplet.resolutionHeight;
		
		final PhotoPreviewBox box = new PhotoPreviewBox(path, 
												VideoDevicesManager.currentVideoDevice.productName,
												currentResolution);
		
		box.photoButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton)e.getSource();
				
				if (b.getBackground() == Color.white)	{
					if (selectedPreview != null)
						selectedPreview.photoButton.setBackground(Color.white);
					
					b.setBackground(Color.green);
					enlargePhotoButton.setEnabled(true);
					deletePhotoButton.setEnabled(true);
					importPhotoButton.setEnabled(true);
					
					selectedPreview = box;
				}
				else	{
					b.setBackground(Color.white);
					enlargePhotoButton.setEnabled(false);
					deletePhotoButton.setEnabled(false);
					importPhotoButton.setEnabled(false);
				}
			}
		});
		
		previewsBox.removeAll();
		
		for (PhotoPreviewBox b : lstPhotoPreviewBoxes)	{
			
			previewsBox.add(b);
		}
		
		lstPhotoPreviewBoxes.add(box);
		previewsBox.add(box);
		
		photoCount++;
		
		for (int i=0; i< 7-photoCount-1; i++)	{
			previewsBox.add(new JPanel());
		}
		
		previewsBox.repaint();
		previewsBox.revalidate();
	}
	
	
	private void removePreview()	{
		previewsBox.remove(selectedPreview);
		previewsBox.repaint();
		previewsBox.revalidate();
		
		lstPhotoPreviewBoxes.remove(selectedPreview);
		
		enlargePhotoButton.setEnabled(false);
		deletePhotoButton.setEnabled(false);
		importPhotoButton.setEnabled(false);
		
		if (photoCount < 7)
			previewsBox.add(new JPanel());
		
		photoCount--;
	}
	
	public void reset() {
		lstPhotoPreviewBoxes.clear();
		previewsBox.removeAll();
	}
}