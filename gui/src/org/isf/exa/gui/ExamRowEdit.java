/*------------------------------
 * ExamRowEdit - add/edit Exams Result 
 * ----------------------------------
 * modification history
 * 3/11/2006 - enlarged the form width 
 * 			 - version is now 1.0 
 *------------------------------*/

package org.isf.exa.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.exa.manager.ExamRowBrowsingManager;
import org.isf.exa.model.Exam;
import org.isf.exa.model.ExamRow;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.VoLimitedTextField;

public class ExamRowEdit extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VERSION=MessageBundle.getMessage("angal.versione");
	
	private EventListenerList examRowListeners = new EventListenerList();

    public interface ExamRowListener extends EventListener {
        public void examRowInserted(AWTEvent e);
    }

    public void addExamListener(ExamRowListener l) {
    	examRowListeners.add(ExamRowListener.class, l);
    }

    public void removeExamListener(ExamRowListener listener) {
    	examRowListeners.remove(ExamRowListener.class, listener);
    }

    private void fireExamRowInserted() {
        AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

        EventListener[] listeners = examRowListeners.getListeners(ExamRowListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((ExamRowListener)listeners[i]).examRowInserted(event);
    }
	
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
	public ExamRowEdit(JDialog owner, ExamRow old, Exam aExam) {
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
					
					examRow.setDescription(descriptionTextField.getText().toUpperCase());
					examRow.setExamCode(exam);
					
					ExamRowBrowsingManager manager = Context.getApplicationContext().getBean(ExamRowBrowsingManager.class);
					try {
						if (true == manager.newExamRow(examRow)) {
							fireExamRowInserted();
							dispose();
						}
					}catch(OHServiceException ex){
						if(ex.getMessages() != null){
							for(OHExceptionMessage msg : ex.getMessages()){
								JOptionPane.showMessageDialog(null, msg.getMessage(), msg.getTitle() == null ? "" : msg.getTitle(), msg.getLevel().getSwingSeverity());
							}
						}
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
