package org.isf.disease.gui;


/*------------------------------------------
 * DiseaseBrowser - This class shows a list of diseases.
 * 					It is possible to filter data with a selection combo box
 * 					and edit-insert-delete records
 * -----------------------------------------
 * modification history
 * 25-gen-2006 - Rick, Vero, Pupo - first beta version
 * 03/11/2006 - ross - version is now 1.0 
 *------------------------------------------*/


import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.ModalJFrame;

public class DiseaseBrowser extends ModalJFrame implements DiseaseEdit.DiseaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 

	public void diseaseInserted(AWTEvent e) {
		pDisease.add(0,disease);
		((DiseaseBrowserModel)table.getModel()).fireTableDataChanged();
		//table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
	}
	
	public void diseaseUpdated(AWTEvent e) {
		pDisease.set(selectedrow,disease);
		((DiseaseBrowserModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow >-1)
			table.setRowSelectionInterval(selectedrow,selectedrow);
		
	}
	
	private int selectedrow;
	private JLabel selectlabel;
	private JComboBox pbox;
	private ArrayList<Disease> pDisease;
	private String[] pColums = {
			MessageBundle.getMessage("angal.common.codem"),
			MessageBundle.getMessage("angal.disease.typem"),
			MessageBundle.getMessage("angal.disease.namem")
	};
	private int[] pColumwidth = {50, 180, 200 };
	private Disease disease;
	private DefaultTableModel model ;
	private JTable table;
	private JFrame myFrame;
	private DiseaseType pSelection;
	private DiseaseBrowserManager manager = Context.getApplicationContext().getBean(DiseaseBrowserManager.class);
	private DiseaseTypeBrowserManager disTypeManager = Context.getApplicationContext().getBean(DiseaseTypeBrowserManager.class);
	
	
	public DiseaseBrowser() {
		
		setTitle(MessageBundle.getMessage("angal.disease.diseasesbrowser")+VERSION+")");
		myFrame = this;
		model = new DiseaseBrowserModel();
		table = new JTable(model);
		table.setDefaultRenderer(Object.class, new ColorTableCellRenderer());
		table.getColumnModel().getColumn(0).setMaxWidth(pColumwidth[0]);
		table.getColumnModel().getColumn(1).setPreferredWidth(pColumwidth[1]);
		table.getColumnModel().getColumn(2).setPreferredWidth(pColumwidth[2]);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		
		selectlabel = new JLabel(MessageBundle.getMessage("angal.disease.selecttype"));
		buttonPanel.add(selectlabel);
		
		pbox = new JComboBox();
		pbox.addItem(new DiseaseType("0", MessageBundle.getMessage("angal.disease.allm")));
		ArrayList<DiseaseType> type = null;
		try {
			type = disTypeManager.getDiseaseType();
		}catch(OHServiceException e){
			if(e.getMessages() != null){
				for(OHExceptionMessage msg : e.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		//for efficiency in the sequent for
		if(type != null){
			for (DiseaseType elem : type) {
				pbox.addItem(elem);
			}
		}
		pbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pSelection = (DiseaseType) pbox.getSelectedItem();
				if (pSelection.getDescription().compareTo(MessageBundle.getMessage("angal.disease.allm")) == 0)
					model = new DiseaseBrowserModel();
				else
					model = new DiseaseBrowserModel(pSelection.getCode());
				model.fireTableDataChanged();
				table.updateUI();
			}
		});
		buttonPanel.add(pbox);
		
		JButton buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				disease=new Disease(null,"",new DiseaseType("",""));	//disease will reference the new record
				DiseaseEdit newrecord = new DiseaseEdit(myFrame,disease,true);
				newrecord.addDiseaseListener(DiseaseBrowser.this);
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
							DiseaseBrowser.this,
							MessageBundle.getMessage("angal.common.pleaseselectarow"),
							MessageBundle.getMessage("angal.hospital"),
							JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {		
					selectedrow = table.getSelectedRow();
					disease = (Disease)(((DiseaseBrowserModel) model).getValueAt(selectedrow, -1));	
					DiseaseEdit editrecord = new DiseaseEdit(myFrame,disease,false);
					editrecord.addDiseaseListener(DiseaseBrowser.this);
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
							DiseaseBrowser.this,
							MessageBundle.getMessage("angal.common.pleaseselectarow"),
							MessageBundle.getMessage("angal.hospital"),
							JOptionPane.PLAIN_MESSAGE);				
					return;									
				}else {
					selectedrow = table.getSelectedRow();
					disease = (Disease)(((DiseaseBrowserModel) model).getValueAt(selectedrow, -1));
					int n = JOptionPane.showConfirmDialog(
							DiseaseBrowser.this,
							MessageBundle.getMessage("angal.disease.deletedisease") + " \""+disease.getDescription()+"\" ?",
							MessageBundle.getMessage("angal.hospital"),
							JOptionPane.YES_NO_OPTION);
					try{
						if ((n == JOptionPane.YES_OPTION) && (manager.deleteDisease(disease))){
							disease.setIpdInInclude(false);
							disease.setIpdOutInclude(false);
							disease.setOpdInclude(false);
							diseaseUpdated(null);
						}
					}catch(OHServiceException e){
						if(e.getMessages() != null){
							for(OHExceptionMessage msg : e.getMessages()){
								JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
							}
						}
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
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	
	class DiseaseBrowserModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DiseaseBrowserModel(String s) {
			try {
				pDisease = manager.getDisease(s);
			}catch(OHServiceException e){
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
		}
		public DiseaseBrowserModel() {
			try {
				pDisease = manager.getDiseaseAll();
			}catch(OHServiceException e){
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
		}
		public int getRowCount() {
			if (pDisease == null)
				return 0;
			return pDisease.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}
		
		public int getColumnCount() {
			return pColums.length;
		}
		
		public Object getValueAt(int r, int c) {
			Disease disease = pDisease.get(r);
			if (c == 0) {
				return disease.getCode();
			} else if (c == -1) {
				return disease;
			} else if (c == 1) {
				return disease.getType().getDescription();
			} else if (c == 2) {
				return disease.getDescription();
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	class ColorTableCellRenderer extends DefaultTableCellRenderer
	{  
	   /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
	      boolean hasFocus, int row, int column)
	   {  
		   Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
		   cell.setForeground(Color.BLACK);
		   if(!((Disease)table.getValueAt(row,-1)).getIpdInInclude() &&
				   !((Disease)table.getValueAt(row,-1)).getIpdOutInclude() &&
				   !((Disease)table.getValueAt(row,-1)).getOpdInclude()) {
			   cell.setForeground(Color.GRAY);
		   }
	      return cell;
	   }
	}
}
