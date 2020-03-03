package org.isf.xmpp.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.isf.xmpp.service.Server;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Interaction{
	
	private final Logger logger = LoggerFactory.getLogger(Interaction.class);

	private Server server;
	private Roster roster;
	
	public Interaction(){
		server = Server.getInstance();
		roster = server.getRoster(); 
	}
	
	public Collection<String> getContactOnline(){

		Presence presence;
		Collection<RosterEntry> entries = roster.getEntries();
		Collection<String> entries_online=new ArrayList<String>();
		for(RosterEntry r:entries)
		{
			presence = roster.getPresence(r.getUser());
			if(presence.isAvailable())
				entries_online.add(r.getName());
		}

		return entries_online;
	}
	
	public void sendMessage(MessageListener listener, String text_message, String to, final boolean visualize){
		
		to = to + server.getUserAddress();
		Message message = new Message(to);
		message.setBody(text_message);
		message.setThread(to);
		Chat chat = server.getChat(to, message.getThread(), listener);

		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public Server getServer()
	{
		return server;
	}



}
