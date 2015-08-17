/**
 * @(#) Ward.java
 * 21-jan-2006
 */
package org.isf.ward.model;

/**
 * Pure Model Ward (Hospital wards): represents a ward
 * 
 * @author bob
 * 
 */
public class Ward {

    private String code;

    private String description;

    private String telephone;

    private String fax;

    private String email;

    private Integer beds;

    private Integer nurs;

    private Integer docs;
    
    private boolean isPharmacy;
    
    private boolean isMale;
    
    private boolean isFemale;

    private Integer lock;

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
			boolean isPharmacy, boolean isMale, boolean isFemale, Integer lock) {
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
			Integer lock) {
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
}
