package org.isf.utils.jobjects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.border.AbstractBorder;

/**
 * 
 * Custom Border which create a component shadow with specified offset and color
 * 
 * @author Mwithi
 *
 */
public class ShadowBorder extends AbstractBorder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color color;
	private int offset;

	/**
	 * Constructor
	 * 
	 * @param aOffset
	 * @param aColor
	 */
	public ShadowBorder(int aOffset, Color aColor) {
		offset = aOffset;
		color = aColor;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {

		g.setColor(color);
		g.fillRect(x + offset, y + offset, w + offset, h + offset);
	}

	public boolean isBorderOpaque() {
		return true;
	}
}