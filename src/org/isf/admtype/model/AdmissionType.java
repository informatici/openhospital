
package org.isf.admtype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Pure Model Exam : represents a disease type
 * @author bob
 *
 *
 */
/*------------------------------------------
 * Admission Type - model for the admission type entity
 * -----------------------------------------
 * modification history
 * ? - bob - first version 
 * 03/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="ADMISSIONTYPE")
public class AdmissionType 
{
	@Id 
	@Column(name="ADMT_ID_A")	
    private String code;
	
	@Column(name="ADMT_DESC")	
    private String description;

	@Transient
	private volatile int hashCode = 0;
	
	public AdmissionType() 
    {
		super();
    }
	 
    /**
     * @param aCode
     * @param aDescription
     */
    public AdmissionType(String aCode, String aDescription) 
    {
        this.code = aCode;
        this.description = aDescription;
    }
    
    public String getCode() 
    {
        return this.code;
    }
    
    public void setCode(String aCode) 
    {
        this.code = aCode;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String aDescription) {
        this.description = aDescription;
    }    
    
    public String toString() {
        return getDescription();
    }
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof AdmissionType)) {
			return false;
		}
		
		AdmissionType admissionType = (AdmissionType)obj;
		return (this.getCode().equals(admissionType.getCode()));
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
