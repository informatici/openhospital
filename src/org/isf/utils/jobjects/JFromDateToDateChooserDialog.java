/**
 * 
 */
package org.isf.utils.jobjects;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.isf.generaldata.MessageBundle;

/**
 * @author Mwithi
 * 
 * 
 * 
 */
public class JFromDateToDateChooserDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Attributes
	 */
	private JFromDateToDateChooser fromDateToDateChooser;
	private JPanel buttonsPanel;
	private JButton buttonPDF;
	private JButton buttonExcel;
	private JButton buttonCancel;
	private boolean cancel = false;

	/*
	 * Return Value
	 */
	private Date dateFrom = null;
	private Date dateTo = null;
	private boolean excel = false;

	public JFromDateToDateChooserDialog(ModalJFrame owner) {
		super(owner, true);
		this.dateFrom = new Date();
		this.dateTo = new Date();
		initComponents();
	}

	public JFromDateToDateChooserDialog(JDialog owner, Date dateFrom, Date dateTo) {
		super(owner, true);
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		initComponents();
	}

	private void initComponents() {
//		setPreferredSize(new Dimension(400, 200));
		getContentPane().setLayout(new BorderLayout(10,10));
		getContentPane().add(getJFromDateToDateChooser(this.dateFrom, this.dateTo), BorderLayout.CENTER);
		getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);

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
					dateFrom = fromDateToDateChooser.getDateFrom();
					dateTo = fromDateToDateChooser.getDateTo();
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
					dateFrom = fromDateToDateChooser.getDateFrom();
					dateTo = fromDateToDateChooser.getDateTo();
					excel = true;
					dispose();
				}
			});
		}
		return buttonExcel;
	}

	public JFromDateToDateChooser getJFromDateToDateChooser(Date dateFrom, Date dateTo) {
		if (fromDateToDateChooser == null) {
			fromDateToDateChooser = new JFromDateToDateChooser(dateFrom, dateTo);
		}
		return fromDateToDateChooser;
	}

	public Date getDateFrom() {
		return dateFrom;
	}
	
	public Date getDateTo() {
		return dateTo;
	}
	
	public boolean isExcel() {
		return excel;
	}

	public boolean isCancel() {
		return cancel;
	}

}
