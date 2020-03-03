/**
 * JLabelRequired.java - 28/gen/2014
 */
package org.isf.utils.jobjects;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author Mwithi
 *
 */
public class JLabelRequired extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1519907071350395237L;
	
	private static String MARK = " *";

	/**
	 * 
	 */
	public JLabelRequired() {
	}

	/**
	 * @param text
	 */
	public JLabelRequired(String text) {
		super(text);
	}

	/**
	 * @param image
	 */
	public JLabelRequired(Icon image) {
		super(image);
	}

	/**
	 * @param text
	 * @param horizontalAlignment
	 */
	public JLabelRequired(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	/**
	 * @param image
	 * @param horizontalAlignment
	 */
	public JLabelRequired(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	/**
	 * @param text
	 * @param icon
	 * @param horizontalAlignment
	 */
	public JLabelRequired(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JLabel#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		super.setText(text + MARK);
	}
}
