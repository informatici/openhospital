package org.isf.utils.jobjects;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * @author Santhosh Kumar T - santhosh@in.fiorano.com
 * 
 */
public class ModalJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final JFrame frame = this;
	
	private final ImageIcon img = new ImageIcon("./rsc/icons/oh.png");
	

	/**
	 * method to enable/disable a owner JFrame launching this ModalJFrame
	 * @param owner - the JFrame owner
	 */
	public void showAsModal(final JFrame owner) {
		
		setIconImage(img.getImage());

		this.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				owner.setEnabled(false);
			}

			public void windowClosing(WindowEvent e) {
				owner.setEnabled(true);
				owner.toFront();
				frame.removeWindowListener(this);
			}

			public void windowClosed(WindowEvent e) {
				owner.setEnabled(true);
				owner.toFront();
				frame.removeWindowListener(this);
			}
		});
		
		owner.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				if (frame.isShowing()) {
					frame.setExtendedState(JFrame.NORMAL);
					frame.toFront();
				} else {
					owner.removeWindowListener(this);
				}
			}
		});
		
		frame.setVisible(true);
	}
}