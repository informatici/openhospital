package org.isf.xmpp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.gui.MainMenu;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;
import org.isf.xmpp.gui.ChatTab.TabButton;
import org.isf.xmpp.manager.Interaction;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommunicationFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(CommunicationFrame.class);
	
	private JPanel leftpanel;
	private JSeparator separator=new JSeparator(SwingConstants.VERTICAL);
	private JList buddyList;
	private ChatTab tabs;
	public  Object user;
	private ChatPanel newChat;
	private Interaction interaction;
	private static JFrame frame;
	private Roster roster;
	private JMenuItem sendFile,getInfo;
	private JTextPane userInfo;
	private ChatMessages area;



	public CommunicationFrame(){
		if (frame==null){

			createFrame();
			frame= this;
			frame.validate();
			frame.repaint();
			frame.setVisible(false);
			frame.validate();
			frame.repaint();
			logger.info("XMPP Server active and running"); //$NON-NLS-1$
		}
		else
		{
			frame=getFrame();
			frame.setVisible(true);
			frame.validate();
			frame.repaint();

		}
	}

	private void createFrame()
	{
		interaction= new Interaction(this);
		activateListeners();
		getContentPane().add(createLeftPanel(),BorderLayout.WEST);
		getContentPane().add(separator,BorderLayout.CENTER);

		tabs= new ChatTab();
		tabs.setPreferredSize(new Dimension(200,400));
		tabs.setMaximumSize(new Dimension(200,400));
		tabs.setMinimumSize(new Dimension(200,400));
		tabs.setSize(new Dimension(200,400));
		getContentPane().add(tabs,BorderLayout.CENTER);
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		setSize(600,450);
		
		StringBuilder sb = new StringBuilder();
		sb.append(MessageBundle.getMessage("angal.xmpp.communication")).append(" - ").append(MainMenu.getUser());
		
		setTitle(sb.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		setResizable(false);
		setLocationRelativeTo(null);
		
	}
	public void activateListeners(){

		Roster roster= interaction.getRoster();

		roster.addRosterListener(new RosterListener() {

			public void presenceChanged(Presence presence) {
				logger.debug("State changed -> "+presence.getFrom()+" - "+presence); //$NON-NLS-1$ //$NON-NLS-2$
				if(!presence.isAvailable())
				{
					String user_name=interaction.userFromAddress(presence.getFrom());
					int index=tabs.indexOfTab(user_name);
					if(index!=-1){
						area=getArea(user_name, true);
						try {
							StringBuilder sb = new StringBuilder();
							sb.append(user_name).append(" ").append(MessageBundle.getMessage("angal.xmpp.isnowoffline"));
							
							area.printNotification(sb.toString()); //$NON-NLS-1$
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}

				}
				else if(presence.isAvailable()){
					String user_name=interaction.userFromAddress(presence.getFrom());
					int index=tabs.indexOfTab(user_name);
					if(index!=-1){
						area=getArea(user_name, true);
						try {
							StringBuilder sb = new StringBuilder();
							sb.append(user_name).append(" ").append(MessageBundle.getMessage("angal.xmpp.isnowonline"));
							
							area.printNotification(sb.toString()); //$NON-NLS-1$

						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					}
				}
				refreshBuddyList();
			}
			public void entriesUpdated(Collection<String> arg0) {}
			public void entriesDeleted(Collection<String> arg0) {}
			public void entriesAdded(Collection<String> arg0) {}
		});

		interaction.incomingChat();
		interaction.receiveFile();
	}
	public void refreshBuddyList()
	{
		getContentPane().remove(leftpanel);
		leftpanel=createLeftPanel();
		getContentPane().add(leftpanel,BorderLayout.WEST);
		validate();
		repaint();
	}
	private JScrollPane createBuddyList(){

		buddyList=interaction.getBuddyList();
		final JPopupMenu popUpMenu= new JPopupMenu();
		popUpMenu.add(sendFile= new JMenuItem(MessageBundle.getMessage("angal.xmpp.sendfile"))); //$NON-NLS-1$
		popUpMenu.add(new JPopupMenu.Separator());
		popUpMenu.add(getInfo=new JMenuItem(MessageBundle.getMessage("angal.xmpp.getinfo"))); //$NON-NLS-1$
		final JFileChooser fileChooser=new JFileChooser();
		sendFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					logger.debug(file.toString());
					String receiver = (String)(((RosterEntry) buddyList.getSelectedValue()).getName());
					logger.debug(receiver);
					interaction.sendFile(receiver, file, null);				
				}
			}
		});
		getInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UserBrowsingManager user=new UserBrowsingManager();
				String user_name = (String)((RosterEntry)buddyList.getSelectedValue()).getName();
                String info = null;
                try {
                    info = user.getUsrInfo(user_name);
                } catch (OHServiceException e) {
                    OHServiceExceptionUtil.showMessages(e);
                }

                StringBuilder sb = new StringBuilder();
				sb.append(MessageBundle.getMessage("angal.xmpp.user")).append(": ");
				sb.append(user_name).append("\n");
				sb.append(MessageBundle.getMessage("angal.xmpp.info")).append(": ");
				sb.append(info);
				
				userInfo.setText(sb.toString()); //$NON-NLS-1$ //$NON-NLS-2$
				validate();
				repaint();

			}
		});

		buddyList.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int index = buddyList.locationToIndex(e.getPoint());
					if (index >= 0) {
						user = ((RosterEntry)buddyList.getModel().getElementAt(index)).getName();
					}
				}				
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)&&!buddyList.isSelectionEmpty()&& buddyList.locationToIndex(e.getPoint())== buddyList.getSelectedIndex())
					popUpMenu.show(buddyList, e.getX(), e.getY());

				if (e.getClickCount() == 2) {
					int index = buddyList.locationToIndex(e.getPoint());
					logger.debug(""+index);
					if (index >= 0) {
						user = ((RosterEntry)buddyList.getModel().getElementAt(index)).getName();
						logger.debug("User selected: " + user.toString()); //$NON-NLS-1$
						newChat=new ChatPanel();
						roster=interaction.getRoster();
						Presence presence=roster.getPresence(((RosterEntry)buddyList.getModel().getElementAt(index)).getUser());
						if(presence.isAvailable()==true){
							if(tabs.indexOfTab((String)user) == -1){
								tabs.addTab((String) user,newChat);
								tabs.setSelectedIndex(tabs.indexOfTab((String)user));
							}
							tabs.setSelectedIndex(tabs.indexOfTab((String)user));
						}
						else
						{
							logger.debug("User offline"); //$NON-NLS-1$
						}
					}
				}
			}
		});
		JScrollPane buddy= new JScrollPane(buddyList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Dimension size = new Dimension(150,1000);
		buddy.setPreferredSize(size);

		return buddy;
	}

	private JTextPane userInfoArea()
	{
		Dimension size = new Dimension(150,150);
		userInfo = new JTextPane();
		userInfo.setForeground(new Color(58,95,205));
		userInfo.setBackground(new Color(238,238,238));
		userInfo.setMinimumSize(size);
		userInfo.setMaximumSize(size);
		userInfo.setSize(size);	
		userInfo.setBorder(BorderFactory.createTitledBorder(MessageBundle.getMessage("angal.xmpp.usersinfo"))); //$NON-NLS-1$
		userInfo.setEditable(false);


		return userInfo;
	}

	private JPanel createLeftPanel(){//pannello lista dei contatti
		leftpanel=new JPanel();
		JScrollPane buddy=new JScrollPane();
		Dimension size = new Dimension(150,200);

		leftpanel.setLayout(new BoxLayout(leftpanel, BoxLayout.Y_AXIS));
		buddy=createBuddyList();
		buddy.setBorder(BorderFactory.createTitledBorder(MessageBundle.getMessage("angal.xmpp.contacts"))); //$NON-NLS-1$
		buddy.setPreferredSize(size);
		buddy.setMaximumSize(size);
		leftpanel.setMaximumSize(size);
		leftpanel.add(buddy);
		leftpanel.add(userInfoArea());

		return leftpanel;
	}

	public ChatMessages getArea(String name,boolean incoming)
	{

		int index=tabs.indexOfTab(name);
		logger.debug("index_:"+index); //$NON-NLS-1$
		if(index!=-1){
			if(incoming){
				((TabButton)tabs.getTabComponentAt(index)).setColor(Color.red);
			}
			else
			{
				((TabButton)tabs.getTabComponentAt(index)).setColor(Color.black);
			}

			return ((ChatPanel) tabs.getComponentAt(index)).getChatMessages();

		}
		else
		{
			logger.debug("index creazione:"+index); //$NON-NLS-1$
			newChat=new ChatPanel();
			tabs.addTab(name, newChat);
			tabs.setTabColor(new Color(176,23,31));
			validate();
			repaint();
			index=tabs.indexOfTab(name);
			logger.debug("index creazione:"+index); //$NON-NLS-1$
			return ((ChatPanel) tabs.getComponentAt(index)).getChatMessages();
		}
	}

	public String getSelectedUser(){
		int index=tabs.getSelectedIndex();
		logger.debug("titolo"+tabs.getTitleAt(index)); //$NON-NLS-1$
		logger.debug("index:"+index); //$NON-NLS-1$
		return tabs.getTitleAt(index);
	}
	public void printMessage(ChatMessages area,String user,String text, boolean visualize){
		try {

			if(text.startsWith("011100100110010101110000011011110111001001110100")){//report jasper //$NON-NLS-1$
				area.printReport(user,text);
			}
			else if(text.startsWith("0101010001000001"))//trasferimento file accettato 0101010001000001=TA //$NON-NLS-1$
			{
				int index = text.indexOf("$"); //$NON-NLS-1$
				area.printNotification(text.substring(index+1));
				logger.debug("listner normale messaggi"); //$NON-NLS-1$
			}
			else if(text.startsWith("0101010001010010")){//trasferimento file rifiutato 0101010001010010=TR //$NON-NLS-1$
				int index = text.indexOf("$"); //$NON-NLS-1$
				logger.debug("listner incoming"); //$NON-NLS-1$
				area.printNotification(text.substring(index+1));
			}
			else{	
				area.printMessage(user, text, visualize);
			}
		}catch (BadLocationException e) {
			e.printStackTrace();
		} 

	}
	public void printNotification(ChatMessages area,String user,String file_transfer, JButton accept, JButton reject){
		area.printNotification(user, file_transfer, accept, reject);
	}
	public void printNotification(ChatMessages area,String text){
		try {
			area.printNotification(text);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String text_message, String to,boolean visualize){

		interaction.sendMessage(text_message, to, visualize);
	}


	public static JFrame getFrame(){
		return frame;
	}


}
