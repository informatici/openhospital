package org.isf.ward.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;

/**
 * This class allows wards edits and inserts
 * 
 * @author Rick
 * 
 */
public class WardEdit extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList wardListeners = new EventListenerList();
	
	public interface WardListener extends EventListener {
		public void wardUpdated(AWTEvent e);
		public void wardInserted(AWTEvent e);
	}
	
	public void addWardListener(WardListener l) {
		wardListeners.add(WardListener.class, l);
	}
	
	public void removeWardListener(WardListener listener) {
		wardListeners.remove(WardListener.class, listener);
	}
	
	private void fireWardInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = wardListeners.getListeners(WardListener.class);
		for (int i = 0; i < listeners.length; i++)
			((WardListener)listeners[i]).wardInserted(event);
	}
	private void fireWardUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = wardListeners.getListeners(WardListener.class);
		for (int i = 0; i < listeners.length; i++)
			((WardListener)listeners[i]).wardUpdated(event);
	}
	
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JLabel codeLabel = null;
	private JLabel telLabel = null;
	private JLabel faxLabel = null;
	private JLabel emailLabel = null;
	private JLabel bedsLabel = null;
	private JLabel nursLabel = null;
	private JLabel docsLabel = null;
	private JLabel requiredLabel = null;
	private JTextField descriptionTextField = null;
	private JTextField codeTextField = null;
	private JTextField telTextField = null;
	private JTextField faxTextField = null;
	private JTextField emailTextField = null;
	private JTextField bedsTextField = null;
	private JTextField nursTextField = null;
	private JTextField docsTextField = null;
	private JCheckBox isPharmacyCheck = null;
	private JCheckBox isMaleCheck = null;
	private JCheckBox isFemaleCheck = null;
	private Ward ward = null;
	private boolean insert = false;
	private int beds;
	private int nurs;
	private int docs;
	
	/**
	 * 
	 * This is the default constructor; we pass the parent frame
	 * (because it is a jdialog), the arraylist and the selected
	 * row because we need to update them
	 */
	public WardEdit(JFrame parent,Ward old,boolean inserting) {
		super(parent, true);
		insert = inserting;
		ward = old;		//operation will be used for every operation
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.ward.newwardrecord"));
		} else {
			this.setTitle(MessageBundle.getMessage("angal.ward.editingwardrecord"));
		}
		pack();
		setLocationRelativeTo(null);
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.CENTER);  // Generated
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);  // Generated
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes dataPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDataPanel() {
		if (dataPanel == null) {
			dataPanel = new JPanel();
			GridBagLayout gbl_dataPanel = new GridBagLayout();
			gbl_dataPanel.columnWeights = new double[]{0.0, 1.0};
			dataPanel.setLayout(gbl_dataPanel);
			codeLabel = new JLabel();
			codeLabel.setText(MessageBundle.getMessage("angal.common.codestar"));
			GridBagConstraints gbc_codeLabel = new GridBagConstraints();
			gbc_codeLabel.anchor = GridBagConstraints.WEST;
			gbc_codeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_codeLabel.gridx = 0;
			gbc_codeLabel.gridy = 0;
			dataPanel.add(codeLabel, gbc_codeLabel);
			GridBagConstraints gbc_codeTextField = new GridBagConstraints();
			gbc_codeTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_codeTextField.insets = new Insets(0, 0, 5, 0);
			gbc_codeTextField.gridx = 1;
			gbc_codeTextField.gridy = 0;
			dataPanel.add(getCodeTextField(), gbc_codeTextField);
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.ward.nameedit"));
			GridBagConstraints gbc_descLabel = new GridBagConstraints();
			gbc_descLabel.anchor = GridBagConstraints.WEST;
			gbc_descLabel.insets = new Insets(0, 0, 5, 5);
			gbc_descLabel.gridx = 0;
			gbc_descLabel.gridy = 1;
			dataPanel.add(descLabel, gbc_descLabel);
			GridBagConstraints gbc_descriptionTextField = new GridBagConstraints();
			gbc_descriptionTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_descriptionTextField.insets = new Insets(0, 0, 5, 0);
			gbc_descriptionTextField.gridx = 1;
			gbc_descriptionTextField.gridy = 1;
			dataPanel.add(getDescriptionTextField(), gbc_descriptionTextField);
			GridBagConstraints gbc_telTextField = new GridBagConstraints();
			gbc_telTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_telTextField.insets = new Insets(0, 0, 5, 0);
			gbc_telTextField.gridx = 1;
			gbc_telTextField.gridy = 2;
			dataPanel.add(getTelTextField(), gbc_telTextField);
			telLabel = new JLabel();
			telLabel.setText(MessageBundle.getMessage("angal.ward.telephoneedit"));
			GridBagConstraints gbc_telLabel = new GridBagConstraints();
			gbc_telLabel.anchor = GridBagConstraints.WEST;
			gbc_telLabel.insets = new Insets(0, 0, 5, 5);
			gbc_telLabel.gridx = 0;
			gbc_telLabel.gridy = 2;
			dataPanel.add(telLabel, gbc_telLabel);
			faxLabel = new JLabel();
			faxLabel.setText(MessageBundle.getMessage("angal.ward.faxedit"));
			GridBagConstraints gbc_faxLabel = new GridBagConstraints();
			gbc_faxLabel.anchor = GridBagConstraints.WEST;
			gbc_faxLabel.insets = new Insets(0, 0, 5, 5);
			gbc_faxLabel.gridx = 0;
			gbc_faxLabel.gridy = 3;
			dataPanel.add(faxLabel, gbc_faxLabel);
			GridBagConstraints gbc_faxTextField = new GridBagConstraints();
			gbc_faxTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_faxTextField.insets = new Insets(0, 0, 5, 0);
			gbc_faxTextField.gridx = 1;
			gbc_faxTextField.gridy = 3;
			dataPanel.add(getFaxTextField(), gbc_faxTextField);
			GridBagConstraints gbc_emailTextField = new GridBagConstraints();
			gbc_emailTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_emailTextField.insets = new Insets(0, 0, 5, 0);
			gbc_emailTextField.gridx = 1;
			gbc_emailTextField.gridy = 4;
			dataPanel.add(getEmailTextField(), gbc_emailTextField);
			emailLabel = new JLabel();
			emailLabel.setText(MessageBundle.getMessage("angal.ward.emailedit"));
			GridBagConstraints gbc_emailLabel = new GridBagConstraints();
			gbc_emailLabel.anchor = GridBagConstraints.WEST;
			gbc_emailLabel.insets = new Insets(0, 0, 5, 5);
			gbc_emailLabel.gridx = 0;
			gbc_emailLabel.gridy = 4;
			dataPanel.add(emailLabel, gbc_emailLabel);
			bedsLabel = new JLabel();
			bedsLabel.setText(MessageBundle.getMessage("angal.ward.bedsedit"));
			GridBagConstraints gbc_bedsLabel = new GridBagConstraints();
			gbc_bedsLabel.anchor = GridBagConstraints.WEST;
			gbc_bedsLabel.insets = new Insets(0, 0, 5, 5);
			gbc_bedsLabel.gridx = 0;
			gbc_bedsLabel.gridy = 5;
			dataPanel.add(bedsLabel, gbc_bedsLabel);
			GridBagConstraints gbc_bedsTextField = new GridBagConstraints();
			gbc_bedsTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_bedsTextField.insets = new Insets(0, 0, 5, 0);
			gbc_bedsTextField.gridx = 1;
			gbc_bedsTextField.gridy = 5;
			dataPanel.add(getBedsTextField(), gbc_bedsTextField);
			nursLabel = new JLabel();
			nursLabel.setText(MessageBundle.getMessage("angal.ward.nursesedit"));
			GridBagConstraints gbc_nursLabel = new GridBagConstraints();
			gbc_nursLabel.anchor = GridBagConstraints.WEST;
			gbc_nursLabel.insets = new Insets(0, 0, 5, 5);
			gbc_nursLabel.gridx = 0;
			gbc_nursLabel.gridy = 6;
			dataPanel.add(nursLabel, gbc_nursLabel);
			GridBagConstraints gbc_nursTextField = new GridBagConstraints();
			gbc_nursTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_nursTextField.insets = new Insets(0, 0, 5, 0);
			gbc_nursTextField.gridx = 1;
			gbc_nursTextField.gridy = 6;
			dataPanel.add(getNursTextField(), gbc_nursTextField);
			docsLabel = new JLabel();
			docsLabel.setText(MessageBundle.getMessage("angal.ward.doctorsedit"));
			GridBagConstraints gbc_docsLabel = new GridBagConstraints();
			gbc_docsLabel.anchor = GridBagConstraints.WEST;
			gbc_docsLabel.insets = new Insets(0, 0, 5, 5);
			gbc_docsLabel.gridx = 0;
			gbc_docsLabel.gridy = 7;
			dataPanel.add(docsLabel, gbc_docsLabel);
			GridBagConstraints gbc_docsTextField = new GridBagConstraints();
			gbc_docsTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_docsTextField.insets = new Insets(0, 0, 5, 0);
			gbc_docsTextField.gridx = 1;
			gbc_docsTextField.gridy = 7;
			dataPanel.add(getDocsTextField(), gbc_docsTextField);
			GridBagConstraints gbc_isPharmacyCheck = new GridBagConstraints();
			gbc_isPharmacyCheck.anchor = GridBagConstraints.WEST;
			gbc_isPharmacyCheck.insets = new Insets(0, 0, 5, 0);
			gbc_isPharmacyCheck.gridwidth = 2;
			gbc_isPharmacyCheck.gridx = 0;
			gbc_isPharmacyCheck.gridy = 8;
			dataPanel.add(getIsPharmacyCheck(), gbc_isPharmacyCheck);
			GridBagConstraints gbc_isMaleCheck = new GridBagConstraints();
			gbc_isMaleCheck.anchor = GridBagConstraints.WEST;
			gbc_isMaleCheck.insets = new Insets(0, 0, 5, 0);
			gbc_isMaleCheck.gridwidth = 2;
			gbc_isMaleCheck.gridx = 0;
			gbc_isMaleCheck.gridy = 9;
			dataPanel.add(getIsMaleCheck(), gbc_isMaleCheck);
			GridBagConstraints gbc_isFemaleCheck = new GridBagConstraints();
			gbc_isFemaleCheck.anchor = GridBagConstraints.WEST;
			gbc_isFemaleCheck.insets = new Insets(0, 0, 5, 0);
			gbc_isFemaleCheck.gridwidth = 2;
			gbc_isFemaleCheck.gridx = 0;
			gbc_isFemaleCheck.gridy = 10;
			dataPanel.add(getIsFemaleCheck(), gbc_isFemaleCheck);
			requiredLabel= new JLabel();
			requiredLabel.setText(MessageBundle.getMessage("angal.ward.requiredfields"));
			GridBagConstraints gbc_requiredLabel = new GridBagConstraints();
			gbc_requiredLabel.gridwidth = 2;
			gbc_requiredLabel.anchor = GridBagConstraints.EAST;
			gbc_requiredLabel.gridx = 0;
			gbc_requiredLabel.gridy = 11;
			dataPanel.add(requiredLabel, gbc_requiredLabel);
		}
		return dataPanel;
	}
	
	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getOkButton(), null);  // Generated
			buttonPanel.add(getCancelButton(), null);  // Generated
		}
		return buttonPanel;
	}
	
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(MessageBundle.getMessage("angal.common.cancel"));  // Generated
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}
	
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText(MessageBundle.getMessage("angal.common.ok"));  // Generated
			okButton.setMnemonic(KeyEvent.VK_O);
			
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					WardBrowserManager manager = Context.getApplicationContext().getBean(WardBrowserManager.class);

					try {
						beds = Integer.parseInt(bedsTextField.getText());
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(WardEdit.this,
								MessageBundle.getMessage("angal.ward.insertavalidbedsnumber"));
						return;
					}
					try {
						nurs = Integer.parseInt(nursTextField.getText());
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(WardEdit.this,
								MessageBundle.getMessage("angal.ward.insertavalidnursesnumber"));
						return;
					}
					try {
						docs = Integer.parseInt(docsTextField.getText());
					} catch (NumberFormatException f) {
						JOptionPane.showMessageDialog(WardEdit.this,
								MessageBundle.getMessage("angal.ward.insertavaliddoctorsnumber"));
						return;
					}

					ward.setDescription(descriptionTextField.getText());
					ward.setCode(codeTextField.getText().toUpperCase().trim());
					ward.setTelephone(telTextField.getText());
					ward.setFax(faxTextField.getText());
					ward.setEmail(emailTextField.getText());
					ward.setBeds(beds);
					ward.setNurs(nurs);
					ward.setDocs(docs);
					ward.setPharmacy(isPharmacyCheck.isSelected());
					ward.setMale(isMaleCheck.isSelected());
					ward.setFemale(isFemaleCheck.isSelected());

					boolean result = false;
					if (insert) { // inserting
						try {
							result = manager.newWard(ward);
						} catch (OHServiceException ex) {
							OHServiceExceptionUtil.showMessages(ex);
						}
						if (result) {
							fireWardInserted();
						}
					} else {
						try { // updating
							result = manager.updateWard(ward);
						} catch (OHServiceException ex) {
							OHServiceExceptionUtil.showMessages(ex);
						}
						if (result) {
							fireWardUpdated();
						}
					}
					if (!result)
						JOptionPane.showMessageDialog(WardEdit.this,
								MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
					else
						dispose();
				}
			});
		}
		return okButton;
	}
	
	/**
	 * This method initializes descriptionTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescriptionTextField() {
		if (descriptionTextField == null) {
			descriptionTextField = new VoLimitedTextField(50);
			if (!insert) {				
				descriptionTextField.setText(ward.getDescription());
			}
		}
		return descriptionTextField;
	}
	
	/**
	 * This method initializes codeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCodeTextField() {
		if (codeTextField == null) {
			codeTextField = new VoLimitedTextField(1, 20);			
			if (!insert) {
				codeTextField.setText(ward.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
	}
	
	/**
	 * This method initializes telTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTelTextField() {
		if (telTextField == null) {
			telTextField = new VoLimitedTextField(50, 20);
			if (!insert) {
				telTextField.setText(ward.getTelephone());
			}
		}
		return telTextField;
	}
	
	/**
	 * This method initializes faxTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFaxTextField() {
		if (faxTextField == null) {
			faxTextField = new VoLimitedTextField(50, 20);
			if (!insert) {
				faxTextField.setText(ward.getFax());
			}
		}
		return faxTextField;
	}
	
	/**
	 * This method initializes emailTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getEmailTextField() {
		if (emailTextField == null) {
			emailTextField = new VoLimitedTextField(50, 20);
			if (!insert) {
				emailTextField.setText(ward.getEmail());
			}
		}
		return emailTextField;
	}
	
	/**
	 * This method initializes bedsTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getBedsTextField() {	
		if (bedsTextField == null) {
			bedsTextField = new VoLimitedTextField(4, 20);
			if (!insert) {
				bedsTextField.setText(Integer.toString(ward.getBeds()));
			}
		}
		return bedsTextField;
	}
	
	/**
	 * This method initializes nursTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNursTextField() {
		if (nursTextField == null) {
			nursTextField = new VoLimitedTextField(4, 20);
			if (!insert) {
				nursTextField.setText(Integer.toString(ward.getNurs()));
			}
		}
		return nursTextField;
	}
	
	/**
	 * This method initializes docsTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDocsTextField() {
		if (docsTextField == null) {
			docsTextField = new VoLimitedTextField(4, 20);
			if (!insert) {
				docsTextField.setText(Integer.toString(ward.getDocs()));
			}
		}
		return docsTextField;
	}
	
	/**
	 * This method initializes isPharmacyCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsPharmacyCheck() {
		if (isPharmacyCheck==null) {
			isPharmacyCheck = new JCheckBox(MessageBundle.getMessage("angal.ward.wardwithpharmacy"));
			if (!insert) {
				isPharmacyCheck.setSelected(ward.isPharmacy());
			}
		}
		return isPharmacyCheck;
	}
	
	/**
	 * This method initializes isFemaleCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsMaleCheck() {
		if (isMaleCheck == null) {
			isMaleCheck = new JCheckBox(MessageBundle.getMessage("angal.ward.maleward"));
			if (!insert) {
				isMaleCheck.setSelected(ward.isMale());
			}
		}
		return isMaleCheck;
	}
	
	/**
	 * This method initializes isFemaleCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsFemaleCheck() {
		if (isFemaleCheck == null) {
			isFemaleCheck = new JCheckBox(MessageBundle.getMessage("angal.ward.femaleward"));
			if (!insert) {
				isFemaleCheck.setSelected(ward.isFemale());
			}
		}
		return isFemaleCheck;
	}
} 