package org.isf.menu.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.EventListenerList;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.User;
import org.isf.utils.db.BCrypt;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login extends JDialog implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 76205822226035164L;

	private final Logger logger = LoggerFactory.getLogger(Login.class);

	private EventListenerList loginListeners = new EventListenerList();

	public interface LoginListener extends EventListener {
		public void loginInserted(AWTEvent e);
	}

	public void addLoginListener(LoginListener listener) {
		loginListeners.add(LoginListener.class, listener);
	}

	public void removeLoginListener(LoginListener listener) {
		loginListeners.remove(LoginListener.class, listener);
	}

	private void fireLoginInserted(User aUser) {
		AWTEvent event = new AWTEvent(aUser, AWTEvent.RESERVED_ID_MAX + 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};

		EventListener[] listeners = loginListeners
				.getListeners(LoginListener.class);
		for (int i = 0; i < listeners.length; i++)
			((LoginListener) listeners[i]).loginInserted(event);
	}

	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			String source = event.getComponent().getName();
			// System.out.println("qq"+source);
			if (source.equalsIgnoreCase("pwd")) {
				acceptPwd();
			} else if (source.equalsIgnoreCase("submit")) {
				acceptPwd();
			} else if (source.equalsIgnoreCase("cancel")) {
				clearText();
			}
		}
	}

	public void keyTyped(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
	}

	private ArrayList<User> users;
	private JComboBox usersList;
	private JPasswordField pwd;
	private MainMenu parent;
	private User returnUser;

	public Login(MainMenu parent) {
		super(parent, "login", true);

		this.parent = parent;

		addLoginListener(parent);

		// add panel to frame
		LoginPanel panel = new LoginPanel(this);
		add(panel);
		pack();

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();

		Dimension mySize = getSize();

		setLocation((screensize.width - mySize.width) / 2,
				(screensize.height - mySize.height) / 2);

		setResizable(false);
		setVisible(true);
	}

	private void clearText() {
		pwd.setText("");
	}

	private void acceptPwd() {
		String userName = (String) usersList.getSelectedItem();
		String passwd = new String(pwd.getPassword());
		String message = MessageBundle.getMessage("angal.menu.passwordincorrectretry");
		boolean found = false;
		for (User u : users) {
			try {
				if (u.getUserName().equals(userName)
						&& BCrypt.checkpw(passwd, u.getPasswd())) {
					returnUser = u;
					found = true;
				}
			} catch (IllegalArgumentException ex) {
				message = MessageBundle.getMessage("angal.menu.invalidpasswordforthisuser");
			}
		}
		if (!found) {
			
			logger.warn("Login failed: " + message);
			JOptionPane.showMessageDialog(this, message, "",
					JOptionPane.PLAIN_MESSAGE);
			pwd.setText("");
		} else {
			fireLoginInserted(returnUser);
			removeLoginListener(parent);
			this.dispose();
		}
	}

	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MessageBundle.getMessage("angal.common.cancel"))) {
			logger.warn("Login cancelled.");
			dispose();
		} else if (command
				.equals(MessageBundle.getMessage("angal.menu.submit"))) {
			acceptPwd();
		}
	}

	private class LoginPanel extends JPanel {

		private static final long serialVersionUID = 4338749100444551874L;

		public LoginPanel(Login myFrame) {

			UserBrowsingManager manager = new UserBrowsingManager();
            try {
                users = manager.getUser();
            } catch (OHServiceException e1) {
                OHServiceExceptionUtil.showMessages(e1);
            }

			/*
			 * for (User u : users) System.out.println(u);
			 */

			usersList = new JComboBox();
			for (User u : users)
				usersList.addItem(u.getUserName());

			Dimension d = usersList.getPreferredSize();
			usersList.setPreferredSize(new Dimension(120, d.height));

			JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
			userPanel.add(usersList);

			pwd = new JPasswordField(25);
			pwd.setName("pwd");
			pwd.addKeyListener(myFrame);

			JPanel pwdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
			pwdPanel.add(pwd);

			JButton submit = new JButton(
					MessageBundle.getMessage("angal.menu.submit"));
			submit.setMnemonic(KeyEvent.VK_S);
			JButton cancel = new JButton(
					MessageBundle.getMessage("angal.common.cancel"));
			cancel.setMnemonic(KeyEvent.VK_C);

			JPanel buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			buttons.add(submit);
			buttons.add(cancel);

			setLayout(new BorderLayout(10, 10));
			add(userPanel, BorderLayout.NORTH);
			add(pwdPanel, BorderLayout.CENTER);
			add(buttons, BorderLayout.SOUTH);

			submit.addActionListener(myFrame);
			submit.setName("submit");
			submit.addKeyListener(myFrame);
			cancel.addActionListener(myFrame);
			cancel.setName("cancel");
			cancel.addKeyListener(myFrame);

		}

	}
}
