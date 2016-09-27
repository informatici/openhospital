/**
 * @(#) Ward.java
 * 21-jan-2006
 */
package org.isf.ward.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Pure Model Ward (Hospital wards): represents a ward
 * 
 * @author bob
 * 
 */
/*------------------------------------------
 * Bill - model for the bill entity
 * -----------------------------------------
 * modification history
 * ? - bob - first version 
 * 30/09/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="WARD")
public class Ward {
	@Id 
	@Column(name="WRD_ID_A")	
    private String code;

	@NotNull
	@Column(name="WRD_NAME")
    private String description;
	
	@Column(name="WRD_TELE")
    private String telephone;
	
	@Column(name="WRD_FAX")
    private String fax;
	
	@Column(name="WRD_EMAIL")
    private String email;

	@NotNull
	@Column(name="WRD_NBEDS")
    private Integer beds;

	@NotNull
	@Column(name="WRD_NQUA_NURS")
    private Integer nurs;

	@NotNull
	@Column(name="WRD_NDOC")
    private Integer docs;

	@NotNull
	@Column(name="WRD_IS_PHARMACY")    
    private boolean isPharmacy;

	@NotNull
	@Column(name="WRD_IS_MALE")   
    private boolean isMale;

	@NotNull
	@Column(name="WRD_IS_FEMALE")    
    private boolean isFemale;

	@NotNull
	@Column(name="WRD_LOCK")
    private Integer lock;
    
	@Transient
	private volatile int hashCode = 0;
	
	public Ward() {
		super();
	}
	
    /**
     * @param aCode
     * @param aName
     * @param aTelephone
     * @param aFax
     * @param aEmail
     * @param aBeds
     * @param aNurs
     * @param aDocs
     * @param isPharmacy
     * @param isMale
     * @param isFemale
     */
    public Ward(String code, String description, String telephone, String fax,
			String email, Integer beds, Integer nurs, Integer docs,
			boolean isPharmacy, boolean isMale, boolean isFemale, Integer lock) 
    {
		super();
		this.code = code;
		this.description = description;
		this.telephone = telephone;
		this.fax = fax;
		this.email = email;
		this.beds = beds;
		this.nurs = nurs;
		this.docs = docs;
		this.isPharmacy = isPharmacy;
		this.isMale = isMale;
		this.isFemale = isFemale;
		this.lock = lock;
	}
    
    public Ward(String code, String description, String telephone, String fax,
			String email, Integer beds, Integer nurs, Integer docs, boolean isMale, boolean isFemale,
			Integer lock) 
    {
		super();
		this.code = code;
		this.description = description;
		this.telephone = telephone;
		this.fax = fax;
		this.email = email;
		this.beds = beds;
		this.nurs = nurs;
		this.docs = docs;
		this.isPharmacy = false;
		this.isMale = isMale;
		this.isFemale = isFemale;
		this.lock = lock;
	}

    public Integer getBeds() {
        return this.beds;
    }

	public void setBeds(Integer aBeds) {
        this.beds = aBeds;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String aCode) {
        this.code = aCode;
    }

    public Integer getDocs() {
        return this.docs;
    }

    public void setDocs(Integer aDocs) {
        this.docs = aDocs;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String aDescription) {
        this.description = aDescription;
    }

    public Integer getNurs() {
        return this.nurs;
    }

    public void setNurs(Integer aNurs) {
        this.nurs = aNurs;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String aTelephone) {
        this.telephone = aTelephone;
    }

    public Integer getLock() {
        return this.lock;
    }

    public void setLock(Integer aLock) {
        this.lock = aLock;
    }

    public boolean isPharmacy() {
		return isPharmacy;
	}

	public void setPharmacy(boolean isPharmacy) {
		this.isPharmacy = isPharmacy;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public boolean isFemale() {
		return isFemale;
	}

	public void setFemale(boolean isFemale) {
		this.isFemale = isFemale;
	}

	@Override
	public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof Ward) ? false
                : (getCode().equals(((Ward) anObject).getCode())
                        && getDescription().equalsIgnoreCase(
                                ((Ward) anObject).getDescription())
                        && getTelephone().equalsIgnoreCase(
                                ((Ward) anObject).getTelephone()) && (getFax()
                        .equalsIgnoreCase(((Ward) anObject).getFax()) && (getEmail()
                        .equalsIgnoreCase(((Ward) anObject).getEmail()) && (getBeds()
                        .equals(((Ward) anObject).getBeds()) && (getNurs()
                        .equals(((Ward) anObject).getNurs()) && (getDocs()
                        .equals(((Ward) anObject).getDocs()) && (getLock()
                        .equals(((Ward) anObject).getLock()))))))));
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
