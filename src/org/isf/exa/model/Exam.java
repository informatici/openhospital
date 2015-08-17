/**
 * @(#) Exam.java
 * 20-jan-2006
 */
package org.isf.exa.model;

import org.isf.exatype.model.ExamType;


/**
 * Pure Model Exam (laboratory exams): represents an exam
 * 
 * @author bob
 */
public class Exam {

	private String code;

	private String description;
	
	private Integer procedure;
	
	private String defaultResult;

	private ExamType examtype;

	private Integer lock;

	public Exam(String code, String description, ExamType examtype,
			Integer procedure, String defaultResult, Integer lock) {
		super();
		this.code = code;
		this.description = description;
		this.examtype = examtype;
		this.lock = lock;
		this.defaultResult = defaultResult;
		this.procedure = procedure;
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

	public ExamType getExamtype() {
		return examtype;
	}

	public void setExamtype(ExamType examtype) {
		this.examtype = examtype;
	}

	public Integer getLock() {
		return lock;
	}

	public void setLock(Integer lock) {
		this.lock = lock;
	}

	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof Exam) ? false
				: (getCode().equals(((Exam) anObject).getCode())
						&& getDescription().equalsIgnoreCase(
								((Exam) anObject).getDescription())
						&& getExamtype()
								.equals(((Exam) anObject).getExamtype()) && (getLock()
						.equals(((Exam) anObject).getLock())));
	}

	public String toString() {
		return getDescription();
	}

	public String getDefaultResult() {
		return defaultResult;
	}

	public void setDefaultResult(String defaultResult) {
		this.defaultResult = defaultResult;
	}

	public Integer getProcedure() {
		return procedure;
	}

	public void setProcedure(Integer procedure) {
		this.procedure = procedure;
	}
}
