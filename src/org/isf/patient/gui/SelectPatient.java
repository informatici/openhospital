package org.isf.patient.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.isf.accounting.gui.BillBrowser;
import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.patient.gui.PatientInsertExtended.PatientListener;
//import org.isf.parameters.manager.Param;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.VoLimitedTextField;

public class SelectPatient extends JDialog {
	
//LISTENER INTERFACE --------------------------------------------------------
	private EventListenerList selectionListener = new EventListenerList();
	
	public interface SelectionListener extends EventListener {
		public void patientSelected(Patient patient);
	}
	
	public void addSelectionListener(SelectionListener l) {
		selectionListener.add(SelectionListener.class, l);
		
	}
	
	
	private void fireSelectedPatient(Patient patient) {
		new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = selectionListener.getListeners(SelectionListener.class);
		for (int i = 0; i < listeners.length; i++)
			((SelectionListener)listeners[i]).patientSelected(patient);
			//System.out.println("patient ..............."+patient.getFirstName());
	}
//---------------------------------------------------------------------------	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelButtons;
	private JPanel jPanelTop;
	private JPanel jPanelCenter;
	private JTable jTablePatient;
	private JScrollPane jScrollPaneTablePatient;
	private JButton jButtonCancel;
	private JButton jButtonSelect;
	private JLabel jLabelSearch;
	private JTextField jTextFieldSearchPatient;
	private JButton jSearchButton;
	private JPanel jPanelDataPatient;
	private Patient patient;
	public Patient getPatient() {
		return patient;
	}
	private JButton buttonNew;
	private PatientSummary ps;
	private String[] patColums = {
			MessageBundle.getMessage("angal.common.code"),
			MessageBundle.getMessage("angal.patient.name")
	}; 
	private int[] patColumsWidth = { 100, 250 };
	private boolean[] patColumsResizable = { false, true };

	PatientBrowserManager patManager = new PatientBrowserManager();
	ArrayList<Patient> patArray = new ArrayList<Patient>();
	ArrayList<Patient> patSearch = new ArrayList<Patient>();
	private String lastKey = "";
		
	public SelectPatient(JFrame owner, Patient pat) {
		super(owner, true);
		if (!GeneralData.ENHANCEDSEARCH) {
			try {
				patArray = patManager.getPatientWithHeightAndWeight(null);
			}catch(OHServiceException ex){
				if(ex.getMessages() != null){
					for(OHExceptionMessage msg : ex.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
				patArray = new ArrayList<Patient>();
			}
			patSearch = patArray;
		}
		if (pat == null) {
			patient = null;
		} else
			patient = pat;
		ps = new PatientSummary(patient);
		initComponents();
		addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				//to free memory
				patArray.clear();
				patSearch.clear();
				dispose();
			}			
		});
		setLocationRelativeTo(null);
	}
	
	public SelectPatient(JDialog owner, Patient pat) {
		super(owner, true);
		if (!GeneralData.ENHANCEDSEARCH) {
			try {
				patArray = patManager.getPatientWithHeightAndWeight(null);
			}catch(OHServiceException ex){
				if(ex.getMessages() != null){
					for(OHExceptionMessage msg : ex.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
				patArray = new ArrayList<Patient>();
			}
			patSearch = patArray;
		}
		if (pat == null) {
			patient = null;
		} else
			patient = pat;
		ps = new PatientSummary(patient);
		initComponents();
		addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				//to free memory
				patArray.clear();
				patSearch.clear();
				dispose();
			}			
		});
		setLocationRelativeTo(null);
	}
	
	public SelectPatient(JDialog owner, String search) {
		super(owner, true);
		if (!GeneralData.ENHANCEDSEARCH) {
			try {
				patArray = patManager.getPatientWithHeightAndWeight(null);
			}catch(OHServiceException ex){
				if(ex.getMessages() != null){
					for(OHExceptionMessage msg : ex.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
				patArray = new ArrayList<Patient>();
			}
			patSearch = patArray;
		}
		ps = new PatientSummary(patient);
		initComponents();
		addWindowListener(new WindowAdapter(){
			
			public void windowClosing(WindowEvent e) {
				//to free memory
				patArray.clear();
				patSearch.clear();
				dispose();
			}			
		});
		setLocationRelativeTo(null);
		jTextFieldSearchPatient.setText(search);
		if (GeneralData.ENHANCEDSEARCH) {
			jSearchButton.doClick();
		}
	}
	public SelectPatient(JFrame owner, boolean abbleAddPatient, boolean full) {
		super(owner, true);
		if (!GeneralData.ENHANCEDSEARCH) {
			if(!full)
				patArray = patManager.getPatientHeadWithHeightAndWeight();
			else
				try {
					patArray = patManager.getPatient();
				} catch (OHServiceException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			patSearch = patArray;
		}
		ps = new PatientSummary(patient);
		initComponents();
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				// to free memory
				patArray.clear();
				patSearch.clear();
				dispose();
			}
		});
		setLocationRelativeTo(null);
		buttonNew.setVisible(abbleAddPatient);
	}

	private void initComponents() {
		add(getJPanelTop(), BorderLayout.NORTH);
		add(getJPanelCenter(), BorderLayout.CENTER);
		add(getJPanelButtons(), BorderLayout.SOUTH);
		setTitle(MessageBundle.getMessage("angal.patient.patientselection"));
		pack();
	}

	private JPanel getJPanelDataPatient() {
		if (jPanelDataPatient == null) {
			jPanelDataPatient = ps.getPatientCompleteSummary();
			jPanelDataPatient.setAlignmentY(Box.TOP_ALIGNMENT);
		}
		return jPanelDataPatient;
	}

	private JTextField getJTextFieldSearchPatient() {
		if (jTextFieldSearchPatient == null) {
			jTextFieldSearchPatient = new VoLimitedTextField(100,20);
			jTextFieldSearchPatient.setText("");
			jTextFieldSearchPatient.selectAll();
			if (GeneralData.ENHANCEDSEARCH) {
				jTextFieldSearchPatient.addKeyListener(new KeyListener() {
	
					public void keyPressed(KeyEvent e) {
						int key = e.getKeyCode();
					     if (key == KeyEvent.VK_ENTER) {
					    	 jSearchButton.doClick();
					     }
					}
	
					public void keyReleased(KeyEvent e) {
					}
	
					public void keyTyped(KeyEvent e) {
					}
				});
			} else {
				jTextFieldSearchPatient.addKeyListener(new KeyListener() {
					
					public void keyTyped(KeyEvent e) {
						lastKey = "";
						String s = "" + e.getKeyChar();
						if (Character.isLetterOrDigit(e.getKeyChar())) {
							lastKey = s;
						}
						filterPatient();
					}
	
					public void keyPressed(KeyEvent e) {
					}
	
					public void keyReleased(KeyEvent e) {
					}
				});
			}
		}
		return jTextFieldSearchPatient;
	}

	private void filterPatient() {
		
		String s = jTextFieldSearchPatient.getText() + lastKey;
		s.trim();
		String[] s1 = s.split(" ");
		
		//System.out.println(s);

		patSearch = new ArrayList<Patient>();
		
		for (Patient pat : patArray) {
		
			if (!s.equals("")) {
				String name = pat.getSearchString();
				int a = 0;
				for (int i = 0; i < s1.length ; i++) {
					if (name.contains(s1[i].toLowerCase())) {
						a++;
					}
				}
				if (a == s1.length) patSearch.add(pat);
			} else {
				patSearch.add(pat);
			}
		}
		
		if (jTablePatient.getRowCount() == 0) {
			
			patient = null;
			updatePatientSummary();
		}
		if (jTablePatient.getRowCount() == 1) {
			
			patient = (Patient)jTablePatient.getValueAt(0, -1);
			updatePatientSummary();
		}
		jTablePatient.updateUI();
		jTextFieldSearchPatient.requestFocus();
	}
	
	private JLabel getJLabelSearch() {
		if (jLabelSearch == null) {
			jLabelSearch = new JLabel();
			jLabelSearch.setText(MessageBundle.getMessage("angal.patient.searchpatient"));
		}
		return jLabelSearch;
	}

	private JButton getJButtonSelect() {
		if (jButtonSelect == null) {
			jButtonSelect = new JButton();
			jButtonSelect.setMnemonic(KeyEvent.VK_S);
			jButtonSelect.setText(MessageBundle.getMessage("angal.patient.select"));
			jButtonSelect.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					
					if (patient != null) {
						//to free memory
						patArray.clear();
						patSearch.clear();
						fireSelectedPatient(patient);
						dispose();
					} else return;
				}				
			});
		}
		return jButtonSelect;
	}

	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setMnemonic(KeyEvent.VK_C);
			jButtonCancel.setText(MessageBundle.getMessage("angal.common.cancel"));
			jButtonCancel.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//to free memory
					patArray.clear();
					patSearch.clear();
					dispose();
				}
			});
		}
		return jButtonCancel;
	}

	private JScrollPane getJScrollPaneTablePatient() {
		if (jScrollPaneTablePatient == null) {
			jScrollPaneTablePatient = new JScrollPane();
			jScrollPaneTablePatient.setViewportView(getJTablePatient());
			jScrollPaneTablePatient.setAlignmentY(Box.TOP_ALIGNMENT);
		}
		return jScrollPaneTablePatient;
	}

	private JTable getJTablePatient() {
		if (jTablePatient == null) {
			jTablePatient = new JTable();
			jTablePatient.setModel(new SelectPatientModel());
			jTablePatient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			for (int i = 0 ; i < patColums.length; i++) {
				jTablePatient.getColumnModel().getColumn(i).setMinWidth(patColumsWidth[i]);
				if (!patColumsResizable[i]) jTablePatient.getColumnModel().getColumn(i).setMaxWidth(patColumsWidth[i]);
			}
			jTablePatient.setAutoCreateColumnsFromModel(false);
			jTablePatient.getColumnModel().getColumn(0).setCellRenderer(new CenterTableCellRenderer());
			
			ListSelectionModel listSelectionModel = jTablePatient.getSelectionModel();
			listSelectionModel.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						
						int index = jTablePatient.getSelectedRow();
						patient = (Patient)jTablePatient.getValueAt(index, -1);
						patient.setPhoto(patient.getPhoto());
						updatePatientSummary();
						
					}
				}
			});
			
			jTablePatient.addMouseListener(new MouseListener() {
				
				public void mouseReleased(MouseEvent e) {}
				
				public void mousePressed(MouseEvent e) {}
				
				public void mouseExited(MouseEvent e) {}
				
				public void mouseEntered(MouseEvent e) {}
				
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && !e.isConsumed()) {
						e.consume();
						jButtonSelect.doClick();
					}
				}
			});
		}
		return jTablePatient;
	}

	private void updatePatientSummary() {
		jPanelCenter.remove(jPanelDataPatient);
		ps = new PatientSummary(patient);
		jPanelDataPatient = ps.getPatientCompleteSummary();
		jPanelDataPatient.setAlignmentY(Box.TOP_ALIGNMENT);
		
		jPanelCenter.add(jPanelDataPatient);
		jPanelCenter.validate();
		jPanelCenter.repaint();
	}
	
	private JPanel getJPanelCenter() {
		if (jPanelCenter == null) {
			jPanelCenter = new JPanel();
			jPanelCenter.setLayout(new BoxLayout(jPanelCenter, BoxLayout.X_AXIS));
			jPanelCenter.add(getJScrollPaneTablePatient());
			jPanelCenter.add(getJPanelDataPatient());

			if (patient != null) {
				for (int i = 0; i < patSearch.size(); i++) {
					if (patSearch.get(i).getCode().equals(patient.getCode())) {
						jTablePatient.addRowSelectionInterval(i, i);
						int j = 0;
						if (i > 10) j = i-10; //to center the selected row
						jTablePatient.scrollRectToVisible(jTablePatient.getCellRect(j,i,true));
						break;
					}
				}
			}
		}
		return jPanelCenter;
	}

	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			jPanelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelTop.add(getJLabelSearch());
			jPanelTop.add(getJTextFieldSearchPatient());
			if (MainMenu.checkUserGrants("btnadmnew"))
				jPanelTop.add(getButtonNew());
			if (GeneralData.ENHANCEDSEARCH)
				jPanelTop.add(getJSearchButton());
		}
		return jPanelTop;
	}

	private JButton getJSearchButton() {
		if (jSearchButton == null) {
			jSearchButton = new JButton();
			jSearchButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
			jSearchButton.setPreferredSize(new Dimension(20, 20));
			jSearchButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					try {
						patArray = patManager.getPatientWithHeightAndWeight(jTextFieldSearchPatient.getText());
					}catch(OHServiceException ex){
						if(ex.getMessages() != null){
							for(OHExceptionMessage msg : ex.getMessages()){
								JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
							}
						}
						patArray = new ArrayList<Patient>();
					}
					filterPatient();
				}
			});
		}
		return jSearchButton;
	}
	private JButton getButtonNew() {
		//JButton buttonNew = new JButton(MessageBundle.getMessage("angal.admission.newpatient"));
		buttonNew = new JButton(MessageBundle.getMessage("angal.admission.newpatient"));
		buttonNew.setMnemonic(KeyEvent.VK_N);
		buttonNew.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				if (GeneralData.PATIENTEXTENDED) {
					PatientInsertExtended newrecord = new PatientInsertExtended(SelectPatient.this, new Patient(),
							true);
					newrecord.addPatientListener((PatientListener) SelectPatient.this);
					newrecord.setVisible(true);
				} else {
					PatientInsert newrecord = new PatientInsert(SelectPatient.this, new Patient(), true);
					newrecord.addPatientListener((org.isf.patient.gui.PatientInsert.PatientListener) SelectPatient.this);
					newrecord.setVisible(true);
				}

			}
		});
		return buttonNew;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonSelect());
			jPanelButtons.add(getJButtonCancel());
		}
		return jPanelButtons;
	}

	class SelectPatientModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SelectPatientModel() {
		}

		public int getRowCount() {
			if (patSearch == null)
				return 0;
			return patSearch.size();
		}

		public String getColumnName(int c) {
			return patColums[c];
		}

		public int getColumnCount() {
			return patColums.length;
		}

		public Object getValueAt(int r, int c) {
			Patient patient = patSearch.get(r); 
			if (c == -1) {
				return patient;
			} else if (c == 0) {
				return patient.getCode();
			}  else if (c == 1) {
				return patient.getName();
			} /*else if (c == 2) {
				return patient.getAge();
			} else if (c == 3) {
				return patient.getSex();
			} else if (c == 4) {
				return patient.getCity() + " "
						+ patient.getAddress();
			} */
			return null;
		}

		@Override
		public boolean isCellEditable(int arg0, int arg1) {
			return false;
		}
	}
	
	class CenterTableCellRenderer extends DefaultTableCellRenderer {  
		   
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {  
		   
			Component cell=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			cell.setForeground(Color.BLACK);
			setHorizontalAlignment(CENTER);	   
			return cell;
	   }
	}

	public void setButtonNew(JButton buttonNew) {
		this.buttonNew = buttonNew;
	}
	List<BillBrowser> billBrowserListeners = new ArrayList<BillBrowser>();
	public void addSelectionListener(BillBrowser l) {
		billBrowserListeners.add(l);
	}
}
