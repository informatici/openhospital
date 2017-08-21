package org.isf.utils.exception.model;

import java.io.Serializable;

/**
 * Composed exception information
 * @author akashytsa
 *
 */
public class OHExceptionMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String message;
	private OHSeverityLevel level;

	public OHExceptionMessage(String title, String message, OHSeverityLevel level) {
		super();
		this.title = title;
		this.message = message;
		this.level = level;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public OHSeverityLevel getLevel() {
		return level;
	}
	public void setLevel(OHSeverityLevel level) {
		this.level = level;
	}
	

}
