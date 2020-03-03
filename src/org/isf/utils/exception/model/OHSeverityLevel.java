package org.isf.utils.exception.model;

public enum OHSeverityLevel {
	
	ERROR(0),
	INFO(1),
	WARNING(2),
	QUESTION(3);
	
	OHSeverityLevel(int swingSeverity){
		this.swingSeverity = swingSeverity;
	}
	
	public int getSwingSeverity() {
		return swingSeverity;
	}

	private int swingSeverity;

}
