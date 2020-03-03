package org.isf.video.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PhotoPreviewPanel extends JPanel	{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image img;
	
	public PhotoPreviewPanel(String path)	{
		try	{
			img = ImageIO.read(new File(path));
		}
		catch (IOException ioe)	{
			System.out.println("Path: " + path);
			ioe.printStackTrace();
		}
		
		setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
	}
	
	//override paint method of panel
	public void paint(Graphics g)	{
		//draw the image
		if(img != null)	{
			g.drawImage(img, 0, 0, this);
		}
	}
}
