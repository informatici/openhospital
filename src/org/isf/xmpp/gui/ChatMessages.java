package org.isf.xmpp.gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.isf.stat.gui.report.GenericReportFromDateToDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatMessages extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Document sDoc;
	private Color greenColor = new Color(0,100,0);
	private Color blueColor = new Color(176,23,31);
	private Color redColor =  new Color (25,25,112);
	private SimpleAttributeSet keyWord;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	private final Logger logger = LoggerFactory.getLogger(ChatMessages.class);


	public ChatMessages(){
		setEditable(false);
		setMinimumSize(getSize());
		keyWord= new SimpleAttributeSet();
	}
	//print general notification
	public void printNotification(String notification) throws BadLocationException{
		StyleConstants.setForeground(keyWord, greenColor);
		sDoc=getDocument();
		sDoc.insertString(sDoc.getEndPosition().getOffset(), "*** "+notification+"\n",keyWord);
	}
	//print notification of file transfer
	public void printNotification(String name,String file_transfer,
			JButton accept, JButton reject){
		
		Document doc=getDocument();
		int position = doc.getEndPosition().getOffset();
		StyleConstants.setForeground(keyWord, greenColor);
		try {
			doc.insertString(position, "\n*** "+file_transfer+"\n", keyWord);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		position = doc.getEndPosition().getOffset();
		select(position, position);
		
		insertComponent(accept);
		insertComponent(reject);
	}
	//print send and received messages
	public void printMessage(String user, String message, boolean incomingType) throws BadLocationException{
		StyleConstants.setBold(keyWord, true);
		if(incomingType)
				StyleConstants.setForeground(keyWord, blueColor);
		else
				StyleConstants.setForeground(keyWord, redColor);
		sDoc= getDocument();
		sDoc.insertString(sDoc.getEndPosition().getOffset(), "("+ sdf.format(new Date()) +") "+user+" : ",keyWord);
		StyleConstants.setBold(keyWord, false);
		StyleConstants.setForeground(keyWord, Color.black);
		sDoc.insertString(sDoc.getEndPosition().getOffset(), message+"\n",keyWord);

	}
	public void printReport(String name,String report){
		final String fromDate;
		final String toDate;
		final String typeReport;
	
		Document doc=getDocument();
		ImageIcon open = new ImageIcon("rsc/icons/open.png");
		final JButton view= new JButton(open);
		view.setMargin(new Insets(1,1,1,1));
		view.setOpaque(false);
		view.setBorderPainted( false );
		view.setContentAreaFilled(false);
		
		String [] reports=new String[4];
		int i=0;
		StringTokenizer st = new StringTokenizer(report);
		while(st.hasMoreTokens()){
			reports[i]=st.nextToken();
			i++;
		}
		fromDate=reports[1];
		logger.debug("fromDate: "+reports[1]);
		toDate=reports[2];
		logger.debug("toDate: "+reports[2]);
		typeReport=reports[3];
		logger.debug("typeReport: "+reports[3]);
		int position = doc.getEndPosition().getOffset();
		StyleConstants.setForeground(keyWord, greenColor);

		try {
			doc.insertString(position, "\n*** "+name+ " wants to share with you this report:"+typeReport+"\n", keyWord);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		position = doc.getEndPosition().getOffset();
		select(position, position);
		
		insertComponent(view);
		view.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new GenericReportFromDateToDate(fromDate,toDate,typeReport, typeReport, false);
				view.setEnabled(false);
			}
		});
		
	}
	
	
	
}
