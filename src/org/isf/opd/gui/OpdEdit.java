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
 * 			         - version is now 1.1 
 * 09/01/2009 - fabrizio - Removed unuseful control on variable dateIn.
 *                         Cosmetic changes to code style.
 *------------------------------------------*/


import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import org.isf.generaldata.MessageBundle;


import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.menu.gui.MainMenu;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.utils.jobjects.VoDateTextField;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;


public class OpdEdit extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = -7369841416710920082L;

	private EventListenerList surgeryListeners = new EventListenerList();
	
	public interface SurgeryListener extends EventListener {
		public void surgeryUpdated(AWTEvent e);
		public void surgeryInserted(AWTEvent e);
	}
	
	public void addSurgeryListener(SurgeryListener l) {
		surgeryListeners.add(SurgeryListener.class, l);
	}
	
	public void removeSurgeryListener(SurgeryListener listener) {
		surgeryListeners.remove(SurgeryListener.class, listener);
	}
	
	private void fireSurgeryInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			private static final long serialVersionUID = -2831804524718368850L;
			
		};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryInserted(event);
	}
	private void fireSurgeryUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			private static final long serialVersionUID = -1073238832996429931L;
			
		};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryUpdated(event);
	}
	
	public void actionPerformed(ActionEvent e) {
//		sexSelect=e.getActionCommand();
	}
	
	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 

	private JPanel insertPanel = null;
	private JLabel jLabel = null;
	private JPanel principalPanel = null;
	private JPanel jAgePanel = null;
	private JPanel jSexPanel = null;
	private JPanel jNewPatientPanel= null;
	private JPanel jDatePanel= null;
	private JPanel jDiseasePanel = null;
	private JPanel jDiseasePanel2 = null;
	private JPanel jDiseasePanel3 = null;
	private JPanel jDiseaseTypePanel = null;
	private JComboBox diseaseTypeBox = null;
	private JLabel jLabel1 = null;
	private JLabel jLabelDis2 = null;
	private JLabel jLabelDis3 = null;
	private JComboBox diseaseBox = null;
	private JComboBox diseaseBox2 = null;
	private JComboBox diseaseBox3 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private GregorianCalendar dateIn = null;
	private DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
	private VoDateTextField OpdDateField = null;
	private JPanel jPanel2 = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JCheckBox newPatientCheckBox = null;
	private JCheckBox referralToCheckBox = null;
	private JCheckBox referralFromCheckBox = null;
	private VoLimitedTextField ageField = null;
	private JPanel sexPanel = null;
	private JRadioButton radiom;
	private JRadioButton radiof;
	private ButtonGroup group=null;
	private Integer age = null;
	private Opd opd;
	private boolean insert;
	private char sex;
//	private String sexSelect="Male";
	private int oldAge;
	private DiseaseType allType= new DiseaseType(MessageBundle.getMessage("angal.opd.alltype"),MessageBundle.getMessage("angal.opd.alltype"));

	/**
	 * This method initializes 
	 * 
	 */
	public OpdEdit(JFrame owner,Opd old,boolean inserting) {
		super(owner,true);
		opd=old;
		insert=inserting;
		if(!insert)oldAge=opd.getAge();
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setBounds(100, 50, 450, 500);
		this.setContentPane(getPrincipalPanel());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.opd.newopdregistration")+"("+VERSION+")");
		}
		else {
			this.setTitle(MessageBundle.getMessage("angal.opd.editopdregistration")+"("+VERSION+")");
		}
		
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPrincipalPanel() {
		if (principalPanel == null) {
			principalPanel = new JPanel();
			principalPanel.setLayout(new BorderLayout());
			principalPanel.add(getInsertPanel(), java.awt.BorderLayout.NORTH);
			principalPanel.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return principalPanel;
	}

	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getInsertPanel() {
		if (insertPanel == null) {
			insertPanel = new JPanel();
			insertPanel.setLayout(new BoxLayout(getInsertPanel(),BoxLayout.Y_AXIS));
			insertPanel.add(getJNewPatientPanel(), null);
			insertPanel.add(getJDatePanel(), null);
			insertPanel.add(getJDiseaseTypePanel(), null);
			insertPanel.add(getDiseaseTypeBox(), null);
			insertPanel.add(getJDiseasePanel(), null);
			insertPanel.add(getDiseaseBox(), null);
			insertPanel.add(getJDiseasePanel2(), null);
			insertPanel.add(getDiseaseBox2(), null);
			insertPanel.add(getJDiseasePanel3(), null);
			insertPanel.add(getDiseaseBox3(), null);
			insertPanel.add(getJAgePanel(), null);
			insertPanel.add(getAgeField(), null);
			insertPanel.add(getJSexPanel(), null);
			insertPanel.add(getSexPanel(), null);
			
		}
		return insertPanel;
	}
	
	
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getDiseaseTypeBox() {
		if (diseaseTypeBox == null) {
			diseaseTypeBox = new JComboBox();
			DiseaseType elem2 = null;
			diseaseTypeBox.setMaximumSize(new Dimension(400,50));
			DiseaseTypeBrowserManager manager = new DiseaseTypeBrowserManager();
			diseaseTypeBox.addItem(allType);
			ArrayList<DiseaseType> types = manager.getDiseaseType();
			for (DiseaseType elem : types) {
				if (!insert && opd.getDiseaseType() != null){
					if(opd.getDiseaseType().equals(elem.getCode())){
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
					diseaseBox.removeAllItems();
					getDiseaseBox();					
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
	public JComboBox getDiseaseBox() {
		if (diseaseBox == null) {
			diseaseBox = new JComboBox();
			diseaseBox.setMaximumSize(new Dimension(400, 50));
		}
		Disease elem2=null;
		ArrayList<Disease> diseases;
		DiseaseBrowserManager manager = new DiseaseBrowserManager();
		//if (((DiseaseType)DiseaseTypeBox.getSelectedItem()).getDescription().equals("All Type")){
		if (diseaseTypeBox.getSelectedIndex() == 0) {
			diseases = manager.getDiseaseOpd();
		}else{
			String code = ((DiseaseType)diseaseTypeBox.getSelectedItem()).getCode();
			diseases = manager.getDiseaseOpd(code);
		}
		diseaseBox.addItem("");

		for (Disease elem : diseases) {
			diseaseBox.addItem(elem);
			if(!insert && opd.getDisease()!=null){
				if(opd.getDisease().equals(elem.getCode())){
					elem2 = elem;}
			}
		}
		if (!insert) {
			if (elem2!= null) {
				diseaseBox.setSelectedItem(elem2);
			} else { //try in the canceled diseases
				if (opd.getDisease() != null) {
					ArrayList<Disease> diseasesAll = manager.getDiseaseAll();
					for (Disease elem : diseasesAll) {
						if (opd.getDisease().getCode().compareTo(elem.getCode()) == 0) {
							JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.disease1mayhavebeencanceled"));
							diseaseBox.addItem(elem);
							diseaseBox.setSelectedItem(elem);
						}
					}
				}
			}
		}
				
		return diseaseBox;
	}
	
	public JComboBox getDiseaseBox2() {
		if (diseaseBox2 == null) {
			diseaseBox2 = new JComboBox();
			diseaseBox2.setMaximumSize(new Dimension(400, 50));
		}
		Disease elem2=null;
		ArrayList<Disease> diseases;
		DiseaseBrowserManager manager = new DiseaseBrowserManager();
		diseases = manager.getDiseaseOpd();
		diseaseBox2.addItem("");

		for (Disease elem : diseases) {
			diseaseBox2.addItem(elem);
			if(!insert && opd.getDisease2()!=null){
				if(opd.getDisease2().equals(elem.getCode())){
					elem2 = elem;}
			}
		}
		if (elem2!= null) {
			diseaseBox2.setSelectedItem(elem2);
		} else { //try in the canceled diseases
			if (opd.getDisease2()!=null) {
				ArrayList<Disease> diseasesAll = manager.getDiseaseAll();
				for (Disease elem : diseasesAll) {
					if (opd.getDisease2().getCode().compareTo(elem.getCode()) == 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.disease2mayhavebeencanceled"));
						diseaseBox2.addItem(elem);
						diseaseBox2.setSelectedItem(elem);
					}
				}
			}
		}
		return diseaseBox2;
	}
	
	public JComboBox getDiseaseBox3() {
		if (diseaseBox3 == null) {
			diseaseBox3 = new JComboBox();
			diseaseBox3.setMaximumSize(new Dimension(400, 50));
		}
		Disease elem2=null;
		ArrayList<Disease> diseases;
		DiseaseBrowserManager manager = new DiseaseBrowserManager();
		diseases = manager.getDiseaseOpd();
		diseaseBox3.addItem("");

		for (Disease elem : diseases) {
			diseaseBox3.addItem(elem);
			if(!insert && opd.getDisease3()!=null){
				if(opd.getDisease3().equals(elem.getCode())){
					elem2 = elem;}
			}
		}
		if (elem2!= null) {
			diseaseBox3.setSelectedItem(elem2);
		} else { //try in the canceled diseases
			if (opd.getDisease3()!=null) {
				ArrayList<Disease> diseasesAll = manager.getDiseaseAll();
				for (Disease elem : diseasesAll) {
					if (opd.getDisease3().getCode().compareTo(elem.getCode()) == 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.disease3mayhavebeencanceled"));
						diseaseBox3.addItem(elem);
						diseaseBox3.setSelectedItem(elem);
					}
				}
			}
		}
		return diseaseBox3;
	}
	
	
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.add(getOkButton(), null);
			jPanel2.add(getCancelButton(), null);
			//jPanel.setLayout(new BoxLayout(getJPanel(),BoxLayout.X_AXIS));
		}
		return jPanel2;
	}
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton(MessageBundle.getMessage("angal.common.ok"));
            okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					OpdBrowserManager manager= new OpdBrowserManager();
					boolean result = false;
					GregorianCalendar gregDate = new GregorianCalendar();
					char newPatient=' ';
					String referralTo="";
					String referralFrom="";
					Disease disease=null;
					Disease disease2=null;
					Disease disease3=null;

					if (diseaseBox.getSelectedIndex()==0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.opd.pleaseselectadisease"));
						return;
					}

					if (age < 0) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.insertage"));
						return;
					}

					if (newPatientCheckBox.isSelected()) {
						newPatient = 'N';
					} else {
						newPatient = 'R';
					}

					if (referralToCheckBox.isSelected()){
						referralTo = "R";
					} else {
						referralTo = "";
					}

					if (referralFromCheckBox.isSelected()){
						referralFrom = "R";
					} else {
						referralFrom = "";
					}

					
					//disease
					if (diseaseBox.getSelectedIndex()>0) {
						disease=((Disease)diseaseBox.getSelectedItem());					
					}
					//disease2
					if (diseaseBox2.getSelectedIndex()>0) {
						disease2=((Disease)diseaseBox2.getSelectedItem());					
					}
					//disease3
					if (diseaseBox3.getSelectedIndex()>0) {
						disease3=((Disease)diseaseBox3.getSelectedItem());					
					}
					// visit date	
					String d = OpdDateField.getText().trim();
					if (d.equals("")) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.pleaseinsertattendancedate"));
						return;
					}
					else {
						try {
							currentDateFormat.setLenient(false);
							Date myDate = currentDateFormat.parse(d);
							gregDate.setTime(myDate);
						} catch (ParseException pe) {
							System.out.println(pe);
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.opd.pleaseinsertavalidattendancedate"));
							//dateOutField.setText("");
							return;
						}
					}
					
					
					if (insert){
						if (radiof.isSelected()) {
							sex='F';
						} else {
							sex='M';
						}
						GregorianCalendar date =new GregorianCalendar();
						opd.setNewPatient(newPatient);
						opd.setReferralFrom(referralFrom);
						opd.setReferralTo(referralTo);
						opd.setAge(age);
						opd.setSex(sex);
						opd.setYear(manager.getProgYear(date.get(GregorianCalendar.YEAR))+1);
						opd.setDisease(disease);				
						opd.setDisease2(disease2);
						opd.setDisease3(disease3);
						opd.setVisitDate(gregDate);
						opd.setNote("");
						opd.setUserID(MainMenu.getUser());
						
						//remember for later use
						RememberDates.setLastOpdVisitDate(gregDate);
						result = manager.newOpd(opd);
						if (result) {
							fireSurgeryInserted();
						}
						if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.thedatacouldnotbesaved"));
						else  dispose();
						}
					else {    //Update
						if (radiof.isSelected()) {
							sex='F';
						} else {
							sex='M';
						}
						opd.setNewPatient(newPatient);
						opd.setReferralFrom(referralFrom);
						opd.setReferralTo(referralTo);
						opd.setAge(age);
						opd.setSex(sex);
						opd.setDisease(disease);				
						opd.setDisease2(disease2);
						opd.setDisease3(disease3);
						opd.setVisitDate(gregDate);
						result = manager.updateOpd(opd);
						if (result) {
							fireSurgeryUpdated();
						};
						if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.thedatacouldnotbesaved"));
						else  dispose();
					};
				};
			}
			);	
		}
		return okButton;
	}
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton(MessageBundle.getMessage("angal.common.cancel"));
            cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}
	
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getAgeField() {
		if (ageField == null) {
			ageField = new VoLimitedTextField(3,2);
			ageField.setText("0");
			ageField.setMaximumSize(new Dimension(50, 50));
			if (insert) {
				age=-1;
				ageField.setText("");
			} else {
				Integer oldage = opd.getAge();
				ageField.setText(oldage.toString());
				age=oldAge;
			}
			ageField.setMinimumSize(new Dimension(100, 50));
		}
		ageField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {				
				try {				
					//ageField.setText(new StringTokenizer(ageField.getText()).nextToken());
					age = Integer.parseInt(ageField.getText());
					if (age < 0 || age > 200) {
						ageField.setText("0");
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.opd.insertvalidage"));
					}
				} catch (NumberFormatException ex) {
					ageField.setText("0");
				}
				
			}
			
			public void focusGained(FocusEvent e) {
			}
		});
		return ageField;
	}
	
	/**
	 * This method initializes sexPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getSexPanel() {
		if (sexPanel == null) {
			sexPanel = new JPanel();
			group=new ButtonGroup();
			radiom = new JRadioButton(MessageBundle.getMessage("angal.opd.male"));
			radiof = new JRadioButton(MessageBundle.getMessage("angal.opd.female"));
			if(insert)radiom.setSelected(true);
			else{
				if(opd.getSex()=='F')
					radiof.setSelected(true);
				else radiom.setSelected(true);
			}			
			//radiom.addActionListener(this);
			//radiof.addActionListener(this);
			group.add(radiom);
			group.add(radiof);
			sexPanel.add(radiom);
			sexPanel.add(radiof);
			
		}
		return sexPanel;
	}

	public JPanel getJAgePanel() {
		if (jAgePanel == null){
			jAgePanel = new JPanel();
			jLabel2 = new JLabel();
			jLabel2.setText(MessageBundle.getMessage("angal.opd.age"));
			jAgePanel.add(jLabel2);
		}
		return jAgePanel;
	}

	public JPanel getJDiseasePanel() {
		if (jDiseasePanel == null){
			jDiseasePanel = new JPanel();
			jLabel1 = new JLabel();
			jLabel1.setText(MessageBundle.getMessage("angal.opd.diagnosis"));
			jDiseasePanel.add(jLabel1);
		}
		return jDiseasePanel;
	}
	public JPanel getJDiseasePanel2() {
		if (jDiseasePanel2 == null){
			jDiseasePanel2 = new JPanel();
			jLabelDis2 = new JLabel();
			jLabelDis2.setText(MessageBundle.getMessage("angal.opd.diagnosisnfulllist"));
			jDiseasePanel2.add(jLabelDis2);
		}
		return jDiseasePanel2;
	}
	public JPanel getJDiseasePanel3() {
		if (jDiseasePanel3 == null){
			jDiseasePanel3 = new JPanel();
			jLabelDis3 = new JLabel();
			jLabelDis3.setText(MessageBundle.getMessage("angal.opd.diagnosisnfulllist3"));
			jDiseasePanel3.add(jLabelDis3);
		}
		return jDiseasePanel3;
	}

	public JPanel getJDiseaseTypePanel() {
		if (jDiseaseTypePanel == null){
			jDiseaseTypePanel = new JPanel();
			jLabel = new JLabel();
			jLabel.setText(MessageBundle.getMessage("angal.opd.diseasetype"));
			jDiseaseTypePanel.add(jLabel);
		}
		return jDiseaseTypePanel;
	}

	public JPanel getJSexPanel() {
		if (jSexPanel == null){
			jSexPanel = new JPanel();
			jLabel3 = new JLabel();
			jLabel3.setText(MessageBundle.getMessage("angal.opd.sex"));
			jSexPanel.add(jLabel3);
		}
		return jSexPanel;
	}
	
	public JPanel getJNewPatientPanel() {
		String referralTo="";
		String referralFrom="";

		if (jNewPatientPanel == null){
			jNewPatientPanel = new JPanel();
			newPatientCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.newattendance"));
			jNewPatientPanel.add(newPatientCheckBox);
			if(!insert){
				if (opd.getNewPatient() == 'N')newPatientCheckBox.setSelected(true);
			}
			referralFromCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.referral.from"));
			jNewPatientPanel.add(referralFromCheckBox);
			if(!insert){
				referralFrom = opd.getReferralFrom();
				if (referralFrom == null) referralFrom="";
				if (referralFrom.equals("R"))referralFromCheckBox.setSelected(true);
			}
			referralToCheckBox = new JCheckBox(MessageBundle.getMessage("angal.opd.referral.to"));
			jNewPatientPanel.add(referralToCheckBox);
			if(!insert){
				referralTo = opd.getReferralTo();
				if (referralTo == null) referralTo="";
				if (referralTo.equals("R"))referralToCheckBox.setSelected(true);
			}
		}
		return jNewPatientPanel;
	}
	
	public JPanel getJDatePanel() {
		if (jDatePanel == null) {
			jDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
			String d = "";
			Date myDate = null;
			if (insert) {
				if (RememberDates.getLastOpdVisitDateGregorian() == null) {
					dateIn = new GregorianCalendar();
				} else {
					dateIn = RememberDates.getLastOpdVisitDateGregorian();
				}
			} else {
				dateIn = opd.getVisitDate();
			}
			
			myDate = dateIn.getTime();
			d = currentDateFormat.format(myDate);
			
			OpdDateField = new VoDateTextField("dd/mm/yy", d, 15);

			jDatePanel.add(OpdDateField);
			jDatePanel = setMyBorder(jDatePanel, MessageBundle.getMessage("angal.opd.attendancedate"));
		}
		
		return jDatePanel;
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

	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"  