package org.isf.supplier.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.supplier.manager.SupplierBrowserManager;
import org.isf.supplier.model.Supplier;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.JLabelRequired;
import org.isf.utils.jobjects.VoLimitedTextField;

/**
 * This class allows suppliers edits and inserts
 * 
 * @author Mwithi
 * 
 */
public class SupplierEdit extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList supplierListeners = new EventListenerList();
	
	public interface SupplierListener extends EventListener {
		public void supplierUpdated(AWTEvent e);
		public void supplierInserted(AWTEvent e);
	}
	
	public void addSupplierListener(SupplierListener l) {
		supplierListeners.add(SupplierListener.class, l);
	}
	
	public void removeSupplierListener(SupplierListener listener) {
		supplierListeners.remove(SupplierListener.class, listener);
	}
	
	private void fireSupplierInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = supplierListeners.getListeners(SupplierListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SupplierListener)listeners[i]).supplierInserted(event);
	}
	private void fireSupplierUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = supplierListeners.getListeners(SupplierListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SupplierListener)listeners[i]).supplierUpdated(event);
	}
	
	private int pfrmBase = 16;
	private int pfrmWidth = 5;
	private int pfrmHeight = 9;
	private int pfrmBordX;
	private int pfrmBordY;
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel nameLabel = null;
	private JLabel addressLabel = null;
	private JLabel idLabel = null;
	private JLabel taxcodeLabel = null;
	private JLabel phoneLabel = null;
	private JLabel faxLabel = null;
	private JLabel emailLabel = null;
	private JLabel noteLabel = null;
	private JLabel isDeletedLabel = null;
	private JLabel requiredLabel = null;
//	private JLabel junkLabel = null;
	private JTextField nameTextField = null;
	private JTextField idTextField = null;
	private JTextField addressTexField = null;
	private JTextField taxcodeTestField = null;
	private JTextField phoneTextField = null;
	private JTextField faxTextField = null;
	private JTextField emailTextField = null;
	private JTextField noteTextField = null;
	private JCheckBox isDeletedCheck = null;
//	private String lastdescription;
	private Supplier supplier = null;
	private boolean insert = false;

	private SupplierBrowserManager supplierBrowserManager = Context.getApplicationContext().getBean(SupplierBrowserManager.class);
	
	/**
	 * 
	 * This is the default constructor; we pass the parent frame
	 * (because it is a jdialog), the arraylist and the selected
	 * row because we need to update them
	 */
	public SupplierEdit(JFrame parent,Supplier old,boolean inserting) {
		super(parent, true);
		insert = inserting;
		supplier = old;		//operation will be used for every operation
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width - (screensize.width / pfrmBase * pfrmWidth)) / 2;
		pfrmBordY = (screensize.height - (screensize.height / pfrmBase * pfrmHeight)) / 2;
		this.setBounds(pfrmBordX,pfrmBordY,screensize.width / pfrmBase * pfrmWidth,screensize.height / pfrmBase * pfrmHeight);
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.supplier.newsupplier"));
		} else {
			this.setTitle(MessageBundle.getMessage("angal.supplier.editsupplier"));
		}
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
			idLabel = new JLabel(MessageBundle.getMessage("angal.supplier.id"));
			nameLabel = new JLabelRequired(MessageBundle.getMessage("angal.supplier.name"));
			addressLabel = new JLabel(MessageBundle.getMessage("angal.supplier.address"));
			taxcodeLabel = new JLabel(MessageBundle.getMessage("angal.supplier.taxcode"));
			phoneLabel = new JLabel(MessageBundle.getMessage("angal.supplier.telephone"));
			faxLabel = new JLabel(MessageBundle.getMessage("angal.supplier.faxnumber"));
			emailLabel = new JLabel(MessageBundle.getMessage("angal.supplier.email"));
			noteLabel = new JLabel(MessageBundle.getMessage("angal.supplier.note"));
			isDeletedLabel = new JLabel(MessageBundle.getMessage("angal.supplier.deleted"));
			requiredLabel= new JLabel(MessageBundle.getMessage("angal.supplier.requiredfields"));
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  // Generated
			dataPanel.add(idLabel, null);
			dataPanel.add(getIdTextField(), null);
			dataPanel.add(nameLabel, null);
			dataPanel.add(getNameTextField(),null);
			dataPanel.add(addressLabel, null);
			dataPanel.add(getAddressTextField(), null);
			dataPanel.add(taxcodeLabel, null);
			dataPanel.add(getTaxcodeTextField(),null);
			dataPanel.add(phoneLabel, null);
			dataPanel.add(getPhoneTextField(),null);
			dataPanel.add(faxLabel, null);
			dataPanel.add(getFaxTextField(),null);
			dataPanel.add(emailLabel, null);
			dataPanel.add(getEmailTextField(),null);
			dataPanel.add(noteLabel, null);
			dataPanel.add(getNoteTextField(),null);
			if (!insert) {
				dataPanel.add(isDeletedLabel, null);
				dataPanel.add(getIsDeleted(),null);
			}
			dataPanel.add(requiredLabel, null);
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
					if (nameTextField.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(				
								null,
								MessageBundle.getMessage("angal.supplier.pleaseinsertaname"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					}

					supplier.setSupName(nameTextField.getText());
					supplier.setSupAddress(addressTexField.getText().trim());
					supplier.setSupTaxcode(taxcodeTestField.getText());
					supplier.setSupPhone(phoneTextField.getText());
					supplier.setSupFax(faxTextField.getText());
					supplier.setSupEmail(emailTextField.getText());
					supplier.setSupNote(noteTextField.getText());
					if (!insert) supplier.setSupDeleted(isDeletedCheck.isSelected() ? 'Y' : 'N');
					else supplier.setSupDeleted('N');
					
					boolean result = false;
					if (insert) { // inserting
						try {
							result = supplierBrowserManager.saveOrUpdate(supplier);
                        } catch (OHServiceException ex) {
                            OHServiceExceptionUtil.showMessages(ex);
                        }
						if (result) {
							fireSupplierInserted();
						}
					}
					else { // updating
						try {
							result = supplierBrowserManager.saveOrUpdate(supplier);
                        } catch (OHServiceException ex) {
                            OHServiceExceptionUtil.showMessages(ex);
                        }
						if (result) {
							fireSupplierUpdated();
						}
					}
					if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
					else  dispose();
				}
			});
		}
		return okButton;
	}
	
	/**
	 * This method initializes idTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getIdTextField() {
		if (idTextField == null) {
			idTextField = new VoLimitedTextField(11, 50);
			if (!insert) {				
				idTextField.setText(String.valueOf(supplier.getSupId()));
			}
			idTextField.setEnabled(false);
		}
		return idTextField;
	}
	
	/**
	 * This method initializes nameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNameTextField() {
		if (nameTextField == null) {
			nameTextField = new VoLimitedTextField(100, 50);
			if (!insert) {				
				nameTextField.setText(supplier.getSupName());
			}
		}
		return nameTextField;
	}
	
	/**
	 * This method initializes addressTexField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getAddressTextField() {
		if (addressTexField == null) {
			addressTexField = new VoLimitedTextField(150, 50);			
			if (!insert) {
				addressTexField.setText(supplier.getSupAddress());
			}
		}
		return addressTexField;
	}
	
	/**
	 * This method initializes taxcodeTestField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTaxcodeTextField() {
		if (taxcodeTestField == null) {
			taxcodeTestField = new VoLimitedTextField(50, 50);
			if (!insert) {
				taxcodeTestField.setText(supplier.getSupTaxcode());
			}
		}
		return taxcodeTestField;
	}
	
	/**
	 * This method initializes phoneTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPhoneTextField() {
		if (phoneTextField == null) {
			phoneTextField = new VoLimitedTextField(20, 50);
			if (!insert) {
				phoneTextField.setText(supplier.getSupPhone());
			}
		}
		return phoneTextField;
	}
	
	/**
	 * This method initializes faxTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFaxTextField() {
		if (faxTextField == null) {
			faxTextField = new VoLimitedTextField(20, 50);
			if (!insert) {
				faxTextField.setText(supplier.getSupFax());
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
			emailTextField = new VoLimitedTextField(100, 50);
			if (!insert) {
				emailTextField.setText(supplier.getSupEmail());
			}
		}
		return emailTextField;
	}
	
	/**
	 * This method initializes noteTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNoteTextField() {
		if (noteTextField == null) {
			noteTextField = new VoLimitedTextField(200, 50);
			if (!insert) {
				noteTextField.setText(supplier.getSupNote());
			}
		}
		return noteTextField;
	}
	
	/**
	 * This method initializes isDeletedCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getIsDeleted() {
		if (isDeletedCheck==null) {
			isDeletedCheck = new JCheckBox();
			if (!insert) {
				isDeletedCheck.setSelected(supplier.getSupDeleted().equals('Y'));
			}
		}
		return isDeletedCheck;
	}
} 