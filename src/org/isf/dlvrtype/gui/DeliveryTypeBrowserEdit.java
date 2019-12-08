package org.isf.dlvrtype.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.isf.dlvrtype.manager.DeliveryTypeBrowserManager;
import org.isf.dlvrtype.model.DeliveryType;
import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.Context;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.jobjects.VoLimitedTextField;

public class DeliveryTypeBrowserEdit extends JDialog{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private EventListenerList deliveryTypeListeners = new EventListenerList();

    public interface DeliveryTypeListener extends EventListener {
        public void deliveryTypeUpdated(AWTEvent e);
        public void deliveryTypeInserted(AWTEvent e);
    }

    public void addDeliveryTypeListener(DeliveryTypeListener l) {
        deliveryTypeListeners.add(DeliveryTypeListener.class, l);
    }

    public void removeDeliveryTypeListener(DeliveryTypeListener listener) {
        deliveryTypeListeners.remove(DeliveryTypeListener.class, listener);
    }

    private void fireDeliveryInserted() {
        AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

            /**
             *
             */
            private static final long serialVersionUID = 1L;};

        EventListener[] listeners = deliveryTypeListeners.getListeners(DeliveryTypeListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((DeliveryTypeListener)listeners[i]).deliveryTypeInserted(event);
    }
    private void fireDeliveryUpdated() {
        AWTEvent event = new AWTEvent(new Object(), AWTEvent.RESERVED_ID_MAX + 1) {

            /**
             *
             */
            private static final long serialVersionUID = 1L;};

        EventListener[] listeners = deliveryTypeListeners.getListeners(DeliveryTypeListener.class);
        for (int i = 0; i < listeners.length; i++)
            ((DeliveryTypeListener)listeners[i]).deliveryTypeUpdated(event);
    }

    private JPanel jContentPane = null;
    private JPanel dataPanel = null;
    private JPanel buttonPanel = null;
    private JButton cancelButton = null;
    private JButton okButton = null;
    private JTextField descriptionTextField = null;
    private VoLimitedTextField codeTextField = null;
    private String lastdescription;
    private DeliveryType deliveryType = null;
    private boolean insert;
    private JPanel jDataPanel = null;
    private JLabel jCodeLabel = null;
    private JPanel jCodeLabelPanel = null;
    private JPanel jDescriptionLabelPanel = null;
    private JLabel jDescripitonLabel = null;
    /**
     *
     * This is the default constructor; we pass the arraylist and the selectedrow
     * because we need to update them
     */
    public DeliveryTypeBrowserEdit(JFrame owner,DeliveryType old,boolean inserting) {
        super(owner,true);
        insert = inserting;
        deliveryType = old;//disease will be used for every operation
        lastdescription= deliveryType.getDescription();
        initialize();
    }


    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {

//		this.setBounds(300,300,350,180);
        this.setContentPane(getJContentPane());
        if (insert) {
            this.setTitle(MessageBundle.getMessage("angal.dlvrtype.newdeliverytyperecord"));
        } else {
            this.setTitle(MessageBundle.getMessage("angal.dlvrtype.editingdeliverytyperecord"));
        }
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        setLocationRelativeTo(null);
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
            dataPanel = new JPanel();
            //dataPanel.setLayout(new BoxLayout(getDataPanel(), BoxLayout.Y_AXIS));  // Generated
            dataPanel.add(getJDataPanel(), null);
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
                	DeliveryTypeBrowserManager manager = Context.getApplicationContext().getBean(DeliveryTypeBrowserManager.class);

                    try{
                        if (descriptionTextField.getText().equals(lastdescription)){
                            dispose();
                        }
                        deliveryType.setDescription(descriptionTextField.getText());
                        deliveryType.setCode(codeTextField.getText());
                        boolean result = false;
                        if (insert) {      // inserting
                            result = manager.newDeliveryType(deliveryType);
                            if (result) {
                                fireDeliveryInserted();
                            }
                            if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.dlvrtype.thdatacouldnotbesaved"));
                            else  dispose();
                        }
                        else {                          // updating
                            if (descriptionTextField.getText().equals(lastdescription)){
                                dispose();
                            }else{
                                result = manager.updateDeliveryType(deliveryType);
                                if (result) {
                                    fireDeliveryUpdated();
                                }
                                if (!result) JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.dlvrtype.thdatacouldnotbesaved"));
                                else  dispose();
                            }

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
            descriptionTextField = new JTextField(20);
            if (!insert) {
                descriptionTextField.setText(deliveryType.getDescription());
                lastdescription=deliveryType.getDescription();
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
            codeTextField = new VoLimitedTextField(1);
            if (!insert) {
                codeTextField.setText(deliveryType.getCode());
                codeTextField.setEnabled(false);
            }
        }
        return codeTextField;
    }

    /**
     * This method initializes jDataPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJDataPanel() {
        if (jDataPanel == null) {
            jDataPanel = new JPanel();
            jDataPanel.setLayout(new BoxLayout(getJDataPanel(),BoxLayout.Y_AXIS));
            jDataPanel.add(getJCodeLabelPanel(), null);
            jDataPanel.add(getCodeTextField(), null);
            jDataPanel.add(getJDescriptionLabelPanel(), null);
            jDataPanel.add(getDescriptionTextField(), null);
        }
        return jDataPanel;
    }

    /**
     * This method initializes jCodeLabel
     *
     * @return javax.swing.JLabel
     */
    private JLabel getJCodeLabel() {
        if (jCodeLabel == null) {
            jCodeLabel = new JLabel();
            jCodeLabel.setText(MessageBundle.getMessage("angal.dlvrtype.codemaxchars"));
        }
        return jCodeLabel;
    }

    /**
     * This method initializes jCodeLabelPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJCodeLabelPanel() {
        if (jCodeLabelPanel == null) {
            jCodeLabelPanel = new JPanel();
            //jCodeLabelPanel.setLayout(new BorderLayout());
            jCodeLabelPanel.add(getJCodeLabel(), BorderLayout.CENTER);
        }
        return jCodeLabelPanel;
    }

    /**
     * This method initializes jDescriptionLabelPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJDescriptionLabelPanel() {
        if (jDescriptionLabelPanel == null) {
            jDescripitonLabel = new JLabel();
            jDescripitonLabel.setText(MessageBundle.getMessage("angal.common.description"));
            jDescriptionLabelPanel = new JPanel();
            jDescriptionLabelPanel.add(jDescripitonLabel, null);
        }
        return jDescriptionLabelPanel;
    }



}  //  @jve:decl-index=0:visual-constraint="146,61"


