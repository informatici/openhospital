
package org.isf.medstockmovtype.model;

/**
 * Represent a movement type.
 */
public class MovementType {
    
    private String code;
    private String description;
    private String type;
    
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
}