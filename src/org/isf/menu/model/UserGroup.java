package org.isf.menu.model;

public class UserGroup {
	private String code;
	private String desc;
	
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
	
}
