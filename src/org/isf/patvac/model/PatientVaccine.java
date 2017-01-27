package org.isf.patvac.model;

/*------------------------------------------
* PatientVaccine - class 
* -----------------------------------------
* modification history
* 25/08/2011 - claudia - first beta version
* 04/06/2015 - Antonio - ported to JPA
*------------------------------------------*/

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

import org.isf.patient.model.Patient;
import org.isf.vaccine.model.Vaccine;

@Entity
@Table(name="PATIENTVACCINE")
public class PatientVaccine 
{
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PAV_ID")
	private int code;

	@NotNull
	@Column(name="PAV_YPROG")
	private int progr;

	@NotNull
	@Column(name="PAV_DATE")
	private GregorianCalendar vaccineDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name="PAV_PAT_ID")
	private Patient patient;

	@NotNull
	@ManyToOne
	@JoinColumn(name="PAV_VAC_ID_A")
	private Vaccine vaccine;
	
	@Column(name="PAV_LOCK")
	private int lock;
	
	@Transient
	private volatile int hashCode = 0;

	
	public PatientVaccine()
	{		
	}
	
	public PatientVaccine(int codeIn, int progIn, GregorianCalendar vacDateIn, 
			Patient patient, Vaccine vacIn, int lockIn) {
		this.code = codeIn;
		this.progr = progIn;
		this.vaccineDate = vacDateIn;
		this.patient = patient;
		this.vaccine = vacIn;
		this.lock = lockIn ;
	}
	
	public PatientVaccine(int codeIn, int progIn, GregorianCalendar vacDateIn, 
			Patient patient, Vaccine vacIn, int lockIn,
                          String patNameIn, int patAgeIn, char patSexIn) {
		this.code = codeIn;
		this.progr = progIn;
		this.vaccineDate = vacDateIn;
		this.patient = patient;
		this.vaccine = vacIn;
		this.lock = lockIn ;
		patient.setFirstName(patNameIn);
		patient.setAge(patAgeIn);
		patient.setSex(patSexIn);
	}
	
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getProgr() {
		return progr;
	}

	public void setProgr(int progr) {
		this.progr = progr;
	}

	public GregorianCalendar getVaccineDate() {
		return vaccineDate;
	}

	public void setVaccineDate(GregorianCalendar vaccineDate) {
		this.vaccineDate = vaccineDate;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}
	

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public String getPatName() {
		return patient.getFirstName();
	}

	public void setPatName(String patName) {
		this.patient.setFirstName(patName);
	}

	public int getPatAge() {
		return patient.getAge();
	}

	public void setPatAge(int patAge) {
		this.patient.setAge(patAge);
	}

	public char getPatSex() {
		return patient.getSex();
	}

	public void setPatSex(char patSex) {
		this.patient.setSex(patSex);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int hascode = 1;
		hascode = prime * hascode + code;
		hascode = prime * hascode + ((patient == null) ? 0 : patient.hashCode());
		hascode = prime * hascode + progr;
		hascode = prime * hascode + ((vaccine == null) ? 0 : vaccine.hashCode());
		hascode = prime * hascode
				+ ((vaccineDate == null) ? 0 : vaccineDate.hashCode());
		return hascode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PatientVaccine)) {
			return false;
		}
		PatientVaccine other = (PatientVaccine) obj;
		if (code != other.code) {
			return false;
		}
		if (patient == null) {
			if (other.patient != null) {
				return false;
			}
		} else if (!patient.equals(other.patient)) {
			return false;
		}
		if (progr != other.progr) {
			return false;
		}
		if (vaccine == null) {
			if (other.vaccine != null) {
				return false;
			}
		} else if (!vaccine.equals(other.vaccine)) {
			return false;
		}
		if (vaccineDate == null) {
			if (other.vaccineDate != null) {
				return false;
			}
		} else if (!vaccineDate.equals(other.vaccineDate)) {
			return false;
		}
		return true;
	}

}
