
package org.isf.medstockmovtype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


/**
 * Represent a movement type.
 */
/*------------------------------------------
 * Movement Medical - model for the movement entity
 * -----------------------------------------
 * modification history
 * ? - bob - first version 
 * 18/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="MEDICALDSRSTOCKMOVTYPE")
public class MovementType 
{
	@Id 
	@Column(name="MMVT_ID_A")	   
    private String code;

	@NotNull
	@Column(name="MMVT_DESC")	
    private String description;

	@NotNull
	@Column(name="MMVT_TYPE")	
    private String type;

	@Transient
	private volatile int hashCode = 0;
	
    public MovementType(){}
    
    /**
     * @param code
     * @param description
     * @param type
     */
    public MovementType(String code, String description, String type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
    public String toString() {
        return getDescription();
    }
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof MovementType)) {
			return false;
		}
		
		MovementType movementType = (MovementType)obj;
		return (this.getCode().equals(movementType.getCode()));
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