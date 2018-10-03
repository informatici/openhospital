package org.isf.malnutrition.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.isf.generaldata.MessageBundle;
import org.isf.malnutrition.manager.MalnutritionManager;
import org.isf.malnutrition.model.Malnutrition;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.time.DateTextField;

public class InsertMalnutrition extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane;

	private JPanel timePanel;

	private JPanel fieldPanel;
	
	private JPanel buttonPanel;

	private DateTextField confDate;

	private DateTextField suppDate;
	
	private JTextField weightField;

	private JTextField heightField;

	private GregorianCalendar gc;

	private Malnutrition maln;
	
	private JButton okButton;

	private JButton cancelButton;

	private boolean inserting;
	
	private JDialog myDialog;

	private MalnutritionManager manager = new MalnutritionManager();

	InsertMalnutrition(JDialog owner, Malnutrition malnutrition, boolean insert) {
		super(owner, true);
		myDialog = this;
		maln = malnutrition;
		inserting = insert;
		setTitle(MessageBundle.getMessage("angal.malnutrition.malnutrition"));
		add(getJContentPane());
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JPanel getJContentPane() {
		jContentPane = new JPanel();
		jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
		jContentPane.add(getTimePanel());
		jContentPane.add(getFieldPanel());
		jContentPane.add(getButtonPanel());
		validate();
		return jContentPane;
	}

	private JPanel getTimePanel() {
		timePanel = new JPanel();
		timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
		
		if (inserting) {
			suppDate = new DateTextField(new GregorianCalendar());
			confDate = new DateTextField();
		}else{
			gc = new GregorianCalendar();
			
			gc.set(GregorianCalendar.DAY_OF_MONTH,maln.getDateSupp().get(GregorianCalendar.DAY_OF_MONTH));
			gc.set(GregorianCalendar.MONTH,maln.getDateSupp().get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.YEAR,maln.getDateSupp().get(GregorianCalendar.YEAR));
			suppDate = new DateTextField(gc);

			gc.set(GregorianCalendar.DAY_OF_MONTH,maln.getDateConf().get(GregorianCalendar.DAY_OF_MONTH));
			gc.set(GregorianCalendar.MONTH,maln.getDateConf().get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.YEAR,maln.getDateConf().get(GregorianCalendar.YEAR));
			confDate = new DateTextField(gc);
			
		}
				
		JLabel suppDateLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.dateofthiscontrol"));
		suppDateLabel.setAlignmentX(CENTER_ALIGNMENT);
		timePanel.add(suppDateLabel);
		timePanel.add(suppDate);
		JLabel confDateLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.dateofthenextcontrol"));
		confDateLabel.setAlignmentX(CENTER_ALIGNMENT);
		timePanel.add(confDateLabel);
		timePanel.add(confDate);
		return timePanel;
	}

	private JPanel getFieldPanel() {
		fieldPanel = new JPanel();
		// fieldPanel.setLayout(new BoxLayout(fieldPanel,BoxLayout.Y_AXIS));
		weightField = new JTextField();
		weightField.setColumns(6);
		heightField = new JTextField();
		heightField.setColumns(6);
		if (!inserting) {
			weightField.setText(String.valueOf(maln.getWeight()));
			heightField.setText(String.valueOf(maln.getHeight()));
		}
		JLabel weightLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.weight"));
		JLabel heightLabel = new JLabel(MessageBundle.getMessage("angal.malnutrition.height"));
		fieldPanel.add(weightLabel);
		fieldPanel.add(weightField);
		fieldPanel.add(heightLabel);
		fieldPanel.add(heightField);
		return fieldPanel;
	}

	private JPanel getButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.add(getOkButton());
		buttonPanel.add(getCancelButton());
		return buttonPanel;
	}

	private JButton getOkButton() {
		okButton = new JButton(MessageBundle.getMessage("angal.common.ok"));
		okButton.setMnemonic(KeyEvent.VK_O);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (heightField.getText().equals("")) {
					maln.setHeight(0);
				} else
					try {
						maln.setHeight(Float.valueOf(heightField.getText()));
					} catch (NumberFormatException e) {
						maln.setHeight(0);
					}
				if (weightField.getText().equals("")) {
					maln.setWeight(0);
				} else
					try {
						maln.setWeight(Float.valueOf(weightField.getText()));
					} catch (NumberFormatException e) {
						maln.setWeight(0);
					}
				if(suppDate.isDateValid()){
					maln.setDateSupp(suppDate.getCompleteDate());
				}else
				maln.setDateSupp(null);
				if(confDate.isDateValid()){
					maln.setDateConf(confDate.getCompleteDate());
				}else
					maln.setDateConf(null);
				if (inserting) {
					
					if (true == isValidForInsert(maln)) {
						boolean inserted;
						
						try {
							inserted = manager.newMalnutrition(maln);
						} catch (OHServiceException e) {
							inserted = false;
							OHServiceExceptionUtil.showMessages(e);
						}
						
						if (true == inserted) {
							myDialog.dispose();
						}
					}
					
				} else {
					
					if (true == isValidForUpdate(maln)) {
						boolean updated;
						
						try {
							updated = manager.updateMalnutrition(maln, true);
							
							if (false == updated) {
								// failed because of valid lock, ask user if they want to update anyway
								String msg = MessageBundle.getMessage("angal.sql.thedatahasbeenupdatedbysomeoneelse") +
										MessageBundle.getMessage("angal.malnutrition.doyouwanttooverwritethedata");
								
								int response = JOptionPane.showConfirmDialog(null, msg, MessageBundle.getMessage("angal.malnutrition.select"), JOptionPane.YES_NO_OPTION);
								if (response == JOptionPane.YES_OPTION){
									updated = manager.updateMalnutrition(maln, false);
								}
							}
						} catch (OHServiceException e) {
							// either the record was deleted during update by another user or the sql failed for some other reason
							// so show the error message propagated up from the exception
							updated = false;
							OHServiceExceptionUtil.showMessages(e);
						}
						
						if (true == updated) {
							myDialog.dispose();
						}
					}
				}
			}

			/**
			 * Checks for update validity and shows error on failure
			 * @param malnutrition
			 * @return
			 */
			private boolean isValidForUpdate(Malnutrition malnutrition) {
				if(malnutrition.getHeight()==0){
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinheightfield"));
					return false;
				}

				if(malnutrition.getWeight()==0){
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinweightfield"));
					return false;
				}
				
				return true;
			}

			/**
			 * Checks for insert validity and shows appropriate error on failure
			 * @param malnutrition
			 * @return
			 */
			private boolean isValidForInsert(Malnutrition malnutrition) {
				if(malnutrition.getDateSupp()==null) {
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.thedatatypemustbeindateofcontrol"));
					return false;
				}

				if(malnutrition.getDateConf()==null) {
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.thedatatypemustbeindateofthenextcontrol"));
					return false;
				}

				if(malnutrition.getWeight()==0) {
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinweightfield"));
					return false;
				}

				if(malnutrition.getHeight()==0) {
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.insertcorrectvalueinheightfield"));
					return false;
				}
				
				GregorianCalendar gc = new GregorianCalendar();
				if((malnutrition.getDateSupp().get(GregorianCalendar.YEAR)>=gc.get(GregorianCalendar.YEAR))&&
						(malnutrition.getDateSupp().get(GregorianCalendar.MONTH)>=gc.get(GregorianCalendar.MONTH))&&
						(malnutrition.getDateSupp().get(GregorianCalendar.DAY_OF_MONTH)>gc.get(GregorianCalendar.DAY_OF_MONTH))){
					
					JOptionPane.showMessageDialog(null,MessageBundle.getMessage("angal.malnutrition.thedateofthiscontrolwillbeafuturedate"));
					return false;
				}
				
				return true;
			}
		});
		return okButton;
	}

	private JButton getCancelButton() {
		cancelButton = new JButton(MessageBundle.getMessage("angal.common.cancel"));
		cancelButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myDialog.dispose();
			}
		});
		return cancelButton;
	}
}
