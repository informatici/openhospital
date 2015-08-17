package org.isf.lab.model;

public class LaboratoryRow {

	private Integer code;
	private Integer labId;
	private String description;
	
	public LaboratoryRow(Integer aCode, Integer aLabId, String aDescription){
		code=aCode;
		labId = aLabId;
		description = aDescription;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getLabId() {
		return labId;
	}
	public void setLabId(Integer labId) {
		this.labId = labId;
	}
	
	
}
