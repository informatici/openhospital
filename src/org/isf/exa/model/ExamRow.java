package org.isf.exa.model;

public class ExamRow {

	private String description;
	
	private String code;
	
	private String examCode;
	
	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public ExamRow (String aCode, String aExamCode, String aDescription){
		code=aCode;
		description=aDescription;
		examCode=aExamCode;
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
	
	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof ExamRow) ? false
				: (getCode().equals(((ExamRow) anObject).getCode())
						&& getDescription().equalsIgnoreCase(
								((ExamRow) anObject).getDescription())
						&& getExamCode()
								.equals(((ExamRow) anObject).getExamCode()));
	}

	public String toString() {
		return getDescription();
	}
	
}
