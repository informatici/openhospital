/*------------------------------
 * ExamRowEdit - add/edit Exams Result 
 * ----------------------------------
 * modification history
 * 3/11/2006 - enlarged the form width 
 * 			 - version is now 1.0 
 *------------------------------*/

package org.isf.exa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.isf.exa.manager.ExamRowBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;

public class ExamRowEdit extends JDialog {

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
	private VoLimitedTextField descriptionTextField = null;
    private Exam exam;
	private ExamRow examRow = null;
    
	/**
     * 
	 * This is the default constructor; we pass the arraylist and the selectedrow
     * because we need to update them
	 */
	public ExamRowEdit(JDialog owner,ExamRow old, Exam aExam) {
		super(owner,true);
		examRow = old;	
		exam = aExam;
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
        final int pfrmBase = 10;
//paolo: changed pfrmWidth from 3 to 4
        final int pfrmWidth = 4;
        final int pfrmHeight = 2;
        this.setBounds((screensize.width - screensize.width * pfrmWidth / pfrmBase ) / 2, (screensize.height - screensize.height * pfrmHeight / pfrmBase)/2, 
                screensize.width * pfrmWidth / pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		this.setContentPane(getJContentPane());
		this.setTitle(MessageBundle.getMessage("angal.exa.neweditresult")+" ("+VERSION+")");
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
			jContentPane.add(getDataPanel(),BorderLayout.NORTH); 
			jContentPane.add(getButtonPanel(),BorderLayout.SOUTH);  // Generated
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
			descLabel = new JLabel();
			descLabel.setText(MessageBundle.getMessage("angal.common.description"));
			dataPanel = new JPanel();
			dataPanel.add(descLabel); 
			dataPanel.add(getDescriptionTextField());  
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
					examRow=new ExamRow();
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
					if(descriptionTextField.getText().equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.exa.insertdescription"));
					}
					else{	
						ExamRowBrowsingManager manager = new ExamRowBrowsingManager();
						examRow.setDescription(descriptionTextField.getText().toUpperCase());
						examRow.setExamCode(exam);
						
						try {
							boolean inserted = manager.newExamRow(examRow);
							
							if (true == inserted) {
								ArrayList<ExamRow> key = manager.getExamRow(examRow.getExamCode().getCode(),examRow.getDescription());
								examRow.setCode(key.get(0).getCode());
							}
						} catch (OHServiceException e1) {
							OHServiceExceptionUtil.showMessages(e1);
						}
						
						dispose();
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
				descriptionTextField = new VoLimitedTextField(50);
				descriptionTextField.setColumns(20);
		}
		return descriptionTextField;
	}
	

}
