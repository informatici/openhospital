
package org.isf.menu.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;
import org.isf.utils.db.BCrypt;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;

public class UserEdit extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EventListenerList userListeners = new EventListenerList();

    public interface UserListener extends EventListener {
        public void userUpdated(AWTEvent e);
        public void userInserted(AWTEvent e);
    }

    public void addUserListener(UserListener l) {
        userListeners.add(UserListener.class, l);
    }

    public void removeUserListener(UserListener listener) {
        userListeners.remove(UserListener.class, listener);
    }

    private void fireUserInserted(User aUser) {
        AWTEvent event = new AWTEvent(aUser, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = userListeners.getListeners(UserListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((UserListener)listeners[i]).userInserted(event);
    }
    private void fireUserUpdated() {
        AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = userListeners.getListeners(UserListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((UserListener)listeners[i]).userUpdated(event);
    }
    
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JTextField descriptionTextField = null;
	private JTextField nameTextField = null;
	private JPasswordField pwdTextField = null;
	private JPasswordField pwd2TextField = null;
	private JLabel typeLabel = null;
	private JLabel nameLabel = null;
	private JLabel pwdLabel = null;
	private JLabel pwd2Label = null;
	private JComboBox typeComboBox = null;
    
	private User user = null;
	private boolean insert = false;
    
	/**
     * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
     * because we need to update them
	 */
	public UserEdit(UserBrowsing parent, User old,boolean inserting) {
		super(parent, (inserting?MessageBundle.getMessage("angal.menu.newuserrecord"):MessageBundle.getMessage("angal.menu.editinguserrecord")),true);
		addUserListener(parent);
		insert = inserting;
		user = old;		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		//this.setBounds(300,300,450,300);
		this.setContentPane(getJContentPane());
		/*
		if (insert) {
			this.setTitle("New user record");
		} else {
			this.setTitle("Editing user record");
		}
		*/
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
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
	 * tipo combo
	 * nome text
	 * desc text
	 * pwd  text
	 * pwd2	text
	 */
	private JPanel getDataPanel() {
		if (dataPanel == null) {
			typeLabel = new JLabel();
			typeLabel.setText(MessageBundle.getMessage("angal.menu.group"));						
			nameLabel = new JLabel();
			nameLabel.setText(MessageBundle.getMessage("angal.menu.name")); 
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.common.description"));  
			
			JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
			JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
			JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  
			
			dataPanel.add(typeLabel, null); 
			
			comboPanel.add(getTypeComboBox());
			dataPanel.add(comboPanel);  			
			
			dataPanel.add(nameLabel, null);  
			namePanel.add(getNameTextField());			
		    dataPanel.add(namePanel);
		    
		    if (insert) {
		    	JPanel pwdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
		    	pwdLabel = new JLabel();
				pwdLabel.setText(MessageBundle.getMessage("angal.menu.password"));
				dataPanel.add(pwdLabel, null);  
				pwdPanel.add(getPwdTextField());
				dataPanel.add(pwdPanel);
				
				JPanel pwd2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
				pwd2Label = new JLabel();
				pwd2Label.setText(MessageBundle.getMessage("angal.menu.retypepassword"));
				dataPanel.add(pwd2Label, null);  
				pwd2Panel.add(getPwd2TextField());
				dataPanel.add(pwd2Panel);
		    }
						
			dataPanel.add(descLabel, null);  
			descPanel.add(getDescriptionTextField());
			dataPanel.add(descPanel);
						 
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
			okButton.setText(MessageBundle.getMessage("angal.common.ok")); // Generated
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String userName = nameTextField.getText();
					if (userName.equals("")) {
						JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.menu.pleaseinsertavalidusername"));
						return;

					}
					user.setUserName(userName);
					user.setDesc(descriptionTextField.getText());
					UserBrowsingManager manager = new UserBrowsingManager();
					boolean result = false;
					if (insert) {
						char[] password = pwdTextField.getPassword();
						char[] repeatPassword = pwd2TextField.getPassword();

						if (Arrays.equals(password, new char[0])) {
							JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.menu.pleaseinsertapassword"));
							return;
						}
						if (Arrays.equals(repeatPassword, new char[0])) {
							JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.menu.pleaseretypethepassword"));
							return;
						}
						if (!Arrays.equals(password, repeatPassword)) {
							JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.menu.passwordincorrectpleaseretype"));
							return;
						}
						String hashed = BCrypt.hashpw(new String(password), BCrypt.gensalt());
						user.setPasswd(hashed);
						user.setUserGroupName((UserGroup) typeComboBox.getSelectedItem());
                        try {
                            result = manager.newUser(user);
                        } catch (OHServiceException e1) {
                            OHServiceExceptionUtil.showMessages(e1);
                        }
                        if (result) {
							fireUserInserted(user);
							Arrays.fill(password, '0');
							Arrays.fill(repeatPassword, '0');
                        }
					} else {
						user.setUserGroupName((UserGroup) typeComboBox.getSelectedItem());
						try {
                            result = manager.updateUser(user);
                        } catch (OHServiceException e1) {
                            OHServiceExceptionUtil.showMessages(e1);
                        }
                        if (result) {
							fireUserUpdated();
                        }
					}

					if (!result) JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
					else {
						dispose();
					}
				}// end of method
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
				descriptionTextField = new JTextField(user.getDesc());
			}
			descriptionTextField.setColumns(25);
		}	
		return descriptionTextField;
	}
	
	
	private JTextField getNameTextField() {
		if (nameTextField == null) {
			if (insert) {
				nameTextField = new JTextField();
			} else {
				nameTextField = new JTextField(user.getUserName());
				nameTextField.setEnabled(false);
			}
			nameTextField.setColumns(15);
		}
		return nameTextField;
	}

	private JPasswordField getPwdTextField() {
		if (pwdTextField == null) {
			pwdTextField = new JPasswordField(15);
		}
		return pwdTextField;
	}
	
	private JTextField getPwd2TextField() {
		if (pwd2TextField == null) {
			pwd2TextField = new JPasswordField(15);
		}
		return pwd2TextField;
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
				UserBrowsingManager manager = new UserBrowsingManager();
                ArrayList<UserGroup> group = null;
                try {
                    group = manager.getUserGroup();
                } catch (OHServiceException e) {
                    OHServiceExceptionUtil.showMessages(e);
                }
                if(group != null) {
                    for (UserGroup elem : group) {
                        typeComboBox.addItem(elem);
                    }
                }
			} else {
				typeComboBox.addItem(user.getUserGroupName());
				typeComboBox.setEnabled(false);
			}
			Dimension d = typeComboBox.getPreferredSize();
			typeComboBox.setPreferredSize(new Dimension(150,d.height));
			
		}
		return typeComboBox;
	}


}
