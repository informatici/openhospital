/**
 * @(#) ExamType.java
 * 20-jan-2006
 */
package org.isf.exatype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Pure Model ExamType (type of exams)
 * 
 * @author bob
 */
/*------------------------------------------
 * AgeType - model for the age type entity
 * -----------------------------------------
 * modification history
 * ? - bob - first version 
 * 18/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="EXAMTYPE")
public class ExamType 
{
	@Id 
	@Column(name="EXC_ID_A") 
	private String code;

	@Column(name="EXC_DESC")	
	private String description;

	@Transient
	private volatile int hashCode = 0;

	public ExamType() 
    {
		super();
    }
	
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
