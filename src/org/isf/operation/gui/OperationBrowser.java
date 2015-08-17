package org.isf.operation.gui;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.operation.manager.OperationBrowserManager;
import org.isf.operation.model.Operation;
import org.isf.opetype.manager.OperationTypeBrowserManager;
import org.isf.opetype.model.OperationType;
import org.isf.utils.jobjects.ModalJFrame;

/**
 * This class shows a list of operations.
 * It is possible to filter data with a selection combo box
 * and edit-insert-delete records
 * 
 * @author Rick, Vero, Pupo
 * 
 */
public class OperationBrowser extends ModalJFrame implements OperationEdit.OperationListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void operationInserted(AWTEvent e) {
		pOperation.add(0,operation);
		((OperationBrowserModel)table.getModel()).fireTableDataChanged();
		//table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	public void operationUpdated(AWTEvent e) {
		pOperation.set(selectedrow,operation);
		((OperationBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow >-1)
			table.setRowSelectionInterval(selectedrow,selectedrow);
		
	}
	
	
	private int pfrmBase = 8;
	private int pfrmWidth = 5;
	private int pfrmHeight = 5;
	private int pfrmBordX;
	private int pfrmBordY;
	private int selectedrow;
	private JLabel selectlabel;
	private JComboBox pbox;
	private ArrayList<Operation> pOperation;
	private String[] pColums = { MessageBundle.getMessage("angal.operation.idm"), MessageBundle.getMessage("angal.operation.typem"), MessageBundle.getMessage("angal.operation.namem")};
	private int[] pColumwidth = {50, 180, 200 };
	private Operation operation;
	private DefaultTableModel model ;
	private JTable table;
	private JFrame myFrame;
	private String pSelection;
	
	public OperationBrowser() {
		
		setTitle(MessageBundle.getMessage("angal.operation.operationsbrowser"));
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width - (screensize.width / pfrmBase * pfrmWidth)) / 2;
		pfrmBordY = (screensize.height - (screensize.height / pfrmBase * pfrmHeight)) / 2;
		this.setBounds(pfrmBordX,pfrmBordY,screensize.width / pfrmBase * pfrmWidth,screensize.height / pfrmBase * pfrmHeight);
		myFrame = this;
		model = new OperationBrowserModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setMaxWidth(pColumwidth[0]);
		table.getColumnModel().getColumn(1).setPreferredWidth(pColumwidth[1]);
		table.getColumnModel().getColumn(2).setPreferredWidth(pColumwidth[2]);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		
		selectlabel = new JLabel(MessageBundle.getMessage("angal.operation.selecttype"));
		buttonPanel.add(selectlabel);
		
		OperationTypeBrowserManager manager = new OperationTypeBrowserManager();
		pbox = new JComboBox();
		pbox.addItem(MessageBundle.getMessage("angal.operation.allm"));
		ArrayList<OperationType> type = manager.getOperationType();	//for efficiency in the sequent for
		for (OperationType elem : type) {
			pbox.addItem(elem);
		}
		pbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pSelection=pbox.getSelectedItem().toString();
				if (pSelection.compareTo(MessageBundle.getMessage("angal.operation.allm"))==0)
					model = new OperationBrowserModel();
				else
					model = new OperationBrowserModel(pSelection);
				model.fireTableDataChanged();
				table.updateUI();
			}
		});
		buttonPanel.add(pbox);
		
		JButton buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				operation=new Operation(null,"",new OperationType("",""), 0,0);	//operation will reference the new record
				OperationEdit newrecord = new OperationEdit(myFrame,operation,true);
				newrecord.addOperationListener(OperationBrowser.this);
				newrecord.setVisible(true);
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
					operation = (Operation)(((OperationBrowserModel) model).getValueAt(table.getSelectedRow(), -1));	
					OperationEdit editrecord = new OperationEdit(myFrame,operation,false);
					editrecord.addOperationListener(OperationBrowser.this);
					editrecord.setVisible(true);
				}
			}
		});
		buttonPanel.add(buttonEdit);
		
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
					OperationBrowserManager manager = new OperationBrowserManager();
					Operation m = (Operation)(((OperationBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
					int n = JOptionPane.showConfirmDialog(
							null,
							MessageBundle.getMessage("angal.operation.deleteoperation")+" \""+m.getDescription()+"\" ?",
							MessageBundle.getMessage("angal.hospital"),
							JOptionPane.YES_NO_OPTION);
					
					if ((n == JOptionPane.YES_OPTION) && (manager.deleteOperation(m))){
						pOperation.remove(table.getSelectedRow());
						model.fireTableDataChanged();
						table.updateUI();
					}
				}
			}
		});
		buttonPanel.add(buttonDelete);
		
		JButton buttonClose = new JButton(MessageBundle.getMessage("angal.common.close"));
		buttonClose.setMnemonic(KeyEvent.VK_C);
		buttonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonPanel.add(buttonClose);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	
	class OperationBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public OperationBrowserModel(String s) {
			OperationBrowserManager manager = new OperationBrowserManager();
			pOperation = manager.getOperation(s);
		}
		public OperationBrowserModel() {
			OperationBrowserManager manager = new OperationBrowserManager();
			pOperation = manager.getOperation();
			
		}
		public int getRowCount() {
			if (pOperation == null)
				return 0;
			return pOperation.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}
		
		public int getColumnCount() {
			return pColums.length;
		}
		
		public Object getValueAt(int r, int c) {
			if (c == 0) {
				return pOperation.get(r).getCode();
			} else if (c == -1) {
				return pOperation.get(r);
			} else if (c == 1) {
				return pOperation.get(r).getType().getDescription();
			} else if (c == 2) {
				return pOperation.get(r).getDescription();
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
