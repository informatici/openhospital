package org.isf.utils.jobjects;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.isf.generaldata.GeneralData;

public class JDateAndTimeChooser extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomJDateChooser date;
	private JTimeTable timeTable;
	private Date dateTime = new Date();

	/**
	 * Create the panel.
	 */
	public JDateAndTimeChooser() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		this.setSize(new Dimension(740, 400));
		this.add(getCustomJDateChooser());
		this.add(getJTimeTable());
	}
	
	private CustomJDateChooser getCustomJDateChooser() {
		if (date == null) {
			date = new CustomJDateChooser(dateTime);
			date.addPropertyChangeListener("date", new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					dateTime = date.getDate();
				}
			});
		}
		return date;
	}

	private JTimeTable getJTimeTable() {
		if (timeTable == null) {
			timeTable = new JTimeTable();
			timeTable.addPropertyChangeListener("hour", new PropertyChangeListener() {
				
				public void propertyChange(PropertyChangeEvent evt) {
					Calendar calendar = date.getCalendar();
					calendar.set(GregorianCalendar.HOUR_OF_DAY, Integer.parseInt((String) evt.getNewValue()));
					calendar.set(GregorianCalendar.SECOND, 0);
					date.setCalendar(calendar);
					dateTime = date.getDate();
				}
			});
			
			timeTable.addPropertyChangeListener("minute", new PropertyChangeListener() {
				
				public void propertyChange(PropertyChangeEvent evt) {
					Calendar calendar = date.getCalendar();
					calendar.set(GregorianCalendar.MINUTE, Integer.parseInt((String) evt.getNewValue()));
					calendar.set(GregorianCalendar.SECOND, 0);
					date.setCalendar(calendar);
					dateTime = date.getDate();
				}
			});
		}
		return timeTable;
	}

	/**
	 * @return the date
	 */
	public CustomJDateChooser getDateChooser() {
		return date;
	}
	
	/**
	 * @return the timeTable
	 */
	public JTimeTable getTimeTable() {
		return timeTable;
	}

	public Date getDateTime() {
		return dateTime;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeneralData.getGeneralData();
		Date date;
		JDateAndTimeChooser dateTimeChooser = new JDateAndTimeChooser();
		int r = JOptionPane.showConfirmDialog(null,
				dateTimeChooser,
                "JOptionPane Example: ",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
		
		if (r == JOptionPane.OK_OPTION) {
			date = dateTimeChooser.getDateTime();
			System.out.println(date);
        } else {
            return;
        }
	}
}
