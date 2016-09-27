package org.isf.menu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/*------------------------------------------
 * User - model for the user entity
 * -----------------------------------------
 * modification history
 * ? - ? - first version 
 * 07/05/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="USER")
public class User 
{
	@Id 
	@Column(name="US_ID_A")		
	private String userName;

	@NotNull
	@ManyToOne
	@JoinColumn(name="US_UG_ID_A")
	private UserGroup userGroupName;

	@NotNull
	@Column(name="US_PASSWD")
	private String passwd;

	@Column(name="US_DESC")
	private String desc;	
	
	@Transient
	private volatile int hashCode = 0;
			
	
	public User(){
	}
	
	public User(String aName, UserGroup aGroup, String aPasswd, String aDesc){
		this.userName = aName;
		this.userGroupName = aGroup;
		this.passwd = aPasswd;
		this.desc = aDesc;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public UserGroup getUserGroupName() {
		return userGroupName;
	}
	public void setUserGroupName(UserGroup userGroupName) {
		this.userGroupName = userGroupName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String toString(){
		return getUserName();		
	}
	
	@Override
	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof User) ? false
				: (getUserName().equalsIgnoreCase(
						((User) anObject).getUserName()) && getDesc()
						.equalsIgnoreCase(
								((User) anObject).getDesc()));
	}

	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + userName.hashCode();
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}	
	
}//class User
