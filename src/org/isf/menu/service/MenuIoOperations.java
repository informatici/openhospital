package org.isf.menu.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.model.GroupMenu;
import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;
import org.isf.menu.model.UserMenuItem;
import org.isf.patient.model.Patient;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuIoOperations 
{
	@Autowired
	private DbJpaUtil jpa;
	/**
	 * returns the list of {@link User}s
	 * 
	 * @return the list of {@link User}s
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<User> getUser() throws OHException 
	{
		
		ArrayList<User> users = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM USER ORDER BY US_ID_A";
		jpa.createQuery(query, User.class, false);
		List<User> userList = (List<User>)jpa.getList();
		users = new ArrayList<User>(userList);			
		
		jpa.commitTransaction();
	
		return users;
	}

	/**
	 * returns the list of {@link User}s in specified groupID
	 * 
	 * @param groupID - the group ID
	 * @return the list of {@link User}s
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<User> getUser(
			String groupID) throws OHException 
	{
		
		ArrayList<User> users = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM USER WHERE US_UG_ID_A = ? ORDER BY US_ID_A";
		jpa.createQuery(query, User.class, false);
		params.add(groupID);
		jpa.setParameters(params, false);		
		List<User> userList = (List<User>)jpa.getList();
		users = new ArrayList<User>(userList);			
		
		jpa.commitTransaction();
	
		return users;
	}
	
	/**
	 * returns {@link User} description from its username
	 * @param userName - the {@link User}'s username
	 * @return the {@link User}'s description
	 * @throws OHException
	 */
	public String getUsrInfo(
			String userName) throws OHException 
	{
		
		User user = null;
				
		
		jpa.beginTransaction();
		
		user = (User)jpa.find(User.class, userName); 
		
		jpa.commitTransaction();
		
		return user.getDesc();
	}
	
	/**
	 * returns the list of {@link UserGroup}s
	 * 
	 * @return the list of {@link UserGroup}s
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<UserGroup> getUserGroup() throws OHException 
	{
		
		ArrayList<UserGroup> users = null;
				
		
		jpa.beginTransaction();
		
		String query = "SELECT * FROM USERGROUP ORDER BY UG_ID_A";
		jpa.createQuery(query, UserGroup.class, false);
		List<UserGroup> userList = (List<UserGroup>)jpa.getList();
		users = new ArrayList<UserGroup>(userList);			
		
		jpa.commitTransaction();
	
		return users;
	}
	
	/**
	 * Checks if the specified {@link User} code is already present.
	 * 
	 * @param userName - the {@link User} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isUserNamePresent(
			String userName) throws OHException 
	{
		
		boolean present = false;
		
		
		jpa.beginTransaction();
		
		User foundUser = (User)jpa.find(User.class, userName); 
		if (foundUser != null)
		{
			present = true;
		}
		
		jpa.commitTransaction();
		
		return present;
	}
	
	/**
	 * Checks if the specified {@link UserGroup} code is already present.
	 * 
	 * @param groupName - the {@link UserGroup} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isGroupNamePresent(
			String groupName) throws OHException 
	{

		
		boolean present = false;
		
		
		jpa.beginTransaction();
		
		UserGroup foundUserGroup = (UserGroup)jpa.find(UserGroup.class, groupName); 
		if (foundUserGroup != null)
		{
			present = true;
		}
		
		jpa.commitTransaction();
		
		return present;
	}
	
	/**
	 * inserts a new {@link User} in the DB
	 * 
	 * @param user - the {@link User} to insert
	 * @return <code>true</code> if the user has been inserted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean newUser(
			User user) throws OHException 
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(user);
    	jpa.commitTransaction();
    	
		return result;
	}
		
	/**
	 * updates an existing {@link User} in the DB
	 * 
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean updateUser(
			User user) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		boolean result = true;
				

		jpa.beginTransaction();		
		
		try {
			query = "UPDATE USER SET US_DESC = ? WHERE US_ID_A = ?";
			jpa.createQuery(query, Patient.class, false);
			params.add(user.getDesc());
			params.add(user.getUserName());
			jpa.setParameters(params, false);
			jpa.executeUpdate();			
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();
    	
		return result;
	}
	
	/**
	 * updates the password of an existing {@link User} in the DB
	 * 
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean updatePassword(
			User user) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		boolean result = true;
				

		jpa.beginTransaction();		
		
		try {
			query = "UPDATE USER SET US_PASSWD = ? WHERE US_ID_A = ?";
			jpa.createQuery(query, Patient.class, false);
			params.add(user.getDesc());
			params.add(user.getUserName());
			jpa.setParameters(params, false);
			jpa.executeUpdate();			
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();
		
		return result;
	}

	/**
	 * deletes an existing {@link User}
	 * 
	 * @param user - the {@link User} to delete
	 * @return <code>true</code> if the user has been deleted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean deleteUser(
			User user) throws OHException 
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(user);
    	jpa.commitTransaction();
    	
		return result;
	}
	
	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link User}
	 * 
	 * @param aUser - the {@link User}
	 * @return the list of {@link UserMenuItem}s 
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<UserMenuItem> getMenu(
			User aUser) throws OHException 
	{
		
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<UserMenuItem> menu = null;
				
		
		jpa.beginTransaction();
		
		String query = "select mn.*,GROUPMENU.GM_ACTIVE as IS_ACTIVE from USERGROUP inner join USER on US_UG_ID_A=UG_ID_A "
				+ " inner join GROUPMENU on UG_ID_A=GM_UG_ID_A inner join MENUITEM as mn on "
				+ " GM_MNI_ID_A=mn.MNI_ID_A where US_ID_A = ? order by MNI_POSITION";
		
		Query q = jpa.getEntityManager().createNativeQuery(query,"UserMenuItemWithStatus");
		q.setParameter(1, aUser.getUserName());
		
		jpa.createQuery(query, UserMenuItem.class, false);
		params.add(aUser.getUserName());
		jpa.setParameters(params, false);
		
		List<Object[]> menuList = (List<Object[]>)q.getResultList();
		menu = new ArrayList<UserMenuItem>();
		Iterator<Object[]> it = menuList.iterator();
		while (it.hasNext()) {
			Object[] object = it.next();
			char active = (Character) object[1];
			UserMenuItem umi = (UserMenuItem)object[0];
			umi.setActive(active == 'Y' ? true : false);
			menu.add(umi);
		}
		
		jpa.commitTransaction();
		System.out.println(menu);
		return menu;
	}

	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link UserGroup}
	 * 
	 * @param aGroup - the {@link UserGroup}
	 * @return the list of {@link UserMenuItem}s 
	 * @throws OHException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<UserMenuItem> getGroupMenu(
			UserGroup aGroup) throws OHException 
	{
		
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<UserMenuItem> menu = null;
				
		
		jpa.beginTransaction();
		
		String query = "select mn.*,GROUPMENU.GM_ACTIVE from USERGROUP "
				+ " inner join GROUPMENU on UG_ID_A=GM_UG_ID_A inner join MENUITEM as mn on "
				+ " GM_MNI_ID_A=mn.MNI_ID_A where UG_ID_A = ? order by MNI_POSITION";
		jpa.createQuery(query, UserMenuItem.class, false);		
		params.add(aGroup.getCode());
		jpa.setParameters(params, false);
		List<UserMenuItem> menuList = (List<UserMenuItem>)jpa.getList();
		menu = new ArrayList<UserMenuItem>(menuList);			
		
		jpa.commitTransaction();
		
		return menu;
	}

	/**
	 * replaces the {@link UserGroup} rights
	 * 
	 * @param aGroup - the {@link UserGroup}
	 * @param menu - the list of {@link UserMenuItem}s
	 * @param insert - specify if is an insert or an update
	 * @return <code>true</code> if the menu has been replaced, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean setGroupMenu(
			UserGroup aGroup, 
			ArrayList<UserMenuItem> menu, 
			boolean insert) throws OHException 
	{
		boolean result = true;

	
		result = _deleteGroupMenu(aGroup);
		
		for (UserMenuItem item : menu) {
			result = result && _insertGroupMenu(aGroup, item, insert);
		}
		
		return result;
	}
	
	public boolean _deleteGroupMenu(
			UserGroup aGroup) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		boolean result = true;
				

		jpa.beginTransaction();		
		
		try {
			query = "DELETE FROM GROUPMENU WHERE GM_UG_ID_A = ?";
			jpa.createQuery(query, Patient.class, false);
			params.add(aGroup.getCode());
			jpa.setParameters(params, false);
			jpa.executeUpdate();			
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();
		
		return result;
	}
	
	public boolean _insertGroupMenu(
			UserGroup aGroup,
			UserMenuItem item, 
			boolean insert) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		boolean result = true;
				

		jpa.beginTransaction();		
		
		try {
			query = "INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) values(?, ?, ?)";
			jpa.createQuery(query, Patient.class, false);
			params.add(aGroup.getCode());
			params.add(item.getCode());
			params.add(item.isActive() ? "Y" : "N");
			jpa.setParameters(params, false);
			jpa.executeUpdate();			
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();
		
		return result;
	}
	
	/**
	 * deletes a {@link UserGroup}
	 * 
	 * @param aGroup - the {@link UserGroup} to delete
	 * @return <code>true</code> if the group has been deleted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteGroup(
			UserGroup aGroup) throws OHException 
	{
		
		ArrayList<Object> params = new ArrayList<Object>();
		boolean result = true;
		
		
		jpa.beginTransaction();		
		
		try {
			jpa.createQuery("DELETE FROM GROUPMENU WHERE GM_UG_ID_A = ?", GroupMenu.class, false);
			params.add(aGroup.getClass());
			jpa.setParameters(params, false);
			jpa.executeUpdate();
			jpa.remove(aGroup);
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 	
		
		jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * insert a new {@link UserGroup} with a minimum set of rights
	 * 
	 * @param aGroup - the {@link UserGroup} to insert
	 * @return <code>true</code> if the group has been inserted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean newUserGroup(
			UserGroup aGroup) throws OHException 
	{
		
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(aGroup);
    	jpa.commitTransaction();
    	
		return result;
	}

	/**
	 * updates an existing {@link UserGroup} in the DB
	 * 
	 * @param aGroup - the {@link UserGroup} to update
	 * @return <code>true</code> if the group has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateUserGroup(
			UserGroup aGroup) throws OHException 
	{
		ArrayList<Object> params = new ArrayList<Object>();
		String query = null;
		boolean result = true;
				

		jpa.beginTransaction();		
		
		try {
			query = "UPDATE USERGROUP SET UG_DESC = ? WHERE UG_ID_A = ?";
			jpa.createQuery(query, Patient.class, false);
			params.add(aGroup.getDesc());
			params.add(aGroup.getCode());
			jpa.setParameters(params, false);
			jpa.executeUpdate();			
		}  catch (OHException e) {
			result = false;
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} 				
	
		jpa.commitTransaction();
		
		return result;
	}
}
