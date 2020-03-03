package org.isf.exatype.gui;

/*------------------------------------------
 * ExamTypeBrowser - list all exam types. let the user select an exam type to edit
 * -----------------------------------------
 * modification history
 * ??/??/2005 - first beta version 
 * 03/11/2006 - ross - version is now 1.0
 *------------------------------------------*/

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

import org.isf.exatype.gui.ExamTypeEdit.ExamTypeListener;
import org.isf.exatype.manager.ExamTypeBrowserManager;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;


public class ExamTypeBrowser extends ModalJFrame implements ExamTypeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 

	private ArrayList<ExamType> pExamType;
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.codem"),
			MessageBundle.getMessage("angal.common.descriptionm")
	};
	private int[] pColumwidth = {80, 200 };

	private JPanel jContainPanel = null;
	private JPanel jButtonPanel = null;
	private JButton jNewButton = null;
	private JButton jEditButton = null;
	private JButton jCloseButton = null;
	private JButton jDeteleButton = null;
	private JTable jTable = null;
	private ExamTypeBrowserModel model;
	private int selectedrow;
	private ExamTypeBrowserManager manager = Context.getApplicationContext().getBean(ExamTypeBrowserManager.class);
	private ExamType examType = null;
	private final JFrame myFrame;
	
	
	
	
	/**
	 * This method initializes 
	 * 
	 */
	public ExamTypeBrowser() {
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
		this.setTitle( MessageBundle.getMessage("angal.exatype.examtypebrowser")+"("+VERSION+")");
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
					examType = new ExamType("","");
					ExamTypeEdit newrecord = new ExamTypeEdit(myFrame,examType, true);
					newrecord.addExamTypeListener(ExamTypeBrowser.this);
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
						examType = (ExamType) (((ExamTypeBrowserModel) model)
								.getValueAt(selectedrow, -1));
						ExamTypeEdit newrecord = new ExamTypeEdit(myFrame,examType, false);
						newrecord.addExamTypeListener(ExamTypeBrowser.this);
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
						ExamType dis = (ExamType) (((ExamTypeBrowserModel) model)
								.getValueAt(jTable.getSelectedRow(), -1));
						int n = JOptionPane.showConfirmDialog(null,
								MessageBundle.getMessage("angal.exatype.deleteexamtype")+"\" "+dis.getDescription() + "\" ?",
								MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);
						
						if (n == JOptionPane.YES_OPTION) {
							
							boolean deleted;
							try {
								deleted = manager.deleteExamType(dis);
							} catch (OHServiceException e) {
								deleted = false;
								OHServiceExceptionUtil.showMessages(e);
							}
							
							if (true == deleted) {
								pExamType.remove(jTable.getSelectedRow());
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
			model = new ExamTypeBrowserModel();
			jTable = new JTable(model);
			jTable.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
			jTable.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);
		}return jTable;
	}
	
class ExamTypeBrowserModel extends DefaultTableModel {
		
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public ExamTypeBrowserModel() {
			ExamTypeBrowserManager manager = Context.getApplicationContext().getBean(ExamTypeBrowserManager.class);
			try {
				pExamType = manager.getExamType();
			} catch (OHServiceException e) {
				pExamType = null;
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		
		public int getRowCount() {
			if (pExamType == null)
				return 0;
			return pExamType.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			ExamType examType = pExamType.get(r);
			if (c == 0) {
				return examType.getCode();
			} else if (c == -1) {
				return examType;
			} else if (c == 1) {
				return examType.getDescription();
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}




	public void examTypeUpdated(AWTEvent e) {
		pExamType.set(selectedrow, examType);
		((ExamTypeBrowserModel) jTable.getModel()).fireTableDataChanged();
		jTable.updateUI();
		if ((jTable.getRowCount() > 0) && selectedrow > -1)
			jTable.setRowSelectionInterval(selectedrow, selectedrow);
	}
	
	
	public void examTypeInserted(AWTEvent e) {
		pExamType.add(0, examType);
		((ExamTypeBrowserModel) jTable.getModel()).fireTableDataChanged();
		if (jTable.getRowCount() > 0)
			jTable.setRowSelectionInterval(0, 0);
	}
	
	
}
