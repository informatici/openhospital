package org.isf.xmpp.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatTab extends JTabbedPane {
	
	private final Logger logger = LoggerFactory.getLogger(ChatTab.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TabButton tab;
	 public void addTab(String title,Component component){
		 	super.addTab(title,component);
		 	int index;
		 	index=indexOfTab(title);
		 	logger.debug("index: " + indexOfTabComponent(this));
	    	tab=new TabButton(title,indexOfTabComponent(this),this);
		 	setTabComponentAt(index, tab);
		 	
	    }
	 public Color getTabColor()
	 {
		
		return tab.getColor();
				
	 }
	 public void setTabColor(Color color)
	 {
		 tab.setColor(color);
	 }
	
	public class TabButton extends JPanel implements ActionListener {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int tab_number;
	    ImageIcon reg = null;
	    ImageIcon over = null;
	    ChatTab tabReference;
	    JLabel user= null;
	    public TabButton( String label,int index,ChatTab tab ){
	    	
	        super(new FlowLayout(FlowLayout.LEFT,1,1));
	    	tabReference=tab;
	    	tab_number=index;
	    	user=new JLabel(label);
	        add(user);
	        try{

	            // load firefox buttons
	            reg = new ImageIcon("rsc/icons/regular_close_tab.JPG");
	            over = new ImageIcon("rsc/icons/hoverOver_close_tab.JPG");

	        } catch( Exception e ){

	            e.printStackTrace();

	        }
	        setOpaque(false);
	        final JButton button = new JButton(reg);
	        button.setMargin(new Insets(1,1,1,1));
	        button.setOpaque(false);
	        button.setRolloverIcon(over);
	        button.setPressedIcon(over);
	        button.setBorderPainted( false );
	        button.setContentAreaFilled(false);
	        button.addActionListener( this );
	        add( button );

	    }
	    
	    public void setColor(Color color){
	    	user.setForeground(color);
	    }
	    public Color getColor()
	    {
	    	return user.getForeground();
	    }
	    public void actionPerformed(ActionEvent ae ){

	        tabReference.remove(tabReference.indexOfTabComponent(this));
	    	
	    }
	   

	}
}

