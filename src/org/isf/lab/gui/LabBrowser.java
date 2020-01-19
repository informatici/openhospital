package org.isf.lab.gui;

/*------------------------------------------
 * LabBrowser - list all exams
 * -----------------------------------------
 * modification history
 * 02/03/2006 - theo, Davide - first beta version
 * 08/11/2006 - ross - changed button Show into Results
 *                     fixed the exam deletion
 * 					   version is now 1.0 
 * 04/01/2009 - ross - do not use roll, use add(week,-1)!
 *                     roll does not change the year! 
 *------------------------------------------*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.lab.gui.LabEdit.LabEditListener;
import org.isf.lab.gui.LabEditExtended.LabEditExtendedListener;
import org.isf.lab.gui.LabNew.LabListener;
import org.isf.lab.manager.LabManager;
import org.isf.lab.model.Laboratory;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.lab.service.LabIoOperations;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.manager.Context;
import org.isf.patient.model.Patient;
import org.isf.serviceprinting.manager.PrintManager;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.VoDateTextField;

public class LabBrowser extends ModalJFrame implements LabListener, LabEditListener, LabEditExtendedListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void labInserted() {
		jTable.setModel(new LabBrowsingModel());
		
	}
	
	public void labUpdated() {
		filterButton.doClick();
	}
	
	private static final String VERSION=MessageBundle.getMessage("angal.versione");
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	private JPanel jContentPane = null;
	private JPanel jButtonPanel = null;
	private JButton buttonEdit = null;
	private JButton buttonNew = null;
	private JButton buttonDelete = null;
	private JButton buttonClose = null;
	private JButton printTableButton = null;
	private JButton filterButton = null;
	private JPanel jSelectionPanel = null;
	private JTable jTable = null;
	private JComboBox comboExams = null;
	private int pfrmHeight;
	private ArrayList<Laboratory> pLabs;
	private String[] pColums = { 
			MessageBundle.getMessage("angal.common.datem"), 
			MessageBundle.getMessage("angal.lab.patient"), 
			MessageBundle.getMessage("angal.lab.examm"), 
			MessageBundle.getMessage("angal.lab.resultm") 
	};
	private boolean[] columnsResizable = {false, true, true, false};
	private int[] pColumwidth = { 100, 200, 200, 200 };
	private int[] maxWidth = {150, 200, 200, 200};
	private boolean[] columnsVisible = { true, GeneralData.LABEXTENDED, true, true};
	private LabManager labManager = Context.getApplicationContext().getBean(LabManager.class);
	private PrintManager printManager = Context.getApplicationContext().getBean(PrintManager.class);
	private LabBrowsingModel model;
	private Laboratory laboratory;
	private int selectedrow;
	private String typeSelected = null;
	private VoDateTextField dateFrom = null;
	private VoDateTextField dateTo = null;
	private final JFrame myFrame;

	private JPanel jPanelDateFrom;

	private JPanel jPanelDateTo;

	/**
	 * This is the default constructor
	 */
	public LabBrowser() {
		super();
		myFrame = this;
		initialize();
		setResizable(false);
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
		final int pfrmWidth = 14;
		final int pfrmHeight = 12;
		this.setBounds((screensize.width - screensize.width * pfrmWidth
				/ pfrmBase) / 2, (screensize.height - screensize.height
				* pfrmHeight / pfrmBase) / 2, screensize.width * pfrmWidth
				/ pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setContentPane(getJContentPane());
		this.setTitle(MessageBundle.getMessage("angal.lab.laboratorybrowsing")+" ("+VERSION+")");
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
			jContentPane.add(new JScrollPane(getJTable()),
					java.awt.BorderLayout.CENTER);
			validate();
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
			if (MainMenu.checkUserGrants("btnlaboratorynew")) jButtonPanel.add(getButtonNew(), null);
			if (MainMenu.checkUserGrants("btnlaboratoryedit")) jButtonPanel.add(getButtonEdit(), null);
			if (MainMenu.checkUserGrants("btnlaboratorydel")) jButtonPanel.add(getButtonDelete(), null);
			jButtonPanel.add((getPrintTableButton()), null);
			jButtonPanel.add((getCloseButton()), null);
			
		}
		return jButtonPanel;
	}

	private JButton getPrintTableButton() {
		if (printTableButton == null) {
			printTableButton = new JButton(MessageBundle.getMessage("angal.lab.printtable"));
			printTableButton.setMnemonic(KeyEvent.VK_P);
			printTableButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					typeSelected = ((Exam) comboExams.getSelectedItem()).toString();
					if (typeSelected.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.all"))) {
						typeSelected = null;
					}
					
					try {
						ArrayList<LaboratoryForPrint> labs;
						labs = labManager.getLaboratoryForPrint(typeSelected, dateFrom.getDate(), dateTo.getDate());
						if (!labs.isEmpty()) {
							
							printManager.print("Laboratory",labs,0);
						}
					} catch (OHServiceException e) {
						OHServiceExceptionUtil.showMessages(e);
					}
					
				}

			});
		}
		return printTableButton;
	}

	private JButton getButtonEdit() {
		if (buttonEdit == null) {
			buttonEdit = new JButton(MessageBundle.getMessage("angal.common.edit"));
			buttonEdit.setMnemonic(KeyEvent.VK_E);
			buttonEdit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					selectedrow = jTable.getSelectedRow();
					if (selectedrow < 0) {
						JOptionPane.showMessageDialog(null,
								MessageBundle.getMessage("angal.common.pleaseselectarow"), MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} 
					laboratory = (Laboratory) (((LabBrowsingModel) model).getValueAt(selectedrow, -1));
					if (GeneralData.LABEXTENDED) {
						LabEditExtended editrecord = new LabEditExtended(myFrame, laboratory, false);
						editrecord.addLabEditExtendedListener(LabBrowser.this);
						editrecord.setVisible(true);
					} else {
						LabEdit editrecord = new LabEdit(myFrame, laboratory, false);
						editrecord.addLabEditListener(LabBrowser.this);
						editrecord.setVisible(true);
					}
				}
			});
		}
		return buttonEdit;
	}

	/**
	 * This method initializes buttonNew, that loads LabEdit Mask
	 * 
	 * @return buttonNew (JButton)
	 */
	private JButton getButtonNew() {
		if (buttonNew == null) {
			buttonNew = new JButton(MessageBundle.getMessage("angal.common.new"));
			buttonNew.setMnemonic(KeyEvent.VK_N);
			buttonNew.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent event) {
                                laboratory = new Laboratory(0, new Exam("", "",
                                                new ExamType("", ""), 0, ""),
                                                new GregorianCalendar(), "P", "", new Patient(), "");
                                if (GeneralData.LABEXTENDED) {
                                    if (GeneralData.LABMULTIPLEINSERT) {
                                            LabNew editrecord = new LabNew(myFrame);
                                            editrecord.addLabListener(LabBrowser.this);
                                            editrecord.setVisible(true);
                                    } else {
                                            LabEditExtended editrecord = new LabEditExtended(myFrame, laboratory, true);
                                            editrecord.addLabEditExtendedListener(LabBrowser.this);
                                            editrecord.setVisible(true);
                                    }
                                } else {
                                    LabEdit editrecord = new LabEdit(myFrame, laboratory, true);
                                    editrecord.addLabEditListener(LabBrowser.this);
                                    editrecord.setVisible(true);
                                }
                            }
			});
		}
		return buttonNew;
	}

	/**
	 * This method initializes buttonDelete, that delets the selected records
	 * 
	 * @return buttonDelete (JButton)
	 */
	private JButton getButtonDelete() {
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
					} else {
						Laboratory lab = (Laboratory) (((LabBrowsingModel) model)
								.getValueAt(jTable.getSelectedRow(), -1));
						
						int n = JOptionPane.showConfirmDialog(LabBrowser.this,
								getLabMessage(lab),
								MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);

						if (n == JOptionPane.YES_OPTION) {
							boolean deleted;
							
							try {
								deleted = labManager.deleteLaboratory(lab);
							} catch (OHServiceException e) {
								deleted = false;
								OHServiceExceptionUtil.showMessages(e);
							}
							
							if (true == deleted) {
								pLabs.remove(jTable.getSelectedRow());
								model.fireTableDataChanged();
								jTable.updateUI();
							}
						}
					}
				}

			});
		}
		return buttonDelete;
	}

	protected String getLabMessage(Laboratory lab) {
		StringBuilder message = new StringBuilder(MessageBundle.getMessage("angal.lab.deletefollowinglabexam"))
				.append(";\n")
				.append(MessageBundle.getMessage("angal.lab.registationdate")).append("=").append(dateFormat.format(lab.getDate().getTime()))
				.append("\n")
				.append(MessageBundle.getMessage("angal.lab.examdate")).append("=").append(dateTimeFormat.format(lab.getExamDate().getTime()))
				.append("\n")
				.append(MessageBundle.getMessage("angal.lab.exam")).append("=").append(lab.getExam())
				.append("\n")
				.append(MessageBundle.getMessage("angal.lab.patient")).append("=").append(lab.getPatName())
				.append("\n")
				.append(MessageBundle.getMessage("angal.lab.result")).append("=").append(lab.getResult())
				.append("\n ?");
		return message.toString();
	}

	/**
	 * This method initializes buttonClose, that disposes the entire Frame
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
			jSelectionPanel.setPreferredSize(new Dimension(200, pfrmHeight));
			jSelectionPanel.add(new JLabel(MessageBundle.getMessage("angal.lab.selectanexam")));
			jSelectionPanel.add(getComboExams());
			jSelectionPanel.add(getDateFromPanel());
			jSelectionPanel.add(getDateToPanel());
			jSelectionPanel.add(getFilterButton());
		}
		return jSelectionPanel;
	}

	/**
	 * This method initializes jTable, that contains the information about the
	 * Laboratory Tests
	 * 
	 * @return jTable (JTable)
	 */
	private JTable getJTable() {
		if (jTable == null) {
			model = new LabBrowsingModel();
			jTable = new JTable(model);
			TableColumnModel columnModel = jTable.getColumnModel();
			for (int i = 0; i < model.getColumnCount(); i++) {
				jTable.getColumnModel().getColumn(i).setMinWidth(pColumwidth[i]);
				if (!columnsResizable[i]) 
					columnModel.getColumn(i).setMaxWidth(maxWidth[i]);
				if (!columnsVisible[i]) {
					columnModel.getColumn(i).setMaxWidth(0);
					columnModel.getColumn(i).setMinWidth(0);
					columnModel.getColumn(i).setPreferredWidth(0);
				}
			}
		}
		return jTable;
	}

	/**
	 * This method initializes comboExams, that allows to choose which Exam the
	 * user want to display on the Table
	 * 
	 * @return comboExams (JComboBox)
	 */
	private JComboBox getComboExams() {
		ExamBrowsingManager managerExams = Context.getApplicationContext().getBean(ExamBrowsingManager.class);
		if (comboExams == null) {
			comboExams = new JComboBox();
			comboExams.setPreferredSize(new Dimension(200, 30));
			comboExams.addItem(new Exam("", MessageBundle.getMessage("angal.lab.all"), new ExamType("", ""), 0, ""));
			ArrayList<Exam> type;
			try {
				type = managerExams.getExams();
			} catch (OHServiceException e1) {
				type = null;
				OHServiceExceptionUtil.showMessages(e1);
			} // for
			// efficiency
			// in
			// the sequent for
			if (null != type) {
				for (Exam elem : type) {
					comboExams.addItem(elem);
				}
			}
			comboExams.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					typeSelected = ((Exam) comboExams.getSelectedItem())
							.toString();
					if (typeSelected.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.all")))
						typeSelected = null;

				}
			});
		}
		return comboExams;
	}

	private VoDateTextField getDateFieldFromPanel() {
		if (dateFrom == null) {
			GregorianCalendar now = new GregorianCalendar();
			//04/01/2009 - ross - do not use roll, use add(week,-1)!
			//now.roll(GregorianCalendar.WEEK_OF_YEAR, false);
			now.add(GregorianCalendar.WEEK_OF_YEAR, -1);
			dateFrom = new VoDateTextField("dd/mm/yyyy", now, 10);
		}
		return dateFrom;
	}
	
	/**
	 * This method initializes dateFrom, which is the Panel that contains the
	 * date (From) input for the filtering
	 * 
	 * @return dateFrom (JPanel)
	 */
	private JPanel getDateFromPanel() {
		if (jPanelDateFrom == null) {
			jPanelDateFrom = new JPanel();
			jPanelDateFrom.add(new JLabel(MessageBundle.getMessage("angal.common.datem") +" "+ MessageBundle.getMessage("angal.common.from") + ": "), null);
			jPanelDateFrom.add(getDateFieldFromPanel());
			
		}
		return jPanelDateFrom;
	}

	private VoDateTextField getDateFieldToPanel() {
		if (dateTo == null) {
			GregorianCalendar now = new GregorianCalendar();
			dateTo = new VoDateTextField("dd/mm/yyyy", now, 10);
			dateTo.setDate(now);
		}
		return dateTo;
	}
	
	/**
	 * This method initializes dateTo, which is the Panel that contains the date
	 * (To) input for the filtering
	 * 
	 * @return dateTo (JPanel)
	 */
	private JPanel getDateToPanel() {
		if (jPanelDateTo == null) {
			jPanelDateTo = new JPanel();
			jPanelDateTo.add(new JLabel(MessageBundle.getMessage("angal.common.datem") +" "+ MessageBundle.getMessage("angal.common.to") + ": "), null);
			jPanelDateTo.add(getDateFieldToPanel());
			
		}
		return jPanelDateTo;
	}

	/**
	 * This method initializes filterButton, which is the button that perform
	 * the filtering and calls the methods to refresh the Table
	 * 
	 * @return filterButton (JButton)
	 */
	private JButton getFilterButton() {
		if (filterButton == null) {
			filterButton = new JButton(MessageBundle.getMessage("angal.lab.search"));
			filterButton.setMnemonic(KeyEvent.VK_S);
			filterButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					typeSelected = ((Exam) comboExams.getSelectedItem()).toString();
					if (typeSelected.equalsIgnoreCase(MessageBundle.getMessage("angal.lab.all")))
						typeSelected = null;
					model = new LabBrowsingModel(typeSelected, dateFrom.getDate(), dateTo.getDate());
					model.fireTableDataChanged();
					jTable.updateUI();
				}
			});
		}
		return filterButton;
	}

	/**
	 * This class defines the model for the Table
	 * 
	 * @author theo
	 * 
	 */
	class LabBrowsingModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private LabManager manager = Context.getApplicationContext().getBean(LabManager.class,Context.getApplicationContext().getBean(LabIoOperations.class));

		public LabBrowsingModel(String exam, GregorianCalendar dateFrom, GregorianCalendar dateTo) {
			try {
				pLabs = manager.getLaboratory(exam, dateFrom, dateTo);
			} catch (OHServiceException e) {
				pLabs = new ArrayList<Laboratory>();
				OHServiceExceptionUtil.showMessages(e);
			}
		}

		public LabBrowsingModel() {
			try {
				pLabs = manager.getLaboratory();
			} catch (OHServiceException e) {
				pLabs = new ArrayList<Laboratory>();
				OHServiceExceptionUtil.showMessages(e);
			}
		}

		public int getRowCount() {
			if (pLabs == null)
				return 0;
			return pLabs.size();
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
		 * @see org.isf.lab.service.LabIoOperations
		 */
		public Object getValueAt(int r, int c) {
			Laboratory lab = pLabs.get(r);
			if (c == -1) {
				return lab;
			} else if (c == 0) {
				return dateFormat.format(lab.getExamDate().getTime());
			} else if (c == 1) {
				return lab.getPatName();
			} else if (c == 2) {
				return lab.getExam();
			} else if (c == 3) {
				return lab.getResult();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			// return super.isCellEditable(arg0, arg1);
			return false;
		}
	}

	/**
	 * This method updates the Table because a laboratory test has been updated
	 * Sets the focus on the same record as before
	 * 
	 */
	public void laboratoryUpdated() {
		pLabs.set(pLabs.size() - selectedrow - 1, laboratory);
		((LabBrowsingModel) jTable.getModel()).fireTableDataChanged();
		jTable.updateUI();
		if ((jTable.getRowCount() > 0) && selectedrow > -1)
			jTable.setRowSelectionInterval(selectedrow, selectedrow);
	}

	/**
	 * This method updates the Table because a laboratory test has been inserted
	 * Sets the focus on the first record
	 * 
	 */
	public void laboratoryInserted() {
		pLabs.add(pLabs.size(), laboratory);
		((LabBrowsingModel) jTable.getModel()).fireTableDataChanged();
		if (jTable.getRowCount() > 0)
			jTable.setRowSelectionInterval(0, 0);
	}
}
