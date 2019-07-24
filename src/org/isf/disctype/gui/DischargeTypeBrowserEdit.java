package org.isf.disctype.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.disctype.manager.DischargeTypeBrowserManager;
import org.isf.disctype.model.DischargeType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;

public class DischargeTypeBrowserEdit extends JDialog{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList dischargeTypeListeners = new EventListenerList();

    public interface DischargeTypeListener extends EventListener {
        public void dischargeTypeUpdated(AWTEvent e);
        public void dischargeTypeInserted(AWTEvent e);
    }

    public void addDischargeTypeListener(DischargeTypeListener l) {
    	dischargeTypeListeners.add(DischargeTypeListener.class, l);
    }

    public void removeDischargeTypeListener(DischargeTypeListener listener) {
    	dischargeTypeListeners.remove(DischargeTypeListener.class, listener);
    }

    private void fireDischargeInserted(DischargeType anDischargeType) {
        AWTEvent event = new AWTEvent(anDischargeType, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = dischargeTypeListeners.getListeners(DischargeTypeListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((DischargeTypeListener)listeners[i]).dischargeTypeInserted(event);
    }
    private void fireDischargeUpdated() {
        AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = dischargeTypeListeners.getListeners(DischargeTypeListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((DischargeTypeListener)listeners[i]).dischargeTypeUpdated(event);
    }
    
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JTextField descriptionTextField = null;
	private VoLimitedTextField codeTextField = null;	
	private String lastdescription;
	private DischargeType dischargeType = null;
	private boolean insert;
	private JPanel jDataPanel = null;	
	private JLabel jCodeLabel = null;
	private JPanel jCodeLabelPanel = null;
	private JPanel jDescriptionLabelPanel = null;
	private JLabel jDescripitonLabel = null;
	/**
     * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
     * because we need to update them
	 */
	public DischargeTypeBrowserEdit(JFrame owner,DischargeType old,boolean inserting) {
		super(owner,true);
		insert = inserting;
		dischargeType = old;//disease will be used for every operation
		lastdescription= dischargeType.getDescription();
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
			this.setTitle(MessageBundle.getMessage("angal.disctype.newdischargetyperecord"));
		} else {
			this.setTitle(MessageBundle.getMessage("angal.disctype.editingdischargetyperecord"));
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
					DischargeTypeBrowserManager manager = new DischargeTypeBrowserManager();

					if (descriptionTextField.getText().equals(lastdescription)){
						dispose();	
					}
				
					dischargeType.setDescription(descriptionTextField.getText());
					dischargeType.setCode(codeTextField.getText());					
					boolean result = false;
					if (insert) {      // inserting
						try {
							result = manager.newDischargeType(dischargeType);
                            if (result) {
                                fireDischargeInserted(dischargeType);
                            }
                            if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
                            else  dispose();
						} catch (OHServiceException e1) {
							OHServiceExceptionUtil.showMessages(e1);
						}
                    }
                    else {                          // updating
                    	if (descriptionTextField.getText().equals(lastdescription)){
    						dispose();	
    					}else{
    						try {
								result = manager.updateDischargeType(dischargeType);
                                if (result) {
                                    fireDischargeUpdated();
                                }
                                if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
                                else  dispose();
							} catch (OHServiceException e1) {
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
				descriptionTextField.setText(dischargeType.getDescription());
				lastdescription=dischargeType.getDescription();
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
				codeTextField.setText(dischargeType.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
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
			jCodeLabel.setText(MessageBundle.getMessage("angal.common.codemaxchars"));
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
	
	
	


}  //  @jve:decl-index=0:visual-constraint="146,61"


