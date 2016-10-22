/**
 * @(#) Hospital.java
 * 21-jan-2006
 */
package org.isf.hospital.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


/**
 * Pure Model Hospital : represents the Hospital 
 * @author bob
 *
 */
/*------------------------------------------
* Disease Type - model for the disease type entity
* -----------------------------------------
* modification history
* ? - bob - first version 
* 06/01/2016 - Antonio - ported to JPA
* 
*------------------------------------------*/
@Entity
@Table(name="HOSPITAL")
public class Hospital 
{
	@Id 
	@Column(name="HOS_ID_A")
    private String code;

	@NotNull
	@Column(name="HOS_NAME")	
    private String description;

	@NotNull
	@Column(name="HOS_ADDR")
    private String address;

	@NotNull
	@Column(name="HOS_CITY")
    private String city;
	
	@Column(name="HOS_TELE")
    private String telephone;
	
	@Column(name="HOS_FAX")
    private String fax;
	
	@Column(name="HOS_EMAIL")
    private String email;

	@Column(name="HOS_CURR_COD")
    private String currencyCod;

	@NotNull
	@Column(name="HOS_LOCK")
    private Integer lock;

	@Transient
	private volatile int hashCode = 0;
	
    /**
     * @param aCode
     * @param aDescription
     * @param aAddress
     * @param aCity
     * @param aTelephone
     * @param aFax
     * @param aEmail
     * @param aCurrencyCod
     * @param aLock
     */
    public Hospital(){
    	super();
        this.code = null;
        this.description = null;
        this.address = null;
        this.city = null;
        this.telephone = null;
        this.fax = null;
        this.email = null;
        this.currencyCod = null;
        this.lock = null;
    }
    
    public Hospital(String aCode, String aDescription, String aAddress, 
    		String aCity, String aTelephone, String aFax, 
    		String aEmail, String aCurrencyCod, Integer aLock) {
        super();
        this.code = aCode;
        this.description = aDescription;
        this.address = aAddress;
        this.city = aCity;
        this.telephone = aTelephone;
        this.fax = aFax;
        this.email = aEmail;
        this.currencyCod = aCurrencyCod;
        this.lock = aLock;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String aAddress) {
        this.address = aAddress;
    }
    
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String aCity) {
        this.city = aCity;
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
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String aEmail) {
        this.email = aEmail;
    }
    
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(String aFax) {
        this.fax = aFax;
    }
    
    public Integer getLock() {
        return this.lock;
    }
    
    public void setLock(Integer aLock) {
        this.lock = aLock;
    }
    
    public String getTelephone() {
        return this.telephone;
    }
    
    public void setTelephone(String aTelephone) {
        this.telephone = aTelephone;
    }
    
    public String getCurrencyCod() {
        return this.currencyCod;
    }
    
    public void setCurrencyCod(String aCurrencyCod) {
        this.currencyCod = aCurrencyCod;
    }

	@Override
    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof Hospital) ? false
                : (getCode().equals(((Hospital) anObject).getCode())
                        && getDescription().equalsIgnoreCase(((Hospital) anObject).getDescription())
                        && getTelephone().equalsIgnoreCase(((Hospital) anObject).getTelephone()) && (getFax()
                        .equalsIgnoreCase(((Hospital) anObject).getFax()) 
                        && (getAddress().equalsIgnoreCase(((Hospital) anObject).getAddress()) 
                        && (getCity().equalsIgnoreCase(((Hospital) anObject).getCity())
                        && (getEmail().equalsIgnoreCase(((Hospital) anObject).getEmail())
                        && (getCurrencyCod() != null && ((Hospital) anObject).getCurrencyCod() != null && getCurrencyCod().equals(((Hospital) anObject).getCurrencyCod()))
                        && (getLock().equals(((Hospital) anObject).getLock())))))));
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
