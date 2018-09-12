package org.isf.xmpp.manager;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.isf.xmpp.gui.CommunicationFrame;
import org.isf.xmpp.service.Server;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Interaction{
	
	private final Logger logger = LoggerFactory.getLogger(Interaction.class);

	private Server server;
	private Roster roster;
	private Chat chat;
	private CommunicationFrame gui;
	private ImageIcon acceptIcon;
	private ImageIcon rejectIcon;

	public Interaction()
	{
		server = Server.getInstance();
		roster = server.getRoster(); 
		gui= (CommunicationFrame)CommunicationFrame.getFrame();
	}

	public Interaction(CommunicationFrame gui){
		this();
		this.gui = gui;
	}
	public Collection<String> getContactOnline(){

		Presence presence;
		Collection<RosterEntry> entries = roster.getEntries();
		Collection<String> entries_online=new ArrayList<String>();
		for(RosterEntry r:entries)
		{
			presence = roster.getPresence(r.getUser());
			if(presence.isAvailable()== true)
				entries_online.add(r.getName());
		}

		return entries_online;
	}
	public JList getBuddyList(){

		List<RosterEntry> entries= new ArrayList<RosterEntry>( roster.getEntries());
		Collections.sort(entries,new Comparator<RosterEntry>(){
			public int compare(RosterEntry r1, RosterEntry r2){
				Presence presence1 = roster.getPresence(r1.getUser());
				Presence presence2 = roster.getPresence(r2.getUser());
				String r1_name = r1.getName();
				String r2_name = r2.getName();
				if(presence1.isAvailable() == presence2.isAvailable())
					return r1_name.toLowerCase().compareTo(r2_name.toLowerCase());

				if(presence1.isAvailable() && (presence2.isAvailable() == false))
					return -1;

				else
					return 1;

			}
		});
		JList buddy = new JList(entries.toArray());

		ListCellRenderer render = new ComplexCellRender(server);

		buddy.setCellRenderer(render);

		return buddy;
	}




	public void sendMessage(String text_message, String to, final boolean visualize){

		MessageListener listener = new MessageListener() {

			@Override
			public void processMessage(Chat chat, Message message) {
				if(message.getType() == Message.Type.chat){
					logger.debug("Send message from: " + chat.getThreadID() );
					String user = chat.getParticipant().substring(0,chat.getParticipant().indexOf("@"));
					gui.printMessage((gui.getArea(user,true)), user, message.getBody(), false);
					if(!gui.isVisible()) {
						gui.setVisible(true);
						gui.setState(java.awt.Frame.ICONIFIED);
						gui.toFront();
					} else {
						gui.toFront();
					}
				}
			}

		};

		to = to + server.getUserAddress();
		Message message = new Message(to);
		message.setBody(text_message);
		message.setThread(to);
		logger.debug("Listener: " + listener);
		chat = server.getChat(to, message.getThread(), listener);
		//chat.addMessageListener(listener);

		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(visualize == true){
			String user = gui.getSelectedUser();
			gui.printMessage((gui.getArea(user,false)), "me", text_message, visualize);
		}
	}


	public void incomingChat(){
		ChatManager chatmanager = getChatManager();
		chatmanager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createLocally) {
				if(!createLocally) {

					chat.addMessageListener(new MessageListener() {

						@Override
						public void processMessage(Chat chat, Message message) {
							if(message.getType() == Message.Type.chat){
								logger.debug("Incoming message from: " + chat.getThreadID());
								logger.debug("GUI: " + gui);
								String user = chat.getParticipant().substring(0,chat.getParticipant().indexOf("@"));
								gui.printMessage(gui.getArea(user,true), userFromAddress(message.getFrom()), message.getBody(), false);
								if(!gui.isVisible()) {
									gui.setVisible(true);
									gui.setState(java.awt.Frame.NORMAL);
									gui.toFront();
								} else {
									gui.toFront();
								}
							}
						}
					});

				}
			}
		});
	}



	public String userFromAddress(String address)
	{
		int index;
		index= address.indexOf("@");
		return address.substring(0,index);
	}

	
	public void sendFile(String user,File file,String description){
		logger.debug("File transfer requested.");
		new ServiceDiscoveryManager(server.getConnection());
		FileTransferManager manager= new FileTransferManager(server.getConnection());
		FileTransferNegotiator.setServiceEnabled(server.getConnection(), true);
		logger.debug("Manager: " + manager);
		String userID = user+server.getUserAddress()+"/Smack";
		//String userID=getUseradd(user);
		logger.debug("Recipient: " + userID);
		//OutgoingFileTransfer.setResponseTimeout(10000);
		OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(userID);
		try {
			transfer.sendFile(file, "msg");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		logger.debug("Trasfer status: " + transfer.isDone() + ", " + transfer.getStatus());

		if(transfer.isDone())
			logger.debug("Transfer successfully completed!");
		if(transfer.getStatus().equals(Status.error))
			logger.debug("Error while transfering: " + transfer.getError());

	}



	public void receiveFile(){
		final FileTransferManager manager = new FileTransferManager(getConnection());
		FileTransferNegotiator.setServiceEnabled(getConnection(), true);

		manager.addFileTransferListener(new FileTransferListener() {

			@Override
			public void fileTransferRequest(final FileTransferRequest request) {

				if(!gui.isVisible())
					gui.setVisible(true);
				
				
				String file_transfer=((userFromAddress(request.getRequestor())+" would like to send: \n"+request.getFileName()));
				acceptIcon= new ImageIcon("rsc/icons/ok_button.png");
				rejectIcon= new ImageIcon("rsc/icons/delete_button.png");
				final JButton accept = new JButton(acceptIcon);
				accept.setMargin(new Insets(1,1,1,1));
				accept.setOpaque(false);
				accept.setBorderPainted( false );
				accept.setContentAreaFilled(false);
				final JButton reject= new JButton(rejectIcon);
				reject.setMargin(new Insets(1,1,1,1));
				reject.setOpaque(false);
				reject.setBorderPainted( false );
				reject.setContentAreaFilled(false);
				final String user=userFromAddress(request.getRequestor());
				gui.printNotification((gui.getArea(user,false)),user, file_transfer, accept, reject);


				accept.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						accept.setEnabled(false);
						reject.setEnabled(false);
						JFileChooser chooser = new JFileChooser(); 
						chooser.setCurrentDirectory(new java.io.File("."));
						chooser.setDialogTitle("Select the directoty");
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

						chooser.setAcceptAllFileFilterUsed(false);

						if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) { 
							logger.debug("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
							logger.debug("getSelectedFile() : " +  chooser.getSelectedFile());
						}
						else {
							logger.debug("No Selection.");
						}
						IncomingFileTransfer transfer = request.accept();
						String path= chooser.getSelectedFile()+"/"+request.getFileName();
						File file = new File(path);
						try {
							transfer.recieveFile(file);
						} catch (XMPPException k) {
							k.printStackTrace();
						}

						gui.printNotification((gui.getArea(user,true)),"the file transfer of: "+request.getFileName()+" between you and "+ user+" ended successfully");
						sendMessage("0101010001000001 $File transfer of: "+request.getFileName()+ " has been accepted",request.getRequestor(),false);
					}
				});
				reject.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						accept.setEnabled(false);
						reject.setEnabled(false);
						request.reject();

						gui.printNotification((gui.getArea(user,false))," you have rejected the file transfer");
						sendMessage("0101010001010010 $File transfer of: "+request.getFileName()+" has been rejected",request.getRequestor(), false);
					}
				});
			}
		});

	}



	public ChatManager getChatManager(){
		return server.getChatManager();
	}
	public Connection getConnection(){
		return server.getConnection();
	}

	public Roster getRoster()
	{
		return server.getRoster();
	}



}
