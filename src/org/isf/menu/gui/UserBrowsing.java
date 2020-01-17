
package org.isf.menu.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;
import org.isf.utils.db.BCrypt;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;



public class UserBrowsing extends ModalJFrame implements UserEdit.UserListener {
/*
	// x cattura chiusura finestra e lancio evento quit
	public void	windowActivated(WindowEvent e) {}
	public void	windowClosed(WindowEvent e) {
		fireQuitInserted();
	}
	public void	windowClosing(WindowEvent e) {
		fireQuitInserted();
	}
	public void	windowDeactivated(WindowEvent e) {}
	public void	windowDeiconified(WindowEvent e) {}
	public void	windowIconified(WindowEvent e) {}
	public void	windowOpened(WindowEvent e) {}
	
	
	
	
	// gestione evento quit _____________________________________
	
	private EventListenerList quitListeners = new EventListenerList();

    public void addQuitListener(QuitListener listener) {
    	quitListeners.add(QuitListener.class, listener);
    }

    public void removeQuitListener(QuitListener listener) {
    	quitListeners.remove(QuitListener.class, listener);
    }

    private void fireQuitInserted() {
        AWTEvent event = new AWTEvent(this, AWTEvent.RESERVED_ID_MAX + 1) {};

        EventListener[] listeners = quitListeners.getListeners(QuitListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((QuitListener)listeners[i]).quitInserted(event);
    }
	*/
	
	// messaggi raccolti da UserEdit_________________________________________ 
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	public void userInserted(AWTEvent e) {				
		User u = (User)e.getSource();
		pUser.add(0,u);	
		((UserBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	public void userUpdated(AWTEvent e) {
		pUser.set(selectedrow,user);
		((UserBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow >-1)
			table.setRowSelectionInterval(selectedrow,selectedrow);	
	}
	
	
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 200;
	private int pfrmWidth;
	private int pfrmHeight;
	private int selectedrow;
	private JLabel selectlabel;
	private JComboBox pbox;
	private ArrayList<User> pUser;
	private String[] pColums = { 
			MessageBundle.getMessage("angal.menu.userm"), 
			MessageBundle.getMessage("angal.menu.groupm"), 
			MessageBundle.getMessage("angal.menu.descm") };
	private int[] pColumwidth = {70, 70, 150 };
	private User user;
	private DefaultTableModel model ;
	private JTable table;
	private JScrollPane scrollPane;
	
	private String pSelection;
	
	private UserBrowsing myFrame;
	private UserBrowsingManager manager = Context.getApplicationContext().getBean(UserBrowsingManager.class);

	public UserBrowsing() {
		
		setTitle(MessageBundle.getMessage("angal.menu.usersbrowser"));
		myFrame = this;
		
		//addWindowListener(this);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmWidth = screensize.width / 2 + 100;
		pfrmHeight = screensize.height / 2;
		setBounds(screensize.width / 4, screensize.height / 4, pfrmWidth,
				pfrmHeight);
		
		model = new UserBrowserModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(pColumwidth[0]);
		table.getColumnModel().getColumn(1).setPreferredWidth(pColumwidth[1]);
		table.getColumnModel().getColumn(2).setPreferredWidth(pColumwidth[2]);
		//table.getColumnModel().getColumn(3).setPreferredWidth(pColumwidth[3]);
				
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		
		selectlabel = new JLabel(MessageBundle.getMessage("angal.menu.selectgroup"));
		buttonPanel.add(selectlabel);
		
		pbox = new JComboBox();
		pbox.addItem(MessageBundle.getMessage("angal.menu.all"));
        ArrayList<UserGroup> group = null;
        try {
            group = manager.getUserGroup();
        } catch (OHServiceException e) {
            OHServiceExceptionUtil.showMessages(e);
        }
        if(group != null) {
            for (UserGroup elem : group) {
                pbox.addItem(elem);
            }
        }
		pbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pSelection=pbox.getSelectedItem().toString();
				if (pSelection.compareTo("ALL") == 0)
					model = new UserBrowserModel();
				else
					model = new UserBrowserModel(pSelection);
				model.fireTableDataChanged();
				table.updateUI();
			}
		});
		buttonPanel.add(pbox);

		JButton buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				user = new User("",new UserGroup(),"","");
				new UserEdit(myFrame, user, true);
			}
		});
		buttonPanel.add(buttonNew);
		
		JButton buttonEdit = new JButton(MessageBundle.getMessage("angal.common.edit"));
		buttonEdit.setMnemonic(KeyEvent.VK_E);
		buttonEdit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {		
					selectedrow = table.getSelectedRow();
					user = (User)(((UserBrowserModel) model).getValueAt(table.getSelectedRow(), -1));	
					new	UserEdit(myFrame, user,false);
				}	 				
			}
		});
		buttonPanel.add(buttonEdit);
		
		JButton buttonResetPassword = new JButton( MessageBundle.getMessage("angal.menu.resetpassword"));
		buttonResetPassword.setMnemonic(KeyEvent.VK_R);
		buttonResetPassword.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {		
					selectedrow = table.getSelectedRow();
					user = (User)(((UserBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
					
					// 1. Insert new password
					JPasswordField pwd = new JPasswordField(10);
					pwd.addAncestorListener(new AncestorListener() {
						
						public void ancestorRemoved(AncestorEvent event) {}
						public void ancestorMoved(AncestorEvent event) {}
						public void ancestorAdded(AncestorEvent event) {
							event.getComponent().requestFocusInWindow();
						}
					});
					String newPassword = "";
					while (newPassword.equals("")) {
					    int action = JOptionPane.showConfirmDialog(UserBrowsing.this, pwd, MessageBundle.getMessage("angal.menu.onepleaseinsertnewpassowrdminsixdigits"), JOptionPane.OK_CANCEL_OPTION);
					    if (action == JOptionPane.CANCEL_OPTION) return;
					    newPassword = new String(pwd.getPassword());
					    if (newPassword == null || newPassword.equals("") || newPassword.length() < 6) {
					    	JOptionPane.showMessageDialog(UserBrowsing.this, MessageBundle.getMessage("angal.menu.pleaseinsertavalidpasswordminsixdigits"));
					    	newPassword = "";
					    	pwd.setText("");
					    }
					}
				    
				    // 2. Retype new password
				    pwd.setText("");
				    int action = JOptionPane.showConfirmDialog(UserBrowsing.this, pwd, MessageBundle.getMessage("angal.menu.twopleaserepeatpassword"), JOptionPane.OK_CANCEL_OPTION);
				    if (action == JOptionPane.CANCEL_OPTION) return;
				    String newPassword2 = new String(pwd.getPassword());
				    
				    // 3. Check & Save
				    if (!newPassword.equals(newPassword2)) {
				    	JOptionPane.showMessageDialog(UserBrowsing.this, MessageBundle.getMessage("angal.menu.retypeerrorpleaseretry"));
				    	return;
				    }
					if (newPassword != null && !newPassword.equals("") && newPassword.length() >= 6) {
						String hashed = BCrypt.hashpw(new String(newPassword), BCrypt.gensalt());
						user.setPasswd(hashed);
                        try {
                            if (manager.updatePassword(user))
                                JOptionPane.showMessageDialog(UserBrowsing.this, MessageBundle.getMessage("angal.menu.thepasswordhasbeenchanged"));
                        } catch (OHServiceException e) {
                            OHServiceExceptionUtil.showMessages(e);
                        }
                    } else {
						JOptionPane.showMessageDialog(UserBrowsing.this, MessageBundle.getMessage("angal.menu.pleaseinsertavalidpasswordminsixdigits"));
					}
				}	 				
			}
		});
		buttonPanel.add(buttonResetPassword);
		
		JButton buttonDelete = new JButton(MessageBundle.getMessage("angal.common.delete"));
		buttonDelete.setMnemonic(KeyEvent.VK_D);
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {
				User m = (User)(((UserBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
				int n = JOptionPane.showConfirmDialog(
                        null,
                        MessageBundle.getMessage("angal.menu.deleteuser")+" \""+m.getUserName()+"\" ?",
                        MessageBundle.getMessage("angal.hospital"),
                        JOptionPane.YES_NO_OPTION);

                    try {
                        if ((n == JOptionPane.YES_OPTION) && (manager.deleteUser(m))){
                            pUser.remove(table.getSelectedRow());
                            model.fireTableDataChanged();
                            table.updateUI();
                            }
                    } catch (OHServiceException e) {
                        OHServiceExceptionUtil.showMessages(e);
                    }
                }
			}
		});
		buttonPanel.add(buttonDelete);
		
		JButton buttonClose = new JButton(MessageBundle.getMessage("angal.common.close"));
		buttonClose.setMnemonic(KeyEvent.VK_C);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		buttonPanel.add(buttonClose);

		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

		
	class UserBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserBrowserModel(String s) {
            try {
                pUser = manager.getUser(s);
            } catch (OHServiceException e) {
                OHServiceExceptionUtil.showMessages(e);
            }
        }
		public UserBrowserModel() {
            try {
                pUser = manager.getUser();
            } catch (OHServiceException e) {
                OHServiceExceptionUtil.showMessages(e);
            }
        }
		public int getRowCount() {
			if (pUser == null)
				return 0;
			return pUser.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			if (c == 0) {
				return pUser.get(r).getUserName();
			} else if (c == -1) {
				return pUser.get(r);
			} else if (c == 1) {
				return pUser.get(r).getUserGroupName();
			} else if (c == 2) {
				return pUser.get(r).getDesc();
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}

}
