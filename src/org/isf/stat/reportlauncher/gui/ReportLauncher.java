package org.isf.stat.reportlauncher.gui;

/*--------------------------------------------------------
 * ReportLauncher - lancia tutti i report che come parametri hanno
 * 					anno e mese
 * 					la classe prevede l'inizializzazione attraverso 
 *                  anno, mese, nome del report (senza .jasper)
 *---------------------------------------------------------
 * modification history
 * 01/01/2006 - rick - prima versione. lancia HMIS1081 e HMIS1081 
 * 11/11/2006 - ross - resa barbaramente generica (ad angal)
 * 16/11/2014 - eppesuig - show WAIT_CURSOR during generateReport()
 *-----------------------------------------------------------------*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.stat.gui.report.GenericReportFromDateToDate;
import org.isf.stat.gui.report.GenericReportMY;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.VoDateTextField;
import org.isf.xmpp.gui.CommunicationFrame;
import org.isf.xmpp.manager.Interaction;

public class ReportLauncher extends ModalJFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pfrmExactWidth = 500;
	private int pfrmExactHeight = 165;
	private int pfrmBordX;
	private int pfrmBordY;
	private JPanel jPanel = null;
	private JPanel jButtonPanel = null;
	private JButton jCloseButton = null;
	private JPanel jContentPanel = null;
	private JButton jOkButton = null;
	private JButton jCSVButton = null;
	private JPanel jMonthPanel = null;
	private JLabel jMonthLabel = null;
	private JComboBox jMonthComboBox = null;
	private JLabel jYearLabel = null;
	private JComboBox jYearComboBox = null;
	private JLabel jFromDateLabel = null;
	private JLabel jToDateLabel = null;
	private VoDateTextField jToDateField = null;
	private VoDateTextField jFromDateField = null;
	
	
	private JLabel jRptLabel = null;
	private JComboBox jRptComboBox = null;
	
	private final int BUNDLE = 0;
	private final int FILENAME = 1;
	private final int TYPE = 2;
	
	private String[][] reportMatrix = {
		{"angal.stat.registeredpatient", 				"OH001_RegisteredPatients", 										"twodates"},
		{"angal.stat.registeredpatientbyprovenance", 	"OH002_RegisteredPatientsByProvenance", 							"twodates"},
		{"angal.stat.registeredpatientbyageandsex", 	"OH003_RegisteredPatientsByAgeAndSex", 								"twodates"},
		{"angal.stat.incomesallbypricecodes", 			"OH004_IncomesAllByPriceCodes", 									"twodates"},
		{"angal.stat.outpatientcount", 					"OH005_opd_count_monthly_report", 									"monthyear"},
		{"angal.stat.outpatientdiagnoses", 				"OH006_opd_dis_monthly_report", 									"monthyear"},
		{"angal.stat.labmonthlybasic", 					"OH007_lab_monthly_report", 										"monthyear"},
		{"angal.stat.labsummaryforopd", 				"OH008_lab_summary_for_opd", 										"monthyear"},
		{"angal.stat.inpatientreport", 					"OH009_InPatientReport", 											"twodates"},
		{"angal.stat.outpatientreport", 				"OH010_OutPatientReport", 											"twodates"},
		{"angal.stat.pageonecensusinfo", 				"hmis108_cover", 													"twodatesfrommonthyear"},
		{"angal.stat.pageonereferrals", 				"hmis108_referrals", 												"monthyear"},
		{"angal.stat.pageoneoperations", 				"hmis108_operations", 												"monthyear"},
		{"angal.stat.inpatientdiagnosisin", 			"hmis108_adm_by_diagnosis_in", 										"monthyear"},
		{"angal.stat.inpatientdiagnosisout", 			"hmis108_adm_by_diagnosis_out", 									"monthyear"},
		{"angal.stat.opdattendance", 					"hmis105_opd_attendance", 											"monthyear"},
		{"angal.stat.opdreferrals", 					"hmis105_opd_referrals", 											"monthyear"},
		{"angal.stat.opdbydiagnosis", 					"hmis105_opd_by_diagnosis", 										"monthyear"},
		{"angal.stat.labmonthlyformatted", 				"hmis055b_lab_monthly_formatted", 									"monthyear"},
		{"angal.stat.weeklyepidemsurveil", 				"hmis033_weekly_epid_surv", 										"twodates"},
		{"angal.stat.weeklyepidemsurveilunder5", 		"hmis033_weekly_epid_surv_under_5", 								"twodates"},
		{"angal.stat.weeklyepidemsurveilover5", 		"hmis033_weekly_epid_surv_over_5", 									"twodates"},
		{"angal.stat.monthlyworkloadreportpage1", 		"MOH717_Monthly_Workload_Report_for_Hospitals_page1", 				"monthyear"},
		{"angal.stat.monthlyworkloadreportpage2", 		"MOH717_Monthly_Workload_Report_for_Hospitals_page2", 				"monthyear"},
		{"angal.stat.dailyopdmorbiditysummaryunder5", 	"MOH705A_Under_5_Years_Daily_Outpatient_Morbidity_Summary_Sheet", 	"monthyear"},
		{"angal.stat.dailyopdmorbiditysummaryover5", 	"MOH705B_Over_5_Years_Daily_Outpatient_Morbidity_Summary_Sheet", 	"monthyear"},
	};
	
	private JComboBox shareWith=null;//nicola
	Interaction userOh=null;

	
	
//	private final JFrame myFrame;
	
	/**
	 * This is the default constructor
	 */
	public ReportLauncher() {
		super();
//		myFrame = this;
		this.setResizable(true);
		initialize();
		setVisible(true);
	}

	/**
	 * This method initializes this	
	 * 	
	 * @return void	
	 */
	private void initialize() {
		this.setTitle(MessageBundle.getMessage("angal.stat.reportlauncher"));
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width / 3) - (pfrmExactWidth / 2);
		pfrmBordY = (screensize.height / 3) - (pfrmExactHeight / 2);
		this.setBounds(pfrmBordX,pfrmBordY,pfrmExactWidth,pfrmExactHeight);
		this.setContentPane(getJPanel());
		selectAction();
		pack();
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJButtonPanel(), BorderLayout.SOUTH);
			jPanel.add(getJContentPanel(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.setLayout(new FlowLayout());
			if(GeneralData.XMPPMODULEENABLED)
				jButtonPanel.add(getComboShareReport(),null);
			jButtonPanel.add(getJOkButton(), null);
			jButtonPanel.add(getJCSVButton(), null);
			//jButtonPanel.add(getJShareButton(),null);
			jButtonPanel.add(getJCloseButton(), null);
			
		}
		return jButtonPanel;
	}

	private JComboBox getComboShareReport() {
		userOh= new Interaction();
		Collection<String> contacts = userOh.getContactOnline();
		contacts.add("-- Share report with : Nobody --");
		shareWith = new JComboBox(contacts.toArray());
		shareWith.setSelectedItem("-- Share report with : Nobody --");
		return shareWith;
	}

	/**
	 * This method initializes jCloseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCloseButton() {
		if (jCloseButton == null) {
			jCloseButton = new JButton();
			jCloseButton.setText(MessageBundle.getMessage("angal.common.close"));
			jCloseButton.setMnemonic(KeyEvent.VK_C);
			jCloseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return jCloseButton;
	}

	/**
	 * This method initializes jContentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPanel() {
		if (jContentPanel == null) {
			
			jContentPanel = new JPanel();
			jContentPanel.setLayout(new BorderLayout());
			
			JPanel rep1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

			rep1.add(getJParameterSelectionPanel());
			rep1 = setMyBorder(rep1, MessageBundle.getMessage("angal.stat.parametersselectionframe") + " ");
			
			jContentPanel.add(rep1, BorderLayout.NORTH);
			//jContentPanel.add(rep2, BorderLayout.SOUTH);
			
				
		}
		return jContentPanel;
	}

	
	
	private JPanel getJParameterSelectionPanel() {

		if (jMonthPanel == null) {

			jMonthPanel = new JPanel();
			jMonthPanel.setLayout(new FlowLayout());
			
			//final DateFormat dtf = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
			//String dt = dtf.format(new java.util.Date());
			//Integer month = Integer.parseInt(dt.substring(3, 5));
			//Integer year = 2000 + Integer.parseInt(dt.substring(6, 8));

			java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
			Integer month=gc.get(Calendar.MONTH);
			Integer year = gc.get(Calendar.YEAR);

			//System.out.println("m="+month +",y="+ year);
			
			jRptLabel = new JLabel();
			jRptLabel.setText(MessageBundle.getMessage("angal.stat.report"));
			
			
			jRptComboBox = new JComboBox();
			for (int i=0;i<reportMatrix.length;i++)
				jRptComboBox.addItem(MessageBundle.getMessage(reportMatrix[i][BUNDLE]));
			
			jRptComboBox.addActionListener(new ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (e.getActionCommand()!= null) {
						if (e.getActionCommand().equalsIgnoreCase("comboBoxChanged")) {
							selectAction();
						}
					}
				}
			});
			
			
			jMonthLabel = new JLabel();
			jMonthLabel.setText("        " + MessageBundle.getMessage("angal.stat.month"));
			
			jMonthComboBox = new JComboBox();
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.january"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.february"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.march"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.april"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.may"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.june"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.july"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.august"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.september"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.october"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.november"));
			jMonthComboBox.addItem(MessageBundle.getMessage("angal.stat.december"));

			jMonthComboBox.setSelectedIndex(month);

			jYearLabel = new JLabel();
			jYearLabel.setText("        " + MessageBundle.getMessage("angal.stat.year"));
			jYearComboBox = new JComboBox();

			for (int i=0;i<4;i++){
				jYearComboBox.addItem((year-i)+"");
			}
			
			jFromDateLabel = new JLabel();
			jFromDateLabel.setText(MessageBundle.getMessage("angal.stat.fromdate"));
			GregorianCalendar defaultDate = new GregorianCalendar();
			defaultDate.add(GregorianCalendar.DAY_OF_MONTH, -8);
			jFromDateField = new VoDateTextField("dd/mm/yyyy", defaultDate, 10);
			jToDateLabel = new JLabel();
			jToDateLabel.setText(MessageBundle.getMessage("angal.stat.todate"));
			defaultDate.add(GregorianCalendar.DAY_OF_MONTH, 7);
			jToDateField = new VoDateTextField("dd/mm/yyyy", defaultDate, 10);
			jToDateLabel.setVisible(false);
			jToDateField.setVisible(false);
			jFromDateLabel.setVisible(false);
			jFromDateField.setVisible(false);
			
			//jMonthPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
			jMonthPanel.add(jRptLabel, null);
			jMonthPanel.add(jRptComboBox, null);
			jMonthPanel.add(jMonthLabel, null);
			jMonthPanel.add(jMonthComboBox, null);
			jMonthPanel.add(jYearLabel, null);
			jMonthPanel.add(jYearComboBox, null);
			jMonthPanel.add(jFromDateLabel, null);
			jMonthPanel.add(jFromDateField, null);
			jMonthPanel.add(jToDateLabel, null);
			jMonthPanel.add(jToDateField, null);
		}
		return jMonthPanel;
	}


	protected void selectAction() {
		String sParType="";
		int rptIndex=jRptComboBox.getSelectedIndex();
		sParType = reportMatrix[rptIndex][TYPE];
		if (sParType.equalsIgnoreCase("twodates")) {
			jMonthComboBox.setVisible(false);
			jMonthLabel.setVisible(false);
			jYearComboBox.setVisible(false);
			jYearLabel.setVisible(false);
			jFromDateLabel.setVisible(true);
			jFromDateField.setVisible(true);
			jToDateLabel.setVisible(true);
			jToDateField.setVisible(true);
		}
		if (sParType.equalsIgnoreCase("twodatesfrommonthyear")) {
			jMonthComboBox.setVisible(true);
			jMonthLabel.setVisible(true);
			jYearComboBox.setVisible(true);
			jYearLabel.setVisible(true);
			jFromDateLabel.setVisible(false);
			jFromDateField.setVisible(false);
			jToDateLabel.setVisible(false);
			jToDateField.setVisible(false);
		}
		if (sParType.equalsIgnoreCase("monthyear")) {
			jMonthComboBox.setVisible(true);
			jMonthLabel.setVisible(true);
			jYearComboBox.setVisible(true);
			jYearLabel.setVisible(true);
			jFromDateLabel.setVisible(false);
			jFromDateField.setVisible(false);
			jToDateLabel.setVisible(false);
			jToDateField.setVisible(false);
		}
	}

	private JButton getJOkButton() {
		if (jOkButton == null) {
			jOkButton = new JButton();
			jOkButton.setBounds(new Rectangle(15, 15, 91, 31));
			jOkButton.setText(MessageBundle.getMessage("angal.stat.launchreport"));
			jOkButton.addActionListener(new ActionListener() {   
				public void actionPerformed(ActionEvent e) {
					generateReport(false);
				}
			});
		}
		return jOkButton;
	}
	
	private JButton getJCSVButton() {
		if (jCSVButton == null) {
			jCSVButton = new JButton();
			jCSVButton.setBounds(new Rectangle(15, 15, 91, 31));
			jCSVButton.setText("Excel");
			jCSVButton.addActionListener(new ActionListener() {   
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					generateReport(true);
				}
			});
		}
		return jCSVButton;
	}
	
	protected void generateReport(boolean toExcel) {
		   
		int rptIndex=jRptComboBox.getSelectedIndex();
		Integer month = jMonthComboBox.getSelectedIndex()+1;
		Integer year = (Integer.parseInt((String)jYearComboBox.getSelectedItem()));
		String fromDate=jFromDateField.getText().trim();
		String toDate=jToDateField.getText().trim();
		
		if (rptIndex>=0) {
			String sParType = reportMatrix[rptIndex][TYPE];
			if (sParType.equalsIgnoreCase("twodates")) {
				new GenericReportFromDateToDate(fromDate, toDate, reportMatrix[rptIndex][FILENAME], MessageBundle.getMessage(reportMatrix[rptIndex][BUNDLE]), toExcel);
				if (GeneralData.XMPPMODULEENABLED) {
					String user= (String)shareWith.getSelectedItem();
					CommunicationFrame frame= (CommunicationFrame)CommunicationFrame.getFrame();
					frame.sendMessage("011100100110010101110000011011110111001001110100 "+fromDate+" "+toDate+" "+reportMatrix[rptIndex][FILENAME],
							user, false);
				}
			}
			if (sParType.equalsIgnoreCase("twodatesfrommonthyear")) {
				GregorianCalendar d = new GregorianCalendar();
				d.set(GregorianCalendar.DAY_OF_MONTH,1 );
				d.set(GregorianCalendar.MONTH, month-1);
				d.set(GregorianCalendar.YEAR, year);
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				fromDate = sdf.format(d.getTime());
				d.set(GregorianCalendar.DAY_OF_MONTH, d.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
				toDate = sdf.format(d.getTime());
				new GenericReportFromDateToDate(fromDate, toDate, reportMatrix[rptIndex][FILENAME], MessageBundle.getMessage(reportMatrix[rptIndex][BUNDLE]), toExcel);
				if (GeneralData.XMPPMODULEENABLED) {
					String user= (String)shareWith.getSelectedItem();
					CommunicationFrame frame= (CommunicationFrame)CommunicationFrame.getFrame();
					frame.sendMessage("011100100110010101110000011011110111001001110100 "+fromDate+" "+toDate+" "+reportMatrix[rptIndex][FILENAME],
							user, false);
				}
			}
			if (sParType.equalsIgnoreCase("monthyear")) {
				new GenericReportMY(month, year, reportMatrix[rptIndex][FILENAME], MessageBundle.getMessage(reportMatrix[rptIndex][BUNDLE]), toExcel);
				if (GeneralData.XMPPMODULEENABLED) {
					String user= (String)shareWith.getSelectedItem();
					CommunicationFrame frame= (CommunicationFrame)CommunicationFrame.getFrame();
					frame.sendMessage("011100100110010101110000011011110111001001110100 "+month+" "+year+" "+reportMatrix[rptIndex][FILENAME],
							user, false);
				}
			}
		}
	
		
	}

	/*
	 * set a specific border+title to a panel
	 */
	private JPanel setMyBorder(JPanel c, String title) {
		javax.swing.border.Border b2 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(title), BorderFactory
						.createEmptyBorder(0, 0, 0, 0));
		c.setBorder(b2);
		return c;
	}

}  
