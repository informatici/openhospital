package org.isf.xmpp.manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.isf.xmpp.service.Server;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public class ComplexCellRender extends JLabel implements ListCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	Server server;
	ImageIcon online= new ImageIcon("rsc/icons/greenlight_label.png");
	ImageIcon offline= new ImageIcon("rsc/icons/greylight_label.png");

	public  ComplexCellRender(Server server2){
		server=server2;
	}
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {


		Color theForeground = null;


		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);
		Roster roster=server.getRoster();
		Presence presence;
		presence =roster.getPresence(((RosterEntry)value).getUser());
		if(presence.isAvailable()){
			renderer.setIcon(online);
			renderer.setFont(new Font("Arial",Font.BOLD,14));
		}
		else{
			renderer.setIcon(offline);
			renderer.setFont(new Font("Arial",Font.ITALIC,14));
			renderer.setForeground(Color.GRAY);

		}
		if (!isSelected) {
			renderer.setForeground(theForeground);
		}


		renderer.setText(((RosterEntry)value).getName());
		return renderer;
	}

}
