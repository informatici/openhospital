package org.isf.opd.gui;

/*------------------------------------------
 * OpdBrowser - list all OPD. let the user select an opd to edit or delete
 * -----------------------------------------
 * modification history
 * 11/12/2005 - Vero, Rick  - first beta version 
 * 07/11/2006 - ross - renamed from Surgery 
 *                   - changed confirm delete message
 * 			         - version is now 1.0 
 *    12/2007 - isf bari - multilanguage version
 * 			         - version is now 1.2 
 * 21/06/2008 - ross - fixed getFilterButton method, need compare to translated string "female" to get correct filter
 *                   - displayed visitdate in the grid instead of opdDate (=system date)
 *                   - fixed "todate" bug (in case of 31/12: 31/12/2008 became 1/1/2008)
 * 			         - version is now 1.2.1 
 * 09/01/2009 - fabrizio - Column full name appears only in OPD extended. Better formatting of OPD date.
 *                         Age column justified to the right. Cosmetic changed to code style.
 * 13/02/2009 - alex - fixed variable visibility in filtering mechanism
 *------------------------------------------*/


import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.Box;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.manager.Context;
import org.isf.opd.manager.OpdBrowserManager;
import org.isf.opd.model.Opd;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.VoLimitedTextField;
import org.isf.utils.time.TimeTools;

public class OpdBrowser extends ModalJFrame implements OpdEdit.SurgeryListener, OpdEditExtended.SurgeryListener {

	private static final long serialVersionUID = 2372745781159245861L;

	private static final String VERSION="1.2.1"; 
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

	private JPanel jButtonPanel = null;
	private JPanel jContainPanel = null;
//	private int pfrmWidth;
	private int pfrmHeight;
	private JButton jNewButton = null;
	private JButton jEditButton = null;
	private JButton jCloseButton = null;
	private JButton jDeteleButton = null;
	private JPanel jSelectionPanel = null;
	private JLabel jLabel = null;
	private JPanel dateFromPanel = null;
	private JPanel dateToPanel = null;
	private JTextField dayFrom = null;
	private JTextField monthFrom = null;
	private JTextField yearFrom = null;
	private JTextField dayTo = null;
	private JTextField monthTo = null;
	private JTextField yearTo = null;
	private JPanel jSelectionDiseasePanel = null;  //  @jve:decl-index=0:visual-constraint="232,358"
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JPanel jAgeFromPanel = null;
	private JLabel jLabel4 = null;
	private VoLimitedTextField jAgeFromTextField = null;
	private JPanel jAgeToPanel = null;
	private JLabel jLabel5 = null;
	private VoLimitedTextField jAgeToTextField = null;
	private JPanel jAgePanel = null;
	private JComboBox jDiseaseTypeBox;
	private JComboBox jDiseaseBox;
	private JPanel sexPanel=null;
	private JPanel newPatientPanel=null;
	private ButtonGroup group=null;
	private ButtonGroup groupNewPatient=null;
	private Integer ageTo = 0;
	private Integer ageFrom = 0;
	private DiseaseType allType= new DiseaseType(
			MessageBundle.getMessage("angal.opd.alltype"),
			MessageBundle.getMessage("angal.opd.alltype"));
	private String[] pColums = { 
			MessageBundle.getMessage("angal.common.datem"), 
			MessageBundle.getMessage("angal.opd.patientid"), 
			MessageBundle.getMessage("angal.opd.fullname"), 
			MessageBundle.getMessage("angal.opd.sexm"), 
			MessageBundle.getMessage("angal.opd.agem"),
			MessageBundle.getMessage("angal.opd.diseasem"),
			MessageBundle.getMessage("angal.opd.diseasetypem"),
			MessageBundle.getMessage("angal.opd.patientstatus")
	};
	private ArrayList<Opd> pSur;
	private JTable jTable = null;
	private OpdBrowsingModel model;
	private int[] pColumwidth = { 70, 70, 150, 30, 30, 195, 195, 50 };
	private boolean[] columnResizable = { false, false, true, false, false, true, true, false };
	private boolean[] columnsVisible = { true, GeneralData.OPDEXTENDED, GeneralData.OPDEXTENDED, true, true, true, true, true };
	private int[] columnsAlignment = { SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.LEFT };
	private boolean[] columnsBold = { false, true, false, false, false, false, false, false };
	private int selectedrow;
	private OpdBrowserManager manager = Context.getApplicationContext().getBean(OpdBrowserManager.class);
	private JButton filterButton = null;
	private String rowCounterText = MessageBundle.getMessage("angal.opd.count") + ": ";
	private JLabel rowCounter = null;
	private JRadioButton radioNew;
	private JRadioButton radioRea;
	private JRadioButton radioAll;
	private final JFrame myFrame;
	private JRadioButton radiom;
	private JRadioButton radiof;
	private JRadioButton radioa;
	
	public JTable getJTable() {
		if (jTable == null) {
			model = new OpdBrowsingModel();
			jTable = new JTable(model);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			TableColumnModel columnModel = jTable.getColumnModel();
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(JLabel.RIGHT);
			for (int i = 0; i < model.getColumnCount(); i++) {
				columnModel.getColumn(i).setMinWidth(pColumwidth[i]);
				columnModel.getColumn(i).setCellRenderer(new AlignmentCellRenderer());
				if (!columnResizable[i])
					columnModel.getColumn(i).setMaxWidth(pColumwidth[i]);
				if (!columnsVisible[i]) {
					columnModel.getColumn(i).setMaxWidth(0);
					columnModel.getColumn(i).setMinWidth(0);
					columnModel.getColumn(i).setPreferredWidth(0);
				}
			}
		}
		return jTable;
	}
	
	class AlignmentCellRenderer extends DefaultTableCellRenderer {  
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			setHorizontalAlignment(columnsAlignment[column]);
			if (columnsBold[column])
				cell.setFont(new Font(null, Font.BOLD, 12));
			return cell;
		}
	}
	
	/**
	 * This method initializes 
	 * 
	 */
	public OpdBrowser() {
		super();
		myFrame=this;
		initialize();
        setVisible(true);
	}
	
	public OpdBrowser(Patient patient) {
		super();
		myFrame=this;
		initialize();
        setVisible(true);
        //if(bOpenEdit)
        Opd newOpd = new Opd(0,' ',-1,new Disease());
        OpdEditExtended editrecord = new OpdEditExtended(myFrame, newOpd, patient, true);
        editrecord.addSurgeryListener(OpdBrowser.this);
		editrecord.setVisible(true);
	}
	
	/**
	 * This method initializes jButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			if (MainMenu.checkUserGrants("btnopdnew")) jButtonPanel.add(getJNewButton(), null);
			if (MainMenu.checkUserGrants("btnopdedit")) jButtonPanel.add(getJEditButton(), null);
			if (MainMenu.checkUserGrants("btnopddel")) jButtonPanel.add(getJDeteleButton(), null);
			jButtonPanel.add(getJCloseButton(), null);
		}
		return jButtonPanel;
	}
	
	/**
	 * This method initializes this
	 * 
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
		this.setTitle(MessageBundle.getMessage("angal.opd.opdoutpatientdepartment")+"("+VERSION+")");
		//this.setSize(new java.awt.Dimension(pfrmWidth,pfrmHeight));
		this.setContentPane(getJContainPanel());
		rowCounter.setText(rowCounterText + pSur.size());
		validate();
		//pack();
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * This method initializes containPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContainPanel() {
		if (jContainPanel == null) {
			jContainPanel = new JPanel();
			jContainPanel.setLayout(new BorderLayout());
			jContainPanel.add(getJButtonPanel(), java.awt.BorderLayout.SOUTH);
			jContainPanel.add(getJSelectionPanel(), java.awt.BorderLayout.WEST);
			jContainPanel.add(new JScrollPane(getJTable()),	java.awt.BorderLayout.CENTER);
			validate();
			//pack();
		}
		return jContainPanel;
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
					Opd newOpd = new Opd(0,' ',-1,new Disease());
					if (GeneralData.OPDEXTENDED) {
						OpdEditExtended newrecord = new OpdEditExtended(myFrame, newOpd, true);
						newrecord.addSurgeryListener(OpdBrowser.this);
						newrecord.setVisible(true);
					} else {
						OpdEdit newrecord = new OpdEdit(myFrame, newOpd, true);
						newrecord.addSurgeryListener(OpdBrowser.this);
						newrecord.setVisible(true);
					}
					
				}
			});
		}
		return jNewButton;
	}
	
	public void NewOpd() {
		jNewButton.doClick();
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
					if (jTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(OpdBrowser.this,
								MessageBundle.getMessage("angal.common.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						selectedrow = jTable.getSelectedRow();
						Opd opd = (Opd)(((OpdBrowsingModel) model).getValueAt(selectedrow, -1));
						if (GeneralData.OPDEXTENDED) {
							OpdEditExtended editrecord = new OpdEditExtended(myFrame, opd, false);
							editrecord.addSurgeryListener(OpdBrowser.this);
							editrecord.setVisible(true);
						} else {
							OpdEdit editrecord = new OpdEdit(myFrame, opd, false);
							editrecord.addSurgeryListener(OpdBrowser.this);
							editrecord.setVisible(true);
						}
					}
				}
			});
		}
		return jEditButton;
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
	 * This method initializes jDeteleButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJDeteleButton() {
		if (jDeteleButton == null) {
			jDeteleButton = new JButton();
			jDeteleButton.setText(MessageBundle.getMessage("angal.common.delete"));
			jDeteleButton.setMnemonic(KeyEvent.VK_D);
			jDeteleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if (jTable.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(OpdBrowser.this,
								MessageBundle.getMessage("angal.common.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						Opd opd = (Opd) (((OpdBrowsingModel) model)
								.getValueAt(jTable.getSelectedRow(), -1));
						String dt="[not specified]";
						try {
							final DateFormat currentDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);
							dt = currentDateFormat.format(opd.getVisitDate().getTime());
						}
						catch (Exception ex){
						}
		
						StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.opd.deletefollowingopd"))
								.append("\n").append(MessageBundle.getMessage("angal.opd.registrationdate"))
								.append("=").append(dateFormat.format(opd.getDate()))
								.append("\n").append(MessageBundle.getMessage("angal.opd.disease"))
								.append(" = ").append(((opd.getDisease().getDescription()==null)? "["+MessageBundle.getMessage("angal.opd.notspecified")+"]": opd.getDisease().getDescription()))
								.append("\n").append(MessageBundle.getMessage("angal.opd.age"))
								.append(" = ").append(opd.getAge())
								.append(", Sex = ").append(opd.getSex())
								.append("\n").append(MessageBundle.getMessage("angal.opd.visitdate"))
								.append(" = ").append(dt).append("?");
						
						int n = JOptionPane.showConfirmDialog(null,
								message.toString(),
								MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);
						try{
							if ((n == JOptionPane.YES_OPTION)
									&& (manager.deleteOpd(opd))) {
								pSur.remove(pSur.size() - jTable.getSelectedRow()
										- 1);
								model.fireTableDataChanged();
								jTable.updateUI();
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
		}
		return jDeteleButton;
	}
	
	/**
	 * This method initializes jSelectionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSelectionPanel() {
		if (jSelectionPanel == null) {
			jLabel3 = new JLabel();
			JPanel sexLabelPanel = new JPanel();
			jLabel3.setText(MessageBundle.getMessage("angal.opd.selectsex"));
			sexLabelPanel.add(jLabel3);
			JPanel newPatientLabelPanel = new JPanel();
			jLabel4 = new JLabel();
			jLabel4.setText(MessageBundle.getMessage("angal.opd.patient"));
			newPatientLabelPanel.add(jLabel4);
			JPanel diseaseLabelPanel = new JPanel();
			jLabel = new JLabel();
			jLabel.setText(MessageBundle.getMessage("angal.opd.selectadisease"));
			diseaseLabelPanel.add(jLabel,null);
			JPanel filterButtonPanel = new JPanel();
			filterButtonPanel.add(getFilterButton());
			jSelectionPanel = new JPanel();
			jSelectionPanel.setLayout(new BoxLayout(getJSelectionPanel(),BoxLayout.Y_AXIS));
			jSelectionPanel.setPreferredSize(new Dimension(300, pfrmHeight));
			jSelectionPanel.add(diseaseLabelPanel,null);
			jSelectionPanel.add(getJSelectionDiseasePanel(),null);
			jSelectionPanel.add(Box.createVerticalGlue(), null);
			jSelectionPanel.add(getDateFromPanel());
			jSelectionPanel.add(getDateToPanel());
			jSelectionPanel.add(Box.createVerticalGlue(), null);
			jSelectionPanel.add(getJAgePanel(), null);
			jSelectionPanel.add(Box.createVerticalGlue(), null);
			jSelectionPanel.add(sexLabelPanel, null);
			jSelectionPanel.add(getSexPanel(), null);
			jSelectionPanel.add(newPatientLabelPanel, null);
			jSelectionPanel.add(getNewPatientPanel(), null);			
			jSelectionPanel.add(filterButtonPanel, null);
			jSelectionPanel.add(getRowCounter(), null);
		}
		return jSelectionPanel;
	}
	
	
	private JLabel getRowCounter() {
		if (rowCounter == null) {
			rowCounter = new JLabel();
			rowCounter.setAlignmentX(Box.CENTER_ALIGNMENT);
		}
		return rowCounter;
	}

	private JPanel getDateFromPanel() {
		if (dateFromPanel == null) {
			dateFromPanel = new JPanel();
			dateFromPanel.add(new JLabel(MessageBundle.getMessage("angal.opd.datefrom")), null);
			dayFrom = new JTextField(2);
			dayFrom.setDocument(new DocumentoLimitato(2));
			dayFrom.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if (dayFrom.getText().length() != 0) {
						if (dayFrom.getText().length() == 1) {
							String typed = dayFrom.getText();
							dayFrom.setText("0" + typed);
						}
						if (!isValidDay(dayFrom.getText()))
							dayFrom.setText("1");
					}
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
			monthFrom = new JTextField(2);
			monthFrom.setDocument(new DocumentoLimitato(2));
			monthFrom.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if (monthFrom.getText().length() != 0) {
						if (monthFrom.getText().length() == 1) {
							String typed = monthFrom.getText();
							monthFrom.setText("0" + typed);
						}
						if (!isValidMonth(monthFrom.getText()))
							monthFrom.setText("1");
					}
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
			yearFrom = new JTextField(4);
			yearFrom.setDocument(new DocumentoLimitato(4));
			yearFrom.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if (yearFrom.getText().length() == 4) {
						if (!isValidYear(yearFrom.getText()))
							yearFrom.setText("2006");
					} else
						yearFrom.setText("2006");
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
			dateFromPanel.add(dayFrom);
			dateFromPanel.add(monthFrom);
			dateFromPanel.add(yearFrom);
			GregorianCalendar now = new GregorianCalendar();
			if (!GeneralData.ENHANCEDSEARCH) now.add(GregorianCalendar.WEEK_OF_YEAR, -1);
			//now.roll(GregorianCalendar.WEEK_OF_YEAR, false);
			dayFrom.setText(String.valueOf(now
					.get(GregorianCalendar.DAY_OF_MONTH)));
			monthFrom.setText(String
					.valueOf(now.get(GregorianCalendar.MONTH) + 1));
			yearFrom.setText(String.valueOf(now.get(GregorianCalendar.YEAR)));
		}
		return dateFromPanel;
	}
	
	public class DocumentoLimitato extends DefaultStyledDocument {
		
		private static final long serialVersionUID = -5098766139884585921L;
		
		private final int NUMERO_MASSIMO_CARATTERI;
		
		public DocumentoLimitato(int numeroMassimoCaratteri) {
			NUMERO_MASSIMO_CARATTERI = numeroMassimoCaratteri;
		}
		
		public void insertString(int off, String text, AttributeSet att)
		throws BadLocationException {
			int numeroCaratteriNelDocumento = getLength();
			int lunghezzaNuovoTesto = text.length();
			if (numeroCaratteriNelDocumento + lunghezzaNuovoTesto > NUMERO_MASSIMO_CARATTERI) {
				int numeroCaratteriInseribili = NUMERO_MASSIMO_CARATTERI
				- numeroCaratteriNelDocumento;
				if (numeroCaratteriInseribili > 0) {
					String parteNuovoTesto = text.substring(0,
							numeroCaratteriInseribili);
					super.insertString(off, parteNuovoTesto, att);
				}
			} else {
				super.insertString(off, text, att);
			}
		}
	}
	
	
	private JPanel getDateToPanel() {
		if (dateToPanel == null) {
			dateToPanel = new JPanel();
			dateToPanel.add(new JLabel(MessageBundle.getMessage("angal.opd.dateto")), null);
			dayTo = new JTextField(2);
			dayTo.setDocument(new DocumentoLimitato(2));
			dayTo.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if (dayTo.getText().length() != 0) {
						if (dayTo.getText().length() == 1) {
							String typed = dayTo.getText();
							dayTo.setText("0" + typed);
						}
						if (!isValidDay(dayTo.getText()))
							dayTo.setText("1");
					}
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
			monthTo = new JTextField(2);
			monthTo.setDocument(new DocumentoLimitato(2));
			monthTo.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if (monthTo.getText().length() != 0) {
						if (monthTo.getText().length() == 1) {
							String typed = monthTo.getText();
							monthTo.setText("0" + typed);
						}
						if (!isValidMonth(monthTo.getText()))
							monthTo.setText("1");
					}
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
			yearTo = new JTextField(4);
			yearTo.setDocument(new DocumentoLimitato(4));
			yearTo.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					if (yearTo.getText().length() == 4) {
						if (!isValidYear(yearTo.getText()))
							yearTo.setText("2006");
					} else
						yearTo.setText("2006");
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
			dateToPanel.add(dayTo);
			dateToPanel.add(monthTo);
			dateToPanel.add(yearTo);
			GregorianCalendar now = new GregorianCalendar();
			dayTo.setText(String.valueOf(now
					.get(GregorianCalendar.DAY_OF_MONTH)));
			monthTo.setText(String
					.valueOf(now.get(GregorianCalendar.MONTH) + 1));
			yearTo.setText(String.valueOf(now.get(GregorianCalendar.YEAR)));
			
		}
		return dateToPanel;
	}
	/**
	 * 
	 * @param day 
	 * 48 == '0'
	 * 57 == '9'
	 * @return
	 */
	private boolean isValidDay(String day) {		
		byte[] typed = day.getBytes();
		if (typed[0] < 48 || typed[0] > 57 || typed[1] < 48 || typed[1] > 57) {
			return false;
		}
		int num = Integer.valueOf(day);
		if (num < 1 || num > 31)
			return false;
		return true;
	}
	
	private boolean isValidMonth(String month) {
		byte[] typed = month.getBytes();
		if (typed[0] < 48 || typed[0] > 57 || typed[1] < 48 || typed[1] > 57) {
			return false;
		}
		int num = Integer.valueOf(month);
		if (num < 1 || num > 12)
			return false;
		return true;
	}
	
	private boolean isValidYear(String year) {
		byte[] typed = year.getBytes();
		if (typed[0] < 48 || typed[0] > 57 || typed[1] < 48 || typed[1] > 57
				|| typed[2] < 48 || typed[2] > 57 || typed[3] < 48
				|| typed[3] > 57) {
			return false;
		}
		return true;
	}
	
	private GregorianCalendar getDateFrom() {
		return new GregorianCalendar(Integer.valueOf(yearFrom.getText()),
									 Integer.valueOf(monthFrom.getText()) - 1, 
									 Integer.valueOf(dayFrom.getText()));
	}
	
	private GregorianCalendar getDateTo() {
		return new GregorianCalendar(Integer.valueOf(yearTo.getText()), 
									 Integer.valueOf(monthTo.getText()) - 1, 
									 Integer.valueOf(dayTo.getText()));
	}

	
	
	
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getDiseaseTypeBox() {
		if (jDiseaseTypeBox == null) {
			jDiseaseTypeBox = new JComboBox();
			jDiseaseTypeBox.setMaximumSize(new Dimension(300,50));
			
			DiseaseTypeBrowserManager manager = Context.getApplicationContext().getBean(DiseaseTypeBrowserManager.class);
			ArrayList<DiseaseType> types = null;
			try {
				types = manager.getDiseaseType();
			}catch(OHServiceException e){
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
			
			jDiseaseTypeBox.addItem(allType);
			if(types != null){
				for (DiseaseType elem : types) {
					jDiseaseTypeBox.addItem(elem);
				}
			}
			
			jDiseaseTypeBox.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					//System.out.println("passato");
					jDiseaseBox.removeAllItems();
					getDiseaseBox();
				}
			});					
		}
		
		return jDiseaseTypeBox;
	}
	
	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getDiseaseBox() {
		if (jDiseaseBox == null) {
			jDiseaseBox = new JComboBox();
			jDiseaseBox.setMaximumSize(new Dimension(300, 50));
			
		};
		DiseaseBrowserManager manager = Context.getApplicationContext().getBean(DiseaseBrowserManager.class);
		ArrayList<Disease> diseases = null;
		try{
			if (((DiseaseType)jDiseaseTypeBox.getSelectedItem()).getDescription().equals(MessageBundle.getMessage("angal.opd.alltype"))){
				diseases = manager.getDiseaseOpd();
			}else{
				diseases = manager.getDiseaseOpd(((DiseaseType)jDiseaseTypeBox.getSelectedItem()).getCode());
			}
		}catch(OHServiceException e){
			if(e.getMessages() != null){
				for(OHExceptionMessage msg : e.getMessages()){
					JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
				}
			}
		}
		Disease allDisease = new Disease(MessageBundle.getMessage("angal.opd.alldisease"), MessageBundle.getMessage("angal.opd.alldisease"), allType);
		jDiseaseBox.addItem(allDisease);
		if(diseases != null){
			for (Disease elem : diseases) {
				jDiseaseBox.addItem(elem);
			}		
		}
		return jDiseaseBox;
	}
	
	/**
	 * This method initializes sexPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getSexPanel() {
		if (sexPanel == null) {
			sexPanel = new JPanel();
			group=new ButtonGroup();
			radiom= new JRadioButton(MessageBundle.getMessage("angal.opd.male"));
			radiof= new JRadioButton(MessageBundle.getMessage("angal.opd.female"));
			radioa= new JRadioButton(MessageBundle.getMessage("angal.opd.all"));
			radioa.setSelected(true);
			group.add(radiom);
			group.add(radiof);
			group.add(radioa);
			sexPanel.add(radioa);
			sexPanel.add(radiom);
			sexPanel.add(radiof);
			
		}
		return sexPanel;
	}
	
	public JPanel getNewPatientPanel() {
		if (newPatientPanel == null) {
			newPatientPanel = new JPanel();
			groupNewPatient=new ButtonGroup();
			radioNew= new JRadioButton(MessageBundle.getMessage("angal.common.new"));
			radioRea= new JRadioButton(MessageBundle.getMessage("angal.opd.reattendance"));
			radioAll= new JRadioButton(MessageBundle.getMessage("angal.opd.all"));
			radioAll.setSelected(true);
			groupNewPatient.add(radioAll);
			groupNewPatient.add(radioNew);
			groupNewPatient.add(radioRea);
			newPatientPanel.add(radioAll);
			newPatientPanel.add(radioNew);
			newPatientPanel.add(radioRea);
		}
		return newPatientPanel;
	}
	
	
	/**
	 * This method initializes jSelectionDiseasePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSelectionDiseasePanel() {
		if (jSelectionDiseasePanel == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("    ");
			jSelectionDiseasePanel = new JPanel();
			jSelectionDiseasePanel.setLayout(new BoxLayout(jSelectionDiseasePanel,BoxLayout.Y_AXIS));
			jSelectionDiseasePanel.add(getDiseaseTypeBox(), null);
			jSelectionDiseasePanel.add(jLabel2, null);
			jSelectionDiseasePanel.add(getDiseaseBox(), null);
		}
		return jSelectionDiseasePanel;
	}
	
	/**
	 * This method initializes jAgePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAgeFromPanel() {
		if (jAgeFromPanel == null) {
			jLabel4 = new JLabel();
			jLabel4.setText(MessageBundle.getMessage("angal.opd.agefrom"));
			jAgeFromPanel = new JPanel();
			jAgeFromPanel.add(jLabel4, null);
			jAgeFromPanel.add(getJAgeFromTextField(), null);
		}
		return jAgeFromPanel;
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
						//ageField.setText(new StringTokenizer(ageField.getText()).nextToken());
						ageFrom = Integer.parseInt(jAgeFromTextField.getText());
						if ((ageFrom<0)||(ageFrom>200)) {
							jAgeFromTextField.setText("");
							JOptionPane.showMessageDialog(OpdBrowser.this, MessageBundle.getMessage("angal.opd.insertvalidage"));
						}
						
					} catch (NumberFormatException ex) {
						jAgeFromTextField.setText("0");
					}
					
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jAgeFromTextField;
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAgeToPanel() {
		if (jAgeToPanel == null) {
			jLabel5 = new JLabel();
			jLabel5.setText(MessageBundle.getMessage("angal.opd.ageto"));
			jAgeToPanel = new JPanel();
			jAgeToPanel.add(jLabel5, null);
			jAgeToPanel.add(getJAgeToTextField(), null);
		}
		return jAgeToPanel;
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
						//ageField.setText(new StringTokenizer(ageField.getText()).nextToken());
						ageTo = Integer.parseInt(jAgeToTextField.getText());
						if ((ageTo<0)||(ageTo>200)) {
							jAgeToTextField.setText("");
							JOptionPane.showMessageDialog(OpdBrowser.this, MessageBundle.getMessage("angal.opd.insertvalidage"));
						}
						
					} catch (NumberFormatException ex) {
						jAgeToTextField.setText("0");
					}
					
				}
				
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jAgeToTextField;
	}

	/**
	 * This method initializes jAgePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJAgePanel() {
		if (jAgePanel == null) {
			jAgePanel = new JPanel();
			jAgePanel.setLayout(new BoxLayout(getJAgePanel(),BoxLayout.Y_AXIS));
			jAgePanel.add(getJAgeFromPanel(), null);
			jAgePanel.add(getJAgeToPanel(), null);
		}
		return jAgePanel;
	}
	
	class OpdBrowsingModel extends DefaultTableModel {
		
		private static final long serialVersionUID = -9129145534999353730L;
		
		public OpdBrowsingModel(String diseaseTypeCode,String diseaseCode, GregorianCalendar dateFrom,GregorianCalendar dateTo,int ageFrom, int ageTo,char sex,char newPatient) {
			try {
				pSur = manager.getOpd(diseaseTypeCode,diseaseCode,dateFrom,dateTo,ageFrom,ageTo,sex,newPatient);
			}catch(OHServiceException e){
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
		}
		
		public OpdBrowsingModel() {
			try {
				pSur = manager.getOpd(!GeneralData.ENHANCEDSEARCH);
			}catch(OHServiceException e){
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
		}
		
		public int getRowCount() {
			if (pSur == null)
				return 0;
			return pSur.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}
		
		public int getColumnCount() {
			return pColums.length;
		}
		
		public Object getValueAt(int r, int c) {
			Opd opd = pSur.get(pSur.size() - r - 1);
			Patient pat = opd.getPatient();
			if (c == -1) {
				return opd;
			} else if (c == 0) {
				String sVisitDate;
				if (opd.getVisitDate() == null) {
					sVisitDate = "";
				} else {
					sVisitDate = dateFormat.format(opd.getVisitDate().getTime());
				}
				return sVisitDate;
			} else if (c == 1) {
				return pat != null ? opd.getPatient().getCode() : null;
			} else if (c == 2) {
				return pat != null ? opd.getFullName() : null;
			} else if (c == 3) {
				return opd.getSex();
			} else if (c == 4) {
				return opd.getAge();
			} else if (c == 5) {
				return opd.getDisease().getDescription();
			} else if (c == 6) {
				return opd.getDisease().getType().getDescription();
			} else if (c == 7) {
				String patientStatus;
				if (opd.getNewPatient() == 'N'){
					patientStatus = MessageBundle.getMessage("angal.common.new");
				} else {
					patientStatus = MessageBundle.getMessage("angal.opd.reattendance");
				}
				return patientStatus;
			}
			
			return null;
		}//"DATE", "PROG YEAR", "SEX", "AGE","DISEASE","DISEASE TYPE"};
		
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
		
	}
	
	
	public void surgeryUpdated(AWTEvent e, Opd opd) {
		pSur.set(pSur.size() - selectedrow - 1, opd);
		//System.out.println("riga->" + selectedrow);
		((OpdBrowsingModel) jTable.getModel()).fireTableDataChanged();
		jTable.updateUI();
		if ((jTable.getRowCount() > 0) && selectedrow > -1)
			jTable.setRowSelectionInterval(selectedrow, selectedrow);
		rowCounter.setText(rowCounterText + pSur.size());
	}
	
	public void surgeryInserted(AWTEvent e, Opd opd) {
		pSur.add(pSur.size(), opd);
		((OpdBrowsingModel) jTable.getModel()).fireTableDataChanged();
		if (jTable.getRowCount() > 0)
			jTable.setRowSelectionInterval(0, 0);
		rowCounter.setText(rowCounterText + pSur.size());
	}
	
	private JButton getFilterButton() {
		if (filterButton == null) {
			filterButton = new JButton(MessageBundle.getMessage("angal.opd.search"));
            filterButton.setMnemonic(KeyEvent.VK_S);
			filterButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					String disease=((Disease)jDiseaseBox.getSelectedItem()).getCode();
					String diseasetype=((DiseaseType)jDiseaseTypeBox.getSelectedItem()).getCode();
					
					char sex;
					if (radioa.isSelected()) sex='A';
					else if (radiom.isSelected()) sex='M';
					else sex='F';
					
					char newPatient;
					if(radioAll.isSelected()) newPatient='A';
					else if(radioNew.isSelected()) newPatient='N';
					else newPatient='R';
					
					GregorianCalendar dateFrom = getDateFrom();
					GregorianCalendar dateTo = getDateTo();
					
					if(dateFrom.after(dateTo)){
						JOptionPane.showMessageDialog(OpdBrowser.this, MessageBundle.getMessage("angal.opd.datefrommustbebefordateto"));
						return;
					}
					
					if(ageFrom>ageTo){
						JOptionPane.showMessageDialog(OpdBrowser.this, MessageBundle.getMessage("angal.opd.agefrommustbelowerthanageto"));
						jAgeFromTextField.setText(ageTo.toString());
						ageFrom=ageTo;
						return;
					}
					
					//TODO: to retrieve resultset size instead of assuming 1 year as limit for the warning
					if (TimeTools.getDaysBetweenDates(dateFrom, dateTo, true) >= 360) {
						int ok = JOptionPane.showConfirmDialog(OpdBrowser.this, 
								MessageBundle.getMessage("angal.common.thiscouldretrievealargeamountofdataproceed"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.OK_CANCEL_OPTION);
						if (ok != JOptionPane.OK_OPTION) return;
					}
					
					model = new OpdBrowsingModel(diseasetype,disease,getDateFrom(), getDateTo(),ageFrom,ageTo,sex,newPatient);
					model.fireTableDataChanged();
					jTable.updateUI();
					rowCounter.setText(rowCounterText + pSur.size());
				}
				
			});
		}
		return filterButton;
	}
	
} 
