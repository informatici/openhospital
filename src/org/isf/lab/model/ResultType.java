package org.isf.lab.model;

public class ResultType {
	
	private String id;
	
	private String description;
	
	public ResultType(String aId, String aDescription){
		id = aId;
		description = aDescription;
	}
	
	public String getId(){
		return id;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setId(String aId){
		id = aId;
	}
	
	public void setDescription(String aDescription){
		description = aDescription;
	}

}
