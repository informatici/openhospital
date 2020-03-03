package org.isf.accounting.gui;

/**
 * Browsing of table BILLS
 * 
 * @author Mwithi
 * 
 */
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.isf.accounting.gui.PatientBillEdit.PatientBillListener;
import org.isf.accounting.manager.BillBrowserManager;
import org.isf.accounting.model.Bill;
import org.isf.accounting.model.BillPayments;
import org.isf.accounting.service.AccountingIoOperations;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.patient.gui.SelectPatient;
import org.isf.patient.model.Patient;
import org.isf.stat.gui.report.GenericReportBill;
import org.isf.stat.gui.report.GenericReportFromDateToDate;
import org.isf.stat.gui.report.GenericReportPatient;
import org.isf.stat.gui.report.GenericReportUserInDate;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.time.TimeTools;
import org.joda.time.DateTime;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class BillBrowser extends ModalJFrame implements PatientBillListener {

	public void billInserted(AWTEvent event){
		if(patientParent!=null){
			try {
				updateDataSet(dateFrom, dateTo, patientParent);
			} catch (OHServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			updateDataSet(dateFrom, dateTo);
		}
		updateTables();
		updateTotals();
		if (event != null) {
			Bill billInserted = (Bill) event.getSource();
			if (billInserted != null) {
				int insertedId = billInserted.getId();
				for (int i = 0; i < jTableBills.getRowCount(); i++) {
					Bill aBill = (Bill) jTableBills.getModel().getValueAt(i, -1);
					if (aBill.getId() == insertedId)
							jTableBills.getSelectionModel().setSelectionInterval(i, i);
				}
			}
		}
	}
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPaneBills;
	private JTable jTableBills;
	private JScrollPane jScrollPaneBills;
	private JTable jTablePending;
	private JScrollPane jScrollPanePending;
	private JTable jTableClosed;
	private JScrollPane jScrollPaneClosed;
	private JTable jTableToday;
	private JTable jTablePeriod;
	private JTable jTableUser;
	private JPanel jPanelRange;
	private JPanel jPanelButtons;
	private JPanel jPanelSouth;
	private JPanel jPanelTotals;
	private JButton jButtonNew;
	private JButton jButtonEdit;
	private JButton jButtonPrintReceipt;
	private JButton jButtonDelete;
	private JButton jButtonClose;
	private Patient patientParent;
	private JButton jAffiliatePersonJButtonAdd  = null;
	private JButton jAffiliatePersonJButtonSupp  = null;
	private JTextField jAffiliatePersonJTextField  = null;
	private JButton jButtonReport;
	private JComboBox jComboUsers;
	private JTextField medicalJTextField  = null;
	private JMonthChooser jComboBoxMonths;
	private JYearChooser jComboBoxYears;
	private JPanel panelSupRange;
	private JLabel jLabelTo;
	private JLabel jLabelFrom;
	private JDateChooser jCalendarTo;
	private JDateChooser jCalendarFrom;
	private GregorianCalendar dateFrom = new GregorianCalendar();
	private GregorianCalendar dateTo = new GregorianCalendar();
	private GregorianCalendar dateToday0 = TimeTools.getDateToday0();
	private GregorianCalendar dateToday24 = TimeTools.getDateToday24();

	private JButton jButtonToday;
	
	private String[] columsNames = {MessageBundle.getMessage("angal.billbrowser.id"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.common.date"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.billbrowser.patientID"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.billbrowser.patient"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.billbrowser.amount"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.billbrowser.lastpayment"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.billbrowser.status"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.billbrowser.balance")};  //$NON-NLS-1$
	private int[] columsWidth = {50, 150, 50, 50, 100, 150, 50, 100};
	private int[] maxWidth = {150, 150, 150, 200, 100, 150, 50, 100};
	private boolean[] columsResizable = {false, false, false, true, false, false, false, false};
	private Class<?>[] columsClasses = {Integer.class, String.class, String.class, String.class, Double.class, String.class, String.class, Double.class};
	private boolean[] alignCenter = {true, true, true, false, false, true, true, false};
	private boolean[] boldCenter = {true, false, false, false, false, false, false, false};
	
	//Totals
	private BigDecimal totalToday;
	private BigDecimal balanceToday;
	private BigDecimal totalPeriod;
	private BigDecimal balancePeriod;
	private BigDecimal userToday;
	private BigDecimal userPeriod;
	private int month;
	private int year;
	
	//Bills & Payments
	private BillBrowserManager billManager = new BillBrowserManager(Context.getApplicationContext().getBean(AccountingIoOperations.class));
	private ArrayList<Bill> billPeriod;
	private HashMap<Integer, Bill> mapBill = new HashMap<Integer, Bill>();
	private ArrayList<BillPayments> paymentsPeriod;
	private ArrayList<Bill> billFromPayments;
	
	private String currencyCod = null;
	
	//Users
	private String user = UserBrowsingManager.getCurrentUser();
	private ArrayList<String> users;
	
	public BillBrowser() {
		try {
			this.currencyCod = Context.getApplicationContext().getBean(HospitalBrowsingManager.class).getHospitalCurrencyCod();
		} catch (OHServiceException e1) {
			this.currencyCod = null;
			if(e1.getMessages() != null){
				for(OHExceptionMessage msg : e1.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		
		try {
			users = billManager.getUsers();
		}catch(OHServiceException e){
			if(e.getMessages() != null){
				for(OHExceptionMessage msg : e.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		updateDataSet();
		initComponents();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initComponents() {
		add(getJPanelRange(), BorderLayout.NORTH);
		add(getJTabbedPaneBills(), BorderLayout.CENTER);
		add(getJPanelSouth(), BorderLayout.SOUTH);
		setTitle(MessageBundle.getMessage("angal.billbrowser.title")); //$NON-NLS-1$
		setMinimumSize(new Dimension(900,600));
		addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				//to free memory
				billPeriod.clear();
				mapBill.clear();
				users.clear();
				dispose();
			}			
		});
		pack();
	}

	private JPanel getJPanelSouth() {
		if (jPanelSouth == null) {
			jPanelSouth = new JPanel();
			jPanelSouth.setLayout(new BoxLayout(jPanelSouth, BoxLayout.X_AXIS));
			jPanelSouth.add(getJPanelTotals());
			jPanelSouth.add(getJPanelButtons());
		}
		return jPanelSouth;
	}

	private JPanel getJPanelTotals() {
		if (jPanelTotals == null) {
			jPanelTotals = new JPanel();
			jPanelTotals.setLayout(new BoxLayout(jPanelTotals, BoxLayout.Y_AXIS));
			jPanelTotals.add(getJTableToday());
			jPanelTotals.add(getJTablePeriod());
			if (!GeneralData.SINGLEUSER) jPanelTotals.add(getJTableUser());
			updateTotals();
		}
		return jPanelTotals;
	}

	private JLabel getJLabelTo() {
		if (jLabelTo == null) {
			jLabelTo = new JLabel();
			jLabelTo.setText(MessageBundle.getMessage("angal.common.to")); //$NON-NLS-1$
		}
		return jLabelTo;
	}

	private JDateChooser getJCalendarFrom() {
		if (jCalendarFrom == null) {
			jCalendarFrom = new JDateChooser(dateToday0.getTime()); // Calendar
			jCalendarFrom.setLocale(new Locale(GeneralData.LANGUAGE));
			jCalendarFrom.setDateFormatString("dd/MM/yy"); //$NON-NLS-1$
			jCalendarFrom.addPropertyChangeListener("date", new PropertyChangeListener() { //$NON-NLS-1$

				public void propertyChange(PropertyChangeEvent evt) {
					jCalendarFrom.setDate((Date) evt.getNewValue());
					dateFrom.setTime((Date) evt.getNewValue());
					dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
					dateFrom.set(GregorianCalendar.MINUTE, 0);
					dateFrom.set(GregorianCalendar.SECOND, 0);
					//dateToday0.setTime(dateFrom.getTime());
					jButtonToday.setEnabled(true);
					//billFilter();
					billInserted(null);
				}
			});
		}			
		return jCalendarFrom;
	}

	private JDateChooser getJCalendarTo() {
		if (jCalendarTo == null) {
			jCalendarTo = new JDateChooser(dateToday24.getTime()); // Calendar
			jCalendarTo.setLocale(new Locale(GeneralData.LANGUAGE));
			jCalendarTo.setDateFormatString("dd/MM/yy"); //$NON-NLS-1$
			jCalendarTo.addPropertyChangeListener("date", new PropertyChangeListener() { //$NON-NLS-1$
				
				public void propertyChange(PropertyChangeEvent evt) {
					jCalendarTo.setDate((Date) evt.getNewValue());
					dateTo.setTime((Date) evt.getNewValue());
					dateTo.set(GregorianCalendar.HOUR_OF_DAY, 23);
					dateTo.set(GregorianCalendar.MINUTE, 59);
					dateTo.set(GregorianCalendar.SECOND, 59);
					//dateToday24.setTime(dateTo.getTime());
					jButtonToday.setEnabled(true);
					billInserted(null);
				}
			});
		}
		return jCalendarTo;
	}
	
	private JLabel getJLabelFrom() {
		if (jLabelFrom == null) {
			jLabelFrom = new JLabel();
			jLabelFrom.setText(MessageBundle.getMessage("angal.common.from")); //$NON-NLS-1$
		}
		return jLabelFrom;
	}
	
	private JButton getJButtonReport() {
		if (jButtonReport == null) {
			jButtonReport = new JButton();
			jButtonReport.setMnemonic(KeyEvent.VK_R);
			jButtonReport.setText(MessageBundle.getMessage("angal.billbrowser.report")); //$NON-NLS-1$
			jButtonReport.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					ArrayList<String> options = new ArrayList<String>();
					if (patientParent != null)
						options.add(MessageBundle.getMessage("angal.billbrowser.patientstatement"));
					options.add(MessageBundle.getMessage("angal.billbrowser.todayclosure"));
					options.add(MessageBundle.getMessage("angal.billbrowser.today"));
					options.add(MessageBundle.getMessage("angal.billbrowser.period"));
					options.add(MessageBundle.getMessage("angal.billbrowser.thismonth"));
					options.add(MessageBundle.getMessage("angal.billbrowser.othermonth"));
					if (patientParent == null)
						options.add(MessageBundle.getMessage("angal.billbrowser.patientstatement"));
					
					Icon icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
					String option = (String) JOptionPane.showInputDialog(BillBrowser.this, 
							MessageBundle.getMessage("angal.billbrowser.pleaseselectareport"), 
							MessageBundle.getMessage("angal.billbrowser.report"), 
							JOptionPane.INFORMATION_MESSAGE, 
							icon, 
							options.toArray(), 
							options.get(0));
					
					if (option == null) return;
					
					String from = null;
					String to = null;
					
					int i = 0;
					
					if (patientParent != null && options.indexOf(option) == i) {
						new GenericReportPatient(patientParent.getCode(), GeneralData.PATIENTBILLSTATEMENT);
						return;
					}
					if (options.indexOf(option) == i) {
							
							from = TimeTools.formatDateTimeReport(dateToday0);
							to = TimeTools.formatDateTimeReport(dateToday24);
							String user;
							if (GeneralData.SINGLEUSER) {
								user = "admin";
							} else {
								user = UserBrowsingManager.getCurrentUser();
							}
							new GenericReportUserInDate(from, to, user, "BillsReportUserInDate");
							return;
						}
					if (options.indexOf(option) == ++i) {
						
						from = TimeTools.formatDateTimeReport(dateToday0);
						to = TimeTools.formatDateTimeReport(dateToday24);
					}
					if (options.indexOf(option) == ++i) {
						
						from = TimeTools.formatDateTimeReport(dateFrom);
						to = TimeTools.formatDateTimeReport(dateTo);
					}
					if (options.indexOf(option) == ++i) {
						
						month = jComboBoxMonths.getMonth();
						GregorianCalendar thisMonthFrom = dateFrom;
						GregorianCalendar thisMonthTo = dateTo;
						thisMonthFrom.set(GregorianCalendar.MONTH, month);
						thisMonthFrom.set(GregorianCalendar.DAY_OF_MONTH, 1);
						thisMonthTo.set(GregorianCalendar.MONTH, month);
						thisMonthTo.set(GregorianCalendar.DAY_OF_MONTH, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
						from = TimeTools.formatDateTimeReport(thisMonthFrom);
						to = TimeTools.formatDateTimeReport(thisMonthTo);
					}
					if (options.indexOf(option) == ++i) {
						
						icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
						
						int month;
						JMonthChooser monthChooser = new JMonthChooser();
						monthChooser.setLocale(new Locale(GeneralData.LANGUAGE));
						
				        int r = JOptionPane.showConfirmDialog(BillBrowser.this, 
				        		monthChooser, 
				        		MessageBundle.getMessage("angal.billbrowser.month"), 
				        		JOptionPane.OK_CANCEL_OPTION, 
				        		JOptionPane.PLAIN_MESSAGE,
				        		icon);

				        if (r == JOptionPane.OK_OPTION) {
				        	month = monthChooser.getMonth();
				        } else {
				            return;
				        }
				        
						GregorianCalendar thisMonthFrom = dateFrom;
						GregorianCalendar thisMonthTo = dateTo;
						thisMonthFrom.set(GregorianCalendar.MONTH, month);
						thisMonthFrom.set(GregorianCalendar.DAY_OF_MONTH, 1);
						thisMonthTo.set(GregorianCalendar.MONTH, month);
						thisMonthTo.set(GregorianCalendar.DAY_OF_MONTH, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
						from = TimeTools.formatDateTimeReport(thisMonthFrom);
						to = TimeTools.formatDateTimeReport(thisMonthTo);
					}
					if (patientParent == null && options.indexOf(option) == ++i) {
						JOptionPane.showMessageDialog(BillBrowser.this, MessageBundle.getMessage("angal.billbrowser.pleaseselectapatient"));
						return;
					}

					options = new ArrayList<String>();
					options.add(MessageBundle.getMessage("angal.billbrowser.shortreportonlybaddebts"));
					options.add(MessageBundle.getMessage("angal.billbrowser.fullreportallbills"));
										
					icon = new ImageIcon("rsc/icons/list_dialog.png"); //$NON-NLS-1$
					option = (String) JOptionPane.showInputDialog(BillBrowser.this, 
							MessageBundle.getMessage("angal.billbrowser.pleaseselectareport"), 
							MessageBundle.getMessage("angal.billbrowser.report"), 
							JOptionPane.INFORMATION_MESSAGE, 
							icon, 
							options.toArray(), 
							options.get(0));
					
					if (option == null) return;
					
					if (options.indexOf(option) == 0) {
						new GenericReportFromDateToDate(from, to, GeneralData.BILLSREPORTPENDING, MessageBundle.getMessage("angal.billbrowser.shortreportonlybaddebts"), false);
					}
					if (options.indexOf(option) == 1) {
						new GenericReportFromDateToDate(from, to, GeneralData.BILLSREPORT, MessageBundle.getMessage("angal.billbrowser.fullreportallbills"), false);
					}
				}
			});
		}
		return jButtonReport;
	}
	
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setText(MessageBundle.getMessage("angal.common.close")); //$NON-NLS-1$
			jButtonClose.setMnemonic(KeyEvent.VK_C);
			jButtonClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//to free memory
					billPeriod.clear();
					mapBill.clear();
					users.clear();
					dispose();
				}
			});
		}
		return jButtonClose;
	}

	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton();
			jButtonEdit.setText(MessageBundle.getMessage("angal.billbrowser.editbill")); //$NON-NLS-1$
			jButtonEdit.setMnemonic(KeyEvent.VK_E);
			jButtonEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						if (jScrollPaneBills.isShowing()) {
							int rowSelected = jTableBills.getSelectedRow();
							Bill editBill = (Bill)jTableBills.getValueAt(rowSelected, -1);
							if (user.equals("admin") || editBill.getStatus().equals("O")) { //$NON-NLS-1$
								PatientBillEdit pbe = new PatientBillEdit(BillBrowser.this, editBill, false);
								pbe.addPatientBillListener(BillBrowser.this);
								pbe.setVisible(true);
							} else {
								new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL);
							}
						}
						if (jScrollPanePending.isShowing()) {
							int rowSelected = jTablePending.getSelectedRow();
							Bill editBill = (Bill)jTablePending.getValueAt(rowSelected, -1);
							PatientBillEdit pbe = new PatientBillEdit(BillBrowser.this, editBill, false);
							pbe.addPatientBillListener(BillBrowser.this);
							pbe.setVisible(true);
						}
						if (jScrollPaneClosed.isShowing()) {
							int rowSelected = jTableClosed.getSelectedRow();
							Bill editBill = (Bill)jTableClosed.getValueAt(rowSelected, -1);
							new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.billbrowser.title"), //$NON-NLS-1$
								JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
		}
		return jButtonEdit;
	}
	
	private JButton getJButtonPrintReceipt() {
		if (jButtonPrintReceipt == null) {
			jButtonPrintReceipt = new JButton();
			jButtonPrintReceipt.setText(MessageBundle.getMessage("angal.billbrowser.receipt")); //$NON-NLS-1$
			jButtonPrintReceipt.setMnemonic(KeyEvent.VK_R);
			jButtonPrintReceipt.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						if (jScrollPaneBills.isShowing()){
							int rowsSelected = jTableBills.getSelectedRowCount();
							if(rowsSelected==1){
								int rowSelected = jTableBills.getSelectedRow();
								Bill editBill = (Bill)jTableBills.getValueAt(rowSelected, -1);
								if (editBill.getStatus().equals("C")) { //$NON-NLS-1$
									new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL, true, true);
								} 
								else if(editBill.getStatus().equals("D")) {
									JOptionPane.showMessageDialog(BillBrowser.this,
											MessageBundle.getMessage("angal.billbrowser.billdeleted"),  //$NON-NLS-1$
											MessageBundle.getMessage("angal.hospital"),  //$NON-NLS-1$
											JOptionPane.CANCEL_OPTION);
									return;
								} else if(editBill.getStatus().equals("O") && GeneralData.ALLOWPRINTOPENEDBILL){
									new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL, true, true);
								} else {
									JOptionPane.showMessageDialog(BillBrowser.this,
											MessageBundle.getMessage("angal.billbrowser.billnotyetclosed"),  //$NON-NLS-1$
											MessageBundle.getMessage("angal.hospital"),  //$NON-NLS-1$
											JOptionPane.CANCEL_OPTION);
									return;
								}
							}
							else if(rowsSelected>1){
								if(patientParent==null){
									JOptionPane.showMessageDialog(null,
											MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
											MessageBundle.getMessage("angal.billbrowser.title"), //$NON-NLS-1$
											JOptionPane.PLAIN_MESSAGE);
									return;
								}
								Bill billTemp = null;
								int[] billIdIndex = jTableBills.getSelectedRows();
								ArrayList<Integer> billsIdList = new ArrayList<Integer>();
								
								for (int i = 0; i < billIdIndex.length; i++) {
									billTemp = (Bill)jTableBills.getValueAt(billIdIndex[i], -1);
									if(!billTemp.getStatus().equals("D")){
										billsIdList.add(billTemp.getId());
									}
								}
								java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
								String fromDate = sdf.format(dateFrom.getTime());
								String toDate = sdf.format(dateTo.getTime());
								new GenericReportBill(billsIdList.get(0), GeneralData.PATIENTBILLGROUPED, patientParent, billsIdList,  fromDate, toDate, true, true);
								
							}
						}
						if (jScrollPanePending.isShowing()) {
							int rowsSelected = jTablePending.getSelectedRowCount();					
							if(rowsSelected==1){
								int rowSelected = jTablePending.getSelectedRow();
								Bill editBill = (Bill)jTablePending.getValueAt(rowSelected, -1);
								if (editBill.getStatus().equals("O") && GeneralData.ALLOWPRINTOPENEDBILL) {
									new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL, true, true);
								}else{
								
									PatientBillEdit pbe = new PatientBillEdit(BillBrowser.this, editBill, false);
									pbe.addPatientBillListener(BillBrowser.this);
									pbe.setVisible(true);
								}
							}else if(rowsSelected > 1){
								if(patientParent==null){
									JOptionPane.showMessageDialog(null,
											MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
											MessageBundle.getMessage("angal.billbrowser.title"), //$NON-NLS-1$
											JOptionPane.PLAIN_MESSAGE);
									return;
								}else if(GeneralData.ALLOWPRINTOPENEDBILL) {
									Bill billTemp = null;
									int[] billIdIndex = jTablePending.getSelectedRows();
									ArrayList<Integer> billsIdList = new ArrayList<Integer>();
								
									for (int i = 0; i < billIdIndex.length; i++) {
										billTemp = (Bill)jTablePending.getValueAt(billIdIndex[i], -1);
										//if(!billTemp.getStatus().equals("D")){
											billsIdList.add(billTemp.getId());
										//}
									}
									java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
									String fromDate = sdf.format(dateFrom.getTime());
									String toDate = sdf.format(dateTo.getTime());
									new GenericReportBill(billsIdList.get(0), GeneralData.PATIENTBILLGROUPED, patientParent, billsIdList,  fromDate, toDate, true, true);
								}else {
									JOptionPane.showMessageDialog(BillBrowser.this,
											MessageBundle.getMessage("angal.billbrowser.billnotyetclosed"),  //$NON-NLS-1$
											MessageBundle.getMessage("angal.hospital"),  //$NON-NLS-1$
											JOptionPane.CANCEL_OPTION);
									return;
								}
							}
						}
						if (jScrollPaneClosed.isShowing()) {
							int rowSelected = jTableClosed.getSelectedRow();
							Bill editBill = (Bill)jTableClosed.getValueAt(rowSelected, -1);
							new GenericReportBill(editBill.getId(), GeneralData.PATIENTBILL);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.billbrowser.title"), //$NON-NLS-1$
								JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
		}
		return jButtonPrintReceipt;
	}

	private void updateDataSet(GregorianCalendar dateFrom, GregorianCalendar dateTo, Patient patient) throws OHServiceException {
		/*
		 * Bills in the period
		 */
		billPeriod = billManager.getBills(dateFrom, dateTo, patient);
		
		/*
		 * Payments in the period
		 */
		paymentsPeriod = billManager.getPayments(dateFrom, dateTo, patient);
		
		/*
		 * Bills not in the period but with payments in the period
		 */
		billFromPayments = billManager.getBills(paymentsPeriod);
	}
	
	private JButton getJButtonNew() {
		if (jButtonNew == null) {
			jButtonNew = new JButton();
			jButtonNew.setText(MessageBundle.getMessage("angal.billbrowser.newbill")); //$NON-NLS-1$
			jButtonNew.setMnemonic(KeyEvent.VK_N);
			jButtonNew.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					PatientBillEdit newBill = new PatientBillEdit(BillBrowser.this, new Bill(), true);
					newBill.addPatientBillListener(BillBrowser.this);
					newBill.setVisible(true);
				}
				
			});
		}
		return jButtonNew;
	}
	
	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new JButton();
			jButtonDelete.setText(MessageBundle.getMessage("angal.billbrowser.deletebill")); //$NON-NLS-1$
			jButtonDelete.setMnemonic(KeyEvent.VK_D);
			jButtonDelete.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					try {
						Bill deleteBill = null;
						int ok = JOptionPane.NO_OPTION;
						if (jScrollPaneBills.isShowing()) {
							int rowSelected = jTableBills.getSelectedRow();
							deleteBill = (Bill)jTableBills.getValueAt(rowSelected, -1);
							ok = JOptionPane.showConfirmDialog(null, 
									MessageBundle.getMessage("angal.billbrowser.doyoureallywanttodeletetheselectedbill"),  //$NON-NLS-1$
									MessageBundle.getMessage("angal.common.delete"), //$NON-NLS-1$
									JOptionPane.YES_NO_OPTION);
							
						}
						if (jScrollPanePending != null && jScrollPanePending.isShowing()) {
							int rowSelected = jTablePending.getSelectedRow();
							deleteBill = (Bill)jTablePending.getValueAt(rowSelected, -1);
							ok = JOptionPane.showConfirmDialog(null, 
									MessageBundle.getMessage("angal.billbrowser.doyoureallywanttodeletetheselectedbill"),  //$NON-NLS-1$
									MessageBundle.getMessage("angal.common.delete"), //$NON-NLS-1$
									JOptionPane.YES_NO_OPTION);
						}
						if (jScrollPaneClosed != null && jScrollPaneClosed.isShowing()) {
							int rowSelected = jTableClosed.getSelectedRow();
							deleteBill = (Bill)jTableClosed.getValueAt(rowSelected, -1);
							ok = JOptionPane.showConfirmDialog(null, 
									MessageBundle.getMessage("angal.billbrowser.doyoureallywanttodeletetheselectedbill"),  //$NON-NLS-1$
									MessageBundle.getMessage("angal.common.delete"), //$NON-NLS-1$
									JOptionPane.YES_NO_OPTION);
						}
						if (ok == JOptionPane.YES_OPTION) {
							try{
								billManager.deleteBill(deleteBill);
							}catch(OHServiceException ex){
								if(ex.getMessages() != null){
									for(OHExceptionMessage msg : ex.getMessages()){
										JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
									}
								}
							}
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.billbrowser.pleaseselectabillfirst"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), //$NON-NLS-1$
								JOptionPane.PLAIN_MESSAGE);
					}
					billInserted(null);
				}
			});
		}
		return jButtonDelete;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			if (MainMenu.checkUserGrants("btnbillnew")) jPanelButtons.add(getJButtonNew());
			if (MainMenu.checkUserGrants("btnbilledit")) jPanelButtons.add(getJButtonEdit());
			if (MainMenu.checkUserGrants("btnbilldelete")) jPanelButtons.add(getJButtonDelete());
			if (MainMenu.checkUserGrants("btnbillreceipt") && GeneralData.RECEIPTPRINTER) jPanelButtons.add(getJButtonPrintReceipt());
			if (MainMenu.checkUserGrants("btnbillreport")) jPanelButtons.add(getJButtonReport());
			jPanelButtons.add(getJButtonClose());
		}
		return jPanelButtons;
	}

	private JPanel getJPanelRange() {
		if (jPanelRange == null) {
			jPanelRange = new JPanel();

				jPanelRange.setLayout(new BorderLayout(0, 0));
				jPanelRange.add(getPanelSupRange(), BorderLayout.NORTH);
				//if( Param.bool("ALLOWFILTERBILLBYMEDICAL")){
				//	jPanelRange.add(getPanelChooseMedical(), BorderLayout.SOUTH);
				//}
		}
		return jPanelRange;
	}
	
	
	private JPanel getPanelSupRange() {
		if (panelSupRange == null) {
			panelSupRange = new JPanel();
			if (!GeneralData.SINGLEUSER && user.equals("admin")) 
				panelSupRange.add(getJComboUsers());
				panelSupRange.add(getJButtonToday());
				panelSupRange.add(getJLabelFrom());
				panelSupRange.add(getJCalendarFrom());
				panelSupRange.add(getJLabelTo());
				panelSupRange.add(getJCalendarTo());
				panelSupRange.add(getJComboMonths());
				panelSupRange.add(getJComboYears());
				panelSupRange.add(getPanelChoosePatient());	
						
		}
		return panelSupRange;
	}

	private JPanel getPanelChoosePatient(){
		JPanel priceListLabelPanel = new JPanel();
		//panelSupRange.add(priceListLabelPanel);
		priceListLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		jAffiliatePersonJButtonAdd  = new JButton();
		jAffiliatePersonJButtonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		jAffiliatePersonJButtonAdd.setIcon(new ImageIcon("rsc/icons/pick_patient_button.png"));
		
		jAffiliatePersonJButtonSupp  = new JButton();
		jAffiliatePersonJButtonSupp.setIcon(new ImageIcon("rsc/icons/remove_patient_button.png"));
		
		jAffiliatePersonJTextField = new JTextField(14);
		jAffiliatePersonJTextField.setEnabled(false);
		priceListLabelPanel.add(jAffiliatePersonJTextField);
		priceListLabelPanel.add(jAffiliatePersonJButtonAdd);
		priceListLabelPanel.add(jAffiliatePersonJButtonSupp);
		
		jAffiliatePersonJButtonAdd.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	        	SelectPatient selectPatient = new SelectPatient(BillBrowser.this, false, true );
					selectPatient.addSelectionListener(BillBrowser.this);
					selectPatient.setVisible(true);	
					Patient pat = selectPatient.getPatient();
					//System.out.println("Patient...........+++++++++++++.............."+pat.getFirstName());
					try {
						patientSelected(pat);
					} catch (OHServiceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
	        }
		});
		
		jAffiliatePersonJButtonSupp.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
		        	patientParent = null;
		        	//garantUserChoose = null;		    
					//comboGaranti.setSelectedItem(null);
					jAffiliatePersonJTextField.setText("");
					billInserted(null);
				}
			});
		
		return priceListLabelPanel;
	}
	
	public void patientSelected(Patient patient) throws OHServiceException {
		patientParent = patient;
		jAffiliatePersonJTextField.setText(patientParent != null 
				? patientParent.getFirstName() + " " + patientParent.getFirstName() 
				: "");
		
		if (patientParent != null) {
			if (medicalJTextField != null)
				medicalJTextField.setText("");
			updateDataSet(dateFrom, dateTo, patientParent);
			updateTables();
			updateTotals();
		}
	}
	
	
	public Patient getPatientParent() {
		return patientParent;
	}
	
	public void setPatientParent(Patient patientParent) {
		this.patientParent = patientParent;
	}
	
	private JComboBox getJComboUsers() {
		if (jComboUsers == null) {
			jComboUsers = new JComboBox();
			for (String user : users) 
				jComboUsers.addItem(user);
			
			jComboUsers.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					user = (String) jComboUsers.getSelectedItem();
					jTableUser.setValueAt("<html><b>"+user+"</b></html>", 0, 0);
					updateTotals();
				}
			});
		}
		return jComboUsers;
	}
	
	private JButton getJButtonToday() {
		if (jButtonToday == null) {
			jButtonToday = new JButton();
			jButtonToday.setText(MessageBundle.getMessage("angal.billbrowser.today")); //$NON-NLS-1$
			jButtonToday.setMnemonic(KeyEvent.VK_T);
			jButtonToday.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dateFrom.setTime(dateToday0.getTime());
					dateTo.setTime(dateToday24.getTime());
					jCalendarFrom.setDate(dateFrom.getTime());
					jCalendarTo.setDate(dateTo.getTime());
					
					jButtonToday.setEnabled(false);
				}
			});
			jButtonToday.setEnabled(false);
		}
		return jButtonToday;
	}

	private JMonthChooser getJComboMonths() {
		if (jComboBoxMonths == null) {
			jComboBoxMonths = new JMonthChooser();
			jComboBoxMonths.setLocale(new Locale(GeneralData.LANGUAGE));
			jComboBoxMonths.addPropertyChangeListener("month", new PropertyChangeListener() { //$NON-NLS-1$

				public void propertyChange(PropertyChangeEvent evt) {
					month = jComboBoxMonths.getMonth();
					dateFrom.set(GregorianCalendar.MONTH, month);
					dateFrom.set(GregorianCalendar.DAY_OF_MONTH, 1);
					dateTo.set(GregorianCalendar.MONTH, month);
					dateTo.set(GregorianCalendar.DAY_OF_MONTH, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
					
					jCalendarFrom.setDate(dateFrom.getTime());
					jCalendarTo.setDate(dateTo.getTime());
				}
			});
		}
		return jComboBoxMonths;
	}

	private JYearChooser getJComboYears() {
		if (jComboBoxYears == null) {
			jComboBoxYears = new JYearChooser();
			jComboBoxYears.setLocale(new Locale(GeneralData.LANGUAGE));
			jComboBoxYears.addPropertyChangeListener("year", new PropertyChangeListener() { //$NON-NLS-1$

				public void propertyChange(PropertyChangeEvent evt) {
					year = jComboBoxYears.getYear();
					dateFrom.set(GregorianCalendar.YEAR, year);
					dateFrom.set(GregorianCalendar.MONTH, 1);
					dateFrom.set(GregorianCalendar.DAY_OF_YEAR, 1);
					dateTo.set(GregorianCalendar.YEAR, year);
					dateTo.set(GregorianCalendar.MONTH, 12);
					dateTo.set(GregorianCalendar.DAY_OF_YEAR, dateFrom.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
					jCalendarFrom.setDate(dateFrom.getTime());
					jCalendarTo.setDate(dateTo.getTime());
				}
			});
		}
		return jComboBoxYears;
	}

	private JScrollPane getJScrollPaneClosed() {
		if (jScrollPaneClosed == null) {
			jScrollPaneClosed = new JScrollPane();
			jScrollPaneClosed.setViewportView(getJTableClosed());
		}
		return jScrollPaneClosed;
	}

	private JTable getJTableClosed() {
		if (jTableClosed == null) {
			jTableClosed = new JTable();
			jTableClosed.setModel(new BillTableModel("C")); //$NON-NLS-1$
			for (int i=0;i<columsWidth.length; i++){
				jTableClosed.getColumnModel().getColumn(i).setMinWidth(columsWidth[i]);
				if (!columsResizable[i]) jTableClosed.getColumnModel().getColumn(i).setMaxWidth(maxWidth[i]);
				if (alignCenter[i]) {
					jTableClosed.getColumnModel().getColumn(i).setCellRenderer(new StringCenterTableCellRenderer());
					if (boldCenter[i]) {
						jTableClosed.getColumnModel().getColumn(i).setCellRenderer(new CenterBoldTableCellRenderer());
					}
				}
			}
			jTableClosed.setAutoCreateColumnsFromModel(false);
			jTableClosed.setDefaultRenderer(String.class, new StringTableCellRenderer());
			jTableClosed.setDefaultRenderer(Integer.class, new IntegerTableCellRenderer());
			jTableClosed.setDefaultRenderer(Double.class, new DoubleTableCellRenderer());
		}
		return jTableClosed;
	}

	private JScrollPane getJScrollPanePending() {
		if (jScrollPanePending == null) {
			jScrollPanePending = new JScrollPane();
			jScrollPanePending.setViewportView(getJTablePending());
		}
		return jScrollPanePending;
	}

	private JTable getJTablePending() {
		if (jTablePending == null) {
			jTablePending = new JTable();
			jTablePending.setModel(new BillTableModel("O")); //$NON-NLS-1$
			for (int i=0;i<columsWidth.length; i++){
				jTablePending.getColumnModel().getColumn(i).setMinWidth(columsWidth[i]);
				if (!columsResizable[i]) jTablePending.getColumnModel().getColumn(i).setMaxWidth(maxWidth[i]);
				if (alignCenter[i]) {
					jTablePending.getColumnModel().getColumn(i).setCellRenderer(new StringCenterTableCellRenderer());
					if (boldCenter[i]) {
						jTablePending.getColumnModel().getColumn(i).setCellRenderer(new CenterBoldTableCellRenderer());
					}
				}
			}
			jTablePending.setAutoCreateColumnsFromModel(false);
			jTablePending.setDefaultRenderer(String.class, new StringTableCellRenderer());
			jTablePending.setDefaultRenderer(Integer.class, new IntegerTableCellRenderer());
			jTablePending.setDefaultRenderer(Double.class, new DoubleTableCellRenderer());
		}
		return jTablePending;
	}

	private JScrollPane getJScrollPaneBills() {
		if (jScrollPaneBills == null) {
			jScrollPaneBills = new JScrollPane();
			jScrollPaneBills.setViewportView(getJTableBills());
		}
		return jScrollPaneBills;
	}

	private JTable getJTableBills() {
		if (jTableBills == null) {
			jTableBills = new JTable();
			jTableBills.setModel(new BillTableModel("ALL")); //$NON-NLS-1$
			for (int i=0;i<columsWidth.length; i++){
				jTableBills.getColumnModel().getColumn(i).setMinWidth(columsWidth[i]);
				if (!columsResizable[i]) jTableBills.getColumnModel().getColumn(i).setMaxWidth(maxWidth[i]);
				if (alignCenter[i]) {
					jTableBills.getColumnModel().getColumn(i).setCellRenderer(new StringCenterTableCellRenderer());
					if (boldCenter[i]) {
						jTableBills.getColumnModel().getColumn(i).setCellRenderer(new CenterBoldTableCellRenderer());
					}
				}
			}
			jTableBills.setAutoCreateColumnsFromModel(false);
			jTableBills.setDefaultRenderer(String.class, new StringTableCellRenderer());
			jTableBills.setDefaultRenderer(Integer.class, new IntegerTableCellRenderer());
			jTableBills.setDefaultRenderer(Double.class, new DoubleTableCellRenderer());
		}
		return jTableBills;
	}

	private JTabbedPane getJTabbedPaneBills() {
		if (jTabbedPaneBills == null) {
			jTabbedPaneBills = new JTabbedPane();
			jTabbedPaneBills.addTab(MessageBundle.getMessage("angal.billbrowser.bills"), getJScrollPaneBills()); //$NON-NLS-1$
			jTabbedPaneBills.addTab(MessageBundle.getMessage("angal.billbrowser.pending"), getJScrollPanePending()); //$NON-NLS-1$
			jTabbedPaneBills.addTab(MessageBundle.getMessage("angal.billbrowser.closed"), getJScrollPaneClosed()); //$NON-NLS-1$
		}
		return jTabbedPaneBills;
	}

	private JTable getJTableToday() {
		if (jTableToday == null) {
			jTableToday = new JTable();
			jTableToday.setModel(
					new DefaultTableModel(new Object[][] {
							{
								"<html><b>"+MessageBundle.getMessage("angal.billbrowser.todaym")+ "</b></html>",
								currencyCod,
								totalToday, 
								"<html><b>"+MessageBundle.getMessage("angal.billbrowser.notpaid")+ "</b></html>", 
								currencyCod,
								balanceToday}
							}, 	
							new String[] {"", "", "", "", "", ""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { JLabel.class, JLabel.class, Double.class, JLabel.class, JLabel.class, Double.class};
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTableToday.getColumnModel().getColumn(1).setPreferredWidth(3);
			jTableToday.getColumnModel().getColumn(4).setPreferredWidth(3);
			jTableToday.setRowSelectionAllowed(false);
			jTableToday.setGridColor(Color.WHITE);

		}
		return jTableToday;
	}
	
	private JTable getJTablePeriod() {
		if (jTablePeriod == null) {
			jTablePeriod = new JTable();
			jTablePeriod.setModel(new DefaultTableModel(
					new Object[][] {
							{
								"<html><b>"+MessageBundle.getMessage("angal.billbrowser.periodm")+"</b></html>", 
								currencyCod,
								totalPeriod, 
								"<html><b>"+MessageBundle.getMessage("angal.billbrowser.notpaid")+"</b></html>", 
								currencyCod,
								balancePeriod}
							}, 
							new String[] {"","","","","",""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { JLabel.class, JLabel.class, Double.class, JLabel.class, JLabel.class, Double.class};
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTablePeriod.getColumnModel().getColumn(1).setPreferredWidth(3);
			jTablePeriod.getColumnModel().getColumn(4).setPreferredWidth(3);
			jTablePeriod.setRowSelectionAllowed(false);
			jTablePeriod.setGridColor(Color.WHITE);

		}
		return jTablePeriod;
	}
	
	private JTable getJTableUser() {
		if (jTableUser == null) {
			jTableUser = new JTable();
			jTableUser.setModel(new DefaultTableModel(new Object[][] {{"<html><b>"+user+"</b></html>", userToday, "<html><b>"+MessageBundle.getMessage("angal.billbrowser.period")+"</b></html>", userPeriod}}, new String[] {"","","",""}) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
				Class<?>[] types = new Class<?>[] { JLabel.class, Double.class, JLabel.class, Double.class};
	
				public Class<?> getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int row, int column){
					return false;
				}
			});
			jTableUser.setRowSelectionAllowed(false);
			jTableUser.setGridColor(Color.WHITE);
		}
		return jTableUser;
	}
	
	private void updateTables() {
		jTableBills.setModel(new BillTableModel("ALL")); //$NON-NLS-1$
		jTablePending.setModel(new BillTableModel("O")); //$NON-NLS-1$
		jTableClosed.setModel(new BillTableModel("C")); //$NON-NLS-1$
	}
	
	private void updateDataSet() {
//		System.out.println(formatDateTime(new DateTime().minusMonths(5).toDateMidnight().toGregorianCalendar()));
//		System.out.println(formatDateTime(new DateTime().toDateMidnight().plusDays(1).toGregorianCalendar()));
		updateDataSet(new DateTime().toDateMidnight().toGregorianCalendar(), new DateTime().toDateMidnight().plusDays(1).toGregorianCalendar());
		
	}
	
	private void updateDataSet(GregorianCalendar dateFrom, GregorianCalendar dateTo){
		try {
			/*
			 * Bills in the period
			 */
			billPeriod = billManager.getBills(dateFrom, dateTo);
		}catch(OHServiceException e){
			if(e.getMessages() != null){
				for(OHExceptionMessage msg : e.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		try {
			/*
			 * Payments in the period
			 */
			paymentsPeriod = billManager.getPayments(dateFrom, dateTo);
		}catch(OHServiceException e){
			if(e.getMessages() != null){
				for(OHExceptionMessage msg : e.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		try {
			/*
			 * Bills not in the period but with payments in the period
			 */
			billFromPayments = billManager.getBills(paymentsPeriod);
		}catch(OHServiceException e){
			if(e.getMessages() != null){
				for(OHExceptionMessage msg : e.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
	}
	
	private void updateTotals() {
		ArrayList<Bill> billToday = null;
		ArrayList<BillPayments> paymentsToday = null;
		if (UserBrowsingManager.getCurrentUser().equals("admin")) {
			try {
				billToday = billManager.getBills(dateToday0, dateToday24);
				paymentsToday = billManager.getPayments(dateToday0, dateToday24);
			}catch(OHServiceException e){
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
		} else {
			billToday = billPeriod;
			paymentsToday = paymentsPeriod;
		}
		
		totalPeriod = new BigDecimal(0);
		balancePeriod = new BigDecimal(0);
		totalToday = new BigDecimal(0);
		balanceToday = new BigDecimal(0);
		userToday = new BigDecimal(0);
		userPeriod = new BigDecimal(0);
		
		ArrayList<Integer> notDeletedBills = new ArrayList<Integer>();
				
		//Bills in range contribute for Not Paid (balance)
		for (Bill bill : billPeriod) {
			if (!bill.getStatus().equals("D")) {
				notDeletedBills.add(bill.getId());
				BigDecimal balance = new BigDecimal(Double.toString(bill.getBalance()));
				balancePeriod = balancePeriod.add(balance);
			}
		}
		
		//Payments in range contribute for Paid Period (total)
		for (BillPayments payment : paymentsPeriod) {
			if (notDeletedBills.contains(payment.getBill().getId())) {
				BigDecimal payAmount = new BigDecimal(Double.toString(payment.getAmount()));
				String payUser = payment.getUser();
				
				totalPeriod = totalPeriod.add(payAmount);
					
				if (!GeneralData.SINGLEUSER && payUser.equals(user))
					userPeriod = userPeriod.add(payAmount);
			}
		}
		
		//Bills in today contribute for Not Paid Today (balance)
		if(billToday != null){
			for (Bill bill : billToday) {
				if (!bill.getStatus().equals("D")) {
					BigDecimal balance = new BigDecimal(Double.toString(bill.getBalance()));
					balanceToday = balanceToday.add(balance);
				}
			}
		}
		
		//Payments in today contribute for Paid Today (total)
		if(paymentsToday != null){
			for (BillPayments payment : paymentsToday) {
				if (notDeletedBills.contains(payment.getBill().getId())) {
					BigDecimal payAmount = new BigDecimal(Double.toString(payment.getAmount()));
					String payUser = payment.getUser();
					totalToday = totalToday.add(payAmount);
					if (!GeneralData.SINGLEUSER && payUser.equals(user))
						userToday = userToday.add(payAmount);
				}
			}
		}
		jTableToday.setValueAt(totalToday, 0, 2);
		jTableToday.setValueAt(balanceToday, 0, 5);
		jTablePeriod.setValueAt(totalPeriod, 0, 2);
		jTablePeriod.setValueAt(balancePeriod, 0, 5);
		if (jTableUser != null) {
			jTableUser.setValueAt(userToday, 0, 1);
			jTableUser.setValueAt(userPeriod, 0, 3);
		}
	}
	
	public class BillTableModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Bill> tableArray = new ArrayList<Bill>();
		
		/*
		 * All Bills
		 */
		private ArrayList<Bill> billAll = new ArrayList<Bill>();
		
		public BillTableModel(String status) {
			loadData(status);
		}
		
		private void loadData(String status) {
			
			tableArray.clear();
			mapBill.clear();
			
			mapping(status);
		}
		
		private void mapping(String status) {
			
			/*
			 * Mappings Bills in the period 
			 */
			for (Bill bill : billPeriod) {
				//mapBill.clear();
				mapBill.put(bill.getId(), bill);
			}
			
			/*
			 * Merging the two bills lists
			 */
			billAll.addAll(billPeriod);
			for (Bill bill : billFromPayments) {
				if (mapBill.get(bill.getId()) == null)
					billAll.add(bill);
			}
			
			if (status.equals("O")) {
				if (patientParent != null) {
					tableArray = billManager.getPendingBillsAffiliate(patientParent.getCode());
				} else {
					if (status.equals("O")) {
						for (Bill bill : billPeriod) {

							if (bill.getStatus().equals(status))
								tableArray.add(bill);
						}
					}
				}
			}
			else if (status.equals("ALL")) {
				
				Collections.sort(billAll);
				tableArray = billAll;

			} 
			else if (status.equals("C")) {
				
				for (Bill bill : billPeriod) {
					
					if (bill.getStatus().equals(status)) 
						tableArray.add(bill);
				}
			}
			
			Collections.sort(tableArray, Collections.reverseOrder());
		}
		
		public Class<?> getColumnClass(int columnIndex) {
			return columsClasses[columnIndex];
		}

		public int getColumnCount() {
			return columsNames.length;
		}

		public String getColumnName(int columnIndex) {
			return columsNames[columnIndex];
		}

		public int getRowCount() {
			if (tableArray == null)
				return 0;
			return tableArray.size();
		}
		
		//["Date", "Patient", "Balance", "Update", "Status", "Amount"};

		public Object getValueAt(int r, int c) {
			int index = -1;
			Bill thisBill = tableArray.get(r);
			if (c == index) {
				return thisBill;
			}
			if (c == ++index) {
				return thisBill.getId();
			}
			if (c == ++index) {
				return TimeTools.formatDateTime(thisBill.getDate(), "dd/MM/yy - HH:mm:ss");
			}
			if (c == ++index) {
				int patID = thisBill.getPatient().getCode();
				return patID == 0 ? "" : String.valueOf(patID);
				//return thisBill.getId();
			}
			if (c == ++index) {
				return thisBill.getPatName();
			}
			if (c == ++index) {
				return thisBill.getAmount();
			}
			if (c == ++index) {
				return TimeTools.formatDateTime(thisBill.getUpdate(), "dd/MM/yy - HH:mm:ss");
			}
			if (c == ++index) {
				return thisBill.getStatus();
			}
			if (c == ++index) {
				return thisBill.getBalance();
			}
			return null;
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}
	
	public boolean isSameDay(GregorianCalendar aDate, GregorianCalendar today) {
		return (aDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)) &&
			   (aDate.get(Calendar.MONTH) == today.get(Calendar.MONTH)) &&
			   (aDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH));
	}
	
	class StringTableCellRenderer extends DefaultTableCellRenderer {  
	   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			if (((String)table.getValueAt(row, 6)).equals("D")) { //$NON-NLS-1$
				cell.setForeground(Color.RED);
			}
			return cell;
	   }
	}
	
	class StringCenterTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			setHorizontalAlignment(CENTER);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			if (((String)table.getValueAt(row, 6)).equals("D")) { //$NON-NLS-1$
				cell.setForeground(Color.RED);
			}
			return cell;
	   }
	}
	
	class IntegerTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			cell.setFont(new Font(null, Font.BOLD, 12));
			setHorizontalAlignment(CENTER);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			if (((String)table.getValueAt(row, 6)).equals("D")) { //$NON-NLS-1$
				cell.setForeground(Color.RED);
			}
			return cell;
	   }
	}
	
	class DoubleTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			setHorizontalAlignment(RIGHT);
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			if (((String)table.getValueAt(row, 6)).equals("D")) { //$NON-NLS-1$
				cell.setForeground(Color.RED);
			}
			return cell;
	   }
	}
	
	class CenterBoldTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			setHorizontalAlignment(CENTER);
			cell.setFont(new Font(null, Font.BOLD, 12));
			if (((String)table.getValueAt(row, 6)).equals("C")) { //$NON-NLS-1$
				cell.setForeground(Color.GRAY);
			}
			if (((String)table.getValueAt(row, 6)).equals("D")) { //$NON-NLS-1$
				cell.setForeground(Color.RED);
			}
			return cell;
	   }
	}
}
