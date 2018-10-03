package org.isf.disease.gui;

/*------------------------------------------
 * LabEdit - Add/edit a Disease
 * -----------------------------------------
 * modification history
 * 25/01/2006 - Rick, Vero, Pupo - first beta version
 * 03/11/2006 - ross - added flags OPD / IPD
 * 			         - changed title, version is now 1.0 
 * 09/06/2007 - ross - when updating, now the user can change the "dis type" also
 *------------------------------------------*/

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;

import org.isf.disease.manager.DiseaseBrowserManager;
import org.isf.disease.model.Disease;
import org.isf.distype.manager.DiseaseTypeBrowserManager;
import org.isf.distype.model.DiseaseType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.VoLimitedTextField;

public class DiseaseEdit extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EventListenerList diseaseListeners = new EventListenerList();
	
	public interface DiseaseListener extends EventListener {
		public void diseaseUpdated(AWTEvent e);
		public void diseaseInserted(AWTEvent e);
	}
	
	public void addDiseaseListener(DiseaseListener l) {
		diseaseListeners.add(DiseaseListener.class, l);
	}
	
	public void removeDiseaseListener(DiseaseListener listener) {
		diseaseListeners.remove(DiseaseListener.class, listener);
	}
	
	private void fireDiseaseInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = diseaseListeners.getListeners(DiseaseListener.class);
		for (int i = 0; i < listeners.length; i++)
			((DiseaseListener)listeners[i]).diseaseInserted(event);
	}
	private void fireDiseaseUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = diseaseListeners.getListeners(DiseaseListener.class);
		for (int i = 0; i < listeners.length; i++)
			((DiseaseListener)listeners[i]).diseaseUpdated(event);
	}
	
	private static final String VERSION=MessageBundle.getMessage("angal.versione"); 

	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JLabel codeLabel = null;
	private JTextField descriptionTextField = null;
	private JTextField codeTextField = null;
	private JLabel typeLabel = null;
	private JComboBox typeComboBox = null;
	private Disease disease = null;
	private boolean insert = false;
	private JPanel jNewPatientPanel = null;
	private JCheckBox includeOpdCheckBox  = null;
	private JCheckBox includeIpdInCheckBox  = null;
	private JCheckBox includeIpdOutCheckBox  = null;

	private String lastDescription;

	/**
	 * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
	 * because we need to update them
	 */
	public DiseaseEdit(JFrame parent,Disease old,boolean inserting) {
		super(parent,true);
		insert = inserting;
		disease = old;		//disease will be used for every operation
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.disease.newdisease")+VERSION+")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.disease.editdisease")+VERSION+")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.CENTER);  // Generated
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);  // Generated
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes dataPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDataPanel() {
		if (dataPanel == null) {
			dataPanel = new JPanel();
			GridBagLayout gbl_dataPanel = new GridBagLayout();
			gbl_dataPanel.columnWidths = new int[]{0, 0, 0};
			gbl_dataPanel.rowHeights = new int[]{31, 31, 31, 31, 0};
			gbl_dataPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_dataPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			dataPanel.setLayout(gbl_dataPanel);
			typeLabel = new JLabel();
			typeLabel.setText(MessageBundle.getMessage("angal.disease.type"));  // Generated
			GridBagConstraints gbc_typeLabel = new GridBagConstraints();
			gbc_typeLabel.fill = GridBagConstraints.BOTH;
			gbc_typeLabel.insets = new Insets(5, 5, 5, 5);
			gbc_typeLabel.gridx = 0;
			gbc_typeLabel.gridy = 0;
			dataPanel.add(typeLabel, gbc_typeLabel);  // Generated
			GridBagConstraints gbc_typeComboBox = new GridBagConstraints();
			gbc_typeComboBox.fill = GridBagConstraints.BOTH;
			gbc_typeComboBox.insets = new Insets(5, 5, 5, 5);
			gbc_typeComboBox.gridx = 1;
			gbc_typeComboBox.gridy = 0;
			dataPanel.add(getTypeComboBox(), gbc_typeComboBox);  // Generated
			codeLabel = new JLabel();
			codeLabel.setText(MessageBundle.getMessage("angal.common.code"));
			GridBagConstraints gbc_codeLabel = new GridBagConstraints();
			gbc_codeLabel.insets = new Insets(5, 5, 5, 5);
			gbc_codeLabel.fill = GridBagConstraints.BOTH;
			gbc_codeLabel.gridx = 0;
			gbc_codeLabel.gridy = 1;
			dataPanel.add(codeLabel, gbc_codeLabel);  // Generated
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.common.description"));  // Generated
			GridBagConstraints gbc_descLabel = new GridBagConstraints();
			gbc_descLabel.fill = GridBagConstraints.BOTH;
			gbc_descLabel.insets = new Insets(5, 5, 5, 5);
			gbc_descLabel.gridx = 0;
			gbc_descLabel.gridy = 2;
			dataPanel.add(descLabel, gbc_descLabel);  // Generated
			GridBagConstraints gbc_descTextField = new GridBagConstraints();
			gbc_descTextField.fill = GridBagConstraints.HORIZONTAL;
			gbc_descTextField.insets = new Insets(5, 5, 5, 5);
			gbc_descTextField.gridy = 2;
			gbc_descTextField.gridx = 1;
			gbc_descLabel.fill = GridBagConstraints.BOTH;
			gbc_descLabel.insets = new Insets(0, 0, 5, 5);
			gbc_descLabel.gridx = 1;
			gbc_descLabel.gridy = 3;
			dataPanel.add(getDescriptionTextField(), gbc_descTextField);  // Generated
			GridBagConstraints gbc_codeTextField = new GridBagConstraints();
			gbc_codeTextField.fill = GridBagConstraints.BOTH;
			gbc_codeTextField.insets = new Insets(5, 5, 5, 5);
			gbc_codeTextField.gridx = 1;
			gbc_codeTextField.gridy = 1;
			dataPanel.add(getCodeTextField(), gbc_codeTextField);  // Generated
			GridBagConstraints gbc_jNewPatientPanel = new GridBagConstraints();
			gbc_jNewPatientPanel.fill = GridBagConstraints.BOTH;
			gbc_jNewPatientPanel.gridx = 1;
			gbc_jNewPatientPanel.gridy = 3;
			dataPanel.add(getJFlagsPanel(), gbc_jNewPatientPanel);
			
		}
		return dataPanel;
	}
	
	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getOkButton(), null);  // Generated
			buttonPanel.add(getCancelButton(), null);  // Generated
		}
		return buttonPanel;
	}
	
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(MessageBundle.getMessage("angal.common.cancel"));  // Generated
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}
	
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText(MessageBundle.getMessage("angal.common.ok"));  // Generated
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DiseaseBrowserManager manager = new DiseaseBrowserManager();

					disease.setType((DiseaseType)typeComboBox.getSelectedItem());
					disease.setDescription(descriptionTextField.getText());
					disease.setCode(codeTextField.getText().trim().toUpperCase());
					disease.setOpdInclude(includeOpdCheckBox.isSelected());
					disease.setIpdInInclude(includeIpdInCheckBox.isSelected());
					disease.setIpdOutInclude(includeIpdOutCheckBox.isSelected());
					
					boolean result = false;
					try{
						if (insert) { // inserting
							result = manager.newDisease(disease);
							if (result) {
								fireDiseaseInserted();
							}
						} else { // updating
							result = manager.updateDisease(disease);
							if (result) {
								fireDiseaseUpdated();
							}
						}
                        if (!result) {
                        	JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"));
                        }
                        else  dispose();
					}catch(OHServiceException ex){
						OHServiceExceptionUtil.showMessages(ex);
					}
				}
			});
		}
		return okButton;
	}
	
	/**
	 * This method initializes descriptionTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescriptionTextField() {
		if (descriptionTextField == null) {
			if (insert) {
				descriptionTextField = new JTextField();
			} else {
				lastDescription = disease.getDescription();
				descriptionTextField = new JTextField(lastDescription);
			}
		}
		return descriptionTextField;
	}
	
	/**
	 * This method initializes codeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCodeTextField() {
		if (codeTextField == null) {
			codeTextField = new VoLimitedTextField(10);
			if (!insert) {
				codeTextField.setText(disease.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
	}

	
	//private JLabel inludeOpdLabel = null;
	//private JCheckBox includeOpdCheckBox  = null;

	
	public JPanel getJFlagsPanel() {
		if (jNewPatientPanel == null){
			jNewPatientPanel = new JPanel();
			includeOpdCheckBox = new JCheckBox(MessageBundle.getMessage("angal.disease.opd"));
			includeIpdInCheckBox = new JCheckBox(MessageBundle.getMessage("angal.disease.ipdin"));
			includeIpdOutCheckBox = new JCheckBox(MessageBundle.getMessage("angal.disease.ipdout"));
			jNewPatientPanel.add(includeOpdCheckBox);
			jNewPatientPanel.add(includeIpdInCheckBox);
			jNewPatientPanel.add(includeIpdOutCheckBox);
			if(!insert){
				if (disease.getOpdInclude()) includeOpdCheckBox.setSelected(true);
				if (disease.getIpdInInclude()) includeIpdInCheckBox.setSelected(true);
				if (disease.getIpdOutInclude()) includeIpdOutCheckBox.setSelected(true);
			}
		}
		return jNewPatientPanel;
	}

	
	
	/**
	 * This method initializes typeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			typeComboBox.setBorder(new EmptyBorder(5, 5, 5, 5));
			try{
				if (insert) {
					DiseaseTypeBrowserManager manager = new DiseaseTypeBrowserManager();
					ArrayList<DiseaseType> types = manager.getDiseaseType();
					for (DiseaseType elem : types) {
						typeComboBox.addItem(elem);
					}
				} else {
					DiseaseType selectedDiseaseType=null;
					DiseaseTypeBrowserManager manager = new DiseaseTypeBrowserManager();
					ArrayList<DiseaseType> types = manager.getDiseaseType();
					for (DiseaseType elem : types) {
						typeComboBox.addItem(elem);
						if (disease.getType().equals(elem)) {
							selectedDiseaseType = elem;
						}
					}
					if (selectedDiseaseType!=null)
						typeComboBox.setSelectedItem(selectedDiseaseType);
					//typeComboBox.setEnabled(false);
				}
			}catch(OHServiceException e){
				if(e.getMessages() != null){
					for(OHExceptionMessage msg : e.getMessages()){
						JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
					}
				}
			}
			
		}
		return typeComboBox;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="82,7"