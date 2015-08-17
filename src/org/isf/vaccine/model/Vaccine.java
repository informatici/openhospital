/**
 * @(#) Vaccine.java
 */
package org.isf.vaccine.model;

  
import org.isf.vactype.model.VaccineType;


/**
 * Pure Model Vaccine (Hospital vaccines): represents a vaccine
 *
 * @author Eva
 *
 * modification history
 * 20/10/2011 - Cla - insert vaccinetype managment
 * 18/11/2011 - Cla - inserted print method
 *
 */
public class Vaccine {

    private String code;

    private String description;

    private VaccineType vaccineType;
    
    private Integer lock;

    /**
     * @param aCode
     * @param aDescription
     * @param avaccineType
     */
    public Vaccine(String aCode, String aDescription, VaccineType aVaccineType, Integer aLock) {
        super();
        this.code = aCode;
        this.description = aDescription;
        this.vaccineType = aVaccineType; 
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

    public VaccineType getVaccineType() {
        return this.vaccineType ;
    }

    public void setVaccineType(VaccineType aVaccineType) {
        this.vaccineType = aVaccineType;
    }
    
    
    public Integer getLock() {
        return this.lock;
    }

    public void setLock(Integer aLock) {
        this.lock = aLock;
    }

    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof Vaccine) ? false
                : (getCode().equals(((Vaccine) anObject).getCode())
                        && getDescription().equalsIgnoreCase(
                                ((Vaccine) anObject).getDescription())
                        && getVaccineType().equals(
                                ((Vaccine) anObject).getVaccineType()));
    }

    
    public String print() {
        return "Vaccine code =."+getCode()+". description =."+getDescription()+".";
    }
    
    public String toString (){
       return getDescription();
    }
}
