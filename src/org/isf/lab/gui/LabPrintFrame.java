package org.isf.lab.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.isf.generaldata.MessageBundle;
import org.isf.lab.model.LaboratoryForPrint;
import org.isf.serviceprinting.manager.PrintManager;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;

@Deprecated
public class LabPrintFrame extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jContentPanel = null;
	
	private JPanel buttonPanel = null;
	
	private JButton okButton = null;
	
	private JButton closeButton = null;
	
	private JComboBox printTypeComboBox = null;
	
	private int printTypeSelected;
	
//	private PrintManager pM;
	
	private List<LaboratoryForPrint> labList = null;
	
	public LabPrintFrame (JFrame owner, List<LaboratoryForPrint> labs) {
		super(owner, true);
		initialize();
		labList = labs;
		setResizable(false);
		setVisible(true);
	}
	
	private void initialize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		final int pfrmBase = 20;
		final int pfrmWidth = 5;
		final int pfrmHeight = 4;
		setBounds((screensize.width - screensize.width * pfrmWidth
				/ pfrmBase) / 2, (screensize.height - screensize.height
				* pfrmHeight / pfrmBase) / 2, screensize.width * pfrmWidth
				/ pfrmBase, screensize.height * pfrmHeight / pfrmBase);
		setContentPane(getJContentPanel());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(MessageBundle.getMessage("angal.lab.laboratoryprinting"));
	}
	
	private JPanel getJContentPanel(){
		if(jContentPanel==null){
			jContentPanel = new JPanel();
			jContentPanel.add(getPrintTypeComboBox());
			jContentPanel.add(getButtonPanel());
		}
		return jContentPanel;
	}
	
	private JPanel getButtonPanel(){
		if(buttonPanel==null){
			buttonPanel = new JPanel();
			buttonPanel.add(getOkButton());
			buttonPanel.add(getCloseButton());
		}
		return buttonPanel;
	}
	
	private JComboBox getPrintTypeComboBox(){
		if(printTypeComboBox==null){
			printTypeComboBox = new JComboBox();
			printTypeComboBox.addItem(MessageBundle.getMessage("angal.lab.openwithjasperReport"));
//			printTypeComboBox.addItem(MessageBundle.getMessage("angal.lab.exporttopdffile"));
			printTypeComboBox.addItem(MessageBundle.getMessage("angal.lab.printreport"));
			printTypeComboBox.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					printTypeSelected = printTypeComboBox.getSelectedIndex();
				}
			
			});
		}
		return printTypeComboBox;
	}
	
	private JButton getOkButton(){
		if(okButton==null){
			okButton = new JButton(MessageBundle.getMessage("angal.common.ok"));
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					try {
						new PrintManager("Laboratory",labList,printTypeSelected);
					} catch (OHServiceException e) {
						OHServiceExceptionUtil.showMessages(e);
					}
					dispose();
				}
			});
		}
		return okButton;
	}
	
	private JButton getCloseButton(){
		if(closeButton==null){
			closeButton = new JButton(MessageBundle.getMessage("angal.common.close"));
			closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			
			});
		}
		return closeButton;
	}
	
}
