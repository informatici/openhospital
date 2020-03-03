
package org.isf.menu.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
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

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.UserGroup;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;

public class GroupEdit extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList groupListeners = new EventListenerList();

	private UserBrowsingManager manager = Context.getApplicationContext().getBean(UserBrowsingManager.class);

    public interface GroupListener extends EventListener {
        public void groupUpdated(AWTEvent e);
        public void groupInserted(AWTEvent e);
    }

    public void addGroupListener(GroupListener l) {
    	groupListeners.add(GroupListener.class, l);
    }

    public void removeGroupListener(GroupListener listener) {
    	groupListeners.remove(GroupListener.class, listener);
    }

    private void fireGroupInserted(UserGroup aGroup) {
        AWTEvent event = new AWTEvent(aGroup, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = groupListeners.getListeners(GroupListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((GroupListener)listeners[i]).groupInserted(event);
    }
    private void fireGroupUpdated() {
        AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = groupListeners.getListeners(GroupListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((GroupListener)listeners[i]).groupUpdated(event);
    }
    
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JTextField descriptionTextField = null;
	private JTextField nameTextField = null;
	private JLabel nameLabel = null;
	
    
	private UserGroup group = null;
	private boolean insert = false;
    
	/**
     * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
     * because we need to update them
	 */
	public GroupEdit(UserGroupBrowsing parent, UserGroup old,boolean inserting) {
		super(parent, (inserting?MessageBundle.getMessage("angal.menu.newgrouprecord"):MessageBundle.getMessage("angal.menu.editinggrouprecord")),true);
		addGroupListener(parent);
		insert = inserting;
		group = old;		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		this.setBounds(300,300,350,150);
		this.setContentPane(getJContentPane());
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
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
	 * 
	 * nome text
	 * desc text
	 
	 */
	private JPanel getDataPanel() {
		if (dataPanel == null) {
								
			nameLabel = new JLabel();
			nameLabel.setText(MessageBundle.getMessage("angal.menu.name")); 
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.common.description"));  
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  
		
			dataPanel.add(nameLabel, null);  
			dataPanel.add(getNameTextField(), null);
			  
			dataPanel.add(descLabel, null);  
			dataPanel.add(getDescriptionTextField(), null);  
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
			buttonPanel.add(getOkButton(), null);  
			buttonPanel.add(getCancelButton(), null); 
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
			cancelButton.setText(MessageBundle.getMessage("angal.common.cancel"));  
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
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (nameTextField.getText().equals("")){
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.menu.pleaseinsertavalidusergroup"));
						return;
					}
					
					group.setCode(nameTextField.getText());
					
					group.setDesc(descriptionTextField.getText());
					boolean result = false;
					if (insert) {      // inserting
						//System.out.println("saving... "+group);
                        try {
                            result = manager.newUserGroup(group);
                        } catch (OHServiceException e1) {
                            OHServiceExceptionUtil.showMessages(e1);
                        }
                        if (result) {
                           fireGroupInserted(group);
                        }
                    }
                    else {                          // updating
                        try {
                            result = manager.updateUserGroup(group);
                        } catch (OHServiceException e1) {
                            OHServiceExceptionUtil.showMessages(e1);
                        }
                        if (result) {
							fireGroupUpdated();
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
	 * This method initializes descriptionTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescriptionTextField() {
		if (descriptionTextField == null) {
			if (insert) {
				descriptionTextField = new JTextField();
			} else {
				descriptionTextField = new JTextField(group.getDesc());
			}
		}
		return descriptionTextField;
	}
	
	
	private JTextField getNameTextField() {
		if (nameTextField == null) {
			if (insert) {
				nameTextField = new JTextField();
			} else {
				nameTextField = new JTextField(group.getCode());
				nameTextField.setEditable(false);
			}
		}
		return nameTextField;
	}
	


}
