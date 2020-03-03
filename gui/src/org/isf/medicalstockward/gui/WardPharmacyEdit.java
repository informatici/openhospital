package org.isf.medicalstockward.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.medicals.model.Medical;
import org.isf.medicalstockward.manager.MovWardBrowserManager;
import org.isf.medicalstockward.model.MedicalWard;
import org.isf.medicalstockward.model.MovementWard;
import org.isf.menu.manager.Context;
import org.isf.patient.manager.PatientBrowserManager;
import org.isf.patient.model.Patient;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;

public class WardPharmacyEdit extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jPanelDrug;
	private JComboBox jComboBoxDrugs;
	private JPanel jPanelPatient;
	private JPanel jPanelInternalUse;
	private JComboBox jComboBoxPatients;
	private JPanel jPanelQuantity;
	private JSpinner jSpinnerQty;
	private JComboBox jComboBoxType;
	private JPanel jPanelButtons;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private JPanel jPanelMain;
	private JTextField jTextFieldSearchPatient;
	private JButton jSearchButton;
	private VoLimitedTextField jAgeTextField;
	private VoLimitedTextField jWeightTextField;
	private VoLimitedTextField jDescriptionTextField;
	private JTextField jTextFieldSearchMedical;
	private JButton jButtonTrashPatient;
	private JButton jButtonTrashMedical;
	private String sPat;
	private String sMed;
	private String lastKey;
	private MovementWard movSelected;
	private Patient movSelectedPatient = null;
	private Medical movSelectedMedical;
	private Double maxQty;
	private int movSelectedAge;
	private float movSelectedWeight;
	private PatientBrowserManager patBrowser = Context.getApplicationContext().getBean(PatientBrowserManager.class);
	private MovWardBrowserManager movWardBrowserManager = Context.getApplicationContext().getBean(MovWardBrowserManager.class);
	private ArrayList<Patient> pat = new ArrayList<Patient>();
	private ArrayList<MedicalWard> medList = null;
	
//	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel"; //$NON-NLS-1$
	
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
	
	private void fireMovementWardUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = movementWardListeners.getListeners(MovementWardListeners.class);
		for (int i = 0; i < listeners.length; i++)
			((MovementWardListeners)listeners[i]).movementUpdated(event);
	}
	
	public WardPharmacyEdit(JFrame owner, MovementWard movWard, ArrayList<MedicalWard> drugList) {
		super(owner, true);
		medList = drugList;
		movSelected = movWard;
		initComponents();
		if (movSelected.isPatient()) jComboBoxPatients.setSelectedItem(movWard.getPatient());
		jComboBoxDrugs.setSelectedItem(movWard.getMedical());
		setTitle(MessageBundle.getMessage("angal.common.edit")); //$NON-NLS-1$
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void initComponents() {
		add(getJPanelMain(), BorderLayout.CENTER);
		pack();
	}

	private JTextField getJTextFieldSearchPatient() {
		if (jTextFieldSearchPatient == null) {
			jTextFieldSearchPatient = new JTextField();
			jTextFieldSearchPatient.setPreferredSize(new Dimension(100,20));
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
//			jTextFieldSearchPatient.addKeyListener(new KeyListener() {
//				public void keyTyped(KeyEvent e) 
//				{
//					lastKey = ""; //$NON-NLS-1$
//					String s = "" + e.getKeyChar(); //$NON-NLS-1$
//					if (Character.isLetterOrDigit(e.getKeyChar())) {
//						lastKey = s;
//					}
//					s = jTextFieldSearchPatient.getText() + lastKey;
//					s.trim();
//					
//					jComboBoxPatients.removeAllItems();
//					getJComboBoxPatients(s);
//				}
//
//				//@Override
//				public void keyPressed(KeyEvent e) {}
//
//				//@Override
//				public void keyReleased(KeyEvent e) {}
//			});
		}
		return jTextFieldSearchPatient;
	}
	
	private JButton getJSearchButton() {
		if (jSearchButton == null) {
			jSearchButton = new JButton();
			jSearchButton.setIcon(new ImageIcon("rsc/icons/zoom_r_button.png"));
			jSearchButton.setPreferredSize(new Dimension(20, 20));
			jSearchButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					jComboBoxPatients.removeAllItems();
					try {
						pat = patBrowser.getPatientWithHeightAndWeight(jTextFieldSearchPatient.getText());
					}catch(OHServiceException ex){
						OHServiceExceptionUtil.showMessages(ex);
						pat = new ArrayList<Patient>();
					}
					getJComboBoxPatients(jTextFieldSearchPatient.getText());
				}
			});
		}
		return jSearchButton;
	}
	
	private JButton getJButtonTrashPatient() {
		
		if (jButtonTrashPatient == null) {
			jButtonTrashPatient = new JButton();
			jButtonTrashPatient.setIcon(new ImageIcon("rsc/icons/trash_button.png")); //$NON-NLS-1$
			jButtonTrashPatient.setBorderPainted(false);
			jButtonTrashPatient.setPreferredSize(new Dimension(20, 20));
			jButtonTrashPatient.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (true) {
						jTextFieldSearchPatient.setText(""); //$NON-NLS-1$
						jComboBoxPatients.removeAllItems();
						getJComboBoxPatients(""); //$NON-NLS-1$
						jTextFieldSearchPatient.requestFocus();
						movSelectedPatient = null;
						movSelectedAge = 0;
						movSelectedWeight = 0;
						jAgeTextField.setText("0");
						jWeightTextField.setText("0");
					}
				}
			});
		}	
		
		return jButtonTrashPatient;
	}
	
	private JTextField getJTextFieldSearchMedical() {
		if (jTextFieldSearchMedical == null) {
			jTextFieldSearchMedical = new JTextField();
			jTextFieldSearchMedical.setPreferredSize(new Dimension(100,20));
			
			jTextFieldSearchMedical.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) 
				{
					lastKey = ""; //$NON-NLS-1$
					String s = "" + e.getKeyChar(); //$NON-NLS-1$
					if (Character.isLetterOrDigit(e.getKeyChar())) {
						lastKey = s;
					}
					s = jTextFieldSearchMedical.getText() + lastKey;
					s.trim();
					
					jComboBoxDrugs.removeAllItems();
					getJComboBoxDrugs(s);
				}
	

				//@Override
				public void keyPressed(KeyEvent e) {}

				//@Override
				public void keyReleased(KeyEvent e) {}
				
			});
		}
		return jTextFieldSearchMedical;
	}
	
	private JButton getJButtonTrashMedical() {
		
		if (jButtonTrashMedical == null) {
			jButtonTrashMedical = new JButton();
			jButtonTrashMedical.setIcon(new ImageIcon("rsc/icons/trash_button.png")); //$NON-NLS-1$
			jButtonTrashMedical.setBorderPainted(false);
			jButtonTrashMedical.setPreferredSize(new Dimension(20, 20));
			jButtonTrashMedical.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (true) {
						jTextFieldSearchMedical.setText(""); //$NON-NLS-1$
						jComboBoxDrugs.removeAllItems();
						getJComboBoxDrugs(""); //$NON-NLS-1$
						jTextFieldSearchMedical.requestFocus();
						movSelectedMedical = null;
					}
				}
			});
		}	
		
		return jButtonTrashMedical;
	}

	private JPanel getJPanelMain() {
		if (jPanelMain == null) {
			jPanelMain = new JPanel();
			jPanelMain.setBounds(13, 259, 290, 100);
			jPanelMain.setLayout(new BoxLayout(jPanelMain, BoxLayout.Y_AXIS));
			if (movSelected.isPatient()) {
				jPanelMain.add(getJPanelPatient());
			} else {
				jPanelMain.add(getJPanelInternalUse());
			}
			jPanelMain.add(getJPanelDrug());
			jPanelMain.add(getJPanelQuantity());
			jPanelMain.add(getJPanelButtons());
		}
		return jPanelMain;
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

	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText(MessageBundle.getMessage("angal.common.savem")); //$NON-NLS-1$
			jButtonOK.setMnemonic(KeyEvent.VK_O);
			jButtonOK.addActionListener(new ActionListener() {

				
				public void actionPerformed(ActionEvent e) {
					
					Object item;
					if (movSelected.isPatient()) {
						item = jComboBoxPatients.getSelectedItem();
						if (item instanceof Patient) {
							movSelectedPatient = (Patient) item;
							movSelected.setPatient(movSelectedPatient);
							movSelected.setDescription(movSelectedPatient.getName());
							movSelected.setAge(movSelectedAge);
							movSelected.setWeight(movSelectedWeight);
						} else {
							JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectapatient")); //$NON-NLS-1$
							return;
						}
					} else {
						movSelected.setDescription(jDescriptionTextField.getText());
					}
					
					item = jComboBoxDrugs.getSelectedItem();
					if (item instanceof Medical) {
						movSelectedMedical = (Medical) item;
					} else {
						JOptionPane.showMessageDialog(null,	MessageBundle.getMessage("angal.medicalstockwardedit.pleaseselectadrug")); //$NON-NLS-1$
						return;
					}
					
					movSelected.setMedical(movSelectedMedical);
					movSelected.setQuantity((Double)jSpinnerQty.getValue());
					movSelected.setUnits((String)jComboBoxType.getSelectedItem());
					
					boolean result;
					try {
						result = movWardBrowserManager.updateMovementWard(movSelected);
					} catch (OHServiceException e1) {
						result = false;
						OHServiceExceptionUtil.showMessages(e1);
					}
					if (result) {
						fireMovementWardUpdated();
					}
					if (!result)
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved")); //$NON-NLS-1$
					else
						dispose();
				}
			});
		}
		return jButtonOK;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setBounds(10, 177, 300, 36);
			jPanelButtons.add(getJButtonOK());
			jPanelButtons.add(getJButtonCancel());
		}
		return jPanelButtons;
	}

	private JComboBox getJComboBoxType() {
		if (jComboBoxType == null) {
			jComboBoxType = new JComboBox();
			jComboBoxType.setModel(new DefaultComboBoxModel(new Object[] { MessageBundle.getMessage("angal.medicalstockwardedit.pieces") })); //$NON-NLS-1$
			jComboBoxType.setPreferredSize(new Dimension(150, 20));
			jComboBoxType.setDoubleBuffered(false);
			jComboBoxType.setBorder(null);
		}
		return jComboBoxType;
	}

	private JSpinner getJSpinnerQty() {
		if (jSpinnerQty == null) {
			Double startQty = 0.;
			Double minQty = 0.;
			Double stepQty = 0.5;
			jSpinnerQty = new JSpinner(new SpinnerNumberModel(startQty,minQty,maxQty,stepQty));
			jSpinnerQty.setPreferredSize(new Dimension(50,20));
			if (movSelected != null) {
				jSpinnerQty.setValue(movSelected.getQuantity());
			}
		}
		return jSpinnerQty;
	}

	private JLabel getJLabelQty() {
		JLabel jLabelQty= new JLabel();
		jLabelQty.setText(MessageBundle.getMessage("angal.common.quantity")); //$NON-NLS-1$
		return jLabelQty;
	}

	private JPanel getJPanelQuantity() {
		if (jPanelQuantity == null) {
			jPanelQuantity = new JPanel(new FlowLayout(FlowLayout.LEFT));
			jPanelQuantity.setBounds(10, 104, 293, 33);
			jPanelQuantity.add(getJLabelQty());
			jPanelQuantity.add(getJSpinnerQty());
			jPanelQuantity.add(getJComboBoxType());
		}
		return jPanelQuantity;
	}

	private JComboBox getJComboBoxPatients(String s) {
		
		String key = s;
		String[] s1;
				
		if (jComboBoxPatients == null) {
			jComboBoxPatients = new JComboBox();
			jComboBoxPatients.setPreferredSize(new Dimension(200,20));
		}
		if (key == null || key.compareTo("") == 0) { //$NON-NLS-1$
			jComboBoxPatients.addItem(MessageBundle.getMessage("angal.medicalstockwardedit.selectapatient")); //$NON-NLS-1$
		}
		
		for (Patient elem : pat) {
			if(key != null)	{
				s1 = key.split(" ");
				int a = 0;
				for (int i = 0; i < s1.length; i++) {
					if(elem.getSearchString().contains(key.toLowerCase()) == true) {
						a++;
					}
				}
				if (a == s1.length)	jComboBoxPatients.addItem(elem);
			} else jComboBoxPatients.addItem(elem);
		}
		
		//Workaround for one item only
		if (jComboBoxPatients.getItemCount() == 1) {
			movSelectedPatient=(Patient)jComboBoxPatients.getSelectedItem();
		}
		//Workaround for first item
		if (jComboBoxPatients.getItemCount() > 0) {
			Object item = jComboBoxPatients.getItemAt(0);
			if (item instanceof Patient) {
				movSelectedPatient=(Patient)item;
			} 
		}
		jTextFieldSearchPatient.requestFocus();
		return jComboBoxPatients;
	}

	private JPanel getJPanelPatient() {
		if (jPanelPatient == null) {
			jPanelPatient = new JPanel();
			jPanelPatient.setLayout(new FlowLayout(FlowLayout.LEFT));
			jPanelPatient.add(getJLabelPatient());
			jPanelPatient.add(getJComboBoxPatients(sPat));
			jPanelPatient.add(getJTextFieldSearchPatient());
			jPanelPatient.add(getJSearchButton());
			jPanelPatient.add(getJButtonTrashPatient());
			jPanelPatient.add(getJLabelAge());
			jPanelPatient.add(getJAgeTextField());
			jPanelPatient.add(getJLabelWeight());
			jPanelPatient.add(getJWeightTextField());
		}
		return jPanelPatient;
	}
	
	private JPanel getJPanelInternalUse() {
		if (jPanelInternalUse == null) {
			jPanelInternalUse = new JPanel();
			jPanelInternalUse.setLayout(new FlowLayout(FlowLayout.LEFT));
			jPanelInternalUse.add(getJLabelInternalUse());
			jPanelInternalUse.add(getJDescriptionTextField());
		}
		return jPanelInternalUse;
	}
	
	private VoLimitedTextField getJDescriptionTextField() {
		jDescriptionTextField = new VoLimitedTextField(100, 40);
		jDescriptionTextField.setText(movSelected.getDescription());
		return jDescriptionTextField;
	}

	private JLabel getJLabelWeight() {
		JLabel jLabelWeight = new JLabel();
		jLabelWeight.setText(MessageBundle.getMessage("angal.medicalstockwardedit.weight")); //$NON-NLS-1$
		return jLabelWeight;
	}
	
	private VoLimitedTextField getJWeightTextField() {
		if (jWeightTextField == null) {
			jWeightTextField = new VoLimitedTextField(5, 5);
			movSelectedWeight = movSelected.getWeight();
			jWeightTextField.setText(String.valueOf(movSelectedWeight));
			jWeightTextField.setMinimumSize(new Dimension(100, 50));
			jWeightTextField.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					try {
						movSelectedWeight = Float.parseFloat(jWeightTextField.getText());
						if ((movSelectedWeight < 0)) {
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstockward.insertvalidweight"));
							movSelectedWeight = 0;
						}
					} catch (NumberFormatException ex) {
						//jWeightTextField.setText(String.valueOf(movSelectedWeight));
					} finally {
						jWeightTextField.setText(String.valueOf(movSelectedWeight));
					}
				}
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jWeightTextField;
	}

	private JLabel getJLabelAge() {
		JLabel jLabelAge = new JLabel();
		jLabelAge.setText(MessageBundle.getMessage("angal.medicalstockwardedit.age")); //$NON-NLS-1$
		return jLabelAge;
	}
	
	private VoLimitedTextField getJAgeTextField() {
		if (jAgeTextField == null) {
			jAgeTextField = new VoLimitedTextField(3, 3);
			movSelectedAge = movSelected.getAge();
			jAgeTextField.setText(String.valueOf(movSelectedAge));
			jAgeTextField.setMinimumSize(new Dimension(100, 50));
			jAgeTextField.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					try {
						movSelectedAge = Integer.parseInt(jAgeTextField.getText());
						if ((movSelectedAge < 0) || (movSelectedAge > 200)) {
							JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.medicalstockwardedit.insertvalidage"));
							movSelectedAge = 0;
						}
					} catch (NumberFormatException ex) {
						//jAgeTextField.setText(String.valueOf(movSelectedAge));
					} finally {
						jAgeTextField.setText(String.valueOf(movSelectedAge));
					}
				}
				public void focusGained(FocusEvent e) {
				}
			});
		}
		return jAgeTextField;
	}

	private JLabel getJLabelPatient() {
		JLabel jLabelPatient = new JLabel();
		jLabelPatient.setText(MessageBundle.getMessage("angal.medicalstockwardedit.patient")); //$NON-NLS-1$
		return jLabelPatient;
	}
	
	private JLabel getJLabelInternalUse() {
		JLabel jLabelInternalUse = new JLabel();
		jLabelInternalUse.setText(MessageBundle.getMessage("angal.medicalstockwardedit.internaluse")); //$NON-NLS-1$
		return jLabelInternalUse;
	}

	private JComboBox getJComboBoxDrugs(String s) {
		String key = s;

		if (jComboBoxDrugs == null) {
			jComboBoxDrugs = new JComboBox();
			jComboBoxDrugs.setDoubleBuffered(false);
			jComboBoxDrugs.setPreferredSize(new Dimension(400,20));
			jComboBoxDrugs.setBorder(null);
		}
		
		if (key == null || key.compareTo("") == 0) { //$NON-NLS-1$
			jComboBoxDrugs.addItem(MessageBundle.getMessage("angal.medicalstockwardedit.selectadrug")); //$NON-NLS-1$
		}
		
		for (MedicalWard elem : medList) {
			if(key != null)
			{
				if(elem.toString().toLowerCase().contains(key.toLowerCase()) == true)
					jComboBoxDrugs.addItem(elem);
			}
			else jComboBoxDrugs.addItem(elem);
		
		}
		
		//Workaround for one item only
		if (jComboBoxDrugs.getItemCount() == 1) {
			movSelectedMedical=(Medical)jComboBoxDrugs.getSelectedItem();
		}
		//Workaround for first item
		if (jComboBoxDrugs.getItemCount() > 0) {
			
			if (jComboBoxDrugs.getItemAt(0) instanceof Patient) {
				movSelectedMedical=(Medical)jComboBoxDrugs.getItemAt(0);
			}
		}
		
		return jComboBoxDrugs;
	}

	private JPanel getJPanelDrug() {
		if (jPanelDrug == null) {
			jPanelDrug = new JPanel();
			jPanelDrug.setLayout(new FlowLayout(FlowLayout.LEFT));
			jPanelDrug.add(getJLabelDrug());
			jPanelDrug.add(getJComboBoxDrugs(sMed));
			jPanelDrug.add(getJTextFieldSearchMedical());
			jPanelDrug.add(getJButtonTrashMedical());
		}
		return jPanelDrug;
	}

	private JLabel getJLabelDrug() {
		JLabel jLabelDrug = new JLabel();
		jLabelDrug.setText(MessageBundle.getMessage("angal.medicalstockwardedit.drug")); //$NON-NLS-1$
		return jLabelDrug;
	}

}
