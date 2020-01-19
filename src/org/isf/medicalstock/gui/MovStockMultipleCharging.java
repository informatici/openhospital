package org.isf.medicalstock.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.PropertyConfigurator;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.medicals.manager.MedicalBrowsingManager;
import org.isf.medicals.model.Medical;
import org.isf.medicalstock.manager.MovStockInsertingManager;
import org.isf.medicalstock.model.Lot;
import org.isf.medicalstock.model.Movement;
import org.isf.medstockmovtype.manager.MedicaldsrstockmovTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.menu.manager.Context;
import org.isf.supplier.manager.SupplierBrowserManager;
import org.isf.supplier.model.Supplier;
import org.isf.utils.db.NormalizeString;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.RequestFocusListener;
import org.isf.utils.jobjects.TextPrompt;
import org.isf.utils.jobjects.TextPrompt.Show;
import org.isf.utils.time.TimeTools;

import com.toedter.calendar.JDateChooser;

public class MovStockMultipleCharging extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss"; //$NON-NLS-1$
	private static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy"; //$NON-NLS-1$
	private static final int CODE_COLUMN_WIDTH = 100;
	
	private JPanel mainPanel;
	private JTextField jTextFieldReference;
	private JTextField jTextFieldSearch;
	private JComboBox jComboBoxChargeType;
	private JDateChooser jDateChooser;
	private JComboBox jComboBoxSupplier;
	private JTable jTableMovements;
	private final String[] columnNames = { 
		MessageBundle.getMessage("angal.common.codem"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.common.descriptionm"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.qtypacket"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.qty"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.unitpack"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.total"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.lotnumberabb"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdate"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.cost"), //$NON-NLS-1$
		MessageBundle.getMessage("angal.medicalstock.multiplecharging.total") }; //$NON-NLS-1$
	private final Class[] columnClasses = { String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class, String.class, Double.class, Double.class };
	private boolean[] columnEditable = { true, false, false, true, true, false, !GeneralData.AUTOMATICLOT, true, true, false };
	private int[] columnWidth = { 50, 100, 70, 50, 70, 50, 50, 80, 50, 80 };
	private boolean[] columnResizable = { false, true, false, false, false, false, false, false, false, false };
	private boolean[] columnVisible = { true, true, true, true, true, true, !GeneralData.AUTOMATICLOT, true, GeneralData.LOTWITHCOST, GeneralData.LOTWITHCOST };
	private int[] columnAlignment = { SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER,
			SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.RIGHT };
	private boolean[] columnBold = { false, false, false, false, false, true, false, false, false, true };
	private HashMap<String, Medical> medicalMap;
	private ArrayList<Integer> units;
	private JTableModel model;
	private String[] qtyOption = new String[] {
			MessageBundle.getMessage("angal.medicalstock.multiplecharging.units"), //$NON-NLS-2$
			MessageBundle.getMessage("angal.medicalstock.multiplecharging.packets") //$NON-NLS-2$
	};
	private JComboBox comboBoxUnits = new JComboBox(qtyOption);
	private final int UNITS = 0;
	private final int PACKETS = 1;
	private int optionSelected = UNITS;
	
	private MovStockInsertingManager movManager = Context.getApplicationContext().getBean(MovStockInsertingManager.class);
	private MedicalBrowsingManager medicalBrowsingManager = Context.getApplicationContext().getBean(MedicalBrowsingManager.class);
	private MedicaldsrstockmovTypeBrowserManager medicaldsrstockmovTypeBrowserManager = Context.getApplicationContext().getBean(MedicaldsrstockmovTypeBrowserManager.class);
	private SupplierBrowserManager supplierBrowserManager = Context.getApplicationContext().getBean(SupplierBrowserManager.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure(new File("./rsc/log4j.properties").getAbsolutePath()); //$NON-NLS-1$
			GeneralData.getGeneralData();
			new MovStockMultipleCharging(new JFrame());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isAutomaticLot() {
		return GeneralData.AUTOMATICLOT;
	}

	/**
	 * Create the dialog.
	 */
	public MovStockMultipleCharging(JFrame owner) {
		super(owner, true);
		initialize();
		initcomponents();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void initialize() {
		ArrayList<Medical> medicals;
		try {
			medicals = medicalBrowsingManager.getMedicals();
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
			medicals = null;
		}

		medicalMap = new HashMap<String, Medical>();
		if (null != medicals) {
			for (Medical med : medicals) {
				String key = med.getProd_code();
				if (key == null || key.equals("")) key = med.getType().getCode() + med.getDescription();
				medicalMap.put(key, med);
			}
		}
		units = new ArrayList<Integer>();
	}
	
	private void initcomponents() {
		setTitle(MessageBundle.getMessage("angal.medicalstock.stockmovementinserting")); //$NON-NLS-1$
		add(getJPanelHeader(), BorderLayout.NORTH);
		add(getJMainPanel(), BorderLayout.CENTER);
		add(getJButtonPane(), BorderLayout.SOUTH);
		setPreferredSize(new Dimension(800, 600));
		pack();
		setLocationRelativeTo(null);
	}

	private JPanel getJPanelHeader() {
		JPanel headerPanel = new JPanel();
		getContentPane().add(headerPanel, BorderLayout.NORTH);
		GridBagLayout gbl_headerPanel = new GridBagLayout();
		gbl_headerPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_headerPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_headerPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_headerPanel.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		headerPanel.setLayout(gbl_headerPanel);
		{
			JLabel jLabelDate = new JLabel(MessageBundle.getMessage("angal.common.date")+":"); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelDate = new GridBagConstraints();
			gbc_jLabelDate.anchor = GridBagConstraints.WEST;
			gbc_jLabelDate.insets = new Insets(5, 5, 5, 5);
			gbc_jLabelDate.gridx = 0;
			gbc_jLabelDate.gridy = 0;
			headerPanel.add(jLabelDate, gbc_jLabelDate);
		}
		{
			GridBagConstraints gbc_dateChooser = new GridBagConstraints();
			gbc_dateChooser.anchor = GridBagConstraints.WEST;
			gbc_dateChooser.insets = new Insets(5, 0, 5, 5);
			gbc_dateChooser.fill = GridBagConstraints.VERTICAL;
			gbc_dateChooser.gridx = 1;
			gbc_dateChooser.gridy = 0;
			headerPanel.add(getJDateChooser(), gbc_dateChooser);
		}
		{
			JLabel jLabelReferenceNo = new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.referencenumberabb")+":"); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelReferenceNo = new GridBagConstraints();
			gbc_jLabelReferenceNo.anchor = GridBagConstraints.EAST;
			gbc_jLabelReferenceNo.insets = new Insets(5, 0, 5, 5);
			gbc_jLabelReferenceNo.gridx = 2;
			gbc_jLabelReferenceNo.gridy = 0;
			headerPanel.add(jLabelReferenceNo, gbc_jLabelReferenceNo);
		}
		{
			jTextFieldReference = new JTextField();
			GridBagConstraints gbc_jTextFieldReference = new GridBagConstraints();
			gbc_jTextFieldReference.insets = new Insets(5, 0, 5, 0);
			gbc_jTextFieldReference.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldReference.gridx = 3;
			gbc_jTextFieldReference.gridy = 0;
			headerPanel.add(jTextFieldReference, gbc_jTextFieldReference);
			jTextFieldReference.setColumns(10);
		}
		{
			JLabel jLabelChargeType = new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.chargetype")+":"); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelChargeType = new GridBagConstraints();
			gbc_jLabelChargeType.anchor = GridBagConstraints.EAST;
			gbc_jLabelChargeType.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelChargeType.gridx = 0;
			gbc_jLabelChargeType.gridy = 1;
			headerPanel.add(jLabelChargeType, gbc_jLabelChargeType);
		}
		{
			GridBagConstraints gbc_jComboBoxChargeType = new GridBagConstraints();
			gbc_jComboBoxChargeType.anchor = GridBagConstraints.WEST;
			gbc_jComboBoxChargeType.insets = new Insets(0, 0, 5, 5);
			gbc_jComboBoxChargeType.gridx = 1;
			gbc_jComboBoxChargeType.gridy = 1;
			headerPanel.add(getJComboBoxChargeType(), gbc_jComboBoxChargeType);
		}
		{
			JLabel jLabelSupplier = new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.supplier")+":"); //$NON-NLS-1$
			GridBagConstraints gbc_jLabelSupplier = new GridBagConstraints();
			gbc_jLabelSupplier.anchor = GridBagConstraints.WEST;
			gbc_jLabelSupplier.insets = new Insets(0, 5, 0, 5);
			gbc_jLabelSupplier.gridx = 0;
			gbc_jLabelSupplier.gridy = 3;
			headerPanel.add(jLabelSupplier, gbc_jLabelSupplier);
		}
		{
			GridBagConstraints gbc_jComboBoxSupplier = new GridBagConstraints();
			gbc_jComboBoxSupplier.anchor = GridBagConstraints.WEST;
			gbc_jComboBoxSupplier.insets = new Insets(0, 0, 0, 5);
			gbc_jComboBoxSupplier.gridx = 1;
			gbc_jComboBoxSupplier.gridy = 3;
			headerPanel.add(getJComboBoxSupplier(), gbc_jComboBoxSupplier);
		}
		return headerPanel;
	}

	private JPanel getJButtonPane() {
		JPanel buttonPane = new JPanel();
		//buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		{
			JButton deleteButton = new JButton(MessageBundle.getMessage("angal.common.delete"));
			deleteButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int row = jTableMovements.getSelectedRow();
					if (row > -1) model.removeItem(row);
				}
			});
			buttonPane.add(deleteButton);
		}
		{
			JButton saveButton = new JButton(MessageBundle.getMessage("angal.common.save")); //$NON-NLS-1$
			saveButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!checkAndPrepareMovements()) {
						return;
					}
					if (!save()) {
						return;
					}
					dispose();
				}
			});
			buttonPane.add(saveButton);
		}
		{
			JButton cancelButton = new JButton(MessageBundle.getMessage("angal.common.cancel")); //$NON-NLS-1$
			cancelButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			buttonPane.add(cancelButton);
		}
		return buttonPane;
	}

	private JPanel getJMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel(new BorderLayout());
			mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			mainPanel.add(getJTextFieldSearch(), BorderLayout.NORTH);
			mainPanel.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return mainPanel;
	}

	private JScrollPane getJScrollPane() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(getJTable());
		scrollPane.setPreferredSize(new Dimension(400, 450));
		return scrollPane;
	}

	private JTable getJTable() {
		if (jTableMovements == null) {
			model = new JTableModel();
			jTableMovements = new JTable(model);
			jTableMovements.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableMovements.setRowHeight(24);
			jTableMovements.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						int row = jTableMovements.getSelectedRow();
						model.removeItem(row);
					}
				}
			});

			for (int i = 0; i < columnNames.length; i++) {
				jTableMovements.getColumnModel().getColumn(i).setCellRenderer(new EnabledTableCellRenderer());
				jTableMovements.getColumnModel().getColumn(i).setMinWidth(columnWidth[i]);
				if (!columnResizable[i]) {
					jTableMovements.getColumnModel().getColumn(i).setResizable(columnResizable[i]);
					jTableMovements.getColumnModel().getColumn(i).setMaxWidth(columnWidth[i]);
				}
				if (!columnVisible[i]) {
					jTableMovements.getColumnModel().getColumn(i).setMinWidth(0);
					jTableMovements.getColumnModel().getColumn(i).setMaxWidth(0);
					jTableMovements.getColumnModel().getColumn(i).setWidth(0);
				}
			}

			TableColumn qtyOptionColumn = jTableMovements.getColumnModel().getColumn(4);
			qtyOptionColumn.setCellEditor(new DefaultCellEditor(comboBoxUnits));
			
			TableColumn costColumn = jTableMovements.getColumnModel().getColumn(8);
			costColumn.setCellRenderer(new DecimalFormatRenderer());
			
			TableColumn totalColumn = jTableMovements.getColumnModel().getColumn(9);
			totalColumn.setCellRenderer(new DecimalFormatRenderer());
			
			comboBoxUnits.setSelectedIndex(optionSelected);
		}
		return jTableMovements;
	}

	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setPreferredSize(new Dimension(300, 30));
			jTextFieldSearch.setHorizontalAlignment(SwingConstants.LEFT);
			
			jTextFieldSearch.setColumns(10);
			TextPrompt suggestion = new TextPrompt(
					MessageBundle.getMessage("angal.medicalstock.typeacodeoradescriptionandpressenter"), //$NON-NLS-1$ 
					jTextFieldSearch, 
					Show.FOCUS_LOST); 
			{
				suggestion.setFont(new Font("Tahoma", Font.PLAIN, 14)); //$NON-NLS-1$
				suggestion.setForeground(Color.GRAY);
				suggestion.setHorizontalAlignment(JLabel.CENTER);
				suggestion.changeAlpha(0.5f);
				suggestion.changeStyle(Font.BOLD + Font.ITALIC);
			}
			jTextFieldSearch.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String text = jTextFieldSearch.getText();
					Medical med = null;
					if (medicalMap.containsKey(text)) {
						// Medical found
						med = medicalMap.get(text);
					} else {
						
						med = chooseMedical(text.toLowerCase());
					}
						
					if (med != null) {	
						
						// Quantity
						int qty = askQuantity(med);
						if (qty == 0)
							return;

						// Lot (PreparationDate && ExpiringDate)
						Lot lot = null;
						boolean isNewLot = false;
						if (isAutomaticLot()) {
							GregorianCalendar preparationDate = new GregorianCalendar();
							GregorianCalendar expiringDate = askExpiringDate();
							lot = new Lot("", preparationDate, expiringDate); //$NON-NLS-1$
							// Cost
							double cost = 0;
							if (GeneralData.LOTWITHCOST) {
								cost = askCost(qty);
								if (cost == 0.) 
									return;
							}
							isNewLot = true;
							lot.setCost(cost);
						} else {
							do {
								lot = chooseLot(med);
								if (lot == null) {
									lot = askLot();
									if (lot == null) {
										return;
									}
									// Lot Cost
									double cost = 0;
									if (GeneralData.LOTWITHCOST) {
										cost = askCost(qty);
										if (cost == 0.) 
											return;
									}
									isNewLot = true;
									lot.setCost(cost);
								}
							} while (lot == null);
						}

						// Date
						GregorianCalendar date = new GregorianCalendar();
						date.setTime(jDateChooser.getDate());
						
						// RefNo
						String refNo = jTextFieldReference.getText().trim();
						
						Movement movement = new Movement(med, (MovementType) jComboBoxChargeType.getSelectedItem(), null, lot, date, qty, new Supplier(), refNo);
						model.addItem(movement, new Boolean(isNewLot));

						units.add(PACKETS);

						jTextFieldSearch.setText(""); //$NON-NLS-1$
						jTextFieldSearch.requestFocus();
					}
				}
			});
		}
		return jTextFieldSearch;
	}

	private JDateChooser getJDateChooser() {
		if (jDateChooser == null) {
			jDateChooser = new JDateChooser(new Date());
			jDateChooser.setDateFormatString(DATE_FORMAT_DD_MM_YYYY_HH_MM_SS);
			jDateChooser.setPreferredSize(new Dimension(150, 24));
		}
		return jDateChooser;
	}

	private JComboBox getJComboBoxChargeType() {
		if (jComboBoxChargeType == null) {
			jComboBoxChargeType = new JComboBox();
			ArrayList<MovementType> movTypes;
			try {
				movTypes = medicaldsrstockmovTypeBrowserManager.getMedicaldsrstockmovType();
			} catch (OHServiceException e) {
				movTypes = null;
				OHServiceExceptionUtil.showMessages(e);
			}
			if (null != movTypes) {
				for (MovementType movType : movTypes) {
					if (movType.getType().contains("+")) //$NON-NLS-1$
						jComboBoxChargeType.addItem(movType);
				}
			}
		}
		return jComboBoxChargeType;
	}

	protected double askCost(int qty) {
		double cost = 0.;
		do {
			String input = JOptionPane.showInputDialog(MovStockMultipleCharging.this, 
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.unitcost"),  //$NON-NLS-1$
					0.);
			if (input != null) {
				try {
					cost = Double.parseDouble(input);
					if (cost < 0)
						throw new NumberFormatException();
					else if (cost == 0.) {
						double total = askTotalCost();
						//if (total == 0.) return;
						cost = total / qty;
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(MovStockMultipleCharging.this, 
							MessageBundle.getMessage("angal.medicalstock.multiplecharging.pleaseinsertavalidvalue")); //$NON-NLS-1$
				}
			} else return cost;
		} while (cost == 0.);
		return cost;
	}
	
	protected double askTotalCost() {
		String input = JOptionPane.showInputDialog(MovStockMultipleCharging.this, 
				MessageBundle.getMessage("angal.medicalstock.multiplecharging.totalcost"), //$NON-NLS-1$
				0.);
		double total = 0.;
		if (input != null) {
			try {
				total = Double.parseDouble(input);
				if (total < 0)
					throw new NumberFormatException();
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(MovStockMultipleCharging.this, 
						MessageBundle.getMessage("angal.medicalstock.multiplecharging.pleaseinsertavalidvalue")); //$NON-NLS-1$
			}
		}
		return total;
	}

	protected Lot askLot() {
		GregorianCalendar preparationDate = new GregorianCalendar();
		GregorianCalendar expiringDate = new GregorianCalendar();
		Lot lot = null;

		JTextField lotNameTextField = new JTextField(15);
		lotNameTextField.addAncestorListener(new RequestFocusListener());
		if (isAutomaticLot())
			lotNameTextField.setEnabled(false);
		TextPrompt suggestion = new TextPrompt(
				MessageBundle.getMessage("angal.medicalstock.multiplecharging.lotid"), //$NON-NLS-1$
				lotNameTextField); 
		{
			suggestion.setFont(new Font("Tahoma", Font.PLAIN, 14)); //$NON-NLS-1$
			suggestion.setForeground(Color.GRAY);
			suggestion.setHorizontalAlignment(JLabel.CENTER);
			suggestion.changeAlpha(0.5f);
			suggestion.changeStyle(Font.BOLD + Font.ITALIC);
		}
		JDateChooser preparationDateChooser = new JDateChooser(new Date());
		{
			preparationDateChooser.setDateFormatString(DATE_FORMAT_DD_MM_YYYY);
		}
		JDateChooser expireDateChooser = new JDateChooser(new Date());
		{
			expireDateChooser.setDateFormatString(DATE_FORMAT_DD_MM_YYYY);
		}
		JPanel panel = new JPanel(new GridLayout(3, 2));
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.lotnumberabb"))); //$NON-NLS-1$
		panel.add(lotNameTextField);
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.preparationdate"))); //$NON-NLS-1$
		panel.add(preparationDateChooser);
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdate"))); //$NON-NLS-1$
		panel.add(expireDateChooser);

		do {
			int ok = JOptionPane.showConfirmDialog(
					MovStockMultipleCharging.this, 
					panel, 
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.lotinformations"), //$NON-NLS-1$
					JOptionPane.OK_CANCEL_OPTION);
			
			if (ok == JOptionPane.OK_OPTION) {
				String lotName = lotNameTextField.getText();
				
				if (expireDateChooser.getDate().before(preparationDateChooser.getDate())) 
				{
					JOptionPane.showMessageDialog(
							MovStockMultipleCharging.this, 
							MessageBundle.getMessage("angal.medicalstock.multiplecharging.expirydatebeforepreparationdate")); //$NON-NLS-1$
				} 
				else if (expireDateChooser.getDate().before(jDateChooser.getDate())) 
				{
					JOptionPane.showMessageDialog(
							MovStockMultipleCharging.this, 
							MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdateinthepastnotallowed")); //$NON-NLS-1$
				} 
				else 
				{
					expiringDate.setTime(expireDateChooser.getDate());
					preparationDate.setTime(preparationDateChooser.getDate());
					lot = new Lot(lotName, preparationDate, expiringDate);
				}
			} else {
				
				return null;
			}
		}
		while (lot == null);
		return lot;
	}
	
	protected Medical chooseMedical(String text) {
		ArrayList<Medical> medList = new ArrayList<Medical>();
		for (Medical aMed : medicalMap.values()) {
			if (NormalizeString.normalizeContains(aMed.getDescription().toLowerCase(), text.toLowerCase()))
				medList.add(aMed);
		}
		Collections.sort(medList);
		Medical med = null;
		
		if (!medList.isEmpty()) {
			JTable medTable = new JTable(new StockMedModel(medList));
			medTable.getColumnModel().getColumn(0).setMaxWidth(CODE_COLUMN_WIDTH);
			medTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JPanel panel = new JPanel();
			panel.add(new JScrollPane(medTable));
			
			int ok = JOptionPane.showConfirmDialog(
					MovStockMultipleCharging.this, 
					panel, 
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.chooseamedical"), //$NON-NLS-1$ 
					JOptionPane.YES_NO_OPTION);
			
			if (ok == JOptionPane.OK_OPTION) {
				int row = medTable.getSelectedRow();
				med = medList.get(row);
			}
			return med;
		}
		return null;
	}

	protected Lot chooseLot(Medical med) {
		ArrayList<Lot> lots;
		try {
			lots = movManager.getLotByMedical(med);
		} catch (OHServiceException e) {
			lots = new ArrayList<Lot>();
			OHServiceExceptionUtil.showMessages(e);
		}
		Lot lot = null;
		if (!lots.isEmpty()) {
			JTable lotTable = new JTable(new StockMovModel(lots));
			lotTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.useanexistinglot")), BorderLayout.NORTH); //$NON-NLS-1$
			panel.add(new JScrollPane(lotTable), BorderLayout.CENTER);
			Object[] options = {
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.selectedlot"), //$NON-NLS-1$
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.newlot")}; //$NON-NLS-1$
						
			int row = -1;
			do {
				
				int ok = JOptionPane.showOptionDialog(MovStockMultipleCharging.this, 
						panel, 
						MessageBundle.getMessage("angal.medicalstock.multiplecharging.existinglot"), //$NON-NLS-1$
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						options, 
						options[0]);

				if (ok == JOptionPane.YES_OPTION) {
					row = lotTable.getSelectedRow();
					if (row != -1) lot = lots.get(row);
					else JOptionPane.showMessageDialog(MovStockMultipleCharging.this, 
							MessageBundle.getMessage("angal.common.pleaseselectarow")); //$NON-NLS-1$
				} else row = 0;
				
			} while (row == -1);
		}
		return lot;
	}

	protected GregorianCalendar askExpiringDate() {
		GregorianCalendar date = new GregorianCalendar();
		JDateChooser expireDateChooser = new JDateChooser(new Date());
		{
			expireDateChooser.setDateFormatString(DATE_FORMAT_DD_MM_YYYY);
		}
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(new JLabel(MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdate"))); //$NON-NLS-1$
		panel.add(expireDateChooser);

		int ok = JOptionPane.showConfirmDialog(MovStockMultipleCharging.this, panel, 
				MessageBundle.getMessage("angal.medicalstock.multiplecharging.expiringdate"), //$NON-NLS-1$
				JOptionPane.OK_CANCEL_OPTION); 

		if (ok == JOptionPane.OK_OPTION) {
			date.setTime(expireDateChooser.getDate());
		}
		return date;
	}

	protected int askQuantity(Medical med) {
		StringBuilder title = new StringBuilder(MessageBundle.getMessage("angal.common.quantity")); //$NON-NLS-1$
		StringBuilder message = new StringBuilder(med.toString());
		String prodCode = med.getProd_code();
		if (prodCode != null && !prodCode.equals("")) {
			title.append(" ").append(MessageBundle.getMessage("angal.common.code")); //$NON-NLS-1$ //$NON-NLS-2$
			title.append(": ").append(prodCode); //$NON-NLS-1$
		} else { 
			title.append(": "); //$NON-NLS-1$
		}
		int qty = 0;
		do {
			String quantity = JOptionPane.showInputDialog(MovStockMultipleCharging.this, 
					message.toString(), 
					title.toString(),
					JOptionPane.QUESTION_MESSAGE);
			if (quantity != null) {
				try {
					qty = Integer.parseInt(quantity);
					if (qty == 0)
						return 0;
					if (qty < 0)
						throw new NumberFormatException();
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(MovStockMultipleCharging.this, 
							MessageBundle.getMessage("angal.medicalstock.multiplecharging.pleaseinsertavalidvalue")); //$NON-NLS-1$
					qty = 0;
				}
			} else return qty;
		} while (qty == 0);
		return qty;
	}

	private JComboBox getJComboBoxSupplier() {
		if (jComboBoxSupplier == null) {
			jComboBoxSupplier = new JComboBox();
			jComboBoxSupplier.addItem(""); //$NON-NLS-1$
			ArrayList<Supplier> suppliers = null;
			try {
				suppliers = (ArrayList<Supplier>) supplierBrowserManager.getList();
            } catch (OHServiceException e) {
                OHServiceExceptionUtil.showMessages(e);
            }
            if(suppliers != null) {
                for (Supplier sup : suppliers) {
                    jComboBoxSupplier.addItem(sup);
                }
            }
		}
		return jComboBoxSupplier;
	}

	public class JTableModel extends AbstractTableModel {

		private ArrayList<Movement> movements;
		private ArrayList<Boolean> newLots;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public JTableModel() {
			movements = new ArrayList<Movement>();
			newLots = new ArrayList<Boolean>();
		}
		
		public ArrayList<Movement> getMovements() {
			return movements;
		}

		public void removeItem(int row) {
			movements.remove(row);
			units.remove(row);
			newLots.remove(row);
			fireTableDataChanged();
		}

		public void addItem(Movement movement, Boolean isNewLot) {
			movements.add(movement);
			newLots.add(isNewLot);
			fireTableDataChanged();
		}

		@Override
		public int getRowCount() {
			return movements.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return columnNames[columnIndex];
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return columnClasses[columnIndex];
		}

		@Override
		public boolean isCellEditable(int r, int c) {
			if (c == 6 || c == 7 || c == 8)
				return newLots.get(r).booleanValue();
			return columnEditable[c];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int r, int c) {
			Movement movement = movements.get(r);
			Medical medical = movement.getMedical();
			Lot lot = movement.getLot();
			String lotName = lot.getCode();
			int qty = movement.getQuantity();
			int ppp = medical.getPcsperpck().intValue() == 0 ? 1 : medical.getPcsperpck().intValue();
			int option = units.get(r);
			int total = option == UNITS ? qty : ppp * qty;
			double cost = lot.getCost();
			if (c == -1) {
				return movement;
			} else if (c == 0) {
				return medical.getProd_code();
			} else if (c == 1) {
				return medical.getDescription();
			} else if (c == 2) {
				return ppp;
			} else if (c == 3) {
				return qty;
			} else if (c == 4) {
				return qtyOption[option];
			} else if (c == 5) {
				return total;
			} else if (c == 6) {
				return lotName.equals("") ? "AUTO" : lotName; //$NON-NLS-1$ //$NON-NLS-2$
			} else if (c == 7) {
				return TimeTools.formatDateTime(lot.getDueDate(), DATE_FORMAT_DD_MM_YYYY);
			} else if (c == 8) {
				return cost;
			} else if (c == 9) {
				return cost * total;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int,
		 * int)
		 */
		@Override
		public void setValueAt(Object value, int r, int c) {
			Movement movement = movements.get(r);
			Lot lot = movement.getLot();
			if (c == 0) {
				String key = String.valueOf(value);
				if (medicalMap.containsKey(key)) {
					movement.setMedical(medicalMap.get(key));
					movements.set(r, movement);
				}
			} else if (c == 3) {
				movement.setQuantity((Integer) value);
			} else if (c == 4) {
				int newOption = 0;
				if (value == qtyOption[1]) newOption = 1; 
				units.set(r, newOption);
			} else if (c == 6) {
				lot.setCode((String) value);
			} else if (c == 7) {
				try {
					GregorianCalendar date = TimeTools.parseDate((String) value, DATE_FORMAT_DD_MM_YYYY, true);
					lot.setDueDate(date);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (c == 8) {
				lot.setCost((Double) value);
			}
			movements.set(r, movement);
			fireTableDataChanged();
		}
	}
	
	private boolean checkAndPrepareMovements() {
		boolean ok = true;

		ArrayList<Movement> movements = model.getMovements();
		if (movements.isEmpty()) {
			JOptionPane.showMessageDialog(MovStockMultipleCharging.this, 
					MessageBundle.getMessage("angal.medicalstock.multiplecharging.noelementtosave")); //$NON-NLS-1$
			return false;
		}
		
		// Check supplier
		Object supplier = jComboBoxSupplier.getSelectedItem();
		if (supplier == null || supplier instanceof String) {
			JOptionPane.showMessageDialog(MovStockMultipleCharging.this, MessageBundle.getMessage("angal.medicalstock.multiplecharging.pleaseselectasupplier")); //$NON-NLS-1$
			return false;
		}
		
		GregorianCalendar thisDate = new GregorianCalendar();
		thisDate.setTime(jDateChooser.getDate());
		
		// Check and set all movements
		for (int i = 0; i < movements.size(); i++) {
			Movement mov = movements.get(i);
			int option = units.get(i);
			mov.setDate(thisDate);
			mov.setRefNo(jTextFieldReference.getText());
			mov.setQuantity(calcTotal(mov, option));
			mov.setType((MovementType) jComboBoxChargeType.getSelectedItem());
			mov.setSupplier(((Supplier) jComboBoxSupplier.getSelectedItem()));
			//mov.getLot().setPreparationDate(thisDate);
		}
		return ok;
	}
	
	private int calcTotal(Movement mov, int option) {
		Medical medical = mov.getMedical();
		int qty = mov.getQuantity();
		int ppp = medical.getPcsperpck().intValue() == 0 ? 1 : medical.getPcsperpck().intValue();
		int total = option == UNITS ? qty : ppp * qty;
		
		return total;
	}
	
	private boolean save() {
		boolean ok = true;
		ArrayList<Movement> movements = model.getMovements();
		try {
			movManager.newMultipleChargingMovements(movements, movements.get(0).getRefNo());
		} catch (OHServiceException e) {
			ok = false;
			OHServiceExceptionUtil.showMessages(e);
		} 
		return ok;
	}
	
	class EnabledTableCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setHorizontalAlignment(columnAlignment[column]);
			if (!table.isCellEditable(row, column)) {
				cell.setBackground(Color.LIGHT_GRAY);
			} else {
				cell.setBackground(Color.WHITE);
			}
			if (columnBold[column]) { 
				cell.setFont(new Font(null, Font.BOLD, 12));
			}
			return cell;
		}
	}
	
	class DecimalFormatRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final DecimalFormat formatter = new DecimalFormat("#,##0.00"); //$NON-NLS-1$

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			// First format the cell value as required
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			value = formatter.format((Number) value);
			setHorizontalAlignment(columnAlignment[column]);
			if (!table.isCellEditable(row, column)) {
				cell.setBackground(Color.LIGHT_GRAY);
			} else {
				cell.setBackground(Color.WHITE);
			}
			if (columnBold[column]) { 
				cell.setFont(new Font(null, Font.BOLD, 12));
			}
			// And pass it on to parent class
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	
	class StockMovModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Lot> lotList;

		public StockMovModel(ArrayList<Lot> lots) {
			lotList = lots;
		}

		public int getRowCount() {
			if (lotList == null)
				return 0;
			return lotList.size();
		}

		public String getColumnName(int c) {
			if (c == 0) {
				return MessageBundle.getMessage("angal.medicalstock.lotid"); //$NON-NLS-1$
			}
			if (c == 1) {
				return MessageBundle.getMessage("angal.medicalstock.prepdate"); //$NON-NLS-1$
			}
			if (c == 2) {
				return MessageBundle.getMessage("angal.medicalstock.duedate"); //$NON-NLS-1$
			}
			if (c == 3) {
				return MessageBundle.getMessage("angal.common.quantity"); //$NON-NLS-1$
			}
			if (GeneralData.LOTWITHCOST) {
				if (c == 4) {
					return MessageBundle.getMessage("angal.medicalstock.multiplecharging.cost"); //$NON-NLS-1$
				}
			}
			return ""; //$NON-NLS-1$
		}

		public int getColumnCount() {
			if (GeneralData.LOTWITHCOST) return 5;
			return 4;
		}

		public Object getValueAt(int r, int c) {
			Lot lot = lotList.get(r);
			if (c == -1) {
				return lot;
			} else if (c == 0) {
				return lot.getCode();
			} else if (c == 1) {
				return TimeTools.formatDateTime(lot.getPreparationDate(), DATE_FORMAT_DD_MM_YYYY);
			} else if (c == 2) {
				return TimeTools.formatDateTime(lot.getDueDate(), DATE_FORMAT_DD_MM_YYYY);
			} else if (c == 3) {
				return lot.getQuantity();
			} else if (c == 4) {
				return lot.getCost();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}
	
	class StockMedModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<Medical> medList;

		public StockMedModel(ArrayList<Medical> meds) {
			medList = meds;
		}

		public int getRowCount() {
			if (medList == null)
				return 0;
			return medList.size();
		}

		public String getColumnName(int c) {
			if (c == 0) {
				return MessageBundle.getMessage("angal.common.code"); //$NON-NLS-1$
			}
			if (c == 1) {
				return MessageBundle.getMessage("angal.common.description"); //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
		}

		public int getColumnCount() {
			return 2;
		}

		public Object getValueAt(int r, int c) {
			Medical med = medList.get(r);
			if (c == -1) {
				return med;
			} else if (c == 0) {
				return med.getProd_code();
			} else if (c == 1) {
				return med.getDescription();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}
}
