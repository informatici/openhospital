/**
 * just a subclass to set transparency and some stuff.
 */
package org.isf.utils.jobjects;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author Mwithi
 * 
 * JButton subclass, it help to create a transparent
 * button with only an Icon in the center
 */
public class IconButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param icon
	 */
	public IconButton(Icon icon) {
		super(icon);
		setOpaque(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
	}
}
