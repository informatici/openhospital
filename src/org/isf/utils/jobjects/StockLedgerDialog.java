package org.isf.utils.jobjects;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.isf.generaldata.MessageBundle;

/**
 * @author Mwithi
 *
 */
public class StockLedgerDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFromDateToDateChooser dateRange;
	private JPanel buttonsPanel;
	private JButton buttonPDF;
	private JButton buttonCancel;
	private Date dateFrom;
	private Date dateTo;
	private boolean cancel = false;

	public StockLedgerDialog(Frame owner) {
		super(owner, true);
		dateRange = new JFromDateToDateChooser();
		initAndShow();
	}

	public StockLedgerDialog(Frame owner, Date dateFrom, Date dateTo) {
		super(owner, true);
		dateRange = new JFromDateToDateChooser(dateFrom, dateTo);
		initAndShow();
	}

	private void initAndShow() {
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
					int n = JOptionPane.showConfirmDialog(StockLedgerDialog.this,
							MessageBundle.getMessage("angal.admission.thiscouldretrievealargeamountofdataproceed"));
					if (n != JOptionPane.OK_OPTION) 
						cancel = true;
					dateFrom = dateRange.getDateFrom();
					dateTo = dateRange.getDateTo();
					dispose();
				}
			});
		}
		return buttonPDF;
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
	 * @return the cancel
	 */
	public boolean isCancel() {
		return cancel;
	}
}