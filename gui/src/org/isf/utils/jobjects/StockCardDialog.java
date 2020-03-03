package org.isf.utils.jobjects;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;

/**
 * @author Mwithi
 *
 */
public class StockCardDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextFieldSearchModel textField;
	private JFromDateToDateChooser dateRange;
	private JPanel buttonsPanel;
	private JButton buttonPDF;
	private JButton buttonExcel;
	private JButton buttonCancel;
	private Date dateFrom;
	private Date dateTo;
	private Medical medical;
	private boolean excel;
	private boolean cancel = false;

	public StockCardDialog(Frame owner) {
		super(owner, true);
		textField = new JTextFieldSearchModel(this, Medical.class);
		dateRange = new JFromDateToDateChooser();
		initAndShow();
	}

	public StockCardDialog(Frame owner, Medical medical) {
		super(owner, true);
		if (medical != null)
			textField = new JTextFieldSearchModel(this, medical);
		else
			textField = new JTextFieldSearchModel(this, Medical.class);
		dateRange = new JFromDateToDateChooser();
		initAndShow();
	}

	public StockCardDialog(Frame owner, Medical medical, Date dateFrom, Date dateTo) {
		super(owner, true);
		if (medical != null)
			textField = new JTextFieldSearchModel(this, medical);
		else
			textField = new JTextFieldSearchModel(this, Medical.class);
		dateRange = new JFromDateToDateChooser(dateFrom, dateTo);
		initAndShow();
	}

	private void initAndShow() {
		add(textField, BorderLayout.NORTH);
		add(dateRange, BorderLayout.CENTER);
		add(getButtonsPanel(), BorderLayout.SOUTH);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.add(getButtonOK());
			buttonsPanel.add(getButtonExcel());
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
					cancel = true;
					dispose();
				}
			});
		}
		return buttonCancel;
	}

	private JButton getButtonOK() {
		if (buttonPDF == null) {
			buttonPDF = new JButton(MessageBundle.getMessage("angal.common.ok"));
			buttonPDF.setMnemonic(KeyEvent.VK_P);
			buttonPDF.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					medical = (Medical) textField.getSelectedObject();
					dateFrom = dateRange.getDateFrom();
					dateTo = dateRange.getDateTo();
					dispose();
				}
			});
		}
		return buttonPDF;
	}

	private JButton getButtonExcel() {
		if (buttonExcel == null) {
			buttonExcel = new JButton(MessageBundle.getMessage("angal.common.excel"));
			buttonExcel.setMnemonic(KeyEvent.VK_E);
			buttonExcel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					medical = (Medical) textField.getSelectedObject();
					dateFrom = dateRange.getDateFrom();
					dateTo = dateRange.getDateTo();
					excel = true;
					dispose();
				}
			});
		}
		return buttonExcel;
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public Date getDateTo() {
		return dateTo;
	}

	/**
	 * @return the med
	 */
	public Medical getMedical() {
		return medical;
	}

	/**
	 * @return the excel
	 */
	public boolean isExcel() {
		return excel;
	}

	/**
	 * @return the cancel
	 */
	public boolean isCancel() {
		return cancel;
	}

}