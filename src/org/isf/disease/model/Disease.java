/**
 * @(#) Disease.java
 * 21-jan-2006
 */
package org.isf.disease.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.distype.model.DiseaseType;

/**
 * Pure Model Exam : represents a disease type
 * @author bob
 *
 *
 /*------------------------------------------
 * Bill - model for the bill entity
 * -----------------------------------------
 * modification history
 * ? - bob - first version 
 * 03/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="DISEASE")
public class Disease 
{
	@Id 
	@Column(name="DIS_ID_A")	    
    private String code;

	@NotNull
	@Column(name="DIS_DESC")
    private String description;

	@NotNull
	@ManyToOne
	@JoinColumn(name="DIS_DCL_ID_A")
	private DiseaseType diseaseType;	   			// values are 'N'(normal)  or 'M' (malnutrition)  default 'N' 

	@NotNull
	@Column(name="DIS_LOCK")
	private Integer lock;

	@NotNull
	@Column(name="DIS_OPD_INCLUDE")
	private boolean opdInclude;

	@NotNull
	@Column(name="DIS_IPD_IN_INCLUDE")
	private boolean ipdInInclude;

	@NotNull
	@Column(name="DIS_IPD_OUT_INCLUDE")
	private boolean ipdOutInclude;

	@Transient
	private volatile int hashCode = 0;

	public Disease() 
    {
		super();
    }
	
    /**
     * @param aCode
     * @param aDescription
     * @param aType
     * @param aLock
     */
    public Disease(String aCode, String aDescription, DiseaseType aType, Integer aLock) {
        super();
        this.code = aCode;
        this.description = aDescription;
        this.diseaseType = aType;
        this.lock = aLock;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String aCode) {
        this.code = aCode;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String aDescription) {
        this.description = aDescription;
    }
    
    public Integer getLock() {
        return this.lock;
    }    
    
    public void setLock(Integer aLock) {
        this.lock = aLock;
    }
    
    public DiseaseType getType() {
        return this.diseaseType;
    }
    
    public void setType(DiseaseType aType) {
        this.diseaseType = aType;
    }
    
    public boolean  getOpdInclude() {
        return this.opdInclude;
    }
    
    public void setOpdInclude(boolean opdInclude) {
        this.opdInclude = opdInclude;
    }
    
    public boolean getIpdInInclude() {
        return this.ipdInInclude;
    }
    
    public void setIpdInInclude(boolean ipdInclude) {
        this.ipdInInclude = ipdInclude;
    }
    
    public boolean getIpdOutInclude() {
		return ipdOutInclude;
	}
    
    public void setIpdOutInclude(boolean ipdOutInclude) {
		this.ipdOutInclude = ipdOutInclude;
	}

	@Override
	public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof Disease) ? false
                : (getCode().equals(((Disease) anObject).getCode())
                        && getDescription().equalsIgnoreCase(
                                ((Disease) anObject).getDescription()) && getType()
                        .equals(((Disease) anObject).getType()) && (getLock().equals(((Disease) anObject).getLock())));
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
