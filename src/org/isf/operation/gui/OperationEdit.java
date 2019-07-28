package org.isf.operation.gui;

/*----------------------------------------------------------
 * modification history
 * ====================
 * 13/02/09 - Alex - added Major/Minor control
 -----------------------------------------------------------*/

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.operation.manager.OperationBrowserManager;
import org.isf.operation.model.Operation;
import org.isf.opetype.manager.OperationTypeBrowserManager;
import org.isf.opetype.model.OperationType;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;

/**
 * This class allows operations edits and inserts
 * 
 * @author Rick, Vero, Pupo
 * 
 */
public class OperationEdit extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList operationListeners = new EventListenerList();

	public interface OperationListener extends EventListener {
		public void operationUpdated(AWTEvent e);

		public void operationInserted(AWTEvent e);
	}

	public void addOperationListener(OperationListener l) {
		operationListeners.add(OperationListener.class, l);
	}

	public void removeOperationListener(OperationListener listener) {
		operationListeners.remove(OperationListener.class, listener);
	}

	private void fireOperationInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};

		EventListener[] listeners = operationListeners.getListeners(OperationListener.class);
		for (int i = 0; i < listeners.length; i++)
			((OperationListener) listeners[i]).operationInserted(event);
	}

	private void fireOperationUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};

		EventListener[] listeners = operationListeners.getListeners(OperationListener.class);
		for (int i = 0; i < listeners.length; i++)
			((OperationListener) listeners[i]).operationUpdated(event);
	}

	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JLabel codeLabel = null;
	private JTextField descriptionTextField = null;
	private JTextField codeTextField = null;
	private JLabel typeLabel = null;
	private JComboBox typeComboBox = null;
	private String lastdescription;
	private Operation operation = null;
	private JRadioButton major = null;
	private JRadioButton minor = null;
	private JPanel radioButtonPanel;
	private boolean insert = false;

	/**
	 * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
	 * because we need to update them
	 */
	public OperationEdit(JFrame parent, Operation old, boolean inserting) {
		super(parent, true);
		insert = inserting;
		operation = old; // operation will be used for every operation
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {

		// Toolkit kit = Toolkit.getDefaultToolkit();
		// Dimension screensize = kit.getScreenSize();
		// pfrmBordX = (screensize.width - (screensize.width / pfrmBase * pfrmWidth)) /
		// 2;
		// pfrmBordY = (screensize.height - (screensize.height / pfrmBase * pfrmHeight))
		// / 2;
		// this.setBounds(pfrmBordX,pfrmBordY,screensize.width / pfrmBase *
		// pfrmWidth,screensize.height / pfrmBase * pfrmHeight);
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.operation.newoperationrecord")); //$NON-NLS-1$
		} else {
			this.setTitle(MessageBundle.getMessage("angal.operation.editingoperationrecord")); //$NON-NLS-1$
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.NORTH); // Generated
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH); // Generated
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
			typeLabel = new JLabel();
			typeLabel.setText(MessageBundle.getMessage("angal.operation.type")); // Generated //$NON-NLS-1$
			typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.common.description")); // Generated //$NON-NLS-1$
			descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			codeLabel = new JLabel();
			codeLabel.setText(MessageBundle.getMessage("angal.common.code")); //$NON-NLS-1$
			codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS)); // Generated
			dataPanel.add(typeLabel, null); // Generated
			dataPanel.add(getTypeComboBox(), null); // Generated
			dataPanel.add(codeLabel, null); // Generated
			dataPanel.add(getCodeTextField(), null); // Generated
			dataPanel.add(descLabel, null); // Generated
			dataPanel.add(getDescriptionTextField(), null); // Generated
			dataPanel.add(getRadioButtonPanel());
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
			buttonPanel.add(getOkButton(), null); // Generated
			buttonPanel.add(getCancelButton(), null); // Generated
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
			cancelButton.setText(MessageBundle.getMessage("angal.common.cancel")); // Generated //$NON-NLS-1$
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
			okButton.setText(MessageBundle.getMessage("angal.common.ok")); // Generated //$NON-NLS-1$
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						if (insert) {
							String key = codeTextField.getText().trim();
							if (key.equals("")) { //$NON-NLS-1$
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.operation.pleaseinsertacode"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
								return;
							}
							if (key.length() > 10) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.common.codetoolongmaxchars"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$

								return;
							}
							OperationBrowserManager manager = new OperationBrowserManager();

							if (manager.codeControl(key)) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.common.codealreadyinuse"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$

								return;
							}

							/*
							 * if (manager.descriptionControl(descriptionTextField.getText(),
							 * ((OperationType)typeComboBox.getSelectedItem()).getCode())){
							 * JOptionPane.showMessageDialog( null, "Operation already present",
							 * "St Luke Hospital", JOptionPane.PLAIN_MESSAGE);
							 * 
							 * return; }
							 */
						}
						if (descriptionTextField.getText().equals("")) { //$NON-NLS-1$
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.operation.pleaseinsertavaliddescription"), //$NON-NLS-1$
									MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$
							return;
						}
						OperationBrowserManager manager = new OperationBrowserManager();
						if (descriptionTextField.getText().equals(lastdescription)) {
						} else {

							if (manager.descriptionControl(descriptionTextField.getText(),
									((OperationType) typeComboBox.getSelectedItem()).getCode())) {
								JOptionPane.showMessageDialog(null,
										MessageBundle.getMessage("angal.operation.operationalreadypresent"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.hospital"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$

								return;
							}
						}

						operation.setType((OperationType) typeComboBox.getSelectedItem());
						operation.setDescription(descriptionTextField.getText());
						operation.setCode(codeTextField.getText().trim().toUpperCase());
						if (major.isSelected()) {
							operation.setMajor(1);
						} else {
							operation.setMajor(0);
						}

						boolean result = false;
						if (insert) { // inserting
							result = manager.newOperation(operation);
							if (result) {
								fireOperationInserted();
							}
						} else { // updating
							result = manager.updateOperation(operation);
							if (result) {
								fireOperationUpdated();
							}
						}
						if (!result)
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved")); //$NON-NLS-1$
						else
							dispose();
					} catch (OHServiceException ex) {
						OHServiceExceptionUtil.showMessages(ex);
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
			descriptionTextField = new VoLimitedTextField(50, 50);
			if (!insert) {
				lastdescription = operation.getDescription();
				descriptionTextField.setText(lastdescription);
			}
		}
		return descriptionTextField;
	}

	/**
	 * This method initializes radioButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRadioButtonPanel() {
		if (radioButtonPanel == null) {

			radioButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			if (major == null) {

				major = getRadioButton(MessageBundle.getMessage("angal.operation.major"), 'a', true); //$NON-NLS-1$
				minor = getRadioButton(MessageBundle.getMessage("angal.operation.minor"), 'm', true); //$NON-NLS-1$

				ButtonGroup radioGroup = new ButtonGroup();

				radioGroup.add(major);
				radioGroup.add(minor);

				radioButtonPanel.add(major);
				radioButtonPanel.add(minor);

				if (insert) {
					major.setSelected(true);
				} else {
					if (operation.getMajor() == 1) {
						major.setSelected(true);
					} else {
						minor.setSelected(true);
					}
				}
			}

		}
		return radioButtonPanel;
	}

	private JRadioButton getRadioButton(String label, char mn, boolean active) {
		JRadioButton rb = new JRadioButton(label);
		rb.setMnemonic(KeyEvent.VK_A + (mn - 'A'));
		rb.setSelected(active);
		rb.setName(label);
		return rb;
	}

	/**
	 * This method initializes codeTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCodeTextField() {
		if (codeTextField == null) {
			codeTextField = new VoLimitedTextField(10, 50);
			if (!insert) {
				codeTextField.setText(operation.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
	}

	/**
	 * This method initializes typeComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			if (insert) {
				OperationTypeBrowserManager manager = new OperationTypeBrowserManager();
				ArrayList<OperationType> types;
				try {
					types = manager.getOperationType();

					for (OperationType elem : types) {
						typeComboBox.addItem(elem);
					}
				} catch (OHServiceException e) {
					OHServiceExceptionUtil.showMessages(e);
					types = null;
				}
			} else {
				typeComboBox.addItem(operation.getType());
				typeComboBox.setEnabled(false);
			}

		}
		return typeComboBox;
	}

}