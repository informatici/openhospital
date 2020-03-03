package org.isf.utils.jobjects;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 * returns a JTextField for date
 * it can manage dates in format dd/mm/yyyy, dd/mm/yy, mm/dd/yyyy or mm/dd/yy
 * 
 * @author Rick
 *
 */
public class VoDateTextField extends JTextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String TYPE;
	private String currentDate = "nothing";
	
	private void setType(String type) {
		this.TYPE = type;
	}
	
	public class ManagedData extends DefaultStyledDocument {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int MAXCHARS = 0;
		
		private ManagedData() {
			if (TYPE.equals("dd/mm/yy") || TYPE.equals("mm/dd/yy"))
				this.MAXCHARS = 8;
			else if (TYPE.equals("dd/mm/yyyy") || TYPE.equals("mm/dd/yyyy") )
				this.MAXCHARS = 10;
		}
		
		public void insertString(int off, String text2, AttributeSet att)
		throws BadLocationException {
			int charsInDocument = getLength();
			int newLength = text2.length();
			if (charsInDocument + newLength > MAXCHARS) {
				int availableChars = MAXCHARS - charsInDocument;
				if (availableChars > 0) {
					String parteNuovoTesto = text2.substring(0, availableChars);
					super.insertString(off, parteNuovoTesto, att);
				}
			} else {
				super.insertString(off, text2, att);
			}
		}
	}
	
	/**
	 * constructor with no default date
	 */
	public VoDateTextField(String type, int cols) throws IllegalArgumentException {
		super(cols);
		if (!(type.equals("dd/mm/yy") || type.equals("dd/mm/yyyy") ||
				type.equals("mm/dd/yy") || type.equals("mm/dd/yyyy")))
			throw new IllegalArgumentException();
		setType(type);
		this.setDocument(new ManagedData());
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
		check();
	}
	
	/**
	 * constructor with default date
	 */
	public VoDateTextField(String type, String todayDate, int cols) throws IllegalArgumentException {
		super(cols);
		if (!(type.equals("dd/mm/yy") || type.equals("dd/mm/yyyy") ||
				type.equals("mm/dd/yyyy") || type.equals("mm/dd/yyyy")))
			throw new IllegalArgumentException();
		setType(type);
		this.setDocument(new ManagedData());
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
		this.setText(todayDate);
		this.currentDate = todayDate;
		check();
	}
	
	public VoDateTextField(String type, GregorianCalendar todayDate, int cols) throws IllegalArgumentException{
		super(cols);
		if (!(type.equals("dd/mm/yy") || type.equals("dd/mm/yyyy") ||
				type.equals("mm/dd/yyyy") || type.equals("mm/dd/yyyy")))
			throw new IllegalArgumentException();
		setType(type);
		this.setDocument(new ManagedData());
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
		this.setText(getConvertedString(todayDate));
		this.setDate(todayDate);
		check();
	}
	
	/**
	 * when focus is lost check if the date is correct or not
	 */
	private void check() {
		
		this.addFocusListener(new FocusListener() {
			
			public void focusGained(FocusEvent e) {}
			
			public void focusLost(FocusEvent e) throws IllegalArgumentException {
				// if date field is not mandatory, can be left empty
				if (getText().length()==0) {
					//System.out.println("empty string");
					return;
				}
				if (getText().length()!=TYPE.length()) {
					JOptionPane.showMessageDialog(				
							null,
							"\""+getText()+"\" is not a valid date",
							"St Luke Hospital",
							JOptionPane.PLAIN_MESSAGE);
						if (currentDate.equals("nothing"))
							setText("");
						else
							setText(currentDate);
						return;
				}
				char separator = getText().charAt(2);
				if (((separator=='/')||(separator=='-'))&&(getText().charAt(5)==separator)) {
					try {
						GregorianCalendar gc = new GregorianCalendar();
						gc.setLenient(false); //must do this
						if (TYPE.equals("dd/mm/yy") || TYPE.equals("mm/dd/yy"))
							gc.set(GregorianCalendar.YEAR, Integer.parseInt(getText().substring(6,8)) + 2000);
						else if (TYPE.equals("dd/mm/yyyy") || TYPE.equals("mm/dd/yyyy") )
							gc.set(GregorianCalendar.YEAR, Integer.parseInt(getText().substring(6,10)));
						
						if (TYPE.equals("dd/mm/yy") || TYPE.equals("dd/mm/yyyy"))
							gc.set(GregorianCalendar.MONTH, Integer.parseInt(getText().substring(3,5)) - 1);
						else
							gc.set(GregorianCalendar.MONTH, Integer.parseInt(getText().substring(0,2)) - 1);
						
						if (TYPE.equals("dd/mm/yy") || TYPE.equals("dd/mm/yyyy"))
							gc.set(GregorianCalendar.DATE, Integer.parseInt(getText().substring(0,2)));
						else
							gc.set(GregorianCalendar.DATE, Integer.parseInt(getText().substring(3,5)));
						gc.getTime(); //exception thrown here (if needed)
						currentDate = getText();
					}
					catch (Exception e1) {
						JOptionPane.showMessageDialog(				
							null,
							"\""+getText()+"\" is not a valid date",
							"St Luke Hospital",
							JOptionPane.PLAIN_MESSAGE);
						if (currentDate.equals("nothing"))
							setText("");
						else
							setText(currentDate);
					}
				} else {
					JOptionPane.showMessageDialog(				
							null,
							"\""+getText()+"\" is not a valid date\n" +
							"Please use / or - to separate",
							"St Luke Hospital",
							JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
	}
	
	/**
	 * returns a GregorianCalendar for date use
	 */
	public GregorianCalendar getDate() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setLenient(false);
		if (TYPE.equals("dd/mm/yyyy"))
			calendar.set(Integer.parseInt(getText().substring(6,10)),Integer.parseInt(getText().substring(3,5))-1,Integer.parseInt(getText().substring(0,2)));
		else if (TYPE.equals("mm/dd/yyyy"))
			calendar.set(Integer.parseInt(getText().substring(6,10)),Integer.parseInt(getText().substring(0,2)) - 1,Integer.parseInt(getText().substring(3,5)));
		else if (TYPE.equals("dd/mm/yy"))
			calendar.set(Integer.parseInt(getText().substring(6,8)) + 2000,Integer.parseInt(getText().substring(3,5))-1,Integer.parseInt(getText().substring(0,2)));
		else if (TYPE.equals("mm/dd/yy"))
			calendar.set(Integer.parseInt(getText().substring(6,8)) + 2000,Integer.parseInt(getText().substring(0,2)) - 1,Integer.parseInt(getText().substring(3,5)));
		//System.out.println(calendar.get(Calendar.DAY_OF_MONTH)+ " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.YEAR));
		return calendar;
	}
	
	public void setDate(GregorianCalendar time){
		String string;
		if(time.get(GregorianCalendar.DAY_OF_MONTH)>9)
			string=String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		else
			string="0"+String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		if(time.get(GregorianCalendar.MONTH)+1>9)
			string+="/"+String.valueOf(time.get(GregorianCalendar.MONTH)+1);
		else
			string+="/0"+String.valueOf(time.get(GregorianCalendar.MONTH)+1);
		string+="/"+String.valueOf(time.get(GregorianCalendar.YEAR));
		currentDate = string;
	}
	
	private String getConvertedString(GregorianCalendar time){
		String string;if(time.get(GregorianCalendar.DAY_OF_MONTH)>9)
			string=String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		else
			string="0"+String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH));
		if(time.get(GregorianCalendar.MONTH)+1>9)
			string+="/"+String.valueOf(time.get(GregorianCalendar.MONTH)+1);
		else
			string+="/0"+String.valueOf(time.get(GregorianCalendar.MONTH)+1);
		string+="/"+String.valueOf(time.get(GregorianCalendar.YEAR));
		return string;
	}
	
}
