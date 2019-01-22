package org.isf.menu.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.UserGroup;
import org.isf.menu.model.UserMenuItem;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.gui.OHServiceExceptionUtil;

/*----------------------------------------------------------
 * modification history
 * ====================
 * 25/03/09 - alex - User PrivilegeTree is build now on rootMenu;
 * 					 new MenuItems for a user will be set as inactive
 -----------------------------------------------------------*/

class PrivilegeTree extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserGroup aGroup;
	
	public PrivilegeTree(UserGroupBrowsing parent, UserGroup aGroup) {
		super(parent,MessageBundle.getMessage("angal.menu.menuitmebrowser"),true );
		this.aGroup=aGroup;
		
		Rectangle r = parent.getBounds();
		setBounds(new Rectangle(r.x+50, r.y+50,280, 350));
		//setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		UserBrowsingManager manager = new UserBrowsingManager();
        ArrayList<UserMenuItem> myMenu = null;
        try {
            myMenu = manager.getGroupMenu(aGroup);
        } catch (OHServiceException e) {
            OHServiceExceptionUtil.showMessages(e);
        }
        ArrayList<UserMenuItem> rootMenu = null;
        try {
            rootMenu = manager.getGroupMenu(new UserGroup("admin",""));
        } catch (OHServiceException e) {
            OHServiceExceptionUtil.showMessages(e);
        }

        UserMenuItem menuRoot = new UserMenuItem("main", "main", "main", "",
				'M', "", "", true, 1, true);
		// the root 
		root = new DefaultMutableTreeNode(menuRoot);
		model = new DefaultTreeModel(root);
		tree = new JTree(model);

		
		//a supporting structure
		ArrayList<UserMenuItem> junkMenu = new ArrayList<UserMenuItem>();
		
		//cycle to process the whole rootMenu
		while (!rootMenu.isEmpty()) {
			Iterator<UserMenuItem> it = rootMenu.iterator();
			while (it.hasNext()) {
				UserMenuItem umi = it.next();
				//The only difference between groups and Admin Menus
				//is that groups have some items set inactive (Admin
				//always active for all of them)
				//
				//So if myMenu does not contains umi
				//it means that there is but is not active
				if (myMenu.contains(umi)) {
					if (addMenuItem(myMenu.get(myMenu.indexOf(umi))) != null)
						junkMenu.add(umi);
				} else {
					umi.setActive(false);
					if (addMenuItem(umi) != null)
						junkMenu.add(umi);
				}
			}
			//cycle to remove already processed rootMenu items
			Iterator<UserMenuItem> altIt = junkMenu.iterator();
			while (altIt.hasNext()) {
				UserMenuItem umi = altIt.next();
				if (rootMenu.contains(umi))
					rootMenu.remove(umi);
			}
			junkMenu = new ArrayList<UserMenuItem>();
		}
		
		/*while (!myMenu.isEmpty()) {
			Iterator<UserMenuItem> it = myMenu.iterator();
			while (it.hasNext()) {
				UserMenuItem umi = it.next();
				//System.out.println("adding..." + umi);
				if (addMenuItem(umi) != null)
					junkMenu.add(umi);
			}
			Iterator<UserMenuItem> altIt = junkMenu.iterator();
			while (altIt.hasNext()) {
				UserMenuItem umi = altIt.next();
				if (myMenu.contains(umi))
					myMenu.remove(umi);
			}
			junkMenu = new ArrayList<UserMenuItem>();
		}*/

		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 1) {
						// mySingleClick(selRow, selPath);
					} else if (e.getClickCount() == 2) {
						doubleClick(selRow, selPath);
					}
				}
			}
		};
		tree.addMouseListener(ml);

		//set up node icons
		UserItemNameTreeCellRenderer renderer = new UserItemNameTreeCellRenderer();
		
		//no icon on leaves
		renderer.setLeafIcon(new ImageIcon(""));
		tree.setCellRenderer(renderer);

		add(new JScrollPane(tree), BorderLayout.CENTER);

		addButton();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void doubleClick(int selRow, TreePath selPath) {
		//System.out.println("Double " + selRow + " " + selPath);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
				.getLastPathComponent();
		UserMenuItem umi = (UserMenuItem) node.getUserObject();
		
		/* Also if node has leafs can be deactivated */
		String user = UserBrowsingManager.getCurrentUser();
		String umiFile = umi.getCode();
		
		if (user.equals("admin") && (umiFile.equals("file") ||
				umiFile.equals("groups") || umiFile.equals("users") || 
				umiFile.equals("usersusers") || umiFile.equals("exit"))) {
			return;
		}
		
		if (umi.isActive())
			umi.setActive(false);
		else
			umi.setActive(true);
			
		tree.expandPath(selPath);
	}

	public void mySingleClick(int selRow, TreePath selPath) {
		System.out.println(MessageBundle.getMessage("angal.menu.single") + selRow + " " + selPath);
	}

	
	public void addButton() {
		JPanel panel = new JPanel();

		ActionListener addListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				ArrayList<UserMenuItem> newUserMenu = new ArrayList<UserMenuItem>();
				
				Enumeration<?> e = root.breadthFirstEnumeration();
				while (e.hasMoreElements()) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
					UserMenuItem umi= (UserMenuItem)node.getUserObject();
					//System.out.println(umi+" "+umi.isActive());
					if (!umi.getCode().equals("main")) newUserMenu.add(umi);
				}	
				UserBrowsingManager manager = new UserBrowsingManager();
                try {
                    manager.setGroupMenu(aGroup, newUserMenu);
                } catch (OHServiceException e1) {
                    OHServiceExceptionUtil.showMessages(e1);
                }
                dispose();
			}
		};

		JButton addButton = new JButton(MessageBundle.getMessage("angal.menu.update"));
		addButton.addActionListener(addListener);
		panel.add(addButton);

		add(panel, BorderLayout.SOUTH);
	}

	/**
	 * Finds an object in the tree.
	 * 
	 * @param obj
	 *            the object to find
	 * @return the node containing the object or null if the object is not
	 *         present in the tree
	 */
	public DefaultMutableTreeNode findUserObject(Object obj) {
		// find the node containing a user object
		Enumeration<?> e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
					.nextElement();
			if (node.getUserObject().equals(obj))
				return node;
		}
		return null;
	}

	public DefaultMutableTreeNode findParent(Object obj) {
		// find the node containing a user object
		Enumeration<?> e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
					.nextElement();

			if (((UserMenuItem) node.getUserObject()).getCode().equals(
					((UserMenuItem) obj).getMySubmenu()))
				return node;
		}
		return null;
	}

	/**
	 * Adds a new item
	 * 
	 * @param c
	 * 
	 * @return the newly added node.
	 */
	public DefaultMutableTreeNode addMenuItem(UserMenuItem c) {
		
		// if the class is already in the tree, return its node
		DefaultMutableTreeNode node = findUserObject(c);
		if (node != null)
			return node;

		DefaultMutableTreeNode parent = findParent(c);

		if (parent == null)
			return null;

		int pos = 0;
		for (int i = 0; i < parent.getChildCount(); i++) {
			UserMenuItem n = (UserMenuItem) ((DefaultMutableTreeNode) parent
					.getChildAt(i)).getUserObject();
			if (n.getPosition() < c.getPosition())
				pos++;
		}

		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(c);
		/*
		System.out.println("inserting " + newNode + " in " + parent + " at "
				+ c.getPosition() + " in "
				+ Math.min(parent.getChildCount(), c.getPosition()));
		*/		
		
		model.insertNodeInto(newNode, parent, pos);
		// make node visible
		TreePath path = new TreePath(model.getPathToRoot(newNode));
		tree.makeVisible(path);

		return newNode;
	}

	private DefaultMutableTreeNode root;

	private DefaultTreeModel model;

	private JTree tree;

}

/**
 * This class renders a item name 
 * 
 */
class UserItemNameTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 11L;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);
		// get the user object
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		UserMenuItem c = (UserMenuItem) node.getUserObject();

		// the first time, derive italic font from plain font
		if (plainFont == null) {
			plainFont = // getFont();
			new Font("Arial", Font.BOLD, 16);
		}
		
		if (c.isActive()) {
			if (c.isASubMenu()) {
				setForeground(Color.BLUE);
				setFont(plainFont);
			}
			else {
				setForeground(Color.BLACK);
				setFont(plainFont);
			}
		} else {
			setForeground(Color.LIGHT_GRAY);
			setFont(plainFont);
		}
		return this;
	}

	private Font plainFont = null;

}
