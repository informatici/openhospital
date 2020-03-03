package org.isf.exa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/*------------------------------------------
* Disease Type - model for the disease type entity
* -----------------------------------------
* modification history
* 05/01/2016 - Antonio - ported to JPA
* 
*------------------------------------------*/
@Entity
@Table(name="EXAMROW")
public class ExamRow 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="EXR_ID")	
	private int code;

	@NotNull
	@Column(name="EXR_DESC")	
	private String description;

	@NotNull
	@ManyToOne
	@JoinColumn(name="EXR_EXA_ID_A")
	private Exam exam;

	@Transient
	private volatile int hashCode = 0;

	public ExamRow() 
    {
		super();
    }
	
	public ExamRow(Exam aExam, String aDescription){
		this.description=aDescription;
		this.exam=aExam;
	}

	public Exam getExamCode() {
		return exam;
	}

	public void setExamCode(Exam exam) {
		this.exam = exam;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
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
		return (anObject == null) || !(anObject instanceof ExamRow) ? false
				: (getCode() == ((ExamRow) anObject).getCode()
						&& getDescription().equalsIgnoreCase(
								((ExamRow) anObject).getDescription())
						&& getExamCode()
								.equals(((ExamRow) anObject).getExamCode()));
	}

	public String toString() {
		return getDescription();
	}	
    
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + code;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}		
}
