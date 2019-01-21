package org.isf.supplier.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.supplier.manager.SupplierBrowserManager;
import org.isf.supplier.model.Supplier;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;

/**
 * This class shows a list of suppliers.
 * It is possible to edit-insert-delete records
 * 
 * @author Mwithi
 * 
 */
public class SupplierBrowser extends ModalJFrame implements SupplierEdit.SupplierListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void supplierInserted(AWTEvent e) {
		pSupplier.add(0,supplier);
		((SupplierBrowserModel)table.getModel()).fireTableDataChanged();
		//table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	
	public void supplierUpdated(AWTEvent e) {
		pSupplier.set(selectedrow,supplier);
		((SupplierBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow >-1)
			table.setRowSelectionInterval(selectedrow,selectedrow);
		
	}
	
	private int pfrmBase = 10;
	private int pfrmWidth = 8;
	private int pfrmHeight = 6;
	private int pfrmBordX;
	private int pfrmBordY;
	private JPanel jContentPane = null;
	private JPanel jButtonPanel = null;
	private JButton jEditButton = null;
	private JButton jNewButton = null;
	private JButton jDeleteButton = null;
	private JButton jCloseButton = null;
	private JScrollPane jScrollPane = null;
	private JTable table = null;
	private DefaultTableModel model = null;
	private String[] pColums = { MessageBundle.getMessage("angal.supplier.id"),
			MessageBundle.getMessage("angal.supplier.namem"),
			MessageBundle.getMessage("angal.supplier.addressm"),
			MessageBundle.getMessage("angal.supplier.taxcode"),
			MessageBundle.getMessage("angal.supplier.telephone"),
			MessageBundle.getMessage("angal.supplier.faxm"),
			MessageBundle.getMessage("angal.supplier.emailm"),
			MessageBundle.getMessage("angal.supplier.note"),
			MessageBundle.getMessage("angal.supplier.deletedm")};
	private int[] pColumwidth = {45, 80, 60, 60, 80, 30, 30, 30, 30};
	private int selectedrow;
	private List<Supplier> pSupplier;
	private Supplier supplier;
	private final JFrame myFrame;
	
	/**
	 * This is the default constructor
	 */
	public SupplierBrowser() {
		super();
		myFrame = this;
		initialize();
		setVisible(true);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(MessageBundle.getMessage("angal.supplier.suppliersbrowser"));
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width - (screensize.width / pfrmBase * pfrmWidth)) / 2;
		pfrmBordY = (screensize.height - (screensize.height / pfrmBase * pfrmHeight)) / 2;
		this.setBounds(pfrmBordX,pfrmBordY,screensize.width / pfrmBase * pfrmWidth,screensize.height / pfrmBase * pfrmHeight);
		this.setContentPane(getJContentPane());
		
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
			jContentPane.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.add(getJNewButton(), null);
			jButtonPanel.add(getJEditButton(), null);
			jButtonPanel.add(getJDeleteButton(), null);
			jButtonPanel.add(getJCloseButton(), null);
		}
		return jButtonPanel;
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
					if (table.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(				
								null,
								MessageBundle.getMessage("angal.common.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);				
						return;									
					}else {		
						selectedrow = table.getSelectedRow();
						supplier = (Supplier)(((SupplierBrowserModel) model).getValueAt(table.getSelectedRow(), -1));	
						SupplierEdit editrecord = new SupplierEdit(myFrame,supplier,false);
						editrecord.addSupplierListener(SupplierBrowser.this);
						editrecord.setVisible(true);
					}
				}
			});
		}
		return jEditButton;
	}
	
	/**
	 * This method initializes jNewButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJNewButton() {
		if (jNewButton == null) {
			jNewButton = new JButton();
			jNewButton.setText(MessageBundle.getMessage("angal.common.new"));
			jNewButton.setMnemonic(KeyEvent.VK_N);
			jNewButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					supplier = new Supplier();	//operation will reference the new record
					SupplierEdit newrecord = new SupplierEdit(myFrame,supplier,true);
					newrecord.addSupplierListener(SupplierBrowser.this);
					newrecord.setVisible(true);
				}
			});
		}
		return jNewButton;
	}
	
	/**
	 * This method initializes jDeleteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJDeleteButton() {
		if (jDeleteButton == null) {
			jDeleteButton = new JButton();
			jDeleteButton.setText(MessageBundle.getMessage("angal.common.delete"));
			jDeleteButton.setMnemonic(KeyEvent.VK_D);
			jDeleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (table.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(				
								SupplierBrowser.this,
								MessageBundle.getMessage("angal.common.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);				
						return;							
					}else {
                        SupplierBrowserManager supManager = new SupplierBrowserManager();
						Supplier m = (Supplier)(((SupplierBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
						if (m.getSupDeleted().equals('Y')) return;
						int n = JOptionPane.showConfirmDialog(
								SupplierBrowser.this,
								MessageBundle.getMessage("angal.supplier.deletesupplier") + " " + m.getSupName() + "?",
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.YES_NO_OPTION);
						
						if (n == JOptionPane.YES_OPTION) {
							m.setSupDeleted('Y');
							try {
								supManager.saveOrUpdate(m);
                            } catch (OHServiceException e) {
                                OHServiceExceptionUtil.showMessages(e);
                            }
							model.fireTableDataChanged();
							table.updateUI();
						}
					}
				}
			});
		}
		return jDeleteButton;
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
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
	
	/**
	 * This method initializes table	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (table == null) {
			model = new SupplierBrowserModel();
			table = new JTable(model);
			table.getColumnModel().getColumn(0).setMaxWidth(pColumwidth[0]);
			table.getColumnModel().getColumn(1).setPreferredWidth(pColumwidth[1]);
			table.getColumnModel().getColumn(2).setPreferredWidth(pColumwidth[2]);
			table.getColumnModel().getColumn(3).setPreferredWidth(pColumwidth[3]);
			table.getColumnModel().getColumn(4).setPreferredWidth(pColumwidth[4]);
			table.getColumnModel().getColumn(5).setPreferredWidth(pColumwidth[5]);
			table.getColumnModel().getColumn(6).setPreferredWidth(pColumwidth[6]);
			table.getColumnModel().getColumn(7).setPreferredWidth(pColumwidth[7]);
			table.getColumnModel().getColumn(8).setPreferredWidth(pColumwidth[8]);
		}
		return table;
	}
	
	class SupplierBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SupplierBrowserModel() {
			SupplierBrowserManager supplierBrowserManager = new SupplierBrowserManager();
			try {
				pSupplier = supplierBrowserManager.getAll();
            } catch (OHServiceException e) {
                OHServiceExceptionUtil.showMessages(e);
            }
		}
		public int getRowCount() {
			if (pSupplier == null)
				return 0;
			return pSupplier.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}
		
		public int getColumnCount() {
			return pColums.length;
		}
		
		public Object getValueAt(int r, int c) {
			Supplier sup = pSupplier.get(r);
			if (c == -1) {
				return sup;
			} else if (c == 0) {
				return sup.getSupId();
			} else if (c == 1) {
				return sup.getSupName();
			} else if (c == 2) {
				return sup.getSupAddress();
			} else if (c == 3) {
				return sup.getSupTaxcode();
			} else if (c == 4) {
				return sup.getSupPhone();
			} else if (c == 5) {
				return sup.getSupFax();
			} else if (c == 6) {
				return sup.getSupEmail();
			} else if (c == 7) {
				return sup.getSupNote();
			} else if (c == 8) {
				return sup.getSupDeleted().equals('Y');
			}
			return null;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == pColums.length - 1) return Boolean.class;
			return super.getColumnClass(columnIndex);
		}
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}
}
