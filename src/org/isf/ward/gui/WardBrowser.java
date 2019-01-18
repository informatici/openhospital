package org.isf.ward.gui;

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

import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.ward.manager.WardBrowserManager;
import org.isf.ward.model.Ward;

/**
 * This class shows a list of wards.
 * It is possible to edit-insert-delete records
 * 
 * @author Rick
 * 
 */
public class WardBrowser extends ModalJFrame implements WardEdit.WardListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void wardInserted(AWTEvent e) {
		pWard.add(0,ward);
		((WardBrowserModel)table.getModel()).fireTableDataChanged();
		//table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	
	public void wardUpdated(AWTEvent e) {
		pWard.set(selectedrow,ward);
		((WardBrowserModel)table.getModel()).fireTableDataChanged();
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
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.code"),
			MessageBundle.getMessage("angal.ward.name"),
			MessageBundle.getMessage("angal.ward.telephone"),
			MessageBundle.getMessage("angal.ward.fax"),
			MessageBundle.getMessage("angal.ward.email"),
			MessageBundle.getMessage("angal.ward.beds"),
			MessageBundle.getMessage("angal.ward.nurses"),
			MessageBundle.getMessage("angal.ward.doctors"),
			MessageBundle.getMessage("angal.ward.haspharmacy"),
			MessageBundle.getMessage("angal.ward.ismale"),
			MessageBundle.getMessage("angal.ward.isfemale")};
	private int[] pColumwidth = {45, 80, 60, 60, 80, 30, 30, 30, 30, 30, 30};
	private Class[] pColumnClass = {String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Boolean.class, Boolean.class, Boolean.class};
	private int selectedrow;
	private ArrayList<Ward> pWard;
	private Ward ward;
	private final JFrame myFrame;
	
	/**
	 * This is the default constructor
	 */
	public WardBrowser() {
		super();
		myFrame = this;
		//check if in the db maternity ward exists
		WardBrowserManager manager = new WardBrowserManager();
		try {
			manager.maternityControl(true);
		}catch(OHServiceException e){
			OHServiceExceptionUtil.showMessages(e);
		}
		initialize();
		setVisible(true);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle(MessageBundle.getMessage("angal.ward.wardsbrowser"));
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
						ward = (Ward)(((WardBrowserModel) model).getValueAt(table.getSelectedRow(), -1));	
						WardEdit editrecord = new WardEdit(myFrame,ward,false);
						editrecord.addWardListener(WardBrowser.this);
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
					ward=new Ward(null,"","","","",null,null,null,false,false);	//operation will reference the new record
					WardEdit newrecord = new WardEdit(myFrame,ward,true);
					newrecord.addWardListener(WardBrowser.this);
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
								WardBrowser.this,
								MessageBundle.getMessage("angal.common.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);				
						return;							
					}else {
						WardBrowserManager wardManager = new WardBrowserManager();
						Ward ward = (Ward)(((WardBrowserModel) model).getValueAt(table.getSelectedRow(), -1));
						int n = JOptionPane.showConfirmDialog(
								WardBrowser.this,
								MessageBundle.getMessage("angal.ward.deleteward") + " \""+ward.getDescription()+"\" ?",
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.YES_NO_OPTION);
						try{
							if ((n == JOptionPane.YES_OPTION) && (wardManager.deleteWard(ward))){
								pWard.remove(table.getSelectedRow());
								model.fireTableDataChanged();
								table.updateUI();
							}
						}catch(OHServiceException e){
							OHServiceExceptionUtil.showMessages(e);
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
			model = new WardBrowserModel();
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
			table.getColumnModel().getColumn(8).setPreferredWidth(pColumwidth[9]);
			table.getColumnModel().getColumn(8).setPreferredWidth(pColumwidth[10]);
		}
		return table;
	}
	
	class WardBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public WardBrowserModel() {
			WardBrowserManager manager = new WardBrowserManager();
			try {
				pWard = manager.getWards();
			}catch(OHServiceException e){
				pWard = new ArrayList<Ward>();
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		public int getRowCount() {
			if (pWard == null)
				return 0;
			return pWard.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}
		
		public int getColumnCount() {
			return pColums.length;
		}
		
		public Object getValueAt(int r, int c) {
			Ward ward = pWard.get(r);
			if (c == 0) {
				return ward.getCode();
			} else if (c == -1) {
				return ward;
			} else if (c == 1) {
				return ward.getDescription();
			} else if (c == 2) {
				return ward.getTelephone();
			} else if (c == 3) {
				return ward.getFax();
			} else if (c == 4) {
				return ward.getEmail();
			} else if (c == 5) {
				return ward.getBeds();
			} else if (c == 6) {
				return ward.getNurs();
			} else if (c == 7) {
				return ward.getDocs();
			} else if (c == 8) {
				return ward.isPharmacy();
			} else if (c == 9) {
				return ward.isMale();
			} else if (c == 10) {
				return ward.isFemale();
			}
			return null;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return pColumnClass[columnIndex];
		}
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}
}
