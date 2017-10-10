package org.isf.menu.manager;

import java.util.*;

import javax.swing.JOptionPane;

import org.isf.menu.gui.Menu;
import org.isf.menu.model.*;
import org.isf.menu.service.*;
import org.isf.utils.exception.OHException;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.utils.exception.model.OHSeverityLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBrowsingManager {

    private final Logger logger = LoggerFactory.getLogger(UserBrowsingManager.class);
	
	private MenuIoOperations ioOperations = Menu.getApplicationContext().getBean(MenuIoOperations.class);
	
	/**
	 * returns the list of {@link User}s
	 * @return the list of {@link User}s
	 */
	public ArrayList<User> getUser() throws OHServiceException {
		try {
			return ioOperations.getUser();
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * returns the list of {@link User}s in specified groupID
	 * @param groupID - the group ID
	 * @return the list of {@link User}s
	 */
	public ArrayList<User> getUser(String groupID) throws OHServiceException {
		try {
			return ioOperations.getUser(groupID);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * inserts a new {@link User} in the DB
	 * @param user - the {@link User} to insert
	 * @return <code>true</code> if the user has been inserted, <code>false</code> otherwise.
	 */
	public boolean newUser(User user) throws OHServiceException {
		try {
			String username = user.getUserName();
			if (ioOperations.isUserNamePresent(username)) {
                throw new OHServiceException(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                        MessageBundle.getMessage("angal.menu.theuser") +
                                " " + username + " " + MessageBundle.getMessage("angal.menu.isalreadypresent"), OHSeverityLevel.ERROR));
			} else{
			    return ioOperations.newUser(user);
            }
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
		
	}
	
	/**
	 * updates an existing {@link User} in the DB
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 */
	public boolean updateUser(User user) throws OHServiceException {
		try {
			return ioOperations.updateUser(user);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * updates the password of an existing {@link User} in the DB
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 */
	public boolean updatePassword(User user) throws OHServiceException {
		try {
			return ioOperations.updatePassword(user);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * deletes an existing {@link User}
	 * @param user - the {@link User} to delete
	 * @return <code>true</code> if the user has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteUser(User user) throws OHServiceException {
		try {
			return ioOperations.deleteUser(user);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * returns the list of {@link UserGroup}s
	 * @return the list of {@link UserGroup}s
	 */
	public ArrayList<UserGroup> getUserGroup() throws OHServiceException {
		try {
			return ioOperations.getUserGroup();
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link User}
	 * @param aUser - the {@link User}
	 * @return the list of {@link UserMenuItem}s 
	 */
	public ArrayList<UserMenuItem> getMenu(User aUser) throws OHServiceException {
		try {
			return ioOperations.getMenu(aUser);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link UserGroup}
	 * @param aGroup - the {@link UserGroup}
	 * @return the list of {@link UserMenuItem}s 
	 */
	public ArrayList<UserMenuItem> getGroupMenu(UserGroup aGroup) throws OHServiceException {
		try {
			return ioOperations.getGroupMenu(aGroup);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * replaces the {@link UserGroup} rights
	 * @param aGroup - the {@link UserGroup}
	 * @param menu - the list of {@link UserMenuItem}s
	 * @return <code>true</code> if the menu has been replaced, <code>false</code> otherwise.
	 */
	public boolean setGroupMenu(UserGroup aGroup, ArrayList<UserMenuItem> menu) throws OHServiceException {
		try {
			return ioOperations.setGroupMenu(aGroup,menu,false);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * returns {@link User} description from its username
	 * @param userName - the {@link User}'s username
	 * @return the {@link User}'s description
	 */
	public String getUsrInfo(String userName) throws OHServiceException {
		try {
			return ioOperations.getUsrInfo(userName);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.couldntfindthedataithasprobablybeendeleted"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * deletes a {@link UserGroup}
	 * @param aGroup - the {@link UserGroup} to delete
	 * @return <code>true</code> if the group has been deleted, <code>false</code> otherwise.
	 */
	public boolean deleteGroup(UserGroup aGroup) throws OHServiceException {
		if (aGroup.getCode().equals("admin")){
		    throw new OHServiceException(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.menu.youcantdeletegroupadmin"), OHSeverityLevel.WARNING));
		}
		ArrayList<User> users = getUser(aGroup.getCode());
		if (users != null && users.size()>0){

            throw new OHServiceException(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.menu.thisgrouphasusers"), OHSeverityLevel.WARNING));
		}
		try {
			return ioOperations.deleteGroup(aGroup);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * insert a new {@link UserGroup} with a minimum set of rights
	 * @param aGroup - the {@link UserGroup} to insert
	 * @return <code>true</code> if the group has been inserted, <code>false</code> otherwise.
	 */
	public boolean newUserGroup(UserGroup aGroup) throws OHServiceException {
		try {
			String code = aGroup.getCode();
			if (ioOperations.isGroupNamePresent(code)) {
                throw new OHServiceException(new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                        MessageBundle.getMessage("angal.menu.thegroup") +
                                " " + code + " " + MessageBundle.getMessage("angal.menu.isalreadypresent"), OHSeverityLevel.ERROR));
			} else{
			    return ioOperations.newUserGroup(aGroup);
            }
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
	
	/**
	 * updates an existing {@link UserGroup} in the DB
	 * @param aGroup - the {@link UserGroup} to update
	 * @return <code>true</code> if the group has been updated, <code>false</code> otherwise.
	 */
	public boolean updateUserGroup(UserGroup aGroup) throws OHServiceException {
		try {
			return ioOperations.updateUserGroup(aGroup);
        }catch(OHException e){
			/*Already cached exception with OH specific error message -
			 * create ready to return OHServiceException and keep existing error message
			 */
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    e.getMessage(), OHSeverityLevel.ERROR));
        }catch(Exception e){
            //Any exception
            logger.error("", e);
            throw new OHServiceException(e, new OHExceptionMessage(MessageBundle.getMessage("angal.hospital"),
                    MessageBundle.getMessage("angal.sql.thedatacouldnotbesaved"), OHSeverityLevel.ERROR));
        }
	}
}