package org.isf.operation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.isf.opetype.model.OperationType;

/*----------------------------------------------------------
 * modification history
 * ====================
 * 13/02/09 - Alex - added Major/Minor control
 -----------------------------------------------------------*/

/**
 * Pure Model Operation : represents a disease
 * 
 * @author Rick, Vero, Pupo
 */
/*------------------------------------------
* Operation - model for the bill entity
* -----------------------------------------
* modification history
* ? - bob - first version 
* 007/01/2015 - Antonio - ported to JPA
* 
*------------------------------------------*/
@Entity
@Table(name="OPERATION")
public class Operation 
{
	@Id 
	@Column(name="OPE_ID_A")	    
    private String code;

	@NotNull
	@Column(name="OPE_DESC")
    private String description;

	@NotNull
	@ManyToOne
	@JoinColumn(name="OPE_OCL_ID_A")
    private OperationType type;

	@NotNull
	@Column(name="OPE_STAT")
    private Integer major;

	@Version
	@Column(name="OPE_LOCK")
    private Integer lock;

	@Transient
    private volatile int hashCode = 0;
    
    
	public Operation() 
    {
		super();
    }
	
    /**
     * @param aCode
     * @param aDescription
     * @param aType
     */
    public Operation(String aCode, String aDescription, OperationType aType, Integer major) {
        super();
        this.code = aCode;
        this.description = aDescription;
        this.type = aType;
        this.major = major;
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
    
    public void setMajor(Integer major) {
		this.major = major;
	}
    
	public Integer getMajor() {
		return major;
	}
	
	public Integer getLock() {
        return this.lock;
    }
	
    public void setLock(Integer aLock) {
        this.lock = aLock;
    }
    
    public OperationType getType() {
        return this.type;
    }
    
    public void setType(OperationType aType) {
        this.type = aType;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
			return true;
		}
		
		if (!(anObject instanceof Operation)) {
			return false;
		}
		
		Operation operation = (Operation)anObject;
		return (this.getCode().equals(operation.getCode()) &&
				this.getDescription().equalsIgnoreCase(operation.getDescription()) &&
				this.getType().equals(operation.getType()) &&
				this.getMajor().equals(operation.getMajor()));
    }
    
    @Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + ((code == null) ? 0 : code.hashCode());
	        c = m * c + ((description == null) ? 0 : description.hashCode());
	        c = m * c + ((type == null) ? 0 : type.hashCode());
	        c = m * c + ((major == null) ? 0 : major);
	        c = m * c + ((lock == null) ? 0 : lock);
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}

    public String toString() {
        return this.description;
    }
}

