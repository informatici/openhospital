package org.isf.patient.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import org.isf.agetype.manager.AgeTypeBrowserManager;
import org.isf.agetype.model.AgeType;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.generaldata.SmsParameters;
import org.isf.patient.gui.PatientInsertExtended.PatientListener;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.video.gui.PatientPhotoPanel;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.toedter.calendar.JDateChooser;

/*------------------------------------------
 * PatientInsertExtended - model for the patient entry
 * -----------------------------------------
 * modification history
 * 11/08/2008 - alessandro - added mother and father names textfield
 * 11/08/2008 - alessandro - changed economicStatut -> hasInsurance
 * 19/08/2008 - mex        - changed educational level with blood type
 * 26/08/2008 - cla		   - added calendar for calculating age
 * 						   - modified age field from int to varchar
 * 28/08/2008 - cla		   - added tooltip for age field and cheching name and age for patient editing
 * 05/09/2008 - alex       - added patient code
 * 01/01/2009 - Fabrizio   - modified assignment to age field to set an int value
 *------------------------------------------*/


public class PatientInsertExtended extends JDialog {

	private static final long serialVersionUID = -827831581202765055L;

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

			private static final long serialVersionUID = -6853617821516727564L;

		};

		EventListener[] listeners = patientListeners.getListeners(PatientListener.class);
		for (int i = 0; i < listeners.length; i++) {
			((PatientListener) listeners[i]).patientInserted(event);
		}
	}

	private void firePatientUpdated(Patient aPatient) {
		AWTEvent event = new AWTEvent(aPatient, AWTEvent.RESERVED_ID_MAX + 1) {

			private static final long serialVersionUID = 7777830932867901993L;

		};

		EventListener[] listeners = patientListeners.getListeners(PatientListener.class);
		for (int i = 0; i < listeners.length; i++)
			((PatientListener) listeners[i]).patientUpdated(event);
	}
	
	// COMPONENTS: Main
	private JPanel jMainPanel = null;
	private boolean insert;
	private boolean justSave;
	final private Patient patient;
	private PatientBrowserManager manager = new PatientBrowserManager();

	// COMPONENTS: Data
	private JPanel jDataPanel = null;

	// COMPONENTS: Anagraph
	private JPanel jDataContainPanel = null;
	private JPanel jAnagraphPanel = null;

	// First Name Components:
	private JPanel jFirstName = null;
	private JPanel jFirstNameLabelPanel = null;
	private JPanel jFirstNameFieldPanel = null;
	private JLabel jFirstNameLabel = null;
	private JTextField jFirstNameTextField = null;

	// Second Name Components:
	private JPanel jSecondName = null;
	private JPanel jSecondNameLabelPanel = null;
	private JPanel jSecondNameFieldPanel = null;
	private JLabel jSecondNameLabel = null;
	private JTextField jSecondNameTextField = null;

	// AgeTypeSelection:
	private JPanel jAgeType = null;
	private JPanel jAgeTypeButtonGroup = null;
	private JPanel jAgeTypeSelection = null;
	private ButtonGroup ageTypeGroup = null;
	private JPanel jAgeType_BirthDatePanel = null;
	private JRadioButton jAgeType_Age = null;
	private JRadioButton jAgeType_BirthDate = null;
	private JRadioButton jAgeType_Description = null;

	// Age Components:
	private JPanel jAge = null;
	private JTextField jAgeYears = null;
	private JTextField jAgeMonths = null;
	private JTextField jAgeDays = null;
	private int years;
	private int months;
	private int days;

	// BirthDate Components:
	private JPanel jBirthDate = null;
	private JPanel jBirthDateLabelPanel = null;
	private JLabel jBirthDateLabel = null;
	private JPanel jBirthDateGroupPanel = null;
	private Calendar cBirthDate = null;
	private JButton jBirthDateReset = null;
	private JLabel jBirthDateAge = null;

	// AgeDescription Components:
	private int ageType;
	private int ageTypeMonths;
	private JPanel jAgeDesc = null;
	private JPanel jAgeDescPanel = null;
	private JPanel jAgeMonthsPanel = null;
	private JComboBox jAgeDescComboBox = null;
	private JComboBox jAgeMonthsComboBox = null;
	private JLabel jAgeMonthsLabel = null;

	// Sex Components:
	private JPanel jSexPanel = null;
	private ButtonGroup sexGroup = null;
	private JPanel jSexLabelPanel = null;
	private JLabel jSexLabel = null;
	private JRadioButton radiof = null;
	private JRadioButton radiom = null;

	// Address Components:
	private JPanel jAddress = null;
	private JPanel jAddressLabelPanel = null;
	private JPanel jAddressFieldPanel = null;
	private JLabel jAddressLabel = null;
	private JTextField jAddressTextField = null;

	// Address Components:
	private JPanel jTaxCodePanel = null;
	private JPanel jTaxCodeLabelPanel = null;
	private JPanel jTaxCodeFieldPanel = null;
	private JLabel jTaxCodeLabel = null;
	private JTextField jTaxCodeTextField = null;

	// City Components:
	private JPanel jCity = null;
	private JPanel jCityLabelPanel = null;
	private JPanel jCityFieldPanel = null;
	private JLabel jCityLabel = null;
	private JTextField jCityTextField = null;

	// NextKin Components:
	private JPanel jNextKin = null;
	private JPanel jNextKinLabelPanel = null;
	private JPanel jNextKinFieldPanel = null;
	private JLabel jNextKinLabel = null;
	private JTextField jNextKinTextField = null;

	// Telephone Components:
	private JPanel jTelephone = null;
	private JPanel jTelephoneLabelPanel = null;
	private JPanel jTelephoneFieldPanel = null;
	private JLabel jTelephoneLabel = null;
	private JTextField jTelephoneTextField = null;

	// COMPONENTS: Extension
	private JPanel jExtensionContent = null;

	// BloodType Components:
	private JPanel jBloodTypePanel = null;
	private JComboBox jBloodTypeComboBox = null;

	// Father Components:
	private JPanel jFatherPanelOptions;
	private JPanel jFatherPanel = null; // added
	private JPanel jFatherNamePanel = null; // added
	private JTextField jFatherNameTextField = null; // added
	private ButtonGroup fatherGroup = null;
	private JPanel jFatherAlivePanel = null;
	private JRadioButton jFather_Dead = null;
	private JRadioButton jFather_Alive = null;
	private JRadioButton jFather_Unknown = null;

	// Mother Components:
	private JPanel jMotherOptions;
	private JPanel jMotherPanel = null; // added
	private JPanel jMotherNamePanel = null; // added
	private JTextField jMotherNameTextField = null; // added
	private ButtonGroup motherGroup = null;
	private JPanel jMotherAlivePanel = null;
	private JRadioButton jMother_Dead = null;
	private JRadioButton jMother_Alive = null;
	private JRadioButton jMother_Unknown = null;

	// ParentTogether Components:
	private JPanel jParentPanel = null;
	private ButtonGroup parentGroup = null;
	private JPanel jParentNoPanel = null;
	private JRadioButton jParent_Yes = null;
	private JRadioButton jParent_No = null;
	private JRadioButton jParent_Unknown = null;

	// private ButtonGroup eduLevelGroup=null; //removed

	// HasInsurance Components:
	private JPanel jInsurancePanel = null;
	private ButtonGroup insuranceGroup = null;
	private JPanel jInsuranceNoPanel = null;
	private JRadioButton jInsurance_Yes = null;
	private JRadioButton jInsurance_No = null;
	private JRadioButton jInsurance_Unknown = null;

	// COMPONENTS: Note
	private JPanel jRightPanel = null;
	private JScrollPane jNoteScrollPane = null;
	private JTextArea jNoteTextArea = null;

	// COMPONENTS: Buttons
	private JPanel jButtonPanel = null;
	private JButton jOkButton = null;
	private JButton jCancelButton = null;

	private JLabel labelRequiredFields;
	
	private PatientPhotoPanel photoPanel;
	

	/**
	 * This method initializes
	 * @param owner 
	 * 
	 */
	public PatientInsertExtended(JFrame owner, Patient old, boolean inserting) {
		super(owner, true);
		patient = old;
		insert = inserting;

		// if (!insert) {
		// lock = patient.getLock();
		// }

		initialize();
	}

	public PatientInsertExtended(JDialog owner, Patient old, boolean inserting) {
		super(owner, true);
		patient = old;
		insert = inserting;

		// if (!insert) {
		// lock = patient.getLock();
		// }

		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {

		this.setContentPane(getJContainPanel());
		if (insert)
			this.setTitle(MessageBundle.getMessage("angal.patient.title"));
		else
			this.setTitle(MessageBundle.getMessage("angal.patient.titleedit"));
		this.setSize(new Dimension(604, 445));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContainPanel() {
		if (jMainPanel == null) {
			jMainPanel = new JPanel();
			jMainPanel.setLayout(new BorderLayout());
			jMainPanel.add(getJDataPanel(), BorderLayout.CENTER);
			jMainPanel.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jMainPanel;
	}

	/**
	 * This method initializes jMainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJDataPanel() {
		if (jDataPanel == null) {
			jDataPanel = new JPanel();
			jDataPanel.setLayout(new BoxLayout(jDataPanel, BoxLayout.X_AXIS));
			jDataPanel.add(getJDataContainPanel(), null);
			jDataPanel.add(getJRightPanel(), null);
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
			jOkButton.setMnemonic(KeyEvent.VK_A + ('O' - 'A'));
			jOkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean ok = true;
					boolean result = false;
					String firstName = jFirstNameTextField.getText().trim();
					String secondName = jSecondNameTextField.getText().trim();
					
					if (firstName.equals("")) {
						JOptionPane.showMessageDialog(PatientInsertExtended.this, MessageBundle.getMessage("angal.patient.insertfirstname"));
						return;
					}
					if (secondName.equals("")) {
						JOptionPane.showMessageDialog(PatientInsertExtended.this, MessageBundle.getMessage("angal.patient.insertsecondname"));
						return;
					}
					if (!checkAge()) {
						JOptionPane.showMessageDialog(PatientInsertExtended.this, MessageBundle.getMessage("angal.patient.insertage"));
						return;
					}
					if (insert) {
						String name = firstName + " " + secondName;
						try{
							if (manager.isPatientPresent(name)) {
								switch (JOptionPane.showConfirmDialog(null,
										MessageBundle.getMessage("angal.patient.thepatientisalreadypresent") + ". /n" + MessageBundle.getMessage("angal.patient.doyouwanttocontinue") + "?",
										MessageBundle.getMessage("angal.patient.select"), JOptionPane.YES_NO_OPTION)) {
										case JOptionPane.OK_OPTION:
											ok = true;
											break;
										case JOptionPane.NO_OPTION:
											ok = false;
											break;
								}
							}
						}catch(OHServiceException ex){
                            OHServiceExceptionUtil.showMessages(ex);
						}
						if (ok) {
							patient.setFirstName(firstName);
							patient.setSecondName(secondName);

							if (radiof.isSelected()) {
								patient.setSex('F');
							} else if (radiom.isSelected()) {
								patient.setSex('M');
							} else {
								JOptionPane.showMessageDialog(PatientInsertExtended.this, "Please select a sex");
								return;
							}
							patient.setTaxCode(jTaxCodeTextField.getText().trim());
							patient.setAddress(jAddressTextField.getText().trim());
							patient.setCity(jCityTextField.getText().trim());
							patient.setNextKin(jNextKinTextField.getText().trim());
							patient.setTelephone(jTelephoneTextField.getText().replaceAll(" ", ""));
							patient.setMother_name(jMotherNameTextField.getText().trim());
							if (jMother_Alive.isSelected()) {
								patient.setMother('A');
							} else {
								if (jMother_Dead.isSelected()) {
									patient.setMother('D');
								} else
									patient.setMother('U');
							}
							patient.setFather_name(jFatherNameTextField.getText().trim());
							if (jFather_Alive.isSelected()) {
								patient.setFather('A');
							} else {
								if (jFather_Dead.isSelected()) {
									patient.setFather('D');
								} else
									patient.setFather('U');
							}
							patient.setBloodType(jBloodTypeComboBox.getSelectedItem().toString());

							if (jInsurance_Yes.isSelected()) {
								patient.setHasInsurance('Y');
							} else {
								if (jInsurance_No.isSelected()) {
									patient.setHasInsurance('N');
								} else
									patient.setHasInsurance('U');
							}

							if (jParent_Yes.isSelected()) {
								patient.setParentTogether('Y');
							} else {
								if (jParent_No.isSelected()) {
									patient.setParentTogether('N');
								} else
									patient.setParentTogether('U');
							}

							patient.setNote(jNoteTextArea.getText().trim());

//							try {
//								Image photo = ImageIO.read(new File(photoPanel.getPhotoFilePath()));
//								patient.setPhoto(photo);
//							} catch (IOException ioe) {
//								// the photo didn't change
//								//logger.debug("Patient photo not changed");
//							}

							try{
								result = manager.newPatient(patient);
							}catch(OHServiceException ex){
								OHServiceExceptionUtil.showMessages(ex);
							}
							if (result)
								firePatientInserted(patient);

							if (!result)
								JOptionPane.showMessageDialog(PatientInsertExtended.this, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
							else {
								if (justSave) {
									insert = false;
									justSave = false;
									PatientInsertExtended.this.requestFocus();
								} else {
									dispose();
								}
							}
						} else
							return;
					} else {// Update

						patient.setFirstName(firstName);
						patient.setSecondName(secondName);
						if (radiof.isSelected()) {
							patient.setSex('F');
						} else if (radiom.isSelected()) {
							patient.setSex('M');
						} else {
							JOptionPane.showMessageDialog(PatientInsertExtended.this, "Please select a sex");
							return;
						}
						patient.setTaxCode(jTaxCodeTextField.getText().trim());
						patient.setAddress(jAddressTextField.getText().trim());
						patient.setCity(jCityTextField.getText().trim());
						patient.setNextKin(jNextKinTextField.getText().trim());
						patient.setTelephone(jTelephoneTextField.getText().replaceAll(" ", ""));
						patient.setMother_name(jMotherNameTextField.getText().trim());

						if (jMother_Alive.isSelected()) {
							patient.setMother('A');
						} else {
							if (jMother_Dead.isSelected()) {
								patient.setMother('D');
							} else {
								patient.setMother('U');
							}
						}
						patient.setFather_name(jFatherNameTextField.getText().trim());
						if (jFather_Alive.isSelected()) {
							patient.setFather('A');
						} else {
							if (jFather_Dead.isSelected()) {
								patient.setFather('D');
							} else {
								patient.setFather('U');
							}
						}
						patient.setBloodType(jBloodTypeComboBox.getSelectedItem().toString());
						
						if (jInsurance_Yes.isSelected()) {
							patient.setHasInsurance('Y');
						} else {
							if (jInsurance_No.isSelected()) {
								patient.setHasInsurance('N');
							} else {
								patient.setHasInsurance('U');
							}
						}

						if (jParent_Yes.isSelected()) {
							patient.setParentTogether('Y');
						} else {
							if (jParent_No.isSelected()) {
								patient.setParentTogether('N');
							} else {
								patient.setParentTogether('U');
							}
						}
						patient.setNote(jNoteTextArea.getText().trim());

//						try {
//							Image photo = ImageIO.read(new File(photoPanel.getPhotoFilePath()));
//							patient.setPhoto(photo);
//						} catch (IOException ioe) {
//							// the photo didn't change
//							//logger.debug("Patient photo not changed");
//						}

						try{
							result = manager.updatePatient(patient);
						}catch(OHServiceException ex){
                            OHServiceExceptionUtil.showMessages(ex);
						}
						if (result) {
							firePatientUpdated(patient);
						}
						if (!result)
							JOptionPane.showMessageDialog(PatientInsertExtended.this, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
						else
							dispose();
					}
				}
			});

		}
		return jOkButton;
	}

	/**
	 * This method checks Age insertion
	 * 
	 * @return javax.swing.JButton
	 */
	private boolean checkAge() {
		DateTime bdate = new DateTime();
		if (jAgeType_Age.isSelected()) {
			try {
				years = Integer.parseInt(jAgeYears.getText());
				months = Integer.parseInt(jAgeMonths.getText());
				days = Integer.parseInt(jAgeDays.getText());
				if (years == 0 && months == 0 && days == 0) throw new NumberFormatException();
				bdate = bdate.minusYears(years).minusMonths(months).minusDays(days);

			} catch (NumberFormatException ex1) {
				JOptionPane.showMessageDialog(PatientInsertExtended.this, MessageBundle.getMessage("angal.patient.insertvalidage"));
				return false;
			}
			if (years < 0 || years > 200)
				return false;
			if (years > 100) {
				if (JOptionPane.showConfirmDialog(null, MessageBundle.getMessage("angal.patient.confirmage"), MessageBundle.getMessage("angal.patient.veryoldpatient"),
						JOptionPane.YES_NO_OPTION) == 1) {
					return false;
				}
			}
			
		} else if (jAgeType_BirthDate.isSelected()) {
			if (cBirthDate == null) return false;
			else {
				bdate = new DateTime(cBirthDate);
				calcAge(bdate);
			}
		} else if (jAgeType_Description.isSelected()) {
			AgeTypeBrowserManager at = new AgeTypeBrowserManager();
			int index = jAgeDescComboBox.getSelectedIndex();
			AgeType ageType = null;
			
			if (index > 0) {
				try {
					ageType = at.getTypeByCode(index);
				}catch(OHServiceException e){
                    OHServiceExceptionUtil.showMessages(e);
				}
			} else
				return false;

			years = ageType.getFrom();
			if (index == 1) {
				months = jAgeMonthsComboBox.getSelectedIndex();
				patient.setAgetype(ageType.getCode() + "/" + months);
				bdate = bdate.minusYears(years).minusMonths(months);
			} else {
				bdate = bdate.minusYears(years);
			}
		}
		
		patient.setAge(years);
		patient.setBirthDate(bdate.toDate());
		patient.setAgetype("");
		return true;
	}

	/**
	 * This method initializes jCancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJCancelButton() {
		if (jCancelButton == null) {
			jCancelButton = new JButton();
			jCancelButton.setText("Cancel");
			jCancelButton.setMnemonic(KeyEvent.VK_A + ('C' - 'A'));
			jCancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return jCancelButton;
	}

	/**
	 * This method initializes jBirthDate
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJBirthDate() {
		if (jBirthDate == null) {
			jBirthDate = new JPanel();
			GridBagLayout gbl_jBirthDate = new GridBagLayout();
			gbl_jBirthDate.columnWidths = new int[]{0, 0};
			gbl_jBirthDate.rowHeights = new int[]{0, 0};
			gbl_jBirthDate.columnWeights = new double[]{0.0, 1.0};
			gbl_jBirthDate.rowWeights = new double[]{0.0, 0.0};
			jBirthDate.setLayout(gbl_jBirthDate);
			GridBagConstraints gbc_jBirthDateLabelPanel = new GridBagConstraints();
			gbc_jBirthDateLabelPanel.anchor = GridBagConstraints.WEST;
			gbc_jBirthDateLabelPanel.gridx = 0;
			gbc_jBirthDateLabelPanel.gridy = 0;
			jBirthDate.add(getJBirthDateLabelPanel(), gbc_jBirthDateLabelPanel);
			GridBagConstraints gbc_jBirthDateGroupPanel = new GridBagConstraints();
			gbc_jBirthDateGroupPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_jBirthDateGroupPanel.anchor = GridBagConstraints.WEST;
			gbc_jBirthDateGroupPanel.gridx = 1;
			gbc_jBirthDateGroupPanel.gridy = 0;
			jBirthDate.add(getJBirthDateGroupPanel(), gbc_jBirthDateGroupPanel);
			GridBagConstraints gbc_jBirthDateAge = new GridBagConstraints();
			gbc_jBirthDateAge.anchor = GridBagConstraints.WEST;
			gbc_jBirthDateAge.gridx = 1;
			gbc_jBirthDateAge.gridy = 1;
			jBirthDate.add(getJBirthDateAge(), gbc_jBirthDateAge);
		}
		return jBirthDate;
	}

	private JLabel getJBirthDateAge() {
		if (jBirthDateAge == null) {
			jBirthDateAge = new JLabel(" ");
		}
		return jBirthDateAge;
	}
	
	private String formatYearsMonthsDays(int years, int months, int days) {
		return years+"y "+months+"m "+days+"d";
	}

	/**
	 * This method initializes jBirthDateLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJBirthDateLabel() {
		if (jBirthDateLabel == null) {
			jBirthDateLabel = new JLabel();
			jBirthDateLabel.setText(MessageBundle.getMessage("angal.patient.birthdate"));
		}
		return jBirthDateLabel;
	}

	/**
	 * This method initializes jBirthDateGroupPanel
	 * 
	 * @return javax.swing.JPanel
	 */

	private JPanel getJBirthDateGroupPanel() {
		class BirthDateChooser extends JDateChooser {

			private static final long serialVersionUID = -78813689560070139L;

			public BirthDateChooser(Calendar cBirthDate) {
				super();
				super.setLocale(new Locale(GeneralData.LANGUAGE));
				super.setDateFormatString("dd/MM/yyyy");
				super.setPreferredSize(new Dimension(150, 20));
				// super.dateEditor.setEnabled(false);

				if (cBirthDate != null) {
					super.setCalendar(cBirthDate);
				}
			}

			public void propertyChange(PropertyChangeEvent e) {
				super.propertyChange(e);

				if (super.dateSelected) {
					cBirthDate = super.jcalendar.getCalendar();
					DateTime bdate = new DateTime(cBirthDate);
					if (bdate.isAfter(new DateTime())) super.setCalendar(new DateTime().toGregorianCalendar());
					else calcAge(bdate);
				}

				if (super.dateEditor.getDate() != null) {
					cBirthDate = super.getCalendar();
					DateTime bdate = new DateTime(cBirthDate);
					if (bdate.isAfter(new DateTime())) super.setCalendar(new DateTime().toGregorianCalendar());
					else calcAge(bdate);
				}
			}
		}
		
		if (jBirthDateGroupPanel == null) {
			jBirthDateGroupPanel = new JPanel();
			jBirthDateGroupPanel.setLayout(new BorderLayout());

			if (!insert) {
				Date sBirthDate = patient.getBirthDate();

				if (sBirthDate != null) {
					cBirthDate = Calendar.getInstance();
					cBirthDate.setTimeInMillis(sBirthDate.getTime());
				}
			}

			final BirthDateChooser jBirthDateChooser = new BirthDateChooser(cBirthDate);
			jBirthDateGroupPanel.add(jBirthDateChooser, BorderLayout.CENTER);

			if (jBirthDateReset == null) {
				jBirthDateReset = new JButton();
				jBirthDateReset.setIcon(new ImageIcon("rsc/icons/trash_button.png"));
				jBirthDateReset.setPreferredSize(new Dimension(20, 20));
				jBirthDateReset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jBirthDateChooser.getDateEditor().setDate(null);
						/*
						 * jAgeField.setText(""); jAgeField.setEditable(true);
						 */
						cBirthDate = null;
					}
				});

				jBirthDateGroupPanel.add(jBirthDateReset, BorderLayout.EAST);
			}
		}
		return jBirthDateGroupPanel;
	}

	private void calcAge(DateTime bdate) {
		Period p = new Period(bdate, new DateTime(), PeriodType.yearMonthDay());
		years = p.getYears();
		months = p.getMonths();
		days = p.getDays();
		getJBirthDateAge();
		jBirthDateAge.setText(formatYearsMonthsDays(years, months, days));
	}
	
	/**
	 * This method initializes jBirthDateLabelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJBirthDateLabelPanel() {
		if (jBirthDateLabelPanel == null) {
			jBirthDateLabelPanel = new JPanel();
			jBirthDateLabelPanel.add(getJBirthDateLabel(), BorderLayout.EAST);
		}
		return jBirthDateLabelPanel;
	}

	/**
	 * This method initializes jFirstNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJFirstNameTextField() {
		if (jFirstNameTextField == null) {
			jFirstNameTextField = new JTextField(15);
			if (!insert)
				jFirstNameTextField.setText(patient.getFirstName());
		}
		return jFirstNameTextField;
	}

	/**
	 * This method initializes jSecondNamePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJSecondNamePanel() {
		if (jSecondNameLabelPanel == null) {
			jSecondNameLabelPanel = new JPanel();
			jSecondNameLabelPanel.add(getJSecondNameLabel(), BorderLayout.EAST);
		}
		return jSecondNameLabelPanel;
	}

	/**
	 * This method initializes jSecondNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJSecondNameTextField() {
		if (jSecondNameTextField == null) {
			jSecondNameTextField = new JTextField(15);
			if (!insert)
				jSecondNameTextField.setText(patient.getSecondName());

		}
		return jSecondNameTextField;
	}

	/**
	 * This method initializes jSexPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSexPanel() {
		if (jSexPanel == null) {
			jSexPanel = new JPanel();
			sexGroup = new ButtonGroup();
			radiom = new JRadioButton(MessageBundle.getMessage("angal.patient.male"));
			radiof = new JRadioButton(MessageBundle.getMessage("angal.patient.female"));
			radiom.setMnemonic(KeyEvent.VK_A + ('M' - 'A'));
			radiof.setMnemonic(KeyEvent.VK_A + ('F' - 'A'));
			jSexPanel.add(getJSexLabelPanel(), null);
			jSexPanel.add(radiom, radiom.getName());
			if (!insert) {
				if (patient.getSex() == 'F')
					radiof.setSelected(true);
				else
					radiom.setSelected(true);
			}
			sexGroup.add(radiom);
			sexGroup.add(radiof);
			jSexPanel.add(radiof);

		}
		return jSexPanel;
	}

	/**
	 * This method initializes jAdressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAddressLabelPanel() {
		if (jAddressLabelPanel == null) {
			jAddressLabel = new JLabel();
			jAddressLabel.setText(MessageBundle.getMessage("angal.patient.address"));
			jAddressLabelPanel = new JPanel();
			jAddressLabelPanel.add(jAddressLabel, BorderLayout.EAST);
		}
		return jAddressLabelPanel;
	}

	/**
	 * This method initializes jTaxCodeLabelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTaxCodeLabelPanel() {
		if (jTaxCodeLabelPanel == null) {
			jTaxCodeLabel = new JLabel();
			jTaxCodeLabel.setText(MessageBundle.getMessage("angal.patient.taxcode"));
			jTaxCodeLabelPanel = new JPanel();
			jTaxCodeLabelPanel.add(jTaxCodeLabel, BorderLayout.EAST);
		}
		return jTaxCodeLabelPanel;
	}

	/**
	 * This method initializes jAdressTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJAdressTextField() {
		if (jAddressTextField == null) {
			jAddressTextField = new JTextField(15);
			if (!insert)
				jAddressTextField.setText(patient.getAddress());
		}
		return jAddressTextField;
	}

	/**
	 * This method initializes jTaxCodeTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTaxCodeTextField() {
		if (jTaxCodeTextField == null) {
			jTaxCodeTextField = new JTextField(15);
			if (!insert)
				jTaxCodeTextField.setText(patient.getTaxCode());
		}
		return jTaxCodeTextField;
	}

	/**
	 * This method initializes jCityLabelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJCityLabelPanel() {
		if (jCityLabelPanel == null) {
			jCityLabel = new JLabel();
			jCityLabel.setText(MessageBundle.getMessage("angal.patient.city"));
			jCityLabelPanel = new JPanel();
			jCityLabelPanel.add(jCityLabel, BorderLayout.EAST);
		}
		return jCityLabelPanel;
	}

	/**
	 * This method initializes jCityTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJCityTextField() {
		if (jCityTextField == null) {
			jCityTextField = new JTextField(15);
			if (!insert)
				jCityTextField.setText(patient.getCity());
		}
		return jCityTextField;
	}

	/**
	 * This method initializes jTelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTelPanel() {
		if (jTelephoneLabelPanel == null) {
			jTelephoneLabel = new JLabel();
			jTelephoneLabel.setText(MessageBundle.getMessage("angal.patient.telephone"));
			jTelephoneLabelPanel = new JPanel();
			jTelephoneLabelPanel.add(jTelephoneLabel, BorderLayout.EAST);
		}
		return jTelephoneLabelPanel;
	}

	/**
	 * This method initializes jTelephoneTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTelephoneTextField() {
		SmsParameters.getSmsParameters();
		if (jTelephoneTextField == null) {
			jTelephoneTextField = new JTextField(15);
			jTelephoneTextField.setText(SmsParameters.ICC);
			if (!insert)
				jTelephoneTextField.setText(patient.getTelephone());
		}
		return jTelephoneTextField;
	}

	/**
	 * This method initializes jNextKinLabelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJNextKinLabelPanel() {
		if (jNextKinLabelPanel == null) {
			jNextKinLabel = new JLabel();
			jNextKinLabel.setText(MessageBundle.getMessage("angal.patient.nextkin"));
			jNextKinLabelPanel = new JPanel();
			jNextKinLabelPanel.add(jNextKinLabel, BorderLayout.EAST);
		}
		return jNextKinLabelPanel;
	}

	/**
	 * This method initializes jNextKinTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJNextKinTextField() {
		if (jNextKinTextField == null) {
			jNextKinTextField = new JTextField(15);
			if (!insert)
				jNextKinTextField.setText(patient.getNextKin());
		}
		return jNextKinTextField;
	}

	/**
	 * This method initializes jBloodTypePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJBloodTypePanel() {
		if (jBloodTypePanel == null) {
			jBloodTypePanel = new JPanel();
			jBloodTypePanel = setMyBorder(jBloodTypePanel, MessageBundle.getMessage("angal.patient.bloodtype"));
			String[] bloodTypes = { MessageBundle.getMessage("angal.patient.bloodtype.unknown"), "0+", "A+", "B+", "AB+", "0-", "A-", "B-", "AB-" };
			jBloodTypeComboBox = new JComboBox(bloodTypes);
			jBloodTypePanel.add(jBloodTypeComboBox);

			if (!insert) {
				jBloodTypeComboBox.setSelectedItem(patient.getBloodType());
			} 
		}
		return jBloodTypePanel;
	}

	/**
	 * This method initializes jFirstNameLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJFirstNameLabel() {
		if (jFirstNameLabel == null) {
			jFirstNameLabel = new JLabel();
			jFirstNameLabel.setText(MessageBundle.getMessage("angal.patient.firstname"));
		}
		return jFirstNameLabel;
	}

	/**
	 * This method initializes jSecondNameLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getJSecondNameLabel() {
		if (jSecondNameLabel == null) {
			jSecondNameLabel = new JLabel();
			jSecondNameLabel.setText(MessageBundle.getMessage("angal.patient.secondname"));
		}
		return jSecondNameLabel;
	}

	/**
	 * This method initializes jFirstNameLabelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJFirstNamePanel() {
		if (jFirstNameLabelPanel == null) {
			jFirstNameLabelPanel = new JPanel();
			jFirstNameLabelPanel.add(getJFirstNameLabel(), BorderLayout.EAST);
		}
		return jFirstNameLabelPanel;
	}

	/**
	 * This method initializes jAnagraphPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAnagraphPanel() {
		if (jAnagraphPanel == null) {
			jAnagraphPanel = new JPanel();
			jAnagraphPanel.setLayout(new BoxLayout(jAnagraphPanel, BoxLayout.Y_AXIS));
			jAnagraphPanel = setMyBorder(jAnagraphPanel, "");
			jAnagraphPanel.add(getJFirstName(), null);
			jAnagraphPanel.add(getJSecondName(), null);
			jAnagraphPanel.add(getJTaxCodePanel(), null);
			// jAnagraphPanel.add(getJBirthDate(), null);
			jAnagraphPanel.add(getJAgeType(), null);
			jAnagraphPanel.add(getSexPanel(), null);
			jAnagraphPanel.add(getJAddressPanel(), null);
			jAnagraphPanel.add(getJCity(), null);
			jAnagraphPanel.add(getJNextKin(), null);
			jAnagraphPanel.add(getJTelephone(), null);
			jAnagraphPanel.add(getJLabelRequiredFields(), null);
		}
		return jAnagraphPanel;
	}
	
	private JLabel getJLabelRequiredFields() {
		if (labelRequiredFields == null) {
			labelRequiredFields = new JLabel(MessageBundle.getMessage("angal.patient.indicatesrequiredfields"));
			labelRequiredFields.setAlignmentX(CENTER_ALIGNMENT);
		}
		return labelRequiredFields;
	}

	/**
	 * This method initializes jSexLabelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJSexLabelPanel() {
		if (jSexLabelPanel == null) {
			jSexLabel = new JLabel();
			jSexLabel.setText(MessageBundle.getMessage("angal.patient.sexstar"));
			jSexLabelPanel = new JPanel();
			jSexLabelPanel.add(jSexLabel, BorderLayout.EAST);
		}
		return jSexLabelPanel;
	}

	/**
	 * This method initializes jSecondNameFieldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJSecondNamePanel1() {
		if (jSecondNameFieldPanel == null) {
			jSecondNameFieldPanel = new JPanel();
			jSecondNameFieldPanel.add(getJSecondNameTextField(), null);
		}
		return jSecondNameFieldPanel;
	}

	/**
	 * This method initializes jFirstName
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJFirstName() {
		if (jFirstName == null) {
			jFirstName = new JPanel();
			jFirstName.setLayout(new BorderLayout());
			jFirstName.add(getJFirstNamePanel(), BorderLayout.WEST);
			jFirstName.add(getJFirstNameFieldPanel(), java.awt.BorderLayout.EAST);
		}
		return jFirstName;
	}


	/**
	 * This method initializes jFirstNameFieldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJFirstNameFieldPanel() {
		if (jFirstNameFieldPanel == null) {
			jFirstNameFieldPanel = new JPanel();
			jFirstNameFieldPanel.add(getJFirstNameTextField(), null);
		}
		return jFirstNameFieldPanel;
	}

	/**
	 * This method initializes jSecondName
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
	 * This method initializes jAgeType
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAgeType() {
		if (jAgeType == null) {
			jAgeType = new JPanel();
			jAgeType = setMyBorder(jAgeType, MessageBundle.getMessage("angal.patient.agestar"));
			jAgeType.setLayout(new BorderLayout());
			jAgeType.add(getJAgeTypeButtonGroup(), BorderLayout.NORTH);
			jAgeType.add(getJAgeTypeSelection(), BorderLayout.CENTER);
			jAgeType.setPreferredSize(new Dimension(100,100));
		}
		return jAgeType;
	}

	/**
	 * This method initializes jAgeTypeButtonGroup
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAgeTypeButtonGroup() {
		if (jAgeTypeButtonGroup == null) {
			jAgeTypeButtonGroup = new JPanel();
			ageTypeGroup = new ButtonGroup();
			ageTypeGroup.add(getJAgeType_Age());
			ageTypeGroup.add(getJAgeType_Description());
			ageTypeGroup.add(getJAgeType_BirthDate());
			jAgeTypeButtonGroup.setLayout(new BorderLayout());
			jAgeTypeButtonGroup.add(getJAgeType_Age(), BorderLayout.WEST);
			jAgeTypeButtonGroup.add(getJAgeType_Description(), BorderLayout.EAST);
			jAgeTypeButtonGroup.add(getJAgeType_BirthDatePanel(), BorderLayout.CENTER);

			ActionListener sliceActionListener = new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					jAgeType.remove(jAgeTypeSelection);
					jAgeType.add(getJAgeTypeSelection());
					jAgeType.validate();
					jAgeType.repaint();

				}
			};

			if (!insert) {
				if (patient.getBirthDate() != null) {
					jAgeType_BirthDate.setSelected(true);
					calcAge(new DateTime(patient.getBirthDate()));
				} else if (patient.getAgetype() != null && patient.getAgetype().compareTo("") != 0) {
					parseAgeType();
					jAgeType_Description.setSelected(true);
				} else {
					jAgeType_Age.setSelected(true);
					years = patient.getAge();
					
				}
			} else {
				jAgeType_Age.setSelected(true);
			}

			jAgeType_Age.addActionListener(sliceActionListener);
			jAgeType_Description.addActionListener(sliceActionListener);
			jAgeType_BirthDate.addActionListener(sliceActionListener);

		}
		return jAgeTypeButtonGroup;
	}

	/**
	 * This method initializes ageType & ageTypeMonths
	 * 
	 * @return javax.swing.JPanel
	 */
	private void parseAgeType() {

		if (patient.getAgetype().compareTo("") != 0) {
			StringTokenizer token = new StringTokenizer(patient.getAgetype(), "/");
			String token1 = token.nextToken();
			String t1 = token1.substring(1, 2);
			ageType = Integer.parseInt(t1);

			if (token.hasMoreTokens()) {

				String token2 = token.nextToken();
				int t2 = Integer.parseInt(token2);
				ageTypeMonths = t2;
			} else
				ageTypeMonths = 0;
		} else {
			ageType = -1;
		}
	}

	/**
	 * This method initializes jAgeTypeSelection
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAgeTypeSelection() {
		
		if (jAgeType_Age.isSelected())
			jAgeTypeSelection = getJAge();
		else if (jAgeType_BirthDate.isSelected())
			jAgeTypeSelection = getJBirthDate();
		else
			jAgeTypeSelection = getJAgeDescription();
		return jAgeTypeSelection;
	}

	/**
	 * This method initializes jAgeType_Age
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJAgeType_Age() {
		if (jAgeType_Age == null) {
			jAgeType_Age = new JRadioButton();
			jAgeType_Age.setMnemonic(KeyEvent.VK_A + ('D' - 'A'));
			jAgeType_Age.setText(MessageBundle.getMessage("angal.patient.modeage"));
			jAgeType_Age.setFocusable(false);
		}
		return jAgeType_Age;
	}

	/**
	 * This method initializes jAgeType_BirthDate
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJAgeType_BirthDate() {
		if (jAgeType_BirthDate == null) {
			jAgeType_BirthDate = new JRadioButton();
			jAgeType_BirthDate.setMnemonic(KeyEvent.VK_A + ('D' - 'A'));
			jAgeType_BirthDate.setText(MessageBundle.getMessage("angal.patient.modebdate"));
			jAgeType_BirthDate.setFocusable(false);
		}
		return jAgeType_BirthDate;
	}

	/**
	 * This method initializes jAgeType_Description
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJAgeType_Description() {
		if (jAgeType_Description == null) {
			jAgeType_Description = new JRadioButton();
			jAgeType_Description.setMnemonic(KeyEvent.VK_A + ('D' - 'A'));
			jAgeType_Description.setText(MessageBundle.getMessage("angal.patient.modedescription"));
			jAgeType_Description.setFocusable(false);
		}
		return jAgeType_Description;
	}

	/**
	 * This method initializes jAgeType_BirthDatePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAgeType_BirthDatePanel() {
		if (jAgeType_BirthDatePanel == null) {
			jAgeType_BirthDatePanel = new JPanel();
			jAgeType_BirthDatePanel.add(getJAgeType_BirthDate(), null);
		}
		return jAgeType_BirthDatePanel;
	}

	/**
	 * This method initializes jAgeDesc
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAgeDescription() {
		if (jAgeDesc == null) {
			jAgeDesc = new JPanel();
			jAgeDesc.add(getJAgeDescPanel());// , java.awt.BorderLayout.WEST);

		}
		return jAgeDesc;
	}

	/**
	 * This method initializes jAgeMonthsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAgeMonthsPanel() {
		if (jAgeMonthsPanel == null) {
			jAgeMonthsPanel = new JPanel();
			jAgeMonthsLabel = new JLabel("months");

			String[] months = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
			jAgeMonthsComboBox = new JComboBox(months);
		}

		jAgeMonthsPanel.add(jAgeMonthsComboBox);
		jAgeMonthsPanel.add(jAgeMonthsLabel);

		if (!insert && ageType == 1) {

			jAgeMonthsComboBox.setSelectedIndex(ageTypeMonths);

		}
		return jAgeMonthsPanel;
	}

	/**
	 * This method initializes jAgeDescPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAgeDescPanel() {
		if (jAgeDescPanel == null) {
			jAgeDescPanel = new JPanel();

			jAgeDescComboBox = new JComboBox();

			AgeTypeBrowserManager at = new AgeTypeBrowserManager();
			ArrayList<AgeType> ageList;
			try {
				ageList = at.getAgeType();
			}catch(OHServiceException e){
				ageList = new ArrayList<AgeType>();
                OHServiceExceptionUtil.showMessages(e);
			}
			jAgeDescComboBox.addItem("");
			for (AgeType ag : ageList) {
				jAgeDescComboBox.addItem(MessageBundle.getMessage(ag.getDescription()));
			}

			jAgeDescPanel.add(jAgeDescComboBox);
			jAgeDescPanel.add(getJAgeMonthsPanel());
			jAgeMonthsComboBox.setEnabled(false);

			jAgeDescComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (jAgeDescComboBox.getSelectedItem().toString().compareTo(MessageBundle.getMessage("angal.agetype.newborn")) == 0) {
						jAgeMonthsComboBox.setEnabled(true);

					} else {
						jAgeMonthsComboBox.setEnabled(false);

					}
				}
			});

			if (!insert) {

				parseAgeType();
				jAgeDescComboBox.setSelectedIndex(ageType + 1);

				if (ageType == 0) {
					jAgeMonthsComboBox.setEnabled(true);
					jAgeMonthsComboBox.setSelectedIndex(ageTypeMonths);
				}
			}

		}
		return jAgeDescPanel;
	}

	/**
	 * This method initializes jAge
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAge() {
		if (jAge == null) {
			jAge = new JPanel();
			jAge.add(new JLabel("Years"));
			jAge.add(getJAgeFieldYears());
			jAge.add(new JLabel("Months"));
			jAge.add(getJAgeFieldMonths());
			jAge.add(new JLabel("Days"));
			jAge.add(getJAgeFieldDays());
		}
		return jAge;
	}

	/**
	 * This method initializes jAddressFieldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAddressFieldPanel() {
		if (jAddressFieldPanel == null) {
			jAddressFieldPanel = new JPanel();
			jAddressFieldPanel.add(getJAdressTextField(), null);
		}
		return jAddressFieldPanel;
	}

	private JTextField getJAgeFieldYears() {
		if (jAgeYears == null) {
			jAgeYears = new JTextField("0", 3);
			jAgeYears.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					JTextField thisField = (JTextField) e.getSource();
					thisField.setSelectionStart(0);
					thisField.setSelectionEnd(thisField.getText().length());
				}
			});
			if (!insert) jAgeYears.setText(""+years); 
		}
		return jAgeYears;
	}
	
	private JTextField getJAgeFieldMonths() {
		if (jAgeMonths == null) {
			jAgeMonths = new JTextField("0", 3);
			jAgeMonths.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					JTextField thisField = (JTextField) e.getSource();
					thisField.setSelectionStart(0);
					thisField.setSelectionEnd(thisField.getText().length());
				}
			});
			if (!insert) jAgeMonths.setText(""+months); 
		}
		return jAgeMonths;
	}
	
	private JTextField getJAgeFieldDays() {
		if (jAgeDays == null) {
			jAgeDays = new JTextField("0", 3);
			jAgeDays.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					JTextField thisField = (JTextField) e.getSource();
					thisField.setSelectionStart(0);
					thisField.setSelectionEnd(thisField.getText().length());
				}
			});
			if (!insert) jAgeDays.setText(""+days); 
		}
		return jAgeDays;
	}

	/**
	 * This method initializes jTaxCodeFieldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTaxCodeFieldPanel() {
		if (jTaxCodeFieldPanel == null) {
			jTaxCodeFieldPanel = new JPanel();
			jTaxCodeFieldPanel.add(getJTaxCodeTextField(), null);
		}
		return jTaxCodeFieldPanel;
	}

	/**
	 * This method initializes jCityFieldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJCityFieldPanel() {
		if (jCityFieldPanel == null) {
			jCityFieldPanel = new JPanel();
			jCityFieldPanel.add(getJCityTextField(), null);
		}
		return jCityFieldPanel;
	}

	/**
	 * This method initializes jNextKinFieldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJNextKinFieldPanel() {
		if (jNextKinFieldPanel == null) {
			jNextKinFieldPanel = new JPanel();
			jNextKinFieldPanel.add(getJNextKinTextField(), null);
		}
		return jNextKinFieldPanel;
	}

	/**
	 * This method initializes jTelephoneFieldPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTelephoneFieldPanel() {
		if (jTelephoneFieldPanel == null) {
			jTelephoneFieldPanel = new JPanel();
			jTelephoneFieldPanel.add(getJTelephoneTextField(), null);
		}
		return jTelephoneFieldPanel;
	}

	/**
	 * This method initializes jAdressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJAddressPanel() {
		if (jAddress == null) {
			jAddress = new JPanel();
			jAddress.setLayout(new BorderLayout());
			jAddress.add(getJAddressLabelPanel(), java.awt.BorderLayout.WEST);
			jAddress.add(getJAddressFieldPanel(), java.awt.BorderLayout.EAST);

		}
		return jAddress;
	}

	/**
	 * This method initializes jTaxCodePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTaxCodePanel() {
		if (jTaxCodePanel == null) {
			jTaxCodePanel = new JPanel();
			jTaxCodePanel.setLayout(new BorderLayout());
			jTaxCodePanel.add(getJTaxCodeLabelPanel(), java.awt.BorderLayout.WEST);
			jTaxCodePanel.add(getJTaxCodeFieldPanel(), java.awt.BorderLayout.EAST);

		}
		return jTaxCodePanel;
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
			jCity.add(getJCityLabelPanel(), java.awt.BorderLayout.WEST);
			jCity.add(getJCityFieldPanel(), java.awt.BorderLayout.EAST);
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
			jNextKin.add(getJNextKinLabelPanel(), java.awt.BorderLayout.WEST);
			jNextKin.add(getJNextKinFieldPanel(), java.awt.BorderLayout.EAST);
		}
		return jNextKin;
	}

	/**
	 * This method initializes jTelephone
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJTelephone() {
		if (jTelephone == null) {
			jTelephone = new JPanel();
			jTelephone.setLayout(new BorderLayout());
			jTelephone.add(getJTelPanel(), java.awt.BorderLayout.WEST);
			jTelephone.add(getJTelephoneFieldPanel(), java.awt.BorderLayout.EAST);
		}
		return jTelephone;
	}

	/**
	 * This method initializes jDataContainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJDataContainPanel() {
		if (jDataContainPanel == null) {
			jDataContainPanel = new JPanel();
			if (!insert) {
				StringBuilder title = new StringBuilder(patient.getName())
						.append(" (")
						.append(MessageBundle.getMessage("angal.common.code"))
						.append(": ")
						.append(patient.getCode())
						.append(")");
				jDataContainPanel = setMyBorderCenter(jDataContainPanel, title.toString());
			} else {
				//int nextcode = manager.getNextPatientCode();
				jDataContainPanel = setMyBorderCenter(jDataContainPanel, MessageBundle.getMessage("angal.patient.insertdataofnewpatient"));
			}
			jDataContainPanel.setLayout(new BorderLayout());
			jDataContainPanel.add(getJAnagraphPanel(), BorderLayout.CENTER);
			jDataContainPanel.add(getJExtensionContent(), BorderLayout.EAST);
		}
		return jDataContainPanel;
	}

	/**
	 * This method initializes jFatherPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJFatherPanel() {
		if (jFatherPanel == null) {
			jFatherPanel = new JPanel();
			jFatherPanel.setLayout(new BorderLayout());
			jFatherNamePanel = new JPanel();
			jFatherNamePanel.add(getJFatherNameTextField());
			fatherGroup = new ButtonGroup();
			fatherGroup.add(getJFather_Dead());
			fatherGroup.add(getJFather_Alive());
			fatherGroup.add(getJFather_Unknown());
			jFatherPanel = setMyBorder(jFatherPanel, MessageBundle.getMessage("angal.patient.fathername"));
			jFatherPanel.add(jFatherNamePanel, BorderLayout.NORTH);
			jFatherPanel.add(getJFatherOptions(), BorderLayout.CENTER);
			if (!insert) {
				switch (patient.getFather()) {
				case 'D':
					getJFather_Dead().setSelected(true);
					break;
				case 'A':
					getJFather_Alive().setSelected(true);
					break;
				default:
					break;
				}
			}

		}
		return jFatherPanel;
	}

	private JPanel getJFatherOptions() {
		if (jFatherPanelOptions == null) {
			jFatherPanelOptions = new JPanel();
			jFatherPanelOptions.add(getJFather_Dead());
			jFatherPanelOptions.add(getJFather_Unknown());
			jFatherPanelOptions.add(getJFatherAlivePanel());
		}
		return jFatherPanelOptions;
	}

	/**
	 * This method initializes jFatherDeadRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJFather_Dead() {
		if (jFather_Dead == null) {
			jFather_Dead = new JRadioButton();
			jFather_Dead.setMnemonic(KeyEvent.VK_A + ('D' - 'A'));
			jFather_Dead.setText(MessageBundle.getMessage("angal.patient.dead"));
		}
		return jFather_Dead;
	}

	/**
	 * This method initializes jFatherAliveRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJFather_Alive() {
		if (jFather_Alive == null) {
			jFather_Alive = new JRadioButton();
			jFather_Alive.setMnemonic(KeyEvent.VK_A + ('A' - 'A'));
			jFather_Alive.setText(MessageBundle.getMessage("angal.patient.alive"));
		}
		return jFather_Alive;
	}

	/**
	 * This method initializes jFatherUnknowRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJFather_Unknown() {
		if (jFather_Unknown == null) {
			jFather_Unknown = new JRadioButton();
			jFather_Unknown.setMnemonic(KeyEvent.VK_A + ('U' - 'A'));
			jFather_Unknown.setText(MessageBundle.getMessage("angal.patient.unknown"));
			jFather_Unknown.setSelected(true);
		}
		return jFather_Unknown;
	}

	/**
	 * This method initializes jMotherPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJMotherPanel() {
		if (jMotherPanel == null) {
			jMotherPanel = new JPanel();
			jMotherPanel = setMyBorder(jMotherPanel, MessageBundle.getMessage("angal.patient.mothername"));

			jMotherPanel.setLayout(new BorderLayout());
			jMotherNamePanel = new JPanel();
			jMotherNamePanel.add(getJMotherNameTextField());
			jMotherPanel.add(jMotherNamePanel, BorderLayout.NORTH);
			jMotherPanel.add(getJMotherOptions(), BorderLayout.CENTER);
			motherGroup = new ButtonGroup();
			motherGroup.add(getJMother_Dead());
			motherGroup.add(getJMother_Alive());
			motherGroup.add(getJMother_Unknown());
			if (!insert) {
				switch (patient.getMother()) {
				case 'D':
					getJMother_Dead().setSelected(true);
					break;
				case 'A':
					getJMother_Alive().setSelected(true);
					break;
				default:
					break;
				}
			}
		}
		return jMotherPanel;
	}

	private JPanel getJMotherOptions() {
		if (jMotherOptions == null) {
			jMotherOptions = new JPanel();
			jMotherOptions.add(getJMother_Dead(), BorderLayout.WEST);
			jMotherOptions.add(getJMother_Unknown(), BorderLayout.EAST);
			jMotherOptions.add(getJMotherAlivePanel(), BorderLayout.CENTER);
		}
		return jMotherOptions;
	}

	/**
	 * This method initializes jMotherDeadRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJMother_Dead() {
		if (jMother_Dead == null) {
			jMother_Dead = new JRadioButton();
			jMother_Dead.setMnemonic(KeyEvent.VK_A + ('D' - 'A'));
			jMother_Dead.setText(MessageBundle.getMessage("angal.patient.dead"));
		}
		return jMother_Dead;
	}

	/**
	 * This method initializes jMotherAliveRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJMother_Alive() {
		if (jMother_Alive == null) {
			jMother_Alive = new JRadioButton();
			jMother_Alive.setMnemonic(KeyEvent.VK_A + ('A' - 'A'));
			jMother_Alive.setText(MessageBundle.getMessage("angal.patient.alive"));
		}
		return jMother_Alive;
	}

	/**
	 * This method initializes jMotherUnknowRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJMother_Unknown() {
		if (jMother_Unknown == null) {
			jMother_Unknown = new JRadioButton();
			jMother_Unknown.setMnemonic(KeyEvent.VK_A + ('U' - 'A'));
			jMother_Unknown.setText(MessageBundle.getMessage("angal.patient.unknown"));
			jMother_Unknown.setSelected(true);
		}
		return jMother_Unknown;
	}

	/**
	 * This method initializes jInsurancePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJInsurancePanel() {
		if (jInsurancePanel == null) {
			jInsurancePanel = new JPanel(new BorderLayout());
			jInsurancePanel = setMyBorder(jInsurancePanel, MessageBundle.getMessage("angal.patient.hasinsurance"));

			JPanel groupPanel = new JPanel();
			groupPanel.add(getJInsurance_Yes());
			groupPanel.add(getJInsurance_NoPanel());
			groupPanel.add(getJInsurance_Unknown());
			
			insuranceGroup = new ButtonGroup();
			insuranceGroup.add(getJInsurance_Yes());
			insuranceGroup.add(getJInsurance_No());
			insuranceGroup.add(getJInsurance_Unknown());
			if (!insert) {
				switch (patient.getHasInsurance()) {
				case 'Y':
					getJInsurance_Yes().setSelected(true);
					break;
				case 'N':
					getJInsurance_No().setSelected(true);
					break;
				default:
					break;
				}
			}
			jInsurancePanel.add(groupPanel, BorderLayout.CENTER);
		}
		return jInsurancePanel;
	}

	/**
	 * This method initializes jInsuranceYesRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJInsurance_Yes() {
		if (jInsurance_Yes == null) {
			jInsurance_Yes = new JRadioButton();
			jInsurance_Yes.setMnemonic(KeyEvent.VK_A + ('R' - 'A'));
			jInsurance_Yes.setText(MessageBundle.getMessage("angal.patient.hasinsuranceyes"));
		}
		return jInsurance_Yes;
	}

	/**
	 * This method initializes jInsuranceNoRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJInsurance_No() {
		if (jInsurance_No == null) {
			jInsurance_No = new JRadioButton();
			jInsurance_No.setMnemonic(KeyEvent.VK_A + ('P' - 'A'));
			jInsurance_No.setText(MessageBundle.getMessage("angal.patient.hasinsuranceno"));
		}
		return jInsurance_No;
	}

	/**
	 * This method initializes jInsuranceUnknownRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJInsurance_Unknown() {
		if (jInsurance_Unknown == null) {
			jInsurance_Unknown = new JRadioButton();
			jInsurance_Unknown.setText(MessageBundle.getMessage("angal.patient.unknown"));
			jInsurance_Unknown.setMnemonic(KeyEvent.VK_A + ('U' - 'A'));
			jInsurance_Unknown.setSelected(true);
		}
		return jInsurance_Unknown;
	}

	/**
	 * This method initializes jParentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJParentPanel() {
		if (jParentPanel == null) {
			jParentPanel = new JPanel();
			parentGroup = new ButtonGroup();
			parentGroup.add(getJParent_Yes());
			parentGroup.add(getJParent_No());
			parentGroup.add(getJParent_Unknown());
			jParentPanel = setMyBorder(jParentPanel, MessageBundle.getMessage("angal.patient.parenttogether"));
			jParentPanel.add(getJParent_Yes());
			jParentPanel.add(getJPanelNoPanel());
			jParentPanel.add(getJParent_Unknown());
			if (!insert) {
				switch (patient.getParentTogether()) {
				case 'Y':
					getJParent_Yes().setSelected(true);
					break;
				case 'N':
					getJParent_No().setSelected(true);
					break;
				default:
					break;
				}
			}
		}
		return jParentPanel;
	}

	/**
	 * This method initializes jParentYesRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJParent_Yes() {
		if (jParent_Yes == null) {
			jParent_Yes = new JRadioButton();
			jParent_Yes.setMnemonic(KeyEvent.VK_A + ('Y' - 'A'));
			jParent_Yes.setText(MessageBundle.getMessage("angal.patient.yes"));
		}
		return jParent_Yes;
	}

	/**
	 * This method initializes jParentNoRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJParent_No() {
		if (jParent_No == null) {
			jParent_No = new JRadioButton();
			jParent_No.setMnemonic(KeyEvent.VK_A + ('N' - 'A'));
			jParent_No.setText(MessageBundle.getMessage("angal.patient.no"));
		}
		return jParent_No;
	}

	/**
	 * This method initializes jParentUnknownRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJParent_Unknown() {
		if (jParent_Unknown == null) {
			jParent_Unknown = new JRadioButton();
			jParent_Unknown.setText(MessageBundle.getMessage("angal.patient.unknown"));
			jParent_Unknown.setMnemonic(KeyEvent.VK_A + ('U' - 'A'));
			jParent_Unknown.setSelected(true);
		}
		return jParent_Unknown;
	}

	/**
	 * This method initializes jExtensionContent
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJExtensionContent() {
		if (jExtensionContent == null) {
			jExtensionContent = new JPanel();
			jExtensionContent.setLayout(new BoxLayout(getJExtensionContent(), BoxLayout.Y_AXIS));
			jExtensionContent.add(getJBloodTypePanel(), null);
			jExtensionContent.add(getJFatherPanel(), null);
			jExtensionContent.add(getJMotherPanel(), null);
			jExtensionContent.add(getJParentPanel(), null);
			jExtensionContent.add(getJInsurancePanel(), null);
		}
		return jExtensionContent;
	}

	/**
	 * set a specific border+title to a panel
	 */
	private JPanel setMyBorder(JPanel c, String title) {
		javax.swing.border.Border b1 = BorderFactory.createLineBorder(Color.lightGray);
		/*
		 * javax.swing.border.Border b2 = BorderFactory.createCompoundBorder(
		 * BorderFactory.createTitledBorder(title),null);
		 */
		javax.swing.border.Border b2 = BorderFactory.createTitledBorder(b1, title, javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP);

		c.setBorder(b2);
		return c;
	}

	private JPanel setMyBorderCenter(JPanel c, String title) {
		javax.swing.border.Border b1 = BorderFactory.createLineBorder(Color.lightGray);
		/*
		 * javax.swing.border.Border b2 = BorderFactory.createCompoundBorder(
		 * BorderFactory.createTitledBorder(title),null);
		 */
		javax.swing.border.Border b2 = BorderFactory.createTitledBorder(b1, title, javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP);

		c.setBorder(b2);
		return c;
	}

	/**
	 * This method initializes jFatherAlivePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJFatherAlivePanel() {
		if (jFatherAlivePanel == null) {
			jFatherAlivePanel = new JPanel();
			jFatherAlivePanel.add(getJFather_Alive(), null);
		}
		return jFatherAlivePanel;
	}

	/**
	 * This method initializes jMotherAlivePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJMotherAlivePanel() {
		if (jMotherAlivePanel == null) {
			jMotherAlivePanel = new JPanel();
			jMotherAlivePanel.add(getJMother_Alive(), null);
		}
		return jMotherAlivePanel;
	}

	/**
	 * This method initializes jInsuranceNoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJInsurance_NoPanel() {
		if (jInsuranceNoPanel == null) {
			jInsuranceNoPanel = new JPanel();
			jInsuranceNoPanel.add(getJInsurance_No(), null);
		}
		return jInsuranceNoPanel;
	}

	/**
	 * This method initializes jParentNoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNoPanel() {
		if (jParentNoPanel == null) {
			jParentNoPanel = new JPanel();
			jParentNoPanel.add(getJParent_No(), null);
		}
		return jParentNoPanel;
	}

	/**
	 * This method initializes jNoteTextArea
	 * 
	 * @return javax.swing.JPanel
	 */
	private JTextArea getJTextArea() {
		if (jNoteTextArea == null) {
			jNoteTextArea = new JTextArea();
			jNoteTextArea.setTabSize(4);
			jNoteTextArea.setAutoscrolls(true);
			jNoteTextArea.setLineWrap(true);
			if (!insert) {
				jNoteTextArea.setText(patient.getNote());
			}
		}
		return jNoteTextArea;
	}

	/**
	 * This method initializes jNotePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJRightPanel() {
		if (jRightPanel == null) {
			jRightPanel = new JPanel(new BorderLayout());
			try {
				//jRightPanel.add(getJPhoto(), BorderLayout.NORTH);
				photoPanel = new PatientPhotoPanel(this, patient.getCode(), patient.getPhoto());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (photoPanel != null) jRightPanel.add(photoPanel, BorderLayout.NORTH);
			jRightPanel.add(getJNoteScrollPane(), BorderLayout.CENTER);

		}
		return jRightPanel;
	}

	private JScrollPane getJNoteScrollPane() {
		if (jNoteScrollPane == null) {
			jNoteScrollPane = new JScrollPane(getJTextArea());
			jNoteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jNoteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jNoteScrollPane.setPreferredSize(new Dimension(200, 200));
			jNoteScrollPane.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(MessageBundle.getMessage("angal.patient.note")), BorderFactory.createEmptyBorder(5, 5, 5, 5)),
					jNoteScrollPane.getBorder()));
		}
		return jNoteScrollPane;
	}

	/**
	 * This method initializes jFatherNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJFatherNameTextField() {
		if (jFatherNameTextField == null) {
			jFatherNameTextField = new JTextField(15);
			if (!insert)
				jFatherNameTextField.setText(patient.getFather_name());
		}
		return jFatherNameTextField;
	}

	/**
	 * This method initializes jMotherNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJMotherNameTextField() {
		if (jMotherNameTextField == null) {
			jMotherNameTextField = new JTextField(15);
			if (!insert)
				jMotherNameTextField.setText(patient.getMother_name());
		}
		return jMotherNameTextField;
	}
	
	public void setPatientPhoto(Image photo) {
		patient.setPhoto(photo);
		patient.setBlobPhoto(null);
	}
}
