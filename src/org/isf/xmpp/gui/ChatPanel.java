package org.isf.xmpp.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class ChatPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChatMessages chatMessages;
	private JTextField send;

	public ChatPanel()
	{
		setLayout(new GridLayout(1,0));
		createChatPanel();

	}

	protected JPanel createChatPanel() {

		chatMessages= new ChatMessages();
		send =new JTextField();
		send.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				int key =e.getKeyCode();
				if(key==KeyEvent.VK_ENTER)
				{
					CommunicationFrame frame = (CommunicationFrame)CommunicationFrame.getFrame();
					String receiver =frame.getSelectedUser();
					frame.sendMessage(send.getText(), receiver,true);
					send.setText("");
				}

			}
		});
		chatMessages.setSize(new Dimension(100, 200));
		final JScrollPane received = new JScrollPane(chatMessages,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		received.setPreferredSize(new Dimension(200,200));
		received.setMaximumSize(new Dimension(10,10));
		received.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			BoundedRangeModel brm = received.getVerticalScrollBar().getModel();
			boolean wasAtBottom = true;	

			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (!brm.getValueIsAdjusting()) {
					if (wasAtBottom)
						brm.setValue(brm.getMaximum());
				} else
					wasAtBottom = ((brm.getValue() + brm.getExtent()) == brm.getMaximum());



			}
		});

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,received,send);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.5); 
		splitPane.setDividerLocation(300);

		add(splitPane);

		return this;
	}

	public ChatMessages getChatMessages(){
		return chatMessages;
	}

}
