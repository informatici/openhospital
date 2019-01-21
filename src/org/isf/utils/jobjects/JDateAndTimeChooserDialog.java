/**
 * 
 */
package org.isf.utils.jobjects;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;

/**
 * @author Mwithi
 * 
 * 
 * 
 */
public class JDateAndTimeChooserDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Constants
	 */
	private final String DATE_TIME_FORMAT = "EEEE, d MMMM yyyy - HH:mm";
	private static final int textSize = 30;

	/*
	 * Attributes
	 */
	private JDateAndTimeChooser dateAndTimeChooser;
	private JPanel buttonsPanel;
	private JButton buttonOK;
	private JButton buttonCancel;

	/*
	 * Return Value
	 */
	private Date date = null;

	public JDateAndTimeChooserDialog(JDialog owner) {
		super(owner, true);
		initComponents();
	}

	public JDateAndTimeChooserDialog(JDialog owner, Date date) {
		super(owner, true);
		this.date = date;
		initComponents();
	}

	private void initComponents() {
		setSize(new Dimension(740, 400));
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getDateAndTimeChooser());
		getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
		setResizable(false);
		setLocationRelativeTo(null);

	}

	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.add(getButtonOK());
			buttonsPanel.add(getButtonCancel());
		}
		return buttonsPanel;
	}

	private JButton getButtonCancel() {
		if (buttonCancel == null) {
			buttonCancel = new JButton(MessageBundle.getMessage("angal.common.cancel"));
			buttonCancel.setMnemonic(KeyEvent.VK_N);
			buttonCancel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return buttonCancel;
	}

	private JButton getButtonOK() {
		if (buttonOK == null) {
			buttonOK = new JButton(MessageBundle.getMessage("angal.common.ok"));
			buttonOK.setMnemonic(KeyEvent.VK_O);
			buttonOK.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					date = dateAndTimeChooser.getDateTime();
					dispose();
				}
			});
		}
		return buttonOK;
	}

	public JDateAndTimeChooser getDateAndTimeChooser() {
		if (dateAndTimeChooser == null) {
			dateAndTimeChooser = new JDateAndTimeChooser();
			dateAndTimeChooser.getDateChooser().setLocale(new Locale(GeneralData.LANGUAGE));
			dateAndTimeChooser.getDateChooser().setDateFormatString(DATE_TIME_FORMAT); //$NON-NLS-1$
			dateAndTimeChooser.getDateChooser().setFont(new Font("Arial", Font.BOLD, textSize), false);
			if (date != null) dateAndTimeChooser.getDateChooser().setDate(date);
		}
		return dateAndTimeChooser;
	}

	public Date getDate() {
		return date;
	}

}
