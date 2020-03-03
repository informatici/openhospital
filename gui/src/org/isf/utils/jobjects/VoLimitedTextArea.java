package org.isf.utils.jobjects;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 * returns a JTextArea of the wanted length
 * 
 * @author studente
 *
 */
public class VoLimitedTextArea extends JTextArea {
	
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
					String parteNuovoTesto = text.substring(0,
							availableChars);
					super.insertString(off, parteNuovoTesto, att);
				}
			} else {
				super.insertString(off, text, att);
			}
		}
	}

	/**
	 * 
	 * @param maxChars - the max number of chars
	 */
	public VoLimitedTextArea(int maxChars) {
		super();
		this.setDocument(new LimitedDimension(maxChars));
	}

	/**
	 * 
	 * @param maxChars - the max number of chars
	 * @param text
	 * @param rows
	 * @param columns
	 */
	public VoLimitedTextArea(int maxChars, String text, int rows, int columns) {
		super(text, rows, columns);
		this.setDocument(new LimitedDimension(maxChars));
	}
	
	/**
	 * 
	 * @param maxChars - the max number of chars
	 * @param rows
	 * @param columns
	 */
	public VoLimitedTextArea(int maxChars, int rows, int columns) {
		super(rows, columns);
		this.setDocument(new LimitedDimension(maxChars));
	}
}
