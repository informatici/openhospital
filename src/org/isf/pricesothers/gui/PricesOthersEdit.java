package org.isf.pricesothers.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.pricesothers.manager.PricesOthersManager;
import org.isf.pricesothers.model.PricesOthers;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.utils.jobjects.VoLimitedTextField;

public class PricesOthersEdit extends JDialog {
	
	private EventListenerList OthersListeners = new EventListenerList();
	
	public interface PricesOthersListener extends EventListener {
		public void pOthersUpdated(AWTEvent e);
		public void pOthersInserted(AWTEvent e);
	}
	
	public void addOtherListener(PricesOthersListener l) {
		OthersListeners.add(PricesOthersListener.class, l);
	}
	
	public void removeOtherListener(PricesOthersListener listener) {
		OthersListeners.remove(PricesOthersListener.class, listener);
	}
	
	private void fireOtherInserted() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};

		EventListener[] listeners = OthersListeners.getListeners(PricesOthersListener.class);
		for (int i = 0; i < listeners.length; i++)
			((PricesOthersListener)listeners[i]).pOthersInserted(event);
	}
	private void fireOtherUpdated() {
		AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		
		EventListener[] listeners = OthersListeners.getListeners(PricesOthersListener.class);
		for (int i = 0; i < listeners.length; i++)
			((PricesOthersListener)listeners[i]).pOthersUpdated(event);
	}
	
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelData;
	private JPanel jPanelCode;
	private JLabel jLabelCode;
	private JTextField jTextFieldCode;
	private JPanel jPanelDescription;
	private JLabel jLabelDescription;
	private JTextField jTextFieldDescription;
	private JPanel jPanelParameters;
	private JCheckBox jCheckBoxOPD;
	private JCheckBox jCheckBoxIPD;
	private JCheckBox jCheckBoxDaily;
	private JCheckBox jCheckBoxDischarge;
	private JCheckBox jCheckBoxUndefined;
	private JPanel jPanelButtons;
	private JButton jButtonOK;
	private JButton jButtonCancel;
	private boolean insert;
	private PricesOthers pOther;
	
	public PricesOthersEdit(JFrame parent, PricesOthers other, boolean inserting) {
		super(parent, true);
		pOther = other;
		insert = inserting;
		initComponents();
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		add(getJPanelData(), BorderLayout.CENTER);
		add(getJPanelButtons(), BorderLayout.SOUTH);
		setSize(400, 180);
		if (insert) {
			this.setTitle(MessageBundle.getMessage("angal.pricesothers.newprice")); //$NON-NLS-1$
		} else {
			this.setTitle(MessageBundle.getMessage("angal.pricesothers.editprice")); //$NON-NLS-1$
		}
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
			jButtonOK.setText(MessageBundle.getMessage("angal.common.ok")); //$NON-NLS-1$
			jButtonOK.setMnemonic(KeyEvent.VK_O);
			jButtonOK.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {

					pOther.setCode(jTextFieldCode.getText());
					pOther.setDescription(jTextFieldDescription.getText());
					pOther.setOpdInclude(jCheckBoxOPD.isSelected());
					pOther.setIpdInclude(jCheckBoxIPD.isSelected());
					pOther.setDaily(jCheckBoxDaily.isSelected());
					pOther.setDischarge(jCheckBoxDischarge.isSelected());
					pOther.setUndefined(jCheckBoxUndefined.isSelected());
					
					PricesOthersManager pOtherManager = new PricesOthersManager();
					boolean result = false;
					try{
						if (insert) {      // inserting
							result = pOtherManager.newOther(pOther);
							if (result) {
								fireOtherInserted();
							}
						}
						else {             // updating
							result = pOtherManager.updateOther(pOther);
							if (result) {
								fireOtherUpdated();
							}
						}
					}catch(OHServiceException e){
						OHServiceExceptionUtil.showMessages(e);
					}
					if (!result) {
						JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved")); //$NON-NLS-1$
						dispose();
					}
					else  dispose();
					
					}				
			});
		}
		return jButtonOK;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonOK());
			jPanelButtons.add(getJButtonCancel());
		}
		return jPanelButtons;
	}

	private JCheckBox getJCheckBoxUndefined() {
		if (jCheckBoxUndefined == null) {
			jCheckBoxUndefined = new JCheckBox();
			jCheckBoxUndefined.setText("Undefined");
			jCheckBoxUndefined.setSelected(insert? false : pOther.isUndefined());
		}
		return jCheckBoxUndefined;
	}
	
	private JCheckBox getJCheckBoxDischarge() {
		if (jCheckBoxDischarge == null) {
			jCheckBoxDischarge = new JCheckBox();
			jCheckBoxDischarge.setText("Discharge");
			jCheckBoxDischarge.setSelected(insert? false : pOther.isDischarge());
		}
		return jCheckBoxDischarge;
	}
	
	private JCheckBox getJCheckBoxDaily() {
		if (jCheckBoxDaily == null) {
			jCheckBoxDaily = new JCheckBox();
			jCheckBoxDaily.setText("Daily");
			jCheckBoxDaily.setSelected(insert? false : pOther.isDaily());
		}
		return jCheckBoxDaily;
	}

	private JCheckBox getJCheckBoxIPD() {
		if (jCheckBoxIPD == null) {
			jCheckBoxIPD = new JCheckBox();
			jCheckBoxIPD.setText("IPD");
			jCheckBoxIPD.setSelected(insert? true : pOther.isIpdInclude());

		}
		return jCheckBoxIPD;
	}

	private JCheckBox getJCheckBoxOPD() {
		if (jCheckBoxOPD == null) {
			jCheckBoxOPD = new JCheckBox();
			jCheckBoxOPD.setText("OPD");
			jCheckBoxOPD.setSelected(insert? true : pOther.isOpdInclude());

		}
		return jCheckBoxOPD;
	}

	private JPanel getJPanelParameters() {
		if (jPanelParameters == null) {
			jPanelParameters = new JPanel();
			jPanelParameters.add(getJCheckBoxOPD());
			jPanelParameters.add(getJCheckBoxIPD());
			jPanelParameters.add(getJCheckBoxDaily());
			jPanelParameters.add(getJCheckBoxDischarge());
			jPanelParameters.add(getJCheckBoxUndefined());
		}
		return jPanelParameters;
	}

	private JTextField getJTextFieldDescription() {
		if (jTextFieldDescription == null) {
			jTextFieldDescription = new VoLimitedTextField(100);
			jTextFieldDescription.setText(insert? "" : pOther.getDescription()); //$NON-NLS-1$
		}
		return jTextFieldDescription;
	}

	private JLabel getJLabelDescription() {
		if (jLabelDescription == null) {
			jLabelDescription = new JLabel();
			jLabelDescription.setText("Description");
			jLabelDescription.setSize(50, 10);
		}
		return jLabelDescription;
	}

	private JPanel getJPanelDescription() {
		if (jPanelDescription == null) {
			jPanelDescription = new JPanel();
			jPanelDescription.setLayout(new GridLayout(2, 2));
			jPanelDescription.add(getJLabelDescription());
			jPanelDescription.add(getJTextFieldDescription());
		}
		return jPanelDescription;
	}

	private JTextField getJTextFieldCode() {
		if (jTextFieldCode == null) {
			jTextFieldCode = new VoLimitedTextField(10);
			jTextFieldCode.setText(insert? MessageBundle.getMessage("angal.pricesothers.othm") : pOther.getCode()); //$NON-NLS-1$
		}
		return jTextFieldCode;
	}

	private JLabel getJLabelCode() {
		if (jLabelCode == null) {
			jLabelCode = new JLabel();
			jLabelCode.setText("Code");
			jLabelCode.setSize(50, 10);
		}
		return jLabelCode;
	}

	private JPanel getJPanelCode() {
		if (jPanelCode == null) {
			jPanelCode = new JPanel();
			jPanelCode.setLayout(new GridLayout(2, 2));
			jPanelCode.add(getJLabelCode());
			jPanelCode.add(getJTextFieldCode());
		}
		return jPanelCode;
	}

	private JPanel getJPanelData() {
		if (jPanelData == null) {
			jPanelData = new JPanel();
			jPanelData.setLayout(new BoxLayout(jPanelData, BoxLayout.Y_AXIS));
			jPanelData.add(getJPanelCode());
			jPanelData.add(getJPanelDescription());
			jPanelData.add(getJPanelParameters());
		}
		return jPanelData;
	}
}
