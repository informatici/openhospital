package org.isf.disctype.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.disctype.gui.DischargeTypeBrowserEdit.DischargeTypeListener;
import org.isf.disctype.manager.DischargeTypeBrowserManager;
import org.isf.disctype.model.DischargeType;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;

/**
 * Browsing of table DischargeType
 * 
 * @author Furlanetto, Zoia
 * 
 */

public class DischargeTypeBrowser extends ModalJFrame implements DischargeTypeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<DischargeType> pDischargeType;
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.codem"),
			MessageBundle.getMessage("angal.common.descriptionm")
	};
	private int[] pColumwidth = {80, 200, 80};
	private JPanel jContainPanel = null;
	private JPanel jButtonPanel = null;
	private JButton jNewButton = null;
	private JButton jEditButton = null;
	private JButton jCloseButton = null;
	private JButton jDeteleButton = null;
	private JTable jTable = null;
	private DischargeTypeBrowserModel model;
	private int selectedrow;
	private DischargeTypeBrowserManager manager = Context.getApplicationContext().getBean(DischargeTypeBrowserManager.class);
	private DischargeType dischargeType = null;
	private final JFrame myFrame;
	
	
	
	
	/**
	 * This method initializes 
	 * 
	 */
	public DischargeTypeBrowser() {
		super();
		myFrame=this;
		initialize();
		setVisible(true);
	}
	
	
	private void initialize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		final int pfrmBase = 10;
        final int pfrmWidth = 5;
        final int pfrmHeight = 4;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2, (screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setTitle(MessageBundle.getMessage("angal.disctype.dischargetypebrowsing"));
		this.setContentPane(getJContainPanel());
		//pack();	
	}
	
	
	private JPanel getJContainPanel() {
		if (jContainPanel == null) {
			jContainPanel = new JPanel();
			jContainPanel.setLayout(new BorderLayout());
			jContainPanel.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContainPanel.add(new JScrollPane(getJTable()),
					java.awt.BorderLayout.CENTER);
			validate();
		}
		return jContainPanel;
	}
	
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.add(getJNewButton(), null);
			jButtonPanel.add(getJEditButton(), null);
			jButtonPanel.add(getJDeteleButton(), null);
			jButtonPanel.add(getJCloseButton(), null);
		}
		return jButtonPanel;
	}
	
	
	private JButton getJNewButton() {
		if (jNewButton == null) {
			jNewButton = new JButton();
			jNewButton.setText(MessageBundle.getMessage("angal.common.new"));
			jNewButton.setMnemonic(KeyEvent.VK_N);
			jNewButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					DischargeType mdsr = new DischargeType("","");
					DischargeTypeBrowserEdit newrecord = new DischargeTypeBrowserEdit(myFrame,mdsr, true);
					newrecord.addDischargeTypeListener(DischargeTypeBrowser.this);
					newrecord.setVisible(true);
				}
			});
		}
		return jNewButton;
	}
	
	/**
	 * This method initializes jEditButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJEditButton() {
		if (jEditButton == null) {
			jEditButton = new JButton();
			jEditButton.setText(MessageBundle.getMessage("angal.common.edit"));
			jEditButton.setMnemonic(KeyEvent.VK_E);
			jEditButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					if (jTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.common.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						selectedrow = jTable.getSelectedRow();
						dischargeType = (DischargeType) (((DischargeTypeBrowserModel) model)
								.getValueAt(selectedrow, -1));
						DischargeTypeBrowserEdit newrecord = new DischargeTypeBrowserEdit(myFrame,dischargeType, false);
						newrecord.addDischargeTypeListener(DischargeTypeBrowser.this);
						newrecord.setVisible(true);
					}
				}
			});
		}
		return jEditButton;
	}
	
	/**
	 * This method initializes jCloseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCloseButton() {
		if (jCloseButton == null) {
			jCloseButton = new JButton();
			jCloseButton.setText(MessageBundle.getMessage("angal.common.close"));
			jCloseButton.setMnemonic(KeyEvent.VK_C);
			jCloseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return jCloseButton;
	}
	
	/**
	 * This method initializes jDeteleButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJDeteleButton() {
		if (jDeteleButton == null) {
			jDeteleButton = new JButton();
			jDeteleButton.setText(MessageBundle.getMessage("angal.common.delete"));
			jDeteleButton.setMnemonic(KeyEvent.VK_D);
			jDeteleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (jTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.common.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						DischargeType dis = (DischargeType) (((DischargeTypeBrowserModel) model)
								.getValueAt(jTable.getSelectedRow(), -1));
                        int n = JOptionPane.showConfirmDialog(null,
                                MessageBundle.getMessage("angal.disctype.deleterow") + " \" "+dis.getDescription() + "\" ?",
                                MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);

                        if ((n == JOptionPane.YES_OPTION)) {

                            boolean deleted;

                            try {
                                deleted = manager.deleteDischargeType(dis);
                            } catch (OHServiceException e) {
                                deleted = false;
                                OHServiceExceptionUtil.showMessages(e);
                            }

                            if (true == deleted) {
                                pDischargeType.remove(jTable.getSelectedRow());
                                model.fireTableDataChanged();
                                jTable.updateUI();
                            }
                        }
					}
				}
				
			});
		}
		return jDeteleButton;
	}
	
	public JTable getJTable() {
		if (jTable == null) {
			model = new DischargeTypeBrowserModel();
			jTable = new JTable(model);
			jTable.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
			jTable.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);
		}return jTable;
	}

class DischargeTypeBrowserModel extends DefaultTableModel {
		
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DischargeTypeBrowserManager manager = Context.getApplicationContext().getBean(DischargeTypeBrowserManager.class);

		public DischargeTypeBrowserModel() {
			try {
				pDischargeType = manager.getDischargeType();
			} catch (OHServiceException e) {
				pDischargeType = null;
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		
		public int getRowCount() {
			if (pDischargeType == null)
				return 0;
			return pDischargeType.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			if (c == 0) {
				return pDischargeType.get(r).getCode();
			} else if (c == -1) {
				return pDischargeType.get(r);
			} else if (c == 1) {
				return pDischargeType.get(r).getDescription();
			} 
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}

public void dischargeTypeUpdated(AWTEvent e) {
	pDischargeType.set(selectedrow, dischargeType);
	((DischargeTypeBrowserModel) jTable.getModel()).fireTableDataChanged();
	jTable.updateUI();
	if ((jTable.getRowCount() > 0) && selectedrow > -1)
		jTable.setRowSelectionInterval(selectedrow, selectedrow);
}

public void dischargeTypeInserted(AWTEvent e) {
	dischargeType = (DischargeType)e.getSource();
	pDischargeType.add(0, dischargeType);
	((DischargeTypeBrowserModel) jTable.getModel()).fireTableDataChanged();
	if (jTable.getRowCount() > 0)
		jTable.setRowSelectionInterval(0, 0);
}
	
}
