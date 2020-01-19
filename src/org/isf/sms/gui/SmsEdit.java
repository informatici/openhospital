package org.isf.sms.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.patient.gui.SelectPatient;
import org.isf.patient.gui.SelectPatient.SelectionListener;
import org.isf.patient.model.Patient;
import org.isf.sms.manager.SmsManager;
import org.isf.sms.model.Sms;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.JDateAndTimeChooserDialog;

import com.toedter.calendar.JDateChooser;

/**
 * 
 * @author Mwithi
 */
public class SmsEdit extends JDialog implements SelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel jCenterPanel;
	private JPanel jButtonPanel;
	private JPanel jNorthPanel;
	private JTextField jNumberTextField;
	private JButton jOkButton;
	private JButton jCancelButton;
	private JPanel panel;
	private JLabel jCharactersLabel;
	private JLabel jLabelCount;
	private JTextArea jTextArea;
	private JDateChooser jSchedDateChooser;
	private JLabel jSchedTimeLabel;
	private JTextField jSchedTimeTextField;
	private JButton JTimeButton;
	private JButton jPatientButton;
	
	private int MAX_LENGHT;
	
	private SmsManager smsManager = Context.getApplicationContext().getBean(SmsManager.class);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		GeneralData.getGeneralData();
		try {
			new SmsEdit(new JFrame());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SmsEdit(JFrame owner) {
		super(owner, true);
		initialize();
		initComponents();
	}
	
	private void initialize() {
		MAX_LENGHT = smsManager.getMAX_LENGHT();
	}

	private void initComponents() {
		setTitle(MessageBundle.getMessage("angal.sms.edit.title"));
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		add(getJNorthPanel(), BorderLayout.NORTH);
		add(getJCenterPanel(), BorderLayout.CENTER);
		add(getJButtonPanel(), BorderLayout.SOUTH);
		setPreferredSize(new Dimension(450, 300));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JPanel getJNorthPanel() {
		if (jNorthPanel == null) {
			jNorthPanel = new JPanel();
			jNorthPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{46, 110, 0, 0};
			gbl_panel.rowHeights = new int[]{20, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jNorthPanel.setLayout(gbl_panel);
			{
				JLabel jSchedDateLabel = new JLabel(MessageBundle.getMessage("angal.sms.scheduleddate")); //$NON-NLS-1$
				GridBagConstraints gbc_jSchedDateLabel = new GridBagConstraints();
				gbc_jSchedDateLabel.anchor = GridBagConstraints.WEST;
				gbc_jSchedDateLabel.insets = new Insets(0, 0, 5, 5);
				gbc_jSchedDateLabel.gridx = 0;
				gbc_jSchedDateLabel.gridy = 0;
				jNorthPanel.add(jSchedDateLabel, gbc_jSchedDateLabel);
			}
			{
				GridBagConstraints gbc_jSchedDateChooser = new GridBagConstraints();
				gbc_jSchedDateChooser.insets = new Insets(0, 0, 5, 5);
				gbc_jSchedDateChooser.anchor = GridBagConstraints.NORTHWEST;
				gbc_jSchedDateChooser.gridx = 1;
				gbc_jSchedDateChooser.gridy = 0;
				jNorthPanel.add(getJSchedDateChooser(), gbc_jSchedDateChooser);
			}
			GridBagConstraints gbc_jSchedTimeLabel = new GridBagConstraints();
			gbc_jSchedTimeLabel.anchor = GridBagConstraints.EAST;
			gbc_jSchedTimeLabel.insets = new Insets(0, 0, 5, 5);
			gbc_jSchedTimeLabel.gridx = 0;
			gbc_jSchedTimeLabel.gridy = 1;
			jNorthPanel.add(getJSchedTimeLabel(), gbc_jSchedTimeLabel);
			GridBagConstraints gbc_jSchedTimeTextField = new GridBagConstraints();
			gbc_jSchedTimeTextField.anchor = GridBagConstraints.WEST;
			gbc_jSchedTimeTextField.insets = new Insets(0, 0, 5, 5);
			gbc_jSchedTimeTextField.gridx = 1;
			gbc_jSchedTimeTextField.gridy = 1;
			jNorthPanel.add(getJSchedTimeTextField(), gbc_jSchedTimeTextField);
			GridBagConstraints gbc_JTimeButton = new GridBagConstraints();
			gbc_JTimeButton.insets = new Insets(0, 0, 5, 0);
			gbc_JTimeButton.gridx = 2;
			gbc_JTimeButton.gridy = 1;
			jNorthPanel.add(getJTimeButton(), gbc_JTimeButton);
			{
				JLabel JNumberLabel = new JLabel(MessageBundle.getMessage("angal.sms.number")); //$NON-NLS-1$
				GridBagConstraints gbc_JNumberLabel = new GridBagConstraints();
				gbc_JNumberLabel.anchor = GridBagConstraints.WEST;
				gbc_JNumberLabel.insets = new Insets(0, 0, 0, 5);
				gbc_JNumberLabel.gridx = 0;
				gbc_JNumberLabel.gridy = 2;
				jNorthPanel.add(JNumberLabel, gbc_JNumberLabel);
			}
			{
				jNumberTextField = new JTextField();
				GridBagConstraints gbc_jNumberTextField = new GridBagConstraints();
				gbc_jNumberTextField.fill = GridBagConstraints.HORIZONTAL;
				gbc_jNumberTextField.insets = new Insets(0, 0, 0, 5);
				gbc_jNumberTextField.gridx = 1;
				gbc_jNumberTextField.gridy = 2;
				jNorthPanel.add(jNumberTextField, gbc_jNumberTextField);
				jNumberTextField.setColumns(15);
			}
			{
				GridBagConstraints gbc_jPatientButton = new GridBagConstraints();
				gbc_jPatientButton.gridx = 2;
				gbc_jPatientButton.gridy = 2;
				jNorthPanel.add(getJPatientButton(), gbc_jPatientButton);
			}
		}
		return jNorthPanel;
		
	}
	
	private JButton getJPatientButton() {
		if (jPatientButton == null) {
			jPatientButton = new JButton();
			jPatientButton.setIcon(new ImageIcon("./rsc/icons/other_button.png")); //$NON-NLS-1$
			jPatientButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SelectPatient sp = new SelectPatient(SmsEdit.this, new String());
					sp.addSelectionListener(SmsEdit.this);
					sp.pack();
					sp.setVisible(true);
				}
			});
		}
		return jPatientButton;
	}
	
	private JDateChooser getJSchedDateChooser() {
		if (jSchedDateChooser == null) {
			jSchedDateChooser = new JDateChooser();
			jSchedDateChooser.setLocale(new Locale(GeneralData.LANGUAGE));
			jSchedDateChooser.setDate(new Date());
			jSchedDateChooser.setDateFormatString("dd/MM/yy"); //$NON-NLS-1$
			jSchedDateChooser.addPropertyChangeListener("date", new PropertyChangeListener() { //$NON-NLS-1$
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Date date = (Date) evt.getNewValue();
					jSchedTimeTextField.setText(formatTime(date));
				}
			});
		}
		return jSchedDateChooser;
	}

	private JPanel getJCenterPanel() {
		if (jCenterPanel == null) {
			jCenterPanel = new JPanel();
			jCenterPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			jCenterPanel.setLayout(new BorderLayout(0, 0));
			jCenterPanel.add(new JScrollPane(getJTextArea()));
			jCenterPanel.add(getPanel(), BorderLayout.SOUTH);
		}
		return jCenterPanel;
	}

	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setWrapStyleWord(true);
			jTextArea.setLineWrap(true);
			jTextArea.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {}
				
				@Override
				public void keyReleased(KeyEvent e) {
					JTextArea thisTextArea = (JTextArea) e.getComponent();
					int remainingChars = MAX_LENGHT - thisTextArea.getText().length();
					jLabelCount.setText(String.valueOf(remainingChars));
				}
				
				@Override
				public void keyPressed(KeyEvent e) {}
				
			});
		}
		return jTextArea;
	}

	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			jButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			jButtonPanel.add(getJOkButton());
			jButtonPanel.add(getJCancelButton());
		}
		return jButtonPanel;
	}
	
	private JButton getJOkButton() {
		if (jOkButton == null) {
			jOkButton = new JButton(MessageBundle.getMessage("angal.common.ok")); //$NON-NLS-1$
			jOkButton.setMnemonic(KeyEvent.VK_O);
			jOkButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					String number = jNumberTextField.getText().replaceAll(" ", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
					String text = jTextArea.getText();
					Date schedDate = jSchedDateChooser.getDate();
					
					Sms smsToSend = new Sms();
					smsToSend.setSmsNumber(number);
					smsToSend.setSmsDateSched(schedDate);
					smsToSend.setSmsUser(UserBrowsingManager.getCurrentUser());
					smsToSend.setSmsText(text);
					smsToSend.setModule("smsmanager");
					
					try {
						
						smsManager.saveOrUpdate(smsToSend, false);
						
					} catch (OHServiceException e1) {
						
						if (e1.getMessages().get(0).getTitle().equals("testMaxLenghtError")) {
							
							int textLenght = text.length();
							int textParts = (textLenght + MAX_LENGHT - 1) / MAX_LENGHT;
							StringBuilder message = new StringBuilder();
							message.append(e1.getMessages().get(0).getMessage())
								.append("\n")
								.append(MessageBundle.getMessage("angal.sms.doyouwanttosplitinmoremessages"))
								.append(" (").append(textParts).append(")?");
							
							int ok = JOptionPane.showConfirmDialog(SmsEdit.this, message.toString());
							if (ok == JOptionPane.YES_OPTION) {
								
								try {
									
									smsManager.saveOrUpdate(smsToSend, true);
									
								} catch (OHServiceException e2) {
									OHServiceExceptionUtil.showMessages(e2, SmsEdit.this);
									return;
								}
								
							} else return;
							
						} else {
							OHServiceExceptionUtil.showMessages(e1, SmsEdit.this);
							return;
						}
					}
					dispose();
				}
			});
		}
		return jOkButton;
	}
	
	private JButton getJCancelButton() {
		if (jCancelButton == null) {
			jCancelButton = new JButton(MessageBundle.getMessage("angal.common.cancel")); //$NON-NLS-1$
			jCancelButton.setMnemonic(KeyEvent.VK_C);
			jCancelButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return jCancelButton;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{366, 53, 0};
			gbl_panel.rowHeights = new int[]{14, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_jCharactersLabel = new GridBagConstraints();
			gbc_jCharactersLabel.insets = new Insets(0, 0, 0, 5);
			gbc_jCharactersLabel.anchor = GridBagConstraints.NORTHEAST;
			gbc_jCharactersLabel.gridx = 0;
			gbc_jCharactersLabel.gridy = 0;
			panel.add(getJCharactersLabel(), gbc_jCharactersLabel);
			GridBagConstraints gbc_jLabelCount = new GridBagConstraints();
			gbc_jLabelCount.anchor = GridBagConstraints.EAST;
			gbc_jLabelCount.gridx = 1;
			gbc_jLabelCount.gridy = 0;
			panel.add(getJLabelCount(), gbc_jLabelCount);
		}
		return panel;
	}
	
	private JLabel getJCharactersLabel() {
		if (jCharactersLabel == null) {
			jCharactersLabel = new JLabel(MessageBundle.getMessage("angal.sms.Characters")); //$NON-NLS-1$
			jCharactersLabel.setForeground(Color.GRAY);
		}
		return jCharactersLabel;
	}
	
	private JLabel getJLabelCount() {
		if (jLabelCount == null) {
			jLabelCount = new JLabel(String.valueOf(MAX_LENGHT));
			jLabelCount.setForeground(Color.GRAY);
		}
		return jLabelCount;
	}
	private JLabel getJSchedTimeLabel() {
		if (jSchedTimeLabel == null) {
			jSchedTimeLabel = new JLabel(MessageBundle.getMessage("angal.sms.scheduledtime")); //$NON-NLS-1$
		}
		return jSchedTimeLabel;
	}
	private JTextField getJSchedTimeTextField() {
		if (jSchedTimeTextField == null) {
			jSchedTimeTextField = new JTextField();
			jSchedTimeTextField.setColumns(5);
			jSchedTimeTextField.setEditable(false);
		}
		return jSchedTimeTextField;
	}
	
	private String formatTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //$NON-NLS-1$
		return sdf.format(date);
	}
	
	private JButton getJTimeButton() {
		if (JTimeButton == null) {
			JTimeButton = new JButton(""); //$NON-NLS-1$
			JTimeButton.setIcon(new ImageIcon("./rsc/icons/clock_button.png")); //$NON-NLS-1$
			JTimeButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					JDateAndTimeChooserDialog schedDate = new JDateAndTimeChooserDialog(SmsEdit.this, jSchedDateChooser.getDate());
					schedDate.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					schedDate.setVisible(true);
					
					Date date = schedDate.getDate();
					
					if (date != null) {
						
						jSchedDateChooser.setDate(date);
						jSchedTimeTextField.setText(formatTime(date));
						
					} else {
						return;
					}
				}
			});
		}
		return JTimeButton;
	}

	@Override
	public void patientSelected(Patient patient) {
		jNumberTextField.setText(patient.getTelephone());
	}
}
