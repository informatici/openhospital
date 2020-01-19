package org.isf.medstockmovtype.gui;

import java.awt.*;
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

import org.isf.generaldata.MessageBundle;
import org.isf.medstockmovtype.gui.MedicaldsrstockmovTypeBrowserEdit.MedicaldsrstockmovTypeListener;
import org.isf.medstockmovtype.manager.MedicaldsrstockmovTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;

/**
 * Browsing of table MedicalStockMovType
 * 
 * @author Furlanetto, Zoia, Finotto
 * 
 */

public class MedicaldsrstockmovTypeBrowser extends ModalJFrame implements MedicaldsrstockmovTypeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<MovementType> pMedicaldsrstockmovType;
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.codem"),
			MessageBundle.getMessage("angal.common.descriptionm"),
			MessageBundle.getMessage("angal.medstockmovtype.typem")
	};
	private int[] pColumwidth = {80, 200, 40};

	private JPanel jContainPanel = null;
	private JPanel jButtonPanel = null;
	private JButton jNewButton = null;
	private JButton jEditButton = null;
	private JButton jCloseButton = null;
	private JButton jDeteleButton = null;
	private JTable jTable = null;
	private MedicaldsrstockmovTypeBrowserModel model;
	private int selectedrow;
	private MedicaldsrstockmovTypeBrowserManager manager = Context.getApplicationContext().getBean(MedicaldsrstockmovTypeBrowserManager.class);
	private MovementType medicaldsrstockmovType = null;
	private final JFrame myFrame;
	
	/**
	 * This method initializes 
	 * 
	 */
	public MedicaldsrstockmovTypeBrowser() {
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
        final int pfrmHeight =4;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2, (screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setTitle(MessageBundle.getMessage("angal.medstockmovtype.medicalsstockmovementtypebrowsing"));
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
					MovementType mdsr = new MovementType("","","");
					MedicaldsrstockmovTypeBrowserEdit newrecord = new MedicaldsrstockmovTypeBrowserEdit(myFrame,mdsr, true);
					newrecord.addMedicaldsrstockmovTypeListener(MedicaldsrstockmovTypeBrowser.this);
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
						medicaldsrstockmovType = (MovementType) (((MedicaldsrstockmovTypeBrowserModel) model)
								.getValueAt(selectedrow, -1));
						MedicaldsrstockmovTypeBrowserEdit newrecord = new MedicaldsrstockmovTypeBrowserEdit(myFrame,medicaldsrstockmovType, false);
						newrecord.addMedicaldsrstockmovTypeListener(MedicaldsrstockmovTypeBrowser.this);
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
						MovementType dis = (MovementType) (((MedicaldsrstockmovTypeBrowserModel) model)
								.getValueAt(jTable.getSelectedRow(), -1));
						int n = JOptionPane.showConfirmDialog(null,
								MessageBundle.getMessage("angal.medstockmovtype.deletemovementtype")+" \" "+dis.getDescription() + "\" ?",
								MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);
						
						if ((n == JOptionPane.YES_OPTION)) {
							
							boolean deleted;
							
							try {
								deleted = manager.deleteMedicaldsrstockmovType(dis);
							} catch (OHServiceException e) {
								deleted = false;
								OHServiceExceptionUtil.showMessages(e);
							}
							
							if (true == deleted) {
								pMedicaldsrstockmovType.remove(jTable.getSelectedRow());
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
			model = new MedicaldsrstockmovTypeBrowserModel();
			jTable = new JTable(model);
			jTable.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
			jTable.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);
		}return jTable;
	}
	
	class MedicaldsrstockmovTypeBrowserModel extends DefaultTableModel {

		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		public MedicaldsrstockmovTypeBrowserModel() {
			try {
				pMedicaldsrstockmovType = manager.getMedicaldsrstockmovType();
			} catch (OHServiceException e) {
				pMedicaldsrstockmovType = null;
				OHServiceExceptionUtil.showMessages(e);
			}
		}

		public int getRowCount() {
			if (pMedicaldsrstockmovType == null)
				return 0;
			return pMedicaldsrstockmovType.size();
		}

		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			MovementType movType = pMedicaldsrstockmovType.get(r);
			if (c == 0) {
				return movType.getCode();
			} else if (c == -1) {
				return movType;
			} else if (c == 1) {
				return movType.getDescription();
			} else if (c == 2) {
				return movType.getType();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
	}




	public void medicaldsrstockmovTypeUpdated(AWTEvent e) {
		pMedicaldsrstockmovType.set(selectedrow, medicaldsrstockmovType);
		((MedicaldsrstockmovTypeBrowserModel) jTable.getModel()).fireTableDataChanged();
		jTable.updateUI();
		if ((jTable.getRowCount() > 0) && selectedrow > -1)
			jTable.setRowSelectionInterval(selectedrow, selectedrow);
	}
	
	
	public void medicaldsrstockmovTypeInserted(AWTEvent e) {
		medicaldsrstockmovType = (MovementType)e.getSource();
		pMedicaldsrstockmovType.add(0, medicaldsrstockmovType);
		((MedicaldsrstockmovTypeBrowserModel) jTable.getModel()).fireTableDataChanged();
		if (jTable.getRowCount() > 0)
			jTable.setRowSelectionInterval(0, 0);
	}
	
	
}
