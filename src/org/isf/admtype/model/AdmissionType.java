
package org.isf.admtype.model;

/**
 * Pure Model Exam : represents a disease type
 * @author bob
 *
 */
public class AdmissionType {
    
    private String code;
    private String description;
    /**
     * @param aCode
     * @param aDescription
     */
    public AdmissionType(String aCode, String aDescription) {
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
    public String toString() {
        return getDescription();
    }
	
	



}
