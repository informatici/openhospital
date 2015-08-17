package org.isf.menu.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.isf.generaldata.MessageBundle;
import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;
import org.isf.menu.model.UserMenuItem;
import org.isf.utils.db.DbQueryLogger;
import org.isf.utils.exception.OHException;

public class IoOperations {

	/**
	 * returns the list of {@link User}s
	 * 
	 * @return the list of {@link User}s
	 * @throws OHException
	 */
	public ArrayList<User> getUser() throws OHException {
		ArrayList<User> users = null;
		String sqlString = "SELECT * FROM USER ORDER BY US_ID_A";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(sqlString,true);
			users = new ArrayList<User>(resultSet.getFetchSize());

			while (resultSet.next()) {
				users.add(new User(resultSet.getString("US_ID_A"), 
						resultSet.getString("US_UG_ID_A"), 
						resultSet.getString("US_PASSWD"), 
						resultSet.getString("US_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return users;
	}

	/**
	 * returns the list of {@link User}s in specified groupID
	 * 
	 * @param groupID - the group ID
	 * @return the list of {@link User}s
	 * @throws OHException
	 */
	public ArrayList<User> getUser(String groupID) throws OHException {
		ArrayList<User> users = null;
		String string = "SELECT * FROM USER WHERE US_UG_ID_A = ? ORDER BY US_ID_A";
		List<Object> parameters = Collections.<Object>singletonList(groupID);
		
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(string, parameters, true);
			users = new ArrayList<User>(resultSet.getFetchSize());

			while (resultSet.next()) {
				users.add(new User(resultSet.getString("US_ID_A"), 
						resultSet.getString("US_UG_ID_A"), 
						resultSet.getString("US_PASSWD"), 
						resultSet.getString("US_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return users;
	}
	
	/**
	 * returns {@link User} description from its username
	 * @param userName - the {@link User}'s username
	 * @return the {@link User}'s description
	 * @throws OHException
	 */
	public String getUsrInfo(String userName) throws OHException {
		String string = "SELECT US_DESC FROM USER WHERE US_ID_A = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(userName);
		String info = null;
		try{
			ResultSet result =dbQuery.getDataWithParams(string, parameters, true);
			while(result.next()) info = result.getString(1);
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return info;
	}
	
	/**
	 * returns the list of {@link UserGroup}s
	 * 
	 * @return the list of {@link UserGroup}s
	 * @throws OHException
	 */
	public ArrayList<UserGroup> getUserGroup() throws OHException {
		ArrayList<UserGroup> group = null;
		String string = "SELECT * FROM USERGROUP ORDER BY UG_ID_A";
		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getData(string,true);
			group = new ArrayList<UserGroup>(resultSet.getFetchSize());

			while (resultSet.next()) {
				group.add(new UserGroup(
						resultSet.getString("UG_ID_A"),
						resultSet.getString("UG_DESC")));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return group;
	}
	
	/**
	 * Checks if the specified {@link User} code is already present.
	 * 
	 * @param userName - the {@link User} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isUserNamePresent(String userName) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(userName);
			String query = "SELECT US_ID_A FROM USER WHERE US_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			if(set.first()) present = true;

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return present;
	}
	
	/**
	 * Checks if the specified {@link UserGroup} code is already present.
	 * 
	 * @param groupName - the {@link UserGroup} code to check.
	 * @return <code>true</code> if the medical code is already stored, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean isGroupNamePresent(String groupName) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		boolean present = false;
		try{
			List<Object> parameters = Collections.<Object>singletonList(groupName);
			String query = "SELECT UG_ID_A FROM USERGROUP WHERE UG_ID_A = ?";
			ResultSet set = dbQuery.getDataWithParams(query, parameters, true);
			if(set.first()) present = true;

		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally{
			dbQuery.releaseConnection();
		}	
		return present;
	}
	
	/**
	 * inserts a new {@link User} in the DB
	 * 
	 * @param user - the {@link User} to insert
	 * @return <code>true</code> if the user has been inserted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean newUser(User user) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		String sqlString = "INSERT INTO USER (US_ID_A , US_UG_ID_A, US_PASSWD, US_DESC ) VALUES (?, ?, ?, ?)";
		parameters.add(user.getUserName());
		parameters.add(user.getUserGroupName());
		parameters.add(user.getPasswd());
		parameters.add(user.getDesc());
			
		result = dbQuery.setDataWithParams(sqlString, parameters, true);

		dbQuery.releaseConnection();
		return result;
	}
	
	/**
	 * updates an existing {@link User} in the DB
	 * 
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean updateUser(User user) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;

		String sqlstring = "UPDATE USER SET US_DESC = ? WHERE US_ID_A = ?";
		parameters.add(user.getDesc());
		parameters.add(user.getUserName());
		
		result = dbQuery.setDataWithParams(sqlstring, parameters, true);

		dbQuery.releaseConnection();
		return result;
	}
	
	/**
	 * updates the password of an existing {@link User} in the DB
	 * 
	 * @param user - the {@link User} to update
	 * @return <code>true</code> if the user has been updated, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean updatePassword(User user) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;

		String sqlstring = "UPDATE USER SET US_PASSWD = ? WHERE US_ID_A = ?";
		parameters.add(user.getPasswd());
		parameters.add(user.getUserName());
		
		result = dbQuery.setDataWithParams(sqlstring, parameters, true);

		dbQuery.releaseConnection();
		return result;
	}

	/**
	 * deletes an existing {@link User}
	 * 
	 * @param user - the {@link User} to delete
	 * @return <code>true</code> if the user has been deleted, <code>false</code> otherwise.
	 * @throws OHException
	 */
	public boolean deleteUser(User user) throws OHException {
		String string = "DELETE FROM USER WHERE US_ID_A = ?";
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(user.getUserName());
		boolean result = false;
		
		result = dbQuery.setDataWithParams(string, parameters, true);
		
		dbQuery.releaseConnection();
		return result;
	}
	
	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link User}
	 * 
	 * @param aUser - the {@link User}
	 * @return the list of {@link UserMenuItem}s 
	 * @throws OHException
	 */
	public ArrayList<UserMenuItem> getMenu(User aUser) throws OHException {
		ArrayList<UserMenuItem> menu = null;
		String string = "select mn.*,GROUPMENU.GM_ACTIVE from USERGROUP inner join USER on US_UG_ID_A=UG_ID_A "
				+ " inner join GROUPMENU on UG_ID_A=GM_UG_ID_A inner join MENUITEM as mn on "
				+ " GM_MNI_ID_A=mn.MNI_ID_A where US_ID_A = ? order by MNI_POSITION";
		List<Object> parameters = Collections.<Object>singletonList(aUser.getUserName());

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(string, parameters, true);
			menu = new ArrayList<UserMenuItem>(resultSet.getFetchSize());

			while (resultSet.next()) {
				menu.add(new UserMenuItem(resultSet.getString("MNI_ID_A"),
						MessageBundle.getMessage(resultSet.getString("MNI_BTN_LABEL")), 
						MessageBundle.getMessage(resultSet.getString("MNI_LABEL")), 
						resultSet.getString("MNI_TOOLTIP"), 
						((resultSet.getString("MNI_SHORTCUT") == null) ? 'x'
								: (char) (resultSet.getString("MNI_SHORTCUT")).charAt(0)), 
								resultSet.getString("MNI_SUBMENU"), 
								resultSet.getString("MNI_CLASS"), 
								(resultSet.getString("MNI_IS_SUBMENU").equals("Y") ? true : false),
						resultSet.getInt("MNI_POSITION"), 
						(resultSet.getString("GM_ACTIVE").equals("Y") ? true : false)));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
		return menu;
	}

	/**
	 * returns the list of {@link UserMenuItem}s that compose the menu for specified {@link UserGroup}
	 * 
	 * @param aGroup - the {@link UserGroup}
	 * @return the list of {@link UserMenuItem}s 
	 * @throws OHException
	 */
	public ArrayList<UserMenuItem> getGroupMenu(UserGroup aGroup) throws OHException {
		ArrayList<UserMenuItem> menu = null;
		String string = "select mn.*,GROUPMENU.GM_ACTIVE from USERGROUP "
				+ " inner join GROUPMENU on UG_ID_A=GM_UG_ID_A inner join MENUITEM as mn on "
				+ " GM_MNI_ID_A=mn.MNI_ID_A where UG_ID_A = ? order by MNI_POSITION";
		List<Object> parameters = Collections.<Object>singletonList(aGroup.getCode());

		DbQueryLogger dbQuery = new DbQueryLogger();
		try {
			ResultSet resultSet = dbQuery.getDataWithParams(string, parameters, true);
			menu = new ArrayList<UserMenuItem>(resultSet.getFetchSize());

			while (resultSet.next()) {
				menu.add(new UserMenuItem(resultSet.getString("MNI_ID_A"),
						MessageBundle.getMessage(resultSet.getString("MNI_BTN_LABEL")), 
						MessageBundle.getMessage(resultSet.getString("MNI_LABEL")), 
						resultSet.getString("MNI_TOOLTIP"), 
						((resultSet.getString("MNI_SHORTCUT") == null) ? 'x'
								: (char) (resultSet.getString("MNI_SHORTCUT")).charAt(0)), 
								resultSet.getString("MNI_SUBMENU"), 
								resultSet.getString("MNI_CLASS"), 
								(resultSet.getString("MNI_IS_SUBMENU").equals("Y") ? true : false),
						resultSet.getInt("MNI_POSITION"), 
						(resultSet.getString("GM_ACTIVE").equals("Y") ? true : false)));
			}
		} catch (SQLException e) {
			throw new OHException(MessageBundle.getMessage("angal.sql.problemsoccurredwiththesqlistruction"), e);
		} finally {
			dbQuery.releaseConnection();
		}
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
	public boolean setGroupMenu(UserGroup aGroup, ArrayList<UserMenuItem> menu, boolean insert) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		String sqlString = "DELETE FROM GROUPMENU WHERE GM_UG_ID_A = ?";
		parameters.add(aGroup.getCode());
		boolean result = true;

		dbQuery.setDataWithParams(sqlString, parameters, false);
		sqlString = "INSERT INTO GROUPMENU (GM_UG_ID_A, GM_MNI_ID_A, GM_ACTIVE) values(?, ?, ?)";	
		for (UserMenuItem item : menu) {
			parameters.clear();
			parameters.add(aGroup.getCode());
			parameters.add(item.getCode());
			parameters.add(item.isActive() ? "Y" : "N");
			result = result && dbQuery.setDataWithParams(sqlString, parameters, false);
		}
		
		if (result) dbQuery.commit();
		else dbQuery.rollback();
		
		if (!insert) dbQuery.releaseConnection();
		return result;
	}
	
	/**
	 * deletes a {@link UserGroup}
	 * 
	 * @param aGroup - the {@link UserGroup} to delete
	 * @return <code>true</code> if the group has been deleted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteGroup(UserGroup aGroup) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = Collections.<Object>singletonList(aGroup.getCode());
		boolean result = true;
	
		String sqlString = "DELETE FROM GROUPMENU WHERE GM_UG_ID_A = ?";
		dbQuery.setDataWithParams(sqlString, parameters, false);
		
		sqlString = "DELETE FROM USERGROUP WHERE UG_ID_A =?";
		dbQuery.setDataWithParams(sqlString, parameters, false);

		if (result) dbQuery.commit();
		else dbQuery.rollback();
		
		dbQuery.releaseConnection();
		return result;
	}

	/**
	 * insert a new {@link UserGroup} with a minimum set of rights
	 * 
	 * @param aGroup - the {@link UserGroup} to insert
	 * @return <code>true</code> if the group has been inserted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean newUserGroup(UserGroup aGroup) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		String sqlString = "INSERT INTO USERGROUP (UG_ID_A, UG_DESC) VALUES(?, ?)";
		parameters.add(aGroup.getCode());
		parameters.add(aGroup.getDesc());

		result = dbQuery.setDataWithParams(sqlString, parameters, true);
		
		// get 'admin' menu (complete)
		ArrayList<UserMenuItem> menu = getGroupMenu(new UserGroup("admin", ""));
		// remove everything except Exit and File items
		for (UserMenuItem e : menu) {
			// Setting inactive also for node
			if (!e.getCode().equalsIgnoreCase("EXIT") && 
				!e.getCode().equalsIgnoreCase("FILE"))
				e.setActive(false);
		}
		// set the new group rights
		result = result && setGroupMenu(aGroup, menu, true);
		
		dbQuery.releaseConnection();
		return result;
	}

	/**
	 * updates an existing {@link UserGroup} in the DB
	 * 
	 * @param aGroup - the {@link UserGroup} to update
	 * @return <code>true</code> if the group has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateUserGroup(UserGroup aGroup) throws OHException {
		DbQueryLogger dbQuery = new DbQueryLogger();
		List<Object> parameters = new ArrayList<Object>();
		boolean result = false;
		String sqlstring = "UPDATE USERGROUP SET UG_DESC = ? WHERE UG_ID_A = ?";
		
		parameters.add(aGroup.getDesc());
		parameters.add(aGroup.getCode());

		result = dbQuery.setDataWithParams(sqlstring, parameters, true);

		dbQuery.releaseConnection();
		return result;
	}
}
