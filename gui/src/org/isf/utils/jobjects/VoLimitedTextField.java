package org.isf.utils.jobjects;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 * returns a JTextField of the wanted length
 * 
 * @author studente
 *
 */
public class VoLimitedTextField extends JTextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public class LimitedDimension extends DefaultStyledDocument {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final int MAXCHARS;
		
		public LimitedDimension(int maxChars) {
			this.MAXCHARS = maxChars;
		}
		
		public void insertString(int off, String text, AttributeSet att)
		throws BadLocationException {
			int charsInDocument = getLength();
			int newLength = text.length();
			if (charsInDocument + newLength > MAXCHARS) {
				int availableChars = MAXCHARS
				- charsInDocument;
				if (availableChars > 0) {
					String newTextPart = text.substring(0,
							availableChars);
					super.insertString(off, newTextPart, att);
				}
			} else {
				super.insertString(off, text, att);
			}
		}
	}


	public VoLimitedTextField(int maxChars) {
		super();
		this.setDocument(new LimitedDimension(maxChars));
	}

	public VoLimitedTextField(int maxChars, String text, int columns) {
		super(text, columns);
		this.setDocument(new LimitedDimension(maxChars));
	}

	public VoLimitedTextField(int maxChars, String text) {
		super(text);
		this.setDocument(new LimitedDimension(maxChars));
	}

	public VoLimitedTextField(int maxChars, int columns) {
		super(columns);
		this.setDocument(new LimitedDimension(maxChars));
	}

}
