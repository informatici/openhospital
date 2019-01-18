package org.isf.malnutrition.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventListener;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

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
	
	private EventListenerList malnutritionListeners = new EventListenerList();

    public interface MalnutritionListener extends EventListener {
        public void malnutritionUpdated(Malnutrition maln);
        public void malnutritionInserted();
    }

    public void addMalnutritionListener(MalnutritionListener l) {
    	malnutritionListeners.add(MalnutritionListener.class, l);
    }

    public void removeMalnutritionListener(MalnutritionListener listener) {
    	malnutritionListeners.remove(MalnutritionListener.class, listener);
    }

    private void fireMalnutritionInserted() {

        EventListener[] listeners = malnutritionListeners.getListeners(MalnutritionListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((MalnutritionListener)listeners[i]).malnutritionInserted();
    }
    private void fireMalnutritionUpdated(Malnutrition maln) {

        EventListener[] listeners = malnutritionListeners.getListeners(MalnutritionListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((MalnutritionListener)listeners[i]).malnutritionUpdated(maln);
    }

	private JPanel jContentPane;

	private JPanel timePanel;

	private JPanel fieldPanel;
	
	private JPanel buttonPanel;

	private DateTextField confDate;

	private DateTextField suppDate;
	
	private JTextField weightField;

	private JTextField heightField;

	private Malnutrition maln;
	
	private JButton okButton;

	private JButton cancelButton;

	private boolean inserting;
	
	private MalnutritionManager manager = new MalnutritionManager();

	InsertMalnutrition(JDialog owner, Malnutrition malnutrition, boolean insert) {
		super(owner, true);
		maln = malnutrition;
		inserting = insert;
		setTitle(MessageBundle.getMessage("angal.malnutrition.malnutrition"));
		add(getJContentPane());
		pack();
		setLocationRelativeTo(null);
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
			suppDate = new DateTextField(maln.getDateSupp());
			confDate = new DateTextField(maln.getDateConf());
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
				try {
					maln.setHeight(Float.valueOf(heightField.getText()));
				} catch (NumberFormatException e) {
					maln.setHeight(0);
				}
				try {
					maln.setWeight(Float.valueOf(weightField.getText()));
				} catch (NumberFormatException e) {
					maln.setWeight(0);
				}
				maln.setDateSupp(suppDate.getCompleteDate());
				maln.setDateConf(confDate.getCompleteDate());
				
				if (inserting) {
					boolean inserted = false;
					try {
						inserted = manager.newMalnutrition(maln);
					} catch (OHServiceException e) {
						OHServiceExceptionUtil.showMessages(e);
					}
					if (true == inserted) {
						fireMalnutritionInserted();
						dispose();
					}
					
				} else {
					Malnutrition updatedMaln = null;
					try {
						updatedMaln = manager.updateMalnutrition(maln);
					} catch (OHServiceException e) {
						OHServiceExceptionUtil.showMessages(e);
					}
					if (updatedMaln != null) {
						fireMalnutritionUpdated(updatedMaln);
						dispose();
					}
				}
			}
		});
		return okButton;
	}

	private JButton getCancelButton() {
		cancelButton = new JButton(MessageBundle.getMessage("angal.common.cancel"));
		cancelButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		return cancelButton;
	}
}
