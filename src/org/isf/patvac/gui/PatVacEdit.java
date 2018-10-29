package org.isf.patvac.gui;

/*------------------------------------------
 * PatVacEdit - edit (new/update) a patient's vaccine
 * -----------------------------------------
 * modification history
 * 25/08/2011 - claudia - first beta version
 * 04/11/2011 - claudia modify vaccine date check on OK button 
 * 14/11/2011 - claudia inserted search condition on patient based on ENHANCEDSEARCH property
 *------------------------------------------*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.patvac.manager.PatVacManager;
import org.isf.patvac.model.PatientVaccine;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.RememberDates;
import org.isf.vaccine.manager.VaccineBrowserManager;
import org.isf.vaccine.model.Vaccine;
import org.isf.vactype.manager.VaccineTypeBrowserManager;
import org.isf.vactype.model.VaccineType;

import com.toedter.calendar.JDateChooser;

public class PatVacEdit extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4271389493861772053L;
	private static final String VERSION = MessageBundle.getMessage("angal.versione");
	private boolean insert = false;

	private PatientVaccine patVac = null;
	private JPanel jContentPane = null;
	private JPanel buttonPanel = null;
	private JPanel dataPanel = null;
	private JPanel jPatientSearchPanel = null;
	private JPanel dataPatient = null;

	private JLabel vaccineLabel = null;
	private JLabel patientLabel = null;
	private JLabel nameLabel = null;
	private JLabel ageLabel = null;
	private JLabel sexLabel = null;
	private JLabel vaccineDateLabel = null;
	private JLabel progrLabel = null;
	private JLabel vaccineTypeLabel = null;

	private JButton okButton = null;
	private JButton cancelButton = null;
	private JButton jSearchButton = null;

	private JComboBox vaccineComboBox = null;
	private JComboBox patientComboBox = null;
	private JComboBox vaccineTypeComboBox = null;

	private VoLimitedTextField patTextField = null;
	private VoLimitedTextField ageTextField = null;
	private VoLimitedTextField sexTextField = null;
	private VoLimitedTextField progrTextField = null;

	private JTextField jTextPatientSrc;
	private Patient selectedPatient = null;
	private String lastKey;
	private String s;
	private ArrayList<Patient> pat = null;
	private JDateChooser vaccineDateFieldCal = null;
	private GregorianCalendar dateIn = null;
	private int patNextYProg;

	private static final Integer panelWidth = 500;
	private static final Integer labelWidth = 50;
	private static final Integer dataPanelHeight = 180;
	private static final Integer dataPatientHeight = 100;
	private static final Integer buttonPanelHeight = 40;
	private static final Integer DeltaBetweenLabels = 40;

	public PatVacEdit(JFrame myFrameIn, PatientVaccine patientVaccineIn, boolean action) {
		super(myFrameIn, true);
		insert = action;
		patVac = patientVaccineIn;
		selectedPatient = patientVaccineIn.getPatient();
		patNextYProg = getPatientVaccineYMaxProg() + 1;
		initialize();
	}

	private int getPatientVaccineYMaxProg() {
		PatVacManager manager = new PatVacManager();
		try {
			return manager.getProgYear(0);
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
			return 0;
		}
	}

	/**
	 * This method initializes this Frame, sets the correct Dimensions
	 * 
	 * @return void
	 */
	private void initialize() {

		this.setBounds(30, 100, panelWidth + 20, dataPanelHeight + dataPatientHeight + buttonPanelHeight + 30);
		this.setContentPane(getJContentPane());
		this.setResizable(false);
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.patvac.newpatientvaccine") + "(" + VERSION + ")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.patvac.edipatientvaccine") + "(" + VERSION + ")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane, adds the main parts of the frame
	 * 
	 * @return jContentPanel (JPanel)
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getDataPanel());
			jContentPane.add(getDataPatient());
			jContentPane.add(getButtonPanel());
		}
		return jContentPane;
	}

	/**
	 * This method initializes dataPanel. This panel contains all items (combo
	 * boxes,calendar) to define a vaccine
	 * 
	 * @return dataPanel (JPanel)
	 */
	private JPanel getDataPanel() {
		if (dataPanel == null) {
			// initialize data panel
			dataPanel = new JPanel();
			dataPanel.setLayout(null);
			dataPanel.setBounds(0, 0, panelWidth, dataPanelHeight);

			// vaccine date
			vaccineDateLabel = new JLabel(MessageBundle.getMessage("angal.common.date"));
			vaccineDateLabel.setBounds(5, 10, labelWidth, 20);
			vaccineDateFieldCal = getVaccineDateFieldCal();
			vaccineDateFieldCal.setLocale(new Locale(GeneralData.LANGUAGE));
			vaccineDateFieldCal.setDateFormatString("dd/MM/yy");
			vaccineDateFieldCal.setBounds(labelWidth + 50, 10, 90, 20);
			// progressive
			progrLabel = new JLabel(MessageBundle.getMessage("angal.patvac.progressive"));
			progrLabel.setBounds(280, 10, labelWidth + 200, 20);
			progrTextField = getProgrTextField();
			progrTextField.setBounds(445, 10, 50, 20);

			// vaccineType combo box
			vaccineTypeLabel = new JLabel(MessageBundle.getMessage("angal.patvac.vaccinetype"));
			vaccineTypeLabel.setBounds(5, 2 * DeltaBetweenLabels, labelWidth + 70, 20);
			vaccineTypeComboBox = getVaccineTypeComboBox();
			vaccineTypeComboBox.setBounds(labelWidth + 50, 2 * DeltaBetweenLabels, 400, 20);

			// vaccine combo box
			vaccineLabel = new JLabel(MessageBundle.getMessage("angal.patvac.vaccine"));
			vaccineLabel.setBounds(5, 3 * DeltaBetweenLabels, labelWidth, 20);
			vaccineComboBox = getVaccineComboBox();
			vaccineComboBox.setBounds(labelWidth + 50, 3 * DeltaBetweenLabels, 400, 20);

			// add all to the data panel
			dataPanel.add(vaccineDateLabel, null);
			dataPanel.add(vaccineDateFieldCal, null);
			dataPanel.add(progrLabel, null);
			dataPanel.add(progrTextField, null);
			dataPanel.add(getPatientSearchPanel(), null);
			dataPanel.add(vaccineTypeLabel, null);
			dataPanel.add(vaccineTypeComboBox, null);
			dataPanel.add(vaccineLabel, null);
			dataPanel.add(vaccineComboBox, null);
		}
		return dataPanel;
	}

	/**
	 * This method initializes getPatientSearchPanel
	 * 
	 * @return JPanel
	 */

	private JPanel getPatientSearchPanel() {
		if (jPatientSearchPanel == null) {
			jPatientSearchPanel = new JPanel();
			jPatientSearchPanel.setLayout(null);
			jPatientSearchPanel.setBounds(0, 10, 500, 70);

			patientLabel = new JLabel(MessageBundle.getMessage("angal.patvac.patientcode"));
			patientLabel.setBounds(5, DeltaBetweenLabels - 8, labelWidth + 40, 20);
			jTextPatientSrc = new JTextField();
			jTextPatientSrc.setBounds(labelWidth + 50, DeltaBetweenLabels - 8, 100, 20);

			if (GeneralData.ENHANCEDSEARCH) {
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
			} else {
				jTextPatientSrc.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent e) {
						lastKey = "";
						String s = "" + e.getKeyChar();
						if (Character.isLetterOrDigit(e.getKeyChar())) {
							lastKey = s;
						}
						s = jTextPatientSrc.getText() + lastKey;
						s.trim();
						filterPatient(s);
					}

					public void keyPressed(KeyEvent e) {
					}

					public void keyReleased(KeyEvent e) {
					}
				});
			} // search condition field

			// patient data
			jPatientSearchPanel.add(patientLabel, null);
			jPatientSearchPanel.add(jTextPatientSrc, null);
			patientComboBox = new JComboBox();
			patientComboBox.setBounds(labelWidth + 180, DeltaBetweenLabels - 8, 270, 20);
			patientComboBox.addItem(MessageBundle.getMessage("angal.patvac.selectapatient"));

			if (GeneralData.ENHANCEDSEARCH) {
				jPatientSearchPanel.add(getJSearchButton(), null);
				s = (insert ? "-" : patVac.getPatName());
			}
			patientComboBox = getPatientComboBox(s);

			if (!insert) {
				patientComboBox.setEnabled(false);
				jTextPatientSrc.setEnabled(false);
			}

			jPatientSearchPanel.add(patientComboBox, null);
		}
		return jPatientSearchPanel;
	}

	/**
	 * This method initializes getJSearchButton
	 * 
	 * @return JButton
	 */

	private JButton getJSearchButton() {
		if (jSearchButton == null) {
			jSearchButton = new JButton();
			jSearchButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
			jSearchButton.setPreferredSize(new Dimension(20, 20));
			jSearchButton.setBounds(labelWidth + 150, DeltaBetweenLabels - 8, 20, 20);
			if (!insert) {
				jSearchButton.setEnabled(false);
			}
			jSearchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					patientComboBox.removeAllItems();
					resetPatVacPat();
					getPatientComboBox(jTextPatientSrc.getText());
				}
			});
		}
		return jSearchButton;
	}

	/**
	 * This method initializes getVaccineDateFieldCal
	 * 
	 * @return JDateChooser
	 */
	private JDateChooser getVaccineDateFieldCal() {
		java.util.Date myDate = null;
		if (insert) {
			dateIn = RememberDates.getLastPatientVaccineDateGregorian();
		} else {
			dateIn = patVac.getVaccineDate();
		}
		if (dateIn != null) {
			myDate = dateIn.getTime();
		}

		return (new JDateChooser(myDate, "dd/MM/yy"));
	}

	/**
	 * This method initializes getProgrTextField about progressive field
	 * 
	 * @return progrTextField (VoLimitedTextField)
	 */
	private VoLimitedTextField getProgrTextField() {
		if (progrTextField == null) {
			progrTextField = new VoLimitedTextField(4);
			if (insert) {
				progrTextField.setText(String.valueOf(patNextYProg));
			} else {
				progrTextField.setText(String.valueOf(patVac.getProgr()));
			}
		}
		return progrTextField;
	}

	/**
	 * This method initializes vaccineTypeCOmboBox. It used to display available
	 * vaccine types
	 * 
	 * @return vaccineTypeComboBox (JComboBox)
	 */
	private JComboBox getVaccineTypeComboBox() {
		if (vaccineTypeComboBox == null) {
			vaccineTypeComboBox = new JComboBox();
			vaccineTypeComboBox.setPreferredSize(new Dimension(200, 30));
			vaccineTypeComboBox.addItem(new VaccineType("", MessageBundle.getMessage("angal.patvac.allvaccinetype")));

			VaccineTypeBrowserManager manager = new VaccineTypeBrowserManager();
			ArrayList<VaccineType> types = null;
			try {
				types = manager.getVaccineType();
			} catch (OHServiceException e1) {
				OHServiceExceptionUtil.showMessages(e1);
			}
			VaccineType vaccineTypeSel = null;
			if(types != null){
				for (VaccineType elem : types) {
					vaccineTypeComboBox.addItem(elem);
					if (!insert && elem.getCode() != null) {
						if (elem.getCode().equalsIgnoreCase((patVac.getVaccine().getVaccineType().getCode()))) {
							vaccineTypeSel = elem;
						}
					}
				}
			}
			if (vaccineTypeSel != null)
				vaccineTypeComboBox.setSelectedItem(vaccineTypeSel);

			vaccineTypeComboBox.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					vaccineComboBox.removeAllItems();
					getVaccineComboBox();
				}
			});
		}
		return vaccineTypeComboBox;
	}

	/**
	 * This method initializes comboVaccine. It used to display available
	 * vaccines
	 * 
	 * @return vaccineComboBox (JComboBox)
	 */
	private JComboBox getVaccineComboBox() {
		Vaccine vaccineSel = null;
		if (vaccineComboBox == null) {
			vaccineComboBox = new JComboBox();
			vaccineComboBox.setPreferredSize(new Dimension(200, 30));
		}
		VaccineBrowserManager manager = new VaccineBrowserManager();

		ArrayList<Vaccine> allVac = null;
		vaccineComboBox.addItem(new Vaccine("", MessageBundle.getMessage("angal.patvac.allvaccine"), new VaccineType("", "")));
        try{
            if (((VaccineType) vaccineTypeComboBox.getSelectedItem()).getDescription().equals(MessageBundle.getMessage("angal.patvac.allvaccinetype"))) {
                allVac = manager.getVaccine();
            } else {
                allVac = manager.getVaccine(((VaccineType) vaccineTypeComboBox.getSelectedItem()).getCode());
            }
        } catch (OHServiceException e) {
            OHServiceExceptionUtil.showMessages(e);
        }
        if(allVac != null) {
            for (Vaccine elem : allVac) {
                if (!insert && elem.getCode() != null) {
                    if (elem.getCode().equalsIgnoreCase((patVac.getVaccine().getCode()))) {
                        vaccineSel = elem;
                    }
                }
                vaccineComboBox.addItem(elem);
            }
        }
		if (vaccineSel != null) {
			vaccineComboBox.setSelectedItem(vaccineSel);
		}
		return vaccineComboBox;
	}

	/**
	 * This method filter patient based on search string
	 * 
	 * @return void
	 */
	private void filterPatient(String key) {
		patientComboBox.removeAllItems();

		if (key == null || key.compareTo("") == 0) {
			patientComboBox.addItem(MessageBundle.getMessage("angal.patvac.selectapatient"));
			resetPatVacPat();
		}

		for (Patient elem : pat) {
			if (key != null) {
				// Search key extended to name and code
				StringBuilder sbName = new StringBuilder();
				sbName.append(elem.getSecondName().toUpperCase());
				sbName.append(elem.getFirstName().toUpperCase());
				sbName.append(elem.getCode());
				String name = sbName.toString();

				if (name.toLowerCase().contains(key.toLowerCase())) {
					patientComboBox.addItem(elem);
				}
			} else {
				patientComboBox.addItem(elem);
			}
		}

		if (patientComboBox.getItemCount() == 1) {
			selectedPatient = (Patient) patientComboBox.getSelectedItem();
			setPatient(selectedPatient);
		}

		if (patientComboBox.getItemCount() > 0) {
			if (patientComboBox.getItemAt(0) instanceof Patient) {
				selectedPatient = (Patient) patientComboBox.getItemAt(0);
				setPatient(selectedPatient);
			} else
				selectedPatient = null;
		} else
			selectedPatient = null;
	}

	/**
	 * This method reset patient's additonal data
	 * 
	 * @return void
	 */
	private void resetPatVacPat() {
		patTextField.setText("");
		ageTextField.setText("");
		sexTextField.setText("");
		selectedPatient = null;
	}

	/**
	 * This method sets patient's additonal data
	 * 
	 * @return void
	 */
	private void setPatient(Patient selectedPatient) {
		patTextField.setText(selectedPatient.getName());
		ageTextField.setText(selectedPatient.getAge() + "");
		sexTextField.setText(selectedPatient.getSex() + "");
	}

	/**
	 * This method initializes patientComboBox. It used to display available
	 * patients
	 * 
	 * @return patientComboBox (JComboBox)
	 */
	private JComboBox getPatientComboBox(String regExp) {

		Patient patSelected = null;
		PatientBrowserManager patBrowser = new PatientBrowserManager();

		if (GeneralData.ENHANCEDSEARCH){
			try {
				pat = patBrowser.getPatientWithHeightAndWeight(regExp);
			}catch(OHServiceException ex){
				OHServiceExceptionUtil.showMessages(ex);
				pat = new ArrayList<Patient>();
			}
		}else{
			try {
				pat = patBrowser.getPatient();
			} catch (OHServiceException e) {
                OHServiceExceptionUtil.showMessages(e);
			}
		}
		if(pat != null){
			for (Patient elem : pat) {
				if (!insert) {
					if (elem.getCode().equals(patVac.getPatient().getCode())) {
						patSelected = elem;
					}
				}
				patientComboBox.addItem(elem);
			}
		}
		if (patSelected != null) {
			patientComboBox.setSelectedItem(patSelected);
			selectedPatient = (Patient) patientComboBox.getSelectedItem();
		} else {
			if (patientComboBox.getItemCount() > 0 && GeneralData.ENHANCEDSEARCH) {
				if (patientComboBox.getItemAt(0) instanceof Patient) {
					selectedPatient = (Patient) patientComboBox.getItemAt(0);
					setPatient(selectedPatient);
				} else
					selectedPatient = null;
			} else
				selectedPatient = null;
		}
		patientComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (patientComboBox.getSelectedIndex() > 0) {
					selectedPatient = (Patient) patientComboBox.getSelectedItem();
					setPatient(selectedPatient);
				} else
					selectedPatient = null;
			}
		});

		return patientComboBox;
	}

	/**
	 * This method initializes dataPatient. This panel contains patient's data
	 * 
	 * @return dataPatient (JPanel)
	 */
	private JPanel getDataPatient() {
		if (dataPatient == null) {
			dataPatient = new JPanel();
			dataPatient.setLayout(null);
			dataPatient.setBounds(0, dataPanelHeight, panelWidth, dataPatientHeight);
			dataPatient.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.patvac.datapatient")));
			nameLabel = new JLabel(MessageBundle.getMessage("angal.patvac.name"));
			nameLabel.setBounds(10, DeltaBetweenLabels, labelWidth, 20);
			patTextField = getPatientTextField();
			patTextField.setBounds(labelWidth + 5, DeltaBetweenLabels, 180, 20);
			ageLabel = new JLabel(MessageBundle.getMessage("angal.patvac.age"));
			ageLabel.setBounds(255, DeltaBetweenLabels, 35, 20);
			ageTextField = getAgeTextField();
			ageTextField.setBounds(295, DeltaBetweenLabels, 50, 20);
			sexLabel = new JLabel(MessageBundle.getMessage("angal.patvac.sex"));
			sexLabel.setBounds(370, DeltaBetweenLabels, 80, 20);
			sexTextField = getSexTextField();
			sexTextField.setBounds(440, DeltaBetweenLabels, 50, 20);

			// add all elements
			dataPatient.add(nameLabel, null);
			dataPatient.add(patTextField, null);
			dataPatient.add(ageLabel, null);
			dataPatient.add(ageTextField, null);
			dataPatient.add(sexLabel, null);
			dataPatient.add(sexTextField, null);
			patTextField.setEditable(false);
			ageTextField.setEditable(false);
			sexTextField.setEditable(false);
		}
		return dataPatient;
	}

	/**
	 * This method initializes getPatientTextField about patient name
	 * 
	 * @return patTextField (VoLimitedTextField)
	 */
	private VoLimitedTextField getPatientTextField() {
		if (patTextField == null) {
			patTextField = new VoLimitedTextField(100);
			if (!insert) {
				patTextField.setText(patVac.getPatName());
			}
		}
		return patTextField;
	}

	/**
	 * This method initializes getAgeTextField about patient
	 * 
	 * @return ageTextField (VoLimitedTextField)
	 */
	private VoLimitedTextField getAgeTextField() {
		if (ageTextField == null) {
			ageTextField = new VoLimitedTextField(3);
			if (insert) {
				ageTextField.setText("");
			} else {
				try {
					Integer intAge = patVac.getPatAge();
					ageTextField.setText(intAge.toString());
				} catch (Exception e) {
					ageTextField.setText("");
				}
			}
		}
		return ageTextField;
	}

	/**
	 * This method initializes getSexTextField about patient
	 * 
	 * @return sexTextField (VoLimitedTextField)
	 */
	private VoLimitedTextField getSexTextField() {
		if (sexTextField == null) {
			sexTextField = new VoLimitedTextField(1);
			if (!insert) {
				sexTextField.setText("" + patVac.getPatSex());
			}
		}
		return sexTextField;
	}

	/**
	 * This method initializes buttonPanel, that contains the buttons of the
	 * frame (on the bottom)
	 * 
	 * @return buttonPanel (JPanel)
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBounds(0, dataPanelHeight + dataPatientHeight, panelWidth, buttonPanelHeight);
			buttonPanel.add(getOkButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes okButton. It is used to update db data
	 * 
	 * @return okButton (JPanel)
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText(MessageBundle.getMessage("angal.common.ok"));
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					GregorianCalendar gregDate = new GregorianCalendar();
					gregDate.setTime(vaccineDateFieldCal.getDate());
                    patVac.setProgr(Integer.parseInt(progrTextField.getText()));

					// check on patient
					if (selectedPatient == null) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patvac.pleaseselectapatient"));
						return;
					}

					patVac.setVaccineDate(gregDate);
					patVac.setVaccine((Vaccine) vaccineComboBox.getSelectedItem());
					patVac.setPatient(selectedPatient);
					patVac.setLock(0);
					patVac.setPatName(selectedPatient.getName());
					patVac.setPatSex(selectedPatient.getSex());

					boolean result;
					PatVacManager manager = new PatVacManager();
					// handling db insert/update
					if (insert) {
						patVac.setPatAge(selectedPatient.getAge());
						try {
							result = manager.newPatientVaccine(patVac);
						} catch (OHServiceException e1) {
							OHServiceExceptionUtil.showMessages(e1);
							result = false;
						}
					} else {
						try {
							result = manager.updatePatientVaccine(patVac);
						} catch (OHServiceException e1) {
							OHServiceExceptionUtil.showMessages(e1);
							result = false;
						}
					}

					if (!result)
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patvac.thedatacouldnobesaved"));
					else {
						patVac = new PatientVaccine(0, 0, new GregorianCalendar(), new Patient(), new Vaccine("", "", new VaccineType("", "")), 0);
						dispose();
					}
				}
			});
		}
		return okButton;
	}

	/**
	 * This method initializes cancelButton.
	 * 
	 * @return cancelButton (JPanel)
	 */

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
}
