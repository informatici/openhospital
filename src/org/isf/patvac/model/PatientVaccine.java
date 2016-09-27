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
	private Patient patId;

	@NotNull
	@ManyToOne
	@JoinColumn(name="PAV_VAC_ID_A")
	private Vaccine vaccine;
	
	@Column(name="PAV_LOCK")
	private int lock;

	
	public PatientVaccine()
	{		
	}
	
	public PatientVaccine(int codeIn, int progIn, GregorianCalendar vacDateIn, 
			Patient patIdIn, Vaccine vacIn, int lockIn) {
		code = codeIn;
		progr = progIn;
		vaccineDate = vacDateIn;
		patId = patIdIn;
		vaccine = vacIn;
		lock = lockIn ;
	}
	
	public PatientVaccine(int codeIn, int progIn, GregorianCalendar vacDateIn, 
			Patient patIdIn, Vaccine vacIn, int lockIn,
                          String patNameIn, int patAgeIn, char patSexIn) {
		code = codeIn;
		progr = progIn;
		vaccineDate = vacDateIn;
		patId = patIdIn;
		vaccine = vacIn;
		lock = lockIn ;
		patId.setFirstName(patNameIn);
		patId.setAge(patAgeIn);
		patId.setSex(patSexIn);
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

	public Patient getPatId() {
		return patId;
	}

	public void setPatId(Patient patId) {
		this.patId = patId;
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
		return patId.getFirstName();
	}

	public void setPatName(String patName) {
		this.patId.setFirstName(patName);
	}

	public int getPatAge() {
		return patId.getAge();
	}

	public void setPatAge(int patAge) {
		this.patId.setAge(patAge);
	}

	public char getPatSex() {
		return patId.getSex();
	}

	public void setPatSex(char patSex) {
		this.patId.setSex(patSex);
	}


	/*
	public void stampa(String cntx) {
		System.out.println("Inizio contesto ====>"+cntx);
		System.out.println("Codice="+code);
		System.out.println("Paziente="+patId);
		System.out.println("vaccino="+vaccine.getCode()+"-"+vaccine.getDescription());
		System.out.println("vaccine.pati.code="+vaccine.getPati().getCode());
		System.out.println("vaccine.pati.descr="+vaccine.getPati().getDescription());
		System.out.println("progressivo="+progr);
		System.out.println("nome="+patName);
		System.out.println("sesso="+patSex);
		System.out.println("age="+patAge);
		System.out.println("Fine contesto ====>"+cntx);
		
	}
	*/
	
	
	public boolean equals(Object other){
		if((other==null)|| (!(other instanceof PatientVaccine))) return false;
		else if((getPatId() == ((PatientVaccine)other).getPatId())
		  		 && getVaccine().equals(((PatientVaccine)other).getVaccine())
		  		 && getVaccineDate().equals(((PatientVaccine)other).getVaccineDate()))
			      return true;
			 else return false;
	}

}
