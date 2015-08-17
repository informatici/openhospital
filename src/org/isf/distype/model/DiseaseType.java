/**
 * @(#) DiseaseType.java
 * 21-jan-2006
 */
package org.isf.distype.model;

/**
 * Pure Model Exam : represents a disease type
 * @author bob
 */
public class DiseaseType {
    
    private String code;
    private String description;
    /**
     * @param aCode
     * @param aDescription
     */
    public DiseaseType(String aCode, String aDescription) {
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
    
    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof DiseaseType) ? false
                : (getCode().equals(((DiseaseType) anObject).getCode())
                        && getDescription().equalsIgnoreCase(
                                ((DiseaseType) anObject).getDescription()));
    }

    public String toString() {
        return getDescription();
    }

}
