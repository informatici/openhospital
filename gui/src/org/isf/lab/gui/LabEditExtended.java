
/*------------------------------------------
 * LabEdit - Add/edit a laboratory exam
 * -----------------------------------------
 * modification history
 * 
 *------------------------------------------*/

package org.isf.lab.gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.EventListenerList;

import org.isf.admission.manager.AdmissionBrowserManager;
import org.isf.admission.model.Admission;
import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.manager.ExamRowBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.lab.manager.LabManager;
import org.isf.lab.manager.LabRowManager;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryRow;
import org.isf.menu.manager.Context;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;

import com.toedter.calendar.JDateChooser;

public class LabEditExtended extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6576310684918676344L;
	
	//LISTENER INTERFACE --------------------------------------------------------
	private EventListenerList labEditExtendedListener = new EventListenerList();
	
	public interface LabEditExtendedListener extends EventListener {
		public void labUpdated();
	}
	
	public void addLabEditExtendedListener(LabEditExtendedListener l) {
		labEditExtendedListener.add(LabEditExtendedListener.class, l);
		
	}
	
	private void fireLabUpdated() {
		new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = labEditExtendedListener.getListeners(LabEditExtendedListener.class);
		for (int i = 0; i < listeners.length; i++)
			((LabEditExtendedListener)listeners[i]).labUpdated();
	}
	//---------------------------------------------------------------------------
	
	//private static final String VERSION=MessageBundle.getMessage("angal.versione");
	private static final String VERSION="2.0";
	
	private boolean insert = false;

	private Laboratory lab = null;
	private JPanel jContentPane = null;
	private JPanel buttonPanel = null;
	private JPanel dataPanel = null;
	private JPanel resultPanel = null;
	private JLabel examLabel = null;
	private JLabel noteLabel = null;
	private JLabel patientLabel = null;
	private JCheckBox inPatientCheckBox = null;
	private JLabel nameLabel = null;
	private JLabel ageLabel = null;
	private JLabel sexLabel = null;
	private JLabel examDateLabel = null;
	private JLabel matLabel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JComboBox matComboBox = null;
	private JComboBox examComboBox = null;
	private JComboBox examRowComboBox = null;
	private JComboBox patientComboBox = null;
	private Exam examSelected = null;
	private JScrollPane noteScrollPane = null;

	private JTextArea noteTextArea = null;

	private VoLimitedTextField patTextField = null;
	private VoLimitedTextField ageTextField = null;
	private VoLimitedTextField sexTextField = null;

	//ADDED: Alex
	private JPanel dataPatient = null;
	private VoLimitedTextField jTextPatientSrc;
	private Patient labPat = null;
	private String lastKey;
	private String s;
	private ArrayList<Patient> pat = null;
	//private JButton jSearchTrashButton = null;
	
	//private VoDateTextField examDateField = null;
	private JDateChooser examDateFieldCal = null;
	private GregorianCalendar dateIn = null;

	
	private static final Integer panelWidth=500; 
	private static final Integer labelWidth=50; 
	private static final Integer dataPanelHeight=90;
	private static final Integer dataPatientHeight=100;
	private static final Integer resultPanelHeight=350; 
	private static final Integer buttonPanelHeight=40; 

	
	private ArrayList<ExamRow> eRows = null;
	
	//private LabManager labManager = new LabManager(Context.getApplicationContext().getBean(LabIoOperations.class));
	private LabManager labManager = Context.getApplicationContext().getBean(LabManager.class);
	private LabRowManager lRowManager = Context.getApplicationContext().getBean(LabRowManager.class);
	private AdmissionBrowserManager admMan = Context.getApplicationContext().getBean(AdmissionBrowserManager.class);
	private ExamRowBrowsingManager rowManager = Context.getApplicationContext().getBean(ExamRowBrowsingManager.class);
	
	public LabEditExtended(JFrame owner, Laboratory laboratory, boolean inserting) {
		super(owner, true);
		insert = inserting;
		lab = laboratory;
		initialize();
	}

	private void initialize() {

		this.setBounds(30,30,panelWidth+20,dataPanelHeight+dataPatientHeight+resultPanelHeight+buttonPanelHeight+30);
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.lab.newlaboratoryexam")+"("+VERSION+")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.lab.editlaboratoryexam")+"("+VERSION+")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}


	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			// data panel
			jContentPane.add(getDataPatient());
			jContentPane.add(getDataPanel());
			resultPanel = new JPanel();
			resultPanel.setBounds(0, dataPanelHeight+dataPatientHeight, panelWidth, resultPanelHeight);
			if (!insert) {
				examSelected = lab.getExam();
				if (examSelected.getProcedure() == 1)
					resultPanel = getFirstPanel();
				else if (examSelected.getProcedure() == 2)
					resultPanel = getSecondPanel();
			}
			resultPanel.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.lab.result")));
			jContentPane.add(resultPanel);
			jContentPane.add(getButtonPanel()); // Generated
		}
		return jContentPane;
	}

	private JPanel getDataPanel() {
		if (dataPanel == null) {
			//initialize data panel
			dataPanel = new JPanel();
			dataPanel.setLayout(null);
			dataPanel.setBounds(0, 0, panelWidth, dataPanelHeight);
			//exam date
			examDateLabel = new JLabel(MessageBundle.getMessage("angal.common.date"));
			examDateLabel.setBounds(5, 10, labelWidth, 20);
			//examDateField=getExamDateField();
			//examDateField.setBounds(labelWidth+5, 10, 70, 20);
			examDateFieldCal = getExamDateFieldCal();
			examDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
			examDateFieldCal.setDateFormatString("dd/MM/yy");
			examDateFieldCal.setBounds(labelWidth+5, 10, 90, 20);
			//material
			matLabel = new JLabel(MessageBundle.getMessage("angal.lab.material"));
			matLabel.setBounds(155, 10, labelWidth, 20);
			matComboBox= getMatComboBox();
			matComboBox.setBounds(215, 10, 280, 20);
			//exam combo
			examLabel = new JLabel(MessageBundle.getMessage("angal.lab.exam"));
			examLabel.setBounds(5, 35, labelWidth, 20);
			examComboBox=getExamComboBox();
			examComboBox.setBounds(labelWidth+5, 35, 440, 20);

			//patient (in or out) data
			patientLabel = new JLabel(MessageBundle.getMessage("angal.lab.patientcode"));
			patientLabel.setBounds(labelWidth+5, 60, 110 , 20);
			
			//ADDED: Alex
			inPatientCheckBox = getInPatientCheckBox();
			inPatientCheckBox.setBounds(5, 60, labelWidth, 20);
			jTextPatientSrc = new VoLimitedTextField(200,20);
			jTextPatientSrc.setBounds(labelWidth+50,60,100,20);
			
			jTextPatientSrc.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) 
				{
					lastKey = "";
					String s = "" + e.getKeyChar();
					if (Character.isLetterOrDigit(e.getKeyChar())) {
						lastKey = s;
					}
					s = jTextPatientSrc.getText() + lastKey;
					s.trim();
					
					filterPatient(s);
				}

				//@Override
				public void keyPressed(KeyEvent e) {}

				//@Override
				public void keyReleased(KeyEvent e) {}
			});
			patientComboBox = getPatientComboBox(s);
			patientComboBox.setBounds(labelWidth+160, 60, 285, 20);

			//add all to the data panel
			dataPanel.add(examDateLabel, null);
			dataPanel.add(examDateFieldCal, null);
			dataPanel.add(matLabel, null);
			dataPanel.add(matComboBox, null);
			dataPanel.add(examLabel, null);
			dataPanel.add(examComboBox, null);
			dataPanel.add(patientLabel, null);
			dataPanel.add(inPatientCheckBox,null);
			//ADDED: Alex
			dataPanel.add(jTextPatientSrc,null);
			dataPanel.add(patientComboBox,null);
			
			dataPanel.setPreferredSize(new Dimension(150,200));
						
		}
		return dataPanel;
	}

	
// REPLACED BY CALENDAR	
//	private VoDateTextField getExamDateField() {
//		String d = "";
//		java.util.Date myDate = null;
//		if (insert)
//			dateIn=RememberDates.getLastLabExamDateGregorian();
//		 else 
//			dateIn  = lab.getExamDate();
//
//		if (dateIn!=null) {
//			myDate = dateIn.getTime();
//			d = currentDateFormat.format(myDate);
//		}
//		return (new VoDateTextField("dd/mm/yy", d, 15));
//	}

	private JPanel getDataPatient() {
		if (dataPatient == null) {
			dataPatient = new JPanel();
			dataPatient.setLayout(null);
			dataPatient.setBounds(0, dataPanelHeight, panelWidth, dataPatientHeight);
			dataPatient.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.lab.datapatient")));
			
			nameLabel = new JLabel(MessageBundle.getMessage("angal.lab.name"));
			nameLabel.setBounds(10, 20, labelWidth, 20);
			patTextField=getPatientTextField();
			patTextField.setBounds(labelWidth+5, 20, 180, 20);
			ageLabel = new JLabel(MessageBundle.getMessage("angal.lab.age"));
			ageLabel.setBounds(255, 20, 35, 20);
			ageTextField=getAgeTextField();
			ageTextField.setBounds(295, 20, 50, 20);
			sexLabel = new JLabel(MessageBundle.getMessage("angal.lab.sexmf"));
			sexLabel.setBounds(370, 20, 80, 20);
			sexTextField=getSexTextField();
			sexTextField.setBounds(440, 20, 50, 20);
			//note			
			noteLabel = new JLabel(MessageBundle.getMessage("angal.lab.note"));
			noteLabel.setBounds(10, 50, labelWidth, 20);
			noteTextArea = getNoteTextArea();
			noteTextArea.setBounds(labelWidth+5, 50, 440, 35);
			noteTextArea.setEditable(true);
			noteTextArea.setWrapStyleWord(true);
			noteTextArea.setAutoscrolls(true);
			
			/*
			 * Teo : Adding scroll capabilities at note textArea
			 */
			if(noteScrollPane == null)
			{
				noteScrollPane = new JScrollPane(noteTextArea);
				noteScrollPane.setBounds(labelWidth+5, 50, 440, 35);
				noteScrollPane.createVerticalScrollBar();
				noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				noteScrollPane.setAutoscrolls(true);
				dataPatient.add(noteScrollPane);
			}
			
			dataPatient.add(nameLabel, null);
			dataPatient.add(patTextField);
			dataPatient.add(ageLabel, null);
			dataPatient.add(ageTextField);
			dataPatient.add(sexLabel, null);
			dataPatient.add(sexTextField);
			dataPatient.add(noteLabel, null);
			
			patTextField.setEditable(false);
			ageTextField.setEditable(false);
			sexTextField.setEditable(false);
			noteTextArea.setEditable(true);
			
		}
		return dataPatient;
	}

	private JDateChooser getExamDateFieldCal() {
		java.util.Date myDate = null;
		if (insert) {
			dateIn = RememberDates.getLastLabExamDateGregorian();
		} else { 
			dateIn = lab.getExamDate();
		}
		if (dateIn != null) {
			myDate = dateIn.getTime();
		}
		return (new JDateChooser(myDate, "dd/MM/yy"));
	}
	
	private JCheckBox getInPatientCheckBox() {
		if (inPatientCheckBox == null) {
			inPatientCheckBox = new JCheckBox(MessageBundle.getMessage("angal.lab.in"));
			if (!insert)
				inPatientCheckBox.setSelected(lab.getInOutPatient().equalsIgnoreCase("I"));
			lab.setInOutPatient((inPatientCheckBox.isSelected()?"I":"R"));
		}
		return inPatientCheckBox;
	}

	/*
	 * TODO: Patient Selection like in LabNew
	 */
	private JComboBox getPatientComboBox(String s) {
		
		//String key = s;
		PatientBrowserManager patBrowser = Context.getApplicationContext().getBean(PatientBrowserManager.class);
		try {
			if (insert){
				pat = patBrowser.getPatient();
			} else  {
				pat = patBrowser.getPatient();
			}
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		if (patientComboBox == null) {
			patientComboBox = new JComboBox();
			patientComboBox.addItem(MessageBundle.getMessage("angal.lab.selectapatient"));
			
			if (!insert && lab.getPatient() != null) {
				try{
					labPat = patBrowser.getPatientAll(lab.getPatient().getCode());
					patientComboBox.addItem(labPat);
					patientComboBox.setSelectedItem(labPat);
					patientComboBox.setEnabled(false);
					jTextPatientSrc.setText(String.valueOf(labPat.getCode()));
					jTextPatientSrc.setEnabled(false);
				} catch (OHServiceException e) {
					OHServiceExceptionUtil.showMessages(e);
				}
				return patientComboBox;
			}
			
			if(pat != null){
				for (Patient elem : pat) {
					patientComboBox.addItem(elem);
				}
			}
			patientComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (patientComboBox.getSelectedIndex()>0) {
						labPat=(Patient)patientComboBox.getSelectedItem();
						setPatient(labPat);
						Admission admission = null;
						try {
							admission = admMan.getCurrentAdmission(labPat);
						}catch(OHServiceException e){
							OHServiceExceptionUtil.showMessages(e);
						}
						inPatientCheckBox.setSelected(admission != null ? true : false);
					}
				}
			});
		}
		return patientComboBox;
	}
	
	private void filterPatient(String key) {
		patientComboBox.removeAllItems();
				
		if (key == null || key.compareTo("") == 0) {
			patientComboBox.addItem(MessageBundle.getMessage("angal.lab.selectapatient"));
			resetLabPat();
		}
		
		for (Patient elem : pat) {
			if (key != null) {
				//Search key extended to name and code
				StringBuilder sbName = new StringBuilder();
				sbName.append(elem.getSecondName().toUpperCase());
				sbName.append(elem.getFirstName().toUpperCase());
				sbName.append(elem.getCode());
				String name = sbName.toString();
				
				if(name.toLowerCase().contains(key.toLowerCase())) {
					patientComboBox.addItem(elem);
				}
			} else {
				patientComboBox.addItem(elem);
			}
		}
		
		if (patientComboBox.getItemCount() == 1) {
			labPat=(Patient)patientComboBox.getSelectedItem();
			setPatient(labPat);
		}
		
		if (patientComboBox.getItemCount() > 0) {
			if (patientComboBox.getItemAt(0) instanceof Patient) {
				labPat = (Patient)patientComboBox.getItemAt(0);
				setPatient(labPat);
			}
		}
	}

	private void resetLabPat() {
		patTextField.setText("");
		ageTextField.setText("");
		sexTextField.setText("");
		noteTextArea.setText("");
		labPat = null;
	}

	private void setPatient(Patient labPat) {
		patTextField.setText(labPat.getName());
		ageTextField.setText(labPat.getAge()+"");
		sexTextField.setText(labPat.getSex()+"");
		noteTextArea.setText(labPat.getNote());		
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBounds(0, dataPanelHeight+dataPatientHeight+resultPanelHeight, panelWidth, buttonPanelHeight);
			buttonPanel.add(getOkButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	private JComboBox getExamComboBox() {
		if (examComboBox == null) {
			examComboBox = new JComboBox();
			Exam examSel=null;
			ExamBrowsingManager manager = Context.getApplicationContext().getBean(ExamBrowsingManager.class);
			ArrayList<Exam> exams;
			try {
				exams = manager.getExams();
			} catch (OHServiceException e) {
				exams = null;
				OHServiceExceptionUtil.showMessages(e);
			}
			examComboBox.addItem(MessageBundle.getMessage("angal.lab.selectanexam"));
			
			if (null != exams) {
				for (Exam elem : exams) {
					if (!insert && elem.getCode()!=null) {
						if (elem.getCode().equalsIgnoreCase((lab.getExam().getCode()))) {
							examSel=elem;
						}
					}
					examComboBox.addItem(elem);
				}
			}
			examComboBox.setSelectedItem(examSel);
			
			examComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (!(examComboBox.getSelectedItem() instanceof String)) {
						examSelected = (Exam) examComboBox
								.getSelectedItem();

						if (examSelected.getProcedure() == 1)
							resultPanel = getFirstPanel();
						else if (examSelected.getProcedure() == 2)
							resultPanel = getSecondPanel();

						validate();
						repaint();
					}
				}
			});
			resultPanel = null;
		}
		return examComboBox;
	}

	
	private JComboBox getMatComboBox() {
		if (matComboBox == null) {
			matComboBox = new JComboBox();
			matComboBox.addItem("");
			for (String elem : labManager.getMaterialList()) {
				matComboBox.addItem(elem);
				if (!insert) {
					try {	
						matComboBox.setSelectedItem(lab.getMaterial());
						}
					catch (Exception e) {}
				}
			}
		}
		return matComboBox;
	}

	
	//prova per gestire un campo note al posto di uno volimited
	private JTextArea getNoteTextArea() {
		if (noteTextArea == null) {
			noteTextArea = new JTextArea(10,30);
			if (!insert){
				noteTextArea.setText(lab.getNote());
			}
			noteTextArea.setLineWrap(true);
			noteTextArea.setPreferredSize(new Dimension(10,30));
			noteTextArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return noteTextArea;
	}
	
	
	
	private VoLimitedTextField getPatientTextField() {
		if (patTextField == null) {
			patTextField = new VoLimitedTextField(100);
			if (!insert) {
				patTextField.setText(lab.getPatName());
			}
		}
		return patTextField;
	}

	
	private VoLimitedTextField getAgeTextField() {
		if (ageTextField == null) {
			ageTextField = new VoLimitedTextField(3);
			if (insert) {
				ageTextField.setText("");
				}
			else {
				try {	
					Integer intAge=lab.getAge();
					ageTextField.setText(intAge.toString());
					}
				catch (Exception e) {
					ageTextField.setText("");
					}
				}
			}
		return ageTextField;
	}
	
	private VoLimitedTextField getSexTextField() {
		if (sexTextField == null) {
			sexTextField = new VoLimitedTextField(1);
			if (!insert) {
				sexTextField.setText(lab.getSex());
			}
		}
		return sexTextField;
	}
	
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(MessageBundle.getMessage("angal.common.cancel"));
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}

	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText(MessageBundle.getMessage("angal.common.ok"));
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (examComboBox.getSelectedIndex() == 0) {
						JOptionPane.showMessageDialog(LabEditExtended.this,
								MessageBundle.getMessage("angal.lab.pleaseselectanexam"));
						return;
					}
					String matSelected=(String)matComboBox.getSelectedItem();
					examSelected=(Exam)examComboBox.getSelectedItem();
					labPat=(Patient)patientComboBox.getSelectedItem();
					GregorianCalendar gregDate = new GregorianCalendar();
					try {
						gregDate.setTime(examDateFieldCal.getDate());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(LabEditExtended.this, 
								MessageBundle.getMessage("angal.lab.pleaseinsertavalidexamdate"));
						return;
					}
					
					ArrayList<String> labRow = new ArrayList<String>();
					lab.setDate(new GregorianCalendar());
					lab.setExamDate(gregDate);
					RememberDates.setLastLabExamDate(gregDate);
					lab.setMaterial(matSelected);
					lab.setExam(examSelected);
					lab.setNote(noteTextArea.getText());
					lab.setInOutPatient((inPatientCheckBox.isSelected()?"I":"O"));
					lab.setPatient(labPat);
					lab.setPatName(labPat.getName());
					lab.setSex(labPat.getSex()+"");
					
					if (examSelected.getProcedure() == 1)
						lab.setResult(examRowComboBox.getSelectedItem()
								.toString());
					else if (examSelected.getProcedure() == 2) {
						lab.setResult(MessageBundle.getMessage("angal.lab.multipleresults"));
						for (int i = 0; i < resultPanel.getComponentCount(); i++) {
							if (((SubPanel) resultPanel.getComponent(i))
									.getSelectedResult().equalsIgnoreCase("P")) {
								labRow.add(eRows.get(i).getDescription());
							}
						}
					}
					boolean result = false;
					if (insert) {
						lab.setAge(labPat.getAge());
						try {
							result = labManager.newLaboratory(lab,	labRow);
						} catch (OHServiceException e1) {
							result = false;
							OHServiceExceptionUtil.showMessages(e1);
							return;
						}
					}
					else {
						try {
							result = labManager.updateLaboratory(lab, labRow);
						} catch (OHServiceException e1) {
							result = false;
							OHServiceExceptionUtil.showMessages(e1);
							return;
						}
					}
					if (!result)
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
					else {
						fireLabUpdated();
						dispose();
					}
				}

				
			});
		}
		return okButton;
	}

	private JPanel getFirstPanel() {
		resultPanel.removeAll();
		String result="";
		examRowComboBox = new JComboBox();
		examRowComboBox.setMaximumSize(new Dimension(200, 25));
		examRowComboBox.setMinimumSize(new Dimension(200, 25));
		examRowComboBox.setPreferredSize(new Dimension(200, 25));
		if (insert) {
			result=examSelected.getDefaultResult();
		} else {
			result=lab.getResult();
		}
		examRowComboBox.addItem(result);

		ArrayList<ExamRow> rows;
		try {
			rows = rowManager.getExamRowByExamCode(examSelected.getCode());
		} catch (OHServiceException e) {
			rows = null;
			OHServiceExceptionUtil.showMessages(e);
		}
		if (null != rows) {
			for (ExamRow r : rows) {
				if (!r.getDescription().equals(result))
					examRowComboBox.addItem(r.getDescription());
			}
		}
		if (examRowComboBox.getItemCount() > 0) resultPanel.add(examRowComboBox);

		return resultPanel;
	}

	private JPanel getSecondPanel() {
		resultPanel.removeAll();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		String examId = examSelected.getCode();
		eRows = null;
		
		try {
			eRows = rowManager.getExamRowByExamCode(examId);
		} catch (OHServiceException e1) {
			OHServiceExceptionUtil.showMessages(e1);
		}
		if (insert) {
			if (null != eRows) {
				for (ExamRow r : eRows)
					resultPanel.add(new SubPanel(r, "N"));
			}
		} else {

			ArrayList<LaboratoryRow> lRows;
			try {
				lRows = lRowManager.getLabRowByLabId(lab.getCode());
			} catch (OHServiceException e) {
				lRows = new ArrayList<LaboratoryRow>();
				OHServiceExceptionUtil.showMessages(e);
			}
			boolean find;
			if (null != eRows) {
				for (ExamRow r : eRows) {
					find = false;
					for (LaboratoryRow lR : lRows) {
						if (r.getDescription()
								.equalsIgnoreCase(lR.getDescription()))
							find = true;
					}
					if (find) {
						resultPanel.add(new SubPanel(r, "P"));
					} else {
						resultPanel.add(new SubPanel(r, "N"));
					}
				}
			}
		}
		return resultPanel;
	}

	class SubPanel extends JPanel {

		private static final long serialVersionUID = -8847689740511562992L;

		private JLabel label = null;

		private JRadioButton radioPos = null;

		private JRadioButton radioNeg = null;

		private ButtonGroup group = null;

		public SubPanel(ExamRow row, String result) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			label = new JLabel(row.getDescription());
			this.add(label);

			group = new ButtonGroup();
			radioPos = new JRadioButton(MessageBundle.getMessage("angal.lab.p"));
			radioNeg = new JRadioButton(MessageBundle.getMessage("angal.lab.n"));
			group.add(radioPos);
			group.add(radioNeg);

			this.add(radioPos);
			this.add(radioNeg);
			if (result.equals(MessageBundle.getMessage("angal.lab.p")))
				radioPos.setSelected(true);
			else
				radioNeg.setSelected(true);
		}

		public String getSelectedResult() {
			if (radioPos.isSelected())
				return MessageBundle.getMessage("angal.lab.p");
			else
				return MessageBundle.getMessage("angal.lab.n");
		}

	}

}
