package org.isf.medicalstockward.model;

import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.isf.medicals.model.Medical;
import org.isf.patient.model.Patient;
import org.isf.ward.model.Ward;

/**
 * 		   @author mwithi
 * 
 */
/*------------------------------------------
 * Medical Ward Movement - model for the medical entity
 * -----------------------------------------
 * modification history
 * ? - ?
 * 17/01/2015 - Antonio - ported to JPA
 * 
 *------------------------------------------*/
@Entity
@Table(name="MEDICALDSRSTOCKMOVWARD")
public class MovementWard 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MMVN_ID")
	private int code;

	@NotNull
	@ManyToOne
	@JoinColumn(name="MMVN_WRD_ID_A")	
	private Ward ward;

	@NotNull
	@Column(name="MMVN_DATE")
	private GregorianCalendar date;

	@NotNull
	@Column(name="MMVN_IS_PATIENT")
	private boolean isPatient;
	
	@ManyToOne
	@JoinColumn(name="MMVN_PAT_ID")
	private Patient patient;
	
	@Column(name="MMVN_PAT_AGE")
	private int age;
	
	@Column(name="MMVN_PAT_WEIGHT")
	private float weight;

	@NotNull
	@Column(name="MMVN_DESC")
	private String description;

	@ManyToOne
	@JoinColumn(name="MMVN_MDSR_ID")
	private Medical medical;

	@NotNull
	@Column(name="MMVN_MDSR_QTY")
	private Double quantity;

	@NotNull
	@Column(name="MMVN_MDSR_UNITS")
	private String units;

	@Transient
	private volatile int hashCode = 0;
	
        @NotNull
	@ManyToOne
	@JoinColumn(name="MMVN_WRD_ID_A_TO")	
	private Ward wardTo;
	
	public MovementWard() {}
	
	/**
	 * 
	 * @param ward
	 * @param date
	 * @param isPatient
	 * @param patient
	 * @param age
	 * @param weight
	 * @param description
	 * @param medical
	 * @param quantity
	 * @param units
	 */
	public MovementWard(Ward ward, GregorianCalendar date, boolean isPatient,
			Patient patient, int age, float weight, String description, Medical medical,
			Double quantity, String units) {
		super();
		this.ward = ward;
		this.date = date;
		this.isPatient = isPatient;
		this.patient = patient;
		this.age = age;
		this.weight = weight;
		this.description = description;
		this.medical = medical;
		this.quantity = quantity;
		this.units = units;
	}

        /**
	 * 
	 * @param ward
	 * @param date
	 * @param isPatient
	 * @param patient
	 * @param age
	 * @param weight
	 * @param description
	 * @param medical
	 * @param quantity
	 * @param units
         * @param wardTo
	 */
	public MovementWard(Ward ward, GregorianCalendar date, boolean isPatient,
			Patient patient, int age, float weight, String description, Medical medical,
			Double quantity, String units, Ward wardTo) {
		super();
		this.ward = ward;
		this.date = date;
		this.isPatient = isPatient;
		this.patient = patient;
		this.age = age;
		this.weight = weight;
		this.description = description;
		this.medical = medical;
		this.quantity = quantity;
		this.units = units;
		this.wardTo = wardTo;
	}
        
	public int getCode(){
		return code;
	}
	
	public Medical getMedical() {		
		return medical;
	}
	
	public GregorianCalendar getDate(){
		return date;
	}
	
	public Double getQuantity(){
		return quantity;
	}
	
	public Ward getWard() {
		return ward;
	}

	public void setWard(Ward ward) {
		this.ward = ward;
	}

	public boolean isPatient() {
		return isPatient;
	}

	public void setPatient(boolean isPatient) {
		this.isPatient = isPatient;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public void setCode(int aCode){
		code=aCode;
	}
	
	public void setMedical(Medical aMedical){
		medical=aMedical;
	}
        
        public Ward getWardTo() {
            return wardTo;
        }

        public void setWardTo(Ward wardTo) {
            this.wardTo = wardTo;
        }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof MovementWard)) {
			return false;
		}
		
		MovementWard movment = (MovementWard)obj;
		return (this.getCode() == movment.getCode());
	}
	
	@Override
	public int hashCode() {
	    if (this.hashCode == 0) {
	        final int m = 23;
	        int c = 133;

	        c = m * c + code;
	        
	        this.hashCode = c;
	    }
	  
	    return this.hashCode;
	}	
}
