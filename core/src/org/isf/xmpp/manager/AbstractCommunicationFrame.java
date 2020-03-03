/**
 * 
 */
package org.isf.xmpp.manager;

import javax.swing.JFrame;

import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;

/**
 * @author Nanni
 *
 */
public abstract class AbstractCommunicationFrame extends JFrame implements MessageListener, FileTransferListener, ChatManagerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
