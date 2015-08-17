/**
 * @(#) ExamType.java
 * 20-jan-2006
 */
package org.isf.exatype.model;


/**
 * Pure Model ExamType (type of exams)
 * 
 * @author bob
 */
public class ExamType {

	private String code;

	private String description;
	

	public ExamType(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof ExamType) ? false
				: (getCode().equals(((ExamType) anObject).getCode())
						&& getDescription().equalsIgnoreCase(
								((ExamType) anObject).getDescription()));
	}

	public String toString() {
		return getDescription();
	}

}
