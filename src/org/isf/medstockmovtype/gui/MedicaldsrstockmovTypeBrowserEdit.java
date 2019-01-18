package org.isf.medstockmovtype.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.medstockmovtype.manager.MedicaldsrstockmovTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;

public class MedicaldsrstockmovTypeBrowserEdit extends JDialog{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList medicaldsrstockmovTypeListeners = new EventListenerList();

    public interface MedicaldsrstockmovTypeListener extends EventListener {
        public void medicaldsrstockmovTypeUpdated(AWTEvent e);
        public void medicaldsrstockmovTypeInserted(AWTEvent e);
    }

    public void addMedicaldsrstockmovTypeListener(MedicaldsrstockmovTypeListener l) {
    	medicaldsrstockmovTypeListeners.add(MedicaldsrstockmovTypeListener.class, l);
    }

    public void removeMedicaldsrstockmovTypeListener(MedicaldsrstockmovTypeListener listener) {
    	medicaldsrstockmovTypeListeners.remove(MedicaldsrstockmovTypeListener.class, listener);
    }

    private void fireMedicaldsrstockmovInserted(MovementType anMedicaldsrstockmovType) {
        AWTEvent event = new AWTEvent(anMedicaldsrstockmovType, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = medicaldsrstockmovTypeListeners.getListeners(MedicaldsrstockmovTypeListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((MedicaldsrstockmovTypeListener)listeners[i]).medicaldsrstockmovTypeInserted(event);
    }
    private void fireMedicaldsrstockmovUpdated() {
        AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = medicaldsrstockmovTypeListeners.getListeners(MedicaldsrstockmovTypeListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((MedicaldsrstockmovTypeListener)listeners[i]).medicaldsrstockmovTypeUpdated(event);
    }
    
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JTextField descriptionTextField = null;
	private VoLimitedTextField codeTextField = null;
	private JComboBox typeComboBox = null;
	private String lastdescription;
	private MovementType medicaldsrstockmovType = null;
	private boolean insert;
	private JPanel jDataPanel = null;
	private JLabel jTypeLabel = null;
	private JPanel jTypeLabelPanel = null;
	private JLabel jCodeLabel = null;
	private JPanel jCodeLabelPanel = null;
	private JPanel jDescriptionLabelPanel = null;
	private JLabel jDescripitonLabel = null;
	
	/**
     * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
     * because we need to update them
	 */
	public MedicaldsrstockmovTypeBrowserEdit(JFrame owner,MovementType old,boolean inserting) {
		super(owner,true);
		insert = inserting;
		medicaldsrstockmovType = old;//disease will be used for every operation
		lastdescription= medicaldsrstockmovType.getDescription();
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		//this.setBounds(300,300,330,210);
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.medstockmovtype.newmedicalsstockmovementtyperecord"));
		} else {
			this.setTitle(MessageBundle.getMessage("angal.medstockmovtype.editingmedicalsstockmovementtyperecord"));
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.NORTH);  // Generated
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
			//dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  // Generated
			dataPanel.add(getJDataPanel(), null);
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
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
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
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					String description = descriptionTextField.getText();
					medicaldsrstockmovType.setDescription(description);
					medicaldsrstockmovType.setCode(codeTextField.getText());
					medicaldsrstockmovType.setType((String) typeComboBox.getSelectedItem());
					
					MedicaldsrstockmovTypeBrowserManager manager = new MedicaldsrstockmovTypeBrowserManager();
					boolean result;
					if (insert) { // inserting
						try {
							result = manager.newMedicaldsrstockmovType(medicaldsrstockmovType);
							if (result) {
								fireMedicaldsrstockmovInserted(medicaldsrstockmovType);
								dispose();
							} else
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
						} catch (OHServiceException e1) {
							result = false;
							OHServiceExceptionUtil.showMessages(e1);
						}
					}
					else { // updating
						if (description.equals(lastdescription)){
							dispose();	
						} else {
							try {
								result = manager.updateMedicaldsrstockmovType(medicaldsrstockmovType);
								if (result) {
									fireMedicaldsrstockmovUpdated();
									dispose();
								} else
									JOptionPane.showMessageDialog(null,
											MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
							} catch (OHServiceException e1) {
								result = false;
								OHServiceExceptionUtil.showMessages(e1);
							}
						}

					}
					
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
			descriptionTextField = new JTextField(20);
			if (!insert) {
				descriptionTextField.setText(medicaldsrstockmovType.getDescription());
				lastdescription=medicaldsrstockmovType.getDescription();
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
			codeTextField = new VoLimitedTextField(10);
			if (!insert) {
				codeTextField.setText(medicaldsrstockmovType.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
	}
	
	/**
	 * This method initializes typeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			typeComboBox.addItem("+");
			typeComboBox.addItem("-");
			if (!insert) {
				typeComboBox.setSelectedItem(medicaldsrstockmovType.getType());
				typeComboBox.setEnabled(false);
			}
		}
		return typeComboBox;
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
			jDataPanel.add(getJCodeLabelPanel(), null);
			jDataPanel.add(getCodeTextField(), null);
			jDataPanel.add(getJDescriptionLabelPanel(), null);
			jDataPanel.add(getDescriptionTextField(), null);
			jDataPanel.add(getJTypeLabelPanel(), null);
			jDataPanel.add(getTypeComboBox(), null);
		}
		return jDataPanel;
	}

	/**
	 * This method initializes jCodeLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJCodeLabel() {
		if (jCodeLabel == null) {
			jCodeLabel = new JLabel();
			jCodeLabel.setText(MessageBundle.getMessage("angal.medstockmovtype.codemaxchars"));
		}
		return jCodeLabel;
	}

	/**
	 * This method initializes jCodeLabelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJCodeLabelPanel() {
		if (jCodeLabelPanel == null) {
			jCodeLabelPanel = new JPanel();
			//jCodeLabelPanel.setLayout(new BorderLayout());
			jCodeLabelPanel.add(getJCodeLabel(), BorderLayout.CENTER);
		}
		return jCodeLabelPanel;
	}

	/**
	 * This method initializes jDescriptionLabelPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJDescriptionLabelPanel() {
		if (jDescriptionLabelPanel == null) {
			jDescripitonLabel = new JLabel();
			jDescripitonLabel.setText(MessageBundle.getMessage("angal.common.description"));
			jDescriptionLabelPanel = new JPanel();
			jDescriptionLabelPanel.add(jDescripitonLabel, null);
		}
		return jDescriptionLabelPanel;
	}
	
	private JPanel getJTypeLabelPanel() {
		if (jTypeLabelPanel == null) {
			jTypeLabel = new JLabel();
			jTypeLabel.setText(MessageBundle.getMessage("angal.medstockmovtype.type"));
			jTypeLabelPanel = new JPanel();
			jTypeLabelPanel.add(jTypeLabel, null);
		}
		return jTypeLabelPanel;
	}
	


}  //  @jve:decl-index=0:visual-constraint="146,61"


