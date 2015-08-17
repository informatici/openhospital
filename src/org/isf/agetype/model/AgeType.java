
package org.isf.agetype.model;

/**
 * Age Model: represent age's ranges
 * @author alex
 *
 */
public class AgeType {
    
    private String code;
    private String description;
    private int from;
    private int to;
    /**
     * @param aCode
     * @param aDescription
     */
    public AgeType(String aCode, String aDescription) {
        super();
        this.code = aCode;
        this.description = aDescription;
    }
    public AgeType(String aCode, int from, int to, String aDescription) {
        super();
        this.code = aCode;
        this.from = from;
        this.to = to;
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
    public void setFrom(int from) {
		this.from = from;
	}
	public int getFrom() {
		return from;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public int getTo() {
		return to;
	}
	public String toString() {
        return getDescription();
    }
	
}
