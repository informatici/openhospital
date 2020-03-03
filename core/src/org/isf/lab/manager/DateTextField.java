/**
 * 02-mar-2006
 * @author Theo
 */
package org.isf.lab.manager;

import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;


public class DateTextField extends JPanel{
	/**
     * 
     */
    private static final long serialVersionUID = -9111828907347374035L;
    private JTextField day;
	private JTextField month;
	private JTextField year;
	private GregorianCalendar date;

	/**
	 * This is the constructor of the DateTextField object
	 * It displays the Date of the parameter "time"
	 * This object consists in 3 textfields (day,month,year) editable by the user
	 * @param time (GregorianCalendar)
	 */
	public DateTextField(GregorianCalendar time){
		date=time;
		day = new JTextField();
		day.setDocument(new DocumentoLimitato(2));
		day.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (day.getText().length() != 0) {
					if (day.getText().length() == 1) {
						String typed = day.getText();
						day.setText("0" + typed);
					}
					if (!isValidDay(day.getText()))
						day.setText("01");
				}
				else day.setText("01");
			}

			public void focusGained(FocusEvent e) {
			}
		});
		month = new JTextField();
		month.setDocument(new DocumentoLimitato(2));
		month.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (month.getText().length() != 0) {
					if (month.getText().length() == 1) {
						String typed = month.getText();
						month.setText("0" + typed);
					}
					if (!isValidMonth(month.getText()))
						month.setText("01");
				}
				else month.setText("01");
			}

			public void focusGained(FocusEvent e) {
			}
		});
		year = new JTextField();
		year.setDocument(new DocumentoLimitato(4));
		year.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if (year.getText().length() == 4) {
					if (!isValidYear(year.getText()))
						year.setText("2006");
				} else
					year.setText("2006");
			}

			public void focusGained(FocusEvent e) {
			}
		});
		setLayout(new FlowLayout(FlowLayout.CENTER,1,0));
		add(day);
		add(new JLabel("/"));
		add(month);
		add(new JLabel("/"));
		add(year);
		if(String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH)).length()==1)
			day.setText("0"+ time.get(GregorianCalendar.DAY_OF_MONTH));
		else day.setText(String.valueOf(time.get(GregorianCalendar.DAY_OF_MONTH)));
		if(String.valueOf(time.get(GregorianCalendar.MONTH)+1).length()==1)
			month.setText("0"+ (time.get(GregorianCalendar.MONTH) + 1));
		else month.setText(String.valueOf(time.get(GregorianCalendar.MONTH)));
		year.setText(String.valueOf(time.get(GregorianCalendar.YEAR)));

	}
	/**
	 * This method returns the day displayed by the object
	 * @return int
	 */
	public int getDay(){
		return Integer.parseInt(day.getText());
	}
	/**
	 * This method returns the month displayed by the object
	 * @return int
	 */
	public int getMonth(){
		return Integer.parseInt(month.getText());
	}
	/**
	 * This method returns the year displayed by the object
	 * @return int
	 */
	public int getYear(){
		return Integer.parseInt(year.getText());
	}
	/**
	 * This method update the parameter toModify setting the date displayed by the object
	 * @param toModify (GregorianCalendar)
	 * @return toModify (GregorianCalendar)
	 */
	public GregorianCalendar getCompleteDate(GregorianCalendar toModify){
		toModify.set(GregorianCalendar.DAY_OF_MONTH,Integer.parseInt(day.getText()));
		toModify.set(GregorianCalendar.MONTH,Integer.parseInt(month.getText()));
		toModify.set(GregorianCalendar.YEAR,Integer.parseInt(year.getText()));
		return toModify;
	}
	/**
	 * This method returns the date displayed by the object
	 * @return GregorianCalendar
	 */
	public GregorianCalendar getCompleteDate(){
		date.set(GregorianCalendar.DAY_OF_MONTH,getDay());
		date.set(GregorianCalendar.MONTH,getMonth()-1);
		date.set(GregorianCalendar.YEAR,getYear());
		return date;
	}
	/**
	 * This is a basic control for the day field input
	 * @param day (String)
	 * @return boolean (true if valid, false otherwise)
	 */
	private boolean isValidDay(String day) {
		if (day.charAt(0) < '0' || day.charAt(0) > '9' || day.charAt(1) < '0' || day.charAt(1) > '9') {
			return false;
		}
		int num = Integer.parseInt(day);
		if (num < 1 || num > 31)
			return false;
		return true;
	}
	/**
	 * This is a basic control for the month field input
	 * @param month (String)
	 * @return boolean (true if valid, false otherwise)
	 */
	private boolean isValidMonth(String month) {
		if (month.charAt(0) < '0' ||month.charAt(0) > '9' || month.charAt(1) < '0' || month.charAt(1) > '9') {
			return false;
		}
		int num = Integer.parseInt(month);
		if (num < 1 || num > 12)
			return false;
		return true;
	}
	/**
	 * This is a basic control for the year field input
	 * @param year (String)
	 * @return boolean (true if valid, false otherwise)
	 */
	private boolean isValidYear(String year) {
		if (year.charAt(0) < '0' || year.charAt(0) > '9' || year.charAt(1) < '0' || year.charAt(1) > '9'
				|| year.charAt(2) < '0' || year.charAt(2) > '0' || year.charAt(3) < '9'|| year.charAt(3) > '0') {
			return false;
		}
		return true;
	}

	/**
	 * This class extends DefaultStyledDocument and is needed to limit of each input field
	 * @author someone (found on the web)
	 */
	
	public class DocumentoLimitato extends DefaultStyledDocument {
		/**
         * 
         */
        private static final long serialVersionUID = 8335305273770830719L;
        private final int NUMERO_MASSIMO_CARATTERI;

		public DocumentoLimitato(int numeroMassimoCaratteri) {
			NUMERO_MASSIMO_CARATTERI = numeroMassimoCaratteri;
		}

		public void insertString(int off, String text, AttributeSet att)
				throws BadLocationException {
			int numeroCaratteriNelDocumento = getLength();
			int lunghezzaNuovoTesto = text.length();
			if (numeroCaratteriNelDocumento + lunghezzaNuovoTesto > NUMERO_MASSIMO_CARATTERI) {
				int numeroCaratteriInseribili = NUMERO_MASSIMO_CARATTERI
						- numeroCaratteriNelDocumento;
				if (numeroCaratteriInseribili > 0) {
					String parteNuovoTesto = text.substring(0,
							numeroCaratteriInseribili);
					super.insertString(off, parteNuovoTesto, att);
				}
			} else {
				super.insertString(off, text, att);
			}
		}
	}

}
