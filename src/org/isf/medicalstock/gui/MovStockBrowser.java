
package org.isf.medicalstock.gui;

/*------------------------------------------
 * MovStockBrowser - list medicals movement. let the user search for movements
 * 					  and insert a new movements
 * -----------------------------------------
 * modification history
 * 30/03/2006 - Theo - first beta version
 * 03/11/2006 - ross - changed title, removed delete all button
 *                   - corrected an error in datetextfield class (the month displayed in the filter was -1
 * 			         - version is now  1.0 
 *------------------------------------------*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.manager.MovBrowserManager;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medstockmovtype.manager.MedicaldsrstockmovTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medtype.manager.MedicalTypeBrowserManager;
import org.isf.medtype.model.MedicalType;
import org.isf.menu.gui.MainMenu;
import org.isf.stat.gui.report.GenericReportPharmaceuticalStockCard;
import org.isf.supplier.manager.SupplierBrowserManager;
import org.isf.supplier.model.Supplier;
import org.isf.utils.excel.ExcelExporter;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.DateTextField;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.StockCardDialog;
import org.isf.utils.jobjects.StockLedgerDialog;
import org.isf.utils.time.TimeTools;
import org.isf.ward.model.Ward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovStockBrowser extends ModalJFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(MovStockBrowser.class);

	private final JFrame myFrame;
	private JPanel contentPane;
	private JPanel buttonPanel;
	private JPanel tablePanel;
	private JButton closeButton;
	private JButton chargeButton;
	private JButton dischargeButton;
	private JButton filterButton;
	private JButton exportToExcel;
//	private JButton importFromExcel;
	private JButton stockCardButton;
	private JButton stockLedgerButton;
	private JPanel filterPanel;
	private JCheckBox jCheckBoxKeepFilter;
	private JComboBox medicalBox;
	private JComboBox medicalTypeBox;
	private JComboBox typeBox;
	private JComboBox wardBox;
	private DateTextField movDateFrom;
	private DateTextField movDateTo;
	private DateTextField lotPrepFrom;
	private DateTextField lotPrepTo;
	private DateTextField lotDueFrom;
	private DateTextField lotDueTo;
	private JTable movTable;
	private JTable jTableTotal;
	private int totalQti;
	private BigDecimal totalAmount;
	private MovBrowserModel model;
	private ArrayList<Movement> moves;
	private String[] pColums = {
			MessageBundle.getMessage("angal.medicalstock.refno"), 
			MessageBundle.getMessage("angal.common.datem"), 				//1 
			MessageBundle.getMessage("angal.medicalstock.typem"), 			//2
			MessageBundle.getMessage("angal.medicalstock.wardm"),			//3
			MessageBundle.getMessage("angal.medicalstock.qtym"), 			//4
			MessageBundle.getMessage("angal.medicalstock.pharmaceuticalm"),	//5
			MessageBundle.getMessage("angal.medicalstock.medtypem"),		//6
			MessageBundle.getMessage("angal.medicalstock.lotm"),			//7
			MessageBundle.getMessage("angal.medicalstock.prepdatem"),		//8
			MessageBundle.getMessage("angal.medicalstock.duedatem"),		//9
			MessageBundle.getMessage("angal.medicalstock.originm"),			//10
			MessageBundle.getMessage("angal.medicalstock.costm"),			//11
			MessageBundle.getMessage("angal.medicalstock.totalm")			//12
	};
	private boolean[] pColumnBold = {true,false,false,false,false,false,false,false,false,false,false,false,false};
	private int[] columnAlignment = {SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.CENTER,
			SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.RIGHT};
	private boolean[] pColumnVisible = {true,true,true,true,true,true,true,!GeneralData.AUTOMATICLOT,!GeneralData.AUTOMATICLOT,true,true,GeneralData.LOTWITHCOST,GeneralData.LOTWITHCOST};

	private int[] pColumwidth = {50, 80, 45, 130, 50, 150, 70, 70, 80, 65, 50, 50, 70};
	private static final String DATE_FORMAT_DD_MM_YY = "dd/MM/yy";
	private static final String DATE_FORMAT_DD_MM_YY_HH_MM = "dd/MM/yy HH:mm";
	
	private String currencyCod;
        
    /*
     *Adds to facilitate the selection of products 
     */
    private JPanel searchPanel;
    private JTextField searchTextField;
    private JButton searchButton;
	
	private SupplierBrowserManager supMan = new SupplierBrowserManager();
	private HashMap<Integer, String> supMap = new HashMap<Integer, String>();

	public MovStockBrowser() {
		myFrame = this;
		setTitle(MessageBundle.getMessage("angal.medicalstock.stockmovementbrowser"));
		try {
			supMap = supMan.getHashMap(true);
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		final int pfrmBase = 30;
		final int pfrmWidth = 24;
		final int pfrmHeight = 22;
		this.setBounds((screensize.width - screensize.width * pfrmWidth
				/ pfrmBase) / 2, (screensize.height - screensize.height
				* pfrmHeight / pfrmBase) / 2, screensize.width * pfrmWidth
				/ pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		setContentPane(getContentpane());
		
		//setResizable(false);
		updateTotals();
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private JPanel getContentpane() {
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(getFilterPanel(), BorderLayout.WEST);
		contentPane.add(getTablesPanel(), BorderLayout.CENTER);
		contentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		return contentPane;
	}
	
	/**
	 * this method controls if the automaticlot option is on
	 * 
	 * @return
	 */
	private boolean isAutomaticLot() {
		return GeneralData.AUTOMATICLOT;
	}
	private JPanel getButtonPanel() {
		buttonPanel = new JPanel();
		if (MainMenu.checkUserGrants("btnpharmstockcharge")) buttonPanel.add(getChargeButton());
		if (MainMenu.checkUserGrants("btnpharmstockdischarge")) buttonPanel.add(getDishargeButton());
		buttonPanel.add(getExportToExcelButton());
		buttonPanel.add(getStockCardButton());
		buttonPanel.add(getStockLedgerButton());
		buttonPanel.add(getCloseButton());
		return buttonPanel;
	}
	
	private JButton getStockCardButton() {
		stockCardButton = new JButton(MessageBundle.getMessage("angal.medicalstock.stockcard"));
		stockCardButton.setMnemonic(KeyEvent.VK_K);
		stockCardButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Medical medical = null;
				if (movTable.getSelectedRow()>-1) { 
					Movement movement = (Movement)(((MovBrowserModel) model).getValueAt(movTable.getSelectedRow(), -1));
					medical = movement.getMedical();
				}
				
				StockCardDialog stockCardDialog = new StockCardDialog(MovStockBrowser.this, 
						medical, 
						movDateFrom.getCompleteDate().getTime(), 
						movDateTo.getCompleteDate().getTime());
				medical = stockCardDialog.getMedical();
				Date dateFrom = stockCardDialog.getDateFrom();
				Date dateTo = stockCardDialog.getDateTo();
				boolean toExcel = stockCardDialog.isExcel();
				
				if (!stockCardDialog.isCancel()) {
					if (medical == null) {
						JOptionPane.showMessageDialog(MovStockBrowser.this,
								MessageBundle.getMessage("angal.medicalstock.chooseamedical"));
						return;
					}
					new GenericReportPharmaceuticalStockCard("ProductLedger", dateFrom, dateTo, medical, null, toExcel);
					return;
				}
			}
		});
		return stockCardButton;
	}
	
	private JButton getStockLedgerButton() {
		stockLedgerButton = new JButton(MessageBundle.getMessage("StockLedger"));
		stockLedgerButton.setMnemonic(KeyEvent.VK_L);
		stockLedgerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				StockLedgerDialog stockCardDialog = new StockLedgerDialog(MovStockBrowser.this, movDateFrom.getCompleteDate().getTime(), movDateTo.getCompleteDate().getTime());
				Date dateFrom = stockCardDialog.getDateFrom();
				Date dateTo = stockCardDialog.getDateTo();
				
				if (!stockCardDialog.isCancel()) {
					new GenericReportPharmaceuticalStockCard("ProductLedger_multi", dateFrom, dateTo, null, null, false);
					return;
				}
			}
		});
		return stockLedgerButton;
	}

	private JPanel getTablesPanel() {
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(getTable(), BorderLayout.CENTER);
		tablePanel.add(getTableTotal(), BorderLayout.SOUTH);
		return tablePanel;
	}
	
	private JScrollPane getTable() {
		JScrollPane scrollPane = new JScrollPane(getMovTable());
		int totWidth = 0;
		for (int colWidth : pColumwidth) {
			totWidth += colWidth;
		}
		scrollPane.setPreferredSize(new Dimension(totWidth, 450));
		return scrollPane;
	}
	
	private JScrollPane getTableTotal() {
		JScrollPane scrollPane = new JScrollPane(getJTableTotal());
		int totWidth = 0;
		for (int colWidth : pColumwidth) {
			totWidth += colWidth;
		}
		scrollPane.setPreferredSize(new Dimension(totWidth, 20));
		scrollPane.setColumnHeaderView(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		return scrollPane;
	}
	
	public void updateTotals() {
		if (jTableTotal == null) return;
		totalQti = 0;
		totalAmount = new BigDecimal(0);
		
		// quantity
		if (!medicalBox.getSelectedItem().equals(MessageBundle.getMessage("angal.medicalstock.all"))) {
			for (Movement mov : moves) {
				if (mov.getType().getType().contains("+")) {
					totalQti += mov.getQuantity();
				} else {
					totalQti -= mov.getQuantity();
				}
			}
			jTableTotal.getModel().setValueAt(totalQti, 0, 4);
		} else {
			jTableTotal.getModel().setValueAt(MessageBundle.getMessage("angal.common.notapplicable"), 0, 4);
		}
		
		// amount
		for (Movement mov : moves) {
			BigDecimal itemAmount = new BigDecimal(Double.toString(mov.getQuantity()));
			if (mov.getType().getType().contains("+")) {
				totalAmount = totalAmount.add(itemAmount.multiply(new BigDecimal(mov.getLot().getCost())));
			} else {
				totalQti -= mov.getQuantity();
				totalAmount = totalAmount.subtract(itemAmount.multiply(new BigDecimal(mov.getLot().getCost())));
			}
		}
		jTableTotal.getModel().setValueAt(totalAmount, 0, 12);
	}

	private JPanel getFilterPanel() {
		if (filterPanel == null) {
			filterPanel = new JPanel();
			filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
					.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.selectionpanel")));
			filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
			filterPanel.add(getMedicalPanel());
			filterPanel.add(getMovementPanel());
			if (!isAutomaticLot()) {
				filterPanel.add(getLotPreparationDatePanel());
			}
			filterPanel.add(getLotDueDatePanel());
			JPanel filterButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			filterButtonPanel.add(getFilterButton());
			filterButtonPanel.add(getJCheckBoxKeepFilter());
			filterPanel.add(filterButtonPanel);
		}
		return filterPanel;
	}

	private JCheckBox getJCheckBoxKeepFilter() {
		if (jCheckBoxKeepFilter == null) {
			jCheckBoxKeepFilter = new JCheckBox(MessageBundle.getMessage("angal.medicalstock.keep"));
		}
		return jCheckBoxKeepFilter;
	}

	private JPanel getMedicalPanel() {
		JPanel medicalPanel = new JPanel();
		medicalPanel.setLayout(new BoxLayout(medicalPanel, BoxLayout.Y_AXIS));
		medicalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.pharmaceutical")));
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.common.description")));
		medicalPanel.add(label1Panel);
                medicalPanel.add(getMedicalSearchPanel());
		JPanel medicalDescPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		medicalDescPanel.add(getMedicalBox());
		medicalPanel.add(medicalDescPanel);
		JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.type")));
		medicalPanel.add(label2Panel);
		JPanel medicalTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		medicalTypePanel.add(getMedicalTypeBox());
		medicalPanel.add(medicalTypePanel);
		return medicalPanel;
	}

	private JPanel getMovementPanel() {
		JPanel movementPanel = new JPanel();
		movementPanel.setLayout(new BoxLayout(movementPanel, BoxLayout.Y_AXIS));
		movementPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.movement")));
		JPanel label3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label3Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.type")));
		movementPanel.add(label3Panel);
		JPanel movementTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		movementTypePanel.add(getMovementTypeBox());
		movementPanel.add(movementTypePanel);

		JPanel label2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label2Panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.ward")));
		movementPanel.add(label2Panel);
		JPanel wardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		wardPanel.add(getWardBox());
		movementPanel.add(wardPanel);

		JPanel label4Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label4Panel.add(new JLabel(MessageBundle.getMessage("angal.common.date")));
		movementPanel.add(label4Panel);

		JPanel moveFromPanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(MessageBundle.getMessage("angal.common.from"));
		label.setVerticalAlignment(SwingConstants.TOP);
		moveFromPanel.add(label, BorderLayout.WEST);
		moveFromPanel.add(getMovDateFrom(), BorderLayout.EAST);
		movementPanel.add(moveFromPanel);
		JPanel moveToPanel = new JPanel(new BorderLayout());
		JLabel label_1 = new JLabel(MessageBundle.getMessage("angal.common.to"));
		label_1.setVerticalAlignment(SwingConstants.TOP);
		moveToPanel.add(label_1, BorderLayout.WEST);
		moveToPanel.add(getMovDateTo(), BorderLayout.EAST);
		movementPanel.add(moveToPanel);
		return movementPanel;
	}

	private JPanel getLotPreparationDatePanel() {
		JPanel lotPreparationDatePanel = new JPanel();
		lotPreparationDatePanel.setLayout(new BoxLayout(
				lotPreparationDatePanel, BoxLayout.Y_AXIS));
		lotPreparationDatePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				MessageBundle.getMessage("angal.medicalstock.lotpreparationdate")));

		JPanel lotPrepFromPanel = new JPanel(new BorderLayout());
		lotPrepFromPanel.add(new JLabel(MessageBundle.getMessage("angal.common.from")), BorderLayout.WEST);
		lotPrepFromPanel.add(getLotPrepFrom(), BorderLayout.EAST);
		lotPreparationDatePanel.add(lotPrepFromPanel);
		JPanel lotPrepToPanel = new JPanel(new BorderLayout());
		lotPrepToPanel.add(new JLabel(MessageBundle.getMessage("angal.common.to")), BorderLayout.WEST);
		lotPrepToPanel.add(getLotPrepTo(), BorderLayout.EAST);
		lotPreparationDatePanel.add(lotPrepToPanel);

		return lotPreparationDatePanel;
	}

	private JPanel getLotDueDatePanel() {
		JPanel lotDueDatePanel = new JPanel();
		lotDueDatePanel.setLayout(new BoxLayout(lotDueDatePanel,
				BoxLayout.Y_AXIS));
		lotDueDatePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY), MessageBundle.getMessage("angal.medicalstock.lotduedate")));

		JPanel lotDueFromPanel = new JPanel(new BorderLayout());
		lotDueFromPanel.add(new JLabel(MessageBundle.getMessage("angal.common.from")), BorderLayout.WEST);
		lotDueFromPanel.add(getLotDueFrom(), BorderLayout.EAST);
		lotDueDatePanel.add(lotDueFromPanel);
		JPanel lotDueToPanel = new JPanel(new BorderLayout());
		lotDueToPanel.add(new JLabel(MessageBundle.getMessage("angal.common.to")), BorderLayout.WEST);
		lotDueToPanel.add(getLotDueTo(), BorderLayout.EAST);
		lotDueDatePanel.add(lotDueToPanel);

		return lotDueDatePanel;
	}

	private JComboBox getWardBox() {
		org.isf.ward.manager.WardBrowserManager wbm = new org.isf.ward.manager.WardBrowserManager();
		wardBox = new JComboBox();
		wardBox.setPreferredSize(new Dimension(130,25));
		wardBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		ArrayList<Ward> wardList;
		try {
			wardList = wbm.getWards();
		}catch(OHServiceException e){
			wardList = new ArrayList<Ward>();
			OHServiceExceptionUtil.showMessages(e);
		}
		for (org.isf.ward.model.Ward elem : wardList) {
			wardBox.addItem(elem);
		}
		wardBox.setEnabled(false);
		return wardBox;
	}

        private JPanel getMedicalSearchPanel() {
            searchButton = new JButton();
            searchButton.setPreferredSize(new Dimension(20, 20));
            searchButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    medicalBox.removeAllItems();
                    MedicalBrowsingManager medicalManager = new MedicalBrowsingManager();
                    ArrayList<Medical> medicals;
                    try {
                            medicals = medicalManager.getMedicals();
                    } catch (OHServiceException e1) {
                            medicals = null;
                            OHServiceExceptionUtil.showMessages(e1);
                    }
                    if (null != medicals) {
                        ArrayList<Medical> results = getSearchMedicalsResults(searchTextField.getText(), medicals);
                        int originalSize = medicals.size();
                        int resultsSize = results.size();
                        if(originalSize == resultsSize) {
                            medicalBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
                        }
                        for (Medical aMedical: results) {
                            medicalBox.addItem(aMedical);
                        }
                    }
                }
            });
            
            searchTextField = new JTextField(10);
            searchTextField.setToolTipText(MessageBundle.getMessage("angal.medicalstock.pharmaceutical"));
            searchTextField.addKeyListener(new KeyListener() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        searchButton.doClick();
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {}
                @Override
                public void keyTyped(KeyEvent e) {}
            });
            
            searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            searchPanel.add(searchTextField);
            searchPanel.add(searchButton);
            searchPanel.setMaximumSize(new Dimension(150, 25));
            searchPanel.setMinimumSize(new Dimension(150, 25));
            searchPanel.setPreferredSize(new Dimension(150, 25));
            return searchPanel;
        }
	private JComboBox getMedicalBox() {
		medicalBox = new JComboBox();
		medicalBox.setMaximumSize(new Dimension(150, 25));
		medicalBox.setMinimumSize(new Dimension(150, 25));
		medicalBox.setPreferredSize(new Dimension(150, 25));
		MedicalBrowsingManager medicalManager = new MedicalBrowsingManager();
		ArrayList<Medical> medical;
		try {
			medical = medicalManager.getMedicals();
		} catch (OHServiceException e1) {
			medical = null;
			OHServiceExceptionUtil.showMessages(e1);
		}
		medicalBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		if (null != medical) {
			for (Medical aMedical : medical) {
				medicalBox.addItem(aMedical);
			}
		}
		medicalBox.addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				medicalBox.setEnabled(true);
				medicalTypeBox.setSelectedIndex(0);
				medicalTypeBox.setEnabled(false);
			}
		});
		return medicalBox;

	}

	private JComboBox getMedicalTypeBox() {
		medicalTypeBox = new JComboBox();
		medicalTypeBox.setPreferredSize(new Dimension(130,25));
		MedicalTypeBrowserManager medicalManager = new MedicalTypeBrowserManager();
		ArrayList<MedicalType> medical;
		
		medicalTypeBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		
		try {
			medical = medicalManager.getMedicalType();
			
			for (MedicalType aMedicalType : medical) {
				medicalTypeBox.addItem(aMedicalType);
			}
		} catch (OHServiceException e1) {
			OHServiceExceptionUtil.showMessages(e1);
		}
		
		medicalTypeBox.addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				medicalTypeBox.setEnabled(true);
				medicalBox.setSelectedIndex(0);
				medicalBox.setEnabled(false);
			}
		});
		medicalTypeBox.setEnabled(false);
		return medicalTypeBox;
	}

	private JComboBox getMovementTypeBox() {
		typeBox = new JComboBox();
		typeBox.setPreferredSize(new Dimension(130,25));
		MedicaldsrstockmovTypeBrowserManager typeManager = new MedicaldsrstockmovTypeBrowserManager();
		ArrayList<MovementType> type;
		try {
			type = typeManager.getMedicaldsrstockmovType();
		} catch (OHServiceException e1) {
			type = null;
			OHServiceExceptionUtil.showMessages(e1);
		}
		typeBox.addItem(MessageBundle.getMessage("angal.medicalstock.all"));
		if (null != type) {
			for (MovementType movementType : type) {
				typeBox.addItem(movementType);
			}
		}
		typeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(typeBox.getSelectedItem() instanceof String)) {
					MovementType selected = (MovementType) typeBox
							.getSelectedItem();
					if (selected.getType().contains("-")) {
						wardBox.setEnabled(true);
					} else {
						wardBox.setSelectedIndex(0);
						wardBox.setEnabled(false);
					}
				} else {
					wardBox.setSelectedIndex(0);
					wardBox.setEnabled(false);
				}
			}
		});
		return typeBox;
	}

	private JTable getMovTable() {
		GregorianCalendar now=new GregorianCalendar();
		GregorianCalendar old=new GregorianCalendar();
		old.add(GregorianCalendar.WEEK_OF_YEAR,-1);
		
		model = new MovBrowserModel(null,null,null,null,old,now,null,null,null,null);
		movTable = new JTable(model);
		
		for (int i = 0; i < pColums.length; i++) {
			movTable.getColumnModel().getColumn(i).setCellRenderer(new EnabledTableCellRenderer());
			movTable.getColumnModel().getColumn(i).setPreferredWidth(pColumwidth[i]);
//			if (!pColumnResizable[i]) {
//				movTable.getColumnModel().getColumn(i).setResizable(pColumnResizable[i]);
//				movTable.getColumnModel().getColumn(i).setMaxWidth(pColumwidth[i]);
//			}
			if (!pColumnVisible[i]) {
				movTable.getColumnModel().getColumn(i).setMinWidth(0);
				movTable.getColumnModel().getColumn(i).setMaxWidth(0);
				movTable.getColumnModel().getColumn(i).setWidth(0);
			}
		}
		
		TableColumn costColumn = movTable.getColumnModel().getColumn(11);
		costColumn.setCellRenderer(new DecimalFormatRenderer());
		
		TableColumn totalColumn = movTable.getColumnModel().getColumn(12);
		totalColumn.setCellRenderer(new DecimalFormatRenderer());
		
		return movTable;
	}
	
	private JTable getJTableTotal() {
		if (jTableTotal == null) {
			jTableTotal = new JTable();
			
			HospitalBrowsingManager hospitalManager = new HospitalBrowsingManager();
			String currencyCod;
			try {
				currencyCod = hospitalManager.getHospitalCurrencyCod();
			} catch (OHServiceException e) {
				currencyCod = null;
				OHServiceExceptionUtil.showMessages(e);
			}
			
			jTableTotal.setModel(new DefaultTableModel(
					new Object[][] {
							{"", "", "", "<html><b>Total Qty: </b></html>", totalQti, "", "", "", "", "", "<html><b>Total: </b></html>", currencyCod, totalAmount}
							}, new String[pColums.length]) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				private static final long serialVersionUID = 1L;
	
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			jTableTotal.setTableHeader(null);
			jTableTotal.setShowVerticalLines(false);
			jTableTotal.setShowHorizontalLines(false);
			jTableTotal.setRowSelectionAllowed(false);
			jTableTotal.setCellSelectionEnabled(false);
			jTableTotal.setColumnSelectionAllowed(false);
			
			for (int i = 0; i < pColums.length; i++) {
				jTableTotal.getColumnModel().getColumn(i).setCellRenderer(new EnabledTableCellRenderer());
				jTableTotal.getColumnModel().getColumn(i).setPreferredWidth(pColumwidth[i]);
				if (!pColumnVisible[i]) {
					jTableTotal.getColumnModel().getColumn(i).setMinWidth(0);
					jTableTotal.getColumnModel().getColumn(i).setMaxWidth(0);
					jTableTotal.getColumnModel().getColumn(i).setWidth(0);
				}
			}
			
			jTableTotal.getColumnModel().getColumn(3).setCellRenderer(new RightAlignCellRenderer());
			TableColumn totalColumn = jTableTotal.getColumnModel().getColumn(4);
			totalColumn.setCellRenderer(new DecimalFormatRenderer());
			
			jTableTotal.getColumnModel().getColumn(11).setCellRenderer(new RightAlignCellRenderer());
			TableColumn totalAmountColumn = jTableTotal.getColumnModel().getColumn(12);
			totalAmountColumn.setCellRenderer(new DecimalFormatRenderer());
		}
		return jTableTotal;
	}

	private DateTextField getMovDateFrom() {
		GregorianCalendar time = new GregorianCalendar();
		//time.roll(GregorianCalendar.WEEK_OF_YEAR, false);
		time.add(GregorianCalendar.WEEK_OF_YEAR, -1);
		movDateFrom = new DateTextField(time);
		return movDateFrom;
	}

	private DateTextField getMovDateTo() {
		movDateTo = new DateTextField(new GregorianCalendar());
		return movDateTo;
	}

	private DateTextField getLotPrepFrom() {
		lotPrepFrom = new DateTextField();
		return lotPrepFrom;
	}

	private DateTextField getLotPrepTo() {
		lotPrepTo = new DateTextField();
		return lotPrepTo;
	}

	private DateTextField getLotDueFrom() {
		lotDueFrom = new DateTextField();
		return lotDueFrom;
	}

	private DateTextField getLotDueTo() {
		lotDueTo = new DateTextField();
		return lotDueTo;
	}

	/**
	 * this method creates the button that filters the data
	 * 
	 * @return
	 */

	private JButton getFilterButton() {
		filterButton = new JButton(MessageBundle.getMessage("angal.medicalstock.filter"));
		filterButton.setMnemonic(KeyEvent.VK_F);
		filterButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Integer medicalSelected = null;
				String medicalTypeSelected = null;
				String typeSelected = null;
				String wardSelected = null;
				boolean dateOk = true;
				
				GregorianCalendar movFrom=movDateFrom.getCompleteDate();
				GregorianCalendar movTo=movDateTo.getCompleteDate();
				if((movFrom==null)||(movTo==null)){
					if(!((movFrom==null)&&(movTo==null))){
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidmovementdate"));
						dateOk=false;
					}
				}else if(movFrom.compareTo(
						movTo) > 0) {
						JOptionPane
								.showMessageDialog(null,
										MessageBundle.getMessage("angal.medicalstock.movementdatefromcannotbelaterthanmovementdateto"));
						dateOk = false;
					}
					
				if (!isAutomaticLot()) {
					GregorianCalendar prepFrom=lotPrepFrom.getCompleteDate();
					GregorianCalendar prepTo=lotPrepTo.getCompleteDate();
					if((prepFrom==null)||(prepTo==null)){
						if(!((prepFrom==null)&&(prepTo==null))){
							JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidpreparationdate"));
							dateOk=false;
						}
					}else if (prepFrom.compareTo(
							prepTo) > 0) {
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.medicalstock.preparationdatefromcannotbelaterpreparationdateto"));
							dateOk = false;
					}
				}
				
				GregorianCalendar dueFrom=lotDueFrom.getCompleteDate();
				GregorianCalendar dueTo=lotDueTo.getCompleteDate();
				if((dueFrom==null)||(dueTo==null)){
					if(!((dueFrom==null)&&(dueTo==null))){
						JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.medicalstock.chooseavalidduedate"));
						dateOk=false;
					}
				}else if (dueFrom.compareTo(
						dueTo) > 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.medicalstock.duedatefromcannotbelaterthanduedateto"));
						dateOk = false;
				}
				
				if (dateOk) {
					if (medicalBox.isEnabled()) {
						if (!(medicalBox.getSelectedItem() instanceof String)) {
							medicalSelected = ((Medical) medicalBox
									.getSelectedItem()).getCode();
						}
					} else {
						if (!(medicalTypeBox.getSelectedItem() instanceof String)) {
							medicalTypeSelected = ((MedicalType) medicalTypeBox
									.getSelectedItem()).getCode();
						}
					}
					if (!(typeBox.getSelectedItem() instanceof String)) {
						typeSelected = ((MovementType) typeBox
								.getSelectedItem()).getCode();
					}
					if (!(wardBox.getSelectedItem() instanceof String)) {
						wardSelected = ((Ward) wardBox.getSelectedItem())
								.getCode();
					}
					if (!isAutomaticLot()) {
						model = new MovBrowserModel(medicalSelected,
								medicalTypeSelected, wardSelected, typeSelected,
								movDateFrom.getCompleteDate(), 
								movDateTo.getCompleteDate(), 
								lotPrepFrom.getCompleteDate(), 
								lotPrepTo.getCompleteDate(), 
								lotDueFrom.getCompleteDate(), 
								lotDueTo.getCompleteDate());
					} else {
						model = new MovBrowserModel(medicalSelected,
								medicalTypeSelected, wardSelected, typeSelected,
								movDateFrom.getCompleteDate(), 
								movDateTo.getCompleteDate(), 
								null, 
								null, 
								lotDueFrom.getCompleteDate(), 
								lotDueTo.getCompleteDate());
					}
					
					if (moves != null) {
						model.fireTableDataChanged();
						movTable.updateUI();
					}
					updateTotals();
				}
			}

		});
		return filterButton;
	}

	/**
	 * this method creates the button that close the mask
	 * 
	 * @return
	 */
	private JButton getCloseButton() {
		closeButton = new JButton(MessageBundle.getMessage("angal.common.close"));
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		return closeButton;
	}

	/**
	 * this method creates the button that deletes all the records in
	 * medicaldsrstockmov and lot it is for training pourposes only to be
	 * deleted in production environment
	 * 
	 * @return
	 */
	/*private JButton getdeleteAllButton() {
		deleteAllButton = new JButton(MessageBundle.getMessage("angal.medicalstock.deleteall"));
		deleteAllButton.setMnemonic(KeyEvent.VK_D);
		deleteAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null,
						MessageBundle.getMessage("angal.medicalstock.reallywanttodeleteallstockmovementsandlot"),
						MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);
				if ((n == JOptionPane.YES_OPTION)) {
					try {
						DbQuery query = new DbQuery();
						String s = "delete from MEDICALDSRSTOCKMOV";
						query.setData(s, true);
						s = "delete from MEDICALDSRLOT;";
						query.setData(s, true);
					} catch (IOException err) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.sql.problemsoccurredwithserverconnection"));
						err.printStackTrace();
					} catch (SQLException err) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.medicalstock.problemsoccurredwithsqlistruction"));
						err.printStackTrace();
					}
				}
				filterButton.doClick();
			}
		});
		return deleteAllButton;
	}*/

//	/**
//	 * this method creates the button that load the insert movement mask
//	 * 
//	 * @return
//	 */
//	private JButton getInsertButton() {
//		insertButton = new JButton(MessageBundle.getMessage("angal.medicalstock.insert"));
//		insertButton.setMnemonic(KeyEvent.VK_I);
//		insertButton.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				new MovStockInserting(myFrame);
//				model = new MovBrowserModel();
//				//model.fireTableDataChanged();
//				movTable.updateUI();
//				if (jCheckBoxKeepFilter.isSelected()) filterButton.doClick();
//			}
//		});
//		return insertButton;
//	}
	
	/**
	 * this method creates the button that load the charging movement mask
	 * 
	 * @return
	 */
	private JButton getChargeButton() {
		chargeButton = new JButton(MessageBundle.getMessage("angal.medicalstock.charge"));
		chargeButton.setMnemonic(KeyEvent.VK_C);
		chargeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new MovStockMultipleCharging(myFrame);
				model = new MovBrowserModel();
				//model.fireTableDataChanged();
				movTable.updateUI();
				updateTotals();
				if (jCheckBoxKeepFilter.isSelected()) filterButton.doClick();
			}
		});
		return chargeButton;
	}
	
	/**
	 * this method creates the button that load the discharging movement mask
	 * 
	 * @return
	 */
	private JButton getDishargeButton() {
		dischargeButton = new JButton(MessageBundle.getMessage("angal.medicalstock.discharge"));
		dischargeButton.setMnemonic(KeyEvent.VK_D);
		dischargeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new MovStockMultipleDischarging(myFrame);
				model = new MovBrowserModel();
				//model.fireTableDataChanged();
				movTable.updateUI();
				updateTotals();
				if (jCheckBoxKeepFilter.isSelected()) filterButton.doClick();
			}
		});
		return dischargeButton;
	}
	
//	private JButton getImportFromExcelButton()
//	{
//		importFromExcel = new JButton(MessageBundle.getMessage("angal.medicalstock.import"));
//		importFromExcel.setMnemonic(KeyEvent.VK_I);
//		importFromExcel.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e) {
//				
//				JFileChooser fcExcel = new JFileChooser();
//				FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("Excel (*.xls)","xls");
//				fcExcel.setFileFilter(excelFilter);
//				
//				int iRetVal = fcExcel.showOpenDialog(MovStockBrowser.this);
//				if(iRetVal == JFileChooser.APPROVE_OPTION)
//				{
//					ExcelImporter xlsImport = new ExcelImporter();
//					try
//					{
//						xlsImport.OpenInTable(movTable, fcExcel.getSelectedFile());
//						movTable.updateUI();
//					} catch(IOException exc)
//					{
//						System.out.println("Import to excel error : "+exc.getMessage());
//					}
//				}
//			}
//		});
//		return importFromExcel;
//	}
	
	private JButton getExportToExcelButton()
	{
		exportToExcel = new JButton(MessageBundle.getMessage("angal.medicalstock.export"));
		exportToExcel.setMnemonic(KeyEvent.VK_E);
		exportToExcel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {

				String fileName = compileFileName();
				File defaultFileName = new File(fileName);
				JFileChooser fcExcel = ExcelExporter.getJFileChooserExcel(defaultFileName);
				
				int iRetVal = fcExcel.showSaveDialog(MovStockBrowser.this);
				if(iRetVal == JFileChooser.APPROVE_OPTION)
				{
					File exportFile = fcExcel.getSelectedFile();
					if (!exportFile.getName().endsWith("xls")) exportFile = new File(exportFile.getAbsoluteFile() + ".xls");
					ExcelExporter xlsExport = new ExcelExporter();
					try
					{
						xlsExport.exportTableToExcel(movTable, exportFile);
					} catch(IOException exc)
					{
						JOptionPane.showMessageDialog(MovStockBrowser.this,
								exc.getMessage(),
		                        MessageBundle.getMessage("angal.hospital"),
		                        JOptionPane.PLAIN_MESSAGE);	
						logger.info("Export to excel error : "+ exc.getMessage());
					}
				}
			}
		});
		return exportToExcel;
	}


	private String compileFileName() {
		StringBuilder filename = new StringBuilder("Stock Ledger");
		if (medicalBox.isEnabled() 
				&& !medicalBox.getSelectedItem().equals(
						MessageBundle.getMessage("angal.medicalstock.all"))) {
			
			filename.append("_").append(medicalBox.getSelectedItem());
		}
		if (medicalTypeBox.isEnabled()
				&& !medicalTypeBox.getSelectedItem().equals(
						MessageBundle.getMessage("angal.medicalstock.all"))) {
			
			filename.append("_").append(medicalTypeBox.getSelectedItem());
		}
		if (typeBox.isEnabled() &&
				!typeBox.getSelectedItem().equals(
						MessageBundle.getMessage("angal.medicalstock.all"))) {
			
			filename.append("_").append(typeBox.getSelectedItem());
		}
		if (wardBox.isEnabled() &&
				!wardBox.getSelectedItem().equals(
						MessageBundle.getMessage("angal.medicalstock.all"))) {
			
			filename.append("_").append(wardBox.getSelectedItem());
		}
		filename.append("_").append(TimeTools.formatDateTime(movDateFrom.getCompleteDate(), "yyyyMMdd"))
				.append("_").append(TimeTools.formatDateTime(movDateTo.getCompleteDate(), "yyyyMMdd"));
		return filename.toString();
	}

        private ArrayList<Medical> getSearchMedicalsResults(String s, ArrayList<Medical> medicalsList) {
            String query = s.trim();
            ArrayList<Medical> results = new ArrayList<Medical>();
            for (Medical medoc : medicalsList) {
                if(!query.equals("")) {
                    String[] patterns = query.split(" ");
                    String code = medoc.getProd_code().toLowerCase();
                    String description = medoc.getDescription().toLowerCase();
                    boolean patternFound = false;
                    for (String pattern : patterns) {
                        if (code.contains(pattern.toLowerCase()) || description.contains(pattern.toLowerCase())) {
                            patternFound = true;
                            //It is sufficient that only one pattern matches the query
                            break;
                        }
                    }
                    if (patternFound){
                        results.add(medoc);
                    }
                } else {
                    results.add(medoc);
                }
            }		
            return results;
        }    

	/**
	 * This is the table model
	 * 
	 */
	class MovBrowserModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MovBrowserModel() {
			GregorianCalendar now=new GregorianCalendar();
			GregorianCalendar old=new GregorianCalendar();
			old.add(GregorianCalendar.WEEK_OF_YEAR,-1);
			
			new MovBrowserModel(null,null,null,null,old,now,null,null,null,null);
			updateTotals();
		}

		public MovBrowserModel(Integer medicalCode, String medicalType,
				String ward, String movType, GregorianCalendar movFrom,
				GregorianCalendar movTo, GregorianCalendar lotPrepFrom,
				GregorianCalendar lotPrepTo, GregorianCalendar lotDueFrom,
				GregorianCalendar lotDueTo) {
			MovBrowserManager manager = new MovBrowserManager();
			try {
				moves = manager.getMovements(medicalCode, medicalType, ward,
						movType, movFrom, movTo, lotPrepFrom, lotPrepTo,
						lotDueFrom, lotDueTo);
			} catch (OHServiceException e) {
				OHServiceExceptionUtil.showMessages(e);
			}
			updateTotals();
		}

		public int getRowCount() {
			if (moves == null)
				return 0;
			return moves.size();
		}

		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		/**
		 * Note: We must get the objects in a reversed way because of the query
		 * 
		 * @see org.isf.medicalstock.service.MedicalStockIoOperations
		 */
		public Object getValueAt(int r, int c) {
			Movement movement = moves.get(r);
			Lot lot = movement.getLot();
			Double cost = lot.getCost();
			int qty = movement.getQuantity();
			int col = -1;
			if (c == col) {
				return movement;
			} else if (c == ++col) {
				return movement.getRefNo();
			} else if (c == ++col) {
				return formatDateTime(movement.getDate());
			} else if (c == ++col) {
				return movement.getType().toString();
			} else if (c == ++col) {
				Ward ward = movement.getWard();
				if (ward != null)
					return ward;
				else
					return "";
			} else if (c == ++col) {
				return qty;
			} else if (c == ++col) {
				return movement.getMedical().getDescription();
			} else if (c == ++col) {
				return movement.getMedical().getType().getDescription();
			} else if (c == ++col) {
				if(isAutomaticLot())
					return MessageBundle.getMessage("angal.medicalstock.generated");
				else return lot;
			} else if (c == ++col) {
				return formatDate(lot.getPreparationDate());
			} else if (c == ++col) {
				return formatDate(lot.getDueDate());
			} else if (c == ++col){
				Supplier origin = movement.getOrigin();
				return origin != null ? supMap.get(new Integer(origin.getSupId())) : "";
			} else if (c == ++col){
				return cost;
			} else if (c == ++col){
				return cost * qty;
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}

	private String formatDate(GregorianCalendar time) {
		if (time == null)
			return MessageBundle.getMessage("angal.medicalstock.nodate");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DD_MM_YY);
		return sdf.format(time.getTime());
	}
	
	private String formatDateTime(GregorianCalendar time) {
		if (time == null)
			return MessageBundle.getMessage("angal.medicalstock.nodate");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DD_MM_YY_HH_MM);
		return sdf.format(time.getTime());
	}
	
	class EnabledTableCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setHorizontalAlignment(columnAlignment[column]);
			if (pColumnBold[column]) { 
				cell.setFont(new Font(null, Font.BOLD, 12));
			}
			return cell;
		}
	}
	
	class RightAlignCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setHorizontalAlignment(SwingConstants.RIGHT);
			return cell;
		}
	}
	
	class DecimalFormatRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final DecimalFormat formatter100 = new DecimalFormat("#,##0.000");
		private final DecimalFormat formatter10 = new DecimalFormat("#,##0.00");
		private final DecimalFormat formatter1 = new DecimalFormat("#,##0");

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setHorizontalAlignment(columnAlignment[column]);
			if (column == 4 && value instanceof Number) value = formatter1.format((Number) value);
			if (column == 11 && value instanceof Number) value = formatter100.format((Number) value);
			if (column == 12 && value instanceof Number) value = formatter10.format((Number) value);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
}
