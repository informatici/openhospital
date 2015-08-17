/**
 * MonthYearChooser.java - 14/dic/2012
 */
package org.isf.utils.jobjects;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.isf.generaldata.GeneralData;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

/**
 * @author Mwithi
 *
 */
public class JMonthYearChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GregorianCalendar gc = new GregorianCalendar();
	
	/**
	 * Create the dialog.
	 */
	public JMonthYearChooser() {
		
		JMonthChooser month = new JMonthChooser();
		month.setLocale(new Locale(GeneralData.LANGUAGE));
		month.addPropertyChangeListener("month", new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				JMonthChooser theChooser = (JMonthChooser) evt.getSource();
				gc.set(GregorianCalendar.MONTH, theChooser.getMonth());
			}
		});
		
		
		JYearChooser year = new JYearChooser();
		year.setLocale(new Locale(GeneralData.LANGUAGE));
		year.addPropertyChangeListener("year", new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				JYearChooser theChooser = (JYearChooser) evt.getSource();
				gc.set(GregorianCalendar.YEAR, theChooser.getYear());
			}
		});
		
		JPanel datePanel = new JPanel();
		datePanel.add(month);
		datePanel.add(year);
		this.add(datePanel);
	}
	
	
	public GregorianCalendar getDate() {
		return gc;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeneralData.getGeneralData();
		GregorianCalendar date;
		JMonthYearChooser monthChooser = new JMonthYearChooser();
		
		int r = JOptionPane.showConfirmDialog(null,
				monthChooser,
                "JOptionPane Example : ",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
		
		if (r == JOptionPane.OK_OPTION) {
			date = monthChooser.getDate();
			System.out.println(date);
        } else {
            return;
        }
	}
}
