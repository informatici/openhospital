package org.isf.priceslist.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.priceslist.gui.ListEdit.ListListener;
import org.isf.priceslist.manager.PriceListManager;
import org.isf.priceslist.model.PriceList;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;

public class ListBrowser extends ModalJFrame  implements ListListener{

	public void listInserted(AWTEvent e) {
		try {
			listArray = listManager.getLists();
		}catch(OHServiceException ex){
			OHServiceExceptionUtil.showMessages(ex);
		}
		jTablePriceLists.setModel(new ListBrowserModel());
	}

	public void listUpdated(AWTEvent e) {
		((ListBrowserModel)jTablePriceLists.getModel()).fireTableDataChanged();
		jTablePriceLists.updateUI();
	}
	
	private static final long serialVersionUID = 1L;
	private JTable jTablePriceLists;
	private JScrollPane jScrollPaneTable;
	private JButton jButtonNew;
	private JPanel jPanelButtons;
	private JButton jButtonEdit;
	private JButton jButtonCopy;
	private JButton jButtonClose;
	private JButton jButtonDelete;
	private String[] columnNames = {
			MessageBundle.getMessage("angal.priceslist.idm"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.priceslist.name"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.common.description"), //$NON-NLS-1$
			MessageBundle.getMessage("angal.priceslist.currency") //$NON-NLS-1$
	};
	private int[] columWidth = {100, 100, 200, 100};
	private boolean[] columResizable = {false, false, true, false};
	
	private PriceList list;
	PriceListManager listManager = Context.getApplicationContext().getBean(PriceListManager.class);
	private ArrayList<PriceList> listArray;
	private JFrame myFrame;
			
	public ListBrowser() {
		myFrame = this;
		initComponents();
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		add(getJScrollPaneTable(), BorderLayout.CENTER);
		add(getJPanelButtons(), BorderLayout.SOUTH);
		setTitle(MessageBundle.getMessage("angal.priceslist.listbrowser"));
		setSize(500, 274);
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
					if (jTablePriceLists.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.priceslist.pleaseselectalisttodelete"));				 //$NON-NLS-1$
						return;									
					}else {		
						if (jTablePriceLists.getRowCount() == 1) {
							
							JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.priceslist.sorryatleastonelist"));
							return;
						}
						int selectedRow = jTablePriceLists.getSelectedRow();
						list = (PriceList)jTablePriceLists.getModel().getValueAt(selectedRow, -1);
						
						int ok = JOptionPane.showConfirmDialog(
								null,
								MessageBundle.getMessage("angal.priceslist.doyoureallywanttodeletethislistandallitsprices"), //$NON-NLS-1$
								list.getName(),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						try{
							if (ok == JOptionPane.OK_OPTION) {

								boolean result = false;
								result = listManager.deleteList(list);

								if (result) {

									listArray = listManager.getLists();
									jTablePriceLists.setModel(new ListBrowserModel());
								} else {
									JOptionPane.showMessageDialog(
											null,
											MessageBundle.getMessage("angal.priceslist.thedatacouldnotbedeleted")); //$NON-NLS-1$
								}
							}
						}catch(OHServiceException e){
							OHServiceExceptionUtil.showMessages(e);
						}
					}
				}
			});
		}
		return jButtonDelete;
	}

	private JButton getJButtonCopy() {
		if (jButtonCopy == null) {
			jButtonCopy = new JButton();
			jButtonCopy.setText(MessageBundle.getMessage("angal.priceslist.copy")); //$NON-NLS-1$
			jButtonCopy.setMnemonic(KeyEvent.VK_P);
			jButtonCopy.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					if (jTablePriceLists.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.priceslist.pleaseselectalisttocopy"));				 //$NON-NLS-1$
						return;									
					}else {		
						int selectedRow = jTablePriceLists.getSelectedRow();
						list = (PriceList)jTablePriceLists.getModel().getValueAt(selectedRow, -1);
						
						String newName = JOptionPane.showInputDialog(MessageBundle.getMessage("angal.priceslist.enterthenameforthenewlist")); //$NON-NLS-1$
						
						if (newName != null) {
							
							Double qty;
							Double step;
							Double startQty = 1.;
							Double minQty = 0.;
							Double maxQty = 100.;
							Double stepQty = 0.01;
							JSpinner jSpinnerQty = new JSpinner(new SpinnerNumberModel(startQty,minQty,maxQty,stepQty));
							
							int r = JOptionPane.showConfirmDialog(ListBrowser.this, 
								new Object[] { MessageBundle.getMessage("angal.priceslist.multiplier"), jSpinnerQty },
								MessageBundle.getMessage("angal.priceslist.multiplier"),
				        		JOptionPane.OK_CANCEL_OPTION, 
				        		JOptionPane.PLAIN_MESSAGE);
						
							if (r == JOptionPane.OK_OPTION) {
								try {
									qty = (Double) jSpinnerQty.getValue();
									if (qty == 0.) {
										JOptionPane.showMessageDialog(ListBrowser.this, 
											MessageBundle.getMessage("angal.priceslist.invalidmultiplierpleasetryagain"), //$NON-NLS-1$
											MessageBundle.getMessage("angal.priceslist.invalidmultiplier"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
										return;
									}
								} catch (Exception eee) {
									JOptionPane.showMessageDialog(ListBrowser.this, 
										MessageBundle.getMessage("angal.priceslist.invalidmultiplierpleasetryagain"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.priceslist.invalidmultiplier"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
									return;
								}
								
								startQty = 0.25;
								minQty = 0.;
								maxQty = 1.;
								stepQty = 0.01;
								jSpinnerQty = new JSpinner(new SpinnerNumberModel(startQty,minQty,maxQty,stepQty));
								
								r = JOptionPane.showConfirmDialog(ListBrowser.this, 
									new Object[] { MessageBundle.getMessage("angal.priceslist.rounduptothenearest"), jSpinnerQty },
									MessageBundle.getMessage("angal.priceslist.roundingfactor"),
					        		JOptionPane.OK_CANCEL_OPTION, 
					        		JOptionPane.PLAIN_MESSAGE);
							
								if (r == JOptionPane.OK_OPTION) {
									try {
										step = (Double) jSpinnerQty.getValue();
										if (step == 0.) {
											JOptionPane.showMessageDialog(ListBrowser.this, 
												MessageBundle.getMessage("angal.priceslist.invalidfactorpleasetryagain"), //$NON-NLS-1$
												MessageBundle.getMessage("angal.priceslist.invalidfactor"), //$NON-NLS-1$
												JOptionPane.ERROR_MESSAGE);
											return;
										}
									} catch (Exception eee) {
										JOptionPane.showMessageDialog(ListBrowser.this, 
											MessageBundle.getMessage("angal.priceslist.invalidfactorpleasetryagain"), //$NON-NLS-1$
											MessageBundle.getMessage("angal.priceslist.invalidfactor"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
										return;
									}
								} else return;
							} else return;
							
							// Save new list
							if(newName.equals("")) newName = MessageBundle.getMessage("angal.priceslist.copyof").concat(" ").concat(list.getName()); 
							PriceList copiedList = new PriceList(list.getId(),MessageBundle.getMessage("angal.priceslist.acode"),newName,MessageBundle.getMessage("angal.priceslist.adescription"),list.getCurrency());
							
							boolean result = false;
							try {
								result = listManager.copyList(copiedList, qty, step);

								if (result) {
									JOptionPane.showMessageDialog(null,
											MessageBundle.getMessage("angal.priceslist.listcopiedremembertoeditinformations")); //$NON-NLS-1$

									listArray = listManager.getLists();
									jTablePriceLists.setModel(new ListBrowserModel());

								} else {
									JOptionPane.showMessageDialog(null,
											MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
								}
							}catch(OHServiceException e){
								OHServiceExceptionUtil.showMessages(e);
							}

						} else return;
						
					}
				}
			});
		}
		return jButtonCopy;
	}

	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton();
			jButtonEdit.setText(MessageBundle.getMessage("angal.common.edit")); //$NON-NLS-1$
			jButtonEdit.setMnemonic(KeyEvent.VK_E);
			jButtonEdit.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
					
					if (jTablePriceLists.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.priceslist.pleaseselectalisttoedit"));				 //$NON-NLS-1$
						return;									
					}else {		
						int selectedRow = jTablePriceLists.getSelectedRow();
						list = (PriceList)jTablePriceLists.getModel().getValueAt(selectedRow, -1);
						ListEdit editList = new ListEdit(myFrame, list, false);	
						editList.addListListener(ListBrowser.this);
						editList.setVisible(true);
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
					
					PriceList newList = new PriceList(0, "", "", "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					ListEdit editList = new ListEdit(myFrame, newList, true);	
					editList.addListListener(ListBrowser.this);
					editList.setVisible(true);

				}
			});

		}
		return jButtonNew;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonNew());
			jPanelButtons.add(getJButtonCopy());
			jPanelButtons.add(getJButtonEdit());
			jPanelButtons.add(getJButtonDelete());
			jPanelButtons.add(getJButtonClose());
		}
		return jPanelButtons;
	}

	private JScrollPane getJScrollPaneTable() {
		if (jScrollPaneTable == null) {
			jScrollPaneTable = new JScrollPane();
			jScrollPaneTable.setViewportView(getJTablePriceLists());
		}
		return jScrollPaneTable;
	}

	private JTable getJTablePriceLists() {
		if (jTablePriceLists == null) {
			jTablePriceLists = new JTable();
			jTablePriceLists.setModel(new ListBrowserModel());
			
			for (int i=0;i<columWidth.length; i++){
				jTablePriceLists.getColumnModel().getColumn(i).setMinWidth(columWidth[i]);
		    	
		    	if (!columResizable[i]) jTablePriceLists.getColumnModel().getColumn(i).setMaxWidth(columWidth[i]);
			}
			jTablePriceLists.setAutoCreateColumnsFromModel(false); 
		}
		return jTablePriceLists;
	}
	
	class ListBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ListBrowserModel() {
			listManager = Context.getApplicationContext().getBean(PriceListManager.class);
			try {
				listArray = listManager.getLists();
			}catch(OHServiceException e){
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		public int getRowCount() {
			if (listArray == null)
				return 0;
			return listArray.size();
		}
		
		public String getColumnName(int c) {
			return columnNames[c];
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return listArray.get(r);
			} else if (c == 0) {
				return listArray.get(r).getCode();
			} else if (c == 1) {
				return listArray.get(r).getName();
			} else if (c == 2) {
				return listArray.get(r).getDescription();
			} else if (c == 3) {
				return listArray.get(r).getCurrency();
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
