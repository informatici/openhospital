package org.isf.opd.gui;

/*------------------------------------------
 * OpdEdit - add/edit an OPD registration
 * -----------------------------------------
 * modification history
 * 11/12/2005 - Vero, Rick  - first beta version 
 * 07/11/2006 - ross - renamed from Surgery 
 *                   - added visit date, disease 2, diseas3
 *                   - disease is not mandatory if re-attendance
 * 			         - version is now 1.0 
 * 28/05/2008 - ross - added referral to / referral from check boxes
 * 12/06/2008 - ross - added patient data
 * 					 - fixed error on checking "male"/"female" option: should check after translation
 * 					 - version is not a resource into the boundle, is locale to the form
 *                   - form rearranged in x,y coordinates 
 * 			         - version is now 1.1 
 * 26/08/2008 - teo  - added patient chooser 
 * 01/09/2008 - alex - added constructor for call from Admission
 * 					 - set Patient oriented OPD
 * 					 - history management for the patients
 * 					 - version now is 1.2
 * 01/01/2009 - Fabrizio - modified age fields back to Integer type
 * 13/02/2009 - Alex - added possibility to edit patient through EditButton
 * 					   added Edit.png icon
 * 					   fixed a bug on the first element in the comboBox
 * 13/02/2009 - Alex - added trash button for resetting searchfield
 * 03/13/2009 - Alex - lastOpdVisit appears at the bottom
 * 					   added control on duplicated diseases
 * 					   added re-attendance checkbox for a clear view
 * 					   new/re-attendance managed freely
 * 07/13/2009 - Alex - note field for the visit recall last visit note when start OPD from
	  				   Admission and added Note even in Last OPD Visit
	  				   Extended patient search to patient code
 *------------------------------------------*/


import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;

import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.examination.gui.PatientExaminationEdit;
import org.isf.examination.manager.ExaminationBrowserManager;
import org.isf.examination.model.GenderPatientExamination;
import org.isf.examination.model.PatientExamination;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.patient.gui.PatientInsert;
import org.isf.patient.gui.PatientInsertExtended;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;
import org.isf.utils.time.TimeTools;

import com.toedter.calendar.JDateChooser;

public class OpdEditExtended extends JDialog implements 
        PatientInsertExtended.PatientListener, PatientInsert.PatientListener, ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void patientInserted(AWTEvent e) {
		opdPatient = (Patient) e.getSource();
		setPatient(opdPatient);
		jComboPatResult.addItem(opdPatient);
		jComboPatResult.setSelectedItem(opdPatient);
		jPatientEditButton.setEnabled(true);
	}

	public void patientUpdated(AWTEvent e) {
		setPatient(opdPatient);
	}

	private EventListenerList surgeryListeners = new EventListenerList();
	
	public interface SurgeryListener extends EventListener {
		public void surgeryUpdated(AWTEvent e, Opd opd);
		public void surgeryInserted(AWTEvent e, Opd opd);
	}
	
	public void addSurgeryListener(SurgeryListener l) {
		surgeryListeners.add(SurgeryListener.class, l);
	}
	
	public void removeSurgeryListener(SurgeryListener listener) {
		surgeryListeners.remove(SurgeryListener.class, listener);
	}
	
	private void fireSurgeryInserted(Opd opd) {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryInserted(event, opd);
	}
	private void fireSurgeryUpdated(Opd opd) {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryUpdated(event, opd);
	}
	
	private static final String VERSION="1.3"; 

	private static final String LastOPDLabel = "<html><i>"+MessageBundle.getMessage("angal.opd.lastopdvisitm")+"</i></html>:";
	private static final String LastNoteLabel = "<html><i>"+MessageBundle.getMessage("angal.opd.lastopdnote")+"</i></html>:";
	
	private JPanel jPanelMain = null;
	private JPanel jPanelNorth;
	private JPanel jPanelCentral;
	private JPanel jPanelData = null;
	private JPanel jPanelButtons = null;
	private JLabel jLabelDate = null;
	private JLabel jLabelDiseaseType1 = null;
	private JLabel jLabelDisease1 = null;
	private JLabel jLabelDis2 = null;
	private JLabel jLabelDis3 = null;

	private JComboBox diseaseTypeBox = null;
	private JComboBox diseaseBox1 = null;
	private JComboBox diseaseBox2 = null;
	private JComboBox diseaseBox3 = null;
	private JLabel jLabelAge = null;
	private JLabel jLabelSex = null;
	private GregorianCalendar visitDateOpd = null;
	private DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
	private JDateChooser OpdDateFieldCal = null; 
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JButton jButtonExamination = null;
	private JCheckBox rePatientCheckBox = null;
	private JCheckBox newPatientCheckBox = null;
	private JCheckBox referralToCheckBox = null;
	private JCheckBox referralFromCheckBox = null;
	private JPanel jPanelSex = null;
	private ButtonGroup group=null;

	private JLabel jLabelfirstName = null;
	private JLabel jLabelsecondName = null;
	private JLabel jLabeladdress = null;
	private JLabel jLabelcity = null;
	private JLabel jLabelnextKin = null;

	private JPanel jPanelPatient = null;

	private VoLimitedTextField jFieldFirstName= null;
	private VoLimitedTextField jFieldSecondName= null;
	private VoLimitedTextField jFieldAddress= null;
	private VoLimitedTextField jFieldCity= null;
	private VoLimitedTextField jFieldNextKin= null;
	private VoLimitedTextField jFieldAge = null;

	private Opd opd;
	private boolean insert;
	private DiseaseType allType= new DiseaseType(MessageBundle.getMessage("angal.opd.alltype"),MessageBundle.getMessage("angal.opd.alltype"));

	private VoLimitedTextField jTextPatientSrc;
	private JComboBox jComboPatResult;
	private JLabel jSearchLabel = null;
	private JRadioButton radiof;
	private JRadioButton radiom;
	private JButton jPatientEditButton = null;
	private JButton jSearchButton = null;
	private JLabel jLabelLastOpdVisit = null;
	private JLabel jFieldLastOpdVisit = null;
	private JLabel jLabelLastOpdNote = null;
	private JLabel jFieldLastOpdNote = null;
	
	private Patient opdPatient = null;
	private JPanel jNotePanel = null;
	private JScrollPane jNoteScrollPane = null;
	private JTextArea jNoteTextArea = null;
	private JPanel jPatientNotePanel = null;
	private JScrollPane jPatientScrollNote = null;
	private JTextArea jPatientNote = null;
	private JPanel jOpdNumberPanel = null;
	private JTextField jOpdNumField = null;
	private JLabel jOpdNumLabel = null;
	
	/*
	 * Managers and Arrays
	 */
	private DiseaseTypeBrowserManager typeManager = Context.getApplicationContext().getBean(DiseaseTypeBrowserManager.class);
	private DiseaseBrowserManager manager = Context.getApplicationContext().getBean(DiseaseBrowserManager.class);
	private ArrayList<DiseaseType> types;
	private ArrayList<Disease> diseasesOPD;
	private ArrayList<Disease> diseasesAll;
	private OpdBrowserManager opdManager = Context.getApplicationContext().getBean(OpdBrowserManager.class);
	private ArrayList<Opd> opdArray = new ArrayList<Opd>();
	
	
	private PatientBrowserManager patBrowser = Context.getApplicationContext().getBean(PatientBrowserManager.class);
	private ArrayList<Patient> pat = new ArrayList<Patient>();

	private Disease lastOPDDisease1;
	private JLabel JlabelOpd;
        
    /*
     * Adds: Textfields and buttoms to enable search in diognoses 
     */
    private JTextField searchDiseaseTextField;
    private JTextField searchDiseaseTextField2;
    private JTextField searchDiseaseTextField3;
    private JButton searchDiseaseButton;
    private JButton searchDiseaseButton2;
    private JButton searchDiseaseButton3;
        
	/**
	 * This method initializes 
	 * @wbp.parser.constructor
	 * 
	 */
	public OpdEditExtended(JFrame owner, Opd old, boolean inserting) {
		super(owner, true);
		opd=old;
		insert=inserting;
		try{
			types = typeManager.getDiseaseType();
			diseasesOPD = manager.getDiseaseOpd();
			diseasesAll = manager.getDiseaseAll();
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		try{
			if(!insert) {
				opdPatient = opd.getPatient();
				if (opdPatient != null && opd.getPatient().getCode() != 0) { 
					PatientBrowserManager patBrowser = Context.getApplicationContext().getBean(PatientBrowserManager.class);
					opdPatient = patBrowser.getPatientAll(opd.getPatient().getCode());
				} else { //old OPD has no PAT_ID => Create Patient from OPD
					opdPatient = new Patient(opd);
					opdPatient.setCode(0);
				}
			}
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		initialize();
	}
	
	public OpdEditExtended(JFrame owner, Opd opd, Patient patient, boolean inserting) {
		super(owner, true);
		this.opd = opd;
		opdPatient = patient;
		insert=inserting;
		try{
			types = typeManager.getDiseaseType();
			diseasesOPD = manager.getDiseaseOpd();
			diseasesAll = manager.getDiseaseAll();
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		try{
			if(!insert) {
				opdPatient = opd.getPatient();
				if (opdPatient != null && opd.getPatient().getCode() != 0) { 
					PatientBrowserManager patBrowser = Context.getApplicationContext().getBean(PatientBrowserManager.class);
					opdPatient = patBrowser.getPatientAll(opd.getPatient().getCode());
				} else { //old OPD has no PAT_ID => Create Patient from OPD
					opdPatient = new Patient(opd);
					opdPatient.setCode(0);
				}
			}
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		initialize();
	}
	
	private void setPatient(Patient p) {
			jFieldAge.setText(TimeTools.getFormattedAge(p.getBirthDate()));
			jFieldFirstName.setText(p.getFirstName());
			jFieldAddress.setText(p.getAddress());
			jFieldCity.setText(p.getCity());
			jFieldSecondName.setText(p.getSecondName());
			jFieldNextKin.setText(p.getNextKin());
			jPatientNote.setText(opdPatient.getNote());
			setMyMatteBorder(jPanelPatient, MessageBundle.getMessage("angal.opd.patient") + " (code: " + opdPatient.getCode() + ")");
			if(p.getSex() == 'M') {
				radiom.setSelected(true);				
			} else if(p.getSex() == 'F') {
				radiof.setSelected(true);			
			}
			if (insert) getLastOpd(p.getCode());
	}
	
	private void resetPatient() {
		jFieldAge.setText("");
		jFieldFirstName.setText("");
		jFieldAddress.setText("");
		jFieldCity.setText("");
		jFieldSecondName.setText("");
		jFieldNextKin.setText("");
		jPatientNote.setText("");
		setMyMatteBorder(jPanelPatient, MessageBundle.getMessage("angal.opd.patient"));
		radiom.setSelected(true);
		opdPatient=null;
	}
	
	//Alex: Resetting history from the last OPD visit for the patient
	private boolean getLastOpd(int code)
	{
		Opd lastOpd = null;
		try {
			lastOpd = opdManager.getLastOpd(code);
		}catch(OHServiceException e){
			OHServiceExceptionUtil.showMessages(e);
		}
		
		if (lastOpd == null) {
			newPatientCheckBox.setSelected(true);
			rePatientCheckBox.setSelected(false);
			jLabelLastOpdVisit.setText("");
			jFieldLastOpdVisit.setText("");
			jLabelLastOpdNote.setText("");
			jFieldLastOpdNote.setText("");
			jNoteTextArea.setText("");
			
			return false;
		}
		
		lastOPDDisease1 = null;
		Disease lastOPDDisease2 = null;
		Disease lastOPDDisease3 = null;
		
		for (Disease disease : diseasesOPD) {
			
			if (lastOpd.getDisease() != null && disease.getCode().compareTo(lastOpd.getDisease().getCode()) == 0) {
					lastOPDDisease1 = disease;
			}
			if (lastOpd.getDisease2() != null && disease.getCode().compareTo(lastOpd.getDisease2().getCode()) == 0) {
				lastOPDDisease2 = disease;
			}
			if (lastOpd.getDisease3() != null && disease.getCode().compareTo(lastOpd.getDisease3().getCode()) == 0) {
				lastOPDDisease3 = disease;
			}
		}
		
		StringBuilder lastOPDDisease = new StringBuilder();
		lastOPDDisease.append(MessageBundle.getMessage("angal.opd.on")).append(" ").append(currentDateFormat.format(lastOpd.getVisitDate().getTime())).append(" - ");
		if (lastOPDDisease1 != null) {
			setAttendance();
			lastOPDDisease.append(lastOPDDisease1.getDescription());
		} 
		if (lastOPDDisease2 != null) lastOPDDisease.append(", ").append(lastOPDDisease2.getDescription());
		if (lastOPDDisease3 != null) lastOPDDisease.append(", ").append(lastOPDDisease3.getDescription());
		jLabelLastOpdVisit.setText(LastOPDLabel);
		jFieldLastOpdVisit.setText(lastOPDDisease.toString());
		jLabelLastOpdNote.setText(LastNoteLabel);
		String note = lastOpd.getNote();
		jFieldLastOpdNote.setText(note.equals("") ? MessageBundle.getMessage("angal.opd.nonote") : note);
		jNoteTextArea.setText(lastOpd.getNote());
		
		return true;		
	}
	
	private void setAttendance() {
		if (!insert) return;
		Object selectedObject = diseaseBox1.getSelectedItem();
		if(selectedObject instanceof Disease) {
			Disease disease = (Disease) selectedObject;
			if (lastOPDDisease1 != null && disease.getCode().equals(lastOPDDisease1.getCode())) {
				rePatientCheckBox.setSelected(true);
				newPatientCheckBox.setSelected(false);
			} else {
				rePatientCheckBox.setSelected(false);
				newPatientCheckBox.setSelected(true);
			}
		}
	}

	/**
	 * @return the jPanelNorth
	 */
	private JPanel getjPanelNorth() {
		if (jPanelNorth == null) {
			String referralTo="";
			String referralFrom="";
			jPanelNorth = new JPanel(new FlowLayout());
			rePatientCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.reattendance"));
			newPatientCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.newattendance"));
			newPatientCheckBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (newPatientCheckBox.isSelected()) {
						newPatientCheckBox.setSelected(true);
						rePatientCheckBox.setSelected(false);
					} else {
						newPatientCheckBox.setSelected(false);
						rePatientCheckBox.setSelected(true);
					}
				}
			});
			rePatientCheckBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (rePatientCheckBox.isSelected()) {
						rePatientCheckBox.setSelected(true);
						newPatientCheckBox.setSelected(false);
					} else {
						newPatientCheckBox.setSelected(true);
						rePatientCheckBox.setSelected(false);
					}
				}
			});
			jPanelNorth.add(rePatientCheckBox);
			jPanelNorth.add(newPatientCheckBox);
			if(!insert){
				if (opd.getNewPatient() == 'N')
					newPatientCheckBox.setSelected(true);
				else
					rePatientCheckBox.setSelected(true);
			}
			referralFromCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.referral.from"));
			jPanelNorth.add(referralFromCheckBox);
			if(!insert){
				referralFrom = opd.getReferralFrom();
				if (referralFrom == null) referralFrom="";
				if (referralFrom.equals("R"))referralFromCheckBox.setSelected(true);
			}
			referralToCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.referral.to"));
			jPanelNorth.add(referralToCheckBox);
			if(!insert){
				referralTo = opd.getReferralTo();
				if (referralTo == null) referralTo="";
				if (referralTo.equals("R"))referralToCheckBox.setSelected(true);
			}
		}
		return jPanelNorth;
	}

	/**
	 * @return the jPanelCentral
	 */
	private JPanel getjPanelCentral() {
		if (jPanelCentral == null) {
			jPanelCentral = new JPanel();
			jPanelCentral.setLayout(new BoxLayout(jPanelCentral, BoxLayout.Y_AXIS));
			jPanelCentral.add(getDataPanel());
			jPanelCentral.add(Box.createVerticalStrut(10));
			jPanelCentral.add(getJPanelPatient());
		}
		return jPanelCentral;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setContentPane(getMainPanel());
		pack();
		setMinimumSize(this.getSize());
		setLocationRelativeTo(null);

		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.opd.newopdregistration")+"("+VERSION+")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.opd.editopdregistration")+"("+VERSION+")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//this.setVisible(true);
		if (insert) {
			jTextPatientSrc.requestFocusInWindow();
		} else {
			jNoteTextArea.requestFocusInWindow();
		}
		this.addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				//to free memory
				pat.clear();
				opdArray.clear();
				diseasesAll.clear();
				diseasesOPD.clear();
				types.clear();
				jComboPatResult.removeAllItems();
				diseaseTypeBox.removeAllItems();
				diseaseBox1.removeAllItems();
				diseaseBox2.removeAllItems();
				diseaseBox3.removeAllItems();
				dispose();
			}			
		});
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (jPanelMain == null) {
			jPanelMain = new JPanel();
			jPanelMain.setLayout(new BorderLayout());
			jPanelMain.add(getjPanelNorth(), BorderLayout.NORTH);
			jPanelMain.add(getJNotePanel(), BorderLayout.EAST);
			jPanelMain.add(getjPanelCentral(), BorderLayout.CENTER);
			jPanelMain.add(getJButtonPanel(), BorderLayout.SOUTH);
		}
		return jPanelMain;
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDataPanel() {
		if (jPanelData == null) {
			jPanelData = new JPanel();
			GridBagLayout gbl_jPanelData = new GridBagLayout();
			gbl_jPanelData.columnWidths = new int[] {80,40,20,80,20};
			gbl_jPanelData.rowHeights = new int[]{20,20,20,20,20,20,20,20};
			gbl_jPanelData.columnWeights = new double[]{0.0, 0.1, 0.0, 1.0, 0.0};
			gbl_jPanelData.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			jPanelData.setLayout(gbl_jPanelData);
			
			jLabelDate= new JLabel(MessageBundle.getMessage("angal.opd.attendancedate"));
			GridBagConstraints gbc_jLabelDate = new GridBagConstraints();
			gbc_jLabelDate.fill = GridBagConstraints.VERTICAL;
			gbc_jLabelDate.anchor = GridBagConstraints.WEST;
			gbc_jLabelDate.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDate.gridy = 0;
			gbc_jLabelDate.gridx = 0;
			jPanelData.add(jLabelDate, gbc_jLabelDate);
			GridBagConstraints gbc_DateFieldCal = new GridBagConstraints();
			gbc_DateFieldCal.weightx = 0.5;
			gbc_DateFieldCal.fill = GridBagConstraints.HORIZONTAL;
			gbc_DateFieldCal.insets = new Insets(5, 5, 5, 5);
			gbc_DateFieldCal.gridy = 0;
			gbc_DateFieldCal.gridx = 1;
			jPanelData.add(getOpdDateFieldCal(), gbc_DateFieldCal);
			GridBagConstraints gbc_jOpdNumberPanel = new GridBagConstraints();
			gbc_jOpdNumberPanel.weightx = 0.5;
			gbc_jOpdNumberPanel.gridwidth = 1;
			gbc_jOpdNumberPanel.fill = GridBagConstraints.BOTH;
			gbc_jOpdNumberPanel.insets = new Insets(5, 5, 5, 5);
			gbc_jOpdNumberPanel.gridy = 0;			
			gbc_jOpdNumberPanel.gridx = 3;
			
			jPanelData.add(getJOpdNumberPanel(), gbc_jOpdNumberPanel);
			GridBagConstraints gbc_JlabelOpd = new GridBagConstraints();
			gbc_JlabelOpd.insets = new Insets(0, 0, 5, 0);
			gbc_JlabelOpd.gridx = 4;
			gbc_JlabelOpd.gridy = 0;
			jPanelData.add(getJlabelOpd(), gbc_JlabelOpd);
			jSearchLabel = new JLabel(MessageBundle.getMessage("angal.opd.search"));
			GridBagConstraints gbc_jSearchLabel = new GridBagConstraints();
			gbc_jSearchLabel.fill = GridBagConstraints.VERTICAL;
			gbc_jSearchLabel.anchor = GridBagConstraints.WEST;
			gbc_jSearchLabel.insets = new Insets(5, 5, 5, 5);
			gbc_jSearchLabel.gridy = 1;
			gbc_jSearchLabel.gridx = 0;
			jPanelData.add(jSearchLabel, gbc_jSearchLabel);
			GridBagConstraints gbc_jTextPatientSrc = new GridBagConstraints();
			gbc_jTextPatientSrc.weightx = 0.5;
			gbc_jTextPatientSrc.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextPatientSrc.insets = new Insets(5, 5, 5, 5);
			gbc_jTextPatientSrc.gridy = 1;
			gbc_jTextPatientSrc.gridx = 1;
			jPanelData.add(getJTextPatientSrc(), gbc_jTextPatientSrc);
			GridBagConstraints gbc_jSearchButton = new GridBagConstraints();
			gbc_jSearchButton.insets = new Insets(5, 5, 5, 5);
			gbc_jSearchButton.gridy = 1;
			gbc_jSearchButton.gridx = 2;
			jPanelData.add(getJSearchButton(), gbc_jSearchButton);
			GridBagConstraints gbc_jSearchBox = new GridBagConstraints();
			gbc_jSearchBox.weightx = 0.5;
			gbc_jSearchBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSearchBox.insets = new Insets(5, 5, 5, 5);
			gbc_jSearchBox.gridy = 1;
			gbc_jSearchBox.gridx = 3;
			jPanelData.add(getSearchBox(), gbc_jSearchBox);
			GridBagConstraints gbc_jPatientEditButton = new GridBagConstraints();
			gbc_jPatientEditButton.insets = new Insets(5, 5, 5, 0);
			gbc_jPatientEditButton.gridy = 1;
			gbc_jPatientEditButton.gridx = 4;
			jPanelData.add(getJPatientEditButton(), gbc_jPatientEditButton);
			
			jLabelDiseaseType1 = new JLabel(MessageBundle.getMessage("angal.opd.diseasetype"));
			GridBagConstraints gbc_jLabelDiseaseType1 = new GridBagConstraints();
			gbc_jLabelDiseaseType1.fill = GridBagConstraints.VERTICAL;
			gbc_jLabelDiseaseType1.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDiseaseType1.anchor = GridBagConstraints.WEST;
			gbc_jLabelDiseaseType1.gridy = 2;
			gbc_jLabelDiseaseType1.gridx = 0;
			jPanelData.add(jLabelDiseaseType1, gbc_jLabelDiseaseType1);
			GridBagConstraints gbc_jLabelDiseaseTypeBox = new GridBagConstraints();
			gbc_jLabelDiseaseTypeBox.insets = new Insets(5, 5, 5, 0);
			gbc_jLabelDiseaseTypeBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelDiseaseTypeBox.gridwidth = 3;
			gbc_jLabelDiseaseTypeBox.gridy = 2;
			gbc_jLabelDiseaseTypeBox.gridx = 1;
			jPanelData.add(getDiseaseTypeBox(), gbc_jLabelDiseaseTypeBox);
			
			jLabelDisease1 = new JLabel(MessageBundle.getMessage("angal.opd.diagnosis"));
			GridBagConstraints gbc_jLabelDisease1 = new GridBagConstraints();
			gbc_jLabelDisease1.fill = GridBagConstraints.VERTICAL;
			gbc_jLabelDisease1.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDisease1.anchor = GridBagConstraints.WEST;
			gbc_jLabelDisease1.gridy = 3;
			gbc_jLabelDisease1.gridx = 0;
            jPanelData.add(jLabelDisease1, gbc_jLabelDisease1);
            /////////////Seach text field/////////////
            GridBagConstraints gbc_searchDiseaseTextField = new GridBagConstraints();
			gbc_searchDiseaseTextField.weightx = 0.5;
			gbc_searchDiseaseTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_searchDiseaseTextField.insets = new Insets(5, 5, 5, 5);
			gbc_searchDiseaseTextField.gridy = 3;
			gbc_searchDiseaseTextField.gridx = 1;
            searchDiseaseTextField = new JTextField(10);
            searchDiseaseTextField.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        searchDiseaseButton.doClick();
                    }
                }
                public void keyReleased(KeyEvent e) {}
                public void keyTyped(KeyEvent e) {}
            });
			jPanelData.add(searchDiseaseTextField, gbc_searchDiseaseTextField);
            /////////////Seach text button/////////////
            GridBagConstraints gbc_searchDiseaseButton = new GridBagConstraints();
			gbc_searchDiseaseButton.insets = new Insets(5, 5, 5, 5);
			gbc_searchDiseaseButton.gridy = 3;
			gbc_searchDiseaseButton.gridx = 2;
            searchDiseaseButton = new JButton();
            searchDiseaseButton.setPreferredSize(new Dimension(20, 20));
            searchDiseaseButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
            searchDiseaseButton.addActionListener(this);
			jPanelData.add(searchDiseaseButton, gbc_searchDiseaseButton);
            /////////////Disesases combo/////////////
			GridBagConstraints gbc_jLabelDiseaseBox = new GridBagConstraints();
			gbc_jLabelDiseaseBox.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDiseaseBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelDiseaseBox.weightx = 0.5;
			gbc_jLabelDiseaseBox.gridy = 3;
			gbc_jLabelDiseaseBox.gridx = 3;
			jPanelData.add(getDiseaseBox1(), gbc_jLabelDiseaseBox);
			
			jLabelDis2 = new JLabel(MessageBundle.getMessage("angal.opd.diagnosisnfulllist"));
			GridBagConstraints gbc_jLabelDis2 = new GridBagConstraints();
			gbc_jLabelDis2.fill = GridBagConstraints.VERTICAL;
			gbc_jLabelDis2.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDis2.anchor = GridBagConstraints.WEST;
			gbc_jLabelDis2.gridy = 4;
			gbc_jLabelDis2.gridx = 0;
			jPanelData.add(jLabelDis2, gbc_jLabelDis2);
            /////////////Seach text field/////////////
            GridBagConstraints gbc_searchDiseaseTextField2 = new GridBagConstraints();
			gbc_searchDiseaseTextField2.weightx = 0.5;
			gbc_searchDiseaseTextField2.fill = GridBagConstraints.HORIZONTAL;
			gbc_searchDiseaseTextField2.insets = new Insets(5, 5, 5, 5);
			gbc_searchDiseaseTextField2.gridy = 4;
			gbc_searchDiseaseTextField2.gridx = 1;
            searchDiseaseTextField2 = new JTextField(10);
            searchDiseaseTextField2.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        searchDiseaseButton2.doClick();
                    }
                }
                public void keyReleased(KeyEvent e) {}
                public void keyTyped(KeyEvent e) {}
            });
			jPanelData.add(searchDiseaseTextField2, gbc_searchDiseaseTextField2);
            /////////////Seach text button/////////////
            GridBagConstraints gbc_searchDiseaseButton2 = new GridBagConstraints();
			gbc_searchDiseaseButton2.insets = new Insets(5, 5, 5, 5);
			gbc_searchDiseaseButton2.gridy = 4;
			gbc_searchDiseaseButton2.gridx = 2;
            searchDiseaseButton2 = new JButton();
            searchDiseaseButton2.setPreferredSize(new Dimension(20, 20));
            searchDiseaseButton2.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
            searchDiseaseButton2.addActionListener(this);
			jPanelData.add(searchDiseaseButton2, gbc_searchDiseaseButton2);
            /////////////Disesases combo/////////////
			GridBagConstraints gbc_jLabelDisBox2 = new GridBagConstraints();
			gbc_jLabelDisBox2.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDisBox2.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelDisBox2.weightx = 0.5;
			gbc_jLabelDisBox2.gridy = 4;
			gbc_jLabelDisBox2.gridx = 3;
			jPanelData.add(getDiseaseBox2(), gbc_jLabelDisBox2);
			
			jLabelDis3 = new JLabel(MessageBundle.getMessage("angal.opd.diagnosisnfulllist3"));
			GridBagConstraints gbc_jLabelDis3 = new GridBagConstraints();
			gbc_jLabelDis3.fill = GridBagConstraints.VERTICAL;
			gbc_jLabelDis3.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDis3.anchor = GridBagConstraints.WEST;
			gbc_jLabelDis3.gridy = 5;
			gbc_jLabelDis3.gridx = 0;
			jPanelData.add(jLabelDis3, gbc_jLabelDis3);
			GridBagConstraints gbc_jLabelDisBox3 = new GridBagConstraints();
            /////////////Seach text field/////////////
            GridBagConstraints gbc_searchDiseaseTextField3 = new GridBagConstraints();
			gbc_searchDiseaseTextField3.weightx = 0.5;
			gbc_searchDiseaseTextField3.fill = GridBagConstraints.HORIZONTAL;
			gbc_searchDiseaseTextField3.insets = new Insets(5, 5, 5, 5);
			gbc_searchDiseaseTextField3.gridy = 5;
			gbc_searchDiseaseTextField3.gridx = 1;
            searchDiseaseTextField3 = new JTextField(10);
            searchDiseaseTextField3.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        searchDiseaseButton3.doClick();
                    }
                }
                public void keyReleased(KeyEvent e) {}
                public void keyTyped(KeyEvent e) {}
            });
			jPanelData.add(searchDiseaseTextField3, gbc_searchDiseaseTextField3);
            /////////////Seach text button/////////////
            GridBagConstraints gbc_searchDiseaseButton3 = new GridBagConstraints();
			gbc_searchDiseaseButton3.insets = new Insets(5, 5, 5, 5);
			gbc_searchDiseaseButton3.gridy = 5;
			gbc_searchDiseaseButton3.gridx = 2;
            searchDiseaseButton3 = new JButton();
            searchDiseaseButton3.setPreferredSize(new Dimension(20, 20));
            searchDiseaseButton3.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
			jPanelData.add(searchDiseaseButton3, gbc_searchDiseaseButton3);
            searchDiseaseButton3.addActionListener(this);
            /////////////Disesases combo/////////////
			gbc_jLabelDisBox3.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDisBox3.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelDisBox3.weightx = 0.5;
			gbc_jLabelDisBox3.gridy = 5;
			gbc_jLabelDisBox3.gridx = 3;
			jPanelData.add(getDiseaseBox3(), gbc_jLabelDisBox3);
			
			jLabelLastOpdVisit = new JLabel(" ");
			jLabelLastOpdVisit.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelLastOpdVisit.setForeground(Color.RED);
			GridBagConstraints gbc_jLabelLastOpdVisit = new GridBagConstraints();
			gbc_jLabelLastOpdVisit.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelLastOpdVisit.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelLastOpdVisit.anchor = GridBagConstraints.EAST;
			gbc_jLabelLastOpdVisit.gridy = 6;
			gbc_jLabelLastOpdVisit.gridx = 0;
			jPanelData.add(jLabelLastOpdVisit, gbc_jLabelLastOpdVisit);
			jFieldLastOpdVisit = new JLabel(" ");
			jFieldLastOpdVisit.setFocusable(false);
			GridBagConstraints gbc_jFieldLastOpdVisit = new GridBagConstraints();
			gbc_jFieldLastOpdVisit.insets = new Insets(5, 5, 5, 0);
			gbc_jFieldLastOpdVisit.fill = GridBagConstraints.HORIZONTAL;
			gbc_jFieldLastOpdVisit.gridwidth = 4;
			gbc_jFieldLastOpdVisit.gridy = 6;
			gbc_jFieldLastOpdVisit.gridx = 1;
			jPanelData.add(jFieldLastOpdVisit, gbc_jFieldLastOpdVisit);
			
			jLabelLastOpdNote = new JLabel(" ");
			jLabelLastOpdNote.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabelLastOpdNote.setForeground(Color.RED);
			GridBagConstraints gbc_jLabelLastOpdNote = new GridBagConstraints();
			gbc_jLabelLastOpdNote.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelLastOpdNote.insets = new Insets(5, 5, 0, 5);
			gbc_jLabelLastOpdNote.anchor = GridBagConstraints.EAST;
			gbc_jLabelLastOpdNote.gridy = 7;
			gbc_jLabelLastOpdNote.gridx = 0;
			jPanelData.add(jLabelLastOpdNote, gbc_jLabelLastOpdNote);
			jFieldLastOpdNote = new JLabel(" ");
			jFieldLastOpdNote.setPreferredSize(new Dimension(500,30));
			jFieldLastOpdNote.setFocusable(false);
			GridBagConstraints gbc_jFieldLastOpdNote = new GridBagConstraints();
			gbc_jFieldLastOpdNote.anchor = GridBagConstraints.WEST;
			gbc_jFieldLastOpdNote.insets = new Insets(5, 5, 0, 0);
			gbc_jFieldLastOpdNote.gridwidth = 4;
			gbc_jFieldLastOpdNote.gridy = 7;
			gbc_jFieldLastOpdNote.gridx = 1;
			jPanelData.add(jFieldLastOpdNote, gbc_jFieldLastOpdNote);
			
		}
		return jPanelData;
	}

	/**
	 * 
	 */
	private JDateChooser getOpdDateFieldCal() {
		if (OpdDateFieldCal == null) {
			String d = "";
	
			java.util.Date myDate = null;
			if (insert) {
				if (RememberDates.getLastOpdVisitDateGregorian()==null) {
					visitDateOpd = new GregorianCalendar();
				}				
				else {
					visitDateOpd=RememberDates.getLastOpdVisitDateGregorian();
				}
			}
			 else {
				visitDateOpd  = opd.getVisitDate();
			}
			if (visitDateOpd==null) {
				d="";
			}
			else {
				myDate = visitDateOpd.getTime();
				d = currentDateFormat.format(myDate);
			}
			try {
				OpdDateFieldCal = new JDateChooser(currentDateFormat.parse(d), "dd/MM/yy");
				OpdDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
				OpdDateFieldCal.setDateFormatString("dd/MM/yy");
				OpdDateFieldCal.addPropertyChangeListener("date", new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						jOpdNumField.setText(getOpdNum());
					}
				});
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return OpdDateFieldCal;
	}
	
	private JPanel getJOpdNumberPanel() {
		if (jOpdNumberPanel == null) {
			
			jOpdNumberPanel = new JPanel();
			
			jOpdNumLabel = new JLabel();
			jOpdNumLabel.setText(MessageBundle.getMessage("angal.opd.opdnumber"));
			
			jOpdNumField = new JTextField(10);

			jOpdNumField.setEditable(true);
			jOpdNumField.setFocusable(true);
			jOpdNumField.setText(getOpdNum());
			
			jOpdNumField.setColumns(11);

			jOpdNumberPanel.add(jOpdNumLabel);
			jOpdNumberPanel.add(jOpdNumField);
		}
		return jOpdNumberPanel;
	}

	private String getOpdNum() {
		int OpdNum;
		if (!insert) return ""+opd.getProgYear();
		GregorianCalendar date = new GregorianCalendar();
		date.setTime(OpdDateFieldCal.getDate());
		try {
			opd.setProgYear(opdManager.getProgYear(date.get(Calendar.YEAR))+1);
		}catch(OHServiceException e){
			OHServiceExceptionUtil.showMessages(e);
		}
		OpdNum = opd.getProgYear();
		return ""+OpdNum;
	}
	
	private JPanel getJNotePanel() {
		if (jNotePanel == null) {
			jNotePanel = new JPanel();
			jNotePanel = setMyBorder(jNotePanel, MessageBundle.getMessage("angal.opd.noteandsymptom"));
			jNoteScrollPane = new JScrollPane(getJTextArea());
			jNoteScrollPane.setVerticalScrollBar(new JScrollBar());
			jNoteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jNoteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jNoteScrollPane.validate();
			jNotePanel.setLayout(new BorderLayout(0, 0));
			jNotePanel.add(jNoteScrollPane);
		}
		return jNotePanel;
	}
	
	private JTextArea getJTextArea() {
		if (jNoteTextArea == null) {
			jNoteTextArea = new JTextArea(15, 20);
			jNoteTextArea.setAutoscrolls(true);
			if (!insert) {
				jNoteTextArea.setText(opd.getNote());
			}
			jNoteTextArea.setWrapStyleWord(true);
			jNoteTextArea.setLineWrap(true);
		}
		return jNoteTextArea;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getDiseaseTypeBox() {
		if (diseaseTypeBox == null) {
			diseaseTypeBox = new JComboBox();
			
			DiseaseType elem2=null;
			diseaseTypeBox.setMaximumSize(new Dimension(400,50));
			diseaseTypeBox.addItem(allType);
			for (DiseaseType elem : types) {
				if(!insert && opd.getDisease().getType() != null) {
					if(opd.getDisease().getType().equals(elem.getCode())) {
						elem2=elem;}
				}
				diseaseTypeBox.addItem(elem);
			}
			if (elem2!=null) { 
				diseaseTypeBox.setSelectedItem(elem2);
			} else {
				diseaseTypeBox.setSelectedIndex(0);
			}
			diseaseTypeBox.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					diseaseBox1.removeAllItems();
					getDiseaseBox1();					
				}
			});
		}
		return diseaseTypeBox;
	}
	
	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getDiseaseBox1() {
		if (diseaseBox1 == null) {
			diseaseBox1 = new JComboBox();
			diseaseBox1.setMaximumSize(new Dimension(400, 50));
			diseaseBox1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAttendance();
				}
			});
		};
		Disease elem2 = null;
		diseaseBox1.addItem("");
		
		for (Disease elem : diseasesOPD) {
			if (((DiseaseType)diseaseTypeBox.getSelectedItem()).equals(allType))
				diseaseBox1.addItem(elem);
			else if (elem.getType().equals((DiseaseType)diseaseTypeBox.getSelectedItem()))
				diseaseBox1.addItem(elem);
			if(!insert && opd.getDisease()!=null){
				if (opd.getDisease().getCode().equals(elem.getCode())) {
					elem2 = elem;}
				
			}
		}
		if (!insert) {
			if (elem2 != null) {
				diseaseBox1.setSelectedItem(elem2);
			} else { //try in the canceled diseases
				if (opd.getDisease()!=null) {
					for (Disease elem : diseasesAll) {
						if (opd.getDisease().getCode().equals(elem.getCode())) {
							JOptionPane.showMessageDialog(OpdEditExtended.this,
									MessageBundle.getMessage("angal.opd.disease1mayhavebeencanceled"));
							diseaseBox1.addItem(elem);
							diseaseBox1.setSelectedItem(elem);
						}
					}
				}
			}
		}
		return diseaseBox1;
	}
	
	public JComboBox getDiseaseBox2() {
		if (diseaseBox2 == null) {
			diseaseBox2 = new JComboBox();
			diseaseBox2.setMaximumSize(new Dimension(400, 50));
		};
		Disease elem2=null;
		diseaseBox2.addItem("");

		for (Disease elem : diseasesOPD) {
			diseaseBox2.addItem(elem);
			if(!insert && opd.getDisease2()!=null){
				if (opd.getDisease2().getCode().equals(elem.getCode())) {
					elem2 = elem;}
			} 
		}
		if (elem2!= null) {
			diseaseBox2.setSelectedItem(elem2);
		} else { //try in the canceled diseases
			if (opd.getDisease2()!=null) {
				for (Disease elem : diseasesAll) {
					
					if (opd.getDisease2().getCode().equals(elem.getCode())) {
						JOptionPane.showMessageDialog(OpdEditExtended.this,
								MessageBundle.getMessage("angal.opd.disease2mayhavebeencanceled"));
						diseaseBox2.addItem(elem);
						diseaseBox2.setSelectedItem(elem);
					}
				}
			}
		}
		return diseaseBox2;
	}

	/**
	 * 
	 */
	private VoLimitedTextField getJTextPatientSrc() {
		if (jTextPatientSrc == null) {
			jTextPatientSrc = new VoLimitedTextField(16,20);
			jTextPatientSrc.addKeyListener(new KeyListener() {
	
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
				     if (key == KeyEvent.VK_ENTER) {
				    	 jSearchButton.doClick();
				     }
				}
	
				public void keyReleased(KeyEvent e) {
				}
	
				public void keyTyped(KeyEvent e) {
				}
			});
		}
		return jTextPatientSrc;
	}
	
	private JButton getJSearchButton() {
		if (jSearchButton == null) {
			jSearchButton = new JButton();
			jSearchButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
			jSearchButton.setBorderPainted(false);
			jSearchButton.setPreferredSize(new Dimension(20, 20));
			jSearchButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					jComboPatResult.removeAllItems();
					try {
						pat = patBrowser.getPatientWithHeightAndWeight(jTextPatientSrc.getText());
					}catch(OHServiceException ex){
						OHServiceExceptionUtil.showMessages(ex);
						pat = new ArrayList<Patient>();
					}
					getSearchBox(jTextPatientSrc.getText());
				}
			});
		}
		return jSearchButton;
	}
	
	private void getSearchBox(String s) {
		String key = s;
		String[] s1;
		
		if (key == null || key.compareTo("") == 0) {
			jComboPatResult.addItem(MessageBundle.getMessage("angal.opd.selectapatient"));
			jComboPatResult.addItem(MessageBundle.getMessage("angal.opd.newpatient"));
			jLabelLastOpdVisit.setText(" ");
			jFieldLastOpdVisit.setText(" ");
			jLabelLastOpdNote.setText(" ");
			jFieldLastOpdNote.setText(" ");
			if (jNoteTextArea != null) jNoteTextArea.setText("");
			if (jPanelPatient != null) resetPatient();
		}
				
		for (Patient elem : pat) {
			if(key != null) {
				s1 = key.split(" ");
				String name = elem.getSearchString();
				int a = 0;
				for (int i = 0; i < s1.length; i++) {
					if(name.contains(s1[i].toLowerCase()) == true) {
						a++;
					}
				}
				if (a == s1.length)	jComboPatResult.addItem(elem);
			} else 
				jComboPatResult.addItem(elem);
		}
		//ADDED: Workaround for no items
		if (jComboPatResult.getItemCount() == 0) {
			
				opdPatient = null;
				//resetOpd();
				//disableOpd();
				if (jPanelPatient != null) resetPatient();
				jPatientEditButton.setEnabled(true);
		}
		//ADDED: Workaround for one item only
		if (jComboPatResult.getItemCount() == 1) {
			
				opdPatient = (Patient)jComboPatResult.getSelectedItem();
				setPatient(opdPatient);
				//getjPanelPatient();
				jPatientEditButton.setEnabled(true);
		}
		//ADDED: Workaround for first item
		if (jComboPatResult.getItemCount() > 0) {
			
			if (jComboPatResult.getItemAt(0) instanceof Patient) {
				opdPatient = (Patient)jComboPatResult.getItemAt(0);
				setPatient(opdPatient);
				//getjPanelPatient();
				jPatientEditButton.setEnabled(true);
			}
		}
		jTextPatientSrc.requestFocus();
	}
	
	private JComboBox getSearchBox() {
		if (jComboPatResult == null) {
			jComboPatResult = new JComboBox();
			//jComboPatResult.setMaximumSize(new Dimension(400,50));
			if (opdPatient != null) {

				jComboPatResult.addItem(opdPatient);
				jComboPatResult.setEnabled(false);
				jTextPatientSrc.setEnabled(false);
				jSearchButton.setEnabled(false);
				
				return jComboPatResult;
			} else {
				jComboPatResult.addItem(MessageBundle.getMessage("angal.opd.selectapatient"));
				jComboPatResult.addItem(MessageBundle.getMessage("angal.opd.newpatient"));
			}

			jComboPatResult.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					if (jComboPatResult.getSelectedItem() != null) {
						if (jComboPatResult.getSelectedItem().toString().compareTo(MessageBundle.getMessage("angal.opd.newpatient")) == 0) {
							if (GeneralData.PATIENTEXTENDED) {
								PatientInsertExtended newrecord = new PatientInsertExtended(OpdEditExtended.this, new Patient(), true);
								newrecord.addPatientListener(OpdEditExtended.this);
								newrecord.setVisible(true);
							} else {
								PatientInsert newrecord = new PatientInsert(OpdEditExtended.this, new Patient(), true);
								newrecord.addPatientListener(OpdEditExtended.this);
								newrecord.setVisible(true);
							}

						} else if (jComboPatResult.getSelectedItem().toString().compareTo(MessageBundle.getMessage("angal.opd.selectapatient")) == 0) {
							jPatientEditButton.setEnabled(false);

						} else {
							opdPatient = (Patient) jComboPatResult.getSelectedItem();
							setPatient(opdPatient);
							jPatientEditButton.setEnabled(true);
						}
					}
				}
			});
		}
		return jComboPatResult;
	}
	
	//ADDED: Alex
	private JButton getJPatientEditButton() {
		if (jPatientEditButton == null) {
			jPatientEditButton = new JButton();
			jPatientEditButton.setIcon(new ImageIcon("rsc/icons/edit_button.png"));
			jPatientEditButton.setBorderPainted(false);
			jPatientEditButton.setPreferredSize(new Dimension(20, 20));
			jPatientEditButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (opdPatient != null) {
						if (GeneralData.PATIENTEXTENDED) {
							PatientInsertExtended editrecord = new PatientInsertExtended(OpdEditExtended.this, opdPatient, false);
							editrecord.addPatientListener(OpdEditExtended.this);
							editrecord.setVisible(true);
						} else {
							PatientInsert editrecord = new PatientInsert(OpdEditExtended.this, opdPatient, false);
							editrecord.addPatientListener(OpdEditExtended.this);
							editrecord.setVisible(true);
						}
					} 
				}
			});
			if (!insert) jPatientEditButton.setEnabled(false);
		}	
		return jPatientEditButton;
	}
	
	private JComboBox getDiseaseBox3() {
		if (diseaseBox3 == null) {
			diseaseBox3 = new JComboBox();
			diseaseBox3.setMaximumSize(new Dimension(400, 50));
		};
		Disease elem2=null;
		diseaseBox3.addItem("");

		for (Disease elem : diseasesOPD) {
			diseaseBox3.addItem(elem);
			if(!insert && opd.getDisease3()!=null){
				if (opd.getDisease3().getCode().equals(elem.getCode())) {
					elem2 = elem;}
			}
		}
		if (elem2!= null) {
			diseaseBox3.setSelectedItem(elem2);
		} else { //try in the canceled diseases
			if (opd.getDisease3()!=null) {	
				for (Disease elem : diseasesAll) {
					if (opd.getDisease3().getCode().equals(elem.getCode())) {
						JOptionPane.showMessageDialog(OpdEditExtended.this,
								MessageBundle.getMessage("angal.opd.disease3mayhavebeencanceled"));
						diseaseBox3.addItem(elem);
						diseaseBox3.setSelectedItem(elem);
					}
				}
			}
		}
		return diseaseBox3;
	}
	
	private JPanel getJPanelPatient() {
		if (jPanelPatient == null){
			
			jPanelPatient = new JPanel();
			GridBagLayout gbl_jPanelPatient = new GridBagLayout();
			gbl_jPanelPatient.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
			gbl_jPanelPatient.columnWeights = new double[]{0.0, 1.0, 1.0};
			gbl_jPanelPatient.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			jPanelPatient.setLayout(gbl_jPanelPatient);
			setMyMatteBorder(jPanelPatient, MessageBundle.getMessage("angal.opd.patient"));
			
			jLabelfirstName = new JLabel();
			jLabelfirstName.setText(MessageBundle.getMessage("angal.opd.first.name") + "\t");
			GridBagConstraints gbc_jLabelfirstName = new GridBagConstraints();
			gbc_jLabelfirstName.fill = GridBagConstraints.BOTH;
			gbc_jLabelfirstName.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelfirstName.gridx = 0;
			gbc_jLabelfirstName.gridy = 0;
			jPanelPatient.add(jLabelfirstName, gbc_jLabelfirstName);
			jFieldFirstName= new VoLimitedTextField(50,20);
			jFieldFirstName.setEditable(false);
			jFieldFirstName.setFocusable(false);
			GridBagConstraints gbc_jFieldFirstName = new GridBagConstraints();
			gbc_jFieldFirstName.insets = new Insets(5, 5, 5, 5);
			gbc_jFieldFirstName.fill = GridBagConstraints.BOTH;
			gbc_jFieldFirstName.gridx = 1;
			gbc_jFieldFirstName.gridy = 0;
			jPanelPatient.add(jFieldFirstName, gbc_jFieldFirstName);
			jLabelsecondName = new JLabel();
			jLabelsecondName.setText(MessageBundle.getMessage("angal.opd.second.name") + "\t");
			GridBagConstraints gbc_jLabelsecondName = new GridBagConstraints();
			gbc_jLabelsecondName.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelsecondName.fill = GridBagConstraints.BOTH;
			gbc_jLabelsecondName.gridx = 0;
			gbc_jLabelsecondName.gridy = 1;
			jPanelPatient.add(jLabelsecondName, gbc_jLabelsecondName);
			jFieldSecondName= new VoLimitedTextField(50,20);
			jFieldSecondName.setEditable(false);
			jFieldSecondName.setFocusable(false);
			GridBagConstraints gbc_jFieldSecondName = new GridBagConstraints();
			gbc_jFieldSecondName.fill = GridBagConstraints.BOTH;
			gbc_jFieldSecondName.insets = new Insets(5, 5, 5, 5);
			gbc_jFieldSecondName.gridx = 1;
			gbc_jFieldSecondName.gridy = 1;
			jPanelPatient.add(jFieldSecondName, gbc_jFieldSecondName);
			jLabeladdress  = new JLabel();
			jLabeladdress.setText(MessageBundle.getMessage("angal.opd.address"));
			GridBagConstraints gbc_jLabeladdress = new GridBagConstraints();
			gbc_jLabeladdress.fill = GridBagConstraints.BOTH;
			gbc_jLabeladdress.insets = new Insets(5, 5, 5, 5);
			gbc_jLabeladdress.gridx = 0;
			gbc_jLabeladdress.gridy = 2;
			jPanelPatient.add(jLabeladdress, gbc_jLabeladdress);
			jFieldAddress= new VoLimitedTextField(50,20);
			jFieldAddress.setEditable(false);
			jFieldAddress.setFocusable(false);
			GridBagConstraints gbc_jFieldAddress = new GridBagConstraints();
			gbc_jFieldAddress.fill = GridBagConstraints.BOTH;
			gbc_jFieldAddress.insets = new Insets(5, 5, 5, 5);
			gbc_jFieldAddress.gridx = 1;
			gbc_jFieldAddress.gridy = 2;
			jPanelPatient.add(jFieldAddress, gbc_jFieldAddress);
			jLabelcity = new JLabel();
			jLabelcity.setText(MessageBundle.getMessage("angal.opd.city"));
			GridBagConstraints gbc_jLabelcity = new GridBagConstraints();
			gbc_jLabelcity.fill = GridBagConstraints.BOTH;
			gbc_jLabelcity.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelcity.gridx = 0;
			gbc_jLabelcity.gridy = 3;
			jPanelPatient.add(jLabelcity, gbc_jLabelcity );
			jFieldCity= new VoLimitedTextField(50,20);
			jFieldCity.setEditable(false);
			jFieldCity.setFocusable(false);
			GridBagConstraints gbc_jFieldCity = new GridBagConstraints();
			gbc_jFieldCity.fill = GridBagConstraints.BOTH;
			gbc_jFieldCity.insets = new Insets(5, 5, 5, 5);
			gbc_jFieldCity.gridx = 1;
			gbc_jFieldCity.gridy = 3;
			jPanelPatient.add(jFieldCity, gbc_jFieldCity);
			jLabelnextKin = new JLabel();
			jLabelnextKin.setText(MessageBundle.getMessage("angal.opd.nextkin"));
			GridBagConstraints gbc_jLabelnextKin = new GridBagConstraints();
			gbc_jLabelnextKin.fill = GridBagConstraints.BOTH;
			gbc_jLabelnextKin.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelnextKin.gridx = 0;
			gbc_jLabelnextKin.gridy = 4;
			jPanelPatient.add(jLabelnextKin, gbc_jLabelnextKin);
			jFieldNextKin= new VoLimitedTextField(50,20);
			jFieldNextKin.setEditable(false);
			jFieldNextKin.setFocusable(false);
			GridBagConstraints gbc_jFieldNextKin = new GridBagConstraints();
			gbc_jFieldNextKin.fill = GridBagConstraints.BOTH;
			gbc_jFieldNextKin.insets = new Insets(5, 5, 5, 5);
			gbc_jFieldNextKin.gridx = 1;
			gbc_jFieldNextKin.gridy = 4;
			jPanelPatient.add(jFieldNextKin, gbc_jFieldNextKin);
			jLabelAge = new JLabel();
			jLabelAge.setText(MessageBundle.getMessage("angal.opd.age"));
			GridBagConstraints gbc_jLabelAge = new GridBagConstraints();
			gbc_jLabelAge.fill = GridBagConstraints.BOTH;
			gbc_jLabelAge.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelAge.gridx = 0;
			gbc_jLabelAge.gridy = 5;
			jPanelPatient.add(jLabelAge, gbc_jLabelAge);
			jFieldAge = new VoLimitedTextField(50,20);
			jFieldAge.setEditable(false);
			jFieldAge.setFocusable(false);
			GridBagConstraints gbc_jFieldAge = new GridBagConstraints();
			gbc_jFieldAge.fill = GridBagConstraints.BOTH;
			gbc_jFieldAge.insets = new Insets(5, 5, 5, 5);
			gbc_jFieldAge.gridx = 1;
			gbc_jFieldAge.gridy = 5;
			jPanelPatient.add(jFieldAge, gbc_jFieldAge);
			jLabelSex = new JLabel();
			jLabelSex.setText(MessageBundle.getMessage("angal.opd.sex"));
			GridBagConstraints gbc_jLabelSex = new GridBagConstraints();
			gbc_jLabelSex.fill = GridBagConstraints.HORIZONTAL;
			gbc_jLabelSex.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelSex.gridx = 0;
			gbc_jLabelSex.gridy = 6;
			jPanelPatient.add(jLabelSex, gbc_jLabelSex);
			radiom= new JRadioButton(MessageBundle.getMessage("angal.opd.male"));
			radiof= new JRadioButton(MessageBundle.getMessage("angal.opd.female"));
			jPanelSex = new JPanel();
			jPanelSex.add(radiom);
			jPanelSex.add(radiof);
			GridBagConstraints gbc_jPanelSex = new GridBagConstraints();
			gbc_jPanelSex.insets = new Insets(5, 5, 5, 5);
			gbc_jPanelSex.fill = GridBagConstraints.HORIZONTAL;
			gbc_jPanelSex.gridx = 1;
			gbc_jPanelSex.gridy = 6;
			jPanelPatient.add(jPanelSex, gbc_jPanelSex);
			GridBagConstraints gbc_jPatientNote = new GridBagConstraints();
			gbc_jPatientNote.fill = GridBagConstraints.BOTH;
			gbc_jPatientNote.insets = new Insets(5, 5, 5, 5);
			gbc_jPatientNote.gridx = 2;
			gbc_jPatientNote.gridy = 0;
			gbc_jPatientNote.gridheight = 7;
			jPanelPatient.add(getJPatientNote(), gbc_jPatientNote);
			
			group = new ButtonGroup();
			group.add(radiom);
			group.add(radiof);
			radiom.setSelected(true);
			radiom.setEnabled(false);
			radiof.setEnabled(false);
			radiom.setFocusable(false);
			radiof.setFocusable(false);

			if(opdPatient != null) setPatient(opdPatient);
		}
		return jPanelPatient;
	}
	
	private JPanel getJPatientNote() {
		if (jPatientNotePanel == null) {
			jPatientNotePanel = new JPanel(new BorderLayout());
			jPatientScrollNote = new JScrollPane(getJPatientNoteArea());
			jPatientScrollNote.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jPatientScrollNote.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jPatientScrollNote.setAutoscrolls(false);
			jPatientScrollNote.validate();
			jPatientNotePanel.add(jPatientScrollNote, BorderLayout.CENTER);
		}
		return jPatientNotePanel;
	}
	
	private JTextArea getJPatientNoteArea() {
		if (jPatientNote == null) {
			jPatientNote = new JTextArea(15, 15);
			if (!insert) {
				jPatientNote.setText(opdPatient.getNote());
			}
			jPatientNote.setLineWrap(true);
			jPatientNote.setEditable(false);
			jPatientNote.setFocusable(false);
		}
		return jPatientNote;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getOkButton(), null);
			if (insert && MainMenu.checkUserGrants("btnopdnewexamination") || 
					!insert && MainMenu.checkUserGrants("btnopdeditexamination"))
				jPanelButtons.add(getJButtonExamination(), null);
			jPanelButtons.add(getCancelButton(), null);
		}
		return jPanelButtons;
	}
	
	private JButton getJButtonExamination() {
		if (jButtonExamination == null) {
			jButtonExamination = new JButton(MessageBundle.getMessage("angal.opd.examination"));
			jButtonExamination.setMnemonic(KeyEvent.VK_E);
			
			jButtonExamination.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					if (opdPatient == null) {
						JOptionPane.showMessageDialog(OpdEditExtended.this,
								MessageBundle.getMessage("angal.opd.pleaseselectapatient"));
						return;
					}
					
					ExaminationBrowserManager examManager = Context.getApplicationContext().getBean(ExaminationBrowserManager.class);
					PatientExamination patex = null;
					PatientExamination lastPatex = null;
					try {
						lastPatex = examManager.getLastByPatID(opdPatient.getCode());
					}catch(OHServiceException ex){
						OHServiceExceptionUtil.showMessages(ex);
					}
					if (lastPatex != null) {
						patex = examManager.getFromLastPatientExamination(lastPatex);
					} else {
						patex = examManager.getDefaultPatientExamination(opdPatient);
					}
					
					GenderPatientExamination gpatex = new GenderPatientExamination(patex, opdPatient.getSex() == 'M');
					
					PatientExaminationEdit dialog = new PatientExaminationEdit(OpdEditExtended.this, gpatex);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setVisible(true);
				}
			});
		}
		return jButtonExamination;
	}
	
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */

	//alex: modified method to take data from Patient Object instead from jTextFields
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton(MessageBundle.getMessage("angal.common.ok"));
            okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean opdNumExist = false;
					if(!jOpdNumField.getText().equals("")||!jOpdNumField.getText().contains(" ")) {
						OpdBrowserManager opm = Context.getApplicationContext().getBean(OpdBrowserManager.class);
						GregorianCalendar gregDate = new GregorianCalendar();
						gregDate.setTime(OpdDateFieldCal.getDate());
						int opdNum;
						try {
							opdNum = Integer.parseInt(jOpdNumField.getText());
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.opd.opdnumbermustbeanumber"));
							return;
						}
						int opdEdit = 0;
						if (insert) {
							try {
								opdNumExist = opm.isExistOpdNum(opdNum, gregDate.get(Calendar.YEAR));
							} catch(OHServiceException e1){
								OHServiceExceptionUtil.showMessages(e1);
							}
						} else {
							opdEdit = opd.getProgYear();
						}

						if (opdNum != opdEdit) {
							try {
								opdNumExist = opm.isExistOpdNum(opdNum, gregDate.get(Calendar.YEAR));
							} catch(OHServiceException e1){
								OHServiceExceptionUtil.showMessages(e1);
							}
						} else {
							opdNumExist = false;
						}
					} else {
						JOptionPane.showMessageDialog(OpdEditExtended.this,
								MessageBundle.getMessage("angal.opd.opdnumbermustbeanumber"));
						return;
					}
					
					if (opdNumExist) {
						JOptionPane.showMessageDialog(OpdEditExtended.this,
								MessageBundle.getMessage("angal.opd.opdnumberalreadyexist"));
						return;
					}
					
					char newPatient=' ';
					String referralTo=null;
					String referralFrom=null;
					Disease disease=null;
					Disease disease2=null;
					Disease disease3=null;

					if (newPatientCheckBox.isSelected()){
						newPatient='N';
					}else{
						newPatient='R';
					}
					if (referralToCheckBox.isSelected()){
						referralTo="R";
					}else{
						referralTo="";
					}
					if (referralFromCheckBox.isSelected()){
						referralFrom="R";
					}else{
						referralFrom="";
					}
					//disease
					if (diseaseBox1.getSelectedIndex()>0) {
						disease=((Disease)diseaseBox1.getSelectedItem());
					}
					//disease2
					if (diseaseBox2.getSelectedIndex()>0) {
						disease2=((Disease)diseaseBox2.getSelectedItem());
					}
					//disease3
					if (diseaseBox3.getSelectedIndex()>0) {
						disease3=((Disease)diseaseBox3.getSelectedItem());					
					}
					
					if(OpdDateFieldCal.getDate() != null) {
					    visitDateOpd = new GregorianCalendar();
                        visitDateOpd.setTime(OpdDateFieldCal.getDate());
                        opd.setVisitDate(visitDateOpd);
                    }else{
                    	opd.setVisitDate(null);
                    }
					
					opd.setNote(jNoteTextArea.getText());
					opd.setPatient(opdPatient);
					opd.setNewPatient(newPatient);
					opd.setReferralFrom(referralFrom);
					opd.setReferralTo(referralTo);
					opd.setDisease(disease);					
					opd.setDisease2(disease2);
					opd.setDisease3(disease3);
					opd.setUserID(UserBrowsingManager.getCurrentUser());
					
					try {
						if (insert){    //Insert
							opd.setProgYear(Integer.parseInt(jOpdNumField.getText()));
							//remember for later use
							RememberDates.setLastOpdVisitDate(visitDateOpd);
							boolean result = opdManager.newOpd(opd);
							if (result) {
								fireSurgeryInserted(opd);
								dispose();
							}
							if (!result) JOptionPane.showMessageDialog(OpdEditExtended.this,
									MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
						}
						else {    //Update
							Opd updatedOpd = opdManager.updateOpd(opd);
							if (updatedOpd != null) {
								fireSurgeryUpdated(updatedOpd);
								dispose();
							};
							if (updatedOpd == null) JOptionPane.showMessageDialog(OpdEditExtended.this,
									MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
						}
					}catch(OHServiceException ex){
						OHServiceExceptionUtil.showMessages(ex);
					}
					
				};
			}
			);	
		}
		return okButton;
	}
	
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton(MessageBundle.getMessage("angal.common.cancel"));
            cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//to free Memory
					pat.clear();
					opdArray.clear();
					diseasesAll.clear();
					diseasesOPD.clear();
					types.clear();
					jComboPatResult.removeAllItems();
					diseaseTypeBox.removeAllItems();
					diseaseBox1.removeAllItems();
					diseaseBox2.removeAllItems();
					diseaseBox3.removeAllItems();
					dispose();
				}
			});
		}
		return cancelButton;
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
	
	/*
	 * set a specific border+title+matte to a panel
	 */
	private JPanel setMyMatteBorder(JPanel c, String title) {
		c.setBorder(new TitledBorder(new MatteBorder(1, 20, 1, 1, (Color) new Color(153, 180, 209)), title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		return c;
	}

	private JLabel getJlabelOpd() {
		if (JlabelOpd == null) {
			JlabelOpd = new JLabel("");
		}
		return JlabelOpd;
	}

	@Override
    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        if(source == searchDiseaseButton) {
            diseaseBox1.removeAllItems();
            diseaseBox1.addItem("");
            for(Disease disease: 
                    getSearchDiagnosisResults(searchDiseaseTextField.getText(), 
                            diseasesOPD == null? diseasesAll : diseasesOPD )) {
                diseaseBox1.addItem(disease);
            }
            if(diseaseBox1.getItemCount() >= 2){
                diseaseBox1.setSelectedIndex(1);
            }
            diseaseBox1.requestFocus();
            if(diseaseBox1.getItemCount() > 2){
                diseaseBox1.showPopup();
            }
        } else if(source == searchDiseaseButton2) {
            diseaseBox2.removeAllItems();
            diseaseBox2.addItem("");
            for(Disease disease: 
                    getSearchDiagnosisResults(searchDiseaseTextField2.getText(), 
                            diseasesOPD == null? diseasesAll : diseasesOPD)) {
                diseaseBox2.addItem(disease);
            }
            if(diseaseBox2.getItemCount() >= 2){
                diseaseBox2.setSelectedIndex(1);
            }
            diseaseBox2.requestFocus();
            if(diseaseBox2.getItemCount() > 2){
                diseaseBox2.showPopup();
            }
        } else if(source == searchDiseaseButton3) {
            diseaseBox3.removeAllItems();
            diseaseBox3.addItem("");
            for(Disease disease: 
                    getSearchDiagnosisResults(searchDiseaseTextField3.getText(), 
                            diseasesOPD == null? diseasesAll : diseasesOPD)) {
                diseaseBox3.addItem(disease);
            }
            if(diseaseBox3.getItemCount() >= 2){
                diseaseBox3.setSelectedIndex(1);
            }
            diseaseBox3.requestFocus();
            if(diseaseBox3.getItemCount() > 2){
                diseaseBox3.showPopup();
            }
        }
    }        
        
    private ArrayList<Disease> getSearchDiagnosisResults(String s, ArrayList<Disease> diseaseList) {
        String query = s.trim();
        ArrayList<Disease> results = new ArrayList<Disease>();
        for (Disease disease : diseaseList) {
            if(!query.equals("")) {
		String[] patterns = query.split(" ");
		String name = disease.getDescription().toLowerCase();
		boolean patternFound = false;
                for (String pattern : patterns) {
                    if (name.contains(pattern.toLowerCase())) {
                        patternFound = true;
                        //It is sufficient that only one pattern matches the query
                        break;
                    }
                }
		if (patternFound){
                    results.add(disease);
                }
            } else {
                results.add(disease);
            }
        }		
        return results;
    }
}
