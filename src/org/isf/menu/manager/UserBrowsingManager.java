package org.isf.menu.manager;

import java.util.*;

import javax.swing.JOptionPane;

import org.isf.menu.gui.Menu;
import org.isf.menu.model.*;
import org.isf.menu.service.*;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;

public class UserBrowsingManager {
	
	private IoOperations ioOperations = Menu.getApplicationContext().getBean(IoOperations.class);
	
	/**
	 * returns the list of {@link User}s
	 * @return the list of {@link User}s
	 */
	public ArrayList<User> getUser() {
		try {
			return ioOperations.getUser();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * returns the list of {@link User}s in specified groupID
	 * @param groupID - the group ID
	 * @return the list of {@link User}s
	 */
	public ArrayList<User> getUser(String groupID) {
		try {
			return ioOperations.getUser(groupID);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * inserts a new {@link User} in the DB
	 * @param user - the {@link User} to insert
	 * @return <code>true</code> if the user has been inserted, <code>false</code> otherwise.
	 */
	public boolean newUser(User user) {
		try {
			String username = user.getUserName();
			if (ioOperations.isUserNamePresent(username)) {
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.menu.theuser") +
						" " + username + " " + MessageBundle.getMessage("angal.menu.isalreadypresent"));
				return false;
			} else return ioOperations.newUser(user);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * updates an existing {@link User} in the DB
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 */
	public boolean updateUser(User user) {
		try {
			return ioOperations.updateUser(user);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * updates the password of an existing {@link User} in the DB
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 */
	public boolean updatePassword(User user) {
		try {
			return ioOperations.updatePassword(user);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * deletes an existing {@link User}
	 * @param user - the {@link User} to delete
	 * @return <code>true</code> if the user has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteUser(User user) {
		try {
			return ioOperations.deleteUser(user);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * returns the list of {@link UserGroup}s
	 * @return the list of {@link UserGroup}s
	 */
	public ArrayList<UserGroup> getUserGroup() {
		try {
			return ioOperations.getUserGroup();
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link User}
	 * @param aUser - the {@link User}
	 * @return the list of {@link UserMenuItem}s 
	 */
	public ArrayList<UserMenuItem> getMenu(User aUser) {
		try {
			return ioOperations.getMenu(aUser);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link UserGroup}
	 * @param aGroup - the {@link UserGroup}
	 * @return the list of {@link UserMenuItem}s 
	 */
	public ArrayList<UserMenuItem> getGroupMenu(UserGroup aGroup) {
		try {
			return ioOperations.getGroupMenu(aGroup);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * replaces the {@link UserGroup} rights
	 * @param aGroup - the {@link UserGroup}
	 * @param menu - the list of {@link UserMenuItem}s
	 * @return <code>true</code> if the menu has been replaced, <code>false</code> otherwise.
	 */
	public boolean setGroupMenu(UserGroup aGroup, ArrayList<UserMenuItem> menu){
		try {
			return ioOperations.setGroupMenu(aGroup,menu,false);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * returns {@link User} description from its username
	 * @param userName - the {@link User}'s username
	 * @return the {@link User}'s description
	 */
	public String getUsrInfo(String userName){
		try {
			return ioOperations.getUsrInfo(userName);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return null;
		}
	}
	
	/**
	 * deletes a {@link UserGroup}
	 * @param aGroup - the {@link UserGroup} to delete
	 * @return <code>true</code> if the group has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteGroup(UserGroup aGroup){
		if (aGroup.getCode().equals("admin")){
			JOptionPane.showMessageDialog(				
                    null,
                    MessageBundle.getMessage("angal.menu.youcantdeletegroupadmin"),
                    MessageBundle.getMessage("angal.hospital"),
                    JOptionPane.WARNING_MESSAGE);	
			return false;
		}
		ArrayList<User> users = getUser(aGroup.getCode());
		if (users != null && users.size()>0){
			JOptionPane.showMessageDialog(				
                    null,
                    MessageBundle.getMessage("angal.menu.thisgrouphasusers"),
                    MessageBundle.getMessage("angal.hospital"),
                    JOptionPane.WARNING_MESSAGE);
			return false;
		}
		try {
			return ioOperations.deleteGroup(aGroup);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * insert a new {@link UserGroup} with a minimum set of rights
	 * @param aGroup - the {@link UserGroup} to insert
	 * @return <code>true</code> if the group has been inserted, <code>false</code> otherwise.
	 */
	public boolean newUserGroup(UserGroup aGroup){
		try {
			String code = aGroup.getCode();
			if (ioOperations.isGroupNamePresent(code)) {
				JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.menu.thegroup") +
						" " + code + " " + MessageBundle.getMessage("angal.menu.isalreadypresent"));
				return false;
			} else return ioOperations.newUserGroup(aGroup);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
	
	/**
	 * updates an existing {@link UserGroup} in the DB
	 * @param aGroup - the {@link UserGroup} to update
	 * @return <code>true</code> if the group has been updated, <code>false</code> otherwise.
	 */
	public boolean updateUserGroup(UserGroup aGroup){
		try {
			return ioOperations.updateUserGroup(aGroup);
		} catch (OHException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return false;
		}
	}
}