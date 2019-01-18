package org.isf.patient.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;

public class PatientInsert extends JDialog implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList patientListeners = new EventListenerList();
	
	public interface PatientListener extends EventListener {
		public void patientUpdated(AWTEvent e);
		public void patientInserted(AWTEvent e);
	}
	
	public void addPatientListener(PatientListener l) {
		patientListeners.add(PatientListener.class, l);
	}
	
	public void removePatientListener(PatientListener listener) {
		patientListeners.remove(PatientListener.class, listener);
	}
	
	 private void firePatientInserted(Patient aPatient) {
	        AWTEvent event = new AWTEvent(aPatient, AWTEvent.RESERVED_ID_MAX + 1) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;};

	        EventListener[] listeners = patientListeners.getListeners(PatientListener.class);
	        for (int i = 0; i < listeners.length; i++)
	            ((PatientListener)listeners[i]).patientInserted(event);
	    }
	 
	
	private void firePatientUpdated(Patient aPatient) {
		AWTEvent event = new AWTEvent(aPatient, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = patientListeners.getListeners(PatientListener.class);
		for (int i = 0; i < listeners.length; i++)
			((PatientListener)listeners[i]).patientUpdated(event);
	}		
	
	 /*
	 private void firePatientUpdated() {
			AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {};
			
			EventListener[] listeners = patientListeners.getListeners(PatientListener.class);
			for (int i = 0; i < listeners.length; i++)
				((PatientListener)listeners[i]).patientUpdated(event);
	}
	 */
	 
	 
	private JPanel jContainPanel = null;
	private JPanel jDataPanel = null;
	private JPanel jButtonPanel = null;
	private JButton jOkButton = null;
	private JButton jCancelButton = null;
	private JTextField ageField = null;
	private Integer age=0;
	private JPanel jAgePanel = null;
	private JTextField jFirstNameTextField = null;
	private JPanel jSecondNamePanel = null;
	private JTextField jSecondNameTextField = null;
	private JPanel sexPanel = null;
	private ButtonGroup sexGroup=null;
	private String sexSelect=" ";
	private char sex='M';
	private boolean insert;
	private Patient patient;
	private JPanel jAddressPanel = null;
	private JLabel jAddressLabel = null;
	private JTextField jAddressTextField = null;
	private JPanel jCityPanel = null;
	private JLabel jCityLabel = null;
	private JTextField jCityTextField = null;
	private JPanel jTelPanel = null;
	private JLabel jTelLabel = null;
	private JTextField jTelephoneTextField = null;
	private JPanel jNextKinPanel = null;
	private JLabel jNextKinLabel = null;
	private JTextField jNextKinTextField = null;
//	private int oldAge;
	private PatientBrowserManager manager = new PatientBrowserManager();
	private JLabel jLabel1 = null;
	private JLabel jLabel = null;
	private JLabel jAgeLabel = null;
	private JPanel jFirstNamePanel = null;
	private JPanel jLabelPanel = null;
	private JPanel jSexLabelPanel = null;
	private JLabel jLabel2 = null;
	private JPanel jSecondNamePanel1 = null;
	private JPanel jAgePanel1 = null;
	private JPanel jFirstName = null;
	private JPanel jPanel1 = null;
	private JPanel jSecondName = null;
	private JPanel jAge = null;
	private JPanel jPanel = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel5 = null;
	private JPanel jAdress = null;
	private JPanel jCity = null;
	private JPanel jNextKin = null;
	private JPanel jTelephone = null;
	private JPanel jDataContainPanel = null;
	private int pfrmBase = 2;
	private int pfrmWidth = 1;
	private int pfrmHeight =1;
	private int pfrmBordX;
	private int pfrmBordY;
	private JTextArea jNoteTextArea = null;
	private JPanel jNotePanel = null;
	private JScrollPane jNoteScrollPane = null;
	public void actionPerformed(ActionEvent e) {
		sexSelect=e.getActionCommand();
		
	}
	
	/**
	 * This method initializes 
	 * 
	 */
	public PatientInsert(JDialog owner, Patient old, boolean inserting) {
		super(owner, true);
		patient=old;
		insert=inserting;
		initialize();
	}
	
	public PatientInsert(JFrame owner, Patient old, boolean inserting) {
		super(owner, true);
		patient=old;
		insert=inserting;
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width - (screensize.width / pfrmBase * pfrmWidth)) / 
		2;
		pfrmBordY = (screensize.height - (screensize.height / pfrmBase * 
		pfrmHeight)) / 2;
		this.setBounds(pfrmBordX+10,pfrmBordY+10,screensize.width / pfrmBase * 
		pfrmWidth,screensize.height / pfrmBase * pfrmHeight);
		this.setContentPane(getJContainPanel());
		this.setTitle(MessageBundle.getMessage("angal.patient.newpatient"));
		this.setSize(new java.awt.Dimension(604,445));
		setSize(screensize.width / pfrmBase * pfrmWidth,
				screensize.height / pfrmBase * pfrmHeight);
		pack();
		//setVisible(true);
		setResizable(false);
	}
	
	/**
	 * This method initializes jContainPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContainPanel() {
		if (jContainPanel == null) {
			jContainPanel = new JPanel();
			jContainPanel.setLayout(new BorderLayout());
			jContainPanel.add(getJDataPanel(), java.awt.BorderLayout.NORTH);
			jContainPanel.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContainPanel;
	}
	
	/**
	 * This method initializes jDataPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJDataPanel() {
		if (jDataPanel == null) {
			jDataPanel = new JPanel();
			jDataPanel.setLayout(new BoxLayout(getJDataPanel(),BoxLayout.Y_AXIS));
			jDataPanel.add(getJDataContainPanel(), null);
			pack();
		}
		return jDataPanel;
	}
	
	/**
	 * This method initializes jButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.add(getJOkButton(), null);
			jButtonPanel.add(getJCancelButton(), null);
		}
		return jButtonPanel;
	}
	
	/**
	 * This method initializes jOkButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJOkButton() {
		if (jOkButton == null) {
			jOkButton = new JButton();
			jOkButton.setText(MessageBundle.getMessage("angal.common.ok"));
			jOkButton.setMnemonic(KeyEvent.VK_A+('O'-'A')); 
			jOkButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean ok = true;
					boolean result = false;
					if (insert) {
						if (jFirstNameTextField.getText().equals("")) {
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patient.insertfirstname"));
						} else {
							if (jSecondNameTextField.getText().equals("")) {
								JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patient.insertsecondname"));
							} else {	
								if (age == -1) {
									JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patient.insertvalidage"));
								} else {
									String name = jFirstNameTextField.getText() + " " + jSecondNameTextField.getText();
									try{
										if (manager.isPatientPresent(name)) {										
											switch (JOptionPane.showConfirmDialog(null, 
													MessageBundle.getMessage("angal.patient.thepatientisalreadypresent") + ". /n" +
															MessageBundle.getMessage("angal.patient.doyouwanttocontinue") + "?", 
															MessageBundle.getMessage("angal.patient.select"), JOptionPane.YES_NO_OPTION)) {
															case JOptionPane.OK_OPTION:
																ok=true;
																break;
															case JOptionPane.NO_OPTION:
																ok=false;
																break;									
											}
										}
									}catch(OHServiceException ex){
                                        OHServiceExceptionUtil.showMessages(ex);
									}
									if (ok) {
										patient.setFirstName(jFirstNameTextField.getText());
										patient.setSecondName(jSecondNameTextField.getText());
										patient.setAge(age.intValue());
									
										if (sexSelect.equals(MessageBundle.getMessage("angal.patient.female"))) {
											sex='F';
										} else {
											sex='M';
										}
										patient.setSex(sex);
										patient.setAddress(jAddressTextField.getText());
										patient.setCity(jCityTextField.getText());
										patient.setNextKin(jNextKinTextField.getText());
										patient.setTelephone(jTelephoneTextField.getText());
										patient.setNote(jNoteTextArea.getText());
										
										//PatientExtended Compatibilty
										patient.setBirthDate(null);
										patient.setAgetype("");
										patient.setMother_name("");
										patient.setMother('U');
										patient.setFather_name("");
										patient.setFather('U');
										patient.setBloodType("");
										patient.setHasInsurance('U');
										patient.setParentTogether('U');
										
										try{
											result = manager.newPatient(patient);
										}catch(OHServiceException ex){
                                            OHServiceExceptionUtil.showMessages(ex);
										}
										if (result) {
											firePatientInserted(patient);
										}
										if (!result) {
											JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
										} else { 
											dispose();
										}
									}
									}
							}
						}
					}else{//Update
						String name= jFirstNameTextField.getText()+" "+jSecondNameTextField.getText();
						if(!(patient.getName().equals(name))){
							try{
								if(manager.isPatientPresent(name)){										
									switch (JOptionPane.showConfirmDialog(null, 
											MessageBundle.getMessage("angal.patient.thepatientisalreadypresent") + ". /n" +
													MessageBundle.getMessage("angal.patient.doyouwanttocontinue") + "?", 
													MessageBundle.getMessage("angal.patient.select"), JOptionPane.YES_NO_OPTION)) {
													case JOptionPane.OK_OPTION:
														ok=true;
														break;
													case JOptionPane.NO_OPTION:
														ok=false;
														break;									
									}
								}
							}catch(OHServiceException ex){
                                OHServiceExceptionUtil.showMessages(ex);
							}
						}else{
							ok=true;
						}
						if(ok){
						
						patient.setFirstName(jFirstNameTextField.getText());
						patient.setSecondName(jSecondNameTextField.getText());
						patient.setAge(age.intValue());
						if (sexSelect.equals(" ")) {
							sex=patient.getSex();
						}else if(sexSelect.equals(MessageBundle.getMessage("angal.patient.female"))) {
							sex='F';
						}else{
							sex='M';
						};
						patient.setSex(sex);
						patient.setAddress(jAddressTextField.getText());
						patient.setCity(jCityTextField.getText());
						patient.setNextKin(jNextKinTextField.getText());
						patient.setTelephone(jTelephoneTextField.getText());
						patient.setNote(jNoteTextArea.getText());
						
						//PatientExtended Compatibilty: NO NEEDED on Edit
						//patient.setBirthDate("");
						//patient.setAgetype("");
						//patient.setMother_name("");
						//patient.setMother('U');
						//patient.setFather_name("");
						//patient.setFather('U');
						//patient.setBloodType("");
						//patient.setHasInsurance('U');
						//patient.setParentTogether('U');*/
						try{
							result = manager.updatePatient(patient);
						}catch(OHServiceException ex){
                            OHServiceExceptionUtil.showMessages(ex);
						}
						if (result) {
							firePatientUpdated(patient);
						}
						if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
						else  dispose();
						
					}
					};					
					
				}
			});
			
			
		}
		return jOkButton;
	}
	
	/**
	 * This method initializes jCancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCancelButton() {
		if (jCancelButton == null) {
			jCancelButton = new JButton();
			jCancelButton.setText(MessageBundle.getMessage("angal.common.cancel"));
			jCancelButton.setMnemonic(KeyEvent.VK_A+('C'-'A')); 
			jCancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return jCancelButton;
	}
	
	
	/**
	 * This method initializes ageField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	
	
	private JTextField getAgeField() {
		if (ageField == null) {
			ageField = new JTextField(15);
			ageField.setText("0");
			ageField.setMaximumSize(new Dimension(20, 50));
			if (insert) {
				age = -1;
				ageField.setText("");
			} else {
				//Integer oldage = parseInt(patient.getAge());
				ageField.setText(String.valueOf(patient.getAge()));
				age = new Integer(patient.getAge());
			}
			ageField.setMinimumSize(new Dimension(100, 50));
		}
		ageField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {				
				try {				
					//ageField.setText(new StringTokenizer(ageField.getText()).nextToken());
					age = Integer.parseInt(ageField.getText());
					if ((age < 0)||(age > 200)) {
						ageField.setText("0");
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patient.insertvalidage"));
					}
				} catch (NumberFormatException ex) {
					//ageField.setText("0");
					JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patient.insertvalidage"));
				}
				
			}
			
			public void focusGained(FocusEvent e) {
			}
		});
		return ageField;
	}
	
	/**
	 * This method initializes jAgePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAgePanel() {
		if (jAgePanel == null) {
			jAgePanel = new JPanel();
			jAgePanel.add(getJAgeLabel(), BorderLayout.EAST);
		}
		return jAgePanel;
	}
	
	/**
	 * This method initializes jFirstNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJFirstNameTextField() {
		if (jFirstNameTextField == null) {
			jFirstNameTextField = new JTextField(15);
			if(!insert)jFirstNameTextField.setText(patient.getFirstName());
		}
		return jFirstNameTextField;
	}
	
	/**
	 * This method initializes jSecondNamePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSecondNamePanel() {
		if (jSecondNamePanel == null) {
			jSecondNamePanel = new JPanel();
			jSecondNamePanel.add(getJLabel(), BorderLayout.EAST);
		}
		return jSecondNamePanel;
	}
	
	/**
	 * This method initializes jSecondNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJSecondNameTextField() {
		if (jSecondNameTextField == null) {
			jSecondNameTextField = new JTextField(15);
			if(!insert)jSecondNameTextField.setText(patient.getSecondName());		
			
		}
		return jSecondNameTextField;
	}
	
	/**
	 * This method initializes jSexPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSexPanel() {
		if (sexPanel == null) {			
			sexPanel = new JPanel();
			sexGroup=new ButtonGroup();
			JRadioButton radiom= new JRadioButton(MessageBundle.getMessage("angal.patient.male"));
			JRadioButton radiof= new JRadioButton(MessageBundle.getMessage("angal.patient.female"));
			radiom.setMnemonic(KeyEvent.VK_A+('M'-'A')); 
			radiof.setMnemonic(KeyEvent.VK_A+('F'-'A')); 
			sexPanel.add(getJSexLabelPanel(), null);
			sexPanel.add(radiom, radiom.getName());
			if(insert){
				radiom.setSelected(true);
			}
			else{
				if(patient.getSex()=='F')
					radiof.setSelected(true);
				else radiom.setSelected(true);
			}			
			radiom.addActionListener(this);
			radiof.addActionListener(this);
			sexGroup.add(radiom);
			sexGroup.add(radiof);
			sexPanel.add(radiof);
			
		}
		return sexPanel;
	}
	
	/**
	 * This method initializes jAdressPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAddressPanel() {
		if (jAddressPanel == null) {
			jAddressLabel = new JLabel();
			jAddressLabel.setText(MessageBundle.getMessage("angal.patient.address"));
			jAddressPanel = new JPanel();
			jAddressPanel.add(jAddressLabel, BorderLayout.EAST);
		}
		return jAddressPanel;
	}
	
	/**
	 * This method initializes jAdressTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJAdressTextField() {
		if (jAddressTextField == null) {
			jAddressTextField = new JTextField(15);
			if(!insert)jAddressTextField.setText(patient.getAddress());
		}
		return jAddressTextField;
	}
	
	/**
	 * This method initializes jCityPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJCityPanel() {
		if (jCityPanel == null) {
			jCityLabel = new JLabel();
			jCityLabel.setText(MessageBundle.getMessage("angal.patient.city"));
			jCityPanel = new JPanel();		
			jCityPanel.add(jCityLabel, BorderLayout.EAST);
		}
		return jCityPanel;
	}
	
	/**
	 * This method initializes jCityTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJCityTextField() {
		if (jCityTextField == null) {
			jCityTextField = new JTextField(15);
			if(!insert)jCityTextField.setText(patient.getCity());
		}
		return jCityTextField;
	}
	
	/**
	 * This method initializes jTelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJTelPanel() {
		if (jTelPanel == null) {
			jTelLabel = new JLabel();
			jTelLabel.setText(MessageBundle.getMessage("angal.patient.telephone"));
			jTelPanel = new JPanel();				
			jTelPanel.add(jTelLabel,  BorderLayout.EAST);
		}
		return jTelPanel;
	}
	
	/**
	 * This method initializes jTelephoneTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTelephoneTextField() {
		if (jTelephoneTextField == null) {
			jTelephoneTextField = new JTextField(15);
			if(!insert)jTelephoneTextField.setText(patient.getTelephone());
		}
		return jTelephoneTextField;
	}
	
	/**
	 * This method initializes jNextKidPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJNextKinPanel() {
		if (jNextKinPanel == null) {
			jNextKinLabel = new JLabel();
			jNextKinLabel.setText(MessageBundle.getMessage("angal.patient.nextkin"));
			jNextKinPanel = new JPanel();			
			jNextKinPanel.add(jNextKinLabel, BorderLayout.EAST);
		}
		return jNextKinPanel;
	}
	
	/**
	 * This method initializes jNextKinTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJNextKinTextField() {
		if (jNextKinTextField == null) {
			jNextKinTextField = new JTextField(15);
			if(!insert)jNextKinTextField.setText(patient.getNextKin());
		}
		return jNextKinTextField;
	}
	
	
	
	
	
	/**
	 * This method initializes jLabel1	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText(MessageBundle.getMessage("angal.patient.firstname"));
		}
		return jLabel1;
	}

	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setText(MessageBundle.getMessage("angal.patient.secondname"));
		}
		return jLabel;
	}

	/**
	 * This method initializes jAgeLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJAgeLabel() {
		if (jAgeLabel == null) {
			jAgeLabel = new JLabel();
			jAgeLabel.setText(MessageBundle.getMessage("angal.patient.age") + " *");
		}
		return jAgeLabel;
	}

	/**
	 * This method initializes jFirstNamePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJFirstNamePanel() {
		if (jFirstNamePanel == null) {
			jFirstNamePanel = new JPanel();
			jFirstNamePanel.add(getJLabel1(), BorderLayout.EAST);
		}
		return jFirstNamePanel;
	}

	/**
	 * This method initializes jLabelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJLabelPanel() {
		if (jLabelPanel == null) {
			jLabelPanel = new JPanel();
			jLabelPanel.setLayout(new BoxLayout(getJLabelPanel(),BoxLayout.Y_AXIS));
			jLabelPanel = setMyBorder(jLabelPanel,"");
			jLabelPanel.add(getJFirstName(), null);
			jLabelPanel.add(getJSecondName(), null);
			jLabelPanel.add(getJAge(), null);
			jLabelPanel.add(getSexPanel(), null);
			jLabelPanel.add(getJAdress(), null);
			jLabelPanel.add(getJCity(), null);
			jLabelPanel.add(getJNextKin(), null);
			jLabelPanel.add(getJTelephone(), null);
		}
		return jLabelPanel;
	}

	/**
	 * This method initializes jSexLabelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSexLabelPanel() {
		if (jSexLabelPanel == null) {
			jLabel2 = new JLabel();
			jLabel2.setText(MessageBundle.getMessage("angal.patient.sexstar"));
			jSexLabelPanel = new JPanel();
			jSexLabelPanel.setLayout(new BorderLayout());
			
			jSexLabelPanel.add(jLabel2, BorderLayout.EAST);
		}
		return jSexLabelPanel;
	}

	/**
	 * This method initializes jSecondNamePanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSecondNamePanel1() {
		if (jSecondNamePanel1 == null) {
			jSecondNamePanel1 = new JPanel();
			jSecondNamePanel1.add(getJSecondNameTextField(), null);
		}
		return jSecondNamePanel1;
	}

	/**
	 * This method initializes jAgePanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAgePanel1() {
		if (jAgePanel1 == null) {
			jAgePanel1 = new JPanel();
			jAgePanel1.add(getAgeField(), null);
		}
		return jAgePanel1;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJFirstName() {
		if (jFirstName == null) {
			jFirstName = new JPanel();
			jFirstName.setLayout(new BorderLayout());
			jFirstName.add(getJFirstNamePanel(), BorderLayout.WEST);
			jFirstName.add(getJPanel1(), java.awt.BorderLayout.EAST);
		}
		return jFirstName;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.add(getJFirstNameTextField(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSecondName() {
		if (jSecondName == null) {
			jSecondName = new JPanel();
			jSecondName.setLayout(new BorderLayout());
			jSecondName.add(getJSecondNamePanel(), java.awt.BorderLayout.WEST);
			jSecondName.add(getJSecondNamePanel1(), java.awt.BorderLayout.EAST);
		}
		return jSecondName;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAge() {
		if (jAge == null) {
			jAge = new JPanel();
			jAge.setLayout(new BorderLayout());
			jAge.add(getJAgePanel(), java.awt.BorderLayout.WEST);
			jAge.add(getJAgePanel1(), java.awt.BorderLayout.EAST);
		}
		return jAge;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getJAdressTextField(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.add(getJCityTextField(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.add(getJNextKinTextField(), null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.add(getJTelephoneTextField(), null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jAdress	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAdress() {
		if (jAdress == null) {
			jAdress = new JPanel();
			jAdress.setLayout(new BorderLayout());
			jAdress.add(getJAddressPanel(), java.awt.BorderLayout.WEST);
			jAdress.add(getJPanel(), java.awt.BorderLayout.EAST);
			
		}
		return jAdress;
	}

	/**
	 * This method initializes jCity	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJCity() {
		if (jCity == null) {
			jCity = new JPanel();
			jCity.setLayout(new BorderLayout());
			jCity.add(getJCityPanel(), java.awt.BorderLayout.WEST);
			jCity.add(getJPanel2(), java.awt.BorderLayout.EAST);
		}
		return jCity;
	}

	/**
	 * This method initializes jNextKin	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJNextKin() {
		if (jNextKin == null) {
			jNextKin = new JPanel();
			jNextKin.setLayout(new BorderLayout());
			jNextKin.add(getJNextKinPanel(), java.awt.BorderLayout.WEST);
			jNextKin.add(getJPanel3(), java.awt.BorderLayout.EAST);
		}
		return jNextKin;
	}

	/**
	 * This method initializes jPanel6	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJTelephone() {
		if (jTelephone == null) {
			jTelephone = new JPanel();
			jTelephone.setLayout(new BorderLayout());
			jTelephone.add(getJTelPanel(), java.awt.BorderLayout.WEST);
			jTelephone.add(getJPanel5(), java.awt.BorderLayout.EAST);
		}
		return jTelephone;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJDataContainPanel() {
		if (jDataContainPanel == null) {
			jDataContainPanel = new JPanel();
			if(!insert){
				jDataContainPanel = setMyBorderCenter(jDataContainPanel,patient.getName());
			}else{
				jDataContainPanel = setMyBorderCenter(jDataContainPanel,MessageBundle.getMessage("angal.patient.insertdataofnewpatient"));
			}
			jDataContainPanel.setLayout(new BorderLayout());
			jDataContainPanel.add(getJLabelPanel(), BorderLayout.CENTER);
			jDataContainPanel.add(getJNotePanel(), BorderLayout.EAST);
		}
		return jDataContainPanel;
	}



	

	/**
	 * set a specific border+title to a panel
	 */
	private JPanel setMyBorder(JPanel c, String title){
		javax.swing.border.Border b1 = BorderFactory.createLineBorder(Color.lightGray);
		
		javax.swing.border.Border b2 = BorderFactory.createTitledBorder(b1,title,
	             javax.swing.border.TitledBorder.LEFT,javax.swing.border.TitledBorder.TOP);
	            
		c.setBorder(b2);
		return c;
	}
	
	private JPanel setMyBorderCenter(JPanel c, String title){
		javax.swing.border.Border b1 = BorderFactory.createLineBorder(Color.lightGray);
		
		javax.swing.border.Border b2 = BorderFactory.createTitledBorder(b1,title,
	             javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.TOP);
	            
		c.setBorder(b2);
		return c;
	}
	

	/**
	 * This method initializes jTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextArea getJTextArea() {
		if (jNoteTextArea == null) {
			jNoteTextArea = new JTextArea(40,30);
			if (!insert){
				jNoteTextArea.setText(patient.getNote());
			}
			jNoteTextArea.setLineWrap(true);
			jNoteTextArea.setPreferredSize(new Dimension(jNoteTextArea.getSize()));	
			jNoteTextArea.setAutoscrolls(true);
		}
		return jNoteTextArea;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJNotePanel() {
		
		if (jNoteScrollPane == null && (jNotePanel == null)) {
			JScrollPane jNoteScrollPane = new JScrollPane(getJTextArea());
			
			jNoteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jNoteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jNoteScrollPane.createVerticalScrollBar();
			jNoteScrollPane.setAutoscrolls(true);
			jNoteScrollPane.setPreferredSize(new Dimension(200, 350));
			jNoteScrollPane.validate();
			//jNoteScrollPane.setBackground(Color.blue);
			
			jNotePanel = new JPanel();
			jNotePanel= setMyBorder(jNotePanel,MessageBundle.getMessage("angal.patient.note"));
			//jNotePanel.add(getJTextArea(), null);
			jNotePanel.add(jNoteScrollPane);

		}
		return jNotePanel;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="378,59"
