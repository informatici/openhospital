
package org.isf.dlvrtype.model;

/**
 * Pure Model Exam : represents a disease type
 * @author bob
 *
 */
public class DeliveryType {
    
    private String code;
    private String description;
    /**
     * @param aCode
     * @param aDescription
     */
    public DeliveryType(String aCode, String aDescription) {
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
        return (anObject == null) || !(anObject instanceof DeliveryType) ? false
                : (getCode().equals(((DeliveryType) anObject).getCode())
                        && getDescription().equalsIgnoreCase(
                                ((DeliveryType) anObject).getDescription()));
    }

    public String toString() {
        return getDescription();
    }

}
