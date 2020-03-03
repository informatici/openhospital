
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.UserGroup;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;



public class UserGroupBrowsing extends ModalJFrame implements GroupEdit.GroupListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void groupInserted(AWTEvent e) {
		pGroup.add(0,group);
		((UserGroupBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	public void groupUpdated(AWTEvent e) {
		pGroup.set(selectedrow,group);
		((UserGroupBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow >-1)
			table.setRowSelectionInterval(selectedrow,selectedrow);
	
	}
	
	private static final int DEFAULT_WIDTH = 200;
	private static final int DEFAULT_HEIGHT = 150;
	private int pfrmWidth;
	private int pfrmHeight;
	private int selectedrow;
	private ArrayList<UserGroup> pGroup;
	private String[] pColums = { MessageBundle.getMessage("angal.menu.groupm"), MessageBundle.getMessage("angal.menu.descm") };
	private int[] pColumwidth = {70,  100 };
	private UserGroup group;
	private DefaultTableModel model ;
	private JTable table;
	
	private UserGroupBrowsing myFrame;

	private UserBrowsingManager manager = Context.getApplicationContext().getBean(UserBrowsingManager.class);

	public UserGroupBrowsing() {
		myFrame = this;
		setTitle(MessageBundle.getMessage("angal.menu.groupsbrowser"));
		
		//addWindowListener(this);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmWidth = screensize.width / 2;
		pfrmHeight = screensize.height / 4;
		setBounds(screensize.width / 4, screensize.height / 4, pfrmWidth,
				pfrmHeight);
		
		model = new UserGroupBrowserModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(pColumwidth[0]);
		table.getColumnModel().getColumn(1).setPreferredWidth(pColumwidth[1]);
				
		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		
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
					group = (UserGroup)(((UserGroupBrowserModel) model).getValueAt(table.getSelectedRow(), -1));	
					//GroupEdit editrecord = 
					new GroupEdit(myFrame, group,false);
					//editrecord.addGroupListener(UserGroupBrowsing.this);
					//editrecord.setVisible(true);
				}	 				
			}
		});
		buttonPanel.add(buttonEdit);
		
		JButton buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				group=new UserGroup();
				//GroupEdit newrecord = 
				new GroupEdit(myFrame, group,true);
				//newrecord.addGroupListener(UserGroupBrowsing.this);
				//newrecord.setVisible(true);
			}
		});
		buttonPanel.add(buttonNew);
		
		JButton buttonPrivilege = new JButton(MessageBundle.getMessage("angal.menu.groupmenu"));
		buttonPrivilege.setMnemonic(KeyEvent.VK_M);
		buttonPrivilege.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() < 0) {
					JOptionPane.showMessageDialog(				
	                        null,
	                        MessageBundle.getMessage("angal.common.pleaseselectarow"),
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {
					UserGroup m = (UserGroup)(((UserGroupBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
					new PrivilegeTree(myFrame, m);
				}
			}
		});
		buttonPanel.add(buttonPrivilege);
		
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
				UserGroup m = (UserGroup)(((UserGroupBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
				int n = JOptionPane.showConfirmDialog(
                        null,
                        MessageBundle.getMessage("angal.menu.deletegroup")+"\""+m.getCode()+"\" ?",
                        MessageBundle.getMessage("angal.hospital"),
                        JOptionPane.YES_NO_OPTION);

                    try {
                        if ((n == JOptionPane.YES_OPTION) && (manager.deleteGroup(m))){
                            pGroup.remove(table.getSelectedRow());
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
		setLocationRelativeTo(null);
		setVisible(true);
	}

		
	class UserGroupBrowserModel extends DefaultTableModel {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserGroupBrowserModel() {
            try {
                pGroup = manager.getUserGroup();
            } catch (OHServiceException e) {
                OHServiceExceptionUtil.showMessages(e);
            }
        }
		public int getRowCount() {
			if (pGroup == null)
				return 0;
			return pGroup.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			if (c == 0) {
				return pGroup.get(r).getCode();
			} else if (c == -1) {
				return pGroup.get(r);
			} else if (c == 1) {
				return pGroup.get(r).getDesc();
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
