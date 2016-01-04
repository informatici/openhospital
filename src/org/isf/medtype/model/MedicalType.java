/**
 * @(#) TipoFarmaco.java
 * 11-dec-2005
 * 14-jan-2006
 */

package org.isf.medtype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Defines a medical type: D: k: S: R:
 * @author  bob
 */
/*------------------------------------------
 * Medical Type - model for the medival type entity
 * -----------------------------------------
 * modification history
 * ? - bob - first version 
 * 18/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="MEDICALDSRTYPE")
public class MedicalType 
{
	@Id 
	@Column(name="MDSRT_ID_A")	
	private String code;

	@Column(name="MDSRT_DESC")	
	private String description;

	@Transient
	private volatile int hashCode = 0;
	
	public MedicalType() 
    {
		super();
    }
	
	public MedicalType(String code, String description) {
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
		return (anObject == null) || !(anObject instanceof MedicalType) ? false
				: (getCode().equalsIgnoreCase(
						((MedicalType) anObject).getCode()) && getDescription()
						.equalsIgnoreCase(
								((MedicalType) anObject).getDescription()));
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
