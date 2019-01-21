package org.isf.video.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.isf.video.manager.VideoDeviceStreamApplet;

class PhotoPanel extends JPanel {

	private static final long serialVersionUID = 7684416938326266810L;
	
	public Image img;
	public VideoDeviceStreamApplet applet;
	private Dimension dimension;

	public PhotoPanel(String img) {
		setLayout(null);
		updatePhoto(new ImageIcon(img).getImage());
	}

	public PhotoPanel(Image img) {
		setLayout(null);
		updatePhoto(img);
	}

	private void refreshPanel(Dimension dimension) {
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		setSize(dimension);

		repaint();
	}
	
	public void addApplet (VideoDeviceStreamApplet applet) {
		this.applet = applet;
		this.img = null;
		
		Dimension dimension = new Dimension(applet.appletWidth, applet.appletHeight);
		refreshPanel(dimension);
		add(applet);
	}
	
	public void removeApplet () {
		if (this.applet != null)
		{
			remove (this.applet);
			repaint();
		}
	}
	
	public void updatePhoto(Image img) {
		
		removeApplet();
		
		this.img = img;
		
		if (this.img != null)	{
			dimension = new Dimension(img.getWidth(null), img.getHeight(null));
			refreshPanel(dimension);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = (getWidth() - dimension.width) / 2;
		int y = (getHeight() - dimension.height) / 2;
		if (img != null) {
			g.drawImage(img, x, y, null);
		}
	}	
}