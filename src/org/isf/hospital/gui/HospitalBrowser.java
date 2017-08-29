package org.isf.hospital.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.isf.generaldata.MessageBundle;
import org.isf.hospital.manager.HospitalBrowsingManager;
import org.isf.hospital.model.Hospital;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.ModalJFrame;
import org.isf.utils.jobjects.VoLimitedTextField;

/**
 * Shows information about the hospital
 * 
 * @author Fin8, Furla, Thoia
 *
 */
public class HospitalBrowser extends ModalJFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pfrmBase = 96;
	private int pfrmWidth = 24;
	private int pfrmHeight = 24;
	private int pfrmBordX;
	private int pfrmBordY;
	private JPanel jContainPanel=null;
	private JPanel jButtonPanel=null;
	private JPanel jDataPanel=null;
	private JPanel jNamePanel=null;
	private JPanel jAddPanel=null;
	private JPanel jCityPanel=null;
	private JPanel jTelePanel=null;
	private JPanel jFaxPanel=null;
	private JPanel jEmailPanel=null;	
	private JPanel jCurrencyCodPanel=null;	
	private JTextField nameJTextField;
	private JTextField addJTextField;
	private JTextField cityJTextField;
	private JTextField teleJTextField;
	private JTextField faxJTextField;
	private JTextField emailJTextField;
	private JTextField currencyCodJTextField;
	private HospitalBrowsingManager manager;
	private Hospital hospital = null;
	private JButton EditJButton;
	private JButton UpdateJButton;
	private JButton ExitJButton;
	
	
	public HospitalBrowser(){
		super();			
		manager = new HospitalBrowsingManager();
		try {
			hospital= manager.getHospital();
		} catch (OHServiceException e) {
			this.hospital = null;
			OHServiceExceptionUtil.showMessages(e);
		}
		initialize();
		setVisible(true);
		pack();
	}
	
	private void initialize() {
		this.setTitle(MessageBundle.getMessage("angal.hospital.hospitalinformations"));
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width - (screensize.width / pfrmBase * pfrmWidth)) / 2;
		pfrmBordY = (screensize.height - (screensize.height / pfrmBase * pfrmHeight)) / 2;
		this.setBounds(pfrmBordX,pfrmBordY,screensize.width / pfrmBase * pfrmWidth,screensize.height / pfrmBase * pfrmHeight);
		this.setContentPane(getJContainPanel());
	}
	
	public JPanel getJContainPanel() {
		if (jContainPanel==null){
			jContainPanel= new JPanel();
			jContainPanel.setLayout(new BorderLayout());
			jContainPanel.add(getJDataPanel(),java.awt.BorderLayout.CENTER);
			jContainPanel.add(getJButtonPanel(),java.awt.BorderLayout.SOUTH);
		}
		return jContainPanel;
	}
	
	public JPanel getJDataPanel() {
		if (jDataPanel==null){
			jDataPanel= new JPanel();
			jDataPanel.setLayout(new BoxLayout(getJDataPanel(),BoxLayout.Y_AXIS));
			jDataPanel.add(getJNamePanel());
			jDataPanel.add(getJAddPanel());
			jDataPanel.add(getJCityPanel(),null);
			jDataPanel.add(getJTelePanel(),null);
			jDataPanel.add(getJFaxPanel(),null);
			jDataPanel.add(getJEmailPanel(),null);
			jDataPanel.add(getJCurrencyCodPanel(),null);
		}
		return jDataPanel;
	}
	
	public JPanel getJNamePanel() {
		if (jNamePanel==null){
			jNamePanel= new JPanel();
			JLabel nameJLabel = new JLabel("              "+MessageBundle.getMessage("angal.hospital.name")+": ");
			jNamePanel.add(nameJLabel);
			nameJTextField = new JTextField(14);
			nameJTextField.setEditable(false);			
			jNamePanel.add(nameJTextField);
			nameJTextField.setText(hospital.getDescription());			
		}
		return jNamePanel;
	}
	
	public JPanel getJAddPanel() {
		if (jAddPanel==null){
			jAddPanel= new JPanel();
			JLabel addJLabel = new JLabel("          "+MessageBundle.getMessage("angal.hospital.address")+": ");
			jAddPanel.add(addJLabel);
			addJTextField = new JTextField(14);
			addJTextField.setEditable(false);			
			jAddPanel.add(addJTextField);
			addJTextField.setText(hospital.getAddress());
		}
		return jAddPanel;
	}
	
	public JPanel getJCityPanel() {
		if (jCityPanel==null){
			jCityPanel= new JPanel();
			JLabel cityJLabel = new JLabel("                  "+MessageBundle.getMessage("angal.hospital.city")+": ");
			jCityPanel.add(cityJLabel);
			cityJTextField = new JTextField(14);
			cityJTextField.setEditable(false);
			cityJTextField.setText(hospital.getCity());
			jCityPanel.add(cityJTextField);
		}
		return jCityPanel;
	}
	public JPanel getJTelePanel() {
		if (jTelePanel==null){
			jTelePanel= new JPanel();
			JLabel teleJLabel = new JLabel("      "+ MessageBundle.getMessage("angal.hospital.telephone")+": ");
			jTelePanel.add(teleJLabel);
			teleJTextField = new JTextField(14);
			teleJTextField.setEditable(false);
			teleJTextField.setText(hospital.getTelephone());
			jTelePanel.add(teleJTextField);
		}
		return jTelePanel;
	}
	public JPanel getJFaxPanel() {
		if (jFaxPanel==null){
			jFaxPanel= new JPanel();
			JLabel faxJLabel = new JLabel("    "+MessageBundle.getMessage("angal.hospital.faxnumber")+": ");
			jFaxPanel.add(faxJLabel);
			faxJTextField = new JTextField(14);
			faxJTextField.setEditable(false);
			faxJTextField.setText(hospital.getFax());
			jFaxPanel.add(faxJTextField);
		}
		return jFaxPanel;
	}
	public JPanel getJEmailPanel() {
		if (jEmailPanel==null){
			jEmailPanel= new JPanel();
			JLabel emailJLabel = new JLabel(""+ MessageBundle.getMessage("angal.hospital.emailaddress")+": ");
			jEmailPanel.add(emailJLabel);
			emailJTextField = new JTextField(14);
			emailJTextField.setEditable(false);
			emailJTextField.setText(hospital.getEmail());
			jEmailPanel.add(emailJTextField);
		}
		return jEmailPanel;
	}	
	public JPanel getJCurrencyCodPanel() {
		if (jCurrencyCodPanel==null){
			jCurrencyCodPanel= new JPanel();
			JLabel currencyCodJLabel = new JLabel(" "+ MessageBundle.getMessage("angal.hospital.currencycod")+": ");
			jCurrencyCodPanel.add(currencyCodJLabel);
			currencyCodJTextField = new VoLimitedTextField(3,14);
			currencyCodJTextField.setEditable(false);
			currencyCodJTextField.setText(hospital.getCurrencyCod());
			jCurrencyCodPanel.add(currencyCodJTextField);
		}
		return jCurrencyCodPanel;
	}
	
	private boolean isModified(){
		
		boolean change=false;
		
		if (!nameJTextField.getText().equalsIgnoreCase(hospital.getDescription()) ||
				!addJTextField.getText().equalsIgnoreCase(hospital.getAddress())||
				!cityJTextField.getText().equalsIgnoreCase(hospital.getCity()) ||
				!teleJTextField.getText().equalsIgnoreCase(hospital.getTelephone()) ||
				!faxJTextField.getText().equalsIgnoreCase(hospital.getFax()) ||
				!emailJTextField.getText().equalsIgnoreCase(hospital.getEmail()) ||
				!currencyCodJTextField.getText().equalsIgnoreCase(hospital.getCurrencyCod()))
		{
			change=true;
		}
		
		return change;
	}	
	
	public void saveConfirm() {
		int n = JOptionPane.showConfirmDialog(
				null,
				MessageBundle.getMessage("angal.hospital.savechanges")+" ?",
				hospital.getDescription(),
				JOptionPane.YES_NO_OPTION);
		
		if ((n == JOptionPane.YES_OPTION)){
			hospital.setDescription(nameJTextField.getText());
			hospital.setAddress(addJTextField.getText());
			hospital.setCity(cityJTextField.getText());
			hospital.setTelephone(teleJTextField.getText());
			hospital.setFax(faxJTextField.getText());
			hospital.setEmail(emailJTextField.getText());
			hospital.setCurrencyCod(currencyCodJTextField.getText());
			
			updateHospital(manager, hospital);
		}
	}
	
	public JPanel getJButtonPanel() {
		if (jButtonPanel==null){
			jButtonPanel= new JPanel();
			EditJButton = new JButton(MessageBundle.getMessage("angal.common.edit"));
			EditJButton.setMnemonic(KeyEvent.VK_E);
			UpdateJButton = new JButton(MessageBundle.getMessage("angal.hospital.update"));
			UpdateJButton.setMnemonic(KeyEvent.VK_U);
			ExitJButton = new JButton(MessageBundle.getMessage("angal.common.close"));
			ExitJButton.setMnemonic(KeyEvent.VK_C);
			UpdateJButton.setEnabled(false);
			
			ExitJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (isModified())
					{						
						//open confirm save window
						saveConfirm();
					}
					dispose();
				}
			});
			EditJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					nameJTextField.setEditable(true);
					addJTextField.setEditable(true);
					cityJTextField.setEditable(true);
					teleJTextField.setEditable(true);
					faxJTextField.setEditable(true);
					emailJTextField.setEditable(true);
					currencyCodJTextField.setEditable(true);
					UpdateJButton.setEnabled(true);
					EditJButton.setEnabled(false);
					SwingUtilities.invokeLater(new Runnable() { 
						public void run() { 
							nameJTextField.requestFocus(); 
						} 
					} );
				}
			});
			UpdateJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					nameJTextField.setEditable(false);
					addJTextField.setEditable(false);
					cityJTextField.setEditable(false);
					teleJTextField.setEditable(false);
					faxJTextField.setEditable(false);
					emailJTextField.setEditable(false);
					currencyCodJTextField.setEditable(false);
					hospital.setDescription(nameJTextField.getText());
					hospital.setAddress(addJTextField.getText());
					hospital.setCity(cityJTextField.getText());
					hospital.setTelephone(teleJTextField.getText());
					hospital.setFax(faxJTextField.getText());
					hospital.setEmail(emailJTextField.getText());
					hospital.setCurrencyCod(currencyCodJTextField.getText());
					
					updateHospital(manager, hospital);
					
					UpdateJButton.setEnabled(false);
					EditJButton.setEnabled(true);
				}
				
			});
			jButtonPanel.add(EditJButton);
			jButtonPanel.add(UpdateJButton);
			jButtonPanel.add(ExitJButton);
		}
		return jButtonPanel;
	}
	
	private void updateHospital(HospitalBrowsingManager manager, Hospital hospital) {
		try {
			manager.updateHospital(hospital);
		} catch (OHServiceException e) {
			OHServiceExceptionUtil.showMessages(e);
		}
	}
}

