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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
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
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoDateTextField;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;


public class OpdEdit extends JDialog {
	
	private static final long serialVersionUID = -7369841416710920082L;

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

			private static final long serialVersionUID = -2831804524718368850L;
			
		};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryInserted(event, opd);
	}
	private void fireSurgeryUpdated(Opd opd) {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			private static final long serialVersionUID = -1073238832996429931L;
			
		};
		
		EventListener[] listeners = surgeryListeners.getListeners(SurgeryListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SurgeryListener)listeners[i]).surgeryUpdated(event, opd);
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
	private int oldAge;
	private DiseaseType allType = new DiseaseType(
			MessageBundle.getMessage("angal.opd.alltype"),
			MessageBundle.getMessage("angal.opd.alltype")
	);
	
	/*
	 * Managers and Arrays
	 */
	private DiseaseTypeBrowserManager typeManager = new DiseaseTypeBrowserManager();
	private DiseaseBrowserManager diseaseManager = new DiseaseBrowserManager();
	private OpdBrowserManager opdManager = new OpdBrowserManager();
	private ArrayList<DiseaseType> types;
	private ArrayList<Disease> diseasesAll;
	
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
	 * 
	 */
	public OpdEdit(JFrame owner,Opd old,boolean inserting) {
		super(owner, true);
		opd = old;
		insert = inserting;
		try{
			types = typeManager.getDiseaseType();
			diseasesAll = diseaseManager.getDiseaseAll();
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		if (!insert)
			oldAge = opd.getAge();
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
			insertPanel.add(getDiseaseBox1(), null);
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
			diseaseTypeBox.addItem(allType);
			if(types != null){
				for (DiseaseType elem : types) {
					if (!insert && opd.getDisease().getType() != null){
						if(opd.getDisease().getType().equals(elem.getCode())){
							elem2=elem;}
					}
					diseaseTypeBox.addItem(elem);
				}
			}
			if (elem2!=null) { 
				diseaseTypeBox.setSelectedItem(elem2);
			} else {
				diseaseTypeBox.setSelectedIndex(0);
			}
			
			diseaseTypeBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					diseaseBox.removeAllItems();
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
	public JComboBox getDiseaseBox1() {
		if (diseaseBox == null) {
			diseaseBox = new JComboBox();
			diseaseBox.setMaximumSize(new Dimension(400, 50));
		}
		Disease thisDiseaseEdit = null;
		ArrayList<Disease> diseases = null;
		try {
			if (diseaseTypeBox.getSelectedIndex() == 0) {
				diseases = diseaseManager.getDiseaseOpd();
			}else{
				String code = ((DiseaseType)diseaseTypeBox.getSelectedItem()).getCode();
				diseases = diseaseManager.getDiseaseOpd(code);
			}
		}catch(OHServiceException e){
			OHServiceExceptionUtil.showMessages(e);
		}
		diseaseBox.addItem("");
		if(diseases != null){
			for (Disease elem : diseases) {
				diseaseBox.addItem(elem);
				if (!insert && opd.getDisease() != null) {
					if (opd.getDisease().getCode().equals(elem.getCode())) {
						thisDiseaseEdit = elem;}
				}
			}
		}
		if (!insert) {
			if (thisDiseaseEdit != null) {
				diseaseBox.setSelectedItem(thisDiseaseEdit);
			} else { //try in the cancelled diseases
				if (opd.getDisease() != null) {
					for (Disease elem : diseasesAll) {
						if (opd.getDisease().getCode().equals(elem.getCode())) {
							JOptionPane.showMessageDialog(OpdEdit.this,MessageBundle.getMessage("angal.opd.disease1mayhavebeencancelled"));
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
		ArrayList<Disease> diseases = null;
		DiseaseBrowserManager manager = new DiseaseBrowserManager();
		try {
			diseases = manager.getDiseaseOpd();
		}catch(OHServiceException e){
			OHServiceExceptionUtil.showMessages(e);
		}
		diseaseBox2.addItem("");
		if(diseases != null){
			for (Disease elem : diseases) {
				diseaseBox2.addItem(elem);
				if(!insert && opd.getDisease2()!=null){
					if (opd.getDisease2().getCode().equals(elem.getCode())) {
						elem2 = elem;}
				}
			}
		}
		if (elem2!= null) {
			diseaseBox2.setSelectedItem(elem2);
		} else { //try in the cancelled diseases
			if (opd.getDisease2()!=null) {
				if(diseasesAll != null){
					for (Disease elem : diseasesAll) {
						if (opd.getDisease2().getCode().equals(elem.getCode())) {
							JOptionPane.showMessageDialog(OpdEdit.this,MessageBundle.getMessage("angal.opd.disease2mayhavebeencancelled"));
							diseaseBox2.addItem(elem);
							diseaseBox2.setSelectedItem(elem);
						}
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
		ArrayList<Disease> diseases = null;
		DiseaseBrowserManager manager = new DiseaseBrowserManager();
		try {
			diseases = manager.getDiseaseOpd();
		}catch(OHServiceException e){
			OHServiceExceptionUtil.showMessages(e);
		}
		diseaseBox3.addItem("");
		if(diseases != null){
			for (Disease elem : diseases) {
				diseaseBox3.addItem(elem);
				if(!insert && opd.getDisease3()!=null){
					if (opd.getDisease3().getCode().equals(elem.getCode())) {
						elem2 = elem;}
				}
			}
		}
		if (elem2!= null) {
			diseaseBox3.setSelectedItem(elem2);
		} else { //try in the cancelled diseases
			if (opd.getDisease3()!=null) {
				if(diseasesAll != null){
					for (Disease elem : diseasesAll) {
						if (opd.getDisease3().getCode().equals(elem.getCode())) {
							JOptionPane.showMessageDialog(OpdEdit.this,MessageBundle.getMessage("angal.opd.disease3mayhavebeencancelled"));
							diseaseBox3.addItem(elem);
							diseaseBox3.setSelectedItem(elem);
						}
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
					boolean result = false;
					GregorianCalendar gregDate = new GregorianCalendar();
					char newPatient=' ';
					String referralTo="";
					String referralFrom="";
					Disease disease=null;
					Disease disease2=null;
					Disease disease3=null;

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
						JOptionPane.showMessageDialog(OpdEdit.this, MessageBundle.getMessage("angal.opd.pleaseinsertattendancedate"));
						return;
					}
					else {
						try {
							currentDateFormat.setLenient(false);
							Date myDate = currentDateFormat.parse(d);
							gregDate.setTime(myDate);
						} catch (ParseException pe) {
							JOptionPane.showMessageDialog(OpdEdit.this,
									MessageBundle.getMessage("angal.opd.pleaseinsertavalidattendancedate"));
							return;
						}
					}
					
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
					opd.setNote("");
					opd.setUserID(UserBrowsingManager.getCurrentUser());
					
					try {
						if (insert){    //Insert
							GregorianCalendar date = new GregorianCalendar();
							opd.setProgYear(opdManager.getProgYear(date.get(GregorianCalendar.YEAR))+1);

							//remember for later use
							RememberDates.setLastOpdVisitDate(gregDate);
							
							result = opdManager.newOpd(opd);
							if (result) {
								fireSurgeryInserted(opd);
								dispose();
							} else 
								JOptionPane.showMessageDialog(OpdEdit.this, 
									MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
						}
						else {    //Update

							Opd updatedOpd = opdManager.updateOpd(opd);
							if (updatedOpd != null) {
								fireSurgeryUpdated(updatedOpd);
								dispose();
							} else 
								JOptionPane.showMessageDialog(OpdEdit.this, 
									MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
						}
					} catch(OHServiceException ex){
						OHServiceExceptionUtil.showMessages(ex);
					}
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
						JOptionPane.showMessageDialog(OpdEdit.this, MessageBundle.getMessage("angal.opd.insertvalidage"));
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
            jDiseasePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			jLabel1 = new JLabel();
			jLabel1.setText(MessageBundle.getMessage("angal.opd.diagnosis"));
			jDiseasePanel.add(jLabel1);
                        
            searchDiseaseTextField = new JTextField();
            jDiseasePanel.add(searchDiseaseTextField);
            searchDiseaseTextField.setColumns(10);
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
            //...
            
            searchDiseaseButton = new JButton("");
            jDiseasePanel.add(searchDiseaseButton);
            searchDiseaseButton.setPreferredSize(new Dimension(20, 20));
            searchDiseaseButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
            searchDiseaseButton.addActionListener(new ActionListener() {
                ArrayList<Disease> diseasesOPD = null;
                public void actionPerformed(ActionEvent arg0) {
                    try {
                        diseasesOPD = diseaseManager.getDiseaseOpd();
                    } catch (OHServiceException ex) {
                        OHServiceExceptionUtil.showMessages(ex);
                    }
                    diseaseBox.removeAllItems();
                    diseaseBox.addItem("");
                    for(Disease disease: 
                            getSearchDiagnosisResults(searchDiseaseTextField.getText(), 
                                    diseasesOPD == null? diseasesAll : diseasesOPD)) {
                        diseaseBox.addItem(disease);
                    }

                    if(diseaseBox.getItemCount() >= 2){
                        diseaseBox.setSelectedIndex(1);
                    }
                    diseaseBox.requestFocus();
                    if(diseaseBox.getItemCount() > 2){
                        diseaseBox.showPopup();
                    }
                }
            });
                        
		}
		return jDiseasePanel;
	}
	public JPanel getJDiseasePanel2() {
        if (jDiseasePanel2 == null){
			jDiseasePanel2 = new JPanel();
	                jDiseasePanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
			jLabelDis2 = new JLabel();
			jLabelDis2.setText(MessageBundle.getMessage("angal.opd.diagnosisnfulllist"));
			jDiseasePanel2.add(jLabelDis2);
	                
	                searchDiseaseTextField2 = new JTextField();
	                        jDiseasePanel2.add(searchDiseaseTextField2);
	                        searchDiseaseTextField2.setColumns(10);
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
	                        //...
	                        
	                        searchDiseaseButton2 = new JButton("");
	                        jDiseasePanel2.add(searchDiseaseButton2);
	                        searchDiseaseButton2.setPreferredSize(new Dimension(20, 20));
	                        searchDiseaseButton2.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
	                        searchDiseaseButton2.addActionListener(new ActionListener() {
	                            ArrayList<Disease> diseasesOPD = null;
	                            public void actionPerformed(ActionEvent arg0) {
	                                try {
	                                    diseasesOPD = diseaseManager.getDiseaseOpd();
	                                } catch (OHServiceException ex) {
	                                    OHServiceExceptionUtil.showMessages(ex);
	                                }
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
	                            }
	                        });
            }
        return jDiseasePanel2;
	}
	public JPanel getJDiseasePanel3() {
		if (jDiseasePanel3 == null){
			jDiseasePanel3 = new JPanel();
			jDiseasePanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
			jLabelDis3 = new JLabel();
			jLabelDis3.setText(MessageBundle.getMessage("angal.opd.diagnosisnfulllist3"));
			jDiseasePanel3.add(jLabelDis3);
                        
            searchDiseaseTextField3 = new JTextField();
            jDiseasePanel3.add(searchDiseaseTextField3);
            searchDiseaseTextField3.setColumns(10);
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
            //...
            
            searchDiseaseButton3 = new JButton("");
            jDiseasePanel3.add(searchDiseaseButton3);
            searchDiseaseButton3.setPreferredSize(new Dimension(20, 20));
            searchDiseaseButton3.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
            searchDiseaseButton3.addActionListener(new ActionListener() {
                ArrayList<Disease> diseasesOPD = null;
                public void actionPerformed(ActionEvent arg0) {
                    try {
                        diseasesOPD = diseaseManager.getDiseaseOpd();
                    } catch (OHServiceException ex) {
                        OHServiceExceptionUtil.showMessages(ex);
                    }
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
            });
		}
		return jDiseasePanel3;
	}

	public JPanel getJDiseaseTypePanel() {
		if (jDiseaseTypePanel == null){
			jDiseaseTypePanel = new JPanel();
			jDiseaseTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
	
}  //  @jve:decl-index=0:visual-constraint="10,10"  