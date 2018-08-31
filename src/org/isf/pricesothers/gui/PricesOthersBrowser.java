package org.isf.pricesothers.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import org.isf.pricesothers.gui.PricesOthersEdit.PricesOthersListener;
import org.isf.pricesothers.manager.PricesOthersManager;
import org.isf.pricesothers.model.PricesOthers;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;

public class PricesOthersBrowser extends ModalJFrame implements PricesOthersListener {

	public void pOthersInserted(AWTEvent e) {
		jTablePricesOthers.setModel(new PricesOthersBrowserModel());
	}

	public void pOthersUpdated(AWTEvent e) {
		jTablePricesOthers.setModel(new PricesOthersBrowserModel());
	}
	
	private static final long serialVersionUID = 1L;
	private JTable jTablePricesOthers;
	private JScrollPane jScrollPaneTable;
	private JPanel jPanelButtons;
	private JButton jButtonNew;
	private JButton jButtonEdit;
	private JButton jButtonDelete;
	private JButton jButtonClose;
	private String[] columnNames = {
			MessageBundle.getMessage("angal.common.code"), 
			MessageBundle.getMessage("angal.common.description"), 
			MessageBundle.getMessage("angal.pricesothers.opdm"), 
			MessageBundle.getMessage("angal.pricesothers.ipdm"), 
			MessageBundle.getMessage("angal.pricesothers.daily"),
			MessageBundle.getMessage("angal.pricesothers.discharge"),
			MessageBundle.getMessage("angal.pricesothers.undefined")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
	private int[] columWidth = {100, 100, 50, 50, 50, 100, 100};
	private boolean[] columResizable = {false, true, false, false, false, false, false};
	
	static protected Class<?>[] cTypes = {String.class, String.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class};
	
	private PricesOthers pOthers;
	PricesOthersManager pOthersManager = new PricesOthersManager();
	private ArrayList<PricesOthers> pOthersArray;
	private JFrame myFrame;
	
	public PricesOthersBrowser() {
		myFrame = this;
		initComponents();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void initComponents() {
		setTitle(MessageBundle.getMessage("angal.pricesothers.titlebrowser")); //$NON-NLS-1$
		add(getJScrollPaneTable(), BorderLayout.CENTER);
		add(getJPanelButtons(), BorderLayout.SOUTH);
		//setSize(550, 240);
		pack();
		setLocationRelativeTo(null);
	}

	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setText(MessageBundle.getMessage("angal.common.close")); //$NON-NLS-1$
			jButtonClose.setMnemonic(KeyEvent.VK_C);
			jButtonClose.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
						dispose();
				}
			});
		}
		return jButtonClose;
	}

	private JButton getJButtonDelete() {
		if (jButtonDelete == null) {
			jButtonDelete = new JButton();
			jButtonDelete.setText(MessageBundle.getMessage("angal.common.delete")); //$NON-NLS-1$
			jButtonDelete.setMnemonic(KeyEvent.VK_D);
			jButtonDelete.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					if (jTablePricesOthers.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.pricesothers.pleaseselectanitemtodelete")); //$NON-NLS-1$
						return;									
					}else {		
						int selectedRow = jTablePricesOthers.getSelectedRow();
						pOthers = (PricesOthers)jTablePricesOthers.getModel().getValueAt(selectedRow, -1);
						if (pOthers.getId() == 1) {
							JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.sql.operationnotpermittedprotectedelement"));
							return;
						}
						int ok = JOptionPane.showConfirmDialog(
								null,
								MessageBundle.getMessage("angal.pricesothers.doyoureallywanttodeletethisitem"), //$NON-NLS-1$
								pOthers.getDescription(),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						
						if (ok == JOptionPane.OK_OPTION) {
							
							boolean result = false;
							try {
								result = pOthersManager.deleteOther(pOthers);
							}catch(OHServiceException e){
								OHServiceExceptionUtil.showMessages(e);
							}
							
							if (result) {
								
								jTablePricesOthers.setModel(new PricesOthersBrowserModel());
							} else {
								JOptionPane.showMessageDialog(
										null,
										MessageBundle.getMessage("angal.pricesothers.thedatacouldnotbedeleted")); //$NON-NLS-1$
							}
						}
					}
				}
			});
		}
		return jButtonDelete;
	}

	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton();
			jButtonEdit.setText(MessageBundle.getMessage("angal.common.edit")); //$NON-NLS-1$
			jButtonEdit.setMnemonic(KeyEvent.VK_E);
			jButtonEdit.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					if (jTablePricesOthers.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.pricesothers.pleaseselectanitemtoedit")); //$NON-NLS-1$
						return;									
					}else {		
						int selectedRow = jTablePricesOthers.getSelectedRow();
						PricesOthers pOther = (PricesOthers)jTablePricesOthers.getModel().getValueAt(selectedRow, -1);
						PricesOthersEdit editOther = new PricesOthersEdit(myFrame, pOther, false);	
						editOther.addOtherListener(PricesOthersBrowser.this);
						editOther.setVisible(true);
					}
				}
			});
		}
		return jButtonEdit;
	}

	private JButton getJButtonNew() {
		if (jButtonNew == null) {
			jButtonNew = new JButton();
			jButtonNew.setText(MessageBundle.getMessage("angal.common.new")); //$NON-NLS-1$
			jButtonNew.setMnemonic(KeyEvent.VK_N);
			jButtonNew.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					PricesOthers pOther = new PricesOthers("", "", true,true, false, false); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					PricesOthersEdit editOther = new PricesOthersEdit(myFrame, pOther, true);	
					editOther.addOtherListener(PricesOthersBrowser.this);
					editOther.setVisible(true);
				}
			});
		}
		return jButtonNew;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonNew());
			jPanelButtons.add(getJButtonEdit());
			jPanelButtons.add(getJButtonDelete());
			jPanelButtons.add(getJButtonClose());
		}
		return jPanelButtons;
	}

	private JScrollPane getJScrollPaneTable() {
		if (jScrollPaneTable == null) {
			jScrollPaneTable = new JScrollPane();
			jScrollPaneTable.setViewportView(getJTablePricesOthers());
			jScrollPaneTable.setSize(jTablePricesOthers.getPreferredSize());
		}
		return jScrollPaneTable;
	}

	private JTable getJTablePricesOthers() {
		if (jTablePricesOthers == null) {
			jTablePricesOthers = new JTable() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				// Override this method so that it returns the preferred
			    // size of the JTable instead of the default fixed size
			    public Dimension getPreferredScrollableViewportSize() {
			        return new Dimension((int) getPreferredSize().getWidth(), 200);
			    }
			};
			jTablePricesOthers.setModel(new PricesOthersBrowserModel());
			for (int i=0;i<columWidth.length; i++){
				jTablePricesOthers.getColumnModel().getColumn(i).setMinWidth(columWidth[i]);
		    	if (!columResizable[i]) jTablePricesOthers.getColumnModel().getColumn(i).setMaxWidth(columWidth[i]);
			}
			jTablePricesOthers.setAutoCreateColumnsFromModel(false);
		}
		return jTablePricesOthers;
	}

class PricesOthersBrowserModel extends DefaultTableModel {
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public PricesOthersBrowserModel() {
			pOthersManager = new PricesOthersManager();
			try {
				pOthersArray = pOthersManager.getOthers();
			}catch(OHServiceException e){
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		public int getRowCount() {
			if (pOthersArray == null)
				return 0;
			return pOthersArray.size();
		}
		
		public String getColumnName(int c) {
			return columnNames[c];
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public Object getValueAt(int r, int c) {
			
			PricesOthers price = pOthersArray.get(r);
			if (c == -1) {
				return price;
			} else if (c == 0) {
				return price.getCode();
			} else if (c == 1) {
				return price.getDescription();
			} else if (c == 2) {
				return price.isOpdInclude();
			} else if (c == 3) {
				return price.isIpdInclude();
			} else if (c == 4) {
				return price.isDaily();
			}  else if (c == 5) {
				return price.isDischarge();
			}  else if (c == 6) {
				return price.isUndefined();
			} return null;
		}
		
		public Class<?> getColumnClass(int column) {
			return cTypes[column];
		}
		
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}

}
