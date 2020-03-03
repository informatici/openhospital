package org.isf.patient.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.patient.gui.PatientInsert.PatientListener;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.ModalJFrame;



public class PatientBrowser extends ModalJFrame implements PatientListener{
	
	
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] pColums = { MessageBundle.getMessage("angal.patient.namem"), 
			MessageBundle.getMessage("angal.patient.agem"),
			MessageBundle.getMessage("angal.patient.sexm"),
			MessageBundle.getMessage("angal.patient.addressm"),
			MessageBundle.getMessage("angal.patient.citym"),
			MessageBundle.getMessage("angal.patient.telephonem") };
	private JPanel jButtonPanel = null;
	private JPanel jContainPanel = null;
	private JButton jNewButton = null;
	private JButton jEditButton = null;
	private JButton jCloseButton = null;
	private JButton jDeteleButton = null;
	private JTable jTable = null;
	private PatientBrowserModel model;
	private int[] pColumwidth = { 200, 30, 25 ,100, 100, 50 };
	private int selectedrow;
	private Patient patient;
	private PatientBrowserManager manager = Context.getApplicationContext().getBean(PatientBrowserManager.class);
	private ArrayList<Patient> pPat;
	
	
	public JTable getJTable() {
		if (jTable == null) {
			model = new PatientBrowserModel();
			jTable = new JTable(model);
			jTable.getColumnModel().getColumn(0).setMinWidth(pColumwidth[0]);
			jTable.getColumnModel().getColumn(1).setMinWidth(pColumwidth[1]);
			jTable.getColumnModel().getColumn(2).setMinWidth(pColumwidth[2]);
			jTable.getColumnModel().getColumn(2).setMaxWidth(pColumwidth[2]);
			jTable.getColumnModel().getColumn(3).setMinWidth(pColumwidth[3]);
			jTable.getColumnModel().getColumn(3).setMaxWidth(pColumwidth[3]);
			jTable.getColumnModel().getColumn(4).setMinWidth(pColumwidth[4]);
			jTable.getColumnModel().getColumn(5).setMinWidth(pColumwidth[5]);
		}return jTable;
	}
	
	/**
	 * This method initializes 
	 * 
	 */
	public PatientBrowser() {
		super();
		initialize();
		setVisible(true);
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
			jButtonPanel.add(getJDeteleButton(), null);
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
		final int pfrmBase = 10;
        final int pfrmWidth = 6;
        final int pfrmHeight = 5;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2, (screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setTitle(MessageBundle.getMessage("angal.patient.patientbrowser"));
		this.setContentPane(getJContainPanel());
		//pack();	
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
			jContainPanel.add(new JScrollPane(getJTable()),
					java.awt.BorderLayout.CENTER);
			validate();
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
					patient = new Patient();
					PatientInsert newrecord = new PatientInsert(PatientBrowser.this, patient, true);
					newrecord.addPatientListener(PatientBrowser.this);
					newrecord.setVisible(true);
				}
			});
		}
		return jNewButton;
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
						JOptionPane.showMessageDialog(PatientBrowser.this,
								MessageBundle.getMessage("angal.common.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						selectedrow = jTable.getSelectedRow();
						patient = (Patient) (((PatientBrowserModel) model)
								.getValueAt(selectedrow, -1));
						PatientInsert editrecord = new PatientInsert(PatientBrowser.this, patient, false);
						editrecord.addPatientListener(PatientBrowser.this);
						editrecord.setVisible(true);
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
						JOptionPane.showMessageDialog(PatientBrowser.this,
								MessageBundle.getMessage("angal.common.pleaseselectarow"),
								MessageBundle.getMessage("angal.hospital"),
								JOptionPane.PLAIN_MESSAGE);
						return;
					} else {
						Patient pat = (Patient) (((PatientBrowserModel) model)
								.getValueAt(jTable.getSelectedRow(), -1));
						int n = JOptionPane.showConfirmDialog(null,
								MessageBundle.getMessage("angal.patient.deletepatient") + " \" "+pat.getName() + "\" ?",
								MessageBundle.getMessage("angal.hospital"), JOptionPane.YES_NO_OPTION);
						try{
							if ((n == JOptionPane.YES_OPTION)
									&& (manager.deletePatient(pat))) {
								pPat.remove(pPat.size() - jTable.getSelectedRow()
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
	
class PatientBrowserModel extends DefaultTableModel {
		
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public PatientBrowserModel() {
			PatientBrowserManager manager = Context.getApplicationContext().getBean(PatientBrowserManager.class);
			try {
				pPat = manager.getPatient();
			} catch (OHServiceException e) {
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
			
		}
		public int getRowCount() {
			if (pPat == null)
				return 0;
			return pPat.size();
		}
		
		public String getColumnName(int c) {
			return pColums[c];
		}

		public int getColumnCount() {
			return pColums.length;
		}

		
		//{ "NAME", "AGE","SEX","ADDRESS","CITY", "TELEPHONE"};
		public Object getValueAt(int r, int c) {
			if (c == 0) {
				return pPat.get(r).getName();
			} else if (c == -1) {
				return pPat.get(r);
			} else if (c == 1) {
				return pPat.get(r).getAge();
			} else if (c == 2) {
				return pPat.get(r).getSex();
			} else if (c == 3) {
				return pPat.get(r).getAddress();
			} else if (c == 4) {
				return pPat.get(r).getCity();
			} else if (c == 5) {
				return pPat.get(r).getTelephone();
			}
			return null;
		}
		
		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			//return super.isCellEditable(arg0, arg1);
			return false;
		}
	}



public void patientUpdated(AWTEvent e) {
	/*pPat.set(selectedrow, patient);
	System.out.println("riga->" + selectedrow);
	((PatientBrowserModel) jTable.getModel()).fireTableDataChanged();
	jTable.updateUI();*/
	model= new PatientBrowserModel();
	model.fireTableDataChanged();
	jTable.updateUI();
	if ((jTable.getRowCount() > 0) && selectedrow > -1)
		jTable.setRowSelectionInterval(selectedrow, selectedrow);
}

public void patientInserted(AWTEvent e) {
	
	pPat.add(0, patient);
	((PatientBrowserModel) jTable.getModel()).fireTableDataChanged();
	if (jTable.getRowCount() > 0)
		jTable.setRowSelectionInterval(0, 0);
}




	
}
