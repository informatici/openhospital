package org.isf.patvac.gui;

/*------------------------------------------
 * PatVacBrowser - list all patient's vaccines
 * -----------------------------------------
 * modification history
 * 25/08/2011 - claudia - first beta version
 * 25/10/2011 - claudia - modify selection section
 * 14/11/2011 - claudia - elimitated @override tag
 *                      - inserted ENHANCEDSEARCH functionality on search
 * 
 *------------------------------------------*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.patient.model.Patient;
import org.isf.patvac.manager.PatVacManager;
import org.isf.patvac.model.PatientVaccine;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.vaccine.manager.VaccineBrowserManager;
import org.isf.vaccine.model.Vaccine;
import org.isf.vactype.manager.VaccineTypeBrowserManager;
import org.isf.vactype.model.VaccineType;

import com.toedter.calendar.JDateChooser;


public class PatVacBrowser extends ModalJFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VERSION=MessageBundle.getMessage("angal.versione");
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	
	private JPanel jContentPane = null;
	private JPanel jButtonPanel = null;
	private JButton buttonEdit = null;
	private JButton buttonNew = null;
	private JButton buttonDelete = null;
	private JButton buttonClose = null;
	private JButton filterButton = null;
	private JPanel jSelectionPanel = null;
	private JPanel jAgePanel = null ;
	private VoLimitedTextField jAgeFromTextField = null;
	private VoLimitedTextField jAgeToTextField = null;
	private Integer ageTo = 0;
	private Integer ageFrom = 0 ;
	private JPanel sexPanel=null;
	private ButtonGroup group=null;
	private JRadioButton radiom;
	private JRadioButton radiof;
	private JRadioButton radioa;
	private String sexSelect = MessageBundle.getMessage("angal.patvac.all");
	private JLabel rowCounter = null;
	private String rowCounterText = MessageBundle.getMessage("angal.patvac.count") + ": ";
	
	
	private JTable jTable = null;
	private JComboBox vaccineComboBox = null;
	private JComboBox vaccineTypeComboBox = null;
	private int pfrmHeight;
	private ArrayList<PatientVaccine> lPatVac;
	
		
	private String[] pColums = { MessageBundle.getMessage("angal.common.datem"), MessageBundle.getMessage("angal.patvac.patientm"), MessageBundle.getMessage("angal.patvac.sexm"),MessageBundle.getMessage("angal.patvac.agem"), MessageBundle.getMessage("angal.patvac.vaccinem"), MessageBundle.getMessage("angal.patvac.vaccinetypem")};
	private int[] pColumwidth = { 100, 150, 50, 50, 150, 150};
	private boolean[] columnsVisible = { true, GeneralData.PATIENTVACCINEEXTENDED, true, true, true, true};
	private PatVacManager manager;
	private PatVacBrowsingModel model;
	private PatientVaccine patientVaccine;
	private int selectedrow;
	private JDateChooser dateFrom = null;
	private JDateChooser dateTo = null;
	private final JFrame myFrame;

	public PatVacBrowser() {
		super();
		myFrame = this;
		manager = new PatVacManager();
		initialize();
		setVisible(true);
		
	}

	/**
	 * This method initializes this Frame, sets the correct Dimensions
	 * 
	 * @return void
	 */
	private void initialize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
        final int pfrmBase = 20;
        final int pfrmWidth = 17;
        final int pfrmHeight = 12;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2,
        		(screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase+50,
                screensize.height * pfrmHeight / pfrmBase+20);
        setTitle(MessageBundle.getMessage("angal.patvac.patientvaccinebrowsing")+" ("+VERSION+")");
		this.setContentPane(getJContentPane());
		updateRowCounter();
		validate();
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * This method initializes jContentPane, adds the main parts of the frame
	 * 
	 * @return jContentPanel (JPanel)
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJSelectionPanel(), java.awt.BorderLayout.WEST);
			jContentPane.add(new JScrollPane(getJTable()), java.awt.BorderLayout.CENTER);
			updateRowCounter();
		}
		return jContentPane;
	}
	
	
	/**
	 * This method initializes JButtonPanel, that contains the buttons of the
	 * frame (on the bottom)
	 * 
	 * @return JButtonPanel (JPanel)
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			if (MainMenu.checkUserGrants("btnpatientvaccinenew")) jButtonPanel.add(getButtonNew(), null);
			if (MainMenu.checkUserGrants("btnpatientvaccineedit")) jButtonPanel.add(getButtonEdit(), null);
			if (MainMenu.checkUserGrants("btnpatientvaccinedel")) jButtonPanel.add(getButtonDelete(), null);
			jButtonPanel.add((getCloseButton()), null);
		}
		return jButtonPanel;
	}

	/**
	 * This method initializes buttonNew, that loads patientVaccineEdit Mask
	 * 
	 * @return buttonNew (JButton)
	 */
	private JButton getButtonNew(){
	   if (buttonNew == null) {
		   buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
		   buttonNew.setMnemonic(KeyEvent.VK_N);
		   buttonNew.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					patientVaccine = new PatientVaccine(0,0,new GregorianCalendar(),new Patient(),
							                new Vaccine ("","",new VaccineType("","")),0);
							
					PatientVaccine  last = new PatientVaccine(0,0,new GregorianCalendar(),new Patient(),
							                     new Vaccine ("","",new VaccineType("","")),0);
                    new PatVacEdit (myFrame, patientVaccine, true);
                    
                    if (!last.equals(patientVaccine)) {
						lPatVac.add(0, patientVaccine);
						((PatVacBrowsingModel) jTable.getModel()).fireTableDataChanged();
						updateRowCounter();
						if (jTable.getRowCount() > 0)
							jTable.setRowSelectionInterval(0, 0);
					}
				}
			});		   
	   }
	   return buttonNew;
	}

	/**
	 * This method initializes buttonEdit, that loads patientVaccineEdit Mask
	 * 
	 * @return buttonEdit (JButton)
	 */
	private JButton getButtonEdit(){
		
		if (buttonEdit == null) {
			buttonEdit = new JButton(MessageBundle.getMessage("angal.common.edit"));
			buttonEdit.setMnemonic(KeyEvent.VK_S);
			buttonEdit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (jTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.common.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} 
					
					selectedrow = jTable.getSelectedRow();
					patientVaccine = (PatientVaccine) (((PatVacBrowsingModel) model).getValueAt(selectedrow, -1));

					PatientVaccine last = new PatientVaccine(patientVaccine.getCode(),
								                  patientVaccine.getProgr(),
								                  patientVaccine.getVaccineDate(),
								                  patientVaccine.getPatient(),
								                  patientVaccine.getVaccine(),
								                  patientVaccine.getLock(),
								                  patientVaccine.getPatName(),
								                  patientVaccine.getPatAge(),
								                  patientVaccine.getPatSex());
					
					new PatVacEdit (myFrame, patientVaccine, false);
					
					if (!last.equals(patientVaccine)) {
						((PatVacBrowsingModel) jTable.getModel()).fireTableDataChanged();
						updateRowCounter();
						if ((jTable.getRowCount() > 0) && selectedrow > -1)
						  	jTable.setRowSelectionInterval(selectedrow, selectedrow);
					}
				}
			});
		}		
     return buttonEdit;
	}

	
	/**
	 * This method initializes buttonDelete, that loads patientVaccineEdit Mask
	 * 
	 * @return buttonDelete (JButton)
	 */
	private JButton getButtonDelete(){
		if (buttonDelete == null) {
			buttonDelete = new JButton(MessageBundle.getMessage("angal.common.delete"));
			buttonDelete.setMnemonic(KeyEvent.VK_D);
			buttonDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (jTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.common.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} 
					selectedrow = jTable.getSelectedRow();
					patientVaccine = (PatientVaccine) (((PatVacBrowsingModel) model).getValueAt(selectedrow, -1));
                    int n = JOptionPane.showConfirmDialog(null,
								MessageBundle.getMessage("angal.patvac.deleteselectedpatientvaccinerow") +
								"\n"+ MessageBundle.getMessage("angal.patvac.vaccinedate")+" = " +  dateFormat.format( patientVaccine.getVaccineDate().getTime()) +
								"\n "+ MessageBundle.getMessage("angal.patvac.vaccine")+" = " + patientVaccine.getVaccine().getDescription() + 
								"\n "+ MessageBundle.getMessage("angal.patvac.patient")+" =" + patientVaccine.getPatName() + 
								"\n ?",
								MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);

					if (n == JOptionPane.YES_OPTION) {
						
							boolean deleted;
							
							try {
								deleted = manager.deletePatientVaccine(patientVaccine);
							} catch (OHServiceException e) {
								deleted = false;
								OHServiceExceptionUtil.showMessages(e);
							}
						
							if (true == deleted) {
								lPatVac.remove(jTable.getSelectedRow());
								model.fireTableDataChanged();
								jTable.updateUI();
							}
					}
				}
			});
		 }
     return buttonDelete;
	}	
	
	/**
	 * This method initializes buttonClose
	 * 
	 * @return buttonClose (JButton)
	 */
	private JButton getCloseButton() {
		if (buttonClose == null) {
			buttonClose = new JButton(MessageBundle.getMessage("angal.common.close"));
			buttonClose.setMnemonic(KeyEvent.VK_C);
			buttonClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return buttonClose;
	}
	
		
	/**
	 * This method initializes JSelectionPanel, that contains the filter objects
	 * 
	 * @return JSelectionPanel (JPanel)
	 */
	private JPanel getJSelectionPanel() {
		if (jSelectionPanel == null) {
			jSelectionPanel = new JPanel();
			jSelectionPanel.setPreferredSize(new Dimension(220, pfrmHeight));
			jSelectionPanel.setLayout(new BoxLayout(jSelectionPanel, BoxLayout.Y_AXIS));
			
			jSelectionPanel.add(getVaccineTypePanel());
			jSelectionPanel.add(getVaccinePanel());
			
			jSelectionPanel.add(getDatePanel());
			jSelectionPanel.add(getAgePanel());

			jSelectionPanel.add(getSexPanel());
			jSelectionPanel.add(getFilterPanel());
			jSelectionPanel.add(getRowCounterPanel());
		}
		return jSelectionPanel;
	}
	

	
	/**
	 * This method initializes getVaccineTypePanel
	 * 
	 * @return vaccineTypePanel  (JPanel)
	 */
	private JPanel getVaccineTypePanel (){
		
		JPanel vaccineTypePanel = new JPanel();
		
		vaccineTypePanel.setLayout(new BoxLayout(vaccineTypePanel, BoxLayout.Y_AXIS));
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.patvac.selectavaccinetype")));
		vaccineTypePanel.add(label1Panel);
		
		label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(getComboVaccineTypes());
		vaccineTypePanel.add(label1Panel,null);
		return vaccineTypePanel;
	}
	
	
	/**
	 * This method initializes getVaccinePanel
	 * 
	 * @return vaccinePanel  (JPanel)
	 */	
	private JPanel getVaccinePanel (){
			
		JPanel vaccinePanel = new JPanel();
		
		vaccinePanel.setLayout(new BoxLayout(vaccinePanel, BoxLayout.Y_AXIS));
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.patvac.selectavaccine")));
		vaccinePanel.add(label1Panel);
		
		label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(getComboVaccines());
		vaccinePanel.add(label1Panel,null);
		return vaccinePanel;
	}

	/**
	 * This method initializes getDatePanel
	 * 
	 * @return datePanel  (JPanel)
	 */
	private JPanel getDatePanel (){
		
		JPanel datePanel = new JPanel();
		
		datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));

		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.common.date") +": "+ MessageBundle.getMessage("angal.common.from")), null);
		datePanel.add(label1Panel);
		
		label1Panel.add(getDateFromPanel());
		datePanel.add(label1Panel,null);
		
		label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		label1Panel.add(new JLabel(MessageBundle.getMessage("angal.common.date") +": "+MessageBundle.getMessage("angal.common.to") +"     "), null);
		datePanel.add(label1Panel);
		
		label1Panel.add(getDateToPanel());
		datePanel.add(label1Panel,null);
		
		return datePanel;
	}

	/**
	 * This method initializes getAgePanel
	 * 
	 * @return jAgePanel  (JPanel)
	 */
	private JPanel getAgePanel() {
		if (jAgePanel == null) {
			jAgePanel = new JPanel();
			jAgePanel.setLayout(new BoxLayout(getAgePanel(),BoxLayout.Y_AXIS));
			
			JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label1Panel.add(new JLabel(MessageBundle.getMessage("angal.patvac.agefrom")), null);
			jAgePanel.add(label1Panel);
			label1Panel.add(getJAgeFromTextField(), null);
			jAgePanel.add(label1Panel);
			
			label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label1Panel.add(new JLabel(MessageBundle.getMessage("angal.patvac.ageto")), null);
			jAgePanel.add(label1Panel);
			label1Panel.add(getJAgeToTextField(), null);
			jAgePanel.add(label1Panel);
		}
		return jAgePanel;
	}
	
	/**
	 * This method initializes getSexPanel
	 * 
	 * @return sexPanel  (JPanel)
	 */
	public JPanel getSexPanel() {
		if (sexPanel == null) {
			sexPanel = new JPanel();
			sexPanel.setLayout(new BoxLayout(sexPanel, BoxLayout.Y_AXIS));
			JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			label1Panel.add(new JLabel(MessageBundle.getMessage("angal.patvac.selectsex")), null);
			sexPanel.add(label1Panel);
			
			label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			group=new ButtonGroup();
			radiom= new JRadioButton(MessageBundle.getMessage("angal.patvac.male"));
			radiof= new JRadioButton(MessageBundle.getMessage("angal.patvac.female"));
			radioa= new JRadioButton(MessageBundle.getMessage("angal.patvac.all"));
			radioa.setSelected(true);
			group.add(radiom);
			group.add(radiof);
			group.add(radioa);
			
			label1Panel.add(radioa);
			sexPanel.add(label1Panel);
			label1Panel.add(radiom);
			sexPanel.add(label1Panel);
			label1Panel.add(radiof);
			sexPanel.add(label1Panel);
		}
		return sexPanel;
	}
	
	/**
	 * This method initializes getFilterPanel 
	 * 
	 * @return filterPanel  (JPanel)
	 */
	private JPanel getFilterPanel (){
		
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    label1Panel.add(getFilterButton());
		filterPanel.add(label1Panel);
		return filterPanel;
	}
	
	/**
	 * This method initializes getRowCounterPanel 
	 * 
	 * @return rowCounterPanel  (JPanel)
	 */
	private JPanel  getRowCounterPanel() {
		
		JPanel rowCounterPanel = new JPanel();
		
		rowCounterPanel.setLayout(new BoxLayout(rowCounterPanel, BoxLayout.Y_AXIS));
		JPanel label1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		rowCounter = new JLabel(MessageBundle.getMessage("angal.patvac.rowcounter"));
		label1Panel.add(rowCounter, null);
		rowCounterPanel.add(label1Panel);
		return rowCounterPanel;
	}
	
	/**
	 * This method initializes jAgeFromTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private VoLimitedTextField getJAgeFromTextField() {
		if (jAgeFromTextField == null) {
			jAgeFromTextField = new VoLimitedTextField(3,2);
			jAgeFromTextField.setText("0");
			jAgeFromTextField.setMinimumSize(new Dimension(100, 50));
			ageFrom=0;
			jAgeFromTextField.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {				
					try {				
						ageFrom = Integer.parseInt(jAgeFromTextField.getText());
						if ((ageFrom<0)||(ageFrom>200)) {
							jAgeFromTextField.setText("0");
							ageFrom = Integer.parseInt(jAgeFromTextField.getText());
						    JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patvac.insertvalidage"));
						}
					} catch (NumberFormatException ex) {
						jAgeFromTextField.setText("0");
						ageFrom = Integer.parseInt(jAgeFromTextField.getText());
					}
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jAgeFromTextField;
	}
	
	
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private VoLimitedTextField getJAgeToTextField() {
		if (jAgeToTextField == null) {
			jAgeToTextField = new VoLimitedTextField(3,2);
			jAgeToTextField.setText("0");
			jAgeToTextField.setMaximumSize(new Dimension(100, 50));
			ageTo=0;
			jAgeToTextField.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {				
					try {				
						ageTo = Integer.parseInt(jAgeToTextField.getText());
						if ((ageTo<0)||(ageTo>200)) {
							jAgeToTextField.setText("0");
							ageTo = Integer.parseInt(jAgeToTextField.getText());
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patvac.insertvalidage"));
							
						}
						if(ageFrom>ageTo){
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patvac.agefrommustbelowerthanageto"));
							jAgeFromTextField.setText(ageTo.toString());
							ageFrom=ageTo;
						}
					} catch (NumberFormatException ex) {
						jAgeToTextField.setText("0");
						ageTo = Integer.parseInt(jAgeToTextField.getText());
					}
					
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jAgeToTextField;
	}
	
	
	/**
	 * This method initializes getComboVaccineTypes	
	 * 	
	 * @return vaccineTypeComboBox (jComboBox)	
	 */
	
	private JComboBox getComboVaccineTypes() {
		if (vaccineTypeComboBox == null) {
			
			vaccineTypeComboBox = new JComboBox();
			vaccineTypeComboBox.setPreferredSize(new Dimension(200, 30));
			vaccineTypeComboBox.addItem(new VaccineType("", MessageBundle.getMessage("angal.patvac.allvaccinetype")));			
			
			VaccineTypeBrowserManager manager = new VaccineTypeBrowserManager();
			ArrayList<VaccineType> types = null;
			try {
				types = manager.getVaccineType();
			} catch (OHServiceException e1) {
				OHServiceExceptionUtil.showMessages(e1);
			}
			if(types != null){
				for (VaccineType elem : types) {
					vaccineTypeComboBox.addItem(elem);
				}
			}
            
            vaccineTypeComboBox.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					vaccineComboBox.removeAllItems();
					getComboVaccines();
				}
			});	
            
            
		}
		return vaccineTypeComboBox;
	}

	/**
	 * This method initializes comboVaccine.
	 * It used to display available vaccine  
      * 
	 * @return vaccineComboBox (JComboBox)
	 */
	
	private JComboBox getComboVaccines() {
		if (vaccineComboBox == null) {
			vaccineComboBox = new JComboBox();
			vaccineComboBox.setPreferredSize(new Dimension(200, 30));
		}
		VaccineBrowserManager manager = new VaccineBrowserManager();
			
		ArrayList<Vaccine> allVac = null ;
		vaccineComboBox.addItem( new Vaccine ( "", MessageBundle.getMessage("angal.patvac.allvaccine"),new VaccineType ("","")));
        try {
            if (((VaccineType)vaccineTypeComboBox.getSelectedItem()).getDescription().equals(MessageBundle.getMessage("angal.patvac.allvaccinetype"))){
                allVac = manager.getVaccine();
            }else{
                allVac = manager.getVaccine( ((VaccineType)vaccineTypeComboBox.getSelectedItem()).getCode());
            }
        } catch (OHServiceException e) {
            OHServiceExceptionUtil.showMessages(e);
        }

        if(allVac != null) {
            for (Vaccine elem : allVac) {
                vaccineComboBox.addItem(elem);
            }
        }
	    return vaccineComboBox;
	}
	
	/**
	 * This method initializes dateFrom, which is the Panel that contains the
	 * date (From) input for the filtering
	 * 
	 * @return dateFrom (JPanel)
	 */
	
	private JDateChooser getDateFromPanel() {
		if (dateFrom == null) {
			GregorianCalendar now = new GregorianCalendar();
			if (!GeneralData.ENHANCEDSEARCH) now.add(GregorianCalendar.WEEK_OF_YEAR, -1);
			java.util.Date myDate = now.getTime();
			dateFrom = new JDateChooser(myDate, "dd/MM/yy");
			dateFrom.setDate(myDate);
			dateFrom.setLocale(new Locale(GeneralData.LANGUAGE));
			dateFrom.setDateFormatString("dd/MM/yy");
		}
		return dateFrom;
	}

	/**
	 * This method initializes dateTo, which is the Panel that contains the
	 * date (To) input for the filtering
	 * 
	 * @return dateFrom (JPanel)
	 */
	private JDateChooser getDateToPanel() {
		if (dateTo == null) {
			GregorianCalendar now = new GregorianCalendar();
			java.util.Date myDate = now.getTime();
			dateTo = new JDateChooser(myDate, "dd/MM/yy");
			dateTo.setLocale(new Locale(GeneralData.LANGUAGE));
			dateTo.setDateFormatString("dd/MM/yy");
			dateTo.setDate(myDate);
		}
		return dateTo;
	}
	
	/**
	 * This method initializes filterButton, which is the button that perform
	 * the filtering and calls the methods to refresh the Table
	 * 
	 * @return filterButton (JButton)
	 */
	private JButton getFilterButton() {
		if (filterButton == null) {
			filterButton = new JButton(MessageBundle.getMessage("angal.patvac.search"));
			filterButton.setMnemonic(KeyEvent.VK_S);
			filterButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					String vaccineTypeCode = ((VaccineType)vaccineTypeComboBox.getSelectedItem()).getCode();
					String vaccineCode = ((Vaccine)vaccineComboBox.getSelectedItem()).getCode();
	
					if (vaccineTypeComboBox.getSelectedItem().toString().equalsIgnoreCase(MessageBundle.getMessage("angal.patvac.allvaccinetype")))
						vaccineTypeCode = null;
					if (vaccineComboBox.getSelectedItem().toString().equalsIgnoreCase(MessageBundle.getMessage("angal.patvac.allvaccine")))
						vaccineCode = null;
					char sex;
			        if (radiof.isSelected()) {
						sex='F';
					}else{
						if (radiom.isSelected()) {
							sex='M'; 
						}
						else {
							sex='A';
						}		
					}
			        		        
			        if (dateFrom.getDate() == null ) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patvac.pleaseinsertvaliddatefrom"));
						return;
					}
			        
			        if (dateTo.getDate() == null){
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patvac.pleaseinsertvaliddateto"));
						return;
					}
			        
			        GregorianCalendar gcFrom = new GregorianCalendar();
					gcFrom.setTime(dateFrom.getDate());
					GregorianCalendar gcTo = new GregorianCalendar();
				    gcTo.setTime(dateTo.getDate());
				   
				   
			        model = new PatVacBrowsingModel(vaccineTypeCode, vaccineCode, gcFrom, gcTo, sex, ageFrom, ageTo);
					model.fireTableDataChanged();
					jTable.updateUI();
					updateRowCounter();
				}
			});
		}
		return filterButton;
	}
	

	/**
	 * This method initializes jTable, that contains the information about the
	 * patient's vaccines
	 * 
	 * @return jTable (JTable)
	 */
	private JTable getJTable() {
		if (jTable == null) {
			model = new PatVacBrowsingModel();
			jTable = new JTable(model);
			TableColumnModel columnModel = jTable.getColumnModel();
			if (GeneralData.PATIENTVACCINEEXTENDED) {
				columnModel.getColumn(0).setMinWidth(pColumwidth[0]);
				columnModel.getColumn(1).setMinWidth(pColumwidth[1]);
				columnModel.getColumn(2).setMinWidth(pColumwidth[2]);
				columnModel.getColumn(3).setMinWidth(pColumwidth[3]);
				columnModel.getColumn(4).setMinWidth(pColumwidth[4]);
				columnModel.getColumn(5).setMinWidth(pColumwidth[5]);
			} else {
				columnModel.getColumn(0).setMinWidth(pColumwidth[0]);
				columnModel.getColumn(1).setMaxWidth(pColumwidth[2]);
				columnModel.getColumn(2).setMinWidth(pColumwidth[3]);
				columnModel.getColumn(3).setMinWidth(pColumwidth[4]);
				columnModel.getColumn(4).setMinWidth(pColumwidth[5]);
			}
		}
		return jTable;
	}
	
	
	
	/**
	 * This class defines the model for the Table
	 * 
	 * 
	 */
	
	class PatVacBrowsingModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private PatVacManager manager = new PatVacManager();


		public PatVacBrowsingModel() {
			PatVacManager manager = new PatVacManager();
			try {
				lPatVac = manager.getPatientVaccine(!GeneralData.ENHANCEDSEARCH);
			} catch (OHServiceException e) {
				lPatVac = null;
				OHServiceExceptionUtil.showMessages(e);
			}
		}
		
		public PatVacBrowsingModel(String vaccineTypeCode, String vaccineCode,
				GregorianCalendar dateFrom, GregorianCalendar dateTo, char sex,
				int ageFrom, int ageTo) {
			try {
				lPatVac = manager.getPatientVaccine(vaccineTypeCode, vaccineCode, dateFrom, dateTo, sex, ageFrom, ageTo);
			} catch (OHServiceException e) {
				lPatVac = null;
				OHServiceExceptionUtil.showMessages(e);
			}
		}

		public int getRowCount() {
			if (lPatVac == null)
				return 0;
			return lPatVac.size();
		}

		public String getColumnName(int c) {
			return pColums[getNumber(c)];
		}

		public int getColumnCount() {
			int c = 0;
			for (int i = 0; i < columnsVisible.length; i++) {
				if (columnsVisible[i]) {
					c++;
				}
			}
			return c;
		}

		/** 
	     * This method converts a column number in the table
	     * to the right number of the datas.
	     */
	    protected int getNumber(int col) {
	    	// right number to return
	        int n = col;    
	        int i = 0;
	        do {
	            if (!columnsVisible[i]) {
	            	n++;
	            }
	            i++;
	        } while (i < n);
	        // If we are on an invisible column, 
	        // we have to go one step further
	        while (!columnsVisible[n]) {
	        	n++;
	        }
	        return n;
	    }
	    
		/**
		 * Note: We must get the objects in a reversed way because of the query
		 * 
		 * @see org.isf.patvac.service.PatVacIoOperations
		 */
		public Object getValueAt(int r, int c) {
			PatientVaccine patVac = lPatVac.get(r);
			if (c == -1) {
				return patVac;
			} else if (getNumber(c) == 0) {
				return dateFormat.format(patVac.getVaccineDate().getTime());
			} else if (getNumber(c) == 1) {
				return patVac.getPatName();
			} else if (getNumber(c) == 2) {
				return patVac.getPatSex();
			} else if (getNumber(c) == 3) {
				return patVac.getPatAge();
			} else if (getNumber(c) == 4) {
			    return patVac.getVaccine().getDescription();
		    } else if (getNumber(c) == 5) {
			    return patVac.getVaccine().getVaccineType().getDescription();
		    } 
			return null;
		}

		
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	
	}//PatVacBrowsingModel

	public void actionPerformed(ActionEvent e) {
		sexSelect=e.getActionCommand();
		
	}

	/**
	 * 
	 */
	private void updateRowCounter() {
		rowCounter.setText(rowCounterText + lPatVac.size());
	}
}

