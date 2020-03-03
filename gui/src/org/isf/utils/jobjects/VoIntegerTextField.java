package org.isf.utils.jobjects;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * 
 * @author http://www.java2s.com/Code/Java/Swing-JFC/Textfieldonlyacceptsnumbers.htm
 *
 */
public class VoIntegerTextField extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param defval - default value
	 * @param columns - number of columns to show
	 */
	public VoIntegerTextField(int defval, int columns) {
		super("" + defval, columns);
	}

	protected Document createDefaultModel() {
		return new IntTextDocument();
	}

	public int getValue() {
		try {
			return Integer.parseInt(getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	class IntTextDocument extends PlainDocument {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null)
				return;
			String oldString = getText(0, getLength());
			String newString = oldString.substring(0, offs) + str + oldString.substring(offs);
			try {
				Integer.parseInt(newString + "0");
				super.insertString(offs, str, a);
			} catch (NumberFormatException e) {
			}
		}
	}
}