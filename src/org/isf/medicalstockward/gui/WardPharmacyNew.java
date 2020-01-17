package org.isf.medicalstockward.gui;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.menu.manager.Context;
import org.isf.patient.gui.SelectPatient;
import org.isf.patient.gui.SelectPatient.SelectionListener;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.isf.ward.model.Ward;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WardPharmacyNew extends JDialog implements SelectionListener {

//LISTENER INTERFACE --------------------------------------------------------
    private EventListenerList movementWardListeners = new EventListenerList();
	
	public interface MovementWardListeners extends EventListener {
		public void movementUpdated(AWTEvent e);
		public void movementInserted(AWTEvent e);
	}
	
	public void addMovementWardListener(MovementWardListeners l) {
		movementWardListeners.add(MovementWardListeners.class, l);
	}
	
	public void removeMovementWardListener(MovementWardListeners listener) {
		movementWardListeners.remove(MovementWardListeners.class, listener);
	}
	
	private void fireMovementWardInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = movementWardListeners.getListeners(MovementWardListeners.class);
		for (int i = 0; i < listeners.length; i++)
			((MovementWardListeners)listeners[i]).movementInserted(event);
	}
	/*private void fireMovementWardUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = movementWardListeners.getListeners(MovementWardListeners.class);
		for (int i = 0; i < listeners.length; i++)
			((MovementWardListeners)listeners[i]).movementUpdated(event);
	}*/
//---------------------------------------------------------------------------
	
	public void patientSelected(Patient patient) {
		patientSelected = patient;
		jTextFieldPatient.setText(patientSelected.getName());
		jTextFieldPatient.setEditable(false);
		jButtonPickPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.changepatient")); //$NON-NLS-1$
		jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.changethepatientassociatedwiththismovement")); //$NON-NLS-1$
		jButtonTrashPatient.setEnabled(true);
//		if (patientSelected.getWeight() == 0) {
//			JOptionPane.showMessageDialog(WardPharmacyNew.this, MessageBundle.getMessage("angal.medicalstockwardedit.theselectedpatienthasnoweightdefined"));
//		}
	}
	
	private static final long serialVersionUID = 1L;
	private JLabel jLabelPatient;
	private JTextField jTextFieldPatient;
	private JButton jButtonPickPatient;
	private JButton jButtonTrashPatient;
	private JPanel jPanelPatient;
	private JPanel jPanelMedicals;
	private JPanel jPanelButtons;
	private JPanel jPanelNorth;
	private JPanel jPanelUse;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private JRadioButton jRadioPatient;
	private JTable jTableMedicals;
	private JScrollPane jScrollPaneMedicals;
	private JPanel jPanelMedicalsButtons;
	private JButton jButtonAddMedical;
	private JButton jButtonRemoveMedical;
	private static final Dimension PatientDimension = new Dimension(300,20);

	private Patient patientSelected = null;
	private Ward wardSelected;
	private Object[] medClasses = {Medical.class, Integer.class};
	private String[] medColumnNames = {MessageBundle.getMessage("angal.medicalstockward.medical"), 
									   MessageBundle.getMessage("angal.common.quantity")};
	private Integer[] medWidth = {200, 150};
	private boolean[] medResizable = {true, false};
	
	//Medicals (ALL)
	//MedicalBrowsingManager medManager = new MedicalBrowsingManager();
	//ArrayList<Medical> medArray = medManager.getMedicals();

	//Medicals (in WARD)
	//ArrayList<MedItem> medItems = new ArrayList<MedItem>();
	private ArrayList<Medical> medArray = new ArrayList<Medical>();
	private ArrayList<Double> qtyArray = new ArrayList<Double>(); 
	private ArrayList<MedicalWard> wardDrugs = null;
	private ArrayList<MedicalWard> medItems = new ArrayList<MedicalWard>();
	private JRadioButton jRadioUse;
	private JTextField jTextFieldUse;
	private JLabel jLabelUse;

        private JRadioButton jRadioWard;
	private JComboBox wardBox;
	private JPanel panelWard;
        /*
         *Adds to facilitate the selection of products 
         */
        private JPanel searchPanel;
        private JTextField searchTextField;
        private JButton searchButton;
        private JComboBox jComboBoxMedicals;
        //private JLabel jLabelSelectWard;

		private MovWardBrowserManager wardManager = Context.getApplicationContext().getBean(MovWardBrowserManager.class);

	public WardPharmacyNew(JFrame owner, Ward ward, ArrayList<MedicalWard> drugs) {
		super(owner, true);
		wardDrugs = drugs;
		for (MedicalWard elem : wardDrugs) {
			try {
				medArray.add(elem.getMedical());
			} catch (OHException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			qtyArray.add(elem.getQty());
		}
		wardSelected = ward;
		initComponents();
	}

	private void initComponents() {
		add(getJPanelButtons(), BorderLayout.SOUTH);
		add(getJPanelMedicals(), BorderLayout.CENTER);
		add(getJPanelNorth(), BorderLayout.NORTH);
		setDefaultCloseOperation(WardPharmacyNew.DISPOSE_ON_CLOSE);
		setTitle(MessageBundle.getMessage("angal.medicalstockwardedit.title"));
		pack();
		setLocationRelativeTo(null);
	}

	private JPanel getJPanelNorth() {
		if (jPanelNorth == null) {
			jPanelNorth = new JPanel();
			jPanelNorth.setLayout(new BoxLayout(jPanelNorth, BoxLayout.Y_AXIS));
			jPanelNorth.add(getJPanelPatient());
			jPanelNorth.add(getJPanelUse());
                        jPanelNorth.add(getPanelWard());
			ButtonGroup group = new ButtonGroup();
			group.add(jRadioPatient);
			group.add(jRadioUse);
                        group.add(jRadioWard);
		}
		return jPanelNorth;
	}

	private JPanel getJPanelUse() {
		if (jPanelUse == null) {
			jPanelUse = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelUse.add(getJRadioUse());
			jPanelUse.add(getJLabelUse());
			jPanelUse.add(getJTextFieldUse());
		}
		return jPanelUse;
	}

	private JLabel getJLabelUse() {
		if (jLabelUse == null) {
			jLabelUse = new JLabel();
			jLabelUse.setText(MessageBundle.getMessage("angal.medicalstockwardedit.internaluse"));
		}
		return jLabelUse;
	}

	private JTextField getJTextFieldUse() {
		if (jTextFieldUse == null) {
			jTextFieldUse = new JTextField();
			jTextFieldUse.setText(MessageBundle.getMessage("angal.medicalstockwardedit.internaluse").toUpperCase()); //$NON-NLS-1$
			jTextFieldUse.setPreferredSize(PatientDimension);
			jTextFieldUse.setEnabled(false);
		}
		return jTextFieldUse;
	}

	private JRadioButton getJRadioUse() {
		if (jRadioUse == null) {
			jRadioUse = new JRadioButton();
			jRadioUse.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					jTextFieldPatient.setEnabled(false);
					jButtonPickPatient.setEnabled(false);
					jButtonTrashPatient.setEnabled(false);
					jTextFieldUse.setEnabled(true);
                                        wardBox.setEnabled(false);
				}
			});
		}
		return jRadioUse;
	}

	private JButton getJButtonAddMedical() {
		if (jButtonAddMedical == null) {
			jButtonAddMedical = new JButton();
			jButtonAddMedical.setText(MessageBundle.getMessage("angal.medicalstockwardedit.medical")); //$NON-NLS-1$
			jButtonAddMedical.setMnemonic(KeyEvent.VK_M);
			jButtonAddMedical.setIcon(new ImageIcon("rsc/icons/plus_button.png")); //$NON-NLS-1$
			jButtonAddMedical.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ArrayList<Medical> currentMeds = new ArrayList<Medical>();

					// remove already inserted items
					for (MedicalWard medItem : medItems) {
						Medical med = null;
						try {
							med = medItem.getMedical();
						} catch (OHException e1) {
							e1.printStackTrace();
						}
						currentMeds.add(med);
					}
					
//					Icon icon = new ImageIcon("rsc/icons/medical_dialog.png"); //$NON-NLS-1$
//					Medical med = (Medical)JOptionPane.showInputDialog(
//					                    WardPharmacyNew.this,
//					                    "TestTest Test", //$NON-NLS-1$
//					                    MessageBundle.getMessage("angal.medicalstockwardedit.medical"), //$NON-NLS-1$
//					                    JOptionPane.PLAIN_MESSAGE,
//					                    icon,
//					                    currentMeds.toArray(),
//					                    ""); //$NON-NLS-1$
	                Medical med = null;
	                if (jComboBoxMedicals.getSelectedItem() instanceof Medical) {
	                        med = (Medical) jComboBoxMedicals.getSelectedItem();
	                }
	               	if(currentMeds.contains(med)) {
	               		JOptionPane.showMessageDialog(WardPharmacyNew.this, 
							MessageBundle.getMessage("angal.medicalstockwardedit.productalreadyinserted"), //$NON-NLS-1$
							MessageBundle.getMessage("angal.medicalstockwardedit.invalidproduct"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
	               		return;
	               	}
					if (med != null) {
						int index = medArray.indexOf(med);
						Double startQty = 0.;
						Double minQty = 0.;
						Double maxQty = qtyArray.get(index);
						Double stepQty = 0.5;
						JSpinner jSpinnerQty = new JSpinner(new SpinnerNumberModel(startQty,minQty,null,stepQty));
						
						StringBuilder messageBld = new StringBuilder(med.getDescription()).append("\n");
						messageBld.append(MessageBundle.getMessage("angal.medicalstockwardedit.insertquantitypiecesormls")).append("\n");
						messageBld.append(MessageBundle.getMessage("angal.medicalstockwardedit.instock")).append(": ").append(maxQty);
						
						int r = JOptionPane.showConfirmDialog(WardPharmacyNew.this, 
								new Object[] { messageBld.toString(), jSpinnerQty },
								MessageBundle.getMessage("angal.common.quantity"),
				        		JOptionPane.OK_CANCEL_OPTION, 
				        		JOptionPane.PLAIN_MESSAGE);
						
						if (r == JOptionPane.OK_OPTION) {
							try {
								Double qty = (Double) jSpinnerQty.getValue();
								if (qty > maxQty) {
									JOptionPane.showMessageDialog(WardPharmacyNew.this, 
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantitypleaseinsertmax") + " " + maxQty, //$NON-NLS-1$
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantity"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
									return;
								}
								double roundedQty = round(qty, stepQty);
								if (roundedQty >= stepQty)
									addItem(med, roundedQty);
								else
									JOptionPane.showMessageDialog(WardPharmacyNew.this, 
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantitypleaseinsertatleast") + " " + stepQty, //$NON-NLS-1$
											MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantity"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
							} catch (Exception eee) {
								JOptionPane.showMessageDialog(WardPharmacyNew.this, 
										MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantitypleasetryagain"), //$NON-NLS-1$
										MessageBundle.getMessage("angal.medicalstockwardedit.invalidquantity"), //$NON-NLS-1$
										JOptionPane.ERROR_MESSAGE);
							}
						} else return;
					}
				}
			});
		}
		return jButtonAddMedical;
	}
	
	public double round(double input, double step) {
		return Math.round(input / step) * step;
	}
	
	private JButton getJButtonRemoveMedical() {
		if (jButtonRemoveMedical == null) {
			jButtonRemoveMedical = new JButton();
			jButtonRemoveMedical.setText(MessageBundle.getMessage("angal.medicalstockwardedit.removeitem")); //$NON-NLS-1$
			jButtonRemoveMedical.setIcon(new ImageIcon("rsc/icons/delete_button.png")); //$NON-NLS-1$
			jButtonRemoveMedical.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (jTableMedicals.getSelectedRow() < 0) { 
						JOptionPane.showMessageDialog(WardPharmacyNew.this,
								MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectanitem"), //$NON-NLS-1$
								"Error", //$NON-NLS-1$
								JOptionPane.WARNING_MESSAGE);
					} else {
						removeItem(jTableMedicals.getSelectedRow());
					}
				}
			});
		}
		return jButtonRemoveMedical;
	}

	private void addItem(Medical med, Double qty) {
		if (med != null) {
			
			MedicalWard item = new MedicalWard(med, qty);
			medItems.add(item);
			medArray.add(med);
			qtyArray.add(qty);
			jTableMedicals.updateUI();
		}
	}
	
	private void removeItem(int row) {
		if (row != -1) {
			medItems.remove(row);
			medArray.remove(row);
			qtyArray.remove(row);
			jTableMedicals.updateUI();
		}
		
	}
	
	private JPanel getJPanelMedicalsButtons() {
		if (jPanelMedicalsButtons == null) {
			jPanelMedicalsButtons = new JPanel();
                        jPanelMedicalsButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
			//jPanelMedicalsButtons.setLayout(new BoxLayout(jPanelMedicalsButtons, BoxLayout.Y_AXIS));
			//jPanelMedicalsButtons.add(getJButtonAddMedical());
			jPanelMedicalsButtons.add(getJButtonRemoveMedical());
		}
		return jPanelMedicalsButtons;
	}

	private JScrollPane getJScrollPaneMedicals() {
		if (jScrollPaneMedicals == null) {
			jScrollPaneMedicals = new JScrollPane();
			jScrollPaneMedicals.setViewportView(getJTableMedicals());
		}
		return jScrollPaneMedicals;
	}

	private JTable getJTableMedicals() {
		if (jTableMedicals == null) {
			jTableMedicals = new JTable();
			jTableMedicals.setModel(new MedicalTableModel());
			for (int i = 0; i < medWidth.length; i++) {
				jTableMedicals.getColumnModel().getColumn(i).setMinWidth(medWidth[i]);
				if (!medResizable[i]) jTableMedicals.getColumnModel().getColumn(i).setMaxWidth(medWidth[i]);
			}
		}
		return jTableMedicals;
	}

	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText(MessageBundle.getMessage("angal.common.ok")); //$NON-NLS-1$
			jButtonOK.setMnemonic(KeyEvent.VK_O);
			jButtonOK.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					boolean isPatient;
					String description = "";
					int age = 0;
					float weight = 0;
					GregorianCalendar newDate = new GregorianCalendar();
					Ward wardTo = null; //
					if (jRadioPatient.isSelected()) {
						isPatient = true;
						if (patientSelected != null) {
							description = patientSelected.getName();
							age = patientSelected.getAge();
							weight = patientSelected.getWeight();
						}
					} 
                    else if (jRadioWard.isSelected()) {
						Object selectedObj = wardBox.getSelectedItem();
						if(selectedObj instanceof Ward){
							wardTo = (Ward) selectedObj;
						}
						else{
							JOptionPane.showMessageDialog(null,
									MessageBundle.getMessage("angal.medicalstock.multipledischarging.pleaseselectaward"));
							return;
						}
                        description = wardTo.getDescription();
						isPatient = false;
					} 
                    else {
						isPatient = false;
						description = jTextFieldUse.getText();
					}
					
//					ArrayList<MovementWard> manyMovementWard = new ArrayList<MovementWard>();
//					for (int i = 0; i < medItems.size(); i++) {
//						try {
//							manyMovementWard.add(new MovementWard(
//									wardSelected,
//									newDate,
//									isPatient,
//									patientSelected,
//									age,
//									weight,
//									description,
//									medItems.get(i).getMedical(),
//									medItems.get(i).getQty(),
//									MessageBundle.getMessage("angal.medicalstockwardedit.pieces"),
//                                                                        wardTo));
//						} catch (OHException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					}
//					MovWardBrowserManager wardManager = new MovWardBrowserManager();
//					boolean result;
//					try {
//						result = wardManager.newMovementWard(manyMovementWard);
//						if (result) {
//							fireMovementWardInserted();
//							dispose();
//						} else
//							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
//					} catch (OHServiceException e1) {
//						result = false;
//						OHServiceExceptionUtil.showMessages(e1);
//					}

                    // innit of the datas needed to store the movement
					//ArrayList<Movement> movements = new ArrayList<Movement>();
					//Lot aLot = new Lot("", newDate, newDate);
					//String refNo = "";
                                        
                    ArrayList<MovementWard> manyMovementWard = new ArrayList<MovementWard>();
                    //MovStockInsertingManager movManager = new MovStockInsertingManager();
                    boolean result;
					try {
						// MovementType typeCharge = new
						// MedicaldsrstockmovTypeBrowserManager().getMovementType("charge");
						for (int i = 0; i < medItems.size(); i++) {
							manyMovementWard.add(new MovementWard(wardSelected, newDate, isPatient, patientSelected,
									age, weight, description, medItems.get(i).getMedical(), medItems.get(i).getQty(),
									MessageBundle.getMessage("angal.medicalstockwardedit.pieces"), wardTo, null));
						}

						result = wardManager.newMovementWard(manyMovementWard);
					} catch (OHServiceException ex) {
                        result = false;
                    } catch (OHException ex) {
                        result = false;
                    }
					if (result) {
                        fireMovementWardInserted();
                        dispose();
					} else {
                        JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
                    
                    }	
				}
			});
		}
		return jButtonOK;
	}
	
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(MessageBundle.getMessage("angal.common.cancel")); //$NON-NLS-1$
			jButtonCancel.setMnemonic(KeyEvent.VK_C);
			jButtonCancel.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
						dispose();
				}
			});
		}
		return jButtonCancel;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonOK());
			jPanelButtons.add(getJButtonCancel());
		}
		return jPanelButtons;
	}

	private JPanel getJPanelMedicals() {
		if (jPanelMedicals == null) {
			jPanelMedicals = new JPanel();
			jPanelMedicals.setLayout(new BoxLayout(jPanelMedicals, BoxLayout.Y_AXIS));
                        jPanelMedicals.add(getJPanelMedicalsSearch());
			jPanelMedicals.add(getJScrollPaneMedicals());
			jPanelMedicals.add(getJPanelMedicalsButtons());
		}
		return jPanelMedicals;
	}

	private JPanel getJPanelPatient() {
		if (jPanelPatient == null) {
			jPanelPatient = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelPatient.add(getJRadioPatient());
			jPanelPatient.add(getJLabelPatient());
			jPanelPatient.add(getJTextFieldPatient());
			jPanelPatient.add(getJButtonPickPatient());
			jPanelPatient.add(getJButtonTrashPatient());
		}
		return jPanelPatient;
	}

	private JRadioButton getJRadioPatient() {
		if (jRadioPatient == null) {
			jRadioPatient = new JRadioButton();
			jRadioPatient.setSelected(true);
			jRadioPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					System.out.println("in jRadioPatient: " + e.getID());
					jTextFieldUse.setEnabled(false);
					jTextFieldPatient.setEnabled(true);
					jButtonPickPatient.setEnabled(true);
                                        wardBox.setEnabled(false);
					if (patientSelected != null) jButtonTrashPatient.setEnabled(true);
					
				}
			});
		}
		return jRadioPatient;
	}

	private JButton getJButtonTrashPatient() {
		if (jButtonTrashPatient == null) {
			jButtonTrashPatient = new JButton();
			jButtonTrashPatient.setMnemonic(KeyEvent.VK_R);
			jButtonTrashPatient.setPreferredSize(new Dimension(25,25));
			jButtonTrashPatient.setIcon(new ImageIcon("rsc/icons/remove_patient_button.png")); //$NON-NLS-1$
			jButtonTrashPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.tooltip.removepatientassociationwiththismovement")); //$NON-NLS-1$
			jButtonTrashPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
					patientSelected = null;
					jTextFieldPatient.setText(""); //$NON-NLS-1$
					jTextFieldPatient.setEditable(true);
					jButtonPickPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.pickpatient"));
					jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.tooltip.associateapatientwiththismovement")); //$NON-NLS-1$
					jButtonTrashPatient.setEnabled(false);
				}
			});
			jButtonTrashPatient.setEnabled(false);
		}
		return jButtonTrashPatient;
	}

	private JButton getJButtonPickPatient() {
		if (jButtonPickPatient == null) {
			jButtonPickPatient = new JButton();
			jButtonPickPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.pickpatient"));
			jButtonPickPatient.setMnemonic(KeyEvent.VK_P);
			jButtonPickPatient.setIcon(new ImageIcon("rsc/icons/pick_patient_button.png")); //$NON-NLS-1$
			jButtonPickPatient.setToolTipText(MessageBundle.getMessage("angal.medicalstockwardedit.tooltip.associateapatientwiththismovement"));
			jButtonPickPatient.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					SelectPatient sp = new SelectPatient(WardPharmacyNew.this, patientSelected);
					sp.addSelectionListener(WardPharmacyNew.this);
					sp.pack();
					sp.setVisible(true);
				}
			});
		}
		return jButtonPickPatient;
	}

	private JTextField getJTextFieldPatient() {
		if (jTextFieldPatient == null) {
			jTextFieldPatient = new JTextField();
			jTextFieldPatient.setText(""); //$NON-NLS-1$
			jTextFieldPatient.setPreferredSize(PatientDimension);
			//Font patientFont=new Font(jTextFieldPatient.getFont().getName(), Font.BOLD, jTextFieldPatient.getFont().getSize() + 4);
			//jTextFieldPatient.setFont(patientFont);
		}
		return jTextFieldPatient;
	}

	private JLabel getJLabelPatient() {
		if (jLabelPatient == null) {
			jLabelPatient = new JLabel();
			jLabelPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.patient"));
		}
		return jLabelPatient;
	}
	
	public class MedicalTableModel implements TableModel {
		
		public MedicalTableModel() {
			
		}
		
		public Class<?> getColumnClass(int i) {
			return medClasses[i].getClass();
		}

		
		public int getColumnCount() {
			return medClasses.length;
		}
		
		public int getRowCount() {
			if (medItems == null)
				return 0;
			return medItems.size();
		}
		
		public Object getValueAt(int r, int c) {
			if (c == -1) {
				return medItems.get(r);
			}
			if (c == 0) {
				try {
					return medItems.get(r).getMedical().getDescription();
				} catch (OHException e) {
					return null;
				}
			}
			if (c == 1) {
				return medItems.get(r).getQty(); 
			}
			return null;
		}
		
		public boolean isCellEditable(int r, int c) {
			if (c == 1) return true;
			return false;
		}
		
		public void setValueAt(Object item, int r, int c) {
			//if (c == 1) billItems.get(r).setItemQuantity((Integer)item);

		}

		public void addTableModelListener(TableModelListener l) {
			
		}

		public String getColumnName(int columnIndex) {
			return medColumnNames[columnIndex];
		}

		public void removeTableModelListener(TableModelListener l) {
		}

	}
        
        private JPanel getPanelWard() {
		if (panelWard == null) {
			panelWard = new JPanel();
			panelWard.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			panelWard.add(getJRadioWard());
			//panelWard.add(getJLabelSelectWard());
			panelWard.add(getWardBox());
		}
		return panelWard;
	}

	private JRadioButton getJRadioWard() {
		if (jRadioWard == null) {
			jRadioWard = new JRadioButton(MessageBundle.getMessage("angal.wardpharmacynew.ward"));
			jRadioWard.setSelected(false);
			jRadioWard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jTextFieldUse.setEnabled(false);
					jTextFieldPatient.setEnabled(false);
					jButtonPickPatient.setEnabled(false);
					wardBox.setEnabled(true);
				}
			});
			jRadioWard.setMinimumSize(new Dimension(55, 22));
			jRadioWard.setMaximumSize(new Dimension(55, 22));
		}
		return jRadioWard;
	}

	private JComboBox getWardBox() {
		if (wardBox == null) {
			wardBox = new JComboBox();
			wardBox.setPreferredSize(new Dimension(300, 30));
			org.isf.ward.manager.WardBrowserManager wbm = new org.isf.ward.manager.WardBrowserManager();
			ArrayList<Ward> wardList = null;
                        try {
                            wardList = wbm.getWards();
                        } catch (OHServiceException ex) {
                            Logger.getLogger(WardPharmacyNew.class.getName()).log(Level.SEVERE, null, ex);
                        }
			wardBox.addItem("");
			if(wardList != null) {
                            for (org.isf.ward.model.Ward elem : wardList) {
				if (!wardSelected.getCode().equals(elem.getCode()))
					wardBox.addItem(elem);
                            }
                        }
			wardBox.setEnabled(false);
		}
		return wardBox;
	}
        
        private JPanel getJPanelMedicalsSearch() {
            searchButton = new JButton();
            searchButton.setPreferredSize(new Dimension(20, 20));
            searchButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    jComboBoxMedicals.removeAllItems();
                    ArrayList<Medical> results = getSearchMedicalsResults(searchTextField.getText(), medArray);
                    for (Medical aMedical : results) {
			jComboBoxMedicals.addItem(aMedical);
                    }
                }
            });
            
            searchTextField = new JTextField(10);
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
            
            searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            searchPanel.add(searchTextField);
            searchPanel.add(searchButton);
            searchPanel.add(getJComboBoxMedicals());
            searchPanel.add(getJButtonAddMedical());
            return searchPanel;
        }
        private JComboBox getJComboBoxMedicals() {
		if (jComboBoxMedicals == null) {
			jComboBoxMedicals = new JComboBox();
			jComboBoxMedicals.setMaximumSize(new Dimension(300, 24));
			jComboBoxMedicals.setPreferredSize(new Dimension(300, 24));
		}
		for (Medical aMedical : medArray) {
                    jComboBoxMedicals.addItem(aMedical);
		}
		return jComboBoxMedicals;
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

//	private JLabel getJLabelSelectWard() {
//		if (jLabelSelectWard == null) {
//			jLabelSelectWard = new JLabel(MessageBundle.getMessage("angal.wardpharmacynew.selectward"));
//			jLabelSelectWard.setVisible(false);
//			jLabelSelectWard.setAlignmentX(JLabel.CENTER_ALIGNMENT);
//		}
//		return jLabelSelectWard;
//	}
}
