package org.isf.operation.model;

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
public class Operation {
	 
    private String code;
    private String description;
    private OperationType type;
    private Integer major;
    private Integer lock;
    
    private volatile int hashCode = 0;
    
    /**
     * @param aCode
     * @param aDescription
     * @param aType
     * @param aLock
     */
    public Operation(String aCode, String aDescription, OperationType aType, Integer major, Integer aLock) {
        super();
        this.code = aCode;
        this.description = aDescription;
        this.type = aType;
        this.major = major;
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
				this.getMajor().equals(operation.getMajor()) &&
				this.getLock().equals(operation.getLock()));
    }
    
    @Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + ((code == null) ? 0 : code.hashCode());
	        c = m * c + ((description == null) ? 0 : description.hashCode());
	        c = m * c + ((type == null) ? 0 : type.hashCode());
	        c = m * c + ((major == null) ? 0 : major.intValue());
	        c = m * c + ((lock == null) ? 0 : lock.intValue());
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}

    public String toString() {
        return this.description;
    }
}

