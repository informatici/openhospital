/**
 * 11-dic-2005
 * author bob
 */
package org.isf.medicals.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medtype.manager.MedicalTypeBrowserManager;
import org.isf.medtype.model.MedicalType;
import org.isf.menu.gui.MainMenu;
import org.isf.stat.gui.report.GenericReportFromDateToDate;
import org.isf.stat.gui.report.GenericReportPharmaceuticalOrder;
import org.isf.utils.excel.ExcelExporter;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.JMonthYearChooser;
import org.isf.utils.jobjects.ModalJFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class shows a complete extended list of medical drugs,
 * supplies-sundries, diagnostic kits -reagents, laboratory chemicals. It is
 * possible to filter data with a selection combo box
 * and edit-insert-delete records
 * 
 * @author bob
 * 		   modified by alex:
 * 			- product code
 * 			- pieces per packet
 * 
 */

public class MedicalBrowser extends ModalJFrame { // implements RowSorterListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(MedicalBrowser.class);

	public void medicalInserted() {
		pMedicals.add(0,medical);
		((MedicalBrowsingModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if (table.getRowCount() > 0)
			table.setRowSelectionInterval(0, 0);
		repaint();
	}
	public void medicalUpdated() {
		pMedicals.set(selectedrow,medical);
		((MedicalBrowsingModel)table.getModel()).fireTableDataChanged();
		table.updateUI();
		if ((table.getRowCount() > 0) && selectedrow >-1)
			table.setRowSelectionInterval(selectedrow,selectedrow);
		repaint();
	
	}
	
	private static final int DEFAULT_WIDTH = 500;
	private static final int DEFAULT_HEIGHT = 400;
	private int pfrmWidth;
	private int pfrmHeight;
	private int selectedrow;
	private JLabel selectlabel;
	private JComboBox pbox;
	private ArrayList<Medical> pMedicals;
	private String[] pColums = {
				MessageBundle.getMessage("angal.medicals.typem"), 
				MessageBundle.getMessage("angal.common.code"), 
				MessageBundle.getMessage("angal.common.descriptionm"), 
				MessageBundle.getMessage("angal.medicals.pcsperpck"), 
				MessageBundle.getMessage("angal.medicals.stockm"),
				MessageBundle.getMessage("angal.medicals.critlevelm"),
				MessageBundle.getMessage("angal.medicals.outofstockm")
			};
	private int[] pColumwidth = {100,100,400,60,60,80,100};
	private boolean[] pColumResizable = {true,true,true,true,true,true,true};
	private Medical medical;
	private DefaultTableModel model ;
	private JTable table;
	private final JFrame me;
	
	private String pSelection;
	
	public MedicalBrowser() {
		me=this;
		setTitle(MessageBundle.getMessage("angal.medicals.pharmaceuticalbrowsing"));
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmWidth = 940; //screensize.width / 2;
		pfrmHeight = screensize.height / 2;
		setBounds((screensize.width - pfrmWidth) / 2, screensize.height / 4, pfrmWidth,
				pfrmHeight);
		
		model = new MedicalBrowsingModel();
		table = new JTable(model);

		table.setDefaultRenderer(Object.class,new ColorTableCellRenderer());
		for (int i=0;i<pColumwidth.length; i++){
			table.getColumnModel().getColumn(i).setMinWidth(pColumwidth[i]);
			if (!pColumResizable[i]) table.getColumnModel().getColumn(i).setMaxWidth(pColumwidth[i]);
		}
		add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		
		selectlabel = new JLabel(MessageBundle.getMessage("angal.medicals.selecttype"));
		buttonPanel.add(selectlabel);
		
		MedicalTypeBrowserManager manager = new MedicalTypeBrowserManager();
		pbox = new JComboBox();
		pbox.addItem(MessageBundle.getMessage("angal.medicals.allm"));
		ArrayList<MedicalType> type;
		try {
			type = manager.getMedicalType();
		
			//for efficiency in the sequent for
			for (MedicalType elem : type) {
				pbox.addItem(elem);
			}
		} catch (OHServiceException e1) {
			type = null;
			OHServiceExceptionUtil.showMessages(e1);
		}
		
		pbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pSelection=pbox.getSelectedItem().toString();
				if (pSelection.compareTo(MessageBundle.getMessage("angal.medicals.allm")) == 0)
					model = new MedicalBrowsingModel();
				else
					model = new MedicalBrowsingModel(pSelection);
				model.fireTableDataChanged();
				table.updateUI();
			}
		});
		buttonPanel.add(pbox);

		JButton buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				medical=new Medical(null,new MedicalType("",""),"","",0,0,0,0,0,0);;	//medical will reference the new record
				MedicalEdit newrecord = new MedicalEdit(medical,true,me);
				newrecord.setVisible(true);
				if(medical.getCode()!=null)medicalInserted();
			}
		});
		if (MainMenu.checkUserGrants("btnpharmaceuticalnew")) buttonPanel.add(buttonNew);
		
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
					medical = (Medical)(((MedicalBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
					MedicalEdit editrecord = new MedicalEdit(medical,false,me);
					editrecord.setVisible(true);
					medicalUpdated();
				}	 				
			}
		});
		if (MainMenu.checkUserGrants("btnpharmaceuticaledit")) buttonPanel.add(buttonEdit);
		
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
					MedicalBrowsingManager manager = new MedicalBrowsingManager();
					Medical m = (Medical)(((MedicalBrowsingModel) model).getValueAt(table.getSelectedRow(), -1));
					int n = JOptionPane.showConfirmDialog(
	                        null,
	                        MessageBundle.getMessage("angal.medicals.deletemedical") + " \""+m.getDescription()+"\" ?",
	                        MessageBundle.getMessage("angal.hospital"),
	                        JOptionPane.YES_NO_OPTION);
	
					if ((n == JOptionPane.YES_OPTION)){
						boolean deleted;
						try {
							deleted = (manager.deleteMedical(m));
						} catch (OHServiceException e) {
							deleted = false;
							OHServiceExceptionUtil.showMessages(e);
						}
						
						if (true == deleted) {
							pMedicals.remove(table.getSelectedRow());
							model.fireTableDataChanged();
							table.updateUI();
						}
					}
				}
			}
		});
		if (MainMenu.checkUserGrants("btnpharmaceuticaldel")) buttonPanel.add(buttonDelete);
		
		JButton buttonExport = new JButton(MessageBundle.getMessage("angal.medicals.export"));
		buttonExport.setMnemonic(KeyEvent.VK_X);
		buttonExport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				
				JFileChooser fcExcel = new JFileChooser();
				FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel (*.xls)","xls");
				fcExcel.setFileFilter(excelFilter);
				fcExcel.setFileSelectionMode(JFileChooser.FILES_ONLY);  
				fcExcel.setSelectedFile(new File(MessageBundle.getMessage("angal.medicals.stock")));
				
				int iRetVal = fcExcel.showSaveDialog(MedicalBrowser.this);
				if(iRetVal == JFileChooser.APPROVE_OPTION)
				{
					File exportFile = fcExcel.getSelectedFile();
					if (!exportFile.getName().endsWith("xls")) exportFile = new File(exportFile.getAbsoluteFile() + ".xls");
					
					ExcelExporter xlsExport = new ExcelExporter();
					try
					{
						xlsExport.exportTableToExcel(table, exportFile);
					} catch(IOException exc)
					{
						logger.info("Export to excel error : "+exc.getMessage());
					}
				}
			}
		});
		buttonPanel.add(buttonExport);
		
		JButton buttonStock = new JButton(MessageBundle.getMessage("angal.medicals.stockm"));
		buttonStock.setMnemonic(KeyEvent.VK_S);
		buttonStock.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				new GenericReportPharmaceuticalOrder(GeneralData.PHARMACEUTICALSTOCK);
			}
		});
		buttonPanel.add(buttonStock);
		
		JButton buttonOrderList = new JButton(MessageBundle.getMessage("angal.medicals.orderlist"));
		buttonOrderList.setMnemonic(KeyEvent.VK_O);
		buttonOrderList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				new GenericReportPharmaceuticalOrder(GeneralData.PHARMACEUTICALORDER);
			}
		});
		buttonPanel.add(buttonOrderList);
		
		JButton buttonExpiring = new JButton(MessageBundle.getMessage("angal.medicals.expiring"));
		buttonExpiring.setMnemonic(KeyEvent.VK_X);
		buttonExpiring.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				launchExpiringReport();
				
			}
		});
		buttonPanel.add(buttonExpiring);
		
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

	protected void launchExpiringReport() {
		
		ArrayList<String> options = new ArrayList<String>();
		options.add(MessageBundle.getMessage("angal.medicals.today"));
		options.add(MessageBundle.getMessage("angal.medicals.thismonth"));
		options.add(MessageBundle.getMessage("angal.medicals.othermonth"));
		
		Icon icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
		String option = (String) JOptionPane.showInputDialog(MedicalBrowser.this, 
				MessageBundle.getMessage("angal.medicals.pleaseselectperiod"), 
				MessageBundle.getMessage("angal.medicals.expiringreport"), 
				JOptionPane.INFORMATION_MESSAGE, 
				icon, 
				options.toArray(), 
				options.get(0));
		
		if (option == null) return;
		
		String from = null;
		String to = null;
		
		int i = 0;
		
		if (options.indexOf(option) == i) {
			GregorianCalendar gc = new GregorianCalendar();
			
			from = formatDateTimeReport(gc);
			to = from;
		}
		if (options.indexOf(option) == ++i) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
			from = formatDateTimeReport(gc);
			
			gc.set(GregorianCalendar.DAY_OF_MONTH, gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
			to = formatDateTimeReport(gc);
		}
		if (options.indexOf(option) == ++i) {
			GregorianCalendar monthYear;
			icon = new ImageIcon("rsc/icons/calendar_dialog.png"); //$NON-NLS-1$
			JMonthYearChooser monthYearChooser = new JMonthYearChooser();
	        int r = JOptionPane.showConfirmDialog(MedicalBrowser.this, 
	        		monthYearChooser, 
	        		MessageBundle.getMessage("angal.billbrowser.month"), 
	        		JOptionPane.OK_CANCEL_OPTION, 
	        		JOptionPane.PLAIN_MESSAGE,
	        		icon);

	        if (r == JOptionPane.OK_OPTION) {
	        	monthYear = monthYearChooser.getDate();
	        } else {
	            return;
	        }
	        
	        GregorianCalendar gc = new GregorianCalendar();
			gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
			gc.set(GregorianCalendar.MONTH, monthYear.get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.YEAR, monthYear.get(GregorianCalendar.YEAR));
			from = formatDateTimeReport(gc);
			
			gc.set(GregorianCalendar.DAY_OF_MONTH, gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
			to = formatDateTimeReport(gc);
		}
		new GenericReportFromDateToDate(from, to, "PharmaceuticalExpiration", MessageBundle.getMessage("angal.medicals.expiringreport"), false);
	}
	
	private String formatDateTimeReport(GregorianCalendar date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  //$NON-NLS-1$
		return sdf.format(date.getTime());
	}

	class MedicalBrowsingModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MedicalBrowsingModel(String s) {
			MedicalBrowsingManager manager = new MedicalBrowsingManager();
			try {
				pMedicals = manager.getMedicals(s);
			} catch (OHServiceException e) {
				pMedicals = null;
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		public MedicalBrowsingModel() {
			MedicalBrowsingManager manager = new MedicalBrowsingManager();
			try {
				pMedicals = manager.getMedicals();
			} catch (OHServiceException e) {
				pMedicals = null;
				OHServiceExceptionUtil.showMessages(e);
			}
		}
 
		
		public Class<?> getColumnClass(int c) { 
			if (c == 0) {
				return String.class;
			} else if (c == 1) {
				return String.class;
			} else if (c == 2) {
				return String.class;
			} else if (c == 3) {
				return Integer.class;
			} else if (c == 4) {
				return Double.class;
			} else if (c == 5) {
				return Double.class;
			} else if (c == 6) {
				return Boolean.class;
			} 
			return null;
		}
		
		public int getRowCount() {
			if (pMedicals == null)
				return 0;
			return pMedicals.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		public Object getValueAt(int r, int c) {
			Medical med = pMedicals.get(r);
			double actualQty = med.getInitialqty()+med.getInqty()-med.getOutqty();
			double minQuantity = med.getMinqty();
			if (c == -1) {
				return med;
			} else if (c == 0) {
				return med.getType().getDescription();
			} else if (c == 1) {
				return med.getProd_code();
			} else if (c == 2) {
				return med.getDescription();
			} else if (c == 3) {
				return med.getPcsperpck();
			} else if (c == 4) {
				return actualQty;
			} else if (c == 5) {
				return minQuantity;
			} else if(c == 6){
				//if(actualQty<=minQuantity)return true;
				if(actualQty == 0)return true;
				else return false;
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
		
	}
	
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
		   Medical med = pMedicals.get(row);
		   double actualQty = med.getInitialqty()+med.getInqty()-med.getOutqty();
		   if (actualQty <= med.getMinqty()) cell.setForeground(Color.RED); //under critical level
		   if(((Boolean)table.getValueAt(row,6)).booleanValue()) cell.setForeground(Color.GRAY); //out of stock
	      return cell;
	   }
	}
}
