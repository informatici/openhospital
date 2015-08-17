package org.isf.opetype.model;

/**
 * Pure Model Exam : represents a disease type
 * 
 * @author Rick, Vero, Pupo
 *
 */
public class OperationType {
	 
    private String code;
    private String description;
    
    private volatile int hashCode = 0;
    
    /**
     * @param aCode
     * @param aDescription
     */
    public OperationType(String aCode, String aDescription) {
        super();
        this.code = aCode;
        this.description = aDescription;
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
    
    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
			return true;
		}
		
		if (!(anObject instanceof OperationType)) {
			return false;
		}
		
		OperationType operationType = (OperationType)anObject;
		return (this.getCode().equals(operationType.getCode()) &&
				this.getDescription().equalsIgnoreCase(operationType.getDescription()));
    }
    
    @Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + ((code == null) ? 0 : code.hashCode());
	        c = m * c + ((description == null) ? 0 : description.hashCode());
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}

    public String toString() {
        return this.description;
    }

}


