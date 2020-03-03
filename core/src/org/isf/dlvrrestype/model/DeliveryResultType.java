
package org.isf.dlvrrestype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Pure Model Exam : represents a disease type
 * @author bob
 *
 */
/*------------------------------------------
* Delivery Type - model for the delivery type entity
* -----------------------------------------
* modification history
* ? - bob - first version 
* 11/01/2015 - Antonio - ported to JPA
* 
*------------------------------------------*/
@Entity
@Table(name="DELIVERYRESULTTYPE")
public class DeliveryResultType 
{
	@Id 
	@Column(name="DRT_ID_A")	    
    private String code;

	@NotNull
	@Column(name="DRT_DESC")
    private String description;
	
	@Transient
	private volatile int hashCode = 0;
	
	public DeliveryResultType() 
    {
		super();
    }
	
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

	@Override
    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof DeliveryResultType) ? false
                : (getCode().equals(((DeliveryResultType) anObject).getCode())
                        && getDescription().equalsIgnoreCase(
                                ((DeliveryResultType) anObject).getDescription()));
    }

    public String toString() {
        return getDescription();
    }

	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;
	        
	        c = m * c + code.hashCode();
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}
}
