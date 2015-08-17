/**
 * @(#) Disease.java
 * 21-jan-2006
 */
package org.isf.disease.model;

import org.isf.distype.model.DiseaseType;

/**
 * Pure Model Disease : represents a disease
 * @author bob
 */
public class Disease {
    
    private String code;
    private String description;
    private DiseaseType type;
    private Integer lock;
    private boolean opdInclude;
    private boolean ipdInInclude;
    private boolean ipdOutInclude;
    
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
        this.type = aType;
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
        return this.type;
    }
    public void setType(DiseaseType aType) {
        this.type = aType;
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
}
