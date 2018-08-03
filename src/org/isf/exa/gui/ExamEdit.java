
/*------------------------------------------
 * ExamEdit - add/edit an exam
 * -----------------------------------------
 * modification history
 * 03/11/2006 - ross - Enlarged Destription from 50 to 100 
 *                   - removed toupper for the description
 * 			         - version is now 1.0 
 *------------------------------------------*/

package org.isf.exa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.isf.exa.manager.ExamBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exatype.model.ExamType;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;

public class ExamEdit extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VERSION=MessageBundle.getMessage("angal.versione");  
    
	private JPanel jContentPane = null;
	private JPanel dataPanel = null;
	private JPanel buttonPanel = null;
	private JButton cancelButton = null;
	private JButton okButton = null;
	private JLabel descLabel = null;
	private JLabel codeLabel= null;
	private JLabel procLabel = null;
	private JLabel defLabel = null;
	private VoLimitedTextField descriptionTextField = null;
	private VoLimitedTextField codeTextField=null;
	private JComboBox procComboBox = null;
	private VoLimitedTextField defTextField = null;
	private JLabel typeLabel = null;
	private JComboBox typeComboBox = null;
	private Exam exam = null;
	private boolean insert = false;
    
	/**
     * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
     * because we need to update them
	 */
	public ExamEdit(JFrame owner,Exam old,boolean inserting) {
		super(owner,true);
		insert = inserting;
		exam = old;		//medical will be used for every operation
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
        final int pfrmBase = 20;
        final int pfrmWidth = 7;
        final int pfrmHeight = 8;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2, (screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setContentPane(getJContentPane());
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.exa.newexam")+" ("+VERSION+")");
		} else {
			this.setTitle(MessageBundle.getMessage("angal.exa.editexam")+" ("+VERSION+")");
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
			jContentPane.add(getDataPanel(), java.awt.BorderLayout.NORTH);  // Generated
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
			typeLabel = new JLabel(MessageBundle.getMessage("angal.exa.type"));
			descLabel = new JLabel(MessageBundle.getMessage("angal.exa.description"));			
			codeLabel=new JLabel(MessageBundle.getMessage("angal.common.code"));
			procLabel = new JLabel(MessageBundle.getMessage("angal.exa.procedure"));
			defLabel = new JLabel(MessageBundle.getMessage("angal.exa.default"));
			dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  // Generated
			dataPanel.add(typeLabel, null);
			dataPanel.add(getTypeComboBox(), null);  
			dataPanel.add(codeLabel,null);
			dataPanel.add(getCodeTextField(),null);
			dataPanel.add(descLabel, null);  
			dataPanel.add(getDescriptionTextField(), null);
			dataPanel.add(procLabel, null);  
			dataPanel.add(getProcComboBox(), null);
			dataPanel.add(defLabel, null);  
			dataPanel.add(getDefTextField(), null);
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
					if((codeTextField.getText().trim().equals(""))||(descriptionTextField.getText().trim().equals(""))){
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.exa.pleaseinsertcodeoranddescription"));
					} else {	
						ExamBrowsingManager manager = new ExamBrowsingManager();
						exam.setExamtype((ExamType)typeComboBox.getSelectedItem());
						exam.setDescription(descriptionTextField.getText());
						
						exam.setCode(codeTextField.getText().toUpperCase());
						exam.setDefaultResult(defTextField.getText().toUpperCase());
						exam.setProcedure(Integer.parseInt(procComboBox.getSelectedItem().toString()));
						
						boolean result = false;
						if (insert) {
							
							boolean keyPresent;
							
							try {
								keyPresent = manager.isKeyPresent(exam);
							} catch (OHServiceException e1) {
								keyPresent = false;
								OHServiceExceptionUtil.showMessages(e1);
							}
							
							if (true == keyPresent) {
								JOptionPane.showMessageDialog(ExamEdit.this, MessageBundle.getMessage("angal.exa.changethecodebecauseisalreadyinuse"));
								return;
							}
							try {
								result = manager.newExam(exam);
							} catch (OHServiceException e1) {
								result = false;
								OHServiceExceptionUtil.showMessages(e1);
							}
						} else {
							try {
								result = manager.updateExam(exam);
								
								if (false == result) {
									String prompt = MessageBundle.getMessage("angal.exa.thedatahasbeenupdatedbysomeoneelse") + ".\n"
											+ MessageBundle.getMessage("angal.exa.doyouwanttooverwritethedata") + "?";
									
									int ok = JOptionPane.showConfirmDialog(null,
											prompt,
											MessageBundle.getMessage("angal.exa.select"), JOptionPane.YES_NO_OPTION);
									
									if (JOptionPane.YES_OPTION == ok) {
										manager.updateExam(exam, false);
									}
								}
							} catch (OHServiceException e1) {
								result = false;
								OHServiceExceptionUtil.showMessages(e1);
							}
						}
						if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.exa.thedatacouldnotbesaved"));
						else  dispose();
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
			//changed size from 50 to 100
			descriptionTextField = new VoLimitedTextField(100);
			if (!insert) {
			descriptionTextField.setText(exam.getDescription());
			}
		}
		return descriptionTextField;
	}
	
	private JTextField getDefTextField() {
		if (defTextField == null) {
				defTextField = new VoLimitedTextField(50);
				if (!insert) {
				defTextField.setText(exam.getDefaultResult());
			}
		}
		return defTextField;
	}
	
	private JTextField getCodeTextField() {
		if (codeTextField == null) {
			
				codeTextField = new VoLimitedTextField(10);
				if (!insert) {
				codeTextField.setText(exam.getCode());
				codeTextField.setEnabled(false);
			}
		}
		return codeTextField;
	}
	
	private JComboBox getProcComboBox(){
		if(procComboBox==null){
			procComboBox = new JComboBox();
			if (insert) {
				procComboBox.addItem("1");
				procComboBox.addItem("2");
			} else {
				procComboBox.addItem(exam.getProcedure());
				procComboBox.setEnabled(false);
			}
		}
		return procComboBox;
	}
	
	/**
	 * This method initializes typeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			if (insert) {
				ExamBrowsingManager manager = new ExamBrowsingManager();
				ArrayList<ExamType> types;
				try {
					types = manager.getExamType();
				} catch (OHServiceException e) {
					types = null;
					OHServiceExceptionUtil.showMessages(e);
				}
				if (null != types) {
					for (ExamType elem : types) {
						typeComboBox.addItem(elem);
					}
				}
			} else {
				typeComboBox.addItem(exam.getExamtype());
				typeComboBox.setEnabled(false);
			}
			
		}
		return typeComboBox;
	}


}  //  @jve:decl-index=0:visual-constraint="82,7"
