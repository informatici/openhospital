package org.isf.menu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/*------------------------------------------
 * User - model for the user entity
 * -----------------------------------------
 * modification history
 * ? - ? - first version 
 * 07/05/2016 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="USERGROUP")
public class UserGroup 
{
	@Id 
	@Column(name="UG_ID_A")
	private String code;
	
	@Column(name="UG_DESC")
	private String desc;
	
	@Transient
	private volatile int hashCode = 0;
	
	
	public UserGroup(String code, String desc){
		this.code=code;
		this.desc=desc;		
	}
	public UserGroup(){
		this("","");		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String toString(){
		return getCode();
	}
	
	@Override
	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof User) ? false
				: (getCode().equalsIgnoreCase(
						((User) anObject).getUserName()) && getDesc()
						.equalsIgnoreCase(
								((User) anObject).getDesc()));
	}

	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + code.hashCode();
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}		
}
