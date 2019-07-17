package org.isf.admission.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.admission.model.Admission;
import org.isf.dicom.gui.DicomGui;
import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.lab.manager.LabManager;
import org.isf.lab.model.Laboratory;
import org.isf.lab.service.LabIoOperations;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.manager.Context;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.patient.gui.PatientInsert;
import org.isf.patient.gui.PatientInsertExtended;
import org.isf.patient.gui.PatientSummary;
import org.isf.patient.model.Patient;
import org.isf.stat.gui.report.GenericReportAdmission;
import org.isf.stat.gui.report.GenericReportDischarge;
import org.isf.stat.gui.report.GenericReportOpd;
import org.isf.stat.gui.report.GenericReportPatient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.table.TableSorter;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;

/** 
* This class shows patient data and the list of admissions and lab exams.
* 
* last release  jun-14-08
* @author chiara
* 
*/

/*----------------------------------------------------------
 * modification history
 * ====================
 * 14/06/08 - chiara - first version
 *                     
 * 30/06/08 - fabrizio - implemented automatic selection of exams within the admission period
 * 
 * 05/09/08 - alessandro - second version:
 * 						 - same PatientSummary than PatientDataBrowser
 * 						 - includes OPD in the table
 -----------------------------------------------------------*/
public class PatientFolderBrowser extends ModalJFrame implements 
				PatientInsert.PatientListener, PatientInsertExtended.PatientListener, AdmissionBrowser.AdmissionListener  {



	/**
	 * 
	 */
	private static final long serialVersionUID = -3427327158197856822L;
	
	private EventListenerList deleteAdmissionListeners = new EventListenerList();

    public interface DeleteAdmissionListener extends EventListener {
        public void deleteAdmissionUpdated(AWTEvent e);
    }

    public void addDeleteAdmissionListener(DeleteAdmissionListener l) {
        deleteAdmissionListeners.add(DeleteAdmissionListener.class, l);
    }

    public void removeDeleteAdmissionListener(DeleteAdmissionListener listener) {
        deleteAdmissionListeners.remove(DeleteAdmissionListener.class, listener);
    }
	
	//---------------------------------------------------------------------
	
	public void patientInserted(AWTEvent e) {
	}

	public void patientUpdated(AWTEvent e) {
		jContentPane = null;
		initialize();		
	}
	
	public void admissionInserted(AWTEvent e) {
	}

	public void admissionUpdated(AWTEvent e) {
		jContentPane = null;
		initialize();		
	}
	
	

	private Patient patient = null;
	
	public PatientFolderBrowser(AdmittedPatientBrowser listener,  Patient myPatient) {
		super();
		patient = myPatient;
		initialize();
	}

	private void initialize() {

		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(MessageBundle.getMessage("angal.admission.patientdata")); //$NON-NLS-1$

		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	
	
	private JPanel jContentPane = null;

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getPatientDataPanel(), java.awt.BorderLayout.NORTH);
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	private JPanel patientData = null;
	
	private JPanel getPatientDataPanel(){
		patientData = new JPanel();
		patientData.setLayout(new BorderLayout());
		patientData.add(getTablesPanel(), BorderLayout.EAST);
		
		PatientSummary ps = new PatientSummary(patient);
		JPanel pp = ps.getPatientCompleteSummary();
		patientData.add(pp, BorderLayout.WEST);
		
		return patientData;
	}
	
	private ArrayList<Admission> admList;
	private ArrayList<Laboratory> labList;	
	private ArrayList<Disease> disease;
//	private ArrayList<Operation> operation;
	private ArrayList<Ward> ward;
	private ArrayList<Opd> opdList;
	
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.datem"),
			MessageBundle.getMessage("angal.admission.wards"),
			MessageBundle.getMessage("angal.admission.diagnosisinm"),
			MessageBundle.getMessage("angal.admission.diagnosisoutm"),
			MessageBundle.getMessage("angal.admission.statusm")
	}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	private int[] pColumwidth = {120, 150, 200, 200, 120 };
	
	private String[] plColums = {
			MessageBundle.getMessage("angal.common.datem"),
			MessageBundle.getMessage("angal.lab.examm"),
			MessageBundle.getMessage("angal.common.codem"),
			MessageBundle.getMessage("angal.lab.resultm")
	}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private int[] plColumwidth = { 150, 200, 50, 200 };

	private DefaultTableModel admModel;
	private DefaultTableModel labModel;
	TableSorter sorter;
	TableSorter sorterLab;
	
	//Alex: added sorters, for Java6 only
//	private TableRowSorter<AdmissionBrowserModel> adm_sorter;
//	private TableRowSorter<LabBrowserModel> lab_sorter;

	private JTable admTable;
	private JTable labTable;

	private JScrollPane scrollPane;
	private JScrollPane scrollPaneLab;
	
	private JPanel tablesPanel=null;
	
	final String DATE_FORMAT = "dd/MM/yy";
		
	private JPanel getTablesPanel(){
		tablesPanel = new JPanel(new BorderLayout());

		//Alex: added sorters, for Java6 only
//		admModel = new AdmissionBrowserModel();
//		admTable = new JTable(admModel);
	
		//Alex: Java5 compatible
		admModel = new AdmissionBrowserModel();
		sorter = new TableSorter(admModel);
		admTable = new JTable(sorter);      
		//sorter.addMouseListenerToHeaderInTable(admTable); no needed
		sorter.sortByColumn(0, false); //sort by first column, descending
		
		for (int i=0;i<pColums.length; i++){
			admTable.getColumnModel().getColumn(i).setPreferredWidth(pColumwidth[i]);
			if (i == 0 || i == 4) {
				admTable.getColumnModel().getColumn(i).setCellRenderer(new DateCellRenderer());
			}
		}
		
		scrollPane = new JScrollPane(admTable);
		scrollPane.setPreferredSize(new Dimension(500,200));
		tablesPanel.add(scrollPane, BorderLayout.CENTER);
		
		//Alex: added sorter, for Java6 only
//		adm_sorter = new TableRowSorter<AdmissionBrowserModel>((AdmissionBrowserModel) admTable.getModel());
//		for(int i=0; i < admTable.getColumnCount(); i++)
//			adm_sorter.setComparator(i, new TableSorter1());
//		admTable.setRowSorter(adm_sorter);
//		//Alex: perform auto sorting on date descending
//		ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
//		sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
//		adm_sorter.setSortKeys(sortKeys);
//		adm_sorter.sort();
		
		labModel = new LabBrowserModel();
		sorterLab = new TableSorter(labModel);
		labTable = new JTable(sorterLab);
		sorterLab.sortByColumn(0, false);
		
		for (int i=0;i<plColums.length; i++){
			labTable.getColumnModel().getColumn(i).setPreferredWidth(plColumwidth[i]);
		}
				
		scrollPaneLab = new JScrollPane(labTable);
		scrollPaneLab.setPreferredSize(new Dimension(500,200));
		tablesPanel.add(scrollPaneLab, BorderLayout.SOUTH);		
		
		ListSelectionModel listSelectionModel = admTable.getSelectionModel();
		listSelectionModel.addListSelectionListener(new ListSelectionListener() {
//			private Object Object;

			public void valueChanged(ListSelectionEvent e) {
				// Check that mouse has been released.
				if (!e.getValueIsAdjusting()) {
					GregorianCalendar startDate = null;
					GregorianCalendar endDate = null;
					int selectedRow = admTable.getSelectedRow();
					Object selectedObject = sorter.getValueAt(selectedRow, -1);
					Object selectedObject2;
										
					//String Selection = (String) admTable.getValueAt(selectedRow, 1);
					//selectedRow = admTable.convertRowIndexToModel(selectedRow); for Java6 only
					
					//if (Selection.compareTo("OPD") != 0) {
					if (selectedObject instanceof Admission) {
						
						Admission ad = (Admission) selectedObject;
						//Admission admission = (Admission) (((AdmissionBrowserModel) admModel)
							//	.getValueAt(selectedRow, -1));
						startDate = ad.getAdmDate();
						endDate = ad.getDisDate();
										
						
					} else {
						
						Opd opd2 = null;
						Admission ad2 = null;
						if (selectedRow > 0) {
							selectedObject2 = sorter.getValueAt(selectedRow-1, -1);
							if (selectedObject2 instanceof Opd) opd2 = (Opd) selectedObject2;
							else ad2 = (Admission) selectedObject2;
						}
							
						Opd opd = (Opd) selectedObject;
						//Opd opd = (Opd) (((AdmissionBrowserModel) admModel)
							//	.getValueAt(selectedRow, -1));
						startDate = opd.getVisitDate();
						if (opd2 != null) endDate = opd2.getVisitDate();
						if (ad2 != null) endDate = ad2.getAdmDate();
					}
					// Clear past selection, if any.
					labTable.clearSelection();
					for (int i = 0; i < labList.size(); i++) {
						//Laboratory laboratory = labList.get(i);
						Laboratory laboratory = (Laboratory) sorterLab.getValueAt(i, -1);
						Date examDate = laboratory.getExamDate().getTime();
						
						// Check that the exam date is included between admission date and discharge date.
						// If the patient has not been discharged yet (and then discharge date doesn't exist)
						// check only that the exam date is the same or after the admission date.
						// On true condition select the corresponding table row.
						if (!examDate.before(startDate.getTime()) &&
								(null == endDate ? true : !examDate.after(endDate.getTime())))  {
							
							labTable.addRowSelectionInterval(i, i);
							
						}
					}
				}
			}
		});
		
		return tablesPanel;
	}

	private JPanel getButtonPanel() {
		JPanel buttonPanel; 
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
			if (MainMenu.checkUserGrants("btnpatfoldopdrpt")) buttonPanel.add(getOpdReportButton(), null); //$NON-NLS-1$
			if (MainMenu.checkUserGrants("btnpatfoldadmrpt")) buttonPanel.add(getAdmReportButton(), null); //$NON-NLS-1$
			if (MainMenu.checkUserGrants("btnpatfoldadmrpt")) buttonPanel.add(getDisReportButton(), null); //$NON-NLS-1$
			if (MainMenu.checkUserGrants("btnpatfoldpatrpt")) buttonPanel.add(getLaunchReportButton(), null); //$NON-NLS-1$
            if (GeneralData.DICOMMODULEENABLED && MainMenu.checkUserGrants("btnpatfolddicom")) buttonPanel.add(getDICOMButton(), null); //$NON-NLS-1$
			buttonPanel.add(getCloseButton(), null);
		return buttonPanel;
	}

	private JButton opdReportButton = null;
	private JButton admReportButton = null;
	private JButton disReportButton = null;
	private JButton launchReportButton = null;
    private JButton dicomButton = null;
	private JButton closeButton=null;

	private JButton getOpdReportButton() {
		if (opdReportButton == null) {
			opdReportButton = new JButton();
			opdReportButton.setMnemonic(KeyEvent.VK_O);
			opdReportButton.setText(MessageBundle.getMessage("angal.admission.patientfolder.opdchart")); //$NON-NLS-1$
			opdReportButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (admTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(PatientFolderBrowser.this, MessageBundle.getMessage("angal.common.pleaseselectarow"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
					
					int selectedRow = admTable.getSelectedRow();
					Object selectedObj = sorter.getValueAt(selectedRow, -1);

					if (selectedObj instanceof Opd) {
						
						Opd opd = (Opd) sorter.getValueAt(selectedRow, -1);
						new GenericReportOpd(opd.getCode(), opd.getPatient().getCode(), GeneralData.OPDCHART);				
					} else {
						JOptionPane.showMessageDialog(PatientFolderBrowser.this, MessageBundle.getMessage("angal.admission.patientfolder.pleaseselectanopd"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
				}
			});
		}
		return opdReportButton;
	}
	
	private JButton getDisReportButton() {
		if (disReportButton == null) {
			disReportButton = new JButton();
			disReportButton.setMnemonic(KeyEvent.VK_S);
			disReportButton.setText(MessageBundle.getMessage("angal.admission.patientfolder.dischart")); //$NON-NLS-1$
			disReportButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (admTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(PatientFolderBrowser.this, MessageBundle.getMessage("angal.common.pleaseselectarow"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
					
					int selectedRow = admTable.getSelectedRow();
					Object selectedObj = sorter.getValueAt(selectedRow, -1);

					if (selectedObj instanceof Admission) {
						
						Admission adm = (Admission) sorter.getValueAt(selectedRow, -1);
						if (adm.getDisDate() == null) {
							JOptionPane.showMessageDialog(PatientFolderBrowser.this, MessageBundle.getMessage("angal.admission.patientfolder.thepatientisnotyetdischarged"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
							return;
						}
						new GenericReportDischarge(adm.getId(), adm.getPatient().getCode(), GeneralData.DISCHART);				
					} else {
						JOptionPane.showMessageDialog(PatientFolderBrowser.this, MessageBundle.getMessage("angal.admission.patientfolder.pleaseselectanadmission"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
				}
			});
		}
		return disReportButton;
	}
	
	private JButton getAdmReportButton() {
		if (admReportButton == null) {
			admReportButton = new JButton();
			admReportButton.setMnemonic(KeyEvent.VK_A);
			admReportButton.setText(MessageBundle.getMessage("angal.admission.patientfolder.admchart")); //$NON-NLS-1$
			admReportButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (admTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(PatientFolderBrowser.this, MessageBundle.getMessage("angal.common.pleaseselectarow"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
					
					int selectedRow = admTable.getSelectedRow();
					Object selectedObj = sorter.getValueAt(selectedRow, -1);

					if (selectedObj instanceof Admission) {
						
						Admission adm = (Admission) sorter.getValueAt(selectedRow, -1);
						new GenericReportAdmission(adm.getId(), adm.getPatient().getCode(), GeneralData.ADMCHART);				
					} else {
						JOptionPane.showMessageDialog(PatientFolderBrowser.this, MessageBundle.getMessage("angal.admission.patientfolder.pleaseselectanadmission"), //$NON-NLS-1$
								MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
						return;
					}
				}
			});
		}
		return admReportButton;
	}
	
	private JButton getLaunchReportButton() {
		if (launchReportButton == null) {
			launchReportButton = new JButton();
			launchReportButton.setMnemonic(KeyEvent.VK_R);
			launchReportButton.setText(MessageBundle.getMessage("angal.admission.patientfolder.launchreport")); //$NON-NLS-1$
			launchReportButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// GenericReportMY rpt3 = new GenericReportMY(new Integer(6), new Integer(2008), "hmis108_adm_by_diagnosis_in");
					new GenericReportPatient(patient.getCode(), GeneralData.PATIENTSHEET);
				}
			});
		}
		return launchReportButton;
	}

    DicomGui dg = null;
    
    public void resetDicomViewer()
    {
        dg = null;
    }

    private JButton getDICOMButton() {
		if (dicomButton == null) {
			dicomButton = new JButton();
			dicomButton.setMnemonic(KeyEvent.VK_D);
			dicomButton.setText(MessageBundle.getMessage("angal.admission.patientfolder.dicom")); //$NON-NLS-1$
			dicomButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e)
                {
                        if (dg==null)
                            dg = new DicomGui(patient, PatientFolderBrowser.this);
                }
			});
		}
		return dicomButton;
	}

    
	
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.setText(MessageBundle.getMessage("angal.common.close"));   //$NON-NLS-1$
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return closeButton;
	}

		
	
	class AdmissionBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -453243229156512947L;

		public AdmissionBrowserModel() {
			AdmissionBrowserManager manager = new AdmissionBrowserManager();
			DiseaseBrowserManager dbm = new DiseaseBrowserManager();
			WardBrowserManager wbm = new WardBrowserManager();
			OpdBrowserManager opd = new OpdBrowserManager();
			try {
				admList = manager.getAdmissions(patient);
			}catch(OHServiceException e){
                OHServiceExceptionUtil.showMessages(e);
			}
			try {
				disease = dbm.getDiseaseAll();
			}catch(OHServiceException e){
                OHServiceExceptionUtil.showMessages(e);
			}
			try {
				ward = wbm.getWards();
			}catch(OHServiceException e){
                OHServiceExceptionUtil.showMessages(e);
			}
			try {
				opdList = opd.getOpdList(patient.getCode());
			}catch(OHServiceException e){
                OHServiceExceptionUtil.showMessages(e);
			}
		}
		
		
		public int getRowCount() {
			if (admList == null && opdList == null)
				return 0;
			
			return admList.size() + opdList.size();
		}

		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}	
		
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				if (r < admList.size())	{
					return admList.get(r);
				} else {
					int z = r - admList.size();
					return opdList.get(z);
				}
			
			} else if (c == 0) {
				if (r < admList.size()) {
					Date myDate = (admList.get(r)).getAdmDate().getTime();	
					return myDate;
				} else {
					int z = r - admList.size();
					Date myDate = (opdList.get(z)).getVisitDate().getTime();
					return myDate;
				}
				
			} else if (c == 1) {				
				if (r < admList.size()) {
					String id = admList.get(r).getWard().getCode();
					for (Ward elem : ward) {
						if (elem.getCode().equalsIgnoreCase(id))
							return elem.getDescription();
					}
				} else {
					return MessageBundle.getMessage("angal.admission.patientfolder.opd"); //$NON-NLS-1$
				}
			}
			else if (c == 2) {
				String id = null;
				if (r < admList.size()) {
					id = admList.get(r).getDiseaseIn().getCode();
					if (id == null){
						id = ""; //$NON-NLS-1$
					}	
				} else {
					int z = r - admList.size();
					id = opdList.get(z).getDisease().getCode();
					if (id == null){
						id = ""; //$NON-NLS-1$
					}
				}
				for (Disease elem : disease) {
					if (elem.getCode().equalsIgnoreCase(id))
						return elem.getDescription();
				}				
				return MessageBundle.getMessage("angal.admission.nodisease"); //$NON-NLS-1$
	
			}else if (c == 3) {
				String id = null;
				if (r < admList.size()) {
					id = admList.get(r).getDiseaseOut1() == null ? null :  admList.get(r).getDiseaseOut1().getCode();
					if (id == null){
						id = ""; //$NON-NLS-1$
					}
				} else {
					int z = r - admList.size();
					Disease dis = opdList.get(z).getDisease3();
					if (dis == null){
						dis = opdList.get(z).getDisease2();
						if (dis == null){
							id = opdList.get(z).getDisease().getCode();
						} else {
							id = dis.getCode();
						}
					} else {
						id = dis.getCode();
					}
				}
				for (Disease elem : disease) {
					if (elem.getCode().equalsIgnoreCase(id))
						return elem.getDescription();
				}				
				return MessageBundle.getMessage("angal.admission.nodisease"); //$NON-NLS-1$
				
			}  else if (c == 4) {
				if (r < admList.size()) {
					if (admList.get(r).getDisDate()==null)
						return MessageBundle.getMessage("angal.admission.present"); //$NON-NLS-1$
					else {
						Date myDate = admList.get(r).getDisDate().getTime();
						return myDate;
					}
				} else {
					int z = r - admList.size();
					String status = "" + opdList.get(z).getNewPatient();
					return (status.compareTo("R")==0?MessageBundle.getMessage("angal.opd.reattendance"):MessageBundle.getMessage("angal.opd.newattendance")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			} 
			
			return null;
		}




		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
	}
	
	class LabBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8245833681073162426L;

		public LabBrowserModel() {
			LabManager lbm = new LabManager(Context.getApplicationContext().getBean(LabIoOperations.class));
			try {
				labList = lbm.getLaboratory(patient);
			} catch (OHServiceException e) {
				labList = new ArrayList<Laboratory>();
                OHServiceExceptionUtil.showMessages(e);
			}
		}
		
		public int getRowCount() {
			if (labList == null)
				return 0;
			return labList.size();
		}

		public String getColumnName(int c) {
			return plColums[c];
		}

		public int getColumnCount() {
			return plColums.length;
		}	
		
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return labList.get(r);
			} else if (c == 0) {
				//System.out.println(labList.get(r).getExam().getExamtype().getDescription());
				
				Date examDate = labList.get(r).getExamDate().getTime();	
				return examDate;
			} else if (c == 1) {
				return labList.get(r).getExam().getDescription();
			}else if (c == 2) {
				return labList.get(r).getCode();
			} else if (c == 3) {
				return labList.get(r).getResult();
			}
			
			return null;
		}




		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
	}
	
	public class DateCellRenderer extends DefaultTableCellRenderer {
		/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
				super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
				
				if ( value instanceof Date ){
				// Use SimpleDateFormat class to get a formatted String from Date object.
				String strDate = new SimpleDateFormat(DATE_FORMAT).format((Date)value);
				
				// Sorting algorithm will work with model value. So you dont need to worry
				// about the renderer's display value. 
				this.setText( strDate );
				}
				return this;
			}
		}
	
}// class
