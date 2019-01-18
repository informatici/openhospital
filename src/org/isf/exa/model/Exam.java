/**
 * @(#) Exam.java
 * 20-jan-2006
 */
package org.isf.exa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.isf.exatype.model.ExamType;


/**
 * Pure Model Exam (laboratory exams): represents an exam
 * 
 * @author bob
 */
/*------------------------------------------
* Disease Type - model for the disease type entity
* -----------------------------------------
* modification history
* ? - bob - first version 
* 05/01/2016 - Antonio - ported to JPA
* 
*------------------------------------------*/
@Entity
@Table(name="EXAM")
public class Exam 
{
	@Id 
	@Column(name="EXA_ID_A")	
	private String code;

	@NotNull
	@Column(name="EXA_DESC")
	private String description;

	@NotNull
	@Column(name="EXA_PROC")
	private Integer procedure;

	@Column(name="EXA_DEFAULT")
	private String defaultResult;

	@NotNull
	@ManyToOne
	@JoinColumn(name="EXA_EXC_ID_A")
	private ExamType examtype;

	@Version
	@Column(name="EXA_LOCK")
	private Integer lock;

	@Transient
	private volatile int hashCode = 0;
	
	public Exam() 
    {
		super();
    }
	
	public Exam(String code, String description, ExamType examtype,
			Integer procedure, String defaultResult) {
		super();
		this.code = code;
		this.description = description;
		this.examtype = examtype;
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

	@Override
	public boolean equals(Object anObject) {
		return (anObject == null) || !(anObject instanceof Exam) ? false
				: (getCode().equals(((Exam) anObject).getCode())
						&& getDescription().equalsIgnoreCase(
								((Exam) anObject).getDescription())
						&& getExamtype()
								.equals(((Exam) anObject).getExamtype()));
	}

	public String toString() {
		return getDescription();
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
