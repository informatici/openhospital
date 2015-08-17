
package org.isf.dlvrrestype.model;

/**
 * Pure Model Exam : represents a disease type
 * @author bob
 *
 */
public class DeliveryResultType {
    
    private String code;
    private String description;
    /**
     * @param aCode
     * @param aDescription
     */
    public DeliveryResultType(String aCode, String aDescription) {
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
        return (anObject == null) || !(anObject instanceof DeliveryResultType) ? false
                : (getCode().equals(((DeliveryResultType) anObject).getCode())
                        && getDescription().equalsIgnoreCase(
                                ((DeliveryResultType) anObject).getDescription()));
    }

    public String toString() {
        return getDescription();
    }

}
