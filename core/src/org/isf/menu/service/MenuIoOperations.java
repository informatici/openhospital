package org.isf.menu.service;

import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;
import org.isf.menu.model.UserMenuItem;
import org.isf.utils.db.TranslateOHServiceException;
import org.isf.utils.exception.OHServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional(rollbackFor=OHServiceException.class)
@TranslateOHServiceException
public class MenuIoOperations 
{
	@Autowired
	private UserIoOperationRepository repository;
	@Autowired
	private UserGroupIoOperationRepository groupRepository;
	@Autowired
	private UserMenuItemIoOperationRepository menuRepository;
	@Autowired
	private GroupMenuIoOperationRepository groupMenuRepository;
	
	/**
	 * returns the list of {@link User}s
	 * 
	 * @return the list of {@link User}s
	 * @throws OHServiceException
	 */
	public ArrayList<User> getUser() throws OHServiceException 
	{
		ArrayList<User> users = (ArrayList<User>) repository.findAllByOrderByUserNameAsc();
				
			
		return users;
	}

	/**
	 * returns the list of {@link User}s in specified groupID
	 * 
	 * @param groupID - the group ID
	 * @return the list of {@link User}s
	 * @throws OHServiceException
	 */
	public ArrayList<User> getUser(
			String groupID) throws OHServiceException 
	{
		ArrayList<User> users = (ArrayList<User>) repository.findAllWhereUserGroupNameByOrderUserNameAsc(groupID);
				
			
		return users;
	}
	
	/**
	 * returns {@link User} from its username
	 * @param userName - the {@link User}'s username
	 * @return {@link User}
	 * @throws OHServiceException
	 */
	public User getUserByName(String userName) throws OHServiceException 
	{ 
		return repository.findByUserName(userName);
	}
	
	/**
	 * returns {@link User} description from its username
	 * @param userName - the {@link User}'s username
	 * @return the {@link User}'s description
	 * @throws OHServiceException
	 */
	public String getUsrInfo(
			String userName) throws OHServiceException 
	{ 
		User user = (User)repository.findOne(userName); 
		
		
		return user.getDesc();
	}
	
	/**
	 * returns the list of {@link UserGroup}s
	 * 
	 * @return the list of {@link UserGroup}s
	 * @throws OHServiceException
	 */
	public ArrayList<UserGroup> getUserGroup() throws OHServiceException 
	{
		ArrayList<UserGroup> users = (ArrayList<UserGroup>) groupRepository.findAllByOrderByCodeAsc();
				
			
		return users;
	}
	
	/**
	 * Checks if the specified {@link User} code is already present.
	 * 
	 * @param userName - the {@link User} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the check.
	 */
	public boolean isUserNamePresent(
			String userName) throws OHServiceException 
	{
		boolean result = true;
	
		
		result = repository.exists(userName);
		
		return result;	
	}
	
	/**
	 * Checks if the specified {@link UserGroup} code is already present.
	 * 
	 * @param groupName - the {@link UserGroup} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHServiceException if an error occurs during the check.
	 */
	public boolean isGroupNamePresent(
			String groupName) throws OHServiceException 
	{
		boolean result = true;
	
		
		result = groupRepository.exists(groupName);
		
		return result;	
	}
	
	/**
	 * inserts a new {@link User} in the DB
	 * 
	 * @param user - the {@link User} to insert
	 * @return <code>true</code> if the user has been inserted, <code>false</code> otherwise.
	 * @throws OHServiceException
	 */
	public boolean newUser(
			User user) throws OHServiceException 
	{
		boolean result = true;
	

		User savedUser = repository.save(user);
		result = (savedUser != null);
		
		return result;
	}
		
	/**
	 * updates an existing {@link User} in the DB
	 * 
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException
	 */
	public boolean updateUser(
			User user) throws OHServiceException 
	{
		boolean result = false;
		
				
		if (repository.updateDescription(user.getDesc(), user.getUserName()) > 0)
		{
			result = true;
		}
    	
		return result;
	}
	
	/**
	 * updates the password of an existing {@link User} in the DB
	 * 
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException
	 */
	public boolean updatePassword(
			User user) throws OHServiceException 
	{
		boolean result = false;
		
				
		if (repository.updatePassword(user.getPasswd(), user.getUserName()) > 0)
		{
			result = true;
		}
		
		return result;
	}

	/**
	 * deletes an existing {@link User}
	 * 
	 * @param user - the {@link User} to delete
	 * @return <code>true</code> if the user has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException
	 */
	public boolean deleteUser(
			User user) throws OHServiceException 
	{
		boolean result = true;
	
		
		repository.delete(user);
		
		return result;	
	}
	
	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link User}
	 * 
	 * @param aUser - the {@link User}
	 * @return the list of {@link UserMenuItem}s 
	 * @throws OHServiceException
	 */
	public ArrayList<UserMenuItem> getMenu(
			User aUser) throws OHServiceException 
	{
		ArrayList<UserMenuItem> menu = null;		
		List<Object[]> menuList = (List<Object[]>)menuRepository.findAllWhereId(aUser.getUserName());
		
		
		menu = new ArrayList<UserMenuItem>();
		for (Object[] object : menuList) {
			char active = (Character) object[9];
			UserMenuItem umi = new UserMenuItem();


			umi.setCode((String) object[0]);
			umi.setButtonLabel((String) object[1]);
			umi.setAltLabel((String) object[2]);
			umi.setTooltip((String) object[3]);
			umi.setShortcut((Character) object[4]);
			umi.setMySubmenu((String) object[5]);
			umi.setMyClass((String) object[6]);
			umi.setASubMenu((Character) object[7] == 'Y');
			umi.setPosition((Integer) object[8]);
			umi.setActive(active == 'Y');
			menu.add(umi);
		}
		
		return menu;
	}

	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link UserGroup}
	 * 
	 * @param aGroup - the {@link UserGroup}
	 * @return the list of {@link UserMenuItem}s 
	 * @throws OHServiceException
	 */
	public ArrayList<UserMenuItem> getGroupMenu(
			UserGroup aGroup) throws OHServiceException 
	{
		ArrayList<UserMenuItem> menu = (ArrayList<UserMenuItem>) menuRepository.findAllWhereGroupId(aGroup.getCode());
				
		
		return menu;
	}

	/**
	 * replaces the {@link UserGroup} rights
	 * 
	 * @param aGroup - the {@link UserGroup}
	 * @param menu - the list of {@link UserMenuItem}s
	 * @param insert - specify if is an insert or an update
	 * @return <code>true</code> if the menu has been replaced, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean setGroupMenu(
			UserGroup aGroup, 
			ArrayList<UserMenuItem> menu, 
			boolean insert) throws OHServiceException 
	{
		boolean result = true;

	
		result = _deleteGroupMenu(aGroup);
		
		for (UserMenuItem item : menu) {
			result = result && _insertGroupMenu(aGroup, item, insert);
		}
		
		return result;
	}
	
	public boolean _deleteGroupMenu(
			UserGroup aGroup) throws OHServiceException 
	{
		boolean result = true;
				

		groupMenuRepository.deleteWhereUserGroup(aGroup.getCode());
		
		return result;
	}
	
	public boolean _insertGroupMenu(
			UserGroup aGroup,
			UserMenuItem item, 
			boolean insert) throws OHServiceException 
	{
		boolean result = true;
				

		groupMenuRepository.insert(aGroup.getCode(), item.getCode(), (item.isActive() ? "Y" : "N"));
				
		return result;
	}
	
	/**
	 * deletes a {@link UserGroup}
	 * 
	 * @param aGroup - the {@link UserGroup} to delete
	 * @return <code>true</code> if the group has been deleted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean deleteGroup(
			UserGroup aGroup) throws OHServiceException 
	{
		boolean result = true;
		
		

		groupMenuRepository.deleteWhereUserGroup(aGroup.getCode());
		groupRepository.delete(aGroup);
		    	
		return result;
	}

	/**
	 * insert a new {@link UserGroup} with a minimum set of rights
	 * 
	 * @param aGroup - the {@link UserGroup} to insert
	 * @return <code>true</code> if the group has been inserted, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean newUserGroup(
			UserGroup aGroup) throws OHServiceException 
	{
		boolean result = true;
	

		UserGroup savedUser = groupRepository.save(aGroup);
		result = (savedUser != null); 
		
		return result;
	}

	/**
	 * updates an existing {@link UserGroup} in the DB
	 * 
	 * @param aGroup - the {@link UserGroup} to update
	 * @return <code>true</code> if the group has been updated, <code>false</code> otherwise.
	 * @throws OHServiceException 
	 */
	public boolean updateUserGroup(
			UserGroup aGroup) throws OHServiceException 
	{
		boolean result = false;
		
				
		if (groupRepository.updateDescription(aGroup.getDesc(), aGroup.getCode()) > 0)
		{
			result = true;
		}
    			
		return result;
	}
}
