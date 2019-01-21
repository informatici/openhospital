package org.isf.stat.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.isf.generaldata.MessageBundle;
import org.isf.stat.gui.report.ExamsList1;
import org.isf.utils.jobjects.ModalJFrame;

/**
 * This class launch reports creation
 * 
 * @author Rick
 *
 */
public class ExamsList1Launcher extends ModalJFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String VERSION="v 1.0"; 

	private int pfrmExactWidth = 356;
	private int pfrmExactHeight = 165;
	private int pfrmBordX;
	private int pfrmBordY;
	private JPanel jPanel = null;
	private JPanel jButtonPanel = null;
	private JButton jCloseButton = null;
	private JPanel jContentPanel = null;
	private JButton jReport1Button = null;
//	private JLabel jReport1Label = null;
//	private final JFrame myFrame;
	
	/**
	 * This is the default constructor
	 */
	public ExamsList1Launcher() {
		super();
//		myFrame = this;
		this.setResizable(true);
		initialize();
		setVisible(true);
	}

	/**
	 * This method initializes this	
	 * 	
	 * @return void	
	 */
	private void initialize() {
		this.setTitle(MessageBundle.getMessage("angal.stat.examsreport") + " ("+VERSION+")");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		pfrmBordX = (screensize.width / 3) - (pfrmExactWidth / 2);
		pfrmBordY = (screensize.height / 3) - (pfrmExactHeight / 2);
		this.setBounds(pfrmBordX,pfrmBordY,pfrmExactWidth,pfrmExactHeight);
		
		this.setContentPane(getJPanel());
		pack();
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJButtonPanel(), BorderLayout.SOUTH);
			jPanel.add(getJContentPanel(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJButtonPanel() {
		if (jButtonPanel == null) {
			jButtonPanel = new JPanel();
			jButtonPanel.setLayout(new FlowLayout());
			jButtonPanel.add(getJCloseButton(), null);
		}
		return jButtonPanel;
	}

	/**
	 * This method initializes jCloseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCloseButton() {
		if (jCloseButton == null) {
			jCloseButton = new JButton();
			jCloseButton.setText(MessageBundle.getMessage("angal.common.close"));
			jCloseButton.setMnemonic(KeyEvent.VK_C);
			jCloseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
		return jCloseButton;
	}

	/**
	 * This method initializes jContentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPanel() {
		if (jContentPanel == null) {
			
			jContentPanel = new JPanel();
			jContentPanel.setLayout(new BorderLayout());
			
			JPanel up = new JPanel(new FlowLayout(FlowLayout.LEFT));
			up.add(getJReport1Button());
			up = setMyBorder(up, MessageBundle.getMessage("angal.stat.examslist"));
			
			jContentPanel.add(up, BorderLayout.NORTH);
		}
		return jContentPanel;
	}
	
	/**
	 * This method initializes jReport1Button	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJReport1Button() {
		if (jReport1Button == null) {
			jReport1Button = new JButton();
			jReport1Button.setBounds(new Rectangle(15, 15, 120, 31));
			jReport1Button.setText(MessageBundle.getMessage("angal.stat.runexamslistreportbytype"));
			jReport1Button.addActionListener(new ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {   
					new ExamsList1();
				}
			});
		}
		return jReport1Button;
	}

	
	/*
	 * set a specific border+title to a panel
	 */
	private JPanel setMyBorder(JPanel c, String title) {
		javax.swing.border.Border b2 = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(title), BorderFactory
						.createEmptyBorder(0, 0, 0, 0));
		c.setBorder(b2);
		return c;
	}

}  
