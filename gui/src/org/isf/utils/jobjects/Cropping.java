package org.isf.utils.jobjects;
/**
 * Cropping.java - 27/gen/2014
 */

/**
 * @author Internet, Mwithi
 *
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

public class Cropping extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BufferedImage image;
	Dimension size;
	Rectangle clip;
	BufferedImage clipped;

	public Cropping(BufferedImage image) {
		this.image = image;
		size = new Dimension(image.getWidth(), image.getHeight());
		ClipMoverAndResizer mover = new ClipMoverAndResizer(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int x = 0;//(getWidth() - size.width) / 2;
		int y = 0;//(getHeight() - size.height) / 2;
		g2.drawImage(image, x, y, this);
		if (clip == null)
			createClip();
		g2.setPaint(Color.red);
		g2.draw(clip);
	}

	public void setClip(int x, int y) {
		// keep clip within raster
		int x0 = (getWidth() - size.width) / 2;
		int y0 = (getHeight() - size.height) / 2;
		if (x < x0 || x + clip.width > x0 + size.width || y < y0 || y + clip.height > y0 + size.height)
			return;
		clip.setLocation(x, y);
		repaint();
	}
	
	public void resizeClip(int x, int y) {
		// keep clip within raster
		int x0 = 100;
		int y0 = 100;
		if (x < x0 || x > size.width || y < y0 || y > size.height)
			return;
		clip.setSize(x, y);
		repaint();
	}

	public Dimension getPreferredSize() {
		return size;
	}

	private void createClip() {
		int min = Math.min(size.width, size.height);
		if (min > 160) min = 160;
		clip = new Rectangle(min, min);
		clip.x = (size.width - clip.width) / 2;
		clip.y = (size.height - clip.height) / 2;
	}

	public BufferedImage clipImage() {
		try {
			int w = clip.width;
			int h = clip.height;
			int x0 = 0;//(getWidth() - size.width) / 2;
			int y0 = 0;//(getHeight() - size.height) / 2;
			int x = clip.x - x0;
			int y = clip.y - y0;
			clipped = image.getSubimage(x, y, w, h);
			return clipped;
		} catch (RasterFormatException rfe) {
			System.out.println("raster format error: " + rfe.getMessage());
			return null;
		}
	}

	public JPanel getUIPanel() {
		JButton clip = new JButton("save");
		clip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clipImage();
			}
		});
		JPanel panel = new JPanel();
		panel.add(clip);
		return panel;
	}

	public static void main(String[] args) throws IOException {
		File file = new File("E:\\Users\\Nanni\\Pictures\\Fototessere\\Pausa pranzo 044 Risate.jpg");
		Cropping test = new Cropping(ImageIO.read(file));
		ClipMoverAndResizer mover = new ClipMoverAndResizer(test);
		test.addMouseListener(mover);
		test.addMouseMotionListener(mover);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new JScrollPane(test));
		f.getContentPane().add(test.getUIPanel(), "South");
		f.setSize(400, 400);
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}

class ClipMover extends MouseInputAdapter {
	Cropping cropping;
	Point offset;
	boolean dragging;

	public ClipMover(Cropping c) {
		cropping = c;
		offset = new Point();
		dragging = false;
	}

	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		if (cropping.clip.contains(p)) {
			offset.x = p.x - cropping.clip.x;
			offset.y = p.y - cropping.clip.y;
			dragging = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			int x = e.getX() - offset.x;
			int y = e.getY() - offset.y;
			cropping.setClip(x, y);
		}
	}
}

class ClipMoverAndResizer extends MouseInputAdapter {
	Cropping cropping;
	Point offset;
	boolean dragging;
	boolean resizing;
	int precision = 10;

	public ClipMoverAndResizer(Cropping c) {
		cropping = c;
		offset = new Point();
		dragging = false;
		resizing = false;
	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		if (Math.abs(cropping.clip.getMaxX() - p.getX()) <= precision &&
				Math.abs(cropping.clip.getMaxY() - p.getY()) <= precision) {
			cropping.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
		} else if (cropping.clip.contains(p)) {
			cropping.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		} else {
			cropping.setCursor(Cursor.getDefaultCursor());
		}
		super.mouseEntered(e);
	}

	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		if (Math.abs(cropping.clip.getMaxX() - p.getX()) <= precision &&
				Math.abs(cropping.clip.getMaxY() - p.getY()) <= precision) {
			cropping.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			resizing = true;
		} else if (cropping.clip.contains(p)) {
			offset.x = p.x - cropping.clip.x;
			offset.y = p.y - cropping.clip.y;
			cropping.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			dragging = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		dragging = false;
		resizing = false;
		cropping.setCursor(Cursor.getDefaultCursor());
	}

	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			int x = e.getX() - offset.x;
			int y = e.getY() - offset.y;
			cropping.setClip(x, y);
		} else if (resizing) {
			int x = e.getX() - cropping.clip.x;
			//int y = e.getY() - cropping.clip.y;
			cropping.resizeClip(x, x); //square
		}
	}
}

