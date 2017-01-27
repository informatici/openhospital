package org.isf.menu.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.isf.menu.model.GroupMenu;
import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;
import org.isf.menu.model.UserMenuItem;
import org.isf.menu.service.MenuIoOperations;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests  
{
	private static DbJpaUtil jpa;
	private static TestUser testUser;
	private static TestUserContext testUserContext;
	private static TestUserGroup testUserGroup;
	private static TestUserGroupContext testUserGroupContext;
	private static TestUserMenu testUserMenu;
	private static TestUserMenuContext testUserMenuContext;
	private static TestGroupMenuContext testGroupMenuContext;
	private static TestGroupMenu testGroupMenu;
		
	
	@BeforeClass
    public static void setUpClass()  
    {
    	jpa = new DbJpaUtil();
    	testUser = new TestUser();
    	testUserContext = new TestUserContext();
    	testUserGroup = new TestUserGroup();
    	testUserGroupContext = new TestUserGroupContext();
    	testUserMenu = new TestUserMenu();
    	testUserMenuContext = new TestUserMenuContext();
    	testGroupMenu = new TestGroupMenu();
    	testGroupMenuContext = new TestGroupMenuContext();
    	
        return;
    }

    @Before
    public void setUp() throws OHException
    {
        jpa.open();
        
        _saveContext();
		
		return;
    }
        
    @After
    public void tearDown() throws Exception 
    {
        _restoreContext();   
        
        jpa.flush();
        jpa.close();
                
        return;
    }
    
    @AfterClass
    public static void tearDownClass() throws OHException 
    {
    	jpa.destroy();

    	return;
    }
	
		
	@Test
	public void testUserGroupGets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestUserGroup(false);
			_checkUserGroupIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testUserGroupSets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestUserGroup(true);
			_checkUserGroupIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	
	
	@Test
	public void testUserGets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestUser(false);
			_checkUserIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testUserSets() throws OHException 
	{
		String code = "";
			

		try 
		{		
			code = _setupTestUser(true);
			_checkUserIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testUserMenuGets() throws OHException 
	{
		String code = "";
			
	
		try 
		{		
			code = _setupTestUserMenu(false);
			_checkUserMenuIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testUserMenuSets() throws OHException 
	{
		String code = "";
			
	
		try 
		{		
			code = _setupTestUserMenu(true);
			_checkUserMenuIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}	
	
	@Test
	public void testGroupMenuGets() throws OHException 
	{
		Integer code = 0;
			
	
		try 
		
		{		
			code = _setupTestGroupMenu(false);
			_checkGroupMenuIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
				
		return;
	}
	
	@Test
	public void testGroupMenuSets() throws OHException 
	{
		Integer code = 0;
		
		
		try 
		{		
			code = _setupTestGroupMenu(true);
			_checkGroupMenuIntoDb(code);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetUser() 
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		
		
		try 
		{		
			code = _setupTestUser(false);
			User foundUser = (User)jpa.find(User.class, code); 
			ArrayList<User> users = ioOperations.getUser();
			
			assertEquals(foundUser.getDesc(), users.get(users.size()-1).getDesc());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetUserFromId() 
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		
		
		try 
		{		
			code = _setupTestUser(false);
			User foundUser = (User)jpa.find(User.class, code); 
			ArrayList<User> users = ioOperations.getUser(foundUser.getUserGroupName().getCode());
			
			assertEquals(foundUser.getDesc(), users.get(users.size()-1).getDesc());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetUserInfo() 
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		
		
		try 
		{		
			code = _setupTestUser(false);
			User foundUser = (User)jpa.find(User.class, code); 
			String description = ioOperations.getUsrInfo(foundUser.getUserName());
			
			assertEquals(foundUser.getDesc(), description);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetUserGroup() 
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		
		
		try 
		{		
			code = _setupTestUserGroup(false);
			UserGroup foundUserGroup = (UserGroup)jpa.find(UserGroup.class, code); 
			ArrayList<UserGroup> userGroups = ioOperations.getUserGroup();
			
			assertEquals(foundUserGroup.getDesc(), userGroups.get(userGroups.size()-1).getDesc());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoIsUserNamePresent() 
	{
		String code = "admin";
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestUser(false);
			result = ioOperations.isUserNamePresent(code);
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoIsGroupNamePresent() 
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestUserGroup(false);
			result = ioOperations.isGroupNamePresent(code);
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewUser() 
	{
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false; 
		
		
		try 
		{			
			UserGroup userGroup = testUserGroup.setup(false);
			
			jpa.beginTransaction();
			User user = testUser.setup(userGroup, false);
			jpa.persist(userGroup);
			jpa.commitTransaction();
			result = ioOperations.newUser(user);
			
			assertEquals(true, result);
			_checkUserIntoDb(user.getUserName());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateUser()
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestUser(false);
			User foundUser = (User)jpa.find(User.class, code); 
			foundUser.setDesc("Update");
			result = ioOperations.updateUser(foundUser);
			User updateUser = (User)jpa.find(User.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateUser.getDesc());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void updatePassword()
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestUser(false);
			User foundUser = (User)jpa.find(User.class, code); 
			foundUser.setPasswd("Update");
			result = ioOperations.updatePassword(foundUser);
			User updateDisease = (User)jpa.find(User.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateDisease.getPasswd());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoDeleteDisease() 
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestUser(false);
			User foundUser = (User)jpa.find(User.class, code); 
			result = ioOperations.deleteUser(foundUser);
			
			assertEquals(true, result);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
		
	@Test
	public void testIoGetMenu() 
	{
		MenuIoOperations ioOperations = new MenuIoOperations();
		
		
		try 
		{		
			jpa.beginTransaction();	
			UserGroup userGroup = testUserGroup.setup(false);
			User user = testUser.setup(userGroup, false);
			UserMenuItem menuItem = testUserMenu.setup(false);
			GroupMenu groupMenu = new GroupMenu(1, userGroup.getCode(), menuItem.getCode(), 'Y');
			jpa.persist(userGroup);
			jpa.persist(user);
			jpa.persist(menuItem);
			jpa.persist(groupMenu);
			jpa.commitTransaction();
			 
			ArrayList<UserMenuItem> menus = ioOperations.getMenu(user);
			
			assertEquals(menuItem.getCode(), menus.get(menus.size()-1).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoGetGroupMenu() 
	{
		MenuIoOperations ioOperations = new MenuIoOperations();
		
		
		try 
		{		
			jpa.beginTransaction();	
			UserGroup userGroup = testUserGroup.setup(false);
			User user = testUser.setup(userGroup, false);
			UserMenuItem menuItem = testUserMenu.setup(false);
			GroupMenu groupMenu = new GroupMenu(1, userGroup.getCode(), menuItem.getCode(), 'Y');
			jpa.persist(userGroup);
			jpa.persist(user);
			jpa.persist(menuItem);
			jpa.persist(groupMenu);
			jpa.commitTransaction();
			 
			ArrayList<UserMenuItem> menus = ioOperations.getGroupMenu(userGroup);
			
			assertEquals(menuItem.getCode(), menus.get(menus.size()-1).getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoSetGroupMenu() 
	{
		//TODO: Do unit test checking insert
		assertEquals(true, false);	
		
		return;
	}
	
	@Test
	public void testIoDeleteUserGroup() 
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false;
		

		try 
		{		
			code = _setupTestUserGroup(false);
			UserGroup foundUserGroup = (UserGroup)jpa.find(UserGroup.class, code); 
			result = ioOperations.deleteGroup(foundUserGroup);
			
			assertEquals(true, result);
			UserGroup deletedUserGroup = (UserGroup)jpa.find(UserGroup.class, code); 
			assertEquals(null, deletedUserGroup);
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoNewUserGroup() 
	{
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false; 
		
		
		try 
		{						
			UserGroup userGroup= testUserGroup.setup(false);
			result = ioOperations.newUserGroup(userGroup);
			
			assertEquals(true, result);
			_checkUserGroupIntoDb(userGroup.getCode());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	@Test
	public void testIoUpdateUserGroup()
	{
		String code = "";
		MenuIoOperations ioOperations = new MenuIoOperations();
		boolean result = false;
		
		
		try 
		{		
			code = _setupTestUserGroup(false);
			UserGroup foundUserGroup = (UserGroup)jpa.find(UserGroup.class, code); 
			foundUserGroup.setDesc("Update");;
			result = ioOperations.updateUserGroup(foundUserGroup);
			UserGroup updateUserGroup = (UserGroup)jpa.find(UserGroup.class, code); 
			
			assertEquals(true, result);
			assertEquals("Update", updateUserGroup.getDesc());
		} 
		catch (Exception e) 
		{
			System.out.println("==> Test Exception: " + e);		
			assertEquals(true, false);
		}
		
		return;
	}
	
	
	private void _saveContext() throws OHException 
    {	
		testUserGroupContext.saveAll(jpa);
		testUserContext.saveAll(jpa);
		testUserMenuContext.saveAll(jpa);
		testGroupMenuContext.saveAll(jpa);
        		
        return;
    }
	
    private void _restoreContext() throws OHException 
    {
    	System.out.println(testUserContext.getAllSaved());
		System.out.println(testUserGroupContext.getAllSaved());
		System.out.println(testUserMenuContext.getAllSaved());
		System.out.println(testGroupMenuContext.getAllSaved());
		testUserContext.deleteNews(jpa);
		testUserGroupContext.deleteNews(jpa);
		testUserMenuContext.deleteNews(jpa);
		testGroupMenuContext.deleteNews(jpa);
        
        return;
    }
        
	private String _setupTestUserGroup(
			boolean usingSet) throws OHException 
	{
		UserGroup userGroup;
		

    	jpa.beginTransaction();	
    	userGroup = testUserGroup.setup(usingSet);
		jpa.persist(userGroup);
    	jpa.commitTransaction();
    	
		return userGroup.getCode();
	}
		
	private void  _checkUserGroupIntoDb(
			String code) throws OHException 
	{
		UserGroup foundUserGroup;
		

		foundUserGroup = (UserGroup)jpa.find(UserGroup.class, code); 
		testUserGroup.check(foundUserGroup);
		
		return;
	}	
    
	private String _setupTestUser(
			boolean usingSet) throws OHException 
	{
		User user;
		UserGroup userGroup = testUserGroup.setup(usingSet);
		
	
		jpa.beginTransaction();	
		user = testUser.setup(userGroup, usingSet);
		jpa.persist(userGroup);
		jpa.persist(user);
		jpa.commitTransaction();
		
		return user.getUserName();
	}
		
	private void  _checkUserIntoDb(
			String code) throws OHException 
	{
		User foundUser;
		
	
		foundUser = (User)jpa.find(User.class, code); 
		testUser.check(foundUser);
		
		return;
	}		
	
	private String _setupTestUserMenu(
			boolean usingSet) throws OHException 
	{
		UserMenuItem userMenu;
		
	
		jpa.beginTransaction();	
		userMenu = testUserMenu.setup(usingSet);
		jpa.persist(userMenu);
		jpa.commitTransaction();
		
		return userMenu.getCode();
	}
		
	private void  _checkUserMenuIntoDb(
			String code) throws OHException 
	{
		UserMenuItem foundUserMenu;
		
	
		foundUserMenu = (UserMenuItem)jpa.find(UserMenuItem.class, code); 
		testUserMenu.check(foundUserMenu);
		
		return;
	}
	
	private Integer _setupTestGroupMenu(
			boolean usingSet) throws OHException 
	{
		GroupMenu groupMenu;
		
	
		jpa.beginTransaction();	
		groupMenu = testGroupMenu.setup(usingSet);
		jpa.persist(groupMenu);
		jpa.commitTransaction();
		
		return groupMenu.getCode();
	}
		
	private void  _checkGroupMenuIntoDb(
			Integer code) throws OHException 
	{
		GroupMenu foundGroupMenu;
		
	
		foundGroupMenu = (GroupMenu)jpa.find(GroupMenu.class, code); 
		testGroupMenu.check(foundGroupMenu);
		
		return;
	}
}


















