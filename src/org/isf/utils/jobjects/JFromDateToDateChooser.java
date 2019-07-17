/**
 * 
 */
package org.isf.utils.jobjects;

import java.awt.Font;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;

/**
 * @author Nanni
 *
 */
public class JFromDateToDateChooser extends JPanel {
	
	private final String DATE_TIME_FORMAT = "dd/MM/yyyy";
	private static final int textSize = 12;
	
	private CustomJDateChooser dateFromDateChooser;
	private CustomJDateChooser dateToDateChooser;
	private Date dateTimeFrom;
	private Date dateTimeTo;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public JFromDateToDateChooser() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		this.dateTimeFrom = new Date();
		this.dateTimeTo = new Date();
		initComponents();
	}
	
	public JFromDateToDateChooser(Date dateFrom, Date dateTo) {
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		this.dateTimeFrom = dateFrom;
		this.dateTimeTo = dateTo;
		initComponents();
	}
	
	private void initComponents() {
		this.add(new JLabel(MessageBundle.getMessage("angal.common.from")+":"));
		this.add(getCustomJDateFrom(this.dateTimeFrom));
		this.add(new JLabel(MessageBundle.getMessage("angal.common.to")+":"));
		this.add(getCustomJDateTo(this.dateTimeTo));
	}
	
	private CustomJDateChooser getCustomJDateFrom(Date dateFrom) {
		if (dateFromDateChooser == null) {
			dateFromDateChooser = new CustomJDateChooser();
			dateFromDateChooser.setDate(dateFrom);
			dateFromDateChooser.setLocale(new Locale(GeneralData.LANGUAGE));
			dateFromDateChooser.setDateFormatString(DATE_TIME_FORMAT); //$NON-NLS-1$
			dateFromDateChooser.setFont(new Font("Arial", Font.BOLD, textSize), false);
			dateFromDateChooser.addPropertyChangeListener("date", new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					dateTimeFrom = dateFromDateChooser.getDate();
				}
			});
		}
		return dateFromDateChooser;
	}
	
	private CustomJDateChooser getCustomJDateTo(Date dateTo) {
		if (dateToDateChooser == null) {
			dateToDateChooser = new CustomJDateChooser();
			dateToDateChooser.setDate(dateTo);
			dateToDateChooser.setLocale(new Locale(GeneralData.LANGUAGE));
			dateToDateChooser.setDateFormatString(DATE_TIME_FORMAT); //$NON-NLS-1$
			dateToDateChooser.setFont(new Font("Arial", Font.BOLD, textSize), false);
			dateToDateChooser.addPropertyChangeListener("date", new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					dateTimeTo = dateToDateChooser.getDate();
				}
			});
		}
		return dateToDateChooser;
	}

	/**
	 * @param layout
	 */
	public JFromDateToDateChooser(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param isDoubleBuffered
	 */
	public JFromDateToDateChooser(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public JFromDateToDateChooser(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDateFrom() {
		return dateTimeFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(CustomJDateChooser dateFrom) {
		this.dateFromDateChooser = dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public Date getDateTo() {
		return dateTimeTo;
	}

	/**
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(CustomJDateChooser dateTo) {
		this.dateToDateChooser = dateTo;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GeneralData.getGeneralData();
		Date date;
		JFromDateToDateChooser fromDateToDateChooser = new JFromDateToDateChooser();
		
		int r = JOptionPane.showConfirmDialog(null,
				fromDateToDateChooser,
                "JOptionPane Example: ",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
		
		if (r == JOptionPane.OK_OPTION) {
			date = fromDateToDateChooser.getDateFrom();
			System.out.println(date);
			date = fromDateToDateChooser.getDateTo();
			System.out.println(date);
        } else {
            return;
        }
	}

}
